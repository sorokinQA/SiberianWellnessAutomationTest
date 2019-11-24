package wellnesssiberian;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class CartTest {
    private ChromeDriver driver;

    public CartTest() {
        System.setProperty("webdriver.chrome.driver", Config.CHROME_DRIVER_PATH);
    }

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(20L, TimeUnit.SECONDS);
    }

    @Test
    public void given_searchField_when_typeSearchAndClickAddResultToBasket_then_itemInBasket() {
        goToSearchPage();
        searchFor("Гинкго билоба и байкальский шлемник");

        WebElement neededProduct = findProduct("500125");
        neededProduct.click();

        addProductToCart();

        goToCart();
        Assert.assertTrue("The needed product is not in the basket!", isPresence("500125"));
    }

    @After
    public void closeUp() {
        driver.quit();
    }

    private void goToSearchPage() {
        driver.get("https://ru.siberianhealth.com/ru/");
    }

    private void goToCart() {
        driver.get("https://ru.siberianhealth.com/ru/shop/cart/");
    }

    private void searchFor(String searchText) {
        WebElement search = driver.findElement(By.id("search-input"));
        search.click();

        search.sendKeys(searchText);
        search.sendKeys(Keys.ENTER);
    }

    private WebElement findProduct(String productId) {
        return driver.findElement(By.xpath("//a[@href='/ru/shop/catalog/product/" + productId + "/']"));
    }

    private void addProductToCart() {
        WebElement addProduct = driver.findElement(By.className("product-spinner-button__add"));

        WebDriverWait wait = new WebDriverWait(driver, Config.PAGE_LOADING_TIMEOUT_SEC);
        wait.until(ExpectedConditions.elementToBeClickable(addProduct));

        addProduct.click();
    }

    private boolean isPresence(String productId) {
        return driver.findElements(By.xpath("//a[@href='/ru/shop/catalog/product/" + productId + "/']")).size() > 0;
    }
}
