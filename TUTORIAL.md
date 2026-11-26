# PS 15 — Dashboard Widget Validation Tutorial

This guide explains how the project is structured, how to run it on this machine, and the exact logical flow behind each of the 7 automated test cases.

---

## 1. Project Architecture

The project uses **Java 17**, **Maven**, **Selenium 4 (`4.28.1`)**, **TestNG (`7.10.2`)**, and **ExtentReports (`5.1.2`)** following the **Page Object Model (POM)** pattern:

| Layer | File | Responsibility |
| --- | --- | --- |
| **Test Base** | `src/test/java/base/BaseTest.java` | Creates ChromeDriver + 20s `WebDriverWait`, opens the target URL, logs in as Admin before each test, and quits the browser after each test. |
| **Test Suite** | `src/test/java/tests/DashboardValidationTest.java` | Defines the 2 parameterized `@Test` methods (`dashboardDisplaysWidget` and `quickLaunchOpensDestination`) totaling 7 test executions. |
| **Driver** | `src/test/java/utils/DriverFactory.java` | Configures `ChromeOptions` (`--window-size=1440,1000`, `--headless=new` by default). |
| **Reporting** | `src/test/java/utils/ExtentManager.java` | TestNG `ITestListener` that records pass/fail/skip results to `target/extent-report.html`. |
| **Page: Login** | `src/test/java/pages/LoginPage.java` | Enters `username` / `password` and submits the login form. |
| **Page: Nav** | `src/test/java/pages/NavigationBar.java` | Clicks the Dashboard link in the left navigation bar. |
| **Page: Dashboard** | `src/test/java/pages/DashboardPage.java` | Reads the breadcrumb heading and polls the browser URL path until it ends with an expected route suffix. |
| **Page: Widgets** | `src/test/java/pages/WidgetSectionPage.java` | Locates a visible dashboard widget header by exact normalized text. |
| **Page: Quick Launch** | `src/test/java/pages/QuickLaunchPage.java` | Locates a Quick Launch card by its label and clicks its shortcut button. |

---

## 2. How to Run the Project

### Directory
Run commands from the project root:
```sh
cd /Users/harshalmore/Desktop/testing_miniproject
```

### Run Command (on this machine)
Because `mvn` and JDK 17 are not in the default `PATH` / `JAVA_HOME`, invoke them explicitly:

```sh
JAVA_HOME=/Users/harshalmore/.jdk/jdk-17.0.18/jdk-17.0.18+8/Contents/Home /Users/harshalmore/.maven/maven-3.9.15/bin/mvn test
```

### Useful Runtime Flags
- **Watch the browser UI** (disable headless mode):
  ```sh
  JAVA_HOME=/Users/harshalmore/.jdk/jdk-17.0.18/jdk-17.0.18+8/Contents/Home /Users/harshalmore/.maven/maven-3.9.15/bin/mvn test -Dheadless=false
  ```
- **Target a custom OrangeHRM instance or credentials**:
  ```sh
  JAVA_HOME=/Users/harshalmore/.jdk/jdk-17.0.18/jdk-17.0.18+8/Contents/Home /Users/harshalmore/.maven/maven-3.9.15/bin/mvn test \
    -DbaseUrl=https://your-instance.example.com \
    -Dusername=Admin \
    -Dpassword=yourPassword
  ```

---

## 3. Common Lifecycle for Every Test

Every one of the 7 test runs executes in an isolated browser session:

1. **`@BeforeMethod` (`BaseTest.setup`)**
   - `DriverFactory.getDriver()` starts a fresh `ChromeDriver` at `1440x1000` (headless unless `-Dheadless=false`).
   - Creates a `WebDriverWait` with a **20-second** timeout.
   - Navigates to `baseUrl` (default: `https://opensource-demo.orangehrmlive.com`).
   - `LoginPage.loginAsAdmin()` waits for `name="username"` and `name="password"`, types the credentials (default: `Admin` / `admin123`), and clicks `button[type='submit']`.
2. **Test Method Execution** (see Section 4 below).
3. **`@AfterMethod(alwaysRun = true)` (`BaseTest.tearDown`)**
   - Calls `driver.quit()` to close Chrome even if the test fails.
4. **Listener (`ExtentManager`)**
   - Logs each test result with its parameters to `target/extent-report.html`.

---

## 4. Logical Overview for Each Test

