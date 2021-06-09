package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.NumberComparisonDsl;

/**
 * A size condition - works out the size of the target
 */
public class HasSize implements Condition {
    private Condition sizeCondition;

    public HasSize(int requiredSize) {
        this.sizeCondition = new NumberCondition<>(requiredSize, NumberCondition.Comparison.EQUAL_TO);
    }

    private HasSize(Condition sizeCondition) {
        this.sizeCondition = sizeCondition;
    }

    /**
     * Switch to <em>Size Condition</em> building context to allow the size to be spec
     * @param parentAssertion the parent assertion to resolve to, when the size is provided
     * @param <A> the type of resulting assertion
     * @return a {@link NumberComparisonDsl} for stipulating the size
     */
    public static <A> NumberComparisonDsl<A> sizeOf(Satisfies<A> parentAssertion) {
        return condition -> parentAssertion.satisfies(new HasSize(condition));
    }

    @SuppressFBWarnings(value = "BC_UNCONFIRMED_CAST",
            justification = "Node type is used in place of instanceof for the switch")
    @Override
    public Result test(JsonNode json) {
        switch (json.getNodeType()) {
          case ARRAY:
              ArrayNode arrayNode = (ArrayNode) json;
              return new Result(describe(), "Array of size " + arrayNode.size(), testNumber(arrayNode.size()));
          case OBJECT:
              ObjectNode objectNode = (ObjectNode) json;
              return new Result(describe(), "Object of size " + objectNode.size(), testNumber(objectNode.size()));
          case STRING:
              return new Result(describe(), "Text of length " + json.asText().length(),
                  testNumber(json.asText().length()));
          default:
              return new Result(describe(), json.getNodeType().toString(), false);
        }
    }

    private boolean testNumber(int number) {
        return sizeCondition.test(new IntNode(number)).isPassed();
    }

    @Override
    public String describe() {
        return "string, array or object where size " + sizeCondition.describe();
    }
}
