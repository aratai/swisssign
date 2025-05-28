package ui.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.ExtentTest;

public class PostHomePage {

    private static final String POST_URL = "https://www.post.ch";

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest extentTest;

    private By cookieShadowHostLocator = By.id("usercentrics-root");
    private By cookieAcceptButtonLocator = By.cssSelector("[data-testid='uc-accept-all-button']");

    private By firstShadowHostLocator = By.cssSelector("swisspost-internet-header");
    private By secondShadowHostLocator = By.cssSelector("post-klp-login-widget");
    private By loginButtonLocator = By.cssSelector("a[title='Login']");

    public PostHomePage(WebDriver driver, WebDriverWait wait, ExtentTest extentTest) {
        this.driver = driver;
        this.wait = wait;
        this.extentTest = extentTest;
    }

    public void navigateToPostCh() {
        driver.get(POST_URL);
    }

    public void clickLogin() {
        System.out.println("Login button found");
        WebElement firstShadowHost = driver.findElement(firstShadowHostLocator);
        SearchContext firstShadowRoot = firstShadowHost.getShadowRoot();
        WebElement secondShadowHost = firstShadowRoot.findElement(secondShadowHostLocator);
        SearchContext secondShadowRoot = secondShadowHost.getShadowRoot();
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                secondShadowRoot.findElement(loginButtonLocator)));

        loginButton.click();
        extentTest.info("Clicked login button");
    }

    public void acceptCookiesIfPresent() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement shadowHost = driver.findElement(cookieShadowHostLocator);
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        WebElement cookieAcceptButton = shadowRoot
                .findElement(cookieAcceptButtonLocator);
        cookieAcceptButton.click();
        extentTest.info("Accepted cookies");
    }
}