package org.vaadin.jchristophe.layouteditor;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import org.vaadin.jchristophe.SortableConfig;
import org.vaadin.jchristophe.SortableGroupStore;
import org.vaadin.jchristophe.SortableLayout;

import java.util.Collection;

/**
 * @author Martin Israelsen
 */
public class LayoutEditor<FIELD> extends Composite<VerticalLayout> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2433957024124431023L;

	private Button btnAddRow = new Button("Add Row");

	private ItemLabelGenerator<FIELD> itemLabelGenerator;

	private Collection<FIELD> listOfFields = null;

	private SortableConfig rowsSortableConfig = new SortableConfig();
	private SortableGroupStore rowsSortableGroupStore = new SortableGroupStore();

	private SortableConfig columnsSortableConfig = new SortableConfig();
	private SortableGroupStore columnsSortableGroupStore = new SortableGroupStore();

	private VerticalLayout rows = new VerticalLayout();

	public LayoutEditor() {
        addClassName("layout-editor");
		VerticalLayout content = getContent();

		rows.setMargin(false);
		rows.setPadding(false);
		rows.setSpacing(false);

		rowsSortableConfig.setGroupName("rows-dragdrop");
		//rowsSortableConfig.allowDragIn(true);
		rowsSortableConfig.addDragInGroupName("rows-dragdrop");
		rowsSortableConfig.allowDragOut(true);
		rowsSortableConfig.setAnimation(150);

		columnsSortableConfig.setGroupName("columns-dragdrop");
		//columnsSortableConfig.allowDragIn(true);
		columnsSortableConfig.addDragInGroupName("columns-dragdrop");
		columnsSortableConfig.allowDragOut(true);
		columnsSortableConfig.setAnimation(150);

		rows.setWidthFull();
		rows.getStyle().set("background", "yellow");
		SortableLayout rowsSortableLayout = new SortableLayout(rows, rowsSortableConfig, rowsSortableGroupStore);
		rowsSortableLayout.setWidthFull();
		rowsSortableLayout.setHandle("row-handle");
		content.add(rowsSortableLayout);

		btnAddRow.addClickListener(e -> addRow());

		content.add(btnAddRow);
	}

	public void setListOfFields(Collection<FIELD> listOfFields) {
		this.listOfFields = listOfFields;
	}

	public LayoutEditorRow<FIELD> addRow() {
		LayoutEditorRow<FIELD> row = new LayoutEditorRow<>(columnsSortableConfig, columnsSortableGroupStore);

		row.addAddFieldClickListener(e -> addFieldClicked(row));
		row.addRemoveRowClickListener(e -> removeRowClicked(row));

		rows.add(row);
		return row;
	}

	private void addFieldClicked(LayoutEditorRow<FIELD> row) {
		Dialog dialog = new Dialog();

		Select<FIELD> select = new Select<>();

		if (this.itemLabelGenerator != null)
			select.setTextRenderer(itemLabelGenerator);

		select.setItems(listOfFields);

		dialog.add(new H4("Select Field"));
		dialog.add(select);

		Button btnOk = new Button("OK", e -> {
			row.addField(select.getValue());
			dialog.close();
		});

		btnOk.setEnabled(false);
		Button btnCancel = new Button("Cancel", e -> dialog.close());

		select.addValueChangeListener(e -> {
			btnOk.setEnabled(true);
		});

		HorizontalLayout buttons = new HorizontalLayout(btnOk, btnCancel);
		dialog.add(buttons);

		dialog.open();

	}

	private void removeRowClicked(LayoutEditorRow<FIELD> row) {
		rows.remove(row);
	}
}
