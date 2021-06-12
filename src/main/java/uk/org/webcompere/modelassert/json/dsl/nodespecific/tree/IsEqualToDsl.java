package uk.org.webcompere.modelassert.json.dsl.nodespecific.tree;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.condition.tree.TreeComparisonCondition;

import java.io.File;
import java.nio.file.Path;

import static uk.org.webcompere.modelassert.json.JsonProviders.*;

/**
 * The core DSL for <code>isEqualTo</code> and <code>isNotEqualTo</code>
 * @param <A> the overall assertion type
 */
public interface IsEqualToDsl<A> {

    /**
     * Terminal statement - apply the final comparison condition to the assertion. Called internally.
     * @param condition the condition
     * @return the assertion for fluent comparison
     */
    A isEqualTo(TreeComparisonCondition condition);

    /**
     * Create isEqualTo condition
     * @param tree the tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(JsonNode tree) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(tree));
    }

    /**
     * Create isEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(String json) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(json, jsonStringProvider()));
    }

    /**
     * Create isEqualTo condition
     * @param object an object to convert to JSON to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(Object object) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(object, jsonObjectProvider()));
    }

    /**
     * Create isEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(File json) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(json, jsonFileProvider()));
    }

    /**
     * Create isEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualTo(Path json) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(json, jsonPathProvider()));
    }

    /**
     * Terminal statement - apply the final comparison condition to the assertion. Called internally.
     * @param condition the condition
     * @return the assertion for fluent comparison
     */
    A isNotEqualTo(TreeComparisonCondition condition);

    /**
     * Create isNotEqualTo condition
     * @param tree the tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(JsonNode tree) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(tree));
    }

    /**
     * Create isNotEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(String json) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(json, jsonStringProvider()));
    }

    /**
     * Create isNotEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(File json) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(json, jsonFileProvider()));
    }

    /**
     * Create isNotEqualTo condition
     * @param json the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualTo(Path json) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(json, jsonPathProvider()));
    }

    /**
     * Create isEqualToYaml condition
     * @param yaml the yaml tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualToYaml(String yaml) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(yaml, yamlStringProvider()));
    }

    /**
     * Create isEqualToYaml condition
     * @param yaml the yaml tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualToYaml(File yaml) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(yaml, yamlFileProvider()));
    }

    /**
     * Create isEqualTo condition
     * @param yaml the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isEqualToYaml(Path yaml) {
        return isEqualTo(TreeComparisonCondition.isEqualTo(yaml, yamlPathProvider()));
    }

    /**
     * Create isNotEqualToYaml condition
     * @param yaml the yaml tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualToYaml(String yaml) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(yaml, yamlStringProvider()));
    }

    /**
     * Create isNotEqualToYaml condition
     * @param yaml the yaml tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualToYaml(File yaml) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(yaml, yamlFileProvider()));
    }

    /**
     * Create isNotEqualTo condition
     * @param yaml the json tree to compare
     * @return the assertion for fluent comparison
     */
    default A isNotEqualToYaml(Path yaml) {
        return isNotEqualTo(TreeComparisonCondition.isEqualTo(yaml, yamlPathProvider()));
    }
}
