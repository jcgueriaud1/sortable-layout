package org.vaadin.jchristophe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("dnd")
public class DndLayoutView extends Div {

    private  DndLayout dndLayout;

    public DndLayoutView() {
        dndLayout = new DndLayout();
        dndLayout.add(new Button("btn 1"));
        dndLayout.add(new Button("btn 2"));
        dndLayout.add(new Button("btn 3"));
        dndLayout.add(new Button("btn 4"));
        add(dndLayout);

        Button button = new Button("Toogle drag mode", event -> toggleDndMode());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(button);
    }

    private void toggleDndMode() {
        dndLayout.setDragEnabled(!dndLayout.isDragEnabled());
    }
}
