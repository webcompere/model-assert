package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.Objects;

public class IsEqualToObject implements Condition {
    private Object expected;

    public IsEqualToObject(Object expected) {
        this.expected = expected;
    }

    @Override
    public Result test(JsonNode node) {
        String expectedValue = Objects.toString(expected);
        String was = node.toString();
        if (expected == null && node.isNull()) {
            return new Result(expectedValue, was, true);
        }

        if (expected == null) {
            return new Result(expectedValue, was, false);
        }

        if (expected instanceof Number) {
            if (node.isNumber()) {
                return new Result(expectedValue, was, node.numberValue().equals(expected));
            }
            return new Result(expectedValue, was, false);
        }

        if (expected instanceof String && node.isTextual()) {
            return new Result(expectedValue, was, node.textValue().equals(expected));
        }
        return new Result(expectedValue, was, false);
    }

    @Override
    public String describe() {
        return "is equal to " + expected;
    }
}