`DashboardValidationTest` contains **2 `@Test` methods** backed by **2 `@DataProvider`s**, producing **7 test cases**:

### Shared Helper: `openDashboard()`
Both test methods start by calling `openDashboard()`:
1. `NavigationBar.openDashboard()` waits for and clicks `nav a[href$='/dashboard/index']`.
2. `DashboardPage.waitForRoute("/dashboard/index")` polls `driver.getCurrentUrl()` until the URL path ends with `/dashboard/index`.
3. `DashboardPage.heading()` reads `.oxd-topbar-header-breadcrumb h6` and asserts it equals `"Dashboard"`.

---

### Group A: Dashboard Widget Visibility (`dashboardDisplaysWidget`)
- **DataProvider (`widgets`)**: Supplies 4 widget titles.
- **Goal**: Confirm that each expected widget card is rendered and visible on the Dashboard.
- **Flow**:
  1. Call `openDashboard()` to ensure the user is on `/dashboard/index` with heading `"Dashboard"`.
  2. Call `WidgetSectionPage.visibleTitle(expectedTitle)`, which builds an XPath targeting leaf elements inside `div[contains(@class,'orangehrm-dashboard-widget-header')]` whose `normalize-space(.)` equals `expectedTitle`, and waits until it is visible.
  3. Assert that the returned text equals `expectedTitle`.

#### Individual Cases (1–4)
1. **`dashboardDisplaysWidget[Time at Work]`**
   - **Logic**: Logs in, navigates to Dashboard, and verifies the `"Time at Work"` widget header is visible on the page.
2. **`dashboardDisplaysWidget[My Actions]`**
   - **Logic**: Logs in, navigates to Dashboard, and verifies the `"My Actions"` widget header is visible on the page.
3. **`dashboardDisplaysWidget[Quick Launch]`**
   - **Logic**: Logs in, navigates to Dashboard, and verifies the `"Quick Launch"` widget header is visible on the page.
4. **`dashboardDisplaysWidget[Buzz Latest Posts]`**
   - **Logic**: Logs in, navigates to Dashboard, and verifies the `"Buzz Latest Posts"` widget header is visible on the page.

---

### Group B: Quick Launch Shortcut Navigation (`quickLaunchOpensDestination`)
- **DataProvider (`shortcuts`)**: Supplies 3 `(label, expectedRoute)` pairs.
- **Goal**: Verify each Quick Launch shortcut button navigates to its intended OrangeHRM route without submitting forms or modifying employee data.
- **Flow**:
  1. Call `openDashboard()` to land on the Dashboard and confirm the `"Dashboard"` heading.
  2. Call `QuickLaunchPage.openShortcut(label)`, which finds the `orangehrm-quick-launch-card` containing a leaf element with `normalize-space(.) == label`, waits until its `//button` is clickable, and clicks it.
  3. Call `DashboardPage.waitForRoute(expectedRoute)`, which waits until `URI.create(driver.getCurrentUrl()).getPath()` ends with `expectedRoute`.
  4. Assert `path.endsWith(expectedRoute)` is `true`.

#### Individual Cases (5–7)
5. **`quickLaunchOpensDestination[Assign Leave, /leave/assignLeave]`**
   - **Logic**: Opens Dashboard, clicks the `"Assign Leave"` Quick Launch button, and verifies the browser navigates to a URL path ending in `/leave/assignLeave`.
6. **`quickLaunchOpensDestination[Leave List, /leave/viewLeaveList]`**
   - **Logic**: Opens Dashboard, clicks the `"Leave List"` Quick Launch button, and verifies the browser navigates to a URL path ending in `/leave/viewLeaveList`.
7. **`quickLaunchOpensDestination[Timesheets, /time/viewEmployeeTimesheet]`**
   - **Logic**: Opens Dashboard, clicks the `"Timesheets"` Quick Launch button, and verifies the browser navigates to a URL path ending in `/time/viewEmployeeTimesheet`.

---

## 5. Viewing Test Reports

After a run completes, inspect the generated outputs:
- **Extent HTML Report**: `target/extent-report.html` (visual summary with pass/fail status per parameterized case)
- **TestNG / Surefire Reports**:
  - `target/surefire-reports/emailable-report.html`
  - `target/surefire-reports/index.html`
  - `target/surefire-reports/TEST-TestSuite.xml`

---

## 6. Core Testing Concepts Used (Selenium + Java)

