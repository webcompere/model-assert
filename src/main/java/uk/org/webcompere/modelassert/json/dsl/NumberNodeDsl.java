package uk.org.webcompere.modelassert.json.dsl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.NumberCondition;
import uk.org.webcompere.modelassert.json.condition.TypedCondition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import static uk.org.webcompere.modelassert.json.condition.Not.not;
import static uk.org.webcompere.modelassert.json.condition.NumberCondition.Comparison.*;

public interface NumberNodeDsl<T, A extends CoreJsonAssertion<T, A>> extends Satisfies<T, A> {

    /**
     * Assert that the value is a number, meeting an additional condition
     * @param condition the number condition
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A satisfiesNumberCondition(Condition condition) {
        return satisfies(new TypedCondition("Number", JsonNode::isNumber, condition));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isGreaterThan(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isGreaterThanInt(int number) {
        return satisfiesNumberCondition(new NumberCondition<>(Integer.class, number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isGreaterThanOrEqualToInt(int number) {
        return satisfiesNumberCondition(new NumberCondition<>(Integer.class, number, GREATER_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isGreaterThanLong(long number) {
        return satisfiesNumberCondition(new NumberCondition<>(Long.class, number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, greater than or equal to
     * @param number the amount
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isGreaterThanOrEqualToLong(long number) {
        return satisfiesNumberCondition(new NumberCondition<>(Long.class, number, GREATER_THAN_OR_EQUAL));
    }

    /**
     * Assert that the value is a number, greater than
     * @param number the amount
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isGreaterThanDouble(double number) {
        return satisfiesNumberCondition(new NumberCondition<>(Double.class, number, GREATER_THAN));
    }

    /**
     * Assert that the value is a number, equal to the given number
     * @param number the number to compare with
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNumberEqualTo(Number number) {
        return satisfiesNumberCondition(new NumberCondition<>(number, EQUAL_TO));
    }

    /**
     * Assert that the value is a number, not equal to the given number
     * @param number the number to compare with
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNumberNotEqualTo(Number number) {
        return satisfiesNumberCondition(not(new NumberCondition<>(number, EQUAL_TO)));
    }
}
