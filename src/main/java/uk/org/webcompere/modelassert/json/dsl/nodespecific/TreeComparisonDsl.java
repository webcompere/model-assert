package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.condition.tree.TreeComparisonCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.IsEqualToDsl;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.WhereDsl;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * Assertions comparing one tree with another
 * @param <A> the assertion type
 */
public interface TreeComparisonDsl<A> extends Satisfies<A>, IsEqualToDsl<A> {

    default A isEqualTo(TreeComparisonCondition condition) {
        return satisfies(condition);
    }

    default A isNotEqualTo(TreeComparisonCondition condition) {
        return satisfies(not(condition));
    }

    default WhereDsl<A> where() {
        return new WhereDsl<>(this);
    }
}
