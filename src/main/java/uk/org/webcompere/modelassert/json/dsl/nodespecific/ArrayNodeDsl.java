package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.ArrayContains;
import uk.org.webcompere.modelassert.json.condition.ConditionList;
import uk.org.webcompere.modelassert.json.condition.HasSize;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * Assertions specific to being an array node
 * @param <A> the assertion type
 */
public interface ArrayNodeDsl<A> extends Satisfies<A>, Sizeable<A> {
    /**
     * Assert that the value is an array, meeting an additional condition
     * @param condition the number condition
     * @return the assertion for fluent assertions, with this condition added
     */
    default A satisfiesArrayCondition(Condition condition) {
        return satisfies(new PredicateWrappedCondition("Array", JsonNode::isArray, condition));
    }

    /**
     * Assert that the value is an array
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isArray() {
        return satisfies(new PredicateWrappedCondition("Object", JsonNode::isArray));
    }

    /**
     * Assert that the value is not an array
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotArray() {
        return satisfies(not(new PredicateWrappedCondition("Object", JsonNode::isArray)));
    }

    /**
     * The array contains given values.
     * @param first the first element
     * @param rest the remaining elements
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isArrayContaining(Object first, Object... rest) {
        return satisfiesArrayCondition(ArrayContains.containsValues(first, rest));
    }

    /**
     * The array contains values matching the conditions
     * @param conditions the conditions to match
     * @return the assertion for fluent assertions, with this condition added
     * @see ConditionList
     */
    default A isArrayContaining(ConditionList conditions) {
        return satisfiesArrayCondition(ArrayContains.containsValues(conditions));
    }

    /**
     * The array contains given values, in the given order, with no extra ones
     * @param first the first element
     * @param rest the remaining elements
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isArrayContainingExactly(Object first, Object... rest) {
        return satisfiesArrayCondition(ArrayContains.containsValuesExactly(first, rest));
    }

    /**
     * The array contains given values, in the given order, with no extra ones
     * @param conditions the conditions to match
     * @return the assertion for fluent assertions, with this condition added
     * @see ConditionList
     */
    default A isArrayContainingExactly(ConditionList conditions) {
        return satisfiesArrayCondition(ArrayContains.containsValuesExactly(conditions));
    }

    /**
     * The array contains given values, in any order, with no extra ones
     * @param first the first element
     * @param rest the remaining elements
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isArrayContainingExactlyInAnyOrder(Object first, Object... rest) {
        return satisfiesArrayCondition(new HasSize(1 + rest.length)
            .and(ArrayContains.containsValues(first, rest)));
    }

    /**
     * The array contains values meeting the given conditions, in any order, with no extra ones
     * @param conditions the conditions
     * @return the assertion for fluent assertions, with this condition added
     * @see ConditionList
     */
    default A isArrayContainingExactlyInAnyOrder(ConditionList conditions) {
        return satisfiesArrayCondition(new HasSize(conditions.getConditionList().size())
            .and(ArrayContains.containsValues(conditions)));
    }
}
