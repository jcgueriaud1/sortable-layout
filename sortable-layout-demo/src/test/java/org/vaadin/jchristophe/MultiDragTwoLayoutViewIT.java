package org.vaadin.jchristophe;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.BoundingBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("playwright")
public class MultiDragTwoLayoutViewIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
    }

    @Test
    public void testMultiDragItems() {
        // Locate the first item in the left list
        Locator leftList = page.locator("ul#left-list").first();

        Locator leftListItem1 = leftList.locator("li").first();
        Locator leftListItem2 = leftList.locator("li").nth(1);
        Locator leftListItem3 = leftList.locator("li").last();

        // Select the 2 items
        leftListItem1.click();
        assertThat(leftListItem1).hasAttribute("draggable", "false");
        assertThat(leftListItem1).hasClass("selected");
        leftListItem2.click();
        assertThat(leftListItem2).hasAttribute("draggable", "false");
        assertThat(leftListItem2).hasClass("selected");
        leftListItem1.dragTo(leftListItem3);

        assertThat(leftList).hasText("left item 3left item 1left item 2");
        Locator info = page.locator("#info").first();
        assertThat(info).hasText("left item 3 left item 1 left item 2");
    }

    @Test
    public void testMultiDragItemsToRightList() {
        // Locate the first item in the left list
        Locator leftList = page.locator("ul#left-list").first();
        Locator rightList = page.locator("ul#right-list").first();

        Locator leftListItem1 = leftList.locator("li").first();
        Locator leftListItem2 = leftList.locator("li").nth(1);
        Locator rightListItem = rightList.locator("li").first();

        // Select the 2 items
        leftListItem1.click();
        assertThat(leftListItem1).hasAttribute("draggable", "false");
        assertThat(leftListItem1).hasClass("selected");
        leftListItem2.click();
        assertThat(leftListItem2).hasAttribute("draggable", "false");
        assertThat(leftListItem2).hasClass("selected");
        leftListItem1.dragTo(
                rightListItem,
                new Locator.DragToOptions()
                        .setSourcePosition(1, 1)
                        .setTargetPosition(1, 1)
        );

        assertThat(leftList).hasText("left item 1left item 2left item 3");
        assertThat(rightList).hasText("left item 1left item 2right item 1right item 2right item 3");
    }

    @Override
    public String getView() {
        return MultiDragTwoLayoutsView.MULTIDRAG_VIEW;
    }
}
