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
public class SortableLayoutViewIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
    }


    @Test
    public void testDragAndDrop() {
        assertThat(page.getByText("Simple demos")).isVisible();
        assertThat(page.locator("#basic-example").getByText("item 1item 2item 3")).isVisible();
        // Locate the first and last items in the list
        Locator locator = page.locator("#basic-example ul li");
        Locator firstItem = locator.first();
        Locator lastItem = locator.last();
        assertThat(firstItem).hasText("item 1");
        assertThat(lastItem).hasText("item 3");

        // Drag the first item to the last item's position
        firstItem.dragTo(lastItem);
        assertThat(page.locator("#basic-example").getByText("item 2item 3item 1")).isVisible();
    }

    @Test
    public void testIndex() {
        assertThat(page.locator("#info")).hasText("Event:");
        // Locate the first and last items in the list
        Locator locator = page.locator("#basic-example ul li");
        Locator firstItem = locator.first();
        Locator lastItem = locator.last();

        // Drag the first item to the last item's position
        firstItem.dragTo(lastItem);
        assertThat(page.locator("#basic-example").getByText("item 2item 3item 1")).isVisible();
        assertThat(page.locator("#info")).hasText("Event: 2");
    }
    @Test
    public void testIndex2() {
        // Locate the first and last items in the list
        Locator locator = page.locator("#basic-example ul li");
        Locator firstItem = locator.first();
        Locator lastItem = locator.nth(1);
        assertThat(firstItem).hasText("item 1");
        assertThat(lastItem).hasText("item 2");

        // Drag the item
        firstItem.dragTo(lastItem);
        assertThat(page.locator("#basic-example").getByText("item 2item 1item 3")).isVisible();
        assertThat(page.locator("#info")).hasText("Event: 1");
    }
}