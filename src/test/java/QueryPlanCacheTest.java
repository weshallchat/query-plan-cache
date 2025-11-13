// ============================================================================
// QueryPlanCacheTest.java - Executable Test Suite
// ============================================================================

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Comprehensive test suite for Query Plan Cache
 * Run with: mvn test or gradle test
 */
public class QueryPlanCacheTest {
    
    private QueryPlanCacheManager cacheManager;
    
    @BeforeEach
    void setUp() {
        // Initialize fresh cache for each test
        cacheManager = new QueryPlanCacheManager(10000, 3600000);
    }
    
    @AfterEach
    void tearDown() {
        if (cacheManager != null) {
            cacheManager.clearCache();
        }
    }
    
    // ========================================================================
    // 1. FUNCTIONAL TESTS - Query Normalization
    // ========================================================================
    
    @Nested
    @DisplayName("Query Normalization Tests")
    class NormalizationTests {
        
        @Test
        @DisplayName("Should normalize simple SELECT with integer")
        void testSimpleSelectNormalization() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            assertNotNull(plan);
            assertEquals(1, plan.getBoundParameters().size());
            assertEquals(101, plan.getBoundParameters().get(0));
        }
        
        @Test
        @DisplayName("Should normalize SELECT with multiple parameters")
        void testMultipleParametersNormalization() {
            String query = "SELECT * FROM orders WHERE customer_id = 101 AND status = 'ACTIVE'";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            assertNotNull(plan);
            assertEquals(2, plan.getBoundParameters().size());
            assertEquals(101, plan.getBoundParameters().get(0));
            assertEquals("ACTIVE", plan.getBoundParameters().get(1));
        }
        
        @Test
        @DisplayName("Should normalize IN clause with multiple values")
        void testInClauseNormalization() {
            String query = "SELECT * FROM products WHERE category_id IN (1, 2, 3)";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            assertNotNull(plan);
            assertEquals(3, plan.getBoundParameters().size());
            assertTrue(plan.getBoundParameters().containsAll(Arrays.asList(1, 2, 3)));
        }
        
        @Test
        @DisplayName("Should normalize whitespace consistently")
        void testWhitespaceNormalization() {
            String query1 = "SELECT   *   FROM   orders   WHERE   customer_id   =   101";
            String query2 = "SELECT * FROM orders WHERE customer_id = 101";
            
            ExecutionPlan plan1 = cacheManager.getExecutionPlan(query1);
            ExecutionPlan plan2 = cacheManager.getExecutionPlan(query2);
            
            // Should hit cache on second query (same normalized form)
            assertEquals(1, cacheManager.getStatistics().getTotalHits());
        }
        
        @Test
        @DisplayName("Should keep NULL as keyword")
        void testNullHandling() {
            String query = "SELECT * FROM orders WHERE cancelled_at IS NULL";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            assertNotNull(plan);
            assertEquals(0, plan.getBoundParameters().size());
        }
        
