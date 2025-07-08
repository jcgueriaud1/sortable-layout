package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Basic demos")
@Route(value = "", layout = MainLayout.class)
public class SortableLayoutView extends Main {

    private final Span info;

    public SortableLayoutView() {
        addClassName(LumoUtility.Padding.MEDIUM);
        info = new Span("Event:");
        info.setId("info");
        add(info);
        createBasicExample();
        createAnimationExample();
        createGroupExample();
        createFilterExample();
        createColorExample();
        createMultiDragExample();

    }

    private void createBasicExample() {
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"),new ListItem("item 2"),new ListItem("item 3"));
        SortableLayout sortableLayout = new SortableLayout(list);
        sortableLayout.setId("basic-example");

        sortableLayout.addSortableComponentReorderListener(event -> {
            info.setText("Event: " + event.getIndex());
        });

        addExample("Basic Example", sortableLayout);
    }


    private void createAnimationExample() {
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"),new ListItem("item 2"),new ListItem("item 3"));
        SortableConfig config = new SortableConfig();
        config.setAnimation(300);
        SortableLayout sortableLayout = new SortableLayout(list,config);

        addExample("Animation Example", sortableLayout);
    }


    private void createGroupExample() {
        VerticalLayout layout = new VerticalLayout();
        UnorderedList foo = new UnorderedList();
        foo.add(new ListItem("foo 1"),new ListItem("foo 2"),new ListItem("foo 3"));
        SortableConfig sortableConfigFoo = new SortableConfig();
        sortableConfigFoo.setGroupName("foo");
        sortableConfigFoo.setAnimation(100);
        SortableGroupStore sortableGroupStore = new SortableGroupStore();
        SortableLayout sortableLayoutFoo = new SortableLayout(foo, sortableConfigFoo, sortableGroupStore);
        UnorderedList bar = new UnorderedList();
        bar.add(new ListItem("bar 1"),new ListItem("bar 2"),new ListItem("bar 3"));
        SortableConfig sortableConfigBar = new SortableConfig();
        sortableConfigBar.setGroupName("bar");
        sortableConfigBar.allowDragOut(true);
        sortableConfigBar.setAnimation(100);
        SortableLayout sortableLayoutBar = new SortableLayout(bar, sortableConfigBar, sortableGroupStore);

        UnorderedList qux = new UnorderedList();
        qux.add(new ListItem("qux 1"));
        SortableConfig sortableConfigQux = new SortableConfig();
        sortableConfigQux.setGroupName("qux");
        sortableConfigQux.addDragInGroupName("foo");
        sortableConfigQux.addDragInGroupName("bar");
        sortableConfigQux.setAnimation(100);
        SortableLayout sortableLayoutQux = new SortableLayout(qux, sortableConfigQux, sortableGroupStore);
        layout.add(sortableLayoutFoo,sortableLayoutBar,sortableLayoutQux);

        addExample("Group Example", layout);
    }


    private void createFilterExample() {
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"));
        ListItem listItemFilterable = new ListItem("item 2 - is not draggable");
        listItemFilterable.addClassName("ignore-elements");
        list.add(listItemFilterable);
        list.add(new ListItem("item 3"));
        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.addFilter("ignore-elements");
        SortableLayout sortableLayout = new SortableLayout(list, sortableConfig);

        addExample("Filter Example", sortableLayout);
    }


    private void createColorExample() {
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"),new ListItem("item 2"),new ListItem("item 3"));
        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setChosenClass("custom-sortable-chosen");
        sortableConfig.setDragClass("custom-sortable-drag");
        sortableConfig.setGhostClass("custom-sortable-ghost");
        SortableLayout sortableLayout = new SortableLayout(list, sortableConfig);

        addExample("Color Example", sortableLayout);
    }


    /// Not working on the server side
    private void createMultiDragExample() {
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"),new ListItem("item 2"),
                new ListItem("item 3"),new ListItem("item 4"),
                new ListItem("item 5"),new ListItem("item 6"),
                new ListItem("item 7"));
        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setMultiDrag(true);
        sortableConfig.setSelectedClass("selected");
        sortableConfig.setAnimation(150);
        SortableLayout sortableLayout = new SortableLayout(list, sortableConfig);

        addExample("Multi Drag Example", sortableLayout);
    }

    private void addExample(String title, Component component) {
        add(new H2(title), component);
    }

}
