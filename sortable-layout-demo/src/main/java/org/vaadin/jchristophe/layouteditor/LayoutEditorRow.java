package org.vaadin.jchristophe.layouteditor;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;
import org.vaadin.jchristophe.SortableConfig;
import org.vaadin.jchristophe.SortableGroupStore;
import org.vaadin.jchristophe.SortableLayout;

/**
 * @author Martin Israelsen
 */
public class LayoutEditorRow<FIELD> extends Composite<HorizontalLayout> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4400265825834628717L;

	private final HorizontalLayout fields = new HorizontalLayout();

	private SortableLayout sortableLayout;

	private Icon moveIcon;
	private Icon addIcon;
	private Icon removeIcon;

	public LayoutEditorRow(SortableConfig sortableConfig, SortableGroupStore sortableGroupStore) {
		HorizontalLayout layout = getContent();
		layout.setAlignItems(Alignment.CENTER);

		Style s = layout.getStyle();
		s.set("background-color", "#eeeeee");
		s.set("padding", "var(--lumo-space-s)");
		s.set("border-radius", "var(--lumo-border-radius)");
		s.set("min-height", "40px");
		s.set("width", "100%");

		moveIcon = VaadinIcon.MENU.create();
		moveIcon.getStyle().set("color", "#dddddd");
		moveIcon.setClassName("row-handle");

		layout.add(moveIcon);

		sortableLayout = new SortableLayout(fields, sortableConfig, sortableGroupStore);
		sortableLayout.setHandle("field-handle");
		sortableLayout.setMinWidth("100px");

		layout.add(sortableLayout);

		addIcon = VaadinIcon.PLUS_CIRCLE.create();
		addIcon.getStyle().set("color", "#dddddd");
		layout.add(addIcon);

		removeIcon = VaadinIcon.MINUS_CIRCLE.create();
		removeIcon.getStyle().set("color", "#dddddd");
		layout.add(removeIcon);
	}

	public Registration addRemoveRowClickListener(ComponentEventListener<ClickEvent<Icon>> listener) {
		return removeIcon.addClickListener(listener);
	}

	public Registration addAddFieldClickListener(ComponentEventListener<ClickEvent<Icon>> listener) {
		return addIcon.addClickListener(listener);
	}

	public void addField(FIELD field) {
		String label = String.valueOf(field);
		this.fields.add(new LayoutEditorField<>(label, field));
	}

}
