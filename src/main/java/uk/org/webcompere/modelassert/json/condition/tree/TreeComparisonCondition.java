package uk.org.webcompere.modelassert.json.condition.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.JsonProvider;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.impl.JsonProviders;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Compare with a whole JSON structure
 */
public class TreeComparisonCondition implements Condition {

    private JsonNode expected;
    private List<PathRule> rules = new LinkedList<>();

    /**
     * Constructor is private, use factory methods
     * @param expected the expected Json as a JSON Node
     */
    private TreeComparisonCondition(JsonNode expected) {
        this.expected = expected;
    }

    /**
     * Construct equals condition from any json object and its converter
     * @param json the object to read the json from
     * @param provider the converter
     * @param <T> the type of the JSON
     * @return the condition
     */
    public static <T> TreeComparisonCondition isEqualTo(T json, JsonProvider<T> provider) {
        return new TreeComparisonCondition(provider.jsonFrom(json));
    }

    /**
     * Construct equals condition from a JsonNode
     * @param tree the json node
     * @return the condition
     */
    public static TreeComparisonCondition isEqualTo(JsonNode tree) {
        return new TreeComparisonCondition(tree);
    }

    /**
     * Construct equals condition from json as a string
     * @param json the json as a string
     * @return the condition
     */
    public static TreeComparisonCondition isEqualTo(String json) {
        return isEqualTo(json, JsonProviders.jsonStringProvider());
    }

    /**
     * Construct equals condition from json as a string
     * @param json the json file
     * @return the condition
     */
    public static TreeComparisonCondition isEqualTo(File json) {
        return isEqualTo(json, JsonProviders.jsonFileProvider());
    }

    /**
     * Construct equals condition from json as a string
     * @param json the json file
     * @return the condition
     */
    public static TreeComparisonCondition isEqualTo(Path json) {
        return isEqualTo(json, JsonProviders.jsonPathProvider());
    }

    /**
     * Add rules to the comparison
     * @param rules the rules
     * @return <code>this</code> for fluent calling
     */
    public TreeComparisonCondition withRules(List<PathRule> rules) {
        this.rules.addAll(rules);
        return this;
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        List<String> failures = new ArrayList<>();

        Location root = new Location();
        compareTrees(json, expected, root, failures);

        if (!failures.isEmpty()) {
            return new Result(describe(), String.join("\n", failures), false);
        }

        return new Result(describe(), "", true);
    }

    private void compareTrees(JsonNode actual, JsonNode expected, Location pathToHere, List<String> failures) {
        Optional<PathRule> alternativeCondition = findRule(pathToHere, TreeRule.CONDITION);
        if (alternativeCondition.isPresent()) {
            Result result = alternativeCondition.get().getRuleCondition().test(actual);
            if (!result.isPassed()) {
                failures.add(pathToHere.toString() + ": expected " + result.getCondition() +
                    " but was " + result.getWas());
            }
            return;
        }

        // early exit if types don't match
        if (actual.getNodeType() != expected.getNodeType()) {
            failures.add(pathToHere.toString() + " different types: expected " + expected.getNodeType() +
                ", actual " + actual.getNodeType());
            return;
        }

        switch (actual.getNodeType()) {
          case BOOLEAN:
          case NULL:
          case NUMBER:
          case STRING:
              if (!actual.equals(expected)) {
                  failures.add(pathToHere.toString() + " value is different: expected " + expected +
                      ", actual " + actual);
              }
              break;
          case ARRAY:
              compareArrays((ArrayNode)actual, (ArrayNode)expected, pathToHere, failures);
              break;
          case OBJECT:
              compareObjects((ObjectNode)actual, (ObjectNode)expected, pathToHere, failures);
              break;
          default:
              failures.add("Unexpected node type: " + actual.getNodeType());
              break;
        }
    }

    private void compareObjects(ObjectNode actual, ObjectNode expected, Location pathToHere, List<String> failures) {
        Set<String> actualKeys = toSet(actual.fieldNames());
        Set<String> expectedKeys = toSet(expected.fieldNames());

        Set<String> missingKeys = new HashSet<>(expectedKeys);
        missingKeys.removeAll(actualKeys);

        Set<String> extraKeys = new HashSet<>(actualKeys);
        extraKeys.removeAll(expectedKeys);

        reportKeys("unexpected", actual, pathToHere, failures, extraKeys);
        reportKeys("missing", actual, pathToHere, failures, missingKeys);

        Set<String> actualKeysWithoutExtras = new LinkedHashSet<>(actualKeys);
        actualKeysWithoutExtras.removeAll(extraKeys);
        Set<String> expectedKeysFoundInActual = new LinkedHashSet<>(expectedKeys);
        expectedKeysFoundInActual.retainAll(actualKeys);

        // conditionally check the order of the keys
        if (!findRule(pathToHere, TreeRule.IGNORE_KEY_ORDER).isPresent()) {
            checkKeyOrder(pathToHere, failures, actualKeysWithoutExtras, expectedKeysFoundInActual);
        }

        // now iterate over the comparable keys
        for (String key: actualKeysWithoutExtras) {
            compareTrees(actual.get(key), expected.get(key), pathToHere.child(key), failures);
        }
    }

    private void reportKeys(String name, ObjectNode actual, Location pathToHere,
                            List<String> failures, Set<String> keys) {
        List<String> filtered = keys.stream()
            .filter(key -> !isKeyAllowedByRules(actual, pathToHere, key))
            .collect(Collectors.toList());

        if (!filtered.isEmpty()) {
            failures.add(pathToHere.toString() + ": " + name + " keys " + filtered);
        }
    }

    private boolean isKeyAllowedByRules(ObjectNode actual, Location pathToHere, String key) {
        return findRule(pathToHere.child(key), TreeRule.CONDITION)
            .map(rule -> rule.getRuleCondition().test(actual.get(key)).isPassed())
            .orElse(false);
    }

    private Optional<PathRule> findRule(Location pathToHere, TreeRule ruleToFind) {
        return rules.stream()
            .filter(rule -> rule.matches(pathToHere) && rule.getRule().equals(ruleToFind))
            .findFirst();
    }

    private void checkKeyOrder(Location pathToHere, List<String> failures,
                               Set<String> actualKeysWithoutExtras,
                               Set<String> expectedKeysFoundInActual) {
        if (!new LinkedList<>(actualKeysWithoutExtras).equals(new LinkedList<>(expectedKeysFoundInActual))) {
            failures.add(pathToHere.toString() + ": keys in the wrong order - expected " +
                expectedKeysFoundInActual + ", found " + actualKeysWithoutExtras);
        }
    }

    private void compareArrays(ArrayNode actual, ArrayNode expected, Location pathToHere, List<String> failures) {
        if (actual.size() != expected.size()) {
            failures.add(pathToHere.toString() + ": arrays have different size, expected: " +
                expected.size() + " actual: " + actual.size());
        }

        for (int i = 0; i < Math.min(actual.size(), expected.size()); i++) {
            compareTrees(actual.get(i), expected.get(i), pathToHere.child(Integer.toString(i)), failures);
        }
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return "equal to " + expected.toPrettyString() + explainRules();
    }

    private String explainRules() {
        if (rules.isEmpty()) {
            return "";
        }
        return "\nWith rules:" + rules.stream().map(PathRule::toString).collect(Collectors.joining("\n"));
    }

    private static Set<String> toSet(Iterator<String> iterable) {
        Set<String> set = new LinkedHashSet<>();
        iterable.forEachRemaining(set::add);
        return set;
    }
}
