package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.tree.TreeComparisonCondition;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;

import java.io.File;
import java.nio.file.Path;

import static uk.org.webcompere.modelassert.json.condition.Not.not;

/**
 * Assertions comparing one tree with another
 * @param <A> the assertion type
 */
public interface TreeComparisonDsl<A> extends Satisfies<A> {

    /**
     * Create isEqualTo condition
     * @param tree the tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(JsonNode tree) {
        return satisfies(TreeComparisonCondition.isEqualTo(tree));
    }

    /**
     * Create isEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(String json) {
        return satisfies(TreeComparisonCondition.isEqualTo(json));
    }

    /**
     * Create isEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(File json) {
        return satisfies(TreeComparisonCondition.isEqualTo(json));
    }

    /**
     * Create isEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(Path json) {
        return satisfies(TreeComparisonCondition.isEqualTo(json));
    }

    /**
     * Create isNotEqualTo condition
     * @param tree the tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(JsonNode tree) {
        return satisfies(not(TreeComparisonCondition.isEqualTo(tree)));
    }

    /**
     * Create isNotEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(String json) {
        return satisfies(not(TreeComparisonCondition.isEqualTo(json)));
    }

    /**
     * Create isNotEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(File json) {
        return satisfies(not(TreeComparisonCondition.isEqualTo(json)));
    }

    /**
     * Create isNotEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(Path json) {
        return satisfies(not(TreeComparisonCondition.isEqualTo(json)));
    }
}
