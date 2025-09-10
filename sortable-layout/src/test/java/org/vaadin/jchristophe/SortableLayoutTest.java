package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Div previousContainer = new Div(component2);
        SortableGroupStore group = new SortableGroupStore();
        group.addRemoveComponent(component2);
        sortableLayout = new SortableLayout(new Div(component1), new SortableConfig(), group);

        JsonArray newIndexes = Json.createArray();
        newIndexes.set(0, 1);
        assertEquals(1L, previousContainer.getChildren().count());

        // when
        sortableLayout.onAddListener(newIndexes, false);

        // then
        List<Component> components = sortableLayout.getComponents();
        assertThat(components).containsExactly(component1, component2);

        assertEquals(0L, previousContainer.getChildren().count());
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

    @Test
    void onRemoveListener_shouldRemoveComponentsSidenav() {
        // given
        var component1 = new SideNavItem("test1");
        component1.setId("1");
        var component2 = new SideNavItem("test2");
        component2.setId("2");
        SortableGroupStore group = new SortableGroupStore();
        SideNav sideNav = new SideNav();
        sideNav.addItem(component1,  component2);
        sortableLayout = new SortableLayout(sideNav, new SortableConfig(), group);

        JsonArray oldIndexes = Json.createArray();
        oldIndexes.set(0, 0);

        // when
        sortableLayout.onRemoveListener(oldIndexes, false);

        // then
        assertThat(group.getRemoveComponents()).containsExactly(component1);
    }
    @Test
    void addChooseListener_shouldBeCalled() {
        // given
        sortableLayout = new SortableLayout(new Div());
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        sortableLayout.addChooseListener(event -> listenerCalled.set(true));

        // when
        ComponentUtil.fireEvent(sortableLayout, new SortableLayout.ChooseEvent(sortableLayout, false));

        // then
        assertThat(listenerCalled.get()).isTrue();
    }

    @Test
    void addUnchooseListener_shouldBeCalled() {
        // given
        sortableLayout = new SortableLayout(new Div());
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        sortableLayout.addUnchooseListener(event -> listenerCalled.set(true));

        // when
        ComponentUtil.fireEvent(sortableLayout, new SortableLayout.UnchooseEvent(sortableLayout, false));

        // then
        assertThat(listenerCalled.get()).isTrue();
    }

    @Test
    void addChangeListener_shouldBeCalled() {
        // given
        sortableLayout = new SortableLayout(new Div());
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        sortableLayout.addChangeListener(event -> listenerCalled.set(true));

        // when
        ComponentUtil.fireEvent(sortableLayout, new SortableLayout.ChangeEvent(sortableLayout, false));

        // then
        assertThat(listenerCalled.get()).isTrue();
    }

    @Test
    void addSortableComponentReorderListener_shouldBeCalled() {
        // given
        sortableLayout = new SortableLayout(new Div());
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        sortableLayout.addSortableComponentReorderListener(event -> listenerCalled.set(true));

        // when
        ComponentUtil.fireEvent(sortableLayout, new SortableLayout.SortableComponentReorderEvent(sortableLayout, false, java.util.Collections.emptyList(), 0));

        // then
        assertThat(listenerCalled.get()).isTrue();
    }

    @Test
    void addSortableComponentDeleteListener_shouldBeCalled() {
        // given
        sortableLayout = new SortableLayout(new Div());
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        sortableLayout.addSortableComponentDeleteListener(event -> listenerCalled.set(true));

        // when
        ComponentUtil.fireEvent(sortableLayout, new SortableLayout.SortableComponentDeleteEvent(sortableLayout, false, java.util.Collections.emptyList()));

        // then
        assertThat(listenerCalled.get()).isTrue();
    }

    @Test
    void addSortableComponentAddListener_shouldBeCalled() {
        // given
        sortableLayout = new SortableLayout(new Div());
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        sortableLayout.addSortableComponentAddListener(event -> listenerCalled.set(true));

        // when
        ComponentUtil.fireEvent(sortableLayout, new SortableLayout.SortableComponentAddEvent(sortableLayout, false, java.util.Collections.emptyList()));

        // then
        assertThat(listenerCalled.get()).isTrue();
    }

    @Test
    void getComponents_withComposite_shouldReturnInnerComponents() {
        // given
        Div innerLayout = new Div();
        innerLayout.add(new Button("Button 1"));
        innerLayout.add(new Button("Button 2"));
        com.vaadin.flow.component.Composite<Div> composite = new com.vaadin.flow.component.Composite<Div>() {
            @Override
            protected Div initContent() {
                return innerLayout;
            }
        };
        sortableLayout = new SortableLayout(composite);

        // when
        List<Component> components = sortableLayout.getComponents();

        // then
        assertThat(components).hasSize(2);
        assertThat(components).containsExactly(innerLayout.getChildren().toArray(Component[]::new));
    }
}