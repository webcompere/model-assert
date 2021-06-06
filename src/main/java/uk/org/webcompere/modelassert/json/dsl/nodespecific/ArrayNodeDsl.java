package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
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
}
