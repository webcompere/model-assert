package uk.org.webcompere.modelassert.json;

public enum PathWildCard {
    /**
     * Can be replaced by any single node in the tree - a field or array index
     */
    ANY,

    /**
     * Can be replaced by any amount of subtree (0 - n) in the path
     */
    ANY_SUBTREE,
}
