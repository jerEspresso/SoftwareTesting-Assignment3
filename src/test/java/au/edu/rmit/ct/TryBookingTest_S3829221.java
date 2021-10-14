/**
 * Trybooking JUnit/Webdriver Tasks:
 * 1) Navigate to https://www.trybooking.com/book/search (Links to an external site.) and print-to-screen the featured event titles. Store these titles in a suitable variable in your test case (list? arrays?). Update your test case to use assertEquals to compare these titles with a new test case run. You are expected to show that this test case passes.
 * 2) Navigate to https://www.trybooking.com/book/search (Links to an external site.) as above and check for featured and events near "you". If any such event has '(Cancelled)' in its title, then this test case should fail.
 * Example Event Title: Prahran Market Discovery Trail (CANCELLED)
 * Example Event URL: https://www.trybooking.com/events/landing?eid=758678
 * 3) Navigate to https://www.trybooking.com/BUOMO (Links to an external site.) and book to one of the Monday or Thursday sessions, completing all of any optional data collection questions asked, using your student email ID.
 */
package au.edu.rmit.ct;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TryBookingTest_S3829221 {
    static WebDriver myDriver;
    static List<String> titleList;

    // Store featured event titles in list
    @BeforeAll
    static void setUpBeforeClass() throws InterruptedException {
        SeleniumDriverFactory sdf = new SeleniumDriverFactory();
        myDriver = sdf.getDriver();

        String url = "https://www.trybooking.com/book/search";
        myDriver.get(url);
        Thread.sleep(3000);

        WebElement featEventSect = myDriver.findElement(By.id("FeaturedEventsSection"));
        List<WebElement> featEvents = featEventSect.findElements(By.tagName("h2"));

        titleList = new ArrayList<>();
        for (WebElement featEvent : featEvents)
            titleList.add(featEvent.getText());

        System.out.println("Feature event titles: ");
        for (String title : titleList) {
            System.out.println(title);
        }

        myDriver.quit();
    }

    @BeforeEach
    void setUp() {
        SeleniumDriverFactory sdf = new SeleniumDriverFactory();
        myDriver = sdf.getDriver();
    }

    @AfterEach
    void tearDown() {
        myDriver.quit();
    }

    @Test
    @Order(0)
    @DisplayName("Sanity test only")
    void sanityTest() {
        // When this passes I know I have the webdriver and Junit set up correctly
        String url = "https://www.trybooking.com/book/search";
        myDriver.get(url);
        assertEquals("Buy tickets | TryBooking Australia", myDriver.getTitle());
    }

    @Test
    @Order(1)
    @DisplayName("Check if featured event titles match")
    void checkFeaturedEvent() throws InterruptedException {
        String url = "https://www.trybooking.com/book/search";
        myDriver.get(url);
        Thread.sleep(3000);

        WebElement featEventSect = myDriver.findElement(By.id("FeaturedEventsSection"));
        List<WebElement> featEvents = featEventSect.findElements(By.tagName("h2"));

        for (WebElement featEvent : featEvents) {
            assertTrue(this.titleList.contains(featEvent.getText()));
        }
    }

    @Test
    @Order(2)
    @DisplayName("Check if any cancelled event appears")
    void checkCancelledEvent() throws InterruptedException {
        String url = "https://www.trybooking.com/book/search";
        myDriver.get(url);
        Thread.sleep(3000);

        WebElement featEventSect = myDriver.findElement(By.id("FeaturedEventsSection"));
        List<WebElement> featEvents = featEventSect.findElements(By.tagName("h2"));

        for (WebElement featEvent : featEvents) {
            if (featEvent.getText().toLowerCase().contains("cancel"))
                fail("Event '" + featEvent.getText() + "' is cancelled.");
        }
    }
}