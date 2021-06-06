package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * An object node contains given keys/field
 */
public class ObjectContainsKeys implements Condition {
    private boolean strictOrder;
    private List<String> keys;

    /**
     * Construct with the keys to find - a varargs with a minimum of 1 argument
     * @param key the first key
     * @param keys additional keys
     */
    public ObjectContainsKeys(String key, String... keys) {
        this(false, key, keys);
    }

    /**
     * Construct with the keys to find - a varargs with a minimum of 1 argument
     * @param strictOrder whether the order must be maintained between the key list and the loaded JSON
     * @param key the first key
     * @param keys additional keys
     */
    public ObjectContainsKeys(boolean strictOrder, String key, String... keys) {
        this.keys = Stream.concat(Stream.of(key), Arrays.stream(keys)).collect(toCollection(LinkedList::new));
        this.strictOrder = strictOrder;
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        if (!json.isObject() || !(json instanceof ObjectNode)) {
            return new Result(describe(), json.getNodeType().toString(), false);
        }
        ObjectNode objectNode = (ObjectNode) json;
        List<String> fieldNames = new LinkedList<>();
        objectNode.fieldNames().forEachRemaining(fieldNames::add);

        if (strictOrder) {
            return new Result(describe(), fieldNames.toString(), keys.equals(fieldNames));
        }

        return new Result(describe(), fieldNames.toString(),
            keys.stream().allMatch(key -> objectNode.get(key) != null));
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        if (keys.size() == 1) {
            return "object contains " + (strictOrder ? " only " : "") +
                "key '" + keys.get(0) + "'";
        }
        return "object contains keys " + keys + (strictOrder ? " in exact order" : "");
    }
}
