package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;

public class AfterLoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest extentTest;

    private By firstShadowHostLocator = By.cssSelector("swisspost-internet-header");
    private By secondShadowHostLocator = By.cssSelector("post-klp-login-widget");
    private By avatarButtonLocator = By.cssSelector("a[title='Meine Übersicht']");
    private By myProfileDropdownOptionLocator = By.cssSelector("a[title='Mein Profil']");
    private By logoutButtonLocator = By.cssSelector("a[title='Logout']");

    public AfterLoginPage(WebDriver driver, WebDriverWait wait, ExtentTest extentTest) {
        this.driver = driver;
        this.wait = wait;
        this.extentTest = extentTest;
    }

    public void goToProfilePage() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstShadowHostLocator));
        WebElement firstShadowHost = driver.findElement(firstShadowHostLocator);
        SearchContext firstShadowRoot = firstShadowHost.getShadowRoot();
        WebElement secondShadowHost = firstShadowRoot.findElement(secondShadowHostLocator);
        SearchContext secondShadowRoot = secondShadowHost.getShadowRoot();
        WebElement avatarButton = secondShadowRoot.findElement(avatarButtonLocator);
        avatarButton.click();
        WebElement myProfileDropdownOption = secondShadowRoot.findElement(myProfileDropdownOptionLocator);
        myProfileDropdownOption.click();

        extentTest.info("Navigating to profile page");
    }

    public void logout() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstShadowHostLocator));
        WebElement firstShadowHost = driver.findElement(firstShadowHostLocator);
        SearchContext firstShadowRoot = firstShadowHost.getShadowRoot();
        WebElement secondShadowHost = firstShadowRoot.findElement(secondShadowHostLocator);
        SearchContext secondShadowRoot = secondShadowHost.getShadowRoot();
        WebElement avatarButton = secondShadowRoot.findElement(avatarButtonLocator);
        avatarButton.click();
        WebElement logoutButton = secondShadowRoot.findElement(logoutButtonLocator);
        logoutButton.click();

        extentTest.info("Logging out");
    }
}