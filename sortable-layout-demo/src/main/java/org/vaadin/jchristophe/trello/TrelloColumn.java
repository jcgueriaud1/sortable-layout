package org.vaadin.jchristophe.trello;

import com.vaadin.flow.component.html.Div;

/**
 * @author jcgueriaud
 */
public class TrelloColumn extends Div {

    private String title;

    public TrelloColumn(String title) {
        this.title = title;
        Div titleBox = new Div();
        titleBox.addClassName("trello__column__title");
        titleBox.setText(title);
        add(titleBox);
        addClassName("trello__column");
    }
}
