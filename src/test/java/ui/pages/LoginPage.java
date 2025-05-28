package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.ExtentTest;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest extentTest;

    private By vielleichtSpaterButtonLocator = By.xpath("//button[contains(text(), 'Vielleicht später ')]");
    private By weiterButtonLocator = By.xpath("//button[contains(text(), 'Weiter')]");
    private By emailFieldLocator = By.xpath("//input[@type='email' or @name='email' or @id='email']");
    private By passwordFieldLocator = By.xpath("//input[@type='password' or @name='password' or @id='password']");
    private By submitButtonLocator = By
            .xpath("//button[@type='submit' or contains(text(), 'Login') or contains(text(), 'Anmelden')]");

    public LoginPage(WebDriver driver, WebDriverWait wait, ExtentTest extentTest) {
        this.driver = driver;
        this.wait = wait;
        this.extentTest = extentTest;
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.presenceOfElementLocated(emailFieldLocator));
        WebElement emailField = driver.findElement(emailFieldLocator);
        emailField.clear();
        emailField.sendKeys(email);
        extentTest.info("Entering email: " + email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.presenceOfElementLocated(passwordFieldLocator));
        WebElement passwordField = driver.findElement(passwordFieldLocator);
        passwordField.clear();
        passwordField.sendKeys(password);
        extentTest.info("Entering password:");
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButtonLocator));
        WebElement submitButton = driver.findElement(submitButtonLocator);
        submitButton.click();
        extentTest.info("Clicked submit button");
    }

    public void clickVielleichtSpater() {
        wait.until(ExpectedConditions.elementToBeClickable(vielleichtSpaterButtonLocator));
        WebElement vielleichtSpaterButton = driver.findElement(vielleichtSpaterButtonLocator);
        vielleichtSpaterButton.click();
        extentTest.info("Clicked vielleicht später button");
    }

    public void clickWeiter() {
        wait.until(ExpectedConditions.elementToBeClickable(weiterButtonLocator));
        WebElement weiterButton = driver.findElement(weiterButtonLocator);
        weiterButton.click();
        extentTest.info("Clicked weiter button");
    }
}