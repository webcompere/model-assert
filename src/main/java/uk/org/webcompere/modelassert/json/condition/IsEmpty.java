package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

/**
 * Detect whether a node is present, but empty
 */
public class IsEmpty implements Condition {

    @SuppressFBWarnings(value = "BC_UNCONFIRMED_CAST",
            justification = "Node type is used in place of instanceof for the switch")
    @Override
    public Result test(JsonNode json) {
        switch (json.getNodeType()) {
          case ARRAY:
              ArrayNode arrayNode = (ArrayNode) json;
              return new Result(describe(), "Array of size " + arrayNode.size(), arrayNode.size() == 0);
          case OBJECT:
              ObjectNode objectNode = (ObjectNode) json;
              return new Result(describe(), "Object of size " + objectNode.size(), objectNode.size() == 0);
          case STRING:
              return new Result(describe(), json.asText(), json.asText().isEmpty());
          default:
              return new Result(describe(), json.getNodeType().toString(), false);
        }
    }

    @Override
    public String describe() {
        return "empty string, array or object";
    }
}
