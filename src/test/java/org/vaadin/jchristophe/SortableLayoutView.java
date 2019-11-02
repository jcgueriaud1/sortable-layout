package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("sortable")
public class SortableLayoutView extends Div {

    private VerticalLayout verticalLayout;
    private SortableBehaviour sortableBehaviour;

    public SortableLayoutView() {
        verticalLayout = new VerticalLayout();
        for (int i = 0; i < 5; i++) {

            Button button1 = new Button("btn "+ i);
            button1.setId("ID "+ i);
            verticalLayout.add(button1);
        }
      //  add(verticalLayout);
        sortableBehaviour = new SortableBehaviour(verticalLayout);
        add(sortableBehaviour);

        sortableBehaviour.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableBehaviour.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });

        Button button = new Button("Toogle drag mode", event -> toggleDndMode());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(button);



         HorizontalLayout horizontalLayout = new HorizontalLayout();
        for (int i = 0; i < 5; i++) {

            Button button1 = new Button("btn HL "+ i);
            button1.setId("ID "+ i);
            horizontalLayout.add(button1);
        }

        SortableBehaviour sortableBehaviour2 = new SortableBehaviour(horizontalLayout);
        add(sortableBehaviour2);

        sortableBehaviour2.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components HL ");
            for (Component sortableLayoutComponent : sortableBehaviour2.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });
    }

    private void toggleDndMode() {
        sortableBehaviour.setSort(!sortableBehaviour.isSort());
    }
}
