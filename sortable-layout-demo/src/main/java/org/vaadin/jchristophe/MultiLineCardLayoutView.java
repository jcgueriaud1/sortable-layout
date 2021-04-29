package org.vaadin.jchristophe;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.content.IconItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@CssImport("./demo.css")
@Route(value = "multilinecard", layout = MainLayout.class)
public class MultiLineCardLayoutView extends Div {

    private HorizontalLayout mainLayout = new HorizontalLayout();

    public MultiLineCardLayoutView() {
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.getStyle().set("flex-wrap","wrap");
        SortableLayout sortableLayout = buildCardPanel();
        add(sortableLayout);
    }

    private SortableLayout buildCardPanel() {

        SortableLayout sortableLayout;
        mainLayout.setSizeFull();
        for (int i = 0; i < 40; i++) {

            Card card = new Card(
                    new IconItem(VaadinIcon.MAGNET.create(),"Card " + i, "Drag with animate "));
            card.setWidth("200px");
            card.setId("ID "+ i);
            mainLayout.add(card);
        }
        sortableLayout = new SortableLayout(mainLayout);
        sortableLayout.setAnimation(150);
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
}
