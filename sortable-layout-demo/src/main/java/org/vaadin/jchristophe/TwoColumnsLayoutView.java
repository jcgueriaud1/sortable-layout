package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@CssImport("./demo.css")
@Route("twocolumns")
public class TwoColumnsLayoutView extends HorizontalLayout {

    private SortableLayout rightSortableLayout;
    private SortableLayout leftSortableLayout;

    public TwoColumnsLayoutView() {

        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setGroupName("shared");
        sortableConfig.allowDragIn(true);
        sortableConfig.allowDragOut(true);
        sortableConfig.setAnimation(100);

        SortableGroupStore group = new SortableGroupStore();
        UnorderedList leftList = new UnorderedList();
        leftList.add(new ListItem("left item 1"),new ListItem("left item 2"),
                new ListItem("left item 3"));
        leftSortableLayout = new SortableLayout(leftList, sortableConfig, group);


        UnorderedList rightList = new UnorderedList();
        rightList.add(new ListItem("right item 1"),
                new ListItem("right item 2"),
                new ListItem("right item 3"));
        rightSortableLayout = new SortableLayout(rightList, sortableConfig, group);

        add(leftSortableLayout, rightSortableLayout);

        leftSortableLayout.setOnOrderChanged(c -> showNotification(c, leftSortableLayout));
        rightSortableLayout.setOnOrderChanged(c -> showNotification(c, rightSortableLayout));
    }

    private void showNotification(Component component, SortableLayout sortableLayout) {
        StringBuilder items = new StringBuilder("Item moved ");
        if (component instanceof HasText) {
            items.append(((HasText) component).getText());
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
