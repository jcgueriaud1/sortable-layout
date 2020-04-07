package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;

/**
 * Store the component drag from one layout
 * and supply it to the other layout
 *
 * @author jcgueriaud
 */
public class SortableGroupStore {

    private Component removeComponent;

    public void setRemoveComponent(Component removeComponent) {
        this.removeComponent = removeComponent;
    }

    public Component getRemoveComponent() {
        return removeComponent;
    }
}
