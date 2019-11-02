package org.vaadin.jchristophe;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag("sortable")
@NpmPackage(value = "sortablejs", version = "1.10.1")
@JavaScript("sortablejs/Sortable.js")
@JavaScript("https://raw.githack.com/SortableJS/Sortable/master/Sortable.js")
@JavaScript("frontend://sortableConnector.js")
public class SortableBehaviour extends Div {

    private SerializableConsumer<Component> onOrderChanged;

    private final Component layout;
    private boolean sort = true;

    private String config;

    public SortableBehaviour(Component layout) {
        if (!(layout instanceof HasComponents)) {
            throw new IllegalArgumentException("Layout must implements HasComponents");
        } else {
            this.layout = layout;
        }
        add(layout);
        initConnector(layout.getElement());
    }

    private void initConnector(Element layout) {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.sortableConnector.initLazy($0, $1, $2)", config,
                getElement(), layout));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        layout.getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(layout, context -> command.accept(ui)));
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
        setOption("sort", sort);
    }

    public void setHandle(String value) {
        setOption("handle", "."+value);
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
        System.out.println("order changed"+ oldIndex + "--" + newIndex);
        Component component = getComponents().get(oldIndex);
        ((HasComponents) layout).remove(component);
        ((HasComponents) layout).addComponentAtIndex(newIndex, component);
     //   children.remove(component);
     //   children.add(newIndex,component);
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
