package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NavigationBar {
    private final WebDriverWait wait;

    public NavigationBar(WebDriverWait wait) {
        this.wait = wait;
    }

    public void openDashboard() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("nav a[href$='/dashboard/index']"))).click();
    }
}
