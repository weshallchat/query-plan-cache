import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
public class ExecutionPlan {
    private final JsonNode planJson;
    private final List<Object> boundParameters;
    
    public ExecutionPlan(JsonNode planJson, List<Object> parameters) {
        this.planJson = planJson;
        this.boundParameters = parameters;
    }
    
    public JsonNode getPlanJson() { return planJson; }
    public List<Object> getBoundParameters() { return boundParameters; }
    
    public void execute() {
        // Execute the plan with bound parameters
        // (Implementation would interact with execution engine)
    }
}