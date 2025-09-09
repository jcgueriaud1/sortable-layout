package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.internal.StateNode;
import elemental.json.Json;
import elemental.json.JsonArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SortableLayoutTest {

    private SortableLayout sortableLayout;


    @Test
    void testConstructorWithInvalidLayout() {
        assertThrows(IllegalArgumentException.class, () -> new SortableLayout(new Button()));
    }

    @Test
    void testConstructorWithValidLayout() {
        sortableLayout = new SortableLayout(new Div());
        assertTrue(sortableLayout.getComponents().isEmpty());
    }

    @Test
    void onReorderListener_shouldReorderComponents() {
        // given
        Div component1 = new Div();
        component1.setId("1");
        Div component2 = new Div();
        component2.setId("2");
        sortableLayout = new SortableLayout(new Div(component1, component2));

        JsonArray oldIndexes = Json.createArray();
        oldIndexes.set(0, 0);
        JsonArray newIndexes = Json.createArray();
        newIndexes.set(0, 1);

        // when
        sortableLayout.onReorderListener(oldIndexes, newIndexes);

        // then
        List<Component> components = sortableLayout.getComponents();
        assertThat(components).containsExactly(component2, component1);
    }

    @Test
    void onAddListener_shouldAddComponents() {
        // given
        Div component1 = new Div();
        component1.setId("1");
        Div component2 = new Div();
        component2.setId("2");
        SortableGroupStore group = new SortableGroupStore();
        group.addRemoveComponent(component2);
        sortableLayout = new SortableLayout(new Div(component1), new SortableConfig(), group);

        JsonArray newIndexes = Json.createArray();
        newIndexes.set(0, 1);

        // when
        sortableLayout.onAddListener(newIndexes, false);

        // then
        List<Component> components = sortableLayout.getComponents();
        assertThat(components).containsExactly(component1, component2);
    }

    @Test
    void onRemoveListener_shouldRemoveComponents() {
        // given
        Div component1 = new Div();
        component1.setId("1");
        Div component2 = new Div();
        component2.setId("2");
        SortableGroupStore group = new SortableGroupStore();
        sortableLayout = new SortableLayout(new Div(component1, component2), new SortableConfig(), group);

        JsonArray oldIndexes = Json.createArray();
        oldIndexes.set(0, 0);

        // when
        sortableLayout.onRemoveListener(oldIndexes, false);

        // then
        assertThat(group.getRemoveComponents()).containsExactly(component1);
    }
}