import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
public class CachedPlan {
    private final JsonNode plan;
    private final List<ParameterMetadata> parameterMetadata;
    private final String schemaVersion;
    private final long creationTime;
    private long lastAccessTime;
    private long hitCount;
    
    public CachedPlan(JsonNode plan, List<ParameterMetadata> metadata,
                      String schemaVersion, long creationTime) {
        this.plan = plan;
        this.parameterMetadata = metadata;
        this.schemaVersion = schemaVersion;
        this.creationTime = creationTime;
        this.lastAccessTime = creationTime;
        this.hitCount = 0;
    }
    
    public void recordAccess() {
        this.lastAccessTime = System.currentTimeMillis();
        this.hitCount++;
    }
    
    public JsonNode getPlan() { return plan; }
    public List<ParameterMetadata> getParameterMetadata() { return parameterMetadata; }
    public String getSchemaVersion() { return schemaVersion; }
    public long getCreationTime() { return creationTime; }
    public long getLastAccessTime() { return lastAccessTime; }
    public long getHitCount() { return hitCount; }
}