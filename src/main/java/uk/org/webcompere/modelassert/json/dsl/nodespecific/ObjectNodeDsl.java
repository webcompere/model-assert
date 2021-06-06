package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.HasSize;
import uk.org.webcompere.modelassert.json.condition.ObjectContainsKeys;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * Assertions specific to object nodes
 * @param <A> the assertion type
 */
public interface ObjectNodeDsl<A> extends Satisfies<A>, Sizeable<A> {
    /**
     * Assert that the value is an object, meeting an additional condition
     * @param condition the number condition
     * @return the assertion for fluent assertions, with this condition added
     */
    default A satisfiesObjectCondition(Condition condition) {
        return satisfies(new PredicateWrappedCondition("Object", JsonNode::isObject, condition));
    }

    /**
     * Assert that the value is an object
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isObject() {
        return satisfies(new PredicateWrappedCondition("Object", JsonNode::isObject));
    }

    /**
     * Assert that the value is not an object
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotObject() {
        return satisfies(not(new PredicateWrappedCondition("Object", JsonNode::isObject)));
    }

    /**
     * Assert that the value contains a key/field
     * @param key the key to search for
     * @return the assertion for fluent assertions, with this condition added
     */
    default A containsKey(String key) {
        return satisfiesObjectCondition(new ObjectContainsKeys(key));
    }

    /**
     * Assert that the value does not contain a key/field
     * @param key the key to search for
     * @return the assertion for fluent assertions, with this condition added
     */
    default A doesNotContainKey(String key) {
        return satisfiesObjectCondition(not(new ObjectContainsKeys(key)));
    }

    /**
     * Assert that the value contains multiple keys/fields
     * @param key the first key to search for
     * @param keys the remaining keys to search for
     * @return the assertion for fluent assertions, with this condition added
     */
    default A containsKeys(String key, String... keys) {
        return satisfiesObjectCondition(new ObjectContainsKeys(key, keys));
    }

    /**
     * Assert that the value does not contain multiple keys/fields
     * @param key the first key to search for
     * @param keys the remaining keys to search for
     * @return the assertion for fluent assertions, with this condition added
     */
    default A doesNotContainKeys(String key, String... keys) {
        return satisfiesObjectCondition(not(new ObjectContainsKeys(key, keys)));
    }

    /**
     * Assert that the value contains multiple keys/fields, no more, no less, order maintained.
     * This should be used instead of {@link ObjectNodeDsl#containsKey(String)} to mean
     * <em>contains only the key</em>.
     * @param key the first key to search for
     * @param keys the remaining keys to search for
     * @return the assertion for fluent assertions, with this condition added
     */
    default A containsKeysExactly(String key, String... keys) {
        return satisfiesObjectCondition(new ObjectContainsKeys(true, key, keys));
    }

    /**
     * Assert that the value contains multiple keys/fields, no more, no less, any order
     * @param key the first key to search for
     * @param keys the remaining keys to search for
     * @return the assertion for fluent assertions, with this condition added
     */
    default A containsKeysExactlyInAnyOrder(String key, String... keys) {
        return satisfiesObjectCondition(new HasSize(1 + keys.length)
            .and(new ObjectContainsKeys(key, keys)));
    }
}
