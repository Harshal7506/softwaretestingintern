package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.NavigationBar;
import pages.QuickLaunchPage;
import pages.WidgetSectionPage;
import utils.ExtentManager;

@Listeners(ExtentManager.class)
public class DashboardValidationTest extends BaseTest {
    @DataProvider
    public Object[][] widgets() {
        return new Object[][] {{"Time at Work"}, {"My Actions"}, {"Quick Launch"}, {"Buzz Latest Posts"}};
    }

    @DataProvider
    public Object[][] shortcuts() {
        return new Object[][] {
            {"Assign Leave", "/leave/assignLeave"},
            {"Leave List", "/leave/viewLeaveList"},
            {"Timesheets", "/time/viewEmployeeTimesheet"}
        };
    }

    @Test(dataProvider = "widgets")
    public void dashboardDisplaysWidget(String expectedTitle) {
        openDashboard();
        Assert.assertEquals(new WidgetSectionPage(wait).visibleTitle(expectedTitle),
                expectedTitle, "Dashboard widget title");
    }

    @Test(dataProvider = "shortcuts")
    public void quickLaunchOpensDestination(String label, String expectedRoute) {
        DashboardPage dashboard = openDashboard();
        new QuickLaunchPage(wait).openShortcut(label);
        Assert.assertTrue(dashboard.waitForRoute(expectedRoute).endsWith(expectedRoute),
                label + " should open " + expectedRoute);
    }

    private DashboardPage openDashboard() {
        new NavigationBar(wait).openDashboard();
        DashboardPage dashboard = new DashboardPage(wait);
        dashboard.waitForRoute(DashboardPage.ROUTE);
        Assert.assertEquals(dashboard.heading(), "Dashboard", "Dashboard page heading");
        return dashboard;
    }
}
