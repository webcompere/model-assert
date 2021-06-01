package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;

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
}
