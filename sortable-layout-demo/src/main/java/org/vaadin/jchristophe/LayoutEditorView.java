package org.vaadin.jchristophe;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.jchristophe.layouteditor.LayoutEditor;
import org.vaadin.jchristophe.layouteditor.LayoutEditorRow;

import java.util.Arrays;

/**
 * @author Martin Israelsen
 */
@Route(value = "layout-editor", layout = MainLayout.class)
public class LayoutEditorView extends VerticalLayout {

	private LayoutEditor<String> editor = new LayoutEditor<>();

	public LayoutEditorView() {

		editor.setListOfFields(
				Arrays.asList("First name", "Last Name", "Address", "City", "Postal Code", "Phone", "Email", "DOB"));
		LayoutEditorRow<String> row = editor.addRow();
		row.addField("First Name");
		row.addField("Last Name");

		row = editor.addRow();
		row.addField("Address");
		row.addField("City");
		row.addField("Postal Code");

		add(editor);
	}
}
