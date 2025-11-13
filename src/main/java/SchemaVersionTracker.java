
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SchemaVersionTracker {
    private String currentVersion;
    private final Map<String, Set<String>> tableToPlanKeys;
    
    public SchemaVersionTracker() {
        this.currentVersion = "v1";
        this.tableToPlanKeys = new ConcurrentHashMap<>();
    }
    
    public String getCurrentVersion() {
        return currentVersion;
    }
    
    public void incrementVersion() {
        // Simple versioning: v1, v2, v3, ...
        int versionNum = Integer.parseInt(currentVersion.substring(1));
        currentVersion = "v" + (versionNum + 1);
    }
    
    public void recordDependencies(String cacheKey, Set<String> tables) {
        for (String table : tables) {
            tableToPlanKeys.computeIfAbsent(table, k -> ConcurrentHashMap.newKeySet())
                          .add(cacheKey);
        }
    }
    
    public Set<String> getAffectedPlans(String tableName) {
        return tableToPlanKeys.getOrDefault(tableName, Collections.emptySet());
    }
}