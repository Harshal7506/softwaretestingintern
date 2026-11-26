# PS 15 — Dashboard Widget Validation

Selenium and TestNG mini-project for the OrangeHRM demo. Four cases verify visible Dashboard widgets. Three cases click Quick Launch shortcuts and verify the destination routes without submitting forms or modifying employee records.

## Run

Install Java 17 or later, Maven, and Google Chrome. Selenium Manager resolves ChromeDriver when needed.

```sh
mvn test
```

Chrome runs headless by default. To watch the tests, run `mvn test -Dheadless=false`. The suite uses the public demo URL and demo admin credentials by default. Use `-DbaseUrl`, `-Dusername`, and `-Dpassword` to target a different test instance.

Open `target/extent-report.html` for the Extent HTML report. Maven also writes the raw results under `target/surefire-reports/`.

The starter framework roles are implemented by `BaseTest`, `LoginPage`, `NavigationBar`, `DriverFactory`, and `ExtentManager`. The three business page objects are `DashboardPage`, `WidgetSectionPage`, and `QuickLaunchPage`.
# softwaretestingintern
