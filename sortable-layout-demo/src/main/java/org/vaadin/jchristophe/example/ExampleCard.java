package org.vaadin.jchristophe.example;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.content.IconItem;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ExampleCard extends Card {

    public ExampleCard() {
        this("Card");
    }


    public ExampleCard(String title) {
        super(new IconItem(VaadinIcon.MAGNET.create(),title, "Drag with animate "));
    }
}
