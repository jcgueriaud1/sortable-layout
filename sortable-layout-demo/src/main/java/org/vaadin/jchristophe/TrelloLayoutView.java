package org.vaadin.jchristophe;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.vaadin.jchristophe.trello.TrelloColumn;
import org.vaadin.jchristophe.trello.TrelloTask;

@CssImport("./trello-demo.css")
@Route(value = "trello", layout = MainLayout.class)
public class TrelloLayoutView extends Div {

    private int taskNumber = 10;
    private SortableLayout todoSortableLayout;
    private SortableLayout inProgressSortableLayout;
    private SortableLayout completeSortableLayout;

    public TrelloLayoutView() {
        addClassName("trello");

        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setGroupName("trello");
        sortableConfig.allowDragIn(true);
        sortableConfig.allowDragOut(true);
        sortableConfig.setAnimation(150);
        sortableConfig.setChosenClass("trello-sortable-chosen");
        sortableConfig.setDragClass("trello-sortable-drag");
        sortableConfig.setGhostClass("trello-sortable-ghost");
        SortableGroupStore group = new SortableGroupStore();
        TrelloColumn todoColumn = new TrelloColumn("Todo");
        todoColumn.addClassName("trello__column--todo");
        addTasks(todoColumn, taskNumber);

        todoSortableLayout = new SortableLayout(todoColumn, sortableConfig,
                group);
        TrelloColumn inProgressColumn = new TrelloColumn("In progress");
        inProgressColumn.addClassName("trello__column--inprogress");
        inProgressSortableLayout = new SortableLayout(inProgressColumn, sortableConfig,
                group);
        TrelloColumn completeColumn = new TrelloColumn("Complete");
        completeColumn.addClassName("trello__column--complete");
        completeSortableLayout = new SortableLayout(completeColumn, sortableConfig,
                group);

        completeSortableLayout.addSortableComponentAddListener(e -> {
            if (e.getComponent() instanceof TrelloTask) {
                TrelloTask trelloTask = (TrelloTask) e.getComponent();
                Notification.show(trelloTask.getTitleText() + " has been completed");
            } else {
                Notification.show( " has been completed");
            }
        });
        /*completeSortableLayout.setOnOrderChanged(e -> {
            e
        });*/
        add(todoSortableLayout, inProgressSortableLayout, completeSortableLayout);
    }

    private void addTasks(TrelloColumn todoColumn, int taskNumber) {
        for (int i = 0; i < taskNumber; i++) {
            todoColumn.add(new TrelloTask("Task "+i, "Description of the task that can be long"));
        }
    }

}
