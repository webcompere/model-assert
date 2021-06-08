package uk.org.webcompere.modelassert.json.dsl.nodespecific.tree;

import uk.org.webcompere.modelassert.json.condition.tree.PathRule;
import uk.org.webcompere.modelassert.json.condition.tree.TreeComparisonCondition;
import uk.org.webcompere.modelassert.json.condition.tree.TreeRule;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * The DSL for the <em>Where Context</em>
 * @param <A> the type of assertion this belongs to
 */
public class WhereDsl<A> implements IsEqualToDsl<A> {
    private Satisfies<A> coreAssertion;
    private List<PathRule> rules = new LinkedList<>();

    /**
     * Construct with the assertion to return to
     * @param coreAssertion the core assertion, as expressed by its {@link Satisfies} entry point
     */
    public WhereDsl(Satisfies<A> coreAssertion) {
        this.coreAssertion = coreAssertion;
    }

    /**
     * Relax the key ordering constraint for everywhere
     * @return <code>this</code> for fluent calling
     */
    public WhereDsl<A> keysInAnyOrder() {
        return pathRule(new PathRule(TreeRule.IGNORE_KEY_ORDER));
    }

    /**
     * Add a path rule to this, and return this
     * @param pathRule the rule
     * @return <code>this</code> for fluent calling
     */
    WhereDsl<A> pathRule(PathRule pathRule) {
        rules.add(pathRule);
        return this;
    }

    /**
     * Enter the path context - specialising a rule for the given path
     * @param pathStart the first String, {@link PathWildCard} or {@link Pattern}
     * @param pathRemainder the remaining String, {@link PathWildCard} or {@link Pattern}s
     * @return the {@link PathDsl} to complete specialising what to do at that path instead of the defaults
     */
    public PathDsl<A> path(Object pathStart, Object... pathRemainder) {
        return new PathDsl<>(this, pathStart, pathRemainder);
    }

    /**
     * Provide a path using the JSON Pointer syntax - i.e. no wildcards or regular expressions used
     * @param jsonPointer the json pointer expression
     * @return the {@link PathDsl} to complete specialising what to do at that path instead of the defaults
     */
    public PathDsl<A> at(String jsonPointer) {
        return PathDsl.fromJsonPointer(this, jsonPointer);
    }

    @Override
    public A isEqualTo(TreeComparisonCondition condition) {
        return coreAssertion.satisfies(condition.withRules(rules));
    }

    @Override
    public A isNotEqualTo(TreeComparisonCondition condition) {
        return coreAssertion.satisfies(not(condition.withRules(rules)));
    }
}
