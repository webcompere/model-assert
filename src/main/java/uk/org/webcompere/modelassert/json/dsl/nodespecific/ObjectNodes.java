package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.PredicateWrappedCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;

/**
 * Reduce the DSL down to just object
 * @param <A> the type of assertion
 */
public class ObjectNodes<A> extends SubsetDsl<A>
        implements ObjectNodeDsl<A> {
    public ObjectNodes(Satisfies<A> satisfies) {
        super(isObject(satisfies));
    }

    private static <A> Satisfies<A> isObject(Satisfies<A> requirements) {
        requirements.satisfies(new PredicateWrappedCondition("Object", JsonNode::isObject));
        return requirements;
    }
}
