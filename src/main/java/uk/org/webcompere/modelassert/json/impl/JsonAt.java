package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

public class JsonAt implements Condition {
    private String path;
    private Condition condition;

    public JsonAt(String path, Condition condition) {
        this.path = path;
        this.condition = condition;
    }

    @Override
    public Result test(JsonNode json) {
        JsonNode node = json.at(path);
        return condition.test(node).withExpected(path);
    }

    @Override
    public String describe() {
        return "Path at " + path + " " + condition.describe();
    }
}
