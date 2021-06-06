package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Array content conditions
 */
public class ArrayContains implements Condition {
    private String description;
    private List<Condition> arrayElementConditions;
    private boolean requireStrict;

    private static class Match {
        private int index;
        private Set<Integer> counterPartIndices = new HashSet<>();

        public Match(int index) {
            this.index = index;
        }

        public int size() {
            return counterPartIndices.size();
        }

        public int getIndex() {
            return index;
        }

        public void add(int counterPart) {
            counterPartIndices.add(counterPart);
        }

        public void remove(int counterPart) {
            counterPartIndices.remove(counterPart);
        }

        public Stream<Integer> streamCounterparts() {
            return counterPartIndices.stream();
        }

        public boolean contains(int counterpart) {
            return counterPartIndices.contains(counterpart);
        }
    }

    /**
     * Construct the array contains condition
     * @param description the name to use - as simple as possible
     * @param arrayElementConditions the per element conditions
     * @param requireStrict whether the conditions are executed in strict order
     */
    public ArrayContains(String description, List<Condition> arrayElementConditions, boolean requireStrict) {
        this.description = description;
        this.arrayElementConditions = arrayElementConditions;
        this.requireStrict = requireStrict;
    }

    /**
     * Create the condition to match the provided values in the array
     * @param first first value
     * @param rest remaining values
     * @return the {@link ArrayContains} to find these values within the array
     */
    public static ArrayContains containsValues(Object first, Object... rest) {
        Object[] remainder = correctRestForNulls(rest);
        return new ArrayContains(describe(first, remainder),
            toConditions(first, remainder), false);
    }

    /**
     * Create the condition to match the provided conditions in the array
     * @param conditions the conditions
     * @return the {@link ArrayContains} to find elements matching these conditions
     */
    public static ArrayContains containsValues(ConditionList conditions) {
        if (conditions == null) {
            // accidental leak into ConditionList overload when the caller must have meant a null value
            return containsValues((Object)null);
        }
        return new ArrayContains(describe(conditions),
            conditions.getConditionList(), false);
    }

    /**
     * Create the condition to match the provided values in the array in exact order
     * @param first first value
     * @param rest remaining values
     * @return the {@link ArrayContains} to match the array with these values
     */
    public static ArrayContains containsValuesExactly(Object first, Object... rest) {
        Object[] remainder = correctRestForNulls(rest);
        return new ArrayContains(describe(first, remainder),
            toConditions(first, remainder), true);
    }

    /**
     * Create the condition to match the provided condition list in the array in exact order
     * @param conditions conditions
     * @return the {@link ArrayContains} to match the array with these conditions
     */
    public static ArrayContains containsValuesExactly(ConditionList conditions) {
        return new ArrayContains(describe(conditions),
            conditions.getConditionList(), true);
    }

    private static Object[] correctRestForNulls(Object[] rest) {
        if (rest != null) {
            return rest;
        }
        return new Object[] { null };
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        // must be an array node
        if (!json.isArray() || !(json instanceof ArrayNode)) {
            return new Result(describe(), json.getNodeType().toString(), false);
        }

        ArrayNode arrayNode = (ArrayNode) json;
        if (requireStrict) {
            return strictComparison(arrayNode);
        }
        return looseComparison(arrayNode);
    }

    private Result looseComparison(ArrayNode arrayNode) {
        // each criterion may match many other elements
        // try them all out
        List<Match> matches = IntStream.range(0, arrayElementConditions.size())
            .mapToObj(index -> calcMatches(index, arrayNode))
            .collect(toList());

        while (!matches.isEmpty()) {
            matches.sort(Comparator.comparing(Match::size));
            if (matches.get(0).size() == 0) {
                return explainMismatches(matches);
            }

            removeLeastOccurringCounterpart(matches);
        }

        return new Result(describe(), "all matched", true);
    }

    private void removeLeastOccurringCounterpart(List<Match> matches) {
        Multiset<Integer> multiset = HashMultiset.create();
        matches.stream().flatMap(Match::streamCounterparts)
            .forEach(multiset::add);

        // least frequently occurring item
        int leastFoundIndex = multiset.entrySet().stream().min(Comparator.comparing(Multiset.Entry::getCount))
            .map(Multiset.Entry::getElement)
            .orElseThrow(() -> new IllegalStateException("Cannot have no elements"));

        // now let's find the smallest match of this
        Match smallestMatch = matches.stream()
            .filter(match -> match.contains(leastFoundIndex)).min(Comparator.comparing(Match::size))
            .orElseThrow(() -> new IllegalStateException("Cannot have no elements"));

        // we can remove this one
        matches.remove(smallestMatch);

        // then take the least found index out of the remainder
        matches.forEach(match -> match.remove(leastFoundIndex));
    }

    private Result explainMismatches(List<Match> matches) {
        matches.sort(Comparator.comparing(Match::getIndex));
        return new Result(describe(), "No matches for:\n" + matches.stream()
            .filter(match -> match.size() == 0)
            .map(match -> "Index " + match.getIndex() + ": " +
                arrayElementConditions.get(match.getIndex()).describe())
            .collect(joining("\n")), false);
    }

    private Match calcMatches(int index, ArrayNode arrayNode) {
        Match match = new Match(index);
        for (int i = 0; i < arrayNode.size(); i++) {
            if (arrayElementConditions.get(index).test(arrayNode.get(i)).isPassed()) {
                match.add(i);
            }
        }
        return match;
    }

    private Result strictComparison(ArrayNode arrayNode) {
        if (arrayNode.size() != arrayElementConditions.size()) {
            return new Result(describe(), "different number of elements: found " + arrayNode.size() +
                " rather than expected " + arrayElementConditions.size(), false);
        }

        Result[] results = new Result[arrayElementConditions.size()];
        for (int i = 0; i < arrayElementConditions.size(); i++) {
            results[i] = arrayElementConditions.get(i).test(arrayNode.get(i));
        }

        if (Arrays.stream(results).allMatch(Result::isPassed)) {
            return new Result(describe(), "all matched", true);
        }

        return new Result(describe(), explainFailures(results), false);
    }

    private String explainFailures(Result[] results) {
        return IntStream.range(0, results.length)
            .filter(index -> !results[index].isPassed())
            .mapToObj(index -> "Index " + index + " expected " +
                results[index].getCondition() + " but was " + results[index].getWas())
            .collect(joining("\n"));
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return description + (requireStrict ? " in exact order" : "");
    }

    private static String describe(Object first, Object... rest) {
        return "Array with values " + Stream.concat(Stream.of(first), Arrays.stream(rest))
            .map(Objects::toString)
            .collect(toList());
    }

    private static String describe(ConditionList conditionList) {
        return "Array where:\n" + IntStream.range(0, conditionList.getConditionList().size())
            .mapToObj(idx -> "Index " + idx + ": " + conditionList.getConditionList().get(idx).describe())
            .collect(joining("\n"));
    }

    private static List<Condition> toConditions(Object first, Object... rest) {
        return Stream.concat(Stream.of(first), Arrays.stream(rest))
            .map(HasValueWithLooseType::new)
            .collect(toList());
    }
}
