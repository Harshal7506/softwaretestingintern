package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
    private DriverFactory() {}

    public static WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000");
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }
}
