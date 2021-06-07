# ModelAssert

[![Build status](https://ci.appveyor.com/api/projects/status/x87jeu1yia9b40bp?svg=true)](https://ci.appveyor.com/project/ashleyfrieze/model-assert) [![codecov](https://codecov.io/gh/webcompere/model-assert/branch/main/graph/badge.svg?token=SJ9ZKQVO5T)](https://codecov.io/gh/webcompere/model-assert)


Assertions for model data. Inspired by [JSONAssert](https://github.com/skyscreamer/JSONassert)
and [AssertJ](https://assertj.github.io/doc/). Built on top of [Jackson](https://github.com/FasterXML/jackson).

Intended as a richer way of writing assertions in unit tests, and as
a more powerful alternative to Spring's [`jsonPath`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html#jsonPath-java.lang.String-org.hamcrest.Matcher-).

Describes paths using [JSON Pointer](https://gregsdennis.github.io/Manatee.Json/usage/pointer.html) syntax, where
a route to the element is a series of `/` delimited field names or array indices.

## Installation

ModelAssert requires Java 8.

t.b.c.

## Quickstart

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

There are more examples in the unit tests, especially [`ExamplesTest`](src/test/java/uk/org/webcompere/modelassert/json/ExamplesTest.java).

In the above example, `at` is just one of the possible conditions. Here we see Jackson's JSON Pointer syntax in action too.

The `assertJson` methods produces stand-alone assertions which
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

## Building the Assertion

The entry point to creating an assertion is:

- `assertJson` - overloaded to take JSON as `String`, `File` or `Path` - **produces a fluent assertion like AssertJ**
- `json` - start creating a hamcrest matcher for a `String`
- `jsonNode` - start creating a hamcrest matcher for a `JsonNode`
- `jsonFile` - start creating a hamcrest matcher for a `File`
- `jsonFilePath` - start creating a hamcrest matcher for a `Path`

After that, there are high level methods to add conditions to the matcher:

- `at` - start creating a JSON Pointer based assertion
- `isNull`/`isNotNull` - asserts whether the whole loaded JSON amounts to `null`
- `satisfies` - plug in a custom `Condition`

When a condition has been added to the assertion then the fluent DSL
allows for further conditions to be added.

> Note: the `assertJson` version executes each condition on the fly, where the hamcrest
version stores them for execution until the `matches` method is invoked by `MatcherAssert.assertThat`
or similar.

## Conditions

There are multiple contexts from which assertions are available:

- Assertion - this allows `at` as well as ALL other assertions
- Inside `at` - allows any `node` assertion, and then returns to `assertion` context
- Node - this allows any assertion on the current node, which may be of any valid json type as well as `missing`
- Type specific - by calling `number`, `text`, `object`, `array`, or `booleanNode` on a node context DSL, the DSL
can be narrowed down to assertions for just that type - this can also be more expressive

```java
assertJson(json)
   .at("/name").text().isText("My Name");
```

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

### Numeric Context Conditions
- `isGreaterThan`, `isGreaterThanOrEqualTo`, `isLessThan`, `isLessThanOrEqualTo` - these
  require that the node is a number of a numeric type, and compares
  ```java
  assertJson(jsonString)
     .at("/count").isGreaterThan(9);
  ```
  More specific typed versions - `isGreaterThanInt` or `isLessThanLong` also exist to avoid a test
  passing through accidental type coercion or overflow.

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

## Customisation

There's room for custom assertions throughout the DSL, and if necessary,
the `Satisfies` interface, allows a condition to be added fluently. Conditions
are based on the `Condition` class. The existing conditions can be used directly
if necessary, and can be composed using `Condition.and` where needed. Similarly, there's
a `not` method in the `Condition` class `Not` to invert any condition.

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

