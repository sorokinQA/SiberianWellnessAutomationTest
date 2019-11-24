package wellnesssiberian;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
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

    /**
     * We need to wait for a successful click on "add to cart" element.
     * For some reason, JS doesn't allow us to do it easily on a first try.
     * Therefore, we use DriverWait.
     */
    private void addProductToCart() {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Config.PAGE_LOADING_TIMEOUT_SEC)
                .ignoring(StaleElementReferenceException.class);

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement element = webDriver.findElement(By.className("product-spinner-button__add"));
                element.click();
                return Boolean.TRUE;
            }
        });
    }

    private boolean isPresence(String productId) {
        return driver.findElements(By.xpath("//a[@href='/ru/shop/catalog/product/" + productId + "/']")).size() > 0;
    }
}
