# Query Plan Cache Manager

A high-performance SQL query plan caching system with **105x speedup** using ANTLR 4 for query normalization and a two-tier cache architecture.

## Features

- **Two-Tier Caching**: Fast-path for identical queries (0.7 µs) + normalized pattern cache (30 µs)
- **105x Performance Improvement**: Cache hits 105x faster than cache misses
- **ANTLR 4-Based SQL Parser**: Supports SELECT, INSERT, UPDATE, DELETE, MERGE, CTEs, JOINs, subqueries
- **Thread-Safe**: Concurrent access with fine-grained locking and double-checked locking pattern
- **Schema Invalidation**: Automatic cache invalidation on schema changes
- **Comprehensive Testing**: 31/32 tests passing (97% success rate)

## Performance Metrics

| Metric | Cache Miss | Tier 2 Hit | Tier 1 Hit | Speedup |
|--------|-----------|------------|------------|---------|
| Latency | 70.5 µs | 30.0 µs | 0.67 µs | 2.3x / **105x** |
| Throughput | 14K q/s | 26K q/s | **1.48M q/s** | 1.9x / **104x** |


## Technology Stack

- **Java 11+** (tested on Java 25)
- **ANTLR 4.13.1** - SQL grammar and parser generation
- **Jackson 2.15.0** - JSON plan representation
- **JUnit 5** - Comprehensive test suite
- **Maven 3.9+** - Build automation

## Project Structure

**Core Files:**
- `pom.xml` - Maven configuration
- `Design Document.md` - Architecture and design decisions
- `TEST_PLAN.md` - Comprehensive test documentation

**Source Code (`src/main/java/`):**
- `QueryPlanCacheManager.java` - Main two-tier cache manager
- `QueryNormalizer.java` - ANTLR-based SQL normalizer
- `NormalizationVisitor.java` - AST visitor for parameter extraction
- `CachedPlan.java` - Cache entry with metadata
- `CacheStatistics.java` - Hit/miss metrics tracking
- `SchemaVersionTracker.java` - Schema version invalidation
- `ExecutionPlan.java` - Execution plan representation
- `NormalizedQuery.java` - Normalized query result
- `ParameterMetadata.java` - Parameter type information
- `ParameterType.java` - Parameter type enum
- `UsageExample.java` - Demo application

**Grammar (`src/main/antlr4/`):**
- `SQL.g4` - ANTLR SQL grammar (SELECT, INSERT, UPDATE, DELETE, MERGE, CTE)

**Tests (`src/test/java/`):**
- `QueryPlanCacheTest.java` - 32 comprehensive tests (31 passing)

### Prerequisites
- Java 11 or higher
- Maven 3.9 or higher

### Build
mvn clean compile 
### Run Tests 
mvn test 
### Run Example
mvn exec:java -Dexec.mainClass="UsageExample"

### Architecture
- **Tier 1**: Exact query string → cache key mapping (bypasses parsing)
- **Tier 2**: Normalized pattern → execution plan mapping
- **Result**: 105x speedup for repeated identical queries

### Query Normalization Strategy
- Replace all literals with `?` placeholders
- Preserve NULL, keywords, identifiers
- Extract parameter metadata (type, position)
- Generate SHA-256 hash for cache key

### Thread Safety
- `ConcurrentHashMap` for lock-free reads
- Per-key locks for plan generation (double-checked locking)
- Atomic counters for statistics

### Cache Management
- LRU eviction policy
- TTL-based expiration (default: 1 hour)
- Configurable max size (default: 10,000 entries)
- Schema-aware invalidation

## Known Limitations

**Selective Schema Invalidation** (Test Failure)
- Current: Global schema version invalidates all plans
- Future: Per-table version tracking for selective invalidation
- Impact: Slightly lower hit rate after schema changes
- Status: Documented as acceptable for v1.0

## Documentation

- [Design Document] Architecture, design decisions, trade-offs
- [Test Plan] Test coverage, execution results, performance benchmarks

## Contributing

This project was developed as a technical assessment for TigerGraph.

## Author

Vishal Chatterjee

