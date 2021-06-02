package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

public class NullCondition implements Condition {
    private static final NullCondition INSTANCE = new NullCondition();

    private NullCondition() {

    }

    /**
     * Access the one and only instance
     * @return the NullCondition instnace
     */
    public static NullCondition getInstance() {
        return INSTANCE;
    }

    @Override
    public Result test(JsonNode json) {
        return new Result("null", json.getNodeType().toString(), json.isNull());
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return "JSON must be null";
    }
}
