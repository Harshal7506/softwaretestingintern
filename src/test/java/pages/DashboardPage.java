package pages;

import java.net.URI;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage {
    public static final String ROUTE = "/dashboard/index";
    private final WebDriverWait wait;

    public DashboardPage(WebDriverWait wait) {
        this.wait = wait;
    }

    public String heading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".oxd-topbar-header-breadcrumb h6"))).getText();
    }

    public String waitForRoute(String expectedSuffix) {
        return wait.until(driver -> {
            String path = URI.create(driver.getCurrentUrl()).getPath();
            return path != null && path.endsWith(expectedSuffix) ? path : null;
        });
    }
}
