package uk.org.webcompere.modelassert.json.dsl;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Matcher;
import uk.org.webcompere.modelassert.json.condition.HasValueWithCoercion;
import uk.org.webcompere.modelassert.json.condition.MatcherCondition;
import uk.org.webcompere.modelassert.json.condition.MissingCondition;
import uk.org.webcompere.modelassert.json.condition.NullCondition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * DSLs available on any JsonNode of unknown type. Aggregates all DSLs.
 * @param <T> the type of the JSON source that will be converted by the provider
 * @param <A> the type of the ultimate assertion
 */
public interface JsonNodeAssertDsl<T, A extends CoreJsonAssertion<T, A>>
        extends Satisfies<T, A>, TextNodeDsl<T, A>, NumberNodeDsl<T, A>, BooleanNodeDsl<T, A> {

    /**
     * Assert that the node has a value is equal to the given object
     * @param expected the expected
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A hasValue(Object expected) {
        return satisfies(new HasValueWithCoercion(expected));
    }

    /**
     * Assert that the path is null
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNull() {
        return satisfies(NullCondition.getInstance());
    }

    /**
     * Assert that the path is not null
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNotNull() {
        return satisfies(not(NullCondition.getInstance()));
    }

    /**
     * Assert that the path is missing
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isMissing() {
        return satisfies(MissingCondition.getInstance());
    }

    /**
     * Assert that the path is not missing
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNotMissing() {
        return satisfies(not(MissingCondition.getInstance()));
    }

    /**
     * Assert that the node matches a Hamcrest Matcher for
     * {@link JsonNode}
     * @param matcher the matcher
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A matches(Matcher<JsonNode> matcher) {
        return satisfies(new MatcherCondition(matcher));
    }

    /**
     * Switch to number context
     * @return a subset of the DSL with just the numeric options
     */
    default Numbers<T, A> number() {
        return new Numbers<>(this);
    }

    /**
     * Switch to string context
     * @return a subset of the DSL with just the numeric options
     */
    default Strings<T, A> text() {
        return new Strings<>(this);
    }
}
