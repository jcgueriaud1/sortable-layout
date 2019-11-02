package org.vaadin.jchristophe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

@Route("")
public class TestView extends Div {


    public TestView() {
        MuuriComponent muuriComponent = new MuuriComponent();
        add(muuriComponent);
        Button button = new Button("Toogle drag mode");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(button);
    }
}
