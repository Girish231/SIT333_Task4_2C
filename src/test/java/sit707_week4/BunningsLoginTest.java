package sit707_week4;

import static org.junit.Assert.assertTrue;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class BunningsLoginTest {

    private WebDriver driver;
    private final String baseUrl = "https://www.bunnings.com.au/login?redirectUri=https%3A%2F%2Flogin.bunnings.com.au%2Foauth2%2Fv1%2Fauthorize%2Fredirect%3Fokta_key%3DX3QHw02MPde-2vkLC-17_O2IbdvH9OjMsCTDLz2j4Sg&login_hint={%22auth%22:{%22operation%22:%22retail_login%22,%22redirectUri%22:%22https://www.bunnings.com.au/login%22}}";

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\gsirp\\Desktop\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    private void performLogin(String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[.//span[text()='Sign in']]")
        ));

        usernameInput.clear();
        usernameInput.sendKeys(username);

        passwordInput.clear();
        passwordInput.sendKeys(password);

        signInButton.click();
    }

    private boolean isLoginSuccessful() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/flogin")
            ));
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isLoginFailed() {
        return driver.getPageSource().toLowerCase().contains("incorrect") ||
               driver.getCurrentUrl().contains("login");
    }

    private void takeScreenshot(String methodName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("screenshots/" + methodName + ".png");
        destFile.getParentFile().mkdirs(); // ensure directory exists

        try {
            Files.copy(screenshot.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot for " + methodName);
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        takeScreenshot(testName.getMethodName());  // Capture screenshot after every test
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testStudentIdentity() {
        String studentId = "222121587";
        Assert.assertNotNull("Student ID should not be null", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Girish";
        Assert.assertNotNull("Student name should not be null", studentName);
    }

    // Test Case 1: Valid Username & Password (changed to wrong password for submission)
    @Test
    public void testValidUsernameValidPassword() {
        performLogin("s222121587@deakin.edu.au", "WrongPassword123");
        assertTrue("Expected login to succeed with valid credentials", isLoginSuccessful());
    }

    // Test Case 2: Valid Username & Invalid Password
    @Test
    public void testValidUsernameInvalidPassword() {
        performLogin("s222121587@deakin.edu.au", "WrongPassword123");
        assertTrue("Expected login to fail with incorrect password", isLoginFailed());
    }

    // Test Case 3: Invalid Username & Valid Password
    @Test
    public void testInvalidUsernameValidPassword() {
        performLogin("invalid@test.com", "WrongPassword123");
        assertTrue("Expected login to fail with incorrect username", isLoginFailed());
    }

    // Test Case 4: Invalid Username & Password
    @Test
    public void testInvalidUsernameInvalidPassword() {
        performLogin("invalid@test.com", "WrongPassword123");
        assertTrue("Expected login to fail with both credentials incorrect", isLoginFailed());
    }

    // Test Case 5: Empty Username & Valid Password
    @Test
    public void testEmptyUsernameValidPassword() {
        performLogin("", "WrongPassword123");
        assertTrue("Expected login to fail with empty username", isLoginFailed());
    }

    // Test Case 6: Valid Username & Empty Password
    @Test
    public void testValidUsernameEmptyPassword() {
        performLogin("s222121587@deakin.edu.au", "");
        assertTrue("Expected login to fail with empty password", isLoginFailed());
    }

    // Test Case 7: Empty Username & Password
    @Test
    public void testEmptyUsernameEmptyPassword() {
        performLogin("", "");
        assertTrue("Expected login to fail with empty credentials", isLoginFailed());
    }
}
