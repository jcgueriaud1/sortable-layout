package org.vaadin.jchristophe.trello;

import com.vaadin.flow.component.html.Div;

/**
 * @author jcgueriaud
 */
public class TrelloTask extends Div {

    private String titleText;

    public TrelloTask(String titleText, String description) {
        this.titleText = titleText;
        Div titleBlock = new Div();
        titleBlock.setText(titleText);
        titleBlock.addClassName("trello__column__task__title");
        Div descriptionBlock = new Div();
        descriptionBlock.setText(description);
        descriptionBlock.addClassName("trello__column__task__description");

        add(titleBlock, descriptionBlock);
        addClassName("trello__column__task");
    }

    public String getTitleText() {
        return titleText;
    }
}
