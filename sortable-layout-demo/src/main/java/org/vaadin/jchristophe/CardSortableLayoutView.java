package org.vaadin.jchristophe;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "card", layout = MainLayout.class)
public class CardSortableLayoutView extends Div {

    private HorizontalLayout mainLayout = new HorizontalLayout();

    public CardSortableLayoutView() {
      /*  SortableLayout sortableLayout = buildCardPanel();
        SortableLayout cardPanelDragOnIcon = buildCardPanelDragOnIcon();
        mainLayout.addAndExpand(sortableLayout, cardPanelDragOnIcon);
        mainLayout.setSizeFull();
        add(mainLayout);*/
    }
/*
    private SortableLayout buildCardPanel() {

        VerticalLayout verticalLayout;
        SortableLayout sortableLayout;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        for (int i = 0; i < 15; i++) {

            Card card = new Card(
                    new IconItem(VaadinIcon.MAGNET.create(),
                            new VerticalLayout(
                                    new Span("Card " + i),
                                    new Span("The entire card is draggable "),
                                    new Button("Test"))));
            card.setWidthFull();
            card.setId("ID "+ i);
            verticalLayout.add(card);
        }
        sortableLayout = new SortableLayout(verticalLayout);

        sortableLayout.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableLayout.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });
        return sortableLayout;
    }


    private SortableLayout buildCardPanelDragOnIcon() {

        VerticalLayout verticalLayout;
        SortableLayout sortableLayout;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        sortableLayout = new SortableLayout(verticalLayout);
        String cssMove = "my-handle";
        sortableLayout.setHandle(cssMove);
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

        sortableLayout.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableLayout.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });
        return sortableLayout;
    }*/
}
