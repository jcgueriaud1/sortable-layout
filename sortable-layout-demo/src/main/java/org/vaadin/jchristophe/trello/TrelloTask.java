package org.vaadin.jchristophe.trello;

import com.vaadin.flow.component.html.Div;

/**
 * @author jcgueriaud
 */
public class TrelloTask extends Div {


    public TrelloTask(String title, String description) {
        Div titleBlock = new Div();
        titleBlock.setText(title);
        titleBlock.addClassName("trello__column__task__title");
        Div descriptionBlock = new Div();
        descriptionBlock.setText(description);
        descriptionBlock.addClassName("trello__column__task__description");

        add(titleBlock, descriptionBlock);
        addClassName("trello__column__task");
    }
}
