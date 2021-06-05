package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.Objects;
import java.util.function.Function;

/**
 * Specific typed "hasValue" comparison
 * @param <V> the type of value
 */
public class HasValue<V> implements Condition {
    private Function<JsonNode, V> valueExtractor;
    private V expected;

    /**
     * Construct the condition
     * @param valueExtractor how to extract the right sort of value from the {@link JsonNode}
     * @param expected the expected value
     */
    public HasValue(Function<JsonNode, V> valueExtractor, V expected) {
        this.valueExtractor = valueExtractor;
        this.expected = expected;
    }

    @Override
    public Result test(JsonNode json) {
        V extracted = valueExtractor.apply(json);
        return new Result(describe(), extracted.toString(), Objects.equals(expected, extracted));
    }

    @Override
    public String describe() {
        return "equal to " + expected.toString();
    }
}
