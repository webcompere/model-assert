package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.AndCondition;

/**
 * A condition that the data must meet in order to pass the test
 */
public interface Condition {

    /**
     * Execute the test of the condition
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    Result test(JsonNode json);

    /**
     * Describe the condition
     * @return description of the condition
     */
    String describe();

    /**
     * Add a second condition to this one using and logic
     * @param other the other one
     * @return the combined condition
     */
    default Condition and(Condition other) {
        return new AndCondition(this, other);
    }
}
