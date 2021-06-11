package uk.org.webcompere.modelassert.json.condition.array;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * Convert a normal element condition into an array element condition
 */
public class ArrayElementConditionAdapter implements ArrayElementCondition {
    private Condition condition;

    public ArrayElementConditionAdapter(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Result test(JsonNode json, int arrayIndex) {
        return condition.test(json);
    }

    @Override
    public String describe() {
        return condition.describe();
    }
}
