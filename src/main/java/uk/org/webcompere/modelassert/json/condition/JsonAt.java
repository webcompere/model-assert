package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * The assertion of some JSON at a node within the tree
 */
public class JsonAt implements Condition {
    private String path;
    private Condition condition;

    /**
     * Construct with the location in the tree to <code>at</code>
     * along with the condition to meet
     * @param path the path in the tree - JSON Pointer
     * @param condition the {@link Condition} to satisfy
     */
    public JsonAt(String path, Condition condition) {
        this.path = path;
        this.condition = condition;
    }

    @Override
    public Result test(JsonNode json) {
        JsonNode node = json.at(path);
        return condition.test(node).withPreCondition(path);
    }

    @Override
    public String describe() {
        return "Path at " + path + " " + condition.describe();
    }
}
