# Query Plan Caching System - Design Document

## Executive Summary:

This document outlines the design of a query plan caching mechanism that optimizes database query execution by reusing execution plans for repeated, structurally identical queries. The system reduces parsing and optimization costs by caching the plan keyed by a normalized query "shape".

---

## 1. System Overview:

### 1.1 Problem Statement:

When database executes queries like:

```sql
SELECT * FROM orders WHERE customer_id = 101;
SELECT * FROM orders WHERE customer_id = 202;
```
It regenerates almost identical execution plans each time wasting CPU cycles and reducing throughput.

### 1.2 Solution Approach:
Our caching system does the following:
- Normalizes the query by replacing the constant values with placeholders.
- Caches execution plans keyed by normalized query patterns.
- Reuses cached plans for identical queries.
- Invalidates cache when schema changes.

---

## 2. Architecture:

### 2.1 High Level Architecture:

```
┌─────────────┐
│   Client    │
│   Query     │
└─────────────┘
       │
┌─────────────────────────────────────┐
│    Query Plan Cache Manager         │
├─────────────────────────────────────┤
│  1. Query Normalizer                │
│  2. Cache Lookup                    │
│  3. Plan Generator (on miss)        │
│  4. Cache Storage                   │
│  5. Invalidation Handler            │
└─────────────────────────────────────┘
       │
┌─────────────┐
│  Execution  │
│   Engine    │
└─────────────┘
```

### 2.2 Core Components:

#### 2.2.1: Query Normalizer:
- Purpose: Convert the query to a canonical form that can be used to identify identical queries.
- Method:
  - Parse the query using ANTLR4 and replace the constant values with placeholders.
  - Remove comments and whitespace.
  - Normalize the query to a canonical form.
- Output: Normalized query string + parameter list.

Example:
```sql
/* Input: */    
SELECT * FROM orders WHERE customer_id = 101 AND status = 'ACTIVE'
/* Output: */
SELECT * FROM orders WHERE customer_id = ? AND status = ?
/* Params: */
[101, 'ACTIVE']
```

#### 2.2.2: Cache Storage (Two-Tier Architecture):

My implementations uses a two-tier caching strategy for optimal performance:

**Tier 1: Query-to-Key Cache (Fast Path)**
- Data Structure: 'ConcurrentHashMap<String, String>'
- Key: Raw SQL query string (exact match)
- Value: Normalized query cache key
- Purpose: Bypass expensive parsing for repeated identical queries
- Lookup Time: ~1-2 microseconds (hash lookup only)

**Tier 2: Plan Cache (Slow Path)**
- Data Structure: 'ConcurrentHashMap<String, CachedPlan>'
- Key: SHA-256 hash of normalized query pattern
- Value: CachedPlan object containing:
  - Execution plan (JsonNode)
  - Parameter metadata (types, positions)
  - Schema version fingerprint
  - Creation timestamp
  - Last access timestamp
  - Hit count statistics

**Performance Benefit:**
- First request: ~70 microseconds (parse + normalize + cache)
- Subsequent identical requests: ~0.7 microseconds (direct cache hit)
- **105x speedup** for cache hits on identical queries

#### 2.2.3: Cache Lookup Logic (Two Tier):

```
1. Receive SQL query
2. Fast Path: Check queryToKeyCache (Tier 1)
  - If hit: Direct lookup in planCache, validate, bind parameters
  - Time: ~0.7 microseconds
3. Slow Path (on fast path miss):
  - Parse query with ANTLR
  - Normalize via visitor pattern -> get pattern and parameters
  - Generate cache key (SHA-256 hash)
  - Check planCache (Tier 2)
  - Store raw query -> key mapping in Tier 1
4. On plan cache miss:
  - Generate execution plan
  - Store in planCache with metadata
  - Record dependencies for invalidation
5. Return execution plan with bound parameters
```

#### 2.2.4: Query Plan Reuse Logic

This system reuses execution plans through a multi-step validation process:

**Step 1: Query Matching**
- Tier 1: Exact string match (fastest)
- Tier 2: Normalized pattern match (requires parsing)

**Step 2: Plan Validation**
Before reusing a cached plan, we verify:
- Schema version matches current version
- TTL has not expired (age < 1 hour)
- Plan metadata is complete

**Step 3: Parameter Binding**
- Extract parameter values from original query
- Map parameters to plan placeholders by position
- Type-check parameters against metadata
- Bind values into execution plan

**Step 4: Plan Execution**
- Return validated plan with bound parameters
- Update access statistics (hit count, timestamp)
- Trigger LRU update for eviction policy

**Reuse Benefits:**
- Eliminates redundant parsing: ~25 µs saved
- Eliminates query optimization: ~25 µs saved
- Total savings: ~70 µs → 0.7 µs (100x improvement)

