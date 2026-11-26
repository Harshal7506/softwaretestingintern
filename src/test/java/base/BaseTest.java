package base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import utils.DriverFactory;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(System.getProperty("baseUrl", "https://opensource-demo.orangehrmlive.com"));
        new LoginPage(wait).loginAsAdmin();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
