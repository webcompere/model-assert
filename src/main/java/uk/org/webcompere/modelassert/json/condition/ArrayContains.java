package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.condition.array.LooseComparison;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Array content conditions
 */
public class ArrayContains implements Condition {
    private String description;
    private List<Condition> arrayElementConditions;
    private boolean requireStrict;

    /**
     * Construct the array contains condition
     * @param description the name to use - as simple as possible
     * @param arrayElementConditions the per element conditions
     * @param requireStrict whether the conditions are executed in strict order
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ArrayContains(String description, List<Condition> arrayElementConditions, boolean requireStrict) {
        this.description = description;
        this.arrayElementConditions = arrayElementConditions;
        this.requireStrict = requireStrict;
    }

    /**
     * Create the condition to match the provided values in the array
     * @param first first value
     * @param rest remaining values
     * @return the {@link ArrayContains} to find these values within the array
     */
    public static ArrayContains containsValues(Object first, Object... rest) {
        Object[] remainder = correctRestForNulls(rest);
        return new ArrayContains(describe(first, remainder),
            toConditions(first, remainder), false);
    }

    /**
     * Create the condition to match the provided conditions in the array
     * @param conditions the conditions
     * @return the {@link ArrayContains} to find elements matching these conditions
     */
    public static ArrayContains containsValues(ConditionList conditions) {
        if (conditions == null) {
            // accidental leak into ConditionList overload when the caller must have meant a null value
            return containsValues((Object)null);
        }
        return new ArrayContains(describe(conditions),
            conditions.getConditionList(), false);
    }

    /**
     * Create the condition to match the provided values in the array in exact order
     * @param first first value
     * @param rest remaining values
     * @return the {@link ArrayContains} to match the array with these values
     */
    public static ArrayContains containsValuesExactly(Object first, Object... rest) {
        Object[] remainder = correctRestForNulls(rest);
        return new ArrayContains(describe(first, remainder),
            toConditions(first, remainder), true);
    }

    /**
     * Create the condition to match the provided condition list in the array in exact order
     * @param conditions conditions
     * @return the {@link ArrayContains} to match the array with these conditions
     */
    public static ArrayContains containsValuesExactly(ConditionList conditions) {
        return new ArrayContains(describe(conditions),
            conditions.getConditionList(), true);
    }

    private static Object[] correctRestForNulls(Object[] rest) {
        if (rest != null) {
            return rest;
        }
        return new Object[] { null };
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        // must be an array node
        if (!json.isArray() || !(json instanceof ArrayNode)) {
            return new Result(describe(), json.getNodeType().toString(), false);
        }

        ArrayNode arrayNode = (ArrayNode) json;
        if (requireStrict) {
            return strictComparison(arrayNode);
        }
        return looseComparison(arrayNode);
    }

    private Result looseComparison(ArrayNode arrayNode) {
        return LooseComparison.fromConditions(arrayElementConditions, () -> description)
            .looseComparison(arrayNode);
    }

    private Result strictComparison(ArrayNode arrayNode) {
        if (arrayNode.size() != arrayElementConditions.size()) {
            return new Result(describe(), "different number of elements: found " + arrayNode.size() +
                " rather than expected " + arrayElementConditions.size(), false);
        }

        Result[] results = new Result[arrayElementConditions.size()];
        for (int i = 0; i < arrayElementConditions.size(); i++) {
            results[i] = arrayElementConditions.get(i).test(arrayNode.get(i));
        }

        if (Arrays.stream(results).allMatch(Result::isPassed)) {
            return new Result(describe(), "all matched", true);
        }

        return new Result(describe(), explainFailures(results), false);
    }

    private String explainFailures(Result[] results) {
        return IntStream.range(0, results.length)
            .filter(index -> !results[index].isPassed())
            .mapToObj(index -> "Index " + index + " expected " +
                results[index].getCondition() + " but was " + results[index].getWas())
            .collect(joining("\n"));
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return description + (requireStrict ? " in exact order" : "");
    }

    private static String describe(Object first, Object... rest) {
        return "Array with values " + Stream.concat(Stream.of(first), Arrays.stream(rest))
            .map(Objects::toString)
            .collect(toList());
    }

    private static String describe(ConditionList conditionList) {
        return "Array where:\n" + IntStream.range(0, conditionList.getConditionList().size())
            .mapToObj(idx -> "Index " + idx + ": " + conditionList.getConditionList().get(idx).describe())
            .collect(joining("\n"));
    }

    private static List<Condition> toConditions(Object first, Object... rest) {
        return Stream.concat(Stream.of(first), Arrays.stream(rest))
            .map(HasValueWithLooseType::new)
            .collect(toList());
    }
}