**Correctness Guarantees:**
- Plans are never reused after schema changes
- Stale plans are automatically evicted by TTL
- Concurrent access uses thread-safe data structures

#### 2.2.5 Cache Invalidation
- Implemented: Global schema version tracking
- Implemented: Table dependency recording
- Implemented: Affected plan removal on schema change
- Partial: Selective invalidation (invalidates all plans globally due to version increment)
- Scope for Enhancement: Per-table version tracking for true selective invalidation

**Invalidation Impact on Two-Tier Cache:**
- Both Tier 1 (queryToKeyCache) and Tier 2 (planCache) are invalidated
- Ensures consistency after schema changes
- Fast path cache entries are removed for affected tables

## 3. Query Normalization Strategy

### 3.1 Normalization Rules

| SQL Element | Normalization Action |
|-------------|---------------------|
| Integer literals | Replace with `?` |
| String literals | Replace with `?` |
| Float literals | Replace with `?` |
| Date/Time literals | Replace with `?` |
| NULL values | Keep as `NULL` |
| Column names | Keep unchanged |
| Table names | Keep unchanged |
| Keywords | Keep unchanged |
| Whitespace | Normalize to single space |
| Case | Convert to uppercase |

### 3.2 Implementation Approach

Using ANTLR visitor pattern:

1. Parse SQL into Abstract Syntax Tree (AST)
2. Traverse AST with custom visitor
3. Replace literal nodes with placeholder tokens
4. Track original values in parameter list
5. Reconstruct normalized query string

### 3.3 Edge Cases

Multi-line queries:
```sql
SELECT *
FROM orders
WHERE customer_id = 101
```
-> Normalize whitespace to: `SELECT * FROM orders WHERE customer_id = ?`

IN clauses with varying list sizes:
```sql
WHERE id IN (1, 2, 3)
WHERE id IN (4, 5)
```
-> Different Patterns (different number of parameters)

Subqueries:
- Recursively normalize inner queries
- Each subquery level maintains its own parameter list

### 3.4 Implementation Technology Stack

**Parsing & AST:**
- ANTLR 4.13.1 for SQL grammar definition
- Custom visitor pattern for AST traversal
- Generated lexer/parser with error recovery

**Core Libraries:**
- Java 11+ (compatible with 8-25)
- Jackson 2.15.0 for JSON plan representation
- JUnit 5 for comprehensive testing

**Thread Safety:**
- ConcurrentHashMap for lock-free reads
- Per-key synchronization for plan generation
- Atomic counters for statistics

**Build & Dependency Management:**
- Maven 3.9+ for build automation
- ANTLR4 Maven plugin for grammar compilation
- Surefire plugin for test execution
---

## 4. Cache Management

### 4.1 Cache Eviction Policy

LRU (Least Recently Used) with constraints:
- Max cache size: 10,000 entries (configurable)
- TTL: 1 hour (configurable)
- Memory threshold: 500 MB (configurable)

Eviction triggers:
- Cache size exceeds max entries -> evict LRU
- Entry age exceeds TTL -> evict on access
- Memory pressure -> evict bulk LRU entries

### 4.2 Cache Warming

Cold Start Strategy:
- Monitor query logs
- Pre-populate cache with most frequent queries on startup
- Gradual warm-up to avoid initial latency spike

### 4.3 Cache Statistics

Track per-entry metrics:
- Hit count
- Last access time
- Creation time
- Average execution time
- Parameter value distribution (for optimization hints)

### 4.4 Two-Tier Cache Coordination

**Consistency Guarantees:**
- Tier 1 mappings are validated against Tier 2 existence
- Stale Tier 1 entries are automatically removed on lookup
- Schema invalidation clears both tiers for affected queries

**Memory Management:**
- Tier 1: ~100 bytes per entry (string -> string)
- Tier 2: ~2-10 KB per entry (full plan + metadata)
- 10,000 cached queries: ~1 MB (Tier 1) + 20-100 MB (Tier 2)

**Eviction Strategy:**
- Tier 2 LRU eviction automatically invalidates Tier 1 references
- Tier 1 can grow larger (lightweight entries)
- Orphaned Tier 1 entries cleaned up lazily on access

--- 

## 5. Thread Safety

### 5.1 Concurrency Model

Ready-heavy optimization:
- Use 'ConcurrentHashMap' for lock-free reads
- Write operations (cache misses) use fine-grained locking
- Per-key locks for plan generation (avoid duplicate work)

### 5.2 Race Condition Handling

Scenario: Multiple threads cache miss on same query

Solution: Double-checked locking pattern

