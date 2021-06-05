package uk.org.webcompere.modelassert.json.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static uk.org.webcompere.modelassert.json.JsonAssertions.json;

@ExtendWith(MockitoExtension.class)
class MockitoArgumentMatcherTest {
    interface SomeInterface {
        String findValueFromJson(String json);
    }

    @Mock
    private SomeInterface someInterface;

    @Test
    void whenCallMockThenCanDetectItByJsonAssertion() {
        someInterface.findValueFromJson("{\"name\":\"foo\"}");

        then(someInterface)
                .should()
                .findValueFromJson(argThat(json()
                        .at("/name").hasValue("foo")
                        .toArgumentMatcher()));
    }

    @Test
    void whenCallMockThenAnswerCanBeBasedOnJson() {
        given(someInterface.findValueFromJson(argThat(json()
                .at("/name").hasValue("foo")
                .toArgumentMatcher())))
                .willReturn("foo");
        given(someInterface.findValueFromJson(argThat(json()
                .at("/name").hasValue("bar")
                .toArgumentMatcher())))
                .willReturn("bar");

        assertThat(someInterface.findValueFromJson("{}")).isNull();
        assertThat(someInterface.findValueFromJson("{\"name\":\"foo\"}")).isEqualTo("foo");
        assertThat(someInterface.findValueFromJson("{\"name\":\"bar\"}")).isEqualTo("bar");
    }
}