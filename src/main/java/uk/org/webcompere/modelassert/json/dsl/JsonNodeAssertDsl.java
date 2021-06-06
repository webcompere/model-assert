package uk.org.webcompere.modelassert.json.dsl;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Matcher;
import uk.org.webcompere.modelassert.json.condition.*;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.*;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import java.util.function.Function;
import java.util.function.Predicate;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * DSLs available on any JsonNode of unknown type. Aggregates all DSLs.
 * @param <T> the type of the JSON source that will be converted by the provider
 * @param <A> the type of the ultimate assertion
 */
public interface JsonNodeAssertDsl<T, A extends CoreJsonAssertion<T, A>>
        extends Satisfies<T, A>, TextNodeDsl<T, A>, NumberNodeDsl<T, A>,
        BooleanNodeDsl<T, A>, ObjectNodeDsl<T, A>, ArrayNodeDsl<T, A> {

    /**
     * Assert that the node has a value is equal to the given object
     * @param expected the expected
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A hasValue(Object expected) {
        return satisfies(new HasValueWithLooseType(expected));
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
     * Add a custom assertion condition
     * @param description the description for the failure message
     * @param customCondition the condition
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A is(String description, Predicate<JsonNode> customCondition) {
        return satisfies(new PredicateWrappedCondition(description, customCondition));
    }

    /**
     * Add a set of custom assertions via a helper method or lambda
     * @param customisedBy the fluent assertions
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A is(Function<JsonNodeAssertDsl<T, A>, A> customisedBy) {
        return customisedBy.apply(this);
    }

    /**
     * Add a custom assertion condition that must be false
     * @param description the description for the failure message
     * @param customCondition the condition
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNot(String description, Predicate<JsonNode> customCondition) {
        return satisfies(not(new PredicateWrappedCondition(description, customCondition)));
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
    default NumberNodes<T, A> number() {
        return new NumberNodes<>(this);
    }

    /**
     * Switch to string context
     * @return a subset of the DSL with just the numeric options
     */
    default TextNodes<T, A> text() {
        return new TextNodes<>(this);
    }

    /**
     * Switch to boolean context
     * @return a subset of the DSL with just the boolean options
     */
    default BooleanNodes<T, A> booleanNode() {
        return new BooleanNodes<>(this);
    }

    /**
     * Switch to array context
     * @return a subset of the DSL with just the boolean options
     */
    default ArrayNodes<T, A> array() {
        return new ArrayNodes<>(this);
    }

    /**
     * Switch to object context
     * @return a subset of the DSL with just the boolean options
     */
    default ObjectNodes<T, A> object() {
        return new ObjectNodes<>(this);
    }

    /**
     * Start adding an {@link JsonAssertDslBuilders.At} expression to the assertion
     * @param path the json path to use
     * @return the {@link JsonAssertDslBuilders.At} to construct the at expression
     */
    default JsonAssertDslBuilders.At<T, A> at(String path) {
        return new JsonAssertDslBuilders.At<>(this, path);
    }

    /**
     * Depending on the type of node, this will detect <em>emptiness</em>
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNotEmpty() {
        return satisfies(not(new IsEmpty()));
    }
}
