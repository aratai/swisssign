package api;

import okhttp3.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SwissSignAPITest {

    ExtentSparkReporter extentSparkReporter;
    ExtentReports extentReports;
    ExtentTest extentTest;

    private static final String BASE_URL = "https://api.ra.pre.swisssign.ch/v2";
    private static final String CREDENTIALS_FILE = "credentials.properties";

    private static String USERNAME;
    private static String USER_SECRET;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String authToken;

    @BeforeClass
    public void startReporter() {
        extentSparkReporter = new ExtentSparkReporter(
                System.getProperty("user.dir") + "/reports/api-report.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(extentSparkReporter);

        extentSparkReporter.config().setDocumentTitle("Post.ch API Report");
        extentSparkReporter.config().setReportName("Post.ch API Test Report");
        extentSparkReporter.config().setTheme(Theme.STANDARD);
        extentSparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
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

            USERNAME = appProps.getProperty("username");
            USER_SECRET = appProps.getProperty("userSecret");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credentials: " + e.getMessage(), e);
        }
    }

    @Test(description = "Verify successful login with valid SwissID credentials")
    public void testLoginWithValidCredentials_ShouldReturnAuthToken() throws IOException {
        extentTest = extentReports.createTest("Login with valid SwissID credentials");

        // Prepare the request body
        RequestBody formBody = new FormBody.Builder()
                .add("userSecret", USER_SECRET)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(BASE_URL + "/jwt/" + USERNAME)
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            // Verify response status
            Assert.assertEquals(response.code(), 200, "Expected status code 200 for successful login");

            // Verify response body is not null
            Assert.assertNotNull(response.body(), "Response body should not be null");

            // Extract the token from response
            String responseBody = response.body().string();
            Assert.assertNotNull(responseBody, "Response body should contain a token");
            Assert.assertFalse(responseBody.trim().isEmpty(), "Token should not be empty");
            Assert.assertFalse(responseBody.contains("error"), "Response should not contain 'error'");
            Assert.assertFalse(responseBody.contains("errorMessage"), "Response should not contain 'errorMessage'");

            // Store the token for next test (assuming response is just the token string)
            this.authToken = responseBody.trim().replace("\"", "");

            extentTest.pass("Login successful. Token obtained.");
        }
    }

    @Test(description = "Verify client information retrieval and product count validation", dependsOnMethods = "testLoginWithValidCredentials_ShouldReturnAuthToken")
    public void testGetClientInformation_ShouldReturnValidProductCount() throws IOException {
        extentTest = extentReports.createTest("Get client information and validate product count");

        // Verify we have a token from the previous test
        Assert.assertNotNull(authToken, "Auth token should be available from login test");

        // Build the request
        Request request = new Request.Builder()
                .url(BASE_URL + "/clients")
                .post(RequestBody.create("", MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + authToken)
                .addHeader("Accept", "application/json")
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            // Verify response status
            Assert.assertEquals(response.code(), 200, "Expected status code 200 for successful client info retrieval");

            // Verify response body is not null
            Assert.assertNotNull(response.body(), "Response body should not be null");

            String responseBody = response.body().string();
            System.out.println("Client information response: " + responseBody);

            // Parse JSON response
            JsonNode rootNode = objectMapper.readTree(responseBody);
            Assert.assertTrue(rootNode.isArray(), "Response should be an array of clients");

            // Count total products across all clients
            int totalProductCount = 0;
            for (JsonNode clientNode : rootNode) {
                JsonNode productsNode = clientNode.get("products");
                if (productsNode != null && productsNode.isArray()) {
                    totalProductCount += productsNode.size();

                    // Log client information
                    String clientName = clientNode.get("name").asText();
                    int clientProductCount = productsNode.size();
                    extentTest.info("Client: " + clientName + " has " + clientProductCount + " products");
                }
            }

            // Verify that we have at least one product
            Assert.assertTrue(totalProductCount > 0, "Should have at least one product across all clients");

            extentTest.pass("Total products found across all clients: " + totalProductCount);
        }
    }

    @AfterClass
    public void tearDown() {
        extentReports.flush();
    }
}
