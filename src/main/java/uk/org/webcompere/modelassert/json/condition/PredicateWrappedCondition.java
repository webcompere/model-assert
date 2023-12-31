package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A condition which checks a predicate for the node and then delegates to a further condition.
 * This can be used without a further condition, just to test the node type
 */
public class PredicateWrappedCondition implements Condition {
    private Optional<Condition> delegate;
    private Predicate<JsonNode> typeDetector;
    private String typeName;

    /**
     * Construct the condition
     * @param typeName the name of the type for description
     * @param typeDetector detects if the node's the right type
     */
    public PredicateWrappedCondition(String typeName, Predicate<JsonNode> typeDetector) {
        this.typeDetector = typeDetector;
        this.delegate = Optional.empty();
        this.typeName = typeName;
    }

    /**
     * Construct the condition
     * @param typeName the name of the type for description
     * @param typeDetector detects if the node's the right type
     * @param delegate the condition to hand over to, if the node's of the correct type
     */
    public PredicateWrappedCondition(String typeName, Predicate<JsonNode> typeDetector, Condition delegate) {
        this.delegate = Optional.ofNullable(delegate);
        this.typeDetector = typeDetector;
        this.typeName = typeName;
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        if (json == null) {
            return new Result(describe(), "<null>", false);
        }
        if (!typeDetector.test(json)) {
            return new Result(describe(), json.getNodeType().toString(), false);
        }
        return delegate.map(del -> del.test(json))
                .orElse(new Result(describe(), json.getNodeType().toString(), true));
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return typeName + " node" + delegate.map(del -> " " + del.describe()).orElse("");
    }
}
