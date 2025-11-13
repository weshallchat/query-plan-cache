import java.util.concurrent.atomic.AtomicLong;

public class CacheStatistics {
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    private final AtomicLong invalidations = new AtomicLong(0);
    
    public void recordHit() { hits.incrementAndGet(); }
    public void recordMiss() { misses.incrementAndGet(); }
    public void recordEviction() { evictions.incrementAndGet(); }
    public void recordInvalidation() { invalidations.incrementAndGet(); }
    public void recordFullClear() { 
        hits.set(0);
        misses.set(0);
    }
    
    public double getHitRatio() {
        long totalRequests = hits.get() + misses.get();
        return totalRequests == 0 ? 0.0 : (double) hits.get() / totalRequests;
    }
    
    public long getTotalHits() { return hits.get(); }
    public long getTotalMisses() { return misses.get(); }
    public long getTotalEvictions() { return evictions.get(); }
    public long getTotalInvalidations() { return invalidations.get(); }
    
    @Override
    public String toString() {
        return String.format(
            "Cache Statistics:\n" +
            "  Hits: %d\n" +
            "  Misses: %d\n" +
            "  Hit Ratio: %.2f%%\n" +
            "  Evictions: %d\n" +
            "  Invalidations: %d",
            hits.get(), misses.get(), getHitRatio() * 100,
            evictions.get(), invalidations.get()
        );
    }
}