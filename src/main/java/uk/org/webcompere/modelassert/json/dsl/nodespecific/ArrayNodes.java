package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;

/**
 * Reduce the DSL down to just arrays
 * @param <A> the type of assertion
 */
public class ArrayNodes<A> extends SubsetDsl<A> implements ArrayNodeDsl<A> {
    public ArrayNodes(Satisfies<A> satisfies) {
        super(isArray(satisfies));
    }


    private static <A> Satisfies<A> isArray(Satisfies<A> requirements) {
        requirements.satisfies(new PredicateWrappedCondition("Array", JsonNode::isArray));
        return requirements;
    }
}
