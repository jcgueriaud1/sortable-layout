package org.vaadin.jchristophe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Accessible drag and drop")
@Route(value = "accessible", layout = MainLayout.class)
public class AccessibleSortableView extends Main {

    private String moveUpLabelId = "up-label-id";
    private String moveDownLabelId = "down-label-id";

    public AccessibleSortableView() {
        addClassName(LumoUtility.Padding.MEDIUM);

        OrderedList list = new OrderedList();
        list.setId("sortable-list");
        list.addClassName("accessible-list");
        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setAnimation(100);
        SortableLayout sortableLayout = new SortableLayout(list, sortableConfig);
        sortableLayout.setDataIdAttr("data-sortable-id");

        for (int index = 0; index < 10; index++) {
            list.add(createListItem(sortableLayout, index));
        }
        Span moveUpLabel = new Span("Move up");
        moveUpLabel.setId(moveUpLabelId);
        moveUpLabel.addClassName(LumoUtility.Display.HIDDEN);
        Span moveDownLabel = new Span("Move down");
        moveDownLabel.setId(moveDownLabelId);
        moveDownLabel.addClassName(LumoUtility.Display.HIDDEN);
        add(new Paragraph("""
                There are many ways to make the drag and drop to sort a list accessible.
                The primary focus has been done for the keyboard action, which is not implemented by default.
                """), moveUpLabel, moveDownLabel, sortableLayout);
    }

    private ListItem createListItem(SortableLayout sortableLayout, int order) {
        String id = "sortable-list-content-" + order;
        Span span = new Span("item " + order);
        span.setId(id);
        Button upButton = new Button(VaadinIcon.ARROW_UP.create());
        upButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        upButton.setAriaLabelledBy(moveUpLabelId + " " + id);
        Button downButton = new Button(VaadinIcon.ARROW_DOWN.create());
        downButton.setAriaLabelledBy(moveDownLabelId + " " + id);
        downButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        ListItem listItem = new ListItem(upButton, downButton, span);
        upButton.addClickListener(e -> {
            sortableLayout.moveUp(listItem);
            upButton.focus();
        });
        downButton.addClickListener(e -> {
            sortableLayout.moveDown(listItem);
            downButton.focus();
        });
        return listItem;
    }
}