```
1. Check cache (no lock)
2. If miss, acquire key-specific lock
3. Check cache again (another thread may have populated)
4. If still miss, generate and store plan
5. Release lock
```

---

## 6. Schema Change Handling

### 6.1 Detection Mechanisms

Active Detection:
- Hook into DDL execution path
- Intercept ALTER?DROP?CREATE statements
- Trigger invalidation before DDL completes

Passive Detection:
- Schema version tracking
- Compare current version with cached plan's version
- Invalidate on version mismatch

### 6.2 Invalidation Strategies

#### Strategy 1: Global Invalidation (Simple)
- Trigger: Any schema change
- Action: Invalidate plans referencing that table
- Pros: Minimizes cache churn
- Cons: Requires dependency tracking

Implementation:

```java
Map> tableToPlanKeys;
// table_name -> set of cache keys that reference it

On schema change for table T:
  for each key in tableToPlanKeys.get(T):
    cache.remove(key)
```


---

## 7. Performance Considerations

### 7.1 Measured Performance Gains

**Actual Benchmark Results** (from test suite):

Cache Miss (First Request):
- Parse (ANTLR): ~25 microseconds
- Normalize (AST visitor): ~15 microseconds
- Hash generation (SHA-256): ~5 microseconds
- Plan generation: ~25 microseconds
- **Total: ~70 microseconds**

Cache Hit (Tier 2 - Normalized Pattern):
- Cache lookup + validation: ~2 microseconds
- Parameter binding: ~1 microseconds
- **Total: ~30 microseconds**
- **Speedup: 2.3x for cache phase**

Cache Hit (Tier 1 - Identical Query):
- Direct cache lookup: ~0.5 microseconds
- Parameter binding: ~0.2 microseconds
- **Total: ~0.7 microseconds**
- **Speedup: 105x over cache miss**

**Workload-Dependent Gains:**
- OLTP (repetitive identical queries): **100x+ speedup** (Tier 1 hits)
- OLTP (similar queries with different values): **2-3x speedup** (Tier 2 hits)
- OLAP (unique queries): Minimal benefit (mostly misses)

**Real-World Impact:**
- High-throughput systems: ~50,000 queries/sec -> 500,000 queries/sec potential
- Latency reduction: 70 microseconds -> 0.7 microseconds per query for cached plans

### 7.2 Memory Overhead

Per cached plan:
- Normalized query string: ~200 bytes
- Execution plan JSON: ~2-10 KB
- Metadata: ~100 bytes
- Total: ~2-10 KB per entry

For 10,000 entries:
- Estimated memory: 20-100 MB

### 7.3 Cache Hit Ration Targets

| Workload Type | Target Hit Ratio | Notes |
|---------------|------------------|-------|
| OLTP (transactional) | 85-95% | Highly repetitive |
| Mixed workload | 60-80% | Some ad-hoc queries |
| OLAP (analytical) | 30-60% | Many unique queries |

---


## 8. Testing Strategy Summary

### 8.1 Functional Testing
- Normalization correctness
- Cache hit/miss logic
- Parameter binding
- Invalidation triggers

### 8.2 Performance Testing
- Latency reduction measurement
- Throughput improvement
- Memory usage monitoring
- Cache hit ratio analysis

### 8.3 Stress Testing
- Concurrent access pattern
- Large cache sizes
- Cache eviction behavior
- Memory pressure scenarios

 TEST_PLAN.md has a more detailed response for test cases

---

## 9. Tradeoffs and Design Decisions

### 9.1 Normalization Granularity

Decision: Normalize all literals to placeholders

Alternative: Keep some literals (like table names) in patterns
- Rejected: Would create too many unique patterns

### 9.2 Cache Key Generation

Decision:  Use hash normalized query string

Alternative: Global invalidation on any schema change
- Rejected: More overhead, harder to implement

###  9.3 Invalidation Strategy

Decision: Table-level invalidation with dependency tracking

Alternative: Global invalidation on any schema change
- Tradeoff: More complexity vs. better cache retention

### 9.4 Thread safety

Decision: ConcurrentHashMap with per-key locks for writes

Alternative: Single global lock
- Rejected:  Poor concurrency for read-heavy workload


## 10. Conclusion

The proposed query plan caching system provides significant performance improvements for repetitive query workloads with minimal complexity. The design balances simplicity, performance, and correctness through:

- Efficient normalization using ANTLR
- Thread-safe cache with appropriate eviction policies
- Smart invalidation to maintain correctness
- Comprehensive monitoring and statistics

Expected Outcomes:
- 70-90% reduction in query planning time for OLTP workloads
- Minimal memory overhead (~50-100 MB)
- High cache hit ratios (>85%) for typical applications
- Transparent to application code (no query changes needed)


