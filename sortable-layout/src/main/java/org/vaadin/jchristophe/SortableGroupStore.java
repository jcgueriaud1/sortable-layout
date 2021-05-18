package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Store the components drag from one layout
 * and supply them to the other layout
 *
 * @author jcgueriaud
 */
public class SortableGroupStore {

    private List<Component> removeComponents = new ArrayList<>();

    public void addRemoveComponent(Component removeComponent) {
        this.removeComponents.add(removeComponent);
    }

    public List<Component> getRemoveComponents() {
        return new ArrayList<>(removeComponents);
    }

    public void clearRemoveComponents() {
        removeComponents.clear();
    }

}
