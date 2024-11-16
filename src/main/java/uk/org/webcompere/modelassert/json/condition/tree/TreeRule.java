package uk.org.webcompere.modelassert.json.condition.tree;

public enum TreeRule {
    /**
     * Ignore the order of keys in an object
     */
    IGNORE_KEY_ORDER,

    /**
     * When the key order is important
     */
    REQUIRE_KEY_ORDER,

    /**
     * Apply a specific condition instead of the tree comparison
     */
    CONDITION,

    /**
     * Allow the array to be in any order
     */
    IGNORE_ARRAY_ORDER,

    /**
     * Allow the array to contain the elements, rather than match perfectly
     * can be combined with allowing any order
     */
    ARRAY_CONTAINS,

    /**
     * Allow an empty array to match null and vice-versa
     */
    NULL_MATCHES_EMPTY_ARRAY,

    /**
     * Skip over fields that are missing in the other object - implies keys in any order
     */
    OBJECT_CONTAINS,
}
