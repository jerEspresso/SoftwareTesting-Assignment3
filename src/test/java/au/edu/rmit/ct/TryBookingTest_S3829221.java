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
import org.openqa.selenium.support.ui.Select;

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
            if (featEvent.getText().toLowerCase().contains("cancelled"))
                fail("Event '" + featEvent.getText() + "' is cancelled.");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Book into an event")
    void bookEvent() throws InterruptedException {
        String url = "https://www.trybooking.com/events/landing?eid=818286&";
        myDriver.get(url);

        // Click on the select button
        WebElement sessionTable = myDriver.findElement(By.id("sessions-table"));
        sessionTable.findElement(By.tagName("button")).click();
        Thread.sleep(3000);

        // Input the number of tickets
        myDriver.findElement(By.id("quantity0")).clear();
        myDriver.findElement(By.id("quantity0")).sendKeys("1");
        myDriver.findElement(By.id("Next_addToCartBtn")).click();
        Thread.sleep(3000);

        // Fill in the booking details
        Select day = new Select(myDriver.findElement(By.id("bookingDataField_546337_day")));
        day.selectByVisibleText("5");

        Select month = new Select(myDriver.findElement(By.id("bookingDataField_546337_month")));
        month.selectByVisibleText("Nov");

        Select year = new Select(myDriver.findElement(By.id("bookingDataField_546337_year")));
        year.selectByVisibleText("2021");

        Select cam = new Select(myDriver.findElement(By.id("bookingDataField_546338")));
        cam.selectByValue("Yes");

        myDriver.findElement(By.id("bookingDataField_546339")).click();
        myDriver.findElement(By.xpath("//div[contains(text(), 'FOXIT')]")).click();

        myDriver.findElement(By.id("ticketHolderDetails_Next")).click();
        Thread.sleep(3000);

        // Fill in the personal details
        myDriver.findElement(By.id("txtFirstName")).clear();
        myDriver.findElement(By.id("txtFirstName")).sendKeys("Chenyu");

        myDriver.findElement(By.id("txtLastName")).clear();
        myDriver.findElement(By.id("txtLastName")).sendKeys("Xiao");

        Select country = new Select(myDriver.findElement(By.id("drpCountry")));
        country.selectByValue("AU");

        myDriver.findElement(By.id("txtEmailAddress")).clear();
        myDriver.findElement(By.id("txtEmailAddress")).sendKeys("s3829221@student.rmit.edu.au");

        myDriver.findElement(By.id("txtConfirmEmailAddress")).clear();
        myDriver.findElement(By.id("txtConfirmEmailAddress")).sendKeys("s3829221@student.rmit.edu.au");

        myDriver.findElement(By.id("btn-purchase-lg")).click();
        Thread.sleep(3000);

        // Check booking details in the confirmation page
        WebElement heading = myDriver.findElement(By.tagName("h1"));
        assertEquals("transaction successful", heading.getText().toLowerCase());

        WebElement emailDiv = myDriver.findElement(By.xpath("//div[contains(text(), 'Email Address')]"));
        WebElement emailAddress = emailDiv.findElement(By.tagName("span"));
        assertEquals("s3829221@student.rmit.edu.au", emailAddress.getText());

        List<WebElement> wes = myDriver.findElements(By.tagName("h5"));
        boolean isTitleCorrect = false;
        for (WebElement we : wes) {
            if (we.getText().equals("RMIT/m2ma Virtual Doughnut Award Ceremony"))
                isTitleCorrect = true;
        }
        assertTrue(isTitleCorrect);

        WebElement attendee = myDriver.findElement(By.className("main-instruction"));
        assertEquals("Attendee x 1", attendee.getText());

        // If the date and time are correct, no exception will be thrown.
        WebElement eventDateTime = myDriver.findElement(By.xpath("//div[text()='Monday 1 November 2021 1:30 PM']"));
    }
}