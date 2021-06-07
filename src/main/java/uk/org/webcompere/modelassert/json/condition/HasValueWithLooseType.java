package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * Performs the hasValue operation with flexibility and type flexibility. It works
 * out the likely type of node from the input.
 */
public class HasValueWithLooseType implements Condition {
    private Object expected;

    /**
     * Construct with the expected value
     * @param expected the value expected
     */
    public HasValueWithLooseType(Object expected) {
        this.expected = expected;
    }

    @Override
    public Result test(JsonNode node) {
        String was = node.toString();
        if (expected == null && node.isNull()) {
            return new Result(describe(), was, true);
        }

        if (expected == null) {
            return new Result(describe(), was, false);
        }

        if (expected instanceof Number) {
            if (node.isNumber()) {
                return new Result(describe(), was, node.numberValue().equals(expected));
            }
            return new Result(describe(), was, false);
        }

        if (expected instanceof String && node.isTextual()) {
            return new Result(describe(), was, node.textValue().equals(expected));
        }

        if (expected instanceof Boolean && node.isBoolean()) {
            return new Result(describe(), was, node.booleanValue() == (Boolean)expected);
        }

        return new Result(describe(), was, false);
    }

    @Override
    public String describe() {
        return "is equal to " + expected;
    }
}
