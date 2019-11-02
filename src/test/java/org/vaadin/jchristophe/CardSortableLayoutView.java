package org.vaadin.jchristophe;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.content.IconItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("card")
public class CardSortableLayoutView extends Div {

    private HorizontalLayout mainLayout = new HorizontalLayout();

    public CardSortableLayoutView() {
        SortableBehaviour sortableBehaviour = buildCardPanel();
        SortableBehaviour cardPanelDragOnIcon = buildCardPanelDragOnIcon();
        mainLayout.addAndExpand(sortableBehaviour, cardPanelDragOnIcon);
        mainLayout.setSizeFull();
        add(mainLayout);
    }

    private SortableBehaviour buildCardPanel() {

        VerticalLayout verticalLayout;
        SortableBehaviour sortableBehaviour;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        for (int i = 0; i < 15; i++) {

            Card card = new Card(
                    new IconItem(VaadinIcon.MAGNET.create(),"Card " + i, "The entire card is draggable "));
            card.setWidthFull();
            card.setId("ID "+ i);
            verticalLayout.add(card);
        }
        sortableBehaviour = new SortableBehaviour(verticalLayout);

        sortableBehaviour.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableBehaviour.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });
        return sortableBehaviour;
    }


    private SortableBehaviour buildCardPanelDragOnIcon() {

        VerticalLayout verticalLayout;
        SortableBehaviour sortableBehaviour;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        sortableBehaviour = new SortableBehaviour(verticalLayout);
        String cssMove = "my-handle";
        sortableBehaviour.setHandle(cssMove);
        for (int i = 0; i < 15; i++) {

            Icon icon = VaadinIcon.ARROWS.create();
            icon.setClassName(cssMove);
            Card card = new Card(
                    new IconItem(icon,"Card " + i, "You can drag the card with the icon "+ i));
            card.getStyle().set("background-color","#dddddd");
            card.setWidthFull();
            card.setId("ID "+ i);
            verticalLayout.add(card);
        }

        sortableBehaviour.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableBehaviour.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });
        return sortableBehaviour;
    }
}
