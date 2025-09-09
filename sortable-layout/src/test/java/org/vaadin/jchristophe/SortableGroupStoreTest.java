package org.vaadin.jchristophe;

import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortableGroupStoreTest {

    private SortableGroupStore sortableGroupStore;

    @BeforeEach
    void setUp() {
        sortableGroupStore = new SortableGroupStore();
    }

    @Test
    void testAddAndGetRemoveComponents() {
        Div component1 = new Div();
        Div component2 = new Div();

        sortableGroupStore.addRemoveComponent(component1);
        sortableGroupStore.addRemoveComponent(component2);

        assertThat(sortableGroupStore.getRemoveComponents()).hasSize(2).contains(component1, component2);
    }

    @Test
    void testClearRemoveComponents() {
        Div component1 = new Div();
        sortableGroupStore.addRemoveComponent(component1);

        sortableGroupStore.clearRemoveComponents();

        assertThat(sortableGroupStore.getRemoveComponents()).isEmpty();
    }

    @Test
    void testGetRemoveComponentsReturnsCopy() {
        Div component1 = new Div();
        sortableGroupStore.addRemoveComponent(component1);

        sortableGroupStore.getRemoveComponents().clear();

        assertThat(sortableGroupStore.getRemoveComponents()).hasSize(1);
    }
}
