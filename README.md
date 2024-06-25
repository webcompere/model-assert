# ModelAssert

[![Build on Push](https://github.com/webcompere/model-assert/actions/workflows/build-actions.yml/badge.svg)](https://github.com/webcompere/model-assert/actions/workflows/build-actions.yml)[![codecov](https://codecov.io/gh/webcompere/model-assert/branch/main/graph/badge.svg?token=SJ9ZKQVO5T)](https://codecov.io/gh/webcompere/model-assert)


Assertions for model data. Inspired by [JSONAssert](https://github.com/skyscreamer/JSONassert)
and [AssertJ](https://assertj.github.io/doc/). Built on top of [Jackson](https://github.com/FasterXML/jackson).

Intended as a richer way of writing assertions in unit tests, and as
a more powerful alternative to Spring's [`jsonPath`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html#jsonPath-java.lang.String-org.hamcrest.Matcher-).

Describes paths using [JSON Pointer](https://gregsdennis.github.io/Manatee.Json/usage/pointer.html) syntax, where
a route to the element is a series of `/` delimited field names or array indices.

> NOTE: To resolve some vulnerabilities, including CVEs found in `snakeyaml`, this version is
> built against an rc of `jackson-databind` and `jackson-dataformat-yaml`. A future release
> will upgrade to the LTS version of these libraries, but the dependency is light weight enough that
> overriding these dependencies in your project is unlikely to be an issue.

## Installation

ModelAssert requires Java 8.

Install from Maven Central:

```xml
<dependency>
  <groupId>uk.org.webcompere</groupId>
  <artifactId>model-assert</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Quickstart

For a walk-through of key features, there's a [tutorial over on Baeldung.com](https://www.baeldung.com/json-modelassert).

### Path Assertions

```java
String json = "{\"name\":\"ModelAssert\"}";

// assertJ style
assertJson(json)
   .at("/name").hasValue("ModelAssert");

// hamcrest style
MatcherAssert.assertThat(json,
    json()
      .at("/name").hasValue("ModelAssert"));
```

In the above example, `at` is just one of the possible conditions. Here we see Jackson's JSON Pointer syntax in action too.

### Whole JSON Comparison

Semantic comparison of the JSON loaded as both expected and actual.

```java
// assertJ style
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .isEqualTo("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}");

// hamcrest style
MatcherAssert.assertThat("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}",
    json().isEqualTo("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}"));
```

These comparisons can be mixed with path asserts, but they compare the whole
object structure and report the differences on error, so there's minimum benefit in using both.

By default, the comparison must match everything in order, but the `isEqualTo`
can be relaxed by using `where`:

```java
// allow object keys in any order
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .where()
        .keysInAnyOrder()
    .isEqualTo("{\"versions\":[1.00, 1.01, 1.02], \"name\":\"ModelAssert\"}");
```

See [where context](#where-context) for more examples.

### Assertion DSL

There are more examples in the unit tests, especially [`ExamplesTest`](src/test/java/uk/org/webcompere/modelassert/json/ExamplesTest.java).

The `assertJson` methods produce stand-alone assertions which
execute each clause in order, stopping on error.

The `json*` methods - `json`, `jsonNode`, `jsonFile`, `jsonFilePath` start the
construction of a hamcrest matcher to which conditions are added.
These are evaluated when the hamcrest matcher's `matches` is called.

> Note: the DSL is intended to provide auto-complete and is largely fluent.
> It's also composable, so multiple comparisons can be added after the
> last one is complete:

```java
assertJson(json)
   .at("/name").hasValue("ModelAssert")
   .at("/license").hasValue("MIT")
   .at("/price").isNull();
```

### Non JSON Comparison

If an object can be converted into Jackson's `JsonNode` structure, which nearly everything can be,
then it can be compared using ModelAssert:

```java
Map<String, Object> objectMap = new HashMap<>();
objectMap.put("a", UUID.randomUUID().toString());
objectMap.put("b", UUID.randomUUID().toString());

Map<String, Object> expectedMap = new HashMap<>();
expectedMap.put("a", "");
expectedMap.put("b", "");

assertJson(objectMap)
    .where()
    .path(Pattern.compile("[ab]")).matches(GUID_PATTERN)
    .isEqualTo(expectedMap);
```

As both `assertJson` and `isEqualTo` allow `JsonNode` as an input,
custom conversions to this can be used from any source.

### YAML Support

As Jackson can load yaml files, the DSL also supports `assertYaml` and `isEqualToYaml`/`isNotEqualToYaml`:

```java
String yaml1 =
    "name: Mr Yaml\n" +
        "age: 42\n" +
        "items:\n" +
        "  - a\n" +
        "  - b\n";

String yaml2 =
    "name: Mrs Yaml\n" +
        "age: 43\n" +
        "items:\n" +
        "  - c\n" +
        "  - d\n";

assertYaml(yaml1)
    .isNotEqualToYaml(yaml2);
```

The Hamcrest version of this uses `yaml`/`yamlFile` and `yamlFilePath`:

```java
MatcherAssert.assertThat(yaml1, yaml().isEqualToYaml(yaml2));
```

### Manipulating Json Before or During Assertions

The assertion DSL allows a lot of navigation within the json under test.
However, it may be desirable to manually load some json for comparison, and perhaps
use only a part of that json:

```java
// load some json to compare against
JsonNode jsonNode = JsonProviders.jsonPathProvider().jsonFrom(jsonFile);

// compare "/child" within a source
assertJson(jsonFile)
    .at("/child")

    // must be equal to the "/child" we've selected
    // from an "actual"
    .isEqualTo(jsonNode.at("/child"));
```

## Building the Assertion

The entry point to creating an assertion is:

- `assertJson` - overloaded to take JSON as `String`, `JsonNode`, `File` or `Path` - **produces a fluent assertion like AssertJ**
  > Note: the Jackson parser has been configured to load unquoted field names
  > so:
  > ```java
  > String unquoted = "{someField: \"value\"}";
  > // is equivalent to
  > String quoted = "{\"someField\": \"value\"}";
  > ```
  > Examples throughout the tests are in the second, more conventional, format.
- `json` - start creating a hamcrest matcher for a `String`
- `jsonNode` - start creating a hamcrest matcher for a `JsonNode`
- `jsonFile` - start creating a hamcrest matcher for a `File`
- `jsonFilePath` - start creating a hamcrest matcher for a `Path`

After that, there are high level methods to add conditions to the matcher:

- `at` - start creating a JSON Pointer based assertion
- `isNull`/`isNotNull` - asserts whether the whole loaded JSON amounts to `null`
- `isEqualTo`/`isNotEqualTo` - compare this tree against another
- `satisfies` - plug in a custom `Condition` or `ConditionList`

When a condition has been added to the assertion then the fluent DSL
allows for further conditions to be added.

> Note: the `assertJson` version executes each condition on the fly, where the hamcrest
version stores them for execution until the `matches` method is invoked by `MatcherAssert.assertThat`
or similar.

## Conditions

There are multiple contexts from which assertions are available:

- **Assertion** - this allows `at` as well as ALL other assertions
- **Inside `at`** - allows any `node` assertion, and then returns to `assertion` context
- **Node** - this allows any assertion on the current node, which may be of any valid json type as well as `missing`
- **Type specific** - by calling `number`, `text`, `object`, `array`, or `booleanNode` on a node context DSL, the DSL
can be narrowed down to assertions for just that type - this can also be more expressive
    ```java
    assertJson(json)
       .at("/name").text().isText("My Name");
    ```
- **Where** - called before `isEqualTo` to create rules for whole tree comparison

### Json At

Build a `JsonAt` condition by using `.at("/some/json/pointer")`.

This is then followed by any of the node context assertions.

Example:

```java
assertJson("{\"name\":null}")
    .at("/name").isNull();
```

The `JsonAt` expression is incomplete with just `at`, but once the rest of the condition is added,
the `this` returned belongs to the main assertion, allowing them to be chained.

```java
assertJson("{\"name\":null}")
    .at("/name").isNull()
    .at("/address").isMissing();
```

JSON Pointer expressions treat field names and array indices as `/` delimited:

```java
assertJson("{\"names\":[\"Model\",\"Assert\"]}")
    .at("/names/1").hasValue("Assert");
```

### Node Context Assertions

These are available on any node of the tree, which might be any type. They include the
type specific assertions below, as well as:

- `hasValue` - assert that a field has a specific value
  ```java
  assertJson(jsonString)
    .at("/name").hasValue("ModelAssert");
  ```
  > Note: this is very forgiving of type, and may be less precise as
  > a consequence. It detects the expected node type from its input.
- `isNull`/`isNotNull` - assert whether this path resolves to `null`
  ```java
  assertJson(jsonString)
    .at("/price").isNull();
  ```
- `isMissing`/`isNotMissing` - assert that this path resolves to _missing_ - i.e. it's an unknown path in the JSON
  ```java
  assertJson(jsonString)
    .at("/random").isMissing();
  ```
- `isAnyNode` - the same as `isNotMissing` - useful when used with `.where()` in full tree matching
- `isEmpty`/`isNotEmpty` - assert that the json at this location
  is an empty text, array, or object node
  ```java
  assertJson(someJson)
    .isEmpty();
  ```
  This can be combined with a more precise type check and a path in the json:
  ```java
  assertJson(someJson)
    .at("/name").isText()
    .at("/name").isEmpty();
  ```
  Though for brevity, the `isEmptyText`/`isNotEmptyText` may be easier:
    ```java
  assertJson(someJson)
    .at("/name").isEmptyText();
  ```
- `matches(Matcher<JsonNode>)` - assert that the **node** found at this JSON path matches a hamcrest matcher for `JsonNode`
  ```java
  assertJson(jsonString)
    .at("/child/someobject").matches(customHamcrestMatcher);
  ```
  This latter example, allows us to reuse the hamcrest form of the
  json assertion across tests, if there's a common pattern, or allows
  us to apply a particular set of assertions to only a subtree of the original:
  ```java
  assertJson(jsonString)
    .at("/root/child/otherchild/interestingplace")
    .matches(jsonNode()  // jsonNode() creates a new matcher
       .at("/name").hasValue("Model")
       .at("/age").hasValue(42));
  ```
  > Note: `satifies` along with `ConditionList` may be a better solution to subtree
  > assertions with `at`
  > ```java
  > assertJson("[" +
  >   "{\"name\":\"Model\",\"ok\":true}," +
  >   "{\"name\":\"Model\",\"ok\":false}," +
  >   "{\"name\":\"Model\"}," +
  >   "{\"age\":1234}" +
  >   "]")
  >   .at("/1").satisfies(conditions()
  >     .at("/name").hasValue("Model")
  >     .at("/ok").isFalse());
  >```
- `is`/`isNot` - provide a description and a `Predicate<JsonNode>` to customise with a custom match condition
  > This is the unlimited customisable assertion - allowing any test to be done on a per node basis, if it's
  > not already part of the DSL
  ```java
  assertJson("42")
    .is("Even number", jsonNode -> jsonNode.isNumber() && jsonNode.asInt() % 2 == 0);
  ```
- `is(Function)` - allows customisation with a standard set of match conditions - to modularise the tests:
  ```java
  @Test
  void canApplyStandardSetOfAssertions() {
      assertJson("{\"root\":{\"name\":\"Mr Name\"}}")
        .is(ExamplesTest::theUsual)
        .isNotEmpty(); // additional clause
  }
  private static <A> A theUsual(JsonNodeAssertDsl<A> assertion) {
      return assertion.at("/root/name").isText("Mr Name");
  }
  ```

### Text Context Conditions

- `isText`/`isNotText` - assert that the node is a text node, with optional specific text - note: this can also be achieved with `hasValue`, but adds
some extra checking that this is a text node
  ```java
  assertJson("\"theText\"")
    .isText();

  assertJson("\"theText\"")
    .isText("theText");

  assertJson("{\"child\":{\"age\":123}}")
    .at("/child/age").isNotText();

    assertJson("{\"child\":{\"name\":"Bob"}}")
    .at("/child/age").isNotText("Bert");
  ```
- `isEmptyText`/`isNotEmptyText` - both of these require the node to be text, and then assert that the text is `""` or not
  ```java
  assertJson("\"\"")
    .isEmptyText();

  // FAILS! - wrong type
  assertJson("0")
    .isNotEmptyText();

  // non empty
  assertJson("\"0\"")
    .isNotEmptyText();
  ```
- `matches(Pattern|String)` - assert that the **text** of this node matches a regular expression - some common patterns are available in the `Patterns` class
  ```java
  assertJson(jsonString)
    .at("/guid").matches(GUID_PATTERN);
  ```
- `textMatches`- allows a custom predicate to be passed in order to perform a custom check
  ```java
  assertJson("\"a-b-c\"")
    .textMatches("Has dashes", text -> text.contains("-"));
  ```
- `textContains`/`textDoesNotContain` - reuses the logic of the regular expression matcher to find substrings
- `textStartsWith`/`textDoesNotStartWith` - reuses the logic of the regular expression matcher to check the prefix of a text node's text

### Numeric Context Conditions
- `isGreaterThan`, `isGreaterThanOrEqualTo`, `isLessThan`, `isLessThanOrEqualTo` - these
  require that the node is a number of a numeric type, and compares
  ```java
  assertJson(jsonString)
     .at("/count").isGreaterThan(9);
  ```
  More specific typed versions - `isGreaterThanInt` or `isLessThanLong` also exist to avoid a test
  passing through accidental type coercion or overflow.
- `isBetween` - asserts that a number falls in a range
  ```java
  assertJson("{number:12}")
    .at("/number").isBetween(2, 29);
  ```
- `isZero` - asserts that the number is zero
- `isNumber`, `isInteger`, `isLong`, `isDouble` - assert this is a numeric node
or of a specific numeric type

### Boolean Context Conditions
- `isTrue`/`isFalse` - requires the node to be boolean and have the correct value
- `isBoolean`/`isNotBoolean` - asserts the type of the node

### Object Context Conditions
- `isObject`/`isNotObject` - asserts the type of the node
- `containsKey`/`containsKeys`/`doesNotContainKey`/`doesNotContainKeys` - checks for the presence of a given set of keys in the object
- `containsKeysExactly` - requires the given keys to be present in the exact order provided
- `containsKeysExactlyInAnyOrder` - requires the given keys all to be present, regardless of order in the JSON

### Array Context Conditions
- `isArray`/`isNotArray` - asserts the type of the node
- `isArrayContaining`/`isArrayContainingExactlyInAnyOrder` - **potentially slow** assertions over the contents
   of an array. Tries all permutations of matching the provided elements to the array elements, allowing for
   duplicates. Uses loose `hasValue` style matching when values provided:
   ```java
   assertJson("[1, 2, 3, 4]")
      .isArrayContaining(1, 4);

   assertJson("[1, 2, 3, 4]")
      .isArrayContainingExactlyInAnyOrder(1, 2, 3, 4);
   ```
- `isArrayContainingExactly` - strictly proves that each element in the array
  matches the elements provided:
  ```java
    assertJson("[1, 2, 3, 4]")
      .isArrayContainingExactly(1, 2, 3, 4);
  ```
  This is more efficient at runtime as it has a simple job.

There are two main ways to assert the contents of an array. It can be done by
value as illustrated above, or it can be done by condition list.

To use the `isArrayContaining` suite of functions with a condition list,
we call `conditions()` within the `ConditionList` class to create a
fluent builder of a list of conditions. As the fluent builder for assertions
adds conditions to the assertion, so the fluent builder inside
`ConditionList` treats each additional condition as an element to search for
in the array:

```java
assertJson("[" +
    "{\"name\":\"Model\",\"ok\":true}," +
    "{\"name\":\"Model\",\"ok\":false}," +
    "{\"name\":\"Assert\"}," +
    "{\"age\":1234}" +
    "]")
    .isArrayContainingExactlyInAnyOrder(conditions()
        .at("/name").isText("Assert")
        .at("/name").hasValue("Model")
        .at("/ok").isFalse()
        .at("/age").isNumberEqualTo(1234));
```

In the above example, the conditions, between them, represent a unique
match in each element of the list, but a condition may match more than one
element (as `.at("/name".isText("Assert")` does). This is where the
permutational search of the ArrayCondition helps to find the best possible match.

Where a single condition cannot describe the required match for an element
then `satisfies`, which is part of every node, allows a `ConditionList`:

```java
assertJson("[" +
    "{\"name\":\"Model\",\"ok\":true}," +
    "{\"name\":\"Model\",\"ok\":false}," +
    "{\"name\":\"Model\"}," +
    "{\"age\":1234}" +
    "]")
    .isArrayContainingExactlyInAnyOrder(conditions()
        // condition A
        .at("/name").isText("Model")

        // condition B
        .satisfies(conditions()
            .at("/name").hasValue("Model")
            .at("/ok").isTrue())

        // condition C
        .satisfies(conditions()
            .at("/ok").isFalse()
            .at("/name").isText("Model"))

        // condition D
        .at("/age").isNumberEqualTo(1234));
```

Each of these composite conditions allows the whole DSL. They're
composed together using `Condition.and`.

> A Hamcrest matcher could also be used with `ConditionList`
> via `matches(Matcher<JsonNode>)`

## Size Assertions (various types)

Object, String and Array can be said to be _sizeable_. For Object, the size is
the number of keys. For String, it's the number of characters. For Array it's the number
of elements.

We can assert this with `hasSize`:

```java
assertJson("\"some string\"")
    .hasSize(11);

assertJson("[1, 2, 3]")
    .hasSize(3);
```

The general purpose `Number` based numeric assertions can be used to assert size via the
`size()` function, which enters the `NumberComparison` context:

```java
// assert that the array has a size between 3 and 9
assertJson("[1, 2, 3]")
    .size().isBetween(3, 9);
```

## Whole Tree Comparison

The tree comparison is intended to perform a semantic comparison of a JSON
tree with another.

It can be used in conjunction with the `at` part of the Node DSL:

```java
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .at("/versions")
    .isEqualTo("[1.00, 1.01, 1.02]");
```

It can also be customised using `where`.

### Where Context

This is used to customise how whole tree comparison works.

The `where` function moves us from node context to customisation of `isEqualTo`:

```java
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .where()
        .keysInAnyOrder()
    .isEqualTo("{\"versions\":[1.00, 1.01, 1.02], \"name\":\"ModelAssert\"}");
```

In the where context, we can add general leniency overrides, or specify overrides
for particular paths.

- `keysInAnyOrder`/`keysInOrder` - controls whether objects observe order checks - when used just after `where` this applies to the whole tree, otherwise it applies to the path exression
- `objectContains` - the object ignored missing values in the _actual_
- `arrayInAnyOrder` - array elements can be in any order
- `arrayContains` - array elements can be in any order and the actual may have additional elements
- `path` - start customising the rule for a particular path in the tree:
  ```java
  // turn off key order sensitivity for the `address` field
  assertJson(...)
     .where().path("address").keysInAnyOrder()
     .isEqualTo(...);
  ```
  The path is expressed as a series of values, which can be:
  - `String` - conforming to a JSON Pointer, but no `/`
  - Regular expression for matching a field - i.e. `Pattern`
  - `PathWildCard` - either `ANY` or `ANY_SUBTREE` - allowing path matching of one or n levels of fields
- `at` - a synonym for `path` where the whole JSON Pointer style path is provided - this is a short-hand for paths where there are no wildcards

Within the path expression, we then add further conditions:

- Any conditions from Node context
- `keysInAnyOrder`/`keysInOrder` - specific matches for the current path
- `objectContains` - the object ignored missing values in the _actual_
- `arrayInAnyOrder` - array elements can be in any order
- `arrayContains` - array elements can be in any order and the actual may have additional elements
- `isIgnored` - the path is just ignored

The purpose of the `where` and `path` contexts is to allow for things
which cannot be predicted at the time of coding, or which do not matter
to the result.

A good example is GUIDs in the output. Let's say we have a process which
produces JSON with random GUIDs in it. We want to assert that there ARE GUIDs
but we can't predict them:

```java
assertJson("{\"a\":{\"guid\":\"fa82142d-13d2-49c4-9878-619c90a9f986\"}," +
    "\"b\":{\"guid\":\"96734f31-33c3-4e50-a72b-49bf2d990e33\"}," +
    "\"c\":{\"guid\":\"064c8c5a-c9c1-4ea0-bf36-1994104aa870\"}}")
    .where()
        .path(ANY_SUBTREE, "guid").matches(GUID_PATTERN)
    .isEqualTo("{\"a\":{\"guid\":\"?\"}," +
        "\"b\":{\"guid\":\"?\"}," +
        "\"c\":{\"guid\":\"?\"}}");
```

Here, the `path(ANY_SUBTREE, "guid").matches(GUID_PATTERN)` phrase is
allowing anything _ending_ in `guid` to be matched using `matches(GUID_PATTERN)`
instead of matching it against the JSON inside `isEqualTo`.

This can be done more specifically using `at`:

```java
assertJson("{\"a\":{\"guid\":\"fa82142d-13d2-49c4-9878-619c90a9f986\"}," +
    "\"b\":{\"guid\":\"96734f31-33c3-4e50-a72b-49bf2d990e33\"}," +
    "\"c\":{\"guid\":\"064c8c5a-c9c1-4ea0-bf36-1994104aa870\"}}")
    .where()
        .at("/a/guid").matches(GUID_PATTERN)
        .at("/b/guid").matches(GUID_PATTERN)
        .at("/c/guid").matches(GUID_PATTERN)
    .isEqualTo("{\"a\":{\"guid\":\"?\"}," +
        "\"b\":{\"guid\":\"?\"}," +
        "\"c\":{\"guid\":\"?\"}}");
```

> Note: the rules used with `where` are evaluated in reverse order
> so the most general should be provided first, and the most specific last.

#### Loose Array Matching

**Warning: performance implications** both `arrayInAnyOrder` and `arrayContains`
try every possible combination of array element in the expected against the
actual in order to work out if the expected elements are present. For
small arrays, this is not a problem, and the unit tests of this project
run very quickly, proving that.

However, an array can, itself, contain objects or other arrays. This can lead
to a large permutational explosion, which can take time.

The easiest way to relax array ordering rules is to use `where().arrayInAnyOrder()`
while setting up `isEqualTo`:

```java
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .where()
    .arrayInAnyOrder()
    .isEqualTo("{\"name\":\"ModelAssert\", \"versions\":[1.02, 1.01, 1.00]}");
```

If only a specific array may be in a random order, it may be better to specialise
this by path:

```java
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .where()
    .path("versions").arrayInAnyOrder()
    .isEqualTo("{\"name\":\"ModelAssert\", \"versions\":[1.02, 1.01, 1.00]}");
```

And, if the value in the expected doesn't contain all the values from the
array in the actual, then we can use `arrayContains` to both relax the order
and allow matching of the ones found:

```java
assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
    .where()
    .path("versions").arrayContains()
    .isEqualTo("{\"name\":\"ModelAssert\", \"versions\":[1.02]}");
```

> Note: loose array comparison also honours the rules set in where
> for the child nodes of the array. **The paths described are routes within
> the actual tree, not the expected tree.**. So as every combination of
> match is tried, the path rules may perform different comparisons on the
> expected data, as it's checked against each actual.

#### Common `where` Configuration

The `configuredBy` function on the `WhereDsl` allows a common comparison configuration
to be implemented and plugged in:

```java
@Test
void matchesAnyGuidUsingCommonConfiguration() {
    assertJson("{\"a\":{\"guid\":\"fa82142d-13d2-49c4-9878-619c90a9f986\"}," +
        "\"b\":{\"guid\":\"96734f31-33c3-4e50-a72b-49bf2d990e33\"}," +
        "\"c\":{\"guid\":\"064c8c5a-c9c1-4ea0-bf36-1994104aa870\"}}")
        .where()
            .configuredBy(ExamplesTest::ignoreGuids)
        .isEqualTo("{\"a\":{\"guid\":\"?\"}," +
            "\"b\":{\"guid\":\"?\"}," +
            "\"c\":{\"guid\":\"?\"}}");
}

private static <A> WhereDsl<A> ignoreGuids(WhereDsl<A> where) {
    return where.path(ANY_SUBTREE, "guid").matches(GUID_PATTERN);
}
```

## Customisation

There's room for custom assertions throughout the DSL, and if necessary,
the `Satisfies` interface, allows a condition to be added fluently. Conditions
are based on the `Condition` class. The existing conditions can be used directly
if necessary, and can be composed using `Condition.and` or `Condition.or`
where needed. Similarly, there's a `not` method in the `Condition`
class `Not` to invert any condition as well as `invert` on `Condition` to invert
the current condition.

A custom condition can be fed to `satisfies`:

```java
// using `and` along with functions from the
// condition classes
assertJson("\"some string\"").satisfies(
    textMatches(Pattern.compile("[a-z ]+"))
        .and(new HasSize(12)));

// using or and inverting the condition - this will
// pass as it fails both the ORed conditions, but the
// whole statement is inverted
assertJson("\"some string!!!\"").satisfies(
    textMatches(Pattern.compile("[a-z ]+"))
        .or(new HasSize(12))
        .inverted());
```

## Interoperability

The assertions can be used stand-alone with `assertJson` or can be built as Hamcrest matchers. The assertion
can also be converted to a `Mockito` `ArgumentMatcher`.

### Mockito Usage

Assuming Mockito 3, the `toArgumentMatcher` method converts the `Hamcrest` style syntax into Mockito's native
`ArgumentMatcher`. Older versions of `Mockito` used Hamcrest natively.

The json matcher can then be used to detect calls to a function either with `verify`/`then` or when setting
up responses to different inputs:

```java
// detecting calls based on the json values passed
someInterface.findValueFromJson("{\"name\":\"foo\"}");

then(someInterface)
        .should()
        .findValueFromJson(argThat(json()
        .at("/name").hasValue("foo")
        .toArgumentMatcher()));


// setting up responses based on the json
given(someInterface.findValueFromJson(argThat(json()
        .at("/name").hasValue("foo")
        .toArgumentMatcher())))
        .willReturn("foo");

assertThat(someInterface.findValueFromJson("{\"name\":\"foo\"}")).isEqualTo("foo");
```

Note, this works with all the types of JSON input sources supported by the Hamcrest version of the library.
You need to choose the type of input via the `json`, `jsonFile` methods etc.

### Interoperability with Spring MVC Matchers

Rather than:

```java
// clause inside ResultMatcher
jsonPath("$.name", "ModelAssert")
```

We can construct the hamcrest matcher version of ModelAssert's JsonAssertion:

```java
content().string(
    json()
        .at("/name")
        .hasValue("ModelAssert"))
```

While this syntax is of limited value in this simple case, the more powerful comparisons supported
by this library are equally possible after the `json()` statement starts creating a matcher.

### Custom Object Mappers

By default, Model Assert uses two `ObjectMapper` objects - one for loading JSON and one for loading YAML.
These can be overridden for the current thread (allowing concurrent testing) and it's advisable to do this
in the `@BeforeAll` of a text fixture:

```java
@BeforeAll
static void beforeAll() {
    // support LocalDateTime
    overrideObjectMapper(defaultObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

    // stop parsing `yes` to mean Boolean `true`
    overrideYamlObjectMapper(new ObjectMapper(
        new YAMLFactory()
            .configure(PARSE_BOOLEAN_LIKE_WORDS_AS_STRINGS, true)));
}
```

and when replacing the object mapper in setup, it's a good idea to put it back in the tear down:

```java
@AfterAll
static void afterAll() {
    clearObjectMapperOverride();
    clearYamlObjectMapperOverride();
}
```

Any assertions used while the override is in place will use the alternative object mapper.

> Note: if using a common alternative object mapper, maybe consider building a small JUnit 5 test extension
> or [use a base class](./src/test/java/uk/org/webcompere/modelassert/json/OverrideObjectMapper.java) for your tests
> which contains the common set up

The functions `defaultObjectMapper` and `defaultYamlMapper` in `JsonProviders` can be used to create a basic `ObjectMapper`
to base a custom one on.

## API Stability

The classes in the root package `uk.org.webcompere.modelassert.json` are the jumping
on point for the API and they will be changed rarely.

Functions elsewhere will be accessed via the fluent API and may move between packages
in later versions, though this should be resolved without changing consuming code.

SemVer numbering will indicate possible breaking changes by increments to the minor version number. Patch
versions are unlikely to have any noticeable effect on the API.

## Contributing

If you experience any problems using this library, or have any ideas, then please
[raise an issue](https://github.com/webcompere/model-assert/issues/new/choose). Please
check for any [existing issues](https://github.com/webcompere/model-assert/issues) first.

PRs will be accepted if they come with unit tests and are linked to an issue.
