package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * Assertions specific to object nodes
 * @param <A> the assertion type
 */
public interface ObjectNodeDsl<A> extends Satisfies<A>, Sizeable<A> {
    /**
     * Assert that the value is an object, meeting an additional condition
     * @param condition the number condition
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A satisfiesObjectCondition(Condition condition) {
        return satisfies(new PredicateWrappedCondition("Object", JsonNode::isObject, condition));
    }

    /**
     * Assert that the value is an object
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isObject() {
        return satisfies(new PredicateWrappedCondition("Object", JsonNode::isObject));
    }

    /**
     * Assert that the value is not an object
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNotObject() {
        return satisfies(not(new PredicateWrappedCondition("Object", JsonNode::isObject)));
    }
}
