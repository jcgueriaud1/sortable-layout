package org.vaadin.jchristophe;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Integer.min;

/**
 * This invisible layout adds drag and drop capacity to the component added.
 *
 *
 */
@Tag("sortable")
@NpmPackage(value = "sortablejs", version = "1.13.0")
@JavaScript("./sortableConnector.js")
public class SortableLayout extends Div {

    private final Logger logger = Logger.getLogger("SortableLayout");

    @FunctionalInterface
    public interface CloneFunction {

        /**
         * Clone the component
         *
         * @param component component to clone
         * @return clone of the component
         */
        Component clone(Component component);
    }

    private CloneFunction cloneFunction;
    private SerializableConsumer<Component> onOrderChanged;

    private final Component layout;

    private boolean disabledSort = false;

    private Supplier<List<Component>> supplyComponentFunction;
    private Consumer<Component> storeComponentFunction;
    private Runnable clearComponentFunction;

    /**
     * Make the layout reorderable
     *
     * @param layout component to be reordered must implements HasComponents
     */
    public SortableLayout(Component layout) {
        this(layout, new SortableConfig());
    }

    public SortableLayout(Component layout, SortableConfig config) {
        this(layout, config, null);
    }

    public SortableLayout(Component layout, SortableConfig config, SortableGroupStore groupStore) {
        this(layout, config, groupStore, null);
    }

    public SortableLayout(Component layout, SortableConfig config,
                          SortableGroupStore groupStore, CloneFunction cloneFunction) {
        this.layout = layout;
        if (!(getLayout() instanceof HasComponents)) {
            throw new IllegalArgumentException("Layout must implements HasComponents");
        }
        add(layout);
        initConnector(layout.getElement(), config);
        if (groupStore != null) {
            setGroup(groupStore);
        } else {
            if (config.requireGroupStore()) {
                throw new IllegalArgumentException("Group store is required if you want to DnD between 2 lists");
            }
        }
        if (cloneFunction != null) {
            this.cloneFunction = cloneFunction;
        } else {
            if (config.requireCloneFunction()) {
                throw new IllegalArgumentException("Clone function is required if you want to clone component");
            }
        }

    }

