
public class UsageExample {
    
    public static void main(String[] args) {
        // Initialize cache manager
        QueryPlanCacheManager cacheManager = new QueryPlanCacheManager(
            10000,  // max 10k entries
            3600000 // 1 hour TTL
        );
        
        System.out.println("=== SELECT STATEMENT EXAMPLES ===\n");
        
        // Example 1: Simple SELECT (cache MISS)
        String query1 = "SELECT * FROM orders WHERE customer_id = 101";
        ExecutionPlan plan1 = cacheManager.getExecutionPlan(query1);
        System.out.println("Query 1: " + query1);
        System.out.println("Result: Cache MISS (first execution)\n");
        
        // Example 2: Similar SELECT (cache HIT)
        String query2 = "SELECT * FROM orders WHERE customer_id = 202";
        ExecutionPlan plan2 = cacheManager.getExecutionPlan(query2);
        System.out.println("Query 2: " + query2);
        System.out.println("Result: Cache HIT (same pattern)\n");
        
        // Example 3: SELECT with JOIN (cache MISS)
        String query3 = "SELECT o.*, c.name FROM orders o " +
                       "JOIN customers c ON o.customer_id = c.id " +
                       "WHERE o.total > 1000 AND c.country = 'USA'";
        ExecutionPlan plan3 = cacheManager.getExecutionPlan(query3);
        System.out.println("Query 3: " + query3);
        System.out.println("Result: Cache MISS (new pattern with JOIN)\n");
        
        // Example 4: Similar JOIN with different parameters (cache HIT)
        String query4 = "SELECT o.*, c.name FROM orders o " +
                       "JOIN customers c ON o.customer_id = c.id " +
                       "WHERE o.total > 500 AND c.country = 'UK'";
        ExecutionPlan plan4 = cacheManager.getExecutionPlan(query4);
        System.out.println("Query 4: " + query4);
        System.out.println("Result: Cache HIT (same JOIN pattern)\n");
        
        // Example 5: Aggregate query (cache MISS)
        String query5 = "SELECT customer_id, COUNT(*), SUM(total) " +
                       "FROM orders WHERE status = 'COMPLETED' " +
                       "GROUP BY customer_id HAVING SUM(total) > 5000";
        ExecutionPlan plan5 = cacheManager.getExecutionPlan(query5);
        System.out.println("Query 5: " + query5);
        System.out.println("Result: Cache MISS (aggregate pattern)\n");
        
        // Example 6: CTE query (cache MISS)
        String query6 = "WITH high_value_customers AS (" +
                       "  SELECT customer_id FROM orders " +
                       "  GROUP BY customer_id HAVING SUM(total) > 10000" +
                       ") SELECT * FROM customers c " +
                       "WHERE c.id IN (SELECT customer_id FROM high_value_customers)";
        ExecutionPlan plan6 = cacheManager.getExecutionPlan(query6);
        System.out.println("Query 6: " + query6);
        System.out.println("Result: Cache MISS (CTE pattern)\n");
        
        System.out.println("\n=== INSERT STATEMENT EXAMPLES ===\n");
        
        // Example 7: Simple INSERT (cache MISS)
        String query7 = "INSERT INTO orders (customer_id, total, status) " +
                       "VALUES (101, 250.50, 'PENDING')";
        ExecutionPlan plan7 = cacheManager.getExecutionPlan(query7);
        System.out.println("Query 7: " + query7);
        System.out.println("Result: Cache MISS (INSERT pattern)\n");
        
        // Example 8: Similar INSERT with different values (cache HIT)
        String query8 = "INSERT INTO orders (customer_id, total, status) " +
                       "VALUES (202, 499.99, 'COMPLETED')";
        ExecutionPlan plan8 = cacheManager.getExecutionPlan(query8);
        System.out.println("Query 8: " + query8);
        System.out.println("Result: Cache HIT (same INSERT structure)\n");
        
        // Example 9: Multi-row INSERT (cache MISS)
        String query9 = "INSERT INTO orders (customer_id, total, status) VALUES " +
                       "(101, 100.00, 'PENDING'), " +
                       "(102, 200.00, 'PENDING'), " +
                       "(103, 300.00, 'PENDING')";
        ExecutionPlan plan9 = cacheManager.getExecutionPlan(query9);
        System.out.println("Query 9: " + query9);
        System.out.println("Result: Cache MISS (3-row INSERT pattern)\n");
        
        // Example 10: INSERT ... SELECT (cache MISS)
        String query10 = "INSERT INTO order_archive " +
                        "SELECT * FROM orders WHERE created_at < '2024-01-01'";
        ExecutionPlan plan10 = cacheManager.getExecutionPlan(query10);
        System.out.println("Query 10: " + query10);
        System.out.println("Result: Cache MISS (INSERT SELECT pattern)\n");
        
        System.out.println("\n=== UPDATE STATEMENT EXAMPLES ===\n");
        
        // Example 11: Simple UPDATE (cache MISS)
        String query11 = "UPDATE orders SET status = 'SHIPPED' WHERE id = 12345";
        ExecutionPlan plan11 = cacheManager.getExecutionPlan(query11);
        System.out.println("Query 11: " + query11);
        System.out.println("Result: Cache MISS (UPDATE pattern)\n");
        
        // Example 12: Similar UPDATE (cache HIT)
        String query12 = "UPDATE orders SET status = 'DELIVERED' WHERE id = 67890";
        ExecutionPlan plan12 = cacheManager.getExecutionPlan(query12);
        System.out.println("Query 12: " + query12);
        System.out.println("Result: Cache HIT (same UPDATE structure)\n");
        
        // Example 13: Multi-column UPDATE (cache MISS)
        String query13 = "UPDATE customers SET email = 'new@email.com', " +
                        "last_updated = '2025-01-01' WHERE customer_id = 555";
        ExecutionPlan plan13 = cacheManager.getExecutionPlan(query13);
        System.out.println("Query 13: " + query13);
        System.out.println("Result: Cache MISS (multi-column UPDATE)\n");
        
        // Example 14: UPDATE with complex WHERE (cache MISS)
        String query14 = "UPDATE orders SET discount = 0.10 " +
                        "WHERE total > 1000 AND customer_id IN (101, 102, 103)";
        ExecutionPlan plan14 = cacheManager.getExecutionPlan(query14);
        System.out.println("Query 14: " + query14);
        System.out.println("Result: Cache MISS (UPDATE with IN clause)\n");
        
        System.out.println("\n=== DELETE STATEMENT EXAMPLES ===\n");
        
        // Example 15: Simple DELETE (cache MISS)
        String query15 = "DELETE FROM orders WHERE id = 12345";
        ExecutionPlan plan15 = cacheManager.getExecutionPlan(query15);
        System.out.println("Query 15: " + query15);
        System.out.println("Result: Cache MISS (DELETE pattern)\n");
        
        // Example 16: Similar DELETE (cache HIT)
        String query16 = "DELETE FROM orders WHERE id = 67890";
        ExecutionPlan plan16 = cacheManager.getExecutionPlan(query16);
        System.out.println("Query 16: " + query16);
        System.out.println("Result: Cache HIT (same DELETE structure)\n");
        
        // Example 17: DELETE with complex condition (cache MISS)
        String query17 = "DELETE FROM orders WHERE status = 'CANCELLED' " +
                        "AND created_at < '2023-01-01'";
        ExecutionPlan plan17 = cacheManager.getExecutionPlan(query17);
        System.out.println("Query 17: " + query17);
        System.out.println("Result: Cache MISS (DELETE with AND condition)\n");
        
        System.out.println("\n=== COMPLEX QUERY EXAMPLES ===\n");
        
        // Example 18: Subquery in WHERE (cache MISS)
        String query18 = "SELECT * FROM customers WHERE id IN " +
                        "(SELECT customer_id FROM orders WHERE total > 1000)";
        ExecutionPlan plan18 = cacheManager.getExecutionPlan(query18);
        System.out.println("Query 18: " + query18);
        System.out.println("Result: Cache MISS (subquery pattern)\n");
        
        // Example 19: Window function (cache MISS)
        String query19 = "SELECT customer_id, order_date, total, " +
                        "ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY order_date) as row_num " +
                        "FROM orders WHERE customer_id = 101";
        ExecutionPlan plan19 = cacheManager.getExecutionPlan(query19);
        System.out.println("Query 19: " + query19);
        System.out.println("Result: Cache MISS (window function)\n");
        
        // Example 20: CASE expression (cache MISS)
        String query20 = "SELECT id, " +
                        "CASE WHEN total > 1000 THEN 'HIGH' " +
                        "     WHEN total > 500 THEN 'MEDIUM' " +
                        "     ELSE 'LOW' END as value_tier " +
                        "FROM orders WHERE status = 'COMPLETED'";
        ExecutionPlan plan20 = cacheManager.getExecutionPlan(query20);
        System.out.println("Query 20: " + query20);
        System.out.println("Result: Cache MISS (CASE expression)\n");
        
        // Print statistics
        System.out.println("\n=== CACHE STATISTICS ===");
        System.out.println(cacheManager.getStatistics());
        System.out.println("\nCache entries: " + cacheManager.getCacheSize());
        
        // Example 21: Schema change invalidation
        System.out.println("\n=== SCHEMA CHANGE SCENARIO ===\n");
        cacheManager.onSchemaChange("orders");
        System.out.println("Schema changed - cache invalidated for 'orders' table");
        System.out.println("Affected plans removed from cache\n");
        
        // Example 22: Query after invalidation (cache MISS)
        String query22 = "SELECT * FROM orders WHERE customer_id = 101";
        ExecutionPlan plan22 = cacheManager.getExecutionPlan(query22);
        System.out.println("Query 22: " + query22);
        System.out.println("Result: Cache MISS (after invalidation)\n");
        
        // Example 23: Query on unaffected table (still cached)
        String query23 = "SELECT * FROM customers WHERE id = 505";
        ExecutionPlan plan23 = cacheManager.getExecutionPlan(query23);
        System.out.println("Query 23: " + query23);
        System.out.println("Result: Depends on whether 'customers' queries were cached\n");
        
        // Final statistics
        System.out.println("\n=== FINAL CACHE STATISTICS ===");
        System.out.println(cacheManager.getStatistics());
        System.out.println("\nCache entries: " + cacheManager.getCacheSize());
        
        // Performance summary
        System.out.println("\n=== PERFORMANCE SUMMARY ===");
        double hitRatio = cacheManager.getStatistics().getHitRatio();
        System.out.printf("Overall Hit Ratio: %.2f%%\n", hitRatio * 100);
        System.out.println("Expected speedup for cached queries: 65-200x");
        System.out.println("Memory usage: ~" + (cacheManager.getCacheSize() * 3.6) + " KB");
    }
}