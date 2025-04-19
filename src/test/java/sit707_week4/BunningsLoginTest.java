package sit707_week4;

import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import java.io.IOException;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BunningsLoginTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String SCREENSHOT_DIR = "screenshots/";
    private static final String BASE_URL = "https://www.bunnings.com.au";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String HOME_URL = BASE_URL + "/";

    private static final String VALID_EMAIL = "xivel95072@agiuse.com";
    private static final String VALID_PASSWORD = "Girish@1234";
    private static final String INVALID_EMAIL = "invalid@test.com";
    private static final String INVALID_PASSWORD = "wrong_password";

    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\gsirp\\Desktop\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create screenshots directory: " + e.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @After
    public void afterEachTest() {
        try {
            if (!driver.getCurrentUrl().startsWith(LOGIN_URL)) {
                driver.get(LOGIN_URL);
            }
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    private void takeScreenshot(String testName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("%s%s_%s.png", SCREENSHOT_DIR, testName, timestamp);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path path = Paths.get(filename);
            Files.write(path, screenshot);
            System.out.println("Screenshot saved to: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }

    private void performLogin(String emailInput, String passwordInput) {
        driver.get(LOGIN_URL);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name*='email'], input[name*='username'], input[type='email']")));

            try {
                WebElement cookieAccept = driver.findElement(
                    By.cssSelector("button.cookie-banner-accept, button#cookie-accept, button[onclick*='cookie']"));
                if (cookieAccept.isDisplayed()) {
                    cookieAccept.click();
                    Thread.sleep(1000);
                }
            } catch (Exception ignored) {}

            WebElement email = driver.findElement(
                By.cssSelector("input[name*='email'], input[name*='username'], input[type='email']"));
            WebElement password = driver.findElement(
                By.cssSelector("input[name*='password'], input[type='password']"));
            WebElement submit = driver.findElement(
                By.cssSelector("button[type='submit'], input[type='submit'], button#sign-in"));

            email.clear();
            password.clear();
            email.sendKeys(emailInput);
            password.sendKeys(passwordInput);
            submit.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(LOGIN_URL)));
        } catch (TimeoutException e) {
            takeScreenshot("login_timeout");
            throw new RuntimeException("Login process timeout: " + e.getMessage());
        } catch (NoSuchElementException e) {
            takeScreenshot("element_not_found");
            throw new RuntimeException("Required element not found: " + e.getMessage());
        } catch (Exception e) {
            takeScreenshot("login_error");
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    private boolean isLoginSuccessful() {
        try {
            return driver.getCurrentUrl().equals(HOME_URL) ||
                   driver.getCurrentUrl().startsWith(HOME_URL + "account") ||
                   driver.findElements(By.cssSelector("div.welcome-message, span.user-name")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLoginErrorPresent() {
        try {
            return driver.findElements(By.xpath(
                "//*[contains(text(), 'Invalid') or contains(text(), 'incorrect') or contains(text(), 'error')]"))
                .size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testValidLogin() {
        performLogin(VALID_EMAIL, VALID_PASSWORD);

        boolean result = isLoginSuccessful();
        if (!result) {
            takeScreenshot("valid_login_failed");
        }
        assertTrue("Valid login failed - not redirected to home page", result);
    }

    @Test
    public void testInvalidPassword() {
        performLogin(VALID_EMAIL, INVALID_PASSWORD);

        if (isLoginErrorPresent() || driver.getCurrentUrl().startsWith(LOGIN_URL)) {
            System.out.println("TEST: Invalid password - PASSED");
            assertTrue(true);
        } else {
            takeScreenshot("invalid_password_failed");
            fail("Failed to detect invalid password error");
        }
    }

    @Test
    public void testInvalidEmail() {
        performLogin(INVALID_EMAIL, VALID_PASSWORD);

        if (isLoginErrorPresent() || driver.getCurrentUrl().startsWith(LOGIN_URL)) {
            System.out.println("TEST: Invalid email - PASSED");
            assertTrue(true);
        } else {
            takeScreenshot("invalid_email_failed");
            fail("Failed to detect invalid email error");
        }
    }

    @Test
    public void testEmptyEmail() {
        performLogin("", VALID_PASSWORD);

        if (driver.getCurrentUrl().startsWith(LOGIN_URL) ||
            driver.findElements(By.xpath("//*[contains(text(), 'required')]")).size() > 0) {
            System.out.println("TEST: Empty email - PASSED");
            assertTrue(true);
        } else {
            takeScreenshot("empty_email_failed");
            fail("Failed to detect empty email validation");
        }
    }

    @Test
    public void testEmptyPassword() {
        performLogin(VALID_EMAIL, "");

        if (driver.getCurrentUrl().startsWith(LOGIN_URL) ||
            driver.findElements(By.xpath("//*[contains(text(), 'required')]")).size() > 0) {
            System.out.println("TEST: Empty password - PASSED");
            assertTrue(true);
        } else {
            takeScreenshot("empty_password_failed");
            fail("Failed to detect empty password validation");
        }
    }

    @Test
    public void testEmptyCredentials() {
        performLogin("", "");

        if (driver.getCurrentUrl().startsWith(LOGIN_URL) ||
            driver.findElements(By.xpath("//*[contains(text(), 'required')]")).size() > 1) {
            System.out.println("TEST: Empty credentials - PASSED");
            assertTrue(true);
        } else {
            takeScreenshot("empty_credentials_failed");
            fail("Failed to detect empty credentials validation");
        }
    }
}
