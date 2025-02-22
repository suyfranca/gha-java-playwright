package dev.christianbaumann;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class PlaywrightTest {
    private static final Logger LOGGER = Logger.getLogger(PlaywrightTest.class.getName());

    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(true);
        browser = playwright.chromium().launch(options);
    }

    @BeforeEach
    void setupTest() {
        page = browser.newPage();
    }

    @Test
    void testPageTitleChriss(TestInfo testInfo) {
        logTestInfo(testInfo, "Starting test: Page Title Verification (CB)");

        // Act
        page.navigate("https://christianbaumann.dev/");

        // Assert
//        assertTrue(page.title().contains("Christian Baumann"), "Page title should contain 'Christian Baumann'");
        assertTrue(page.title().contains("Frodo Baggins"), "Page title should contain 'Christian Baumann'");
        logTestInfo(testInfo, "✅ Title validation successful (CB).");
    }

    @Test
    void testPageTitlePlaywright(TestInfo testInfo) {
        logTestInfo(testInfo, "Starting test: Page Title Verification");

        // Act
        page.navigate("https://playwright.dev/");

        // Assert
        assertTrue(page.title().contains("Playwright"), "Page title should contain 'Playwright'");
        logTestInfo(testInfo, "✅ Title validation successful (PW).");
    }

    @Test
    void testSearchFunctionality(TestInfo testInfo) {
        logTestInfo(testInfo, "Starting test: Search Functionality");

        // Arrange
        page.navigate("https://playwright.dev/");

        // Act
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search (Ctrl+K)")).click();
        page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("Search")).fill("locator");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Locators").setExact(true)).click();

        page.waitForTimeout(1000);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("test-results/search.png")));

        // Assert
        assertTrue(page.url().contains("locators"), "Search should navigate to 'Locators' page");
        logTestInfo(testInfo, "✅ Search functionality works.");
    }

    @Test
    void testNavigationToAPIPage(TestInfo testInfo) {
        logTestInfo(testInfo, "Starting test: Navigation to API Page");

        // Arrange
        page.navigate("https://playwright.dev/");

        // Act
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("API")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("test-results/api-page.png")));

        // Assert
        assertTrue(page.url().contains("api"), "API page should be opened");
        logTestInfo(testInfo, "✅ Navigation to API page successful.");
    }

    @AfterEach
    void tearDownTest() {
        page.close();
    }

    @AfterAll
    static void tearDownClass() {
        browser.close();
        playwright.close();
    }

    /**
     * Utility method to log test information.
     */
    private void logTestInfo(TestInfo testInfo, String message) {
        LOGGER.info(() -> String.format("[TEST: %s] %s", testInfo.getDisplayName(), message));
    }
}
