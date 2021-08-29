package org.vaadin.jchristophe.layouteditor;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

/**
 * @author Martin Israelsen
 */
public class LayoutEditorField<FIELD> extends Composite<HorizontalLayout> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3817064579517262436L;
	private FIELD field;

	public LayoutEditorField(String name, FIELD field) {

		this.field = field;

		HorizontalLayout layout = getContent();
		layout.setAlignItems(Alignment.CENTER);
		Style s = layout.getStyle();
		s.set("background-color", "#dddddd");
		s.set("margin", "var(--lumo-space-s)");
		s.set("padding", "var(--lumo-space-s)");
		s.set("border-radius", "var(--lumo-border-radius)");

		Span fieldname = new Span(name);
		fieldname.addClassName("field-handle");

		layout.add(fieldname);

		Icon edit = VaadinIcon.ELLIPSIS_DOTS_H.create();
		edit.getStyle().set("color", "#777777");
		edit.setSize("1em");
		edit.addClickListener(e -> {
			Notification.show("Field clicked");
		});
		layout.add(edit);
	}

	public FIELD getField() {
		return field;
	}
}
