package dev.christianbaumann;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlaywrightTest {
    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void setupTest() {
        page = browser.newPage();
    }

    @Test
    void testPageTitle() {
        page.navigate("https://playwright.dev/");
        assertEquals("Playwright", page.title(), "Page title should be 'Playwright'");
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
}
