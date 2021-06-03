package uk.org.webcompere.modelassert.json.dsl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.HasValue;
import uk.org.webcompere.modelassert.json.condition.TypedCondition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Assertions for Boolean nodes
 * @param <T> the type of the source json
 * @param <A> the overall type of the assertion
 */
public interface BooleanNodeDsl<T, A extends CoreJsonAssertion<T, A>> extends Satisfies<T, A> {
    /**
     * Assert that the node is boolean true
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isTrue() {
        return satisfies(new TypedCondition("Boolean", JsonNode::isBoolean,
                new HasValue<>(JsonNode::asBoolean, true)));
    }

    /**
     * Assert that the node is boolean false
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isFalse() {
        return satisfies(new TypedCondition("Boolean", JsonNode::isBoolean,
                new HasValue<>(JsonNode::asBoolean, false)));
    }

    /**
     * Assert that the node is boolean
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isBoolean() {
        return satisfies(new TypedCondition("Boolean", JsonNode::isBoolean));
    }
}
