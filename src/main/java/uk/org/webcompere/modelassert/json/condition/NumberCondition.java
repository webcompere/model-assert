package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.Arrays;

/**
 * Numeric comparisons
 * @param <N> the type of number being compared with
 */
public class NumberCondition<N extends Number> implements Condition {
    public enum Comparison {
        NONE("correct type", new Comparison[0]),
        EQUAL_TO("equal to", new Comparison[0]),
        GREATER_THAN("greater than", new Comparison[0]),
        GREATER_THAN_OR_EQUAL("greater than or equal to", new Comparison[] { GREATER_THAN, EQUAL_TO }),
        LESS_THAN("less than", new Comparison[0]),
        LESS_THAN_OR_EQUAL("less than or equal to", new Comparison[] { LESS_THAN, EQUAL_TO });

        private String name;
        private Comparison[] alternatives;

        Comparison(String name, Comparison[] alternatives) {
            this.name = name;
            this.alternatives = alternatives;
        }
    }

    private Comparison comparison;
    private N expected;
    private Class<? extends Number> requiredType;

    /**
     * Construct a number condition for any numeric type
     * @param expected the expected value
     * @param comparison the comparison to be true
     */
    public NumberCondition(N expected, Comparison comparison) {
        this.comparison = comparison;
        this.requiredType = Number.class;
        this.expected = expected;
    }

    /**
     * Construct a number condition for a specific numeric type
     * @param requiredType the type of number the value must be
     * @param expected the expected value
     * @param comparison the comparison to be true
     */
    public NumberCondition(Class<N> requiredType, N expected, Comparison comparison) {
        this.comparison = comparison;
        this.expected = expected;
        this.requiredType = requiredType;
    }

    @Override
    public Result test(JsonNode json) {
        return new Result(describe(), json.asText(), nodePasses(json));
    }

    private boolean nodePasses(JsonNode json) {
        return passesTypeTest(json) && passesNumericTest(json);
    }

    private boolean passesTypeTest(JsonNode json) {
        if (comparison == Comparison.NONE) {
            if (requiredType.equals(Integer.class)) {
                return json.isInt();
            }
            if (requiredType.equals(Long.class)) {
                return json.isLong();
            }
        }
        if (requiredType.equals(Integer.class)) {
            return json.canConvertToInt();
        }
        if (requiredType.equals(Long.class)) {
            return json.canConvertToLong();
        }
        if (requiredType.equals(Double.class)) {
            return json.isDouble();
        }
        return json.isNumber();
    }

    private boolean passesNumericTest(JsonNode json) {
        if (comparison == Comparison.NONE) {
            return true;
        }
        Comparison ordering = getNumericComparison(json);
        return comparison == ordering || Arrays.stream(comparison.alternatives).anyMatch(ordering::equals);
    }

    private Comparison getNumericComparison(JsonNode json) {
        if (json.isDouble() || requiredType.equals(Double.class)) {
            return doubleComparison(json.asDouble());
        }
        if (json.isLong() || requiredType.equals(Long.class)) {
            return longComparison(json.asLong());
        }
        return intComparison(json.asInt());
    }

    private Comparison doubleComparison(double asDouble) {
        double expectedDouble = expected.doubleValue();
        if (asDouble == expectedDouble) {
            return Comparison.EQUAL_TO;
        }
        if (asDouble > expectedDouble) {
            return Comparison.GREATER_THAN;
        }
        return Comparison.LESS_THAN;
    }

    private Comparison longComparison(long asLong) {
        long expectedLong = expected.longValue();
        if (asLong == expectedLong) {
            return Comparison.EQUAL_TO;
        }
        if (asLong > expectedLong) {
            return Comparison.GREATER_THAN;
        }
        return Comparison.LESS_THAN;
    }

    private Comparison intComparison(long asInt) {
        int expectedInt = expected.intValue();
        if (asInt == expectedInt) {
            return Comparison.EQUAL_TO;
        }
        if (asInt > expectedInt) {
            return Comparison.GREATER_THAN;
        }
        return Comparison.LESS_THAN;
    }

    @Override
    public String describe() {
        return "is " + requiredType.getTypeName() + " " + comparison.name + " " + expected;
    }
}
