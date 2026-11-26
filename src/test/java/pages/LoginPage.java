package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriverWait wait;

    public LoginPage(WebDriverWait wait) {
        this.wait = wait;
    }

    public void loginAsAdmin() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                .sendKeys(System.getProperty("username", "Admin"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")))
                .sendKeys(System.getProperty("password", "admin123"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();
    }
}
