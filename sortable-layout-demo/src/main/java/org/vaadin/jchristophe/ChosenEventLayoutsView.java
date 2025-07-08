package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Demo with chosen event")
@Route(value = "chosen-twolayouts", layout = MainLayout.class)
public class ChosenEventLayoutsView extends Main {

    private SortableLayout rightSortableLayout;
    private SortableLayout leftSortableLayout;

    public ChosenEventLayoutsView() {

        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setGroupName("shared");
        sortableConfig.allowDragIn(true);
        sortableConfig.allowDragOut(true);
        sortableConfig.cloneOnDragOut(true);
        sortableConfig.setAnimation(100);

        SortableGroupStore group = new SortableGroupStore();
        UnorderedList leftList = new UnorderedList();
        leftList.add(new ListItem("left item 1"),new ListItem("left item 2"),
                new ListItem("left item 3"));
        leftSortableLayout = new SortableLayout(leftList, sortableConfig,
                group, ChosenEventLayoutsView::cloneComponent);
        leftSortableLayout.addChooseListener(this::choose);
        leftSortableLayout.addUnchooseListener(this::unchoose);
        UnorderedList rightList = new UnorderedList();
        rightList.add(new ListItem("right item 1"),
                new ListItem("right item 2"),
                new ListItem("right item 3"));
        rightSortableLayout = new SortableLayout(rightList, sortableConfig,
                group, ChosenEventLayoutsView::cloneComponent);

        rightSortableLayout.addChooseListener(this::choose);
        rightSortableLayout.addUnchooseListener(this::unchoose);
        add(new Paragraph("""
                You can use `addChooseListener` and `addUnchooseListener` to run Java code when the user is starting to drag the item.
                That can be use to indicate where you can drag the component.
                """
        ), leftSortableLayout, rightSortableLayout);

        leftSortableLayout.addSortableComponentReorderListener(e -> showNotification(e.getComponent(), leftSortableLayout));
        rightSortableLayout.addSortableComponentReorderListener(e -> showNotification(e.getComponent(), rightSortableLayout));
    }

    private void choose(SortableLayout.ChooseEvent event) {
        rightSortableLayout.addClassName("chosen-layout");
        leftSortableLayout.addClassName("chosen-layout");

    }

    private void unchoose(SortableLayout.UnchooseEvent event) {
        rightSortableLayout.removeClassName("chosen-layout");
        leftSortableLayout.removeClassName("chosen-layout");
    }

    private static Component cloneComponent(Component component) {
        if (component instanceof ListItem) {
            return new ListItem(((ListItem) component).getText());
        }
        throw new IllegalArgumentException("Error");
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
