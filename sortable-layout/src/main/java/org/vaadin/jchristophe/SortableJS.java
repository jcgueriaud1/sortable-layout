package org.vaadin.jchristophe;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Tag("sortable-js")
@NpmPackage(value = "sortablejs", version = "1.10.2")
@JsModule("./polymer-sortablejs.js")
public class SortableJS extends PolymerTemplate<SortableJSModel> implements HasSize, HasComponents {

    public SortableJS(Component... components) {
        add(components);
    }
}
