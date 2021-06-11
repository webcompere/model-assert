package uk.org.webcompere.modelassert.json.condition.tree;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.condition.array.ArrayElementCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Allow the {@link uk.org.webcompere.modelassert.json.condition.array.LooseComparison} algorithm to
 * use elements of the expected as conditions, while still honouring the various rules that guide how matching
 * is performed
 */
public class ArrayComparisonElementCondition implements ArrayElementCondition {
    private JsonNode elementInExpected;
    private int indexOfExpected;
    private Location pathToHere;
    private TreeComparisonCondition treeComparisonCondition;

    /**
     * Compare an element in the target array
     * @param elementInExpected which target element is being matched
     * @param indexOfExpected what is its index
     * @param pathToHere what is the path in the tree to this point
     * @param treeComparisonCondition what is the parent tree comparer - used to recurse into for child elements
     */
    public ArrayComparisonElementCondition(JsonNode elementInExpected, int indexOfExpected,
                                           Location pathToHere, TreeComparisonCondition treeComparisonCondition) {
        this.elementInExpected = elementInExpected;
        this.indexOfExpected = indexOfExpected;
        this.pathToHere = pathToHere;
        this.treeComparisonCondition = treeComparisonCondition;
    }

    @Override
    public Result test(JsonNode json, int arrayIndex) {
        List<String> failures = new LinkedList<>();
        treeComparisonCondition.compareTrees(json, elementInExpected,
            pathToHere.child(Integer.toString(arrayIndex)), failures);
        return new Result(this::describe, failures.toString(), failures.isEmpty());
    }

    @Override
    public String describe() {
        return "Has match for " + pathToHere.child(Integer.toString(indexOfExpected)).toString() + " in expected";
    }
}
