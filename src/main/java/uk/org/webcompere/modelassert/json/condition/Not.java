package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * Inverts a condition. Create using the {@link Not#not(Condition)} factory method
 */
public class Not implements Condition {
    private static final String NOT_PREFIX = "not";
    private Condition delegate;

    public static Not not(Condition condition) {
        return new Not(condition);
    }

    private Not(Condition delegate) {
        this.delegate = delegate;
    }


    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        return delegate.test(json)
                .invert()
                .withPreCondition(NOT_PREFIX);
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return NOT_PREFIX + " " + delegate.describe();
    }
}
