/**
 * JPetstore JUnit/Webdriver Tasks:
 * 1) Check the pet name, price and check if there is stock for one pet of your choice (outside of Male Chihuahua). (as outlined for W11 prac)
 * 2) Start a menagerie! Select a specific fish, specific cat, and a third pet (they will have a unique item ID). Add 3 multiples of the first, 2 multiples of the cat, and one of the third pet to the cart. Check the subtotal matches the expected price. You are expected to show that this test case passes.
 */
package au.edu.rmit.ct;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JPetstoreTest_S3829221 {
    WebDriver myDriver;

    @BeforeEach
    void setUp() {
        SeleniumDriverFactory sdf = new SeleniumDriverFactory();
        this.myDriver = sdf.getDriver();
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
        String petStoreURL = "https://petstore.octoperf.com";
        myDriver.get(petStoreURL);
        assertEquals("JPetStore Demo", myDriver.getTitle());
    }

    @Test
    @Order(1)
    @DisplayName("Check the name of Adult Male Persian at product page")
    void checkPersianName() {
        String persianURL = "https://petstore.octoperf.com/actions/Catalog.action?viewItem=&itemId=EST-17";
        myDriver.get(persianURL);

        List<WebElement> tdElements = myDriver.findElements(By.tagName("td"));
        boolean isNameCorrect = false;

        for (WebElement tdElement : tdElements) {
            if (tdElement.getText().equals("Adult Male Persian"))
                isNameCorrect = true;
        }

        assertTrue(isNameCorrect);
    }

    @Test
    @Order(2)
    @DisplayName("Check the price of Adult Male Persian at product page")
    void checkPersianPrice() {
        String persianURL = "https://petstore.octoperf.com/actions/Catalog.action?viewItem=&itemId=EST-17";
        myDriver.get(persianURL);

        List<WebElement> tdElements = myDriver.findElements(By.tagName("td"));
        boolean isPriceCorrect = false;

        for (WebElement tdElement : tdElements) {
            if (tdElement.getText().equals("$93.50"))
                isPriceCorrect = true;
        }

        assertTrue(isPriceCorrect);
    }

    @Test
    @Order(3)
    @DisplayName("Check if there is any stock of Adult Male Persian at product page")
    void checkPersianStock() {
        String persianURL = "https://petstore.octoperf.com/actions/Catalog.action?viewItem=&itemId=EST-17";
        myDriver.get(persianURL);

        WebElement we = myDriver.findElement(By.xpath("//td[contains(text(), 'in stock')]"));
        int numInStock = Integer.parseInt(we.getText().split(" ")[0]);

        assertTrue(numInStock > 0);
    }

    @Test
    @Order(4)
    @DisplayName("Check the subtotal in cart")
    void checkSubtotal() {
        String goldfishURL = "https://petstore.octoperf.com/actions/Catalog.action?viewItem=&itemId=EST-20";
        String persianURL = "https://petstore.octoperf.com/actions/Catalog.action?viewItem=&itemId=EST-17";
        String bulldogURL = "https://petstore.octoperf.com/actions/Catalog.action?viewItem=&itemId=EST-6";
        String cartURL = "https://petstore.octoperf.com/actions/Cart.action?viewCart=";

        myDriver.get(goldfishURL);
        myDriver.findElement(By.xpath("//a[text()='Add to Cart']")).click();

        myDriver.get(persianURL);
        myDriver.findElement(By.xpath("//a[text()='Add to Cart']")).click();

        myDriver.get(bulldogURL);
        myDriver.findElement(By.xpath("//a[text()='Add to Cart']")).click();

        myDriver.get(cartURL);
        myDriver.findElement(By.name("EST-20")).clear();
        myDriver.findElement(By.name("EST-20")).sendKeys("3");
        myDriver.findElement(By.name("EST-17")).clear();
        myDriver.findElement(By.name("EST-17")).sendKeys("2");
        myDriver.findElement(By.name("updateCartQuantities")).click();

        WebElement we = myDriver.findElement(By.xpath("//td[contains(text(), 'Sub Total')]"));

        assertEquals("Sub Total: $222.00", we.getText());
    }
}