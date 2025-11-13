
import java.util.*;
public class NormalizedQuery {
    private final String pattern;
    private final List<Object> parameters;
    private final List<ParameterMetadata> parameterMetadata;
    private final Set<String> referencedTables;
    
    public NormalizedQuery(String pattern, List<Object> parameters,
                          List<ParameterMetadata> metadata, Set<String> tables) {
        this.pattern = pattern;
        this.parameters = parameters;
        this.parameterMetadata = metadata;
        this.referencedTables = tables;
    }
    
    public String getPattern() { return pattern; }
    public List<Object> getParameters() { return parameters; }
    public List<ParameterMetadata> getParameterMetadata() { return parameterMetadata; }
    public Set<String> getReferencedTables() { return referencedTables; }
}
