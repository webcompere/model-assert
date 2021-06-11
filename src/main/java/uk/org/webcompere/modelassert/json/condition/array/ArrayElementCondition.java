package uk.org.webcompere.modelassert.json.condition.array;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Result;

public interface ArrayElementCondition {
    /**
     * Execute the test of the condition
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    Result test(JsonNode json, int arrayIndex);

    /**
     * Describe the condition
     * @return description of the condition
     */
    String describe();
}
