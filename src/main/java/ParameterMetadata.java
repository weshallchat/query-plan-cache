
public class ParameterMetadata {
    private final int index;
    private final ParameterType type;
    private final Object sampleValue;
    
    public ParameterMetadata(int index, ParameterType type, Object sampleValue) {
        this.index = index;
        this.type = type;
        this.sampleValue = sampleValue;
    }
    
    public int getIndex() { return index; }
    public ParameterType getType() { return type; }
    public Object getSampleValue() { return sampleValue; }
}