    private void initConnector(Element layout, SortableConfig config) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.sortableConnector.initLazy($0, $1, $2)", config.toJson(),
                getElement(), layout));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        layout.getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(layout, context -> command.accept(ui)));
    }

    public boolean isDisabledSort() {
        return disabledSort;
    }

    /**
     * Disables the sortable if set to true.
     *
     * @param disabled
     */
    public void setDisabledSort(boolean disabled) {
        this.disabledSort = disabled;
        setOption("disabled", disabled);
    }

    /**
     * Drag handle selector within list items
     * @param cssClassname classname of the element to drag
     */
    public void setHandle(String cssClassname) {
        setOption("handle", "."+cssClassname);
    }

    public void setAnimation(int value) {
        setOption("animation", value);
    }

    public void setOption(String option, String value) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setOption", option,
                        value));
    }

    public void setOption(String option, boolean value) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setOption", option,
                        value));
    }

    public void setOption(String option, int value) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setOption", option,
                        value));
    }

    @ClientCallable
    private void onReorderListener(JsonArray oldIndexes, JsonArray newIndexes) {
        List<Component> components = new ArrayList<>();
        int smallestNewIndex = Integer.MAX_VALUE;
        for (int i = 0; i < oldIndexes.length(); i++) {
            int oldIndex = (int) oldIndexes.get(i).asNumber();
            int newIndex = (int) newIndexes.get(i).asNumber();
            smallestNewIndex = min(newIndex, smallestNewIndex);
            logger.severe("Reorder listener called drag index=" + oldIndex
                    + " drop index= " + newIndex);
            Component component = getComponents().get(oldIndex);
            ((HasComponents) getLayout()).remove(component);
            ((HasComponents) getLayout()).addComponentAtIndex(newIndex, component);
            if (onOrderChanged != null) {
                onOrderChanged.accept(component);
            }
        }

        fireEvent(new SortableComponentReorderEvent(this,true, components));
    }

    @ClientCallable
    protected void onAddListener(JsonArray newIndexes, boolean clone) {
        List<Component> addedComponents = supplyComponentFunction.get();
        int smallestNewIndex = Integer.MAX_VALUE;
        for (int i = 0; i < newIndexes.length(); i++) {
            int newIndex = (int) newIndexes.get(i).asNumber();
            if (newIndex < 0) {
                // there is an issue in sortable layout when the index can be negative
                logger.severe("Add listener called drop index=" + newIndex);
            } else {
                smallestNewIndex = min(newIndex, smallestNewIndex);
            }
        }
        // add all the entries in the
        Element[] elements = addedComponents.stream().map(Component::getElement).toArray(Element[]::new);
        getLayout().getElement().insertChild(smallestNewIndex, elements);

        for (int i = 0; i < addedComponents.size(); i++) {
            Component addedComponent = addedComponents.get(i);
            if (onOrderChanged != null) {
                onOrderChanged.accept(addedComponent);
            }
        }
        clearComponentFunction.run();
        fireEvent(new SortableComponentAddEvent(this, true, addedComponents));
    }

    @ClientCallable
    private void onRemoveListener(JsonArray oldIndexes, JsonArray newIndexes, boolean clone) {
        System.out.println("Before remove "+getLayout().getChildren().count());
        List<Component> removedComponents = new ArrayList<>();
        for (int i = 0; i < oldIndexes.length(); i++) {
            int oldIndex = (int) oldIndexes.get(i).asNumber();
            logger.severe("remove listener called drag index=" + oldIndex);
            Component removedComponent = getComponents().get(oldIndex);
            removedComponents.add(removedComponent);
        }
        for (int i = 0; i < removedComponents.size(); i++) {
            Component removedComponent = removedComponents.get(i);
            storeComponentFunction.accept(removedComponent);
            if (clone) { // remove the component if clone and replace it by a clone
                Component clonedComponent = cloneFunction.clone(removedComponent);
                replace((HasComponents) getLayout(), removedComponent, clonedComponent);
            }
            if (onOrderChanged != null) {
                onOrderChanged.accept(removedComponent);
            }
        }

        fireEvent(new SortableComponentDeleteEvent(this,true, removedComponents));
    }

    /**
     * @deprecated  use the listener addSortableComponentReorderListener instead
     *
     * @param onOrderChanged function called when a component is reordered or moved
     */
    @Deprecated
    public void setOnOrderChanged(SerializableConsumer<Component> onOrderChanged) {
        this.onOrderChanged = onOrderChanged;
    }

    /**
     *
     * @return the list of components in the right order
     */
    public List<Component> getComponents() {
        return getLayout().getChildren().collect(Collectors.toList());
    }

    private Component getLayout() {
        if (layout instanceof Composite) {
            return (((Composite) layout).getContent());
        } else {
            return layout;
        }
    }
    private void setGroup(SortableGroupStore group) {
        supplyComponentFunction = group::getRemoveComponents;
        storeComponentFunction = group::addRemoveComponent;
        clearComponentFunction = group::clearRemoveComponents;
    }
    /**
     * Adds a add listener to this component. Called when a component is dropped inside the component
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    public Registration addSortableComponentAddListener(ComponentEventListener<SortableComponentAddEvent> listener) {
        return addListener(SortableComponentAddEvent.class, listener);
    }
    /**
     * Adds a delete listener to this component. Called when a component is dropped outside the component
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addSortableComponentDeleteListener(ComponentEventListener<SortableComponentDeleteEvent> listener) {
        return addListener(SortableComponentDeleteEvent.class, listener);
    }
    /**
     * Adds a reorder listener to this component.
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addSortableComponentReorderListener(ComponentEventListener<SortableComponentReorderEvent> listener) {
        return addListener(SortableComponentReorderEvent.class, listener);
    }

    public static class SortableComponentAddEvent extends ComponentEvent<SortableLayout> {

        private final List<Component> components;

        public SortableComponentAddEvent(SortableLayout source, boolean fromClient, List<Component> components) {
            super(source, fromClient);
            this.components = components;
        }

        /**
         *
         * @return added component
         */
        public Component getComponent() {
            return components.get(0);
        }

        public List<Component> getComponents() {
            return components;
        }
    }

    public static class SortableComponentDeleteEvent extends ComponentEvent<SortableLayout> {

        private final List<Component> components;

        public SortableComponentDeleteEvent(SortableLayout source, boolean fromClient, List<Component> components) {
            super(source, fromClient);
            this.components = components;
        }

        /**
         *
         * @return deleted component
         */
        public Component getComponent() {
            return components.get(0);
        }

        public List<Component> getComponents() {
            return components;
        }
    }
    public static class SortableComponentReorderEvent extends ComponentEvent<SortableLayout> {

        private final List<Component> components;

        public SortableComponentReorderEvent(SortableLayout source, boolean fromClient, List<Component> components) {
            super(source, fromClient);
            this.components = components;
        }

        /**
         *
         * @return reordered component
         */
        public Component getComponent() {
            return components.get(0);
        }

        public List<Component> getComponents() {
            return components;
        }
    }

    private static void replace(HasComponents layout, Component oldComponent, Component newComponent) {
        if (oldComponent == null && newComponent == null) {
            // NO-OP
            return;
        }
        if (oldComponent == null) {
            layout.add(newComponent);
        } else if (newComponent == null) {
            layout.remove(oldComponent);
        } else {
            Element element = layout.getElement();
            int oldIndex = element.indexOfChild(oldComponent.getElement());
            int newIndex = element.indexOfChild(newComponent.getElement());
            if (oldIndex >= 0 && newIndex >= 0) {
                element.insertChild(oldIndex, newComponent.getElement());
                element.insertChild(newIndex, oldComponent.getElement());
            } else if (oldIndex >= 0) {
                element.setChild(oldIndex, newComponent.getElement());
            } else {
                layout.add(newComponent);
            }
        }
    }
}
