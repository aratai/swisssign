package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentTest;

public class EditProfilePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest extentTest;

    private By languageDropdownLocator = By.xpath("//select[contains(@name, 'language')]");
    private By confirmLanguageChangeButtonLocator = By.xpath("//button[@id='confirm']");

    public EditProfilePage(WebDriver driver, WebDriverWait wait, ExtentTest extentTest) {
        this.driver = driver;
        this.wait = wait;
        this.extentTest = extentTest;
    }

    public void selectLanguageFromDropdown() {
        wait.until(ExpectedConditions.presenceOfElementLocated(languageDropdownLocator));
        WebElement languageDropdown = driver.findElement(languageDropdownLocator);
        Select select = new Select(languageDropdown);
        select.selectByValue("DE");
        extentTest.info("Selecting German language");
    }

    public void clickConfirmLanguageChangeButton() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmLanguageChangeButtonLocator));
        WebElement confirmLanguageChangeButton = driver.findElement(confirmLanguageChangeButtonLocator);
        confirmLanguageChangeButton.click();
        extentTest.info("Clicking confirm language change button");
    }
}