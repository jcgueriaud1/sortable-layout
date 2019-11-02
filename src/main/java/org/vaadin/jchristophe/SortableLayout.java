package org.vaadin.jchristophe;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NpmPackage(value = "sortablejs", version = "1.10.1")
@JavaScript("sortablejs/Sortable.js")
@JavaScript("https://raw.githack.com/SortableJS/Sortable/master/Sortable.js")
@JavaScript("frontend://sortableConnector.js")
public class SortableLayout extends Div {

    private List<Component> children = new ArrayList<>();

    private SerializableConsumer<Component> onOrderChanged;

    private boolean sort = true;

    private String config;

    public SortableLayout() {
    }

    @Override
    public void add(Component... components) {
        children.addAll(Arrays.asList(components));
        super.add(components);
    }

    @Override
    public void remove(Component... components) {
        children.removeAll(Arrays.asList(components));
        super.remove(components);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.sortableConnector.initLazy($0, $1)", config,
                getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
        setOption("sort", sort);
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
        remove(component);
        addComponentAtIndex(newIndex, component);
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
        return getChildren().collect(Collectors.toList());
    }
}
