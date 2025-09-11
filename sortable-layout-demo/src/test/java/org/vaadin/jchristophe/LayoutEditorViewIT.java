package org.vaadin.jchristophe;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("playwright")
public class LayoutEditorViewIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
    }

    @Test
    public void testDragAndDropRow() {
        // Locate the first and last items in the list
        Locator row1 = page.locator(".layout-editor .layout-editor-row").first();
        Locator row2 = page.locator(".layout-editor .layout-editor-row").nth(1);

        assertThat(row1).containsText("First Name");
        assertThat(row2).containsText("Address");
        Locator firstItem = row1.locator(".row-handle").first();

        // Drag the first item to the row 2
        firstItem.dragTo(row2);
        // check that the rows have been swapped
        assertThat(row2).containsText("First Name");
        assertThat(row1).containsText("Address");
    }

    @Override
    public String getView() {
        return LayoutEditorView.LAYOUT_EDITOR_VIEW;
    }
}
