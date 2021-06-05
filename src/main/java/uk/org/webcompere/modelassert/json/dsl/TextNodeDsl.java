package uk.org.webcompere.modelassert.json.dsl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.HasValue;
import uk.org.webcompere.modelassert.json.condition.MatchesCondition;
import uk.org.webcompere.modelassert.json.condition.TypedCondition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import java.util.regex.Pattern;

public interface TextNodeDsl<T, A extends CoreJsonAssertion<T, A>> extends Satisfies<T, A> {
    /**
     * Assert that the value is text, meeting an additional condition
     * @param condition the number condition
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A satisfiesTextCondition(Condition condition) {
        return satisfies(new TypedCondition("Text", JsonNode::isTextual, condition));
    }

    /**
     * Assert that the node is text matching a regular expression
     * @param regex the expression
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A matches(Pattern regex) {
        return satisfiesTextCondition(new MatchesCondition(regex));
    }

    /**
     * Assert that the text matches a regular expression
     * @param regex the expression
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A matches(String regex) {
        return satisfiesTextCondition(new MatchesCondition(Pattern.compile(regex)));
    }

    /**
     * Assert that the node is a text node
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isText() {
        return satisfies(new TypedCondition("Text", JsonNode::isTextual));
    }

    /**
     * Assert that the node is a text node with a given value
     * @param text the expected value
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isText(String text) {
        return satisfiesTextCondition(new HasValue<>(JsonNode::asText, text));
    }
}
