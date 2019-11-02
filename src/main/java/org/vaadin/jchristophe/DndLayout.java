package org.vaadin.jchristophe;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;

import java.util.ArrayList;
import java.util.List;

@JavaScript(MuuriUtils.WEBANIM_LIB)
@JavaScript(MuuriUtils.MUURI_LIB)
@JavaScript("frontend://muuriConnector.js")
@CssImport("frontend://dnd-styles.css")
public class DndLayout extends Div {

    private final List<Component> children = new ArrayList<>();

    private boolean dragEnabled = true;

    private String config;

    private Div board = new Div();
    private Div boardColumnTodo = new Div();
    private Div boardColumnWorking = new Div();
    private Div boardColumnDone = new Div();

    public DndLayout() {
        addClassName("dnd-layout");
    }

    @Override
    public void add(Component... components) {
        for (Component component : components) {
            super.add(wrapItem(component));
            children.add(component);
        }
    }

    private Div wrapItem(Component component) {
        Div dndItem = new Div();
        dndItem.addClassNames("dnd-item");
        dndItem.add(component);
        return dndItem;
    }

    private void setupColumn(Div boardColumn, String title, String cssColumn) {
        boardColumn.addClassNames("board-column",cssColumn);
        Div header = new Div();
        header.addClassName("board-column-header");
        header.setText(title);
        Div content = new Div();
        content.addClassName("board-column-content");

        int size = 5;
        for (int i = 0; i < size; i++) {
            Div boardItem = new Div();
            boardItem.addClassName("board-item");
            Div boardItemContent = new Div();
            boardItemContent.addClassName("board-item-content");
            boardItemContent.add(new Button("Item "+ title + i));
            boardItem.add(boardItemContent);
            content.add(boardItem);
        }
        boardColumn.add(header,content);

    }
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.muuriConnector.initLazy($0, $1)", config,
                getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public boolean isDragEnabled() {
        return dragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
        setOption("dragEnabled", dragEnabled);
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
}
