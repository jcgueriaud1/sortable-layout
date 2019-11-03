package org.vaadin.jchristophe;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.demo.DemoView;
import com.vaadin.flow.router.Route;

@Route("")
public class SortableLayoutView extends DemoView {

    @Override
    protected void initView() {
        getElement().setAttribute("style","max-width:90%;");
        createBasicExample();
        createAnimationExample();
    }


    private void createBasicExample() {
        Div message = createMessageDiv("basic-message");

        // begin-source-example
        // source-example-heading: Basic Example
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"),new ListItem("item 2"),new ListItem("item 3"));
        SortableLayout sortableLayout = new SortableLayout(list);
        // end-source-example

        addCard("Basic Example", sortableLayout, message);
    }


    private void createAnimationExample() {
        Div message = createMessageDiv("animation-message");

        // begin-source-example
        // source-example-heading: Animation Example
        UnorderedList list = new UnorderedList();
        list.add(new ListItem("item 1"),new ListItem("item 2"),new ListItem("item 3"));
        SortableConfig config = new SortableConfig();
        config.setAnimation(300);
        SortableLayout sortableLayout = new SortableLayout(list,config);
        // end-source-example

        addCard("Animation Example", sortableLayout, message);
    }



    private Div createMessageDiv(String id) {
        Div message = new Div();
        message.setId(id);
        message.getStyle().set("whiteSpace", "pre");
        return message;
    }
}
