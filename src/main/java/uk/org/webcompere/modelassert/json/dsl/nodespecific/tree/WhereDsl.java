package uk.org.webcompere.modelassert.json.dsl.nodespecific.tree;

import uk.org.webcompere.modelassert.json.PathWildCard;
import uk.org.webcompere.modelassert.json.condition.tree.PathRule;
import uk.org.webcompere.modelassert.json.condition.tree.TreeComparisonCondition;
import uk.org.webcompere.modelassert.json.condition.tree.TreeRule;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
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
     * Allow missing keys in objects
     * @return <code>this</code> for fluent calling
     */
    public WhereDsl<A> objectContains() {
        return pathRule(new PathRule(TreeRule.OBJECT_CONTAINS));
    }

    /**
     * Relax the ordering requirement for an array, everywhere
     * @return <code>this</code> for fluent calling
     */
    public WhereDsl<A> arrayInAnyOrder() {
        return pathRule(new PathRule(TreeRule.IGNORE_ARRAY_ORDER));
    }

    /**
     * Allow arrays to just contain the other elements rather than match completely
     * @return <code>this</code> for fluent calling
     */
    public WhereDsl<A> arrayContains() {
        return pathRule(new PathRule(TreeRule.ARRAY_CONTAINS));
    }

    /**
     * Add common configuration to the where dsl
     * @param configurer the configurer to use
     * @return the {@link WhereDsl} for further customisation
     */
    public WhereDsl<A> configuredBy(UnaryOperator<WhereDsl<A>> configurer) {
        return configurer.apply(this);
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
     * Enter the path context - specialising a rule for the given path. The path is a list of node ids
     * or wildcards leading from the root element up to some part of the tree we're going to specialise.
     * This is useful for mixing named fields or array indices with {@link PathWildCard}s like
     * {@link PathWildCard#ANY} or {@link PathWildCard#ANY_SUBTREE}, but can be long winded for when there's
     * a fixed path, and doesn't allow for expressing a rule on root. There's also {@link #at(String)} which
     * will allow a root path - <code>"/"</code>
     * @param pathStart the first String, {@link PathWildCard} or {@link Pattern}
     * @param pathRemainder the remaining String, {@link PathWildCard} or {@link Pattern}s
     * @return the {@link PathDsl} to complete specialising what to do at that path instead of the defaults
     */
    public PathDsl<A> path(Object pathStart, Object... pathRemainder) {
        return new PathDsl<>(this, pathStart, pathRemainder);
    }

    /**
     * Provide a path using the JSON Pointer syntax - i.e. no wildcards or regular expressions used.
     * Note: this allows for matching to the root object with a path of <code>"/"</code>
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
