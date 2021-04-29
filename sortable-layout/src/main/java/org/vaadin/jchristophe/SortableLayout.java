package org.vaadin.jchristophe;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Tag("sortable")
@NpmPackage(value = "sortablejs", version = "1.10.2")
@JavaScript("./sortableConnector.js")
public class SortableLayout extends Div {

    private Logger logger = Logger.getLogger("SortableLayout");

    @FunctionalInterface
    public interface CloneFunction {

        /**
         * Clone the component
         *
         * @param component
         * @return clone of the component
         */
        Component clone(Component component);
    }

    private CloneFunction cloneFunction;
    private SerializableConsumer<Component> onOrderChanged;

    private final Component layout;

    private boolean disabledSort = false;

    private Supplier<Component> supplyComponentFunction;
    private Consumer<Component> storeComponentFunction;

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

    public void setDisabledSort(boolean disabled) {
        this.disabledSort = disabled;
        setOption("disabled", disabled);
    }

    public void setHandle(String value) {
        setOption("handle", "."+value);
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
    private void onReorderListener(int oldIndex, int newIndex) {
        //System.out.println("onReorderListener");
        logger.finest("Reorder listener called drag index=" + oldIndex
                + " drop index= " + newIndex);
        Component component = getComponents().get(oldIndex);
        ((HasComponents) getLayout()).remove(component);
        ((HasComponents) getLayout()).addComponentAtIndex(newIndex, component);
        if (onOrderChanged != null) {
            onOrderChanged.accept(component);
        }
        fireEvent(new SortableComponentReorderEvent(this,true, component));
    }

    @ClientCallable
    private void onAddListener(int newIndex, boolean clone) {
        logger.finest("Add listener called drop index=" + newIndex);
        Component addedComponent = supplyComponentFunction.get();
        ((HasComponents) getLayout()).addComponentAtIndex(newIndex, addedComponent);

        if (onOrderChanged != null) {
            onOrderChanged.accept(addedComponent);
        }
        fireEvent(new SortableComponentAddEvent(this,true, addedComponent));
    }

    @ClientCallable
    private void onRemoveListener(int oldIndex, boolean clone) {
        logger.finest("remove listener called drag index=" + oldIndex);
        Component removedComponent = getComponents().get(oldIndex);
        storeComponentFunction.accept(removedComponent);
        if (clone) { // remove the component if clone and replace it by a clone
            ((HasComponents) getLayout()).remove(removedComponent);
            Component clonedComponent = cloneFunction.clone(removedComponent);
            ((HasComponents) getLayout()).addComponentAtIndex(oldIndex, clonedComponent);
        }
        if (onOrderChanged != null) {
            onOrderChanged.accept(removedComponent);
        }
        fireEvent(new SortableComponentDeleteEvent(this,true, removedComponent));
    }

    /**
     * @deprecated  use the listener instead
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
        supplyComponentFunction = group::getRemoveComponent;
        storeComponentFunction = group::setRemoveComponent;
    }
    /**
     * Adds a add listener to this component. Called when a component is dropped inside the component
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addSortableComponentAddListener(ComponentEventListener<SortableComponentAddEvent> listener) {
        return addListener(SortableComponentAddEvent.class, (ComponentEventListener) listener);
    }
    /**
     * Adds a delete listener to this component. Called when a component is dropped outside the component
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addSortableComponentDeleteListener(ComponentEventListener<SortableComponentDeleteEvent> listener) {
        return addListener(SortableComponentDeleteEvent.class, (ComponentEventListener) listener);
    }
    /**
     * Adds a reorder listener to this component.
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addSortableComponentReorderListener(ComponentEventListener<SortableComponentReorderEvent> listener) {
        return addListener(SortableComponentReorderEvent.class, (ComponentEventListener) listener);
    }

    public static class SortableComponentAddEvent extends ComponentEvent<SortableLayout> {

        private final Component component;

        public SortableComponentAddEvent(SortableLayout source, boolean fromClient, Component component) {
            super(source, fromClient);
            this.component = component;
        }

        /**
         *
         * @return added component
         */
        public Component getComponent() {
            return component;
        }
    }

    public static class SortableComponentDeleteEvent extends ComponentEvent<SortableLayout> {

        private final Component component;

        public SortableComponentDeleteEvent(SortableLayout source, boolean fromClient, Component component) {
            super(source, fromClient);
            this.component = component;
        }

        /**
         *
         * @return deleted component
         */
        public Component getComponent() {
            return component;
        }
    }
    public static class SortableComponentReorderEvent extends ComponentEvent<SortableLayout> {

        private final Component component;

        public SortableComponentReorderEvent(SortableLayout source, boolean fromClient, Component component) {
            super(source, fromClient);
            this.component = component;
        }

        /**
         *
         * @return reordered component
         */
        public Component getComponent() {
            return component;
        }
    }
}
