package org.vaadin.jchristophe;

import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.css.grid.sizes.Flex;
import com.github.appreciated.css.grid.sizes.Length;
import com.github.appreciated.css.grid.sizes.MinMax;
import com.github.appreciated.css.grid.sizes.Repeat;
import com.github.appreciated.layout.FlexibleGridLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.vaadin.jchristophe.example.ExampleCard;

@CssImport("./demo.css")
@Route(value = "grid", layout = MainLayout.class)
public class GridSortableLayoutView extends Div {

    private FlexibleGridLayout flexibleGridLayout = new FlexibleGridLayout();
    private SortableLayout sortableLayout;

    public GridSortableLayoutView() {
        flexibleGridLayout
                .withColumns(Repeat.RepeatMode.AUTO_FIT, new MinMax(new Length("190px"), new Flex(1)))
                .withAutoRows(new Length("190px"))
                .withPadding(true)
                .withSpacing(true)
                .withAutoFlow(GridLayoutComponent.AutoFlow.ROW_DENSE)
                .withOverflow(GridLayoutComponent.Overflow.AUTO);

        for (int i = 0; i < 12; i++) {
            ExampleCard exampleCard = new ExampleCard("Card " + i);
            Button button = new Button("1x1");
            button.addClickListener(event -> {
                flexibleGridLayout.setItemRowHeight(exampleCard, 1);
                flexibleGridLayout.setItemWidth(exampleCard, 1);
            });
            exampleCard.add(button);
            Button button2 = new Button("2x1");
            button2.addClickListener(event -> {
                flexibleGridLayout.setItemWidth(exampleCard, 2);
                flexibleGridLayout.setItemRowHeight(exampleCard, 1);
            });
            exampleCard.add(button2);
            Button button12 = new Button("1x2");
            button12.addClickListener(event -> {
                flexibleGridLayout.setItemWidth(exampleCard, 1);
                flexibleGridLayout.setItemRowHeight(exampleCard, 2);
            });
            exampleCard.add(button12);
            Button button22 = new Button("2x2");
            button22.addClickListener(event -> {
                flexibleGridLayout.setItemWidth(exampleCard, 2);
                flexibleGridLayout.setItemRowHeight(exampleCard, 2);
            });
            exampleCard.add(button22);
            exampleCard.setId(i +"");
            flexibleGridLayout.add(exampleCard);
            if (i % 3 == 0) {
                flexibleGridLayout.setItemRowHeight(exampleCard, 2);
            }
            if (i % 4 == 0) {
                flexibleGridLayout.setItemWidth(exampleCard, 2);
            }
        }
        flexibleGridLayout.setSizeFull();
        setSizeFull();
        sortableLayout = new SortableLayout(flexibleGridLayout);
        sortableLayout.setAnimation(150);
        add(sortableLayout);

        sortableLayout.setOnOrderChanged(component -> {
            StringBuilder ids = new StringBuilder("components ");
            for (Component sortableLayoutComponent : sortableLayout.getComponents()) {
                if (sortableLayoutComponent.getId().isPresent()) {
                    ids.append(" ").append(sortableLayoutComponent.getId().get());
                }
            }

            Notification.show(ids.toString());
        });
    }
}
