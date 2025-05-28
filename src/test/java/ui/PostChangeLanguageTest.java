package ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;
import ui.pages.AfterLoginPage;
import ui.pages.EditProfilePage;
import ui.pages.PostHomePage;
import ui.pages.ProfilePage;
import ui.pages.LoginPage;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class PostChangeLanguageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    ExtentSparkReporter extentSparkReporter;
    ExtentReports extentReports;
    ExtentTest extentTest;

    private static final String CREDENTIALS_FILE = "credentials.properties";

    private static String EMAIL;
    private static String PASSWORD;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeClass
    public void initializeAuthToken() {
        Properties appProps = new Properties();
        try (InputStream rootPath = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(CREDENTIALS_FILE)) {
            if (rootPath == null) {
                throw new RuntimeException("credentials.properties file not found in classpath!");
            }
            appProps.load(rootPath);

            EMAIL = appProps.getProperty("email");
            PASSWORD = appProps.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credentials: " + e.getMessage(), e);
        }
    }

    @BeforeClass
    public void startReporter() {
        extentSparkReporter = new ExtentSparkReporter(
                System.getProperty("user.dir") + "/reports/ui-report.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(extentSparkReporter);

        extentSparkReporter.config().setDocumentTitle("Post.ch Automation Report");
        extentSparkReporter.config().setReportName("Post.ch Test Report");
        extentSparkReporter.config().setTheme(Theme.STANDARD);
        extentSparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @Test(description = "Verify successful login with SwissID credentials and profile language change", priority = 1)
    public void testLoginAndChangeLanguageToGerman() throws InterruptedException {
        extentTest = extentReports.createTest("Login and change language to German", "");

        // Navigate to the Post.ch website and go to login page
        PostHomePage postHomePage = new PostHomePage(driver, wait, extentTest);
        postHomePage.navigateToPostCh();
        postHomePage.acceptCookiesIfPresent();
        postHomePage.clickLogin();

        // Login with SwissID
        LoginPage swissIdLoginPage = new LoginPage(driver, wait, extentTest);
        swissIdLoginPage.enterEmail(EMAIL);
        swissIdLoginPage.clickWeiter();
        swissIdLoginPage.enterPassword(PASSWORD);
        swissIdLoginPage.clickWeiter();
        swissIdLoginPage.clickVielleichtSpater();

        // After login, go to profile page
        wait.until(ExpectedConditions.urlContains("kvm/app/ui/"));
        AfterLoginPage afterLoginPage = new AfterLoginPage(driver, wait, extentTest);
        afterLoginPage.goToProfilePage();

        // Go to language settings
        ProfilePage profilePage = new ProfilePage(driver, wait, extentTest);
        profilePage.clickChangeLanguageButton();

        // Select German language
        EditProfilePage editProfilePage = new EditProfilePage(driver, wait, extentTest);
        editProfilePage.selectLanguageFromDropdown();
        editProfilePage.clickConfirmLanguageChangeButton();

        // Verify language change
        Assert.assertTrue(profilePage.isLanguageGerman(), "Language should be set to Deutsch");
        extentTest.pass("Verified language change to German");

        // Logout
        afterLoginPage.logout();

        // Verify logout
        // Assert.assertTrue(postHomePage.isLoginButtonVisible(), "Login button should
        // be visible");
        // addToTheReport("Verifying logout", "PostHomePage_IsLoginButtonVisible");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        extentReports.flush();
    }
}