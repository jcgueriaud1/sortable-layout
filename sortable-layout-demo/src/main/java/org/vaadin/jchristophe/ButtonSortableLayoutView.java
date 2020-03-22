package org.vaadin.jchristophe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@CssImport("./demo.css")
@Route("buttons")
public class ButtonSortableLayoutView extends Div {

    private VerticalLayout verticalLayout;
    private SortableJS sortableLayout;

    public ButtonSortableLayoutView() {
        verticalLayout = new VerticalLayout();
        for (int i = 0; i < 5; i++) {
            Button button1 = new Button("btn HL " + i);
            button1.setId("ID " + i);
            button1.getElement().setAttribute("style", "pointer-events:none");
            verticalLayout.add(new Div(button1));
        }

        sortableLayout = new SortableJS(verticalLayout);
        // sortableLayout.setAnimation(150);
        add(sortableLayout);

       /* sortableLayout.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableLayout.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });*/

        Button button = new Button("Toogle drag mode", event -> toggleDndMode());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(button);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        for (int i = 0; i < 5; i++) {
            Button button1 = new Button("btn HL " + i);
            button1.setId("ID " + i);
            button1.getElement().setAttribute("style", "pointer-events:none");
            horizontalLayout.add(new Div(button1));
        }

        SortableJS sortableLayout2 = new SortableJS(horizontalLayout);
        add(sortableLayout2);

      /*  sortableLayout2.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components HL ");
            for (Component sortableLayoutComponent : sortableLayout2.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });*/
    }

    private void toggleDndMode() {
        //sortableLayout.setDisabledSort(!sortableLayout.isDisabledSort());
    }
}
