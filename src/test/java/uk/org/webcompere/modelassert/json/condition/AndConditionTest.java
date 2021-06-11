package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AndConditionTest {

    @Mock
    private Condition condition1;

    @Mock
    private Condition condition2;

    @Mock
    private JsonNode mockNode;

    private AndCondition andCondition;

    @BeforeEach
    void beforeEach() {
        andCondition = new AndCondition(condition1, condition2);
    }

    @Test
    void whenFirstConditionFailsThenAndFailsAndShortCircuits() {
        given(condition1.test(any()))
            .willReturn(new Result("a", "b", false));

        assertThat(andCondition.test(mockNode).isPassed()).isFalse();
        then(condition2)
            .should(never())
            .test(any());
    }

    @Test
    void whenFirstConditionPassesAndSecondFailsThenAndFails() {
        given(condition1.test(any()))
            .willReturn(new Result("a", "b", true));

        given(condition2.test(any()))
            .willReturn(new Result("a", "b", false));

        assertThat(andCondition.test(mockNode).isPassed()).isFalse();
    }

    @Test
    void whenBothPassThenPasses() {
        given(condition1.test(any()))
            .willReturn(new Result("a", "b", true));

        given(condition2.test(any()))
            .willReturn(new Result("a", "b", true));

        assertThat(andCondition.test(mockNode).isPassed()).isTrue();
    }
}
