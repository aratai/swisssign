package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.ExtentTest;

public class ProfilePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest extentTest;

    private By changeLanguageButtonLocator = By.xpath("//a[@id='editUserInfoLanguage']");
    private By languageSpanLocator = By.id("userInfoLanguage");

    public ProfilePage(WebDriver driver, WebDriverWait wait, ExtentTest extentTest) {
        this.driver = driver;
        this.wait = wait;
        this.extentTest = extentTest;
    }

    public void clickChangeLanguageButton() {
        wait.until(ExpectedConditions.elementToBeClickable(changeLanguageButtonLocator));
        WebElement changeLanguageButton = driver.findElement(changeLanguageButtonLocator);
        changeLanguageButton.click();
        extentTest.info("Clicking change language button");
    }

    public boolean isLanguageGerman() {
        WebElement languageSpan = wait.until(ExpectedConditions.presenceOfElementLocated(languageSpanLocator));
        return languageSpan.getText().trim().equals("Deutsch");
    }
}