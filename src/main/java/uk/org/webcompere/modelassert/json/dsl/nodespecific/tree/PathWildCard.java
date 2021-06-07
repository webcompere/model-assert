package uk.org.webcompere.modelassert.json.dsl.nodespecific.tree;

public enum PathWildCard {
    /**
     * Can be replaced by any field
     */
    ANY_FIELD,

    /**
     * Can be replaced by any amount of subtree (0 - n) in the path
     */
    ANY_SUBTREE,
}
