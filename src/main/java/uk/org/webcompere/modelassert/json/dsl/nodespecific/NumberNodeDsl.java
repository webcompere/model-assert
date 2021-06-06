package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.NumberCondition;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import static uk.org.webcompere.modelassert.json.condition.Not.not;
import static uk.org.webcompere.modelassert.json.condition.NumberCondition.Comparison.*;

/**
 * DSL assertions specific to numeric notes
 * @param <A> the assertion type
 */
public interface NumberNodeDsl<A> extends Satisfies<A> {

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
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanInt(int number) {
        return satisfiesNumberCondition(new NumberCondition<>(Integer.class, number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanDouble(double number) {
        return satisfiesNumberCondition(new NumberCondition<>(Double.class, number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanOrEqualToDouble(double number) {
        return satisfiesNumberCondition(new NumberCondition<>(Double.class, number, GREATER_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, greater than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanOrEqualToInt(int number) {
        return satisfiesNumberCondition(new NumberCondition<>(Integer.class, number, GREATER_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanLong(long number) {
        return satisfiesNumberCondition(new NumberCondition<>(Long.class, number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isGreaterThanOrEqualToLong(long number) {
        return satisfiesNumberCondition(new NumberCondition<>(Long.class, number, GREATER_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, less than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanOrEqualToInt(int number) {
        return satisfiesNumberCondition(new NumberCondition<>(Integer.class, number, LESS_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, less than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanOrEqualToLong(long number) {
        return satisfiesNumberCondition(new NumberCondition<>(Long.class, number, LESS_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, less than or equal to
     * @param number the amount
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanOrEqualToDouble(double number) {
        return satisfiesNumberCondition(new NumberCondition<>(Double.class, number, LESS_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, equal to the given number
     * @param number the number to compare with
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNumberEqualTo(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, EQUAL_TO));
    }

    /**
     * Assert that the value is a number, not equal to the given number
     * @param number the number to compare with
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNumberNotEqualTo(Number number) {
        return satisfiesNumberCondition(not(new NumberCondition<>(number, EQUAL_TO)));
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
     * Assert that the value is a number, less than the given number
     * @param number the number to compare with
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanInt(int number) {
        return satisfiesNumberCondition(new NumberCondition<>(Integer.class, number, LESS_THAN));
    }

    /**
     * Assert that the value is a number, less than the given number
     * @param number the number to compare with
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanLong(long number) {
        return satisfiesNumberCondition(new NumberCondition<>(Long.class, number, LESS_THAN));
    }

    /**
     * Assert that the value is a number, less than the given number
     * @param number the number to compare with
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isLessThanDouble(double number) {
        return satisfiesNumberCondition(new NumberCondition<>(Double.class, number, LESS_THAN));
    }
}
