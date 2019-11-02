package org.vaadin.jchristophe;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

@NpmPackage(value = "web-animations-js", version = "2.3.2")
@NpmPackage(value="muuri", version = "0.8.0")
@JavaScript("web-animations-js/web-animations.min.js")
@JavaScript("muuri/dist/muuri.min.js")
@JavaScript(MuuriUtils.WEBANIM_LIB)
@JavaScript(MuuriUtils.MUURI_LIB)
@CssImport("./dnd-styles.css")
public class MuuriComponent extends Div {

    private Div board = new Div();
    private Div boardColumnTodo = new Div();
    private Div boardColumnWorking = new Div();
    private Div boardColumnDone = new Div();

    public MuuriComponent() {
        board.addClassName("board");
        setupColumn(boardColumnTodo, "todo", "todo");
        setupColumn(boardColumnWorking, "Working","working");
        setupColumn(boardColumnDone, "Done", "done");
        board.add(boardColumnTodo, boardColumnWorking, boardColumnDone);
        add(board);
        String jsInit = createInitFunction();
        UI.getCurrent().getPage().executeJs(jsInit);
    }

    private String createInitFunction() {
        return "var itemContainers = [].slice.call(document.querySelectorAll('.board-column-content'));\n" +
                "var columnGrids = [];\n" +
                "var boardGrid;\n" +
                "\n" +
                "// Define the column grids so we can drag those\n" +
                "// items around.\n" +
                "itemContainers.forEach(function (container) {\n" +
                "\n" +
                "  // Instantiate column grid.\n" +
                "  var grid = new Muuri(container, {\n" +
                "    items: '.board-item',\n" +
                "    layoutDuration: 400,\n" +
                "    layoutEasing: 'ease',\n" +
                "    dragEnabled: true,\n" +
                "    dragSort: function () {\n" +
                "      return columnGrids;\n" +
                "    },\n" +
                "    dragSortInterval: 0,\n" +
                "    dragContainer: document.body,\n" +
                "    dragReleaseDuration: 400,\n" +
                "    dragReleaseEasing: 'ease'\n" +
                "  })\n" +
                "  .on('dragStart', function (item) {\n" +
                "    // Let's set fixed widht/height to the dragged item\n" +
                "    // so that it does not stretch unwillingly when\n" +
                "    // it's appended to the document body for the\n" +
                "    // duration of the drag.\n" +
                "    item.getElement().style.width = item.getWidth() + 'px';\n" +
                "    item.getElement().style.height = item.getHeight() + 'px';\n" +
                "  })\n" +
                "  .on('dragReleaseEnd', function (item) {\n" +
                "    // Let's remove the fixed width/height from the\n" +
                "    // dragged item now that it is back in a grid\n" +
                "    // column and can freely adjust to it's\n" +
                "    // surroundings.\n" +
                "    item.getElement().style.width = '';\n" +
                "    item.getElement().style.height = '';\n" +
                "    // Just in case, let's refresh the dimensions of all items\n" +
                "    // in case dragging the item caused some other items to\n" +
                "    // be different size.\n" +
                "    columnGrids.forEach(function (grid) {\n" +
                "      grid.refreshItems();\n" +
                "    });\n" +
                "  })\n" +
                "  .on('layoutStart', function () {\n" +
                "    // Let's keep the board grid up to date with the\n" +
                "    // dimensions changes of column grids.\n" +
                "    boardGrid.refreshItems().layout();\n" +
                "  });\n" +
                "\n" +
                "  // Add the column grid reference to the column grids\n" +
                "  // array, so we can access it later on.\n" +
                "  columnGrids.push(grid);\n" +
                "\n" +
                "});\n" +
                "\n" +
                "// Instantiate the board grid so we can drag those\n" +
                "// columns around.\n" +
                "boardGrid = new Muuri('.board', {\n" +
                "  layoutDuration: 400,\n" +
                "  layoutEasing: 'ease',\n" +
                "  dragEnabled: true,\n" +
                "  dragSortInterval: 0,\n" +
                "  dragStartPredicate: {\n" +
                "    handle: '.board-column-header'\n" +
                "  },\n" +
                "  dragReleaseDuration: 400,\n" +
                "  dragReleaseEasing: 'ease'\n" +
                "});";
    }

    private void setupColumn(Div boardColumn, String title, String cssColumn) {
        boardColumn.addClassNames("board-column",cssColumn);
        Div header = new Div();
        header.addClassName("board-column-header");
        header.setText(title);
        Div content = new Div();
        content.addClassName("board-column-content");

        int size = 5;
        for (int i = 0; i < size; i++) {
            Div boardItem = new Div();
            boardItem.addClassName("board-item");
            Div boardItemContent = new Div();
            boardItemContent.addClassName("board-item-content");
            boardItemContent.add(new Button("Item "+ title + i));
            boardItem.add(boardItemContent);
            content.add(boardItem);
        }
        boardColumn.add(header,content);


    }
}
