package uk.org.webcompere.modelassert.json;

import org.opentest4j.AssertionFailedError;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;
import static uk.org.webcompere.modelassert.json.JsonAssertions.json;

public class TestAssertions {

    public static void assertAllWays(String actualThatPasses,
                                      String actualThatFails,
                                      Consumer<CoreJsonAssertion<?, ?>> addRules) {
        addRules.accept(assertJson(actualThatPasses));

        assertThatThrownBy(() -> addRules.accept(assertJson(actualThatFails)))
                .describedAs("Negative test of fails")
                .isInstanceOf(AssertionFailedError.class);

        CoreJsonAssertion assertion = json().assertion();
        addRules.accept(assertion);
        assertThat(actualThatPasses, assertion);
        assertThat(actualThatFails, not(assertion));
    }
}
