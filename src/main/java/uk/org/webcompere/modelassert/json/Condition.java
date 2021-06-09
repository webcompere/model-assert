package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.AndCondition;
import uk.org.webcompere.modelassert.json.condition.OrCondition;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

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
     * Add a second condition to this one using <em>and</em> logic. Short-circuits
     * so the second condition only evaluated if the first is true
     * @param other the other one
     * @return the combined condition
     */
    default Condition and(Condition other) {
        return new AndCondition(this, other);
    }

    /**
     * Add a second condition to this one using <em>or</em> logic. Short-circuits
     * so the second condition only evaluated if the first is false
     * @param other the other one
     * @return the combined condition
     */
    default Condition or(Condition other) {
        return new OrCondition(this, other);
    }

    /**
     * Invert the condition - i.e. wrap it in a <em>not</em>
     * @return the opposite of the condition
     */
    default Condition inverted() {
        return not(this);
    }
}
