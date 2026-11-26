package utils;

import java.util.Arrays;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentManager implements ITestListener {
    private final ExtentReports report = new ExtentReports();

    @Override
    public void onStart(ITestContext context) {
        report.attachReporter(new ExtentSparkReporter("target/extent-report.html"));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        entry(result).pass("All assertions passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        entry(result).fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = entry(result);
        if (result.getThrowable() == null) {
            test.skip("Skipped");
        } else {
            test.skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        report.flush();
    }

    private ExtentTest entry(ITestResult result) {
        return report.createTest(result.getName() + " " + Arrays.toString(result.getParameters()));
    }
}
