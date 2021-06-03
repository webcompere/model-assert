# ModelAssert

Assertions for model data. Inspired by [JSONAssert](https://github.com/skyscreamer/JSONassert)
and [AssertJ](https://assertj.github.io/doc/). Built on top of [Jackson](https://github.com/FasterXML/jackson).

Intended as a richer way of writing assertions in unit tests, and as
a more powerful alternative to Spring's [`jsonPath`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html#jsonPath-java.lang.String-org.hamcrest.Matcher-).

Describes paths using [JSON Pointer](https://gregsdennis.github.io/Manatee.Json/usage/pointer.html) syntax, where
a route to the element is a series of `/` delimited field names or array indices.

## Installation

t.b.c.

## Quickstart

```java
String json = "{.... some json ...}";

// assertJ style
assertJson(json)
   .at("/name").hasValue("ModelAssert");

// hamcrest style
MatcherAssert.assertThat(json,
    json()
        .at("/name").hasValue());
```

`at` is one possible condition, in this case using Jackson's JSON Pointer syntax.

The `assertJson` methods produces stand-alone assertions which
execute each clause in order, stopping on error.

The `json*` methods - `json`, `jsonNode`, `jsonFile`, `jsonFilePath` start the
construction of a hamcrest matcher which conditions are added to.
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
- Type specific - by calling `number`, `text`, `object`, `array`, or `boolean` on a node context DSL, the DSL
can be narrowed down to assertions for just that type - this can also be more expressive
  
```java
assertJson(json)
   .at("/name").text().isText("My Name");
```

### Json At

Build a `JsonAt` condition by using `.at("/some/json/pointer")`.

This is then followed by any of node context assertions.

Example:

```java
assertJson("{\"name\":null})
    .at("/name").isNull();
```

The `JsonAt` expression is incomplete with just `at`, but once the condition is added,
the `this` returned belongs to the main assertion, allowing them to be chained.

```java
assertJson("{\"name\":null}")
    .at("/name").isNull()
    .at("/address").isMissing();
```

### Node Context Assertions

These are available on any node of the tree, which might be any type. They include the
type specific assertions below, as well as:

- `hasValue` - assert that a field has a specific value
  ```java
  assertJson(jsonString)
    .at("/name").hasValue("ModelAssert");
  ```
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

### String Context Conditions

- `isText` - assert that the node is a text node, with optional specific text - note: this can also be achieved with `hasValue`, but adds
some extra checking that this is a text node
- `matches(Pattern|String)` - assert that the **text** of this node matches a regular expression - some common patterns are available in the `Patterns` class
  ```java
  assertJson(jsonString)
    .at("/guid").matches(GUID_PATTERN);
  ```

### Numeric Context Conditions
- `isGreaterThan`, `isGreaterThanOrEqualTo`, `isLessThan`, `isLessThanOrEqualTo` - these
  require that the node is a number of a numeric type, and compares
  ```java
  assertJson(jsonString)
     .at("/count").isGreaterThan(9);
  ```
  More specific typed versions - `isGreaterThanInt` or `isLessThanLong` also exist to avoid a test
  passing through accidental type coercion or overflow.
  
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

