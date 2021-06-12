package uk.org.webcompere.modelassert.json.condition;

import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.Condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.condition.ConditionList.conditions;

class ConditionListTest {

    @Test
    void cannotCreateAnEmptyConditionList() {
        assertThatThrownBy(() -> conditions().toCondition())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void conditionListOfOneConditionIsJustThatCondition() {
        Condition someCondition = new HasSize(1);

        Condition amalgamatedCondition = conditions().satisfies(someCondition).toCondition();
        assertThat(amalgamatedCondition).isSameAs(someCondition);
    }

    @Test
    void conditionListOfTwoConditionsIsNeither() {
        Condition someCondition1 = new HasSize(1);
        Condition someCondition2 = new HasSize(2);

        Condition amalgamatedCondition = conditions()
            .satisfies(someCondition1)
            .satisfies(someCondition2)
            .toCondition();

        assertThat(amalgamatedCondition)
            .isNotSameAs(someCondition1)
            .isNotSameAs(someCondition2);
    }
}
