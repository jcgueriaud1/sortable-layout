package org.vaadin.jchristophe;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

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
        logger.info("Reorder listener called drag index=" + oldIndex
                + " drop index= " + newIndex);
        Component component = getComponents().get(oldIndex);
        ((HasComponents) getLayout()).remove(component);
        ((HasComponents) getLayout()).addComponentAtIndex(newIndex, component);
        if (onOrderChanged != null) {
            onOrderChanged.accept(component);
        }
    }

    @ClientCallable
    private void onAddListener(int newIndex) {
        logger.info("Add listener called drop index=" + newIndex);
        Component removedComponent = supplyComponentFunction.get();
        ((HasComponents) getLayout()).addComponentAtIndex(newIndex, removedComponent);
        if (onOrderChanged != null) {
            onOrderChanged.accept(removedComponent);
        }
    }

    @ClientCallable
    private void onRemoveListener(int oldIndex) {
        logger.info("remove listener called drag index=" + oldIndex);
        Component removedComponent = getComponents().get(oldIndex);
        storeComponentFunction.accept(removedComponent);
        //group.setRemoveComponent(removedComponent);
        ((HasComponents) getLayout()).remove(removedComponent);
        if (onOrderChanged != null) {
            onOrderChanged.accept(removedComponent);
        }
    }

    public void setOnOrderChanged(SerializableConsumer<Component> onOrderChanged) {
        this.onOrderChanged = onOrderChanged;
    }

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

    ///// GROUP
    public void setGroup(SortableGroupStore group) {
        supplyComponentFunction = group::getRemoveComponent;
        storeComponentFunction = group::setRemoveComponent;
    }
}
