package stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.DriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.assertEquals;

public class MyStepdefs {
    WebDriver driver;
    String priceDetailText;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Given("I open the {string} URL")
    public void openTheURL(String url) throws InterruptedException {
        driver.navigate().to(url);
        driver.manage().window().maximize();
        closeCookiePopup();
        wait(3);
    }

    public void closeCookiePopup() {
        try {
            WebElement cookiePopup = driver.findElement(By.id("onetrust-accept-btn-handler"));
            if (cookiePopup.isDisplayed()) {
                cookiePopup.click();
                System.out.println("Cookie policy has been closed");
            }
        } catch (Exception e) {
            System.out.println("Cookie pop-up not found or already closed");
        }
    }

    @When("I navigate to category")
    public void navigateTotCategory() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        WebElement electronics = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(@class, 'sf-MenuItems')]//span[text()='Elektronik']")));
        electronics.click();
        WebElement computerTabletCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Bilgisayar/Tablet')]")));
        actions.moveToElement(computerTabletCategory).perform();
        WebElement tabletCategory = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Tablet']")));
        tabletCategory.click();
        wait(5);
    }

    @And("I filter by brand and screen size")
    public void filterByBrandAndScreenSize() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement brandCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Apple']")));
        brandCheckbox.click();
        driver.navigate().refresh();
        WebElement screenSizeFilter = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='13,2 inç']")));
        screenSizeFilter.click();
        driver.navigate().refresh();
        wait(5);
    }

    @Then("I select the most expensive product")
    public void clickOnMaxPrice() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'price-R57b2z0LFOTTCaDIKTgo')]")));
        List<Integer> prices = new ArrayList<>();
        List<WebElement> productElements = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            try {
                String priceText = priceElement.getText().replaceAll("[^\\d]", "");
                int price = Integer.parseInt(priceText);
                prices.add(price);
                productElements.add(priceElement);
            } catch (NumberFormatException e) {
                System.out.println("Could not convert price: " + priceElement.getText());
            }
        }
        int maxPrice = prices.stream().max(Integer::compareTo).orElse(0);
        int maxPriceIndex = prices.indexOf(maxPrice);
        if (maxPriceIndex != -1) {
            WebElement productToClick = productElements.get(maxPriceIndex);
            productToClick.click();
            System.out.println("Clicked on the product with the highest price: " + maxPrice);
        } else {
            System.out.println("No valid price elements found");
        }
        wait(5);
    }

    @When("I add the product to the cart")
    public void addProductToCart() throws InterruptedException {
        WebElement priceElement = driver.findElement(By.xpath("//div[@data-test-id='price-current-price']"));
        priceDetailText = priceElement.getText().replace("TL", "").trim();
        System.out.println(priceDetailText);
        WebElement addToCartButton = driver.findElement(By.xpath("//div[text()='Sepete ekle']"));
        addToCartButton.click();
        wait(5);
    }
    public void wait(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    @Then("I verify the product is added to the cart and the price matches")
    public void verifyProductAddedToCart() throws InterruptedException {
        WebElement goToCartButton = driver.findElement(By.xpath("//span[@id='shoppingCart' and text()='Sepetim']"));
        goToCartButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cartPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='basket_payedPrice']")));
        String cartPriceText = cartPrice.getText().replace("TL", "").trim();
        System.out.println(cartPriceText);
        assertEquals(priceDetailText, cartPriceText, "The prices do not match");
        wait(5);
    }
}


