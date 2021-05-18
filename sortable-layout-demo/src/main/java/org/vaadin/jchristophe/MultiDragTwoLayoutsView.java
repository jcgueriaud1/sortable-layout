package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@CssImport("./demo.css")
@Route(value = "multidrag", layout = MainLayout.class)
public class MultiDragTwoLayoutsView extends HorizontalLayout {

    private SortableLayout rightSortableLayout;
    private SortableLayout leftSortableLayout;

    public MultiDragTwoLayoutsView() {

        SortableConfig multisortableConfig = new SortableConfig();
        multisortableConfig.setGroupName("shared");
        multisortableConfig.allowDragIn(true);
        multisortableConfig.allowDragOut(true);
        multisortableConfig.cloneOnDragOut(true);
        multisortableConfig.setAnimation(100);
        multisortableConfig.setMultiDrag(true);
        multisortableConfig.setSelectedClass("selected");


        SortableGroupStore group = new SortableGroupStore();
        UnorderedList leftList = new UnorderedList();
        leftList.add(new ListItem("left item 1"),new ListItem("left item 2"),
                new ListItem("left item 3"));
        leftSortableLayout = new SortableLayout(leftList, multisortableConfig,
                group, MultiDragTwoLayoutsView::cloneComponent);
        UnorderedList rightList = new UnorderedList();
        rightList.add(new ListItem("right item 1"),
                new ListItem("right item 2"),
                new ListItem("right item 3"));
        rightSortableLayout = new SortableLayout(rightList, multisortableConfig,
                group, MultiDragTwoLayoutsView::cloneComponent);

        add(leftSortableLayout, rightSortableLayout);

        leftSortableLayout.addSortableComponentReorderListener(e -> showNotification(e.getComponents(), leftSortableLayout));
        rightSortableLayout.addSortableComponentReorderListener(e -> showNotification(e.getComponents(), rightSortableLayout));
        rightSortableLayout.addSortableComponentAddListener(e -> showNotification(e.getComponents(), rightSortableLayout));
        leftSortableLayout.addSortableComponentAddListener(e -> showNotification(e.getComponents(), leftSortableLayout));
    }

    private static Component cloneComponent(Component component) {
        if (component instanceof ListItem) {
            return new ListItem(((ListItem) component).getText());
        }
        throw new IllegalArgumentException("Error");
    }

    private void showNotification(List<Component> components, SortableLayout sortableLayout) {
        StringBuilder items = new StringBuilder("Item moved ");
        for (Component component : components) {
            if (component instanceof HasText) {
                items.append(((HasText) component).getText());
            }
        }
        items.append(" - new List:");
        for (Component sortableLayoutComponent : sortableLayout.getComponents()) {
            if (sortableLayoutComponent instanceof HasText) {
                items.append(" ").append(((HasText) sortableLayoutComponent).getText());
            }
        }
        Notification.show(items.toString());
    }
}
