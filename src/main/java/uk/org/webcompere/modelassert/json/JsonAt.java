package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonAt implements Comparison {
    private String path;
    private Object expected;

    public JsonAt(String path, Object expected) {
        this.path = path;
        this.expected = expected;
    }

    @Override
    public Result test(JsonNode json) {
        String expectedPath = path;
        JsonNode node = json.at(path);
        String was = node.toString();
        if (expected == null && node.isNull()) {
            return new Result(expectedPath, was, true);
        }

        if (expected == null) {
            return new Result(expectedPath, was, false);
        }

        if (expected instanceof Number) {
            if (node.isNumber()) {
                return new Result(expectedPath, was, node.numberValue().equals(expected));
            }
            return new Result(expectedPath, was, false);
        }

        if (expected instanceof String && node.isTextual()) {
            return new Result(expectedPath, was, node.textValue().equals(expected));
        }
        return new Result(expectedPath, was, false);
    }

    @Override
    public String describe() {
        return "Path at " + path + " is equal to " + expected;
    }
}
