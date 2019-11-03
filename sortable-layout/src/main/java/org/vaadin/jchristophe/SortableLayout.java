package org.vaadin.jchristophe;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

import java.util.List;
import java.util.stream.Collectors;

@Tag("sortable")
@NpmPackage(value = "sortablejs", version = "1.10.1")
@JavaScript("sortablejs/Sortable.js")
@JavaScript("https://raw.githack.com/SortableJS/Sortable/master/Sortable.js")
@JavaScript("frontend://sortableConnector.js")
public class SortableLayout extends Div {

    private SerializableConsumer<Component> onOrderChanged;

    private final Component layout;

    private boolean disabledSort = false;

    public SortableLayout(Component layout) {
        this(layout, new SortableConfig());
    }

    public SortableLayout(Component layout, SortableConfig config) {
        if (!(layout instanceof HasComponents)) {
            throw new IllegalArgumentException("Layout must implements HasComponents");
        } else {
            this.layout = layout;
        }
        add(layout);
        initConnector(layout.getElement(), config);
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
        Component component = getComponents().get(oldIndex);
        ((HasComponents) layout).remove(component);
        ((HasComponents) layout).addComponentAtIndex(newIndex, component);
        if (onOrderChanged != null) {
            onOrderChanged.accept(component);
        }
    }

    public void setOnOrderChanged(SerializableConsumer<Component> onOrderChanged) {
        this.onOrderChanged = onOrderChanged;
    }

    public List<Component> getComponents() {
        return layout.getChildren().collect(Collectors.toList());
    }
}
