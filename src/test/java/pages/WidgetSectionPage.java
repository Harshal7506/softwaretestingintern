package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WidgetSectionPage {
    private final WebDriverWait wait;

    public WidgetSectionPage(WebDriverWait wait) {
        this.wait = wait;
    }

    public String visibleTitle(String title) {
        By heading = By.xpath("//div[contains(@class,'orangehrm-dashboard-widget-header')]"
                + "//*[normalize-space(.)='" + title + "' and not(*)]");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(heading)).getText();
    }
}
