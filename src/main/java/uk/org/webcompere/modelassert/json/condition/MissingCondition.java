package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

public class MissingCondition implements Condition {
    private static final MissingCondition INSTANCE = new MissingCondition();

    private MissingCondition() {

    }

    /**
     * Access the one and only instance
     * @return the MissingCondition instnace
     */
    public static MissingCondition getInstance() {
        return INSTANCE;
    }

    @Override
    public Result test(JsonNode json) {
        return new Result(describe(), json != null ? json.getNodeType().toString() : "null",
            json == null || json.isMissingNode());
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return "missing";
    }
}
