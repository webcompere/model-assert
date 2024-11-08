package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.TextNodeDsl;

/**
 * Reduce the DSL down to just string nodes
 * @param <A> the type of assertion
 */
public class TextNodes<A> extends SubsetDsl<A> implements TextNodeDsl<A> {
    public TextNodes(Satisfies<A> satisfies) {
        super(isText(satisfies));
    }

    private static <A> Satisfies<A> isText(Satisfies<A> requirements) {
        requirements.satisfies(new PredicateWrappedCondition("Text", JsonNode::isTextual));
        return requirements;
    }
}