If you are new to Selenium with Java, here are the key concepts this project uses, explained simply:

### 1. Selenium `WebDriver` (Browser Automation)
- **What it is**: `WebDriver` is the remote control for a real web browser. Your Java code sends commands (open a URL, type text, click a button), and Chrome executes them.
- **In this project**: `DriverFactory.getDriver()` creates a `ChromeDriver` with `ChromeOptions` (such as window size `1440x1000` and `--headless=new`).

### 2. Headless Browser Execution
- **What it is**: Running Chrome *without* showing a visible window on your screen. It still loads pages, runs JavaScript, and clicks elements the same way, just faster and cleaner for automated runs.
- **In this project**: Headless mode is on by default (`headless=true`), and you can turn it off anytime with `-Dheadless=false` to watch the browser live.

### 3. Locators (Finding Elements on a Page)
- **What it is**: Before Selenium can click or read something, it needs to know *where* it is in the HTML. Locators (`By.*`) are the addresses used to find elements:
  - **`By.name(...)`**: Simple attribute lookup, e.g., `By.name("username")` in `LoginPage`.
  - **`By.cssSelector(...)`**: Uses CSS rules, e.g., `By.cssSelector("button[type='submit']")` or `By.cssSelector(".oxd-topbar-header-breadcrumb h6")`.
  - **`By.xpath(...)`**: Searches by structure or visible text, useful when an element has no unique `id` or `name`—for example, finding a widget header or Quick Launch card by its exact label in `WidgetSectionPage` and `QuickLaunchPage`.

### 4. Explicit Waits (`WebDriverWait`)
- **What it is**: Modern web pages load data asynchronously. If your test clicks immediately before an element appears, it fails. Instead of a fixed pause like `Thread.sleep(...)`, an **explicit wait** polls the page repeatedly until a condition is met (or a timeout expires).
- **In this project**: `BaseTest` sets a 20-second `WebDriverWait`. Page objects use `ExpectedConditions.visibilityOfElementLocated(...)` (before reading/typing) and `ExpectedConditions.elementToBeClickable(...)` (before clicking). `DashboardPage.waitForRoute(...)` also uses a custom lambda wait to poll the URL path until navigation finishes.

### 5. Page Object Model (POM)
- **What it is**: A design pattern where each page or UI section has its own Java class (e.g., `LoginPage`, `DashboardPage`, `QuickLaunchPage`).
- **Why it helps**: Locators and UI actions live in one place, while test methods in `DashboardValidationTest` read like plain English steps. If a button's CSS selector changes later, you only update one page class instead of every test.

### 6. TestNG Annotations & Test Lifecycle
- **`@BeforeMethod` / `@AfterMethod`**: Runs `setup()` before *each* test and `tearDown()` (`driver.quit()`) after *each* test. This gives **test isolation**—every test starts with a clean browser and fresh login, so one failure never breaks the next test.
- **`@Test`**: Marks a Java method as an executable test case.
- **`@DataProvider` (Data-Driven Testing)**: Lets you write one test method and run it multiple times with different inputs. Here, `widgets()` runs `dashboardDisplaysWidget` 4 times, and `shortcuts()` runs `quickLaunchOpensDestination` 3 times.
- **`@Listeners(ExtentManager.class)`**: Hooks into TestNG events (`onTestSuccess`, `onTestFailure`, `onTestSkipped`) to automatically build `target/extent-report.html`.

### 7. Assertions (`Assert`)
- **What it is**: A test is not a test without a check. Assertions compare what the app *actually* did against what you *expected*. If they don't match, the test fails immediately.
- **In this project**: `Assert.assertEquals(...)` checks the `"Dashboard"` heading and widget titles, and `Assert.assertTrue(...)` checks that the destination URL ends with the expected route.

### 8. Configurable System Properties (`-D` flags)
- **What it is**: `System.getProperty("key", "defaultValue")` lets you change test settings from the command line without editing Java code.
- **In this project**: `baseUrl`, `username`, `password`, and `headless` all have defaults and can be overridden with `-DbaseUrl=...`, `-Dusername=...`, `-Dpassword=...`, and `-Dheadless=false`.

### 9. Maven & Surefire (`pom.xml`)
- **What it is**: Maven downloads the required libraries (Selenium, TestNG, ExtentReports), compiles the Java test classes, and uses the **Maven Surefire Plugin** to execute the TestNG suite via `mvn test`.

