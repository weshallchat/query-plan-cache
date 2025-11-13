
import java.util.*;
import java.util.concurrent.*;
import java.security.MessageDigest;
import java.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
public class QueryPlanCacheManager {
    
    private final ConcurrentHashMap<String, CachedPlan> planCache;
    private final QueryNormalizer normalizer;
    private final PlanGenerator planGenerator;
    private final CacheStatistics statistics;
    private final SchemaVersionTracker schemaTracker;
    private final int maxCacheSize;
    private final long ttlMillis;
    
    // Per-key locks to prevent duplicate plan generation
    private final ConcurrentHashMap<String, Object> generationLocks;
    // Cache to store the cache key for a query
    private final ConcurrentHashMap<String, String> queryToKeyCache;
    
    public QueryPlanCacheManager(int maxCacheSize, long ttlMillis) {
        this.planCache = new ConcurrentHashMap<>();
        this.normalizer = new QueryNormalizer();
        this.planGenerator = new PlanGenerator();
        this.statistics = new CacheStatistics();
        this.schemaTracker = new SchemaVersionTracker();
        this.maxCacheSize = maxCacheSize;
        this.ttlMillis = ttlMillis;
        this.generationLocks = new ConcurrentHashMap<>();
        this.queryToKeyCache = new ConcurrentHashMap<>();
    }
    
    /**
     * Main entry point: Get execution plan for a query
     */
    public ExecutionPlan getExecutionPlan(String sqlQuery) {
        // statistics.recordRequest();
    
        // FAST PATH: Check if we've seen this exact query before
        String cachedKey = queryToKeyCache.get(sqlQuery);
        if (cachedKey != null) {
            CachedPlan cached = planCache.get(cachedKey);
            if (cached != null && isValid(cached)) {
                // Super-fast cache hit - no parsing needed!
                statistics.recordHit();
                cached.recordAccess();
                return bindParameters(cached.getPlan(), cached.getParameterMetadata());
            } else {
                // Cached key is stale, remove it
                queryToKeyCache.remove(sqlQuery);
            }
        }
        
        // SLOW PATH: Parse and normalize the query
        NormalizedQuery normalized = normalizer.normalize(sqlQuery);
        String cacheKey = generateCacheKey(normalized.getPattern());
        
        // Try cache lookup with normalized key
        CachedPlan cached = planCache.get(cacheKey);
        
        if (cached != null && isValid(cached)) {
            // Cache HIT (after normalization)
            statistics.recordHit();
            cached.recordAccess();
            
            // Cache the raw query → key mapping for next time
            queryToKeyCache.put(sqlQuery, cacheKey);
            
            // Bind parameters and return
            return bindParameters(cached.getPlan(), normalized.getParameters());
        }
        
        // Cache MISS - need to generate plan
        statistics.recordMiss();
        ExecutionPlan plan = generateAndCachePlan(cacheKey, normalized);
        
        // Cache the raw query → key mapping for next time
        queryToKeyCache.put(sqlQuery, cacheKey);
        
        return plan;
    }
    
    /**
     * Generate plan with double-checked locking to avoid duplicate work
     */
    private ExecutionPlan generateAndCachePlan(String cacheKey, NormalizedQuery normalized) {
        // Get or create a lock object for this specific key
        Object lock = generationLocks.computeIfAbsent(cacheKey, k -> new Object());
        
        synchronized (lock) {
            // Double-check: another thread may have generated the plan
            CachedPlan cached = planCache.get(cacheKey);
            if (cached != null && isValid(cached)) {
                statistics.recordHit(); // Actually a hit on retry
                return bindParameters(cached.getPlan(), normalized.getParameters());
            }
            
            // Generate new plan
            JsonNode planJson = planGenerator.generate(normalized.getPattern());
            
            // Create cached plan with metadata
            CachedPlan cachedPlan = new CachedPlan(
                planJson,
                normalized.getParameterMetadata(),
                schemaTracker.getCurrentVersion(),
                System.currentTimeMillis()
            );
            
            // Store in cache (with eviction if needed)
            storePlan(cacheKey, cachedPlan, normalized);
            
            // Return bound plan
            return bindParameters(planJson, normalized.getParameters());
        }
    }
    
    /**
     * Store plan in cache with eviction policy
     */
    private void storePlan(String cacheKey, CachedPlan plan, NormalizedQuery normalized) {
        // Check if we need to evict
        if (planCache.size() >= maxCacheSize) {
            evictLRU();
        }
        
        planCache.put(cacheKey, plan);
        
        // Track table dependencies for targeted invalidation
        schemaTracker.recordDependencies(cacheKey, normalized.getReferencedTables());
    }
    
    /**
     * Validate cached plan (check TTL and schema version)
     */
    private boolean isValid(CachedPlan cached) {
        // Check TTL
        long age = System.currentTimeMillis() - cached.getCreationTime();
        if (age > ttlMillis) {
            return false;
        }
        
        // Check schema version
        if (!cached.getSchemaVersion().equals(schemaTracker.getCurrentVersion())) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Evict least recently used entries
     */
    private void evictLRU() {
        CachedPlan oldest = null;
        String oldestKey = null;
        
        for (Map.Entry<String, CachedPlan> entry : planCache.entrySet()) {
            if (oldest == null || entry.getValue().getLastAccessTime() < oldest.getLastAccessTime()) {
                oldest = entry.getValue();
                oldestKey = entry.getKey();
            }
        }
        
        if (oldestKey != null) {
            planCache.remove(oldestKey);
            statistics.recordEviction();
        }
    }
    
    /**
     * Invalidate cache on schema change
     */
    public void onSchemaChange(String tableName) {
        Set<String> affectedKeys = schemaTracker.getAffectedPlans(tableName);
        
        for (String key : affectedKeys) {
            planCache.remove(key);
            statistics.recordInvalidation();
        }
        // Clear query-to-key cache for affected plans
        queryToKeyCache.entrySet().removeIf(entry -> affectedKeys.contains(entry.getValue()));
    
        // Update schema version
        schemaTracker.incrementVersion();
    }
    
    /**
     * Clear entire cache
     */
    public void clearCache() {
        planCache.clear();
        queryToKeyCache.clear(); 
        statistics.recordFullClear();
    }
    
    /**
     * Bind parameters to execution plan
     */
    private ExecutionPlan bindParameters(JsonNode planJson, Object params) {
        if (params instanceof List) {
            List<?> paramList = (List<?>) params;

            if (!paramList.isEmpty() && paramList.get(0) instanceof ParameterMetadata) {
                return new ExecutionPlan(planJson, Collections.emptyList());
            } else {
                return new ExecutionPlan(planJson, (List<Object>) paramList);
            }
        }
        return new ExecutionPlan(planJson, Collections.emptyList()); 
    }
    
    /**
     * Generate cache key from normalized pattern
     */
    private String generateCacheKey(String normalizedPattern) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(normalizedPattern.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return String.valueOf(normalizedPattern.hashCode());
        }
    }
    
    public CacheStatistics getStatistics() {
        return statistics;
    }
    
    /**
     * Get current cache size
     */
    public int getCacheSize() {
        return planCache.size();
    }
}