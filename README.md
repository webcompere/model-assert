# ModelAssert

Assertions for model data. Inspired by [JSONAssert](https://github.com/skyscreamer/JSONassert)
and [AssertJ](https://assertj.github.io/doc/). Built on top of [Jackson](https://github.com/FasterXML/jackson).

Intended as a richer way of writing assertions in unit tests, and as
a more powerful alternative to Spring's [`jsonPath`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html#jsonPath-java.lang.String-org.hamcrest.Matcher-).

## Installation

t.b.c.

## Quickstart

```java
String json = ".... some json ...";

// assertJ style
assertJson(json)
   .at("/name")
   .isEqualTo("ModelAssert");

// hamcrest style
MatcherAssert.assertThat(json, 
        json().at("/name")
           .isEqualTo());
```

`at` is one possible condition, in this case using Jackson's JSON Pointer syntax.

The `assertJson` methods produces stand-alone assertions which
execute each clause in order, stopping on error.

The `json*` methods - `json`, `jsonFile`, `jsonFilePath` start the
construction of a hamcrest matcher which conditions are added to.
These are executed when the hamcrest matcher is executed.

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
        .isEqualTo("ModelAssert"))
```

While this syntax is of limited value in this simple case, it opens up the full
power of the assertion library.

## Building the Assertion

The entry point to creating an assertion is:

- `assertJson` - overloaded to take JSON as `String`, `File` or `Path` - **produces a fluent assertion like AssertJ**
- `json` - start creating a hamcrest matcher from a `String`
- `jsonFile` - start creating a hamcrest matcher from a `File`
- `jsonFilePath` - start creating a hamcrest matcher from a `Path`

After that, there are high level methods to add conditions to the matcher:

- `at` - start creating a JSON Pointer based assertion
- `isNull` - assert that the whole loaded JSON amounts to `null`
- `satisfies` - plug in a custom `Condition`

When a condition has been added to the assertion then the fluent interface
allows for further condition to be added.

> Note: the `assertJson` version executes all conditions on the fly, where the hamcrest
version stores them for execution when the matcher is invoked by `MatcherAssert.assertThat`
or similar.

## Assertions

### Json At

Build a `JsonAt` condition by using `.at("/some/json/pointer")`.

This allows assertions within the tree:

- `isEqualTo` - assert that a field has a specific value
- `isNull` - assert that this path resolves to `null`
- `isMissing` - assert that this path resolves to _missing_ - i.e. it's an unknown path in the JSON
- `matches` - assert that the **text** of this node matches a regular expression - some common patterns are available in the `Patterns` class