        @Test
        @DisplayName("Should handle string escaping")
        void testStringEscaping() {
            String query = "SELECT * FROM users WHERE name = 'O''Reilly'";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            assertNotNull(plan);
            assertEquals(1, plan.getBoundParameters().size());
            assertEquals("O'Reilly", plan.getBoundParameters().get(0));
        }
    }
    
    // ========================================================================
    // 2. FUNCTIONAL TESTS - Cache Hit/Miss Logic
    // ========================================================================
    
    @Nested
    @DisplayName("Cache Hit/Miss Tests")
    class CacheHitMissTests {
        
        @Test
        @DisplayName("First query should be cache MISS")
        void testCacheMissOnFirstQuery() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            long missesBelow = cacheManager.getStatistics().getTotalMisses();
            cacheManager.getExecutionPlan(query);
            long missesAfter = cacheManager.getStatistics().getTotalMisses();
            
            assertEquals(1, missesAfter - missesBelow);
        }
        
        @Test
        @DisplayName("Similar query should be cache HIT")
        void testCacheHitOnSimilarQuery() {
            String query1 = "SELECT * FROM orders WHERE customer_id = 101";
            String query2 = "SELECT * FROM orders WHERE customer_id = 202";
            
            cacheManager.getExecutionPlan(query1); // MISS
            
            long hitsBelow = cacheManager.getStatistics().getTotalHits();
            cacheManager.getExecutionPlan(query2); // HIT
            long hitsAfter = cacheManager.getStatistics().getTotalHits();
            
            assertEquals(1, hitsAfter - hitsBelow);
        }
        
        @Test
        @DisplayName("Different query structure should be cache MISS")
        void testCacheMissOnDifferentStructure() {
            String query1 = "SELECT * FROM orders WHERE customer_id = 101";
            String query2 = "SELECT * FROM orders WHERE status = 'ACTIVE'";
            
            cacheManager.getExecutionPlan(query1);
            
            long missesBelow = cacheManager.getStatistics().getTotalMisses();
            cacheManager.getExecutionPlan(query2);
            long missesAfter = cacheManager.getStatistics().getTotalMisses();
            
            assertEquals(2, missesAfter); // Both are misses
        }
        
        @Test
        @DisplayName("Should maintain correct hit ratio")
        void testHitRatioCalculation() {
            // Execute 10 unique patterns, each twice
            for (int i = 0; i < 10; i++) {
                String query = "SELECT * FROM orders WHERE customer_id = " + (i + 100);
                cacheManager.getExecutionPlan(query); // MISS
                cacheManager.getExecutionPlan(query); // HIT
            }
            
            // 10 misses, 10 hits = 50% hit ratio
            double hitRatio = cacheManager.getStatistics().getHitRatio();
            assertTrue(hitRatio >= 0.5, "Hit ratio should be at least 50%");
        }
    }
    
    // ========================================================================
    // 3. FUNCTIONAL TESTS - All Statement Types
    // ========================================================================
    
    @Nested
    @DisplayName("Statement Type Tests")
    class StatementTypeTests {
        
        @Test
        @DisplayName("Should handle SELECT statements")
        void testSelectStatement() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            assertNotNull(plan);
        }
        
        @Test
        @DisplayName("Should handle SELECT with JOIN")
        void testSelectWithJoin() {
            String query = "SELECT o.*, c.name FROM orders o " +
                          "JOIN customers c ON o.customer_id = c.id " +
                          "WHERE o.total > 1000";
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            assertNotNull(plan);
            assertEquals(1, plan.getBoundParameters().size());
        }
        
        @Test
        @DisplayName("Should handle INSERT statements")
        void testInsertStatement() {
            String query = "INSERT INTO orders (customer_id, total, status) " +
                          "VALUES (101, 250.50, 'PENDING')";
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            assertNotNull(plan);
            assertEquals(3, plan.getBoundParameters().size());
        }
        
        @Test
        @DisplayName("Should handle UPDATE statements")
        void testUpdateStatement() {
            String query = "UPDATE orders SET status = 'SHIPPED' WHERE id = 12345";
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            assertNotNull(plan);
            assertEquals(2, plan.getBoundParameters().size());
        }
        
        @Test
        @DisplayName("Should handle DELETE statements")
        void testDeleteStatement() {
            String query = "DELETE FROM orders WHERE id = 12345";
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            assertNotNull(plan);
            assertEquals(1, plan.getBoundParameters().size());
        }
        
        @Test
        @DisplayName("Should handle CTE queries")
        void testCteQuery() {
            String query = "WITH high_value AS (SELECT * FROM orders WHERE total > 1000) " +
                          "SELECT * FROM high_value WHERE customer_id = 101";
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            assertNotNull(plan);
            assertEquals(2, plan.getBoundParameters().size());
        }
    }
    
    // ========================================================================
    // 4. FUNCTIONAL TESTS - Schema Invalidation
    // ========================================================================
    
    @Nested
    @DisplayName("Schema Invalidation Tests")
    class SchemaInvalidationTests {
        
        @Test
        @DisplayName("Should invalidate cache on schema change")
        void testSchemaChangeInvalidation() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            // Execute and cache
            cacheManager.getExecutionPlan(query);
            assertEquals(1, cacheManager.getCacheSize());
            
            // Trigger schema change
            cacheManager.onSchemaChange("orders");
            
            // Cache should be cleared for orders table
            assertTrue(cacheManager.getCacheSize() < 1);
        }
        
        @Test
        @DisplayName("Should only invalidate affected tables")
        void testSelectiveInvalidation() {
            String ordersQuery = "SELECT * FROM orders WHERE id = 1";
            String usersQuery = "SELECT * FROM users WHERE id = 1";
            
            cacheManager.getExecutionPlan(ordersQuery);
            cacheManager.getExecutionPlan(usersQuery);
            assertEquals(2, cacheManager.getCacheSize());
            
            // Change only orders schema
            cacheManager.onSchemaChange("orders");
            
            // Users query should still be cached
            long hitsBefore = cacheManager.getStatistics().getTotalHits();
            cacheManager.getExecutionPlan(usersQuery);
            long hitsAfter = cacheManager.getStatistics().getTotalHits();
            
            assertEquals(1, hitsAfter - hitsBefore);
        }
        
        @Test
        @DisplayName("Should handle multiple schema changes")
        void testMultipleSchemaChanges() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            cacheManager.getExecutionPlan(query);
            cacheManager.onSchemaChange("orders");
            cacheManager.onSchemaChange("customers");
            cacheManager.onSchemaChange("products");
            
            // Re-execute query - should be cache miss
            long misses = cacheManager.getStatistics().getTotalMisses();
            cacheManager.getExecutionPlan(query);
            
            assertTrue(cacheManager.getStatistics().getTotalMisses() > misses);
        }
    }
    
    // ========================================================================
    // 5. PERFORMANCE TESTS
    // ========================================================================
    
    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {
        
        @Test
        @DisplayName("Cache lookup should be faster than plan generation")
        void testCacheLookupSpeed() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            // First execution (cache miss - includes plan generation)
            long startMiss = System.nanoTime();
            cacheManager.getExecutionPlan(query);
            long timeMiss = System.nanoTime() - startMiss;
            
            // Second execution (cache hit)
            long startHit = System.nanoTime();
            cacheManager.getExecutionPlan(query);
            long timeHit = System.nanoTime() - startHit;
            System.out.println("Time miss: " + timeMiss + " Time hit: " + timeHit);
            // Cache hit should be significantly faster
            assertTrue(timeHit < timeMiss / 10, "Cache hit should be at least 10x faster than miss");
            
        }
        
        @Test
        @DisplayName("Should handle high query volume efficiently")
        void testHighVolumePerformance() {
            int numQueries = 10000;
            int numPatterns = 100;
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < numQueries; i++) {
                int patternId = i % numPatterns;
                String query = "SELECT * FROM orders WHERE customer_id = " + patternId;
                cacheManager.getExecutionPlan(query);
            }
            
            long duration = System.currentTimeMillis() - startTime;
            
            // Should complete in reasonable time
            assertTrue(duration < 5000, "10k queries should complete in <5 seconds");
            
            // Should have high hit ratio
            double hitRatio = cacheManager.getStatistics().getHitRatio();
            assertTrue(hitRatio > 0.90, "Hit ratio should be >90% for repetitive workload");
        }
        
        @Test
        @DisplayName("Should measure throughput improvement")
        void testThroughputImprovement() {
            String query = "SELECT * FROM orders WHERE customer_id = ";
            int iterations = 1000;
            
            // Warmup
            for (int i = 0; i < 10; i++) {
                cacheManager.getExecutionPlan(query + i);
            }
            
            // Measure with cache
            long startCached = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                cacheManager.getExecutionPlan(query + (i % 10));
            }
            long durationCached = System.currentTimeMillis() - startCached;
            
            double qps = (double) iterations / (durationCached / 1000.0);
            
            // Should achieve high queries per second
            assertTrue(qps > 1000, "Should process >1000 queries/sec with cache");
        }
    }
    
    // ========================================================================
    // 6. CONCURRENCY TESTS
    // ========================================================================
    
    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {
        
        @Test
        @DisplayName("Should handle concurrent cache misses safely")
        void testConcurrentCacheMisses() throws Exception {
            int numThreads = 10;
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            CountDownLatch latch = new CountDownLatch(numThreads);
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            
            List<Future<ExecutionPlan>> futures = new ArrayList<>();
            
            // All threads execute same query simultaneously
            for (int i = 0; i < numThreads; i++) {
                futures.add(executor.submit(() -> {
                    latch.countDown();
                    latch.await(); // Wait for all threads
                    return cacheManager.getExecutionPlan(query);
                }));
            }
            
            // Wait for completion
            List<ExecutionPlan> plans = new ArrayList<>();
            for (Future<ExecutionPlan> future : futures) {
                plans.add(future.get());
            }
            
            executor.shutdown();
            
            // All should succeed
            assertEquals(numThreads, plans.size());
            plans.forEach(plan -> assertNotNull(plan));
            
            // Should only have 1 cache entry (not duplicated)
            assertEquals(1, cacheManager.getCacheSize());
        }
        
        @Test
        @DisplayName("Should handle concurrent reads and writes")
        void testConcurrentReadsAndWrites() throws Exception {
            int numReaders = 20;
            int numWriters = 5;
            int operationsPerThread = 100;
            
            ExecutorService executor = Executors.newFixedThreadPool(numReaders + numWriters);
            List<Future<?>> futures = new ArrayList<>();
            
            // Reader threads (cache hits)
            for (int i = 0; i < numReaders; i++) {
                final int readerId = i;
                futures.add(executor.submit(() -> {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String query = "SELECT * FROM orders WHERE customer_id = " + (readerId % 5);
                        cacheManager.getExecutionPlan(query);
                    }
                }));
            }
            
            // Writer threads (cache misses)
            for (int i = 0; i < numWriters; i++) {
                final int writerId = i;
                futures.add(executor.submit(() -> {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String query = "SELECT * FROM products WHERE id = " + (writerId * 100 + j);
                        cacheManager.getExecutionPlan(query);
                    }
                }));
            }
            
            // Wait for all to complete
            for (Future<?> future : futures) {
                future.get();
            }
            
            executor.shutdown();
            
            // No exceptions should occur
            long totalOps = (numReaders + numWriters) * operationsPerThread;
            long hits = cacheManager.getStatistics().getTotalHits();
            long misses = cacheManager.getStatistics().getTotalMisses();
            
            assertEquals(totalOps, hits + misses, 10);
        }
        
        @Test
        @DisplayName("Should handle concurrent invalidations")
        void testConcurrentInvalidations() throws Exception {
            // Pre-populate cache
            for (int i = 0; i < 100; i++) {
                String query = "SELECT * FROM orders WHERE id = " + i;
                cacheManager.getExecutionPlan(query);
            }
            
            int numThreads = 10;
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<?>> futures = new ArrayList<>();
            
            // Threads trigger schema changes and query simultaneously
            for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                futures.add(executor.submit(() -> {
                    for (int j = 0; j < 50; j++) {
                        if (threadId % 3 == 0) {
                            cacheManager.onSchemaChange("orders");
                        } else {
                            String query = "SELECT * FROM orders WHERE id = " + j;
                            cacheManager.getExecutionPlan(query);
                        }
                    }
                }));
            }
            
            for (Future<?> future : futures) {
                future.get();
            }
            
            executor.shutdown();
            
            // Should complete without exceptions
            assertTrue(true);
        }
    }
    
    // ========================================================================
    // 7. CORRECTNESS TESTS
    // ========================================================================
    
    @Nested
    @DisplayName("Correctness Tests")
    class CorrectnessTests {
        
        @Test
        @DisplayName("Should bind parameters correctly")
        void testParameterBinding() {
            String query = "SELECT * FROM orders WHERE customer_id = 101 AND total > 500.00";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            List<Object> params = plan.getBoundParameters();
            assertEquals(2, params.size());
            assertEquals(101, params.get(0));
            assertEquals(500.00, (Double) params.get(1), 0.01);
        }
        
        @Test
        @DisplayName("Should maintain parameter order")
        void testParameterOrder() {
            String query = "INSERT INTO orders (customer_id, total, status) VALUES (101, 250.50, 'PENDING')";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            List<Object> params = plan.getBoundParameters();
            assertEquals(101, params.get(0));
            assertEquals(250.50, (Double) params.get(1), 0.01);
            assertEquals("PENDING", params.get(2));
        }
        
        @Test
        @DisplayName("Should produce consistent results after cache clear")
        void testConsistencyAfterClear() {
            String query = "SELECT * FROM orders WHERE customer_id = 101";
            
            ExecutionPlan plan1 = cacheManager.getExecutionPlan(query);
            cacheManager.clearCache();
            ExecutionPlan plan2 = cacheManager.getExecutionPlan(query);
            
            // Plans should be equivalent
            assertEquals(plan1.getBoundParameters(), plan2.getBoundParameters());
        }
    }
    
    // ========================================================================
    // 8. EDGE CASE TESTS
    // ========================================================================
    
    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle very long queries")
        void testVeryLongQuery() {
            StringBuilder query = new StringBuilder("SELECT * FROM orders WHERE id IN (");
            for (int i = 0; i < 500; i++) {
                query.append(i);
                if (i < 499) query.append(",");
            }
            query.append(")");
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query.toString());
            
            assertNotNull(plan);
            assertEquals(500, plan.getBoundParameters().size());
        }
        
        @Test
        @DisplayName("Should handle empty string parameters")
        void testEmptyStringParameter() {
            String query = "SELECT * FROM users WHERE email = ''";
            
            ExecutionPlan plan = cacheManager.getExecutionPlan(query);
            
            assertNotNull(plan);
            assertEquals(1, plan.getBoundParameters().size());
            assertEquals("", plan.getBoundParameters().get(0));
        }
        
        @Test
        @DisplayName("Should handle maximum cache size")
        void testMaxCacheSize() {
            int maxSize = 100;
            QueryPlanCacheManager smallCache = new QueryPlanCacheManager(maxSize, 3600000);
            
            // Add more than max size
            for (int i = 0; i < maxSize + 50; i++) {
                String query = "SELECT * FROM orders WHERE id = " + i;
                smallCache.getExecutionPlan(query);
            }
            
            // Should not exceed max size
            assertTrue(smallCache.getCacheSize() <= maxSize);
        }
    }
    
    // ========================================================================
    // 9. INTEGRATION TESTS
    // ========================================================================
    
    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {
        
        @Test
        @DisplayName("Should handle mixed workload")
        void testMixedWorkload() {
            // Simulate realistic workload
            String[] queries = {
                "SELECT * FROM orders WHERE customer_id = ",
                "INSERT INTO orders (customer_id, total) VALUES (",
                "UPDATE orders SET status = 'SHIPPED' WHERE id = ",
                "DELETE FROM orders WHERE id = "
            };
            
            for (int i = 0; i < 1000; i++) {
                String baseQuery = queries[i % queries.length];
                String query;
                
                if (baseQuery.contains("INSERT")) {
                    query = baseQuery + (i % 100) + ", 100.00)";
                } else {
                    query = baseQuery + (i % 100);
                }
                
                cacheManager.getExecutionPlan(query);
            }
            
            // Should have reasonable hit ratio
            double hitRatio = cacheManager.getStatistics().getHitRatio();
            assertTrue(hitRatio > 0.70);
        }
    }
}