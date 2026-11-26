package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QuickLaunchPage {
    private final WebDriverWait wait;

    public QuickLaunchPage(WebDriverWait wait) {
        this.wait = wait;
    }

    public void openShortcut(String label) {
        By shortcut = By.xpath("//*[contains(@class,'orangehrm-quick-launch-card')]"
                + "[.//*[normalize-space(.)='" + label + "' and not(*)]]//button");
        wait.until(ExpectedConditions.elementToBeClickable(shortcut)).click();
    }
}
