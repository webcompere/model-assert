package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * This condition always passes, ignoring its input
 */
public class Ignore implements Condition {

    @Override
    public Result test(JsonNode json) {
        return new Result(describe(), "", true);
    }

    @Override
    public String describe() {
        return "Ignored";
    }
}
