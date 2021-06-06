package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * A size condition - works out the size of the target
 */
public class HasSize implements Condition {
    private int requiredSize;

    public HasSize(int requiredSize) {
        this.requiredSize = requiredSize;
    }

    @SuppressFBWarnings(value = "BC_UNCONFIRMED_CAST",
            justification = "Node type is used in place of instanceof for the switch")
    @Override
    public Result test(JsonNode json) {
        switch (json.getNodeType()) {
          case ARRAY:
              ArrayNode arrayNode = (ArrayNode) json;
              return new Result(describe(), "Array of size " + arrayNode.size(), arrayNode.size() == requiredSize);
          case OBJECT:
              ObjectNode objectNode = (ObjectNode) json;
              return new Result(describe(), "Object of size " + objectNode.size(), objectNode.size() == requiredSize);
          case STRING:
              return new Result(describe(), "Text of length " + json.asText().length(),
                      json.asText().length() == requiredSize);
          default:
              return new Result(describe(), json.getNodeType().toString(), false);
        }
    }

    @Override
    public String describe() {
        return "string, array or object of size " + requiredSize;
    }
}
