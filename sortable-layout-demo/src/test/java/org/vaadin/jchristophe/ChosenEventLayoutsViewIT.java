package org.vaadin.jchristophe;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.options.BoundingBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("playwright")
public class ChosenEventLayoutsViewIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
    }

    @Test
    public void testChosenAndUnchosenEvents() {
        // Locate the first item in the left list
        Locator leftList = page.locator("main ul").first();
        Locator rightList = page.locator("main ul").nth(1);

        Locator leftListItem = leftList.locator("li").first();
        Locator rightListItem = rightList.locator("li").first();

        BoundingBox from = leftListItem.boundingBox(); // waits for visibility internally
        BoundingBox to = rightListItem.boundingBox(); // waits for visibility internally

        // Move to the center of the source, press and hold
        page.mouse().move(from.x + from.width / 2, from.y + from.height / 2);
        page.mouse().down();

        page.mouse().move(to.x + to.width / 2, to.y + to.height / 2);

        // Verify that the 'chosen-layout' class is added to both sortable layouts
        assertThat(page.locator(".chosen-layout")).hasCount(2);

        // Release the mouse to trigger the unchoose event
        page.mouse().up();

        // Verify that the 'chosen-layout' class is removed from both sortable layouts
        assertThat(page.locator(".chosen-layout")).hasCount(0);
        assertThat(leftList).hasText("left item 1left item 2left item 3");
        assertThat(rightList).hasText("left item 1right item 1right item 2right item 3");
    }

    @Override
    public String getView() {
        return ChosenEventLayoutsView.CHOSEN_TWOLAYOUTS_VIEW;
    }
}
