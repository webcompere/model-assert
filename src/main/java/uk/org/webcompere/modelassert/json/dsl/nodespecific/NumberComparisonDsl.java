package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.NumberCondition;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import static uk.org.webcompere.modelassert.json.condition.NumberCondition.Comparison.*;

/**
 * DSL assertions specific to arbitrary numbers
 * @param <A> the assertion type
 */
public interface NumberComparisonDsl<A> extends Satisfies<A> {
    /**
     * Assert that the value is a number, meeting an additional condition
     * @param condition the number condition
     * @return the assertion for fluent assertions, with this condition added
     */
    default A satisfiesNumberCondition(Condition condition) {
        return satisfies(new PredicateWrappedCondition("Number", JsonNode::isNumber, condition));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThan(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanOrEqualTo(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, GREATER_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, less than the given number
     * @param number the number to compare with
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThan(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, LESS_THAN));
    }

    /**
     * Assert that the value is a number, less than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanOrEqualTo(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, LESS_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value lies between the limits
     * @param firstInclusive the first one which the value must be greater than or equal to
     * @param lastInclusive the last one which the value must be less than or equal to
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isBetween(Number firstInclusive, Number lastInclusive) {
        return satisfiesNumberCondition(new NumberCondition<>(firstInclusive, GREATER_THAN_OR_EQUAL)
            .and(new NumberCondition<>(lastInclusive, LESS_THAN_OR_EQUAL)));
    }

    /**
     * Assert that the value is a number, equal to 0
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isZero() {
        return satisfiesNumberCondition(new NumberCondition<>(0, EQUAL_TO));
    }
}
