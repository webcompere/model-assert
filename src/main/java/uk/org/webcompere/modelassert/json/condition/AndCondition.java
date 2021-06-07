package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

public class AndCondition implements Condition {
    private Condition first;
    private Condition second;

    public AndCondition(Condition first, Condition second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Result test(JsonNode json) {
        Result firstResult = first.test(json);
        if (!firstResult.isPassed()) {
            return firstResult.withNewDescription(describe());
        }
        return second.test(json).withNewDescription(describe());
    }

    @Override
    public String describe() {
        return first.describe() + " and " + second.describe();
    }
}
