package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.HasValue;
import uk.org.webcompere.modelassert.json.condition.IsEmpty;
import uk.org.webcompere.modelassert.json.condition.MatchesCondition;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import java.util.regex.Pattern;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * Test node specific assertions
 * @param <A> the final assertion
 */
public interface TextNodeDsl<A> extends Satisfies<A>, Sizeable<A> {
    /**
     * Assert that the value is text, meeting an additional condition
     * @param condition the number condition
     * @return the assertion for fluent assertions, with this condition added
     */
    default A satisfiesTextCondition(Condition condition) {
        return satisfies(new PredicateWrappedCondition("Text", JsonNode::isTextual, condition));
    }

    /**
     * Assert that the node is text matching a regular expression
     * @param regex the expression
     * @return the assertion for fluent assertions, with this condition added
     */
    default A matches(Pattern regex) {
        return satisfiesTextCondition(new MatchesCondition(regex));
    }

    /**
     * Assert that the text matches a regular expression
     * @param regex the expression
     * @return the assertion for fluent assertions, with this condition added
     */
    default A matches(String regex) {
        return satisfiesTextCondition(new MatchesCondition(Pattern.compile(regex)));
    }

    /**
     * Assert that the node is a text node
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isText() {
        return satisfies(new PredicateWrappedCondition("Text", JsonNode::isTextual));
    }

    /**
     * Assert that the node is a text node with a given value
     * @param text the expected value
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isText(String text) {
        return satisfiesTextCondition(new HasValue<>(JsonNode::asText, text));
    }

    /**
     * Assert that the node is not a text node
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotText() {
        return satisfies(not(new PredicateWrappedCondition("Text", JsonNode::isTextual)));
    }

    /**
     * Assert that the node is text and empty
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isEmptyText() {
        return satisfiesTextCondition(new IsEmpty());
    }

    /**
     * Assert that the node <em>is text</em> and is not empty
     * @return the assertion for fluent assertions, with this condition added
     */
    default A isNotEmptyText() {
        return satisfiesTextCondition(not(new IsEmpty()));
    }
}
