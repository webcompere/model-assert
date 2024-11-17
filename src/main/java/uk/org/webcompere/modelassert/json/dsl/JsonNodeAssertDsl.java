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
 * @param <A> the type of the ultimate assertion
 */
public interface JsonNodeAssertDsl<A>
        extends Satisfies<A>, TextNodeDsl<A>, NumberNodeDsl<A>,
        BooleanNodeDsl<A>, ObjectNodeDsl<A>, ArrayNodeDsl<A>, TreeComparisonDsl<A> {

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
     * A missing node is not anything, everything else is
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isAnyNode() {
        return isNotMissing();
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
    default A is(Function<JsonNodeAssertDsl<A>, A> customisedBy) {
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
    default NumberNodes<A> number() {
        return new NumberNodes<>(this);
    }

    /**
     * Switch to string context
     * @return a subset of the DSL with just the text options
     */
    default TextNodes<A> text() {
        return new TextNodes<>(this);
    }

    /**
     * Switch to boolean context
     * @return a subset of the DSL with just the boolean options
     */
    default BooleanNodes<A> booleanNode() {
        return new BooleanNodes<>(this);
    }

    /**
     * Switch to array context
     * @return a subset of the DSL with just the array options
     */
    default ArrayNodes<A> array() {
        return new ArrayNodes<>(this);
    }

    /**
     * Switch to object context
     * @return a subset of the DSL with just the object options
     */
    default ObjectNodes<A> object() {
        return new ObjectNodes<>(this);
    }

    /**
     * Start adding an {@link JsonAssertDslBuilders.At} expression to the assertion
     * @param path the json path to use
     * @return the {@link JsonAssertDslBuilders.At} to construct the at expression
     */
    default JsonAssertDslBuilders.At<A> at(String path) {
        return new JsonAssertDslBuilders.At<>(this, path);
    }

    /**
     * Depending on the type of node, this will detect the opposite of <em>emptiness</em>. For something
     * to be not empty, it needs to be truthy AND it needs to have contents if it's something that contains things. <br>
     * <em>Warning!</em> this is best used prefixed with a type assertion so know we have
     * a {@link Sizeable} item in the node. E.g. <pre>assertJson(json).at("/field").array().isNotEmpty();</pre> if
     * used without the type assertion, then this is quite a vague assertion.
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     * @deprecated use a type assertion such as <code>.text()</code> before this to avoid confusion
     */
    @Deprecated
    default A isNotEmpty() {
        return satisfies(not(MissingCondition.getInstance())
            .and(not(NullCondition.getInstance()))
            .and(not(new IsEmpty())));
    }

    /**
     * Assert that the node is boolean
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isBoolean() {
        return satisfies(new PredicateWrappedCondition("Boolean", JsonNode::isBoolean));
    }

    /**
     * Assert that the node is not boolean
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotBoolean() {
        return satisfies(not(new PredicateWrappedCondition("Boolean", JsonNode::isBoolean)));
    }

    /**
     * Assert that the node is not a number node
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotNumber() {
        return satisfies(not(new PredicateWrappedCondition("Number", JsonNode::isNumber)));
    }

    /**
     * Assert that the node is a number node
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNumber() {
        return satisfies(new PredicateWrappedCondition("Number", JsonNode::isNumber));
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
     * Assert that the node is a text node
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isText() {
        return satisfies(new PredicateWrappedCondition("Text", JsonNode::isTextual));
    }

    /**
     * Assert that the node is not a text node
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotText() {
        return satisfies(not(new PredicateWrappedCondition("Text", JsonNode::isTextual)));
    }
}
