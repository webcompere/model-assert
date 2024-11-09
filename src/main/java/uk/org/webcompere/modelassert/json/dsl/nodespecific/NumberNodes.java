package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;

/**
 * Reduce the DSL down to just numbers
 * @param <A> the type of assertion
 */
public class NumberNodes<A> extends SubsetDsl<A> implements NumberNodeDsl<A> {
    public NumberNodes(Satisfies<A> satisfies) {
        super(isNumber(satisfies));
    }

    private static <A> Satisfies<A> isNumber(Satisfies<A> requirements) {
        requirements.satisfies(new PredicateWrappedCondition("Number", JsonNode::isNumber));
        return requirements;
    }
}
