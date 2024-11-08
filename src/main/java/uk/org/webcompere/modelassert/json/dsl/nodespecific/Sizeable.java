package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.condition.HasSize;
import uk.org.webcompere.modelassert.json.condition.IsEmpty;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

public interface Sizeable<A> extends Satisfies<A> {
    /**
     * Depending on the type of node, this will detect <em>emptiness</em>.<br>
     * <em>Warning!</em> this is best used prefixed with a type assertion so know we have
     * a {@link Sizeable} item in the node. E.g. <pre>assertJson(json).at("/field").array().isEmpty();</pre>
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isEmpty() {
        return satisfies(new IsEmpty());
    }

    /**
     * Depending on the type of node, this will detect the opposite of <em>emptiness</em>. At this level it
     * is specifically the inverse of an empty collection, object or string.
     * <em>Warning!</em> this is best used prefixed with a type assertion so know we have
     * a {@link Sizeable} item in the node. E.g. <pre>assertJson(json).at("/field").array().isNotEmpty();</pre>
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isNotEmpty() {
        return satisfies(not(new IsEmpty()));
    }

    /**
     * Depending on the type of node, this will detect <em>size</em>
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A hasSize(int expected) {
        return satisfies(new HasSize(expected));
    }

    /**
     * Requiring this node to be a sizeable node, this switches to {@link NumberComparisonDsl}
     * to allow criteria to be specified for the size.
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default NumberComparisonDsl<A> size() {
        return HasSize.sizeOf(this);
    }
}
