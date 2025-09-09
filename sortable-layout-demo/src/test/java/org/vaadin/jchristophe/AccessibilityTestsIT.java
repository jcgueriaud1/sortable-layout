package org.vaadin.jchristophe;

import java.util.Collections;
import java.util.List;

import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.deque.html.axecore.results.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("playwright")
public class AccessibilityTestsIT extends BasePlayWrightIT {

    @BeforeEach
    public void setupTest() throws Exception {
        super.setupTest();
    }

    @Test
        // 2
    void shouldNotHaveAutomaticallyDetectableAccessibilityIssues() throws Exception {
        assertThat(page.locator("#outlet")).isVisible();
        AxeResults accessibilityScanResults = new AxeBuilder(page)
                .exclude("copilot-main")
                .exclude("vaadin-dev-tools")
                .exclude("vaadin-connection-indicator").analyze(); // 4

        List<Rule> violations = accessibilityScanResults.getViolations();
        assertEquals(1, violations.size()); // 5
    }
}