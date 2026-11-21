![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Run Maven with GitHub CI](https://github.com/anhtester/AutomationFrameworkSelenium/actions/workflows/maven.yml/badge.svg)](https://github.com/anhtester/AutomationFrameworkSelenium/actions/workflows/maven.yml)

## Test Automation Framework using Selenium Java and TestNG building by Anh Tester

Version **3.0.0** - see [CHANGELOG.txt](CHANGELOG.txt) · Allure Report: https://anhtester.github.io/AutomationFrameworkSelenium/

### **💥Important**: when clone this repo, you should select '**Recursive**' to get all submodules

```
git clone --recursive https://github.com/anhtester/AutomationFrameworkSelenium.git
```

The keyword library **WebUI** is the submodule https://github.com/anhtester/keywords

🔆 **SOME FEATURES IN FRAMEWORK**

1. Run test cases in parallel (TestNG), on Chrome, Edge, Firefox, Safari, local or Selenium Grid
2. Config in properties files, every key can be overridden with `-Dkey=value` or an environment variable
3. Main keyword **WebUI**: waits, retry on stale/covered elements, soft assert, logs and reports in one call
4. Detailed logs: one log file per test case, every WebDriver action with its duration
5. Allure Report 2 and Extent Report, with stack trace, page source, browser console logs and test log on failure
6. Screenshot and video record of test cases
7. Read test data from Excel, CSV, JSON and properties files
8. Retry failed tests (IRetryAnalyzer + IAnnotationTransformer)
9. Send report by email and Telegram Bot
10. Generate test data with DataFaker
11. Selenium Grid with Docker (`docker-compose.yml`)
12. CI with GitHub Actions: Chrome + Edge, reports published to GitHub Pages

### ✳️ **SYSTEM REQUIREMENTS**

- Java JDK 17 or newer
- Maven 3.9+ (or the Maven bundled with IntelliJ IDEA)
- Chrome, Edge or Firefox. Browser drivers are downloaded automatically by Selenium Manager (no PATH setting)
- Allure: nothing to install, Maven downloads Allure Report 2.46.2 (`mvn allure:serve`)
- Docker (optional): only to run Selenium Grid locally
- **IntelliJ IDEA** is the best choice (easy change the JDK version)

![image](https://user-images.githubusercontent.com/87883620/161707184-7ad558f2-0d7d-4851-bfd6-2796d4e46593.png)

### ✳️ **HOW TO USE**

**1. Run the test cases**

- Default suite of `mvn test` and CI: `src/test/resources/suites/CMS/RegressionCMS.xml` (all CMS tests grouped in one suite)

```
mvn clean test
```

- Run any suite without editing pom.xml (PowerShell: put the parameter in quotes):

```
mvn test "-Dsuite=src/test/resources/suites/SuiteAll.xml"
```

- Run a suite XML directly from IntelliJ IDEA: `src/test/resources/suites/`

**2. Config**

- `src/test/resources/config/config.properties` (browser, headless, timeouts, reports...) and `data.properties` (test data)
- Every key can be overridden by a JVM property or an environment variable (CI):
  `mvn test -DBROWSER=firefox -DHEADLESS=false -DRETRY_TEST_FAIL=0`
- Browser priority: `-DBROWSER` / env `BROWSER` > parameter `BROWSER` of the suite XML > `BROWSER` in config.properties > chrome
- Stability options: `RETRY_TEST_FAIL`, `DRIVER_CREATE_RETRY`, `CLICK_RETRY` (stale/covered element, last attempt uses a JS click)
- Object repository (locators): `src/test/resources/objects/locators.properties`, read with `ObjectUtils.getByLocatorFromConfig("name")`

**3. Logs**

- `exports/logs/applog.log`: whole run, `exports/logs/error.log`: only WARN/ERROR
- `exports/logs/tests/<Class.method>.log`: one detailed (DEBUG) log file per test case, including every WebDriver action
  with its duration (`WEBDRIVER_EVENT_LOG=yes`). Parallel tests never mix their logs.
- Every line has the thread, the test name and the caller (`Class.method:line`); errors keep the full stack trace
- Passwords/tokens typed with `setText` are masked (`******`)
- Levels: `-Dlog.level=debug` (console + applog), `-Dlog.test.level=info` (per-test files), `-Dlog.dir=...`
- In your code: `LogUtils.info()`, `LogUtils.warn()`, `LogUtils.error()`, `LogUtils.debug()` (`com.anhtester.utils.LogUtils`)

**4. Allure Report**

- Without installing Allure: `mvn allure:serve` (open in the browser) or `mvn allure:report` (`target/site/allure-maven-plugin`)
- With Allure CLI 2.x: `allure serve target/allure-results`
- When a test fails (`FAILURE_DIAGNOSTICS=yes`): screenshot, stack trace, URL, page source, browser console logs (Chrome/Edge) and the test log
- Insert **@Step("title/message")** above **_@Test_** or any **_Method_** in the project

![image](https://user-images.githubusercontent.com/87883620/161662507-9e4dc698-e452-4b43-a4f5-9808c81419a2.png)

![image](https://user-images.githubusercontent.com/87883620/161657680-af29973d-4e52-451f-b1d6-40b12d182845.png)

**5. Extent Report**

- File: `exports/ExtentReports/ExtentReports.html`
- Insert **FrameworkAnnotation** (author, category) above the test method, values from enums `src/main/java/com/anhtester/enums`
- Setup in `TestListener`

![image](https://user-images.githubusercontent.com/87883620/161657646-3dd652c6-a310-4006-a0cb-de63e2e87e36.png)

**6. Screenshot and video record**

- `config.properties`: `SCREENSHOT_PASSED_TCS`, `SCREENSHOT_FAILED_TCS`, `SCREENSHOT_SKIPPED_TCS`, `SCREENSHOT_ALL_STEPS` = yes/no
- Screenshots: `exports/ExportData/Images`, downloads of the browser: `exports/Downloads`
- `VIDEO_RECORD=yes`: video of each test in `exports/ExportData/Videos` (needs a visible screen: `-DHEADLESS=false`)

**7. Read test data**

- Excel with DataProvider, example: `src/test/java/com/anhtester/dataprovider/DataProviderAddProduct.java` (`ExcelHelpers`, Map and Hashtable)
- JSON: `JsonUtils.get("key")` or JsonPath `JsonUtils.getData("$.store.book[0]")`
- Properties: `PropertiesHelpers.getValue("key")`

**8. Main Keyword: WebUI**

- WebUI (`src/main/java/com/anhtester/keywords`, submodule https://github.com/anhtester/keywords) contains the common functions
- How to use: `WebUI.function_name`, example: `WebUI.clickElement(By by)`, `WebUI.setText(By by, String value)`, `WebUI.verifyElementVisible(By by)`
- Base functions: `src/main/java/com/anhtester/utils`, `src/main/java/com/anhtester/helpers`
- Samples:
  - CMS test cases: `src/test/java/com/anhtester/projects/cms/testcases` (extend `BaseTestCMS`)
  - Keyword and utility demos: `src/test/java/com/anhtester/projects/demo` (`TestHandle`, `TestSimpleCode`)
- Keywords with `java.awt.Robot` (upload with local form, drag and drop by mouse) need a visible local browser:
  they are skipped in headless mode and on Selenium Grid

**9. Retry failed tests**

- `RETRY_TEST_FAIL = 1` in config.properties (or `-DRETRY_TEST_FAIL=0` to disable)
- Enabled for every run (suite XML, IDE, Maven): `AnnotationTransformer` is registered in
  `src/test/resources/META-INF/services/org.testng.ITestNGListener`. A test with its own
  `@Test(retryAnalyzer = ...)` keeps it

**10. Send report by email**

- `SEND_EMAIL_TO_USERS = yes` in config.properties
- Mail account with JVM properties or environment variables (never commit a password):
  `EMAIL_FROM`, `EMAIL_PASSWORD`, `EMAIL_TO` (comma separated), `EMAIL_SERVER` (default smtp.gmail.com), `EMAIL_PORT` (default 587)
- Gmail: use an App Password

![image](https://user-images.githubusercontent.com/87883620/161659238-88337f69-b742-4cd7-87f2-76670519c8dd.png)

**11. Send report to Telegram Bot**

- `SEND_REPORT_TO_TELEGRAM = yes`, `TELEGRAM_TOKEN`, `TELEGRAM_CHATID` (config.properties, `-D` or environment variables)
- Create a bot and get its token with **@BotFather**: https://core.telegram.org/bots/tutorial
- Open your bot and send **/start**, then get the Chat ID: `https://api.telegram.org/bot<YOUR_TOKEN>/getUpdates` ⇒ `chat.id`

```
"chat": {
    "id": 123456789,
    "first_name": "Anh Tester",
    "username": "anhtester",
    "type": "private"
}
```

**12. Use Selenium Grid**

- Run Selenium Grid with Docker: Hub + Chrome/Edge/Firefox nodes (`docker-compose.yml`, version matches `selenium.version` in pom.xml)

```
docker compose up -d
mvn test -DTARGET=remote -DREMOTE_URL=localhost -DREMOTE_PORT=4444 -DBROWSER=chrome
```

- Grid console: http://localhost:4444/ui
- Without Docker: download [selenium-server-4.53.0.jar](https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.53.0/selenium-server-4.53.0.jar) and run `java -jar selenium-server-4.53.0.jar standalone`
- `REMOTE_URL` can also be a full URL (ex: `https://hub.example.com/wd/hub`)

**13. Generate test data with DataFaker**

- `DataFakerUtils`, `DataGenerateUtils` (`LOCATE = vi` in config.properties)
- Document DataFaker: https://www.datafaker.net/documentation/getting-started/
- Anh Tester blog: https://anhtester.com/blog/tao-du-lieu-gia-voi-datafaker

**14. CI with GitHub Actions**

- `.github/workflows/maven.yml`: run the tests on Chrome then Edge, the job is red when a test fails
- Artifacts of each run: Allure Report, Extent Report, logs, screenshots, videos
- Allure Report and Javadoc are published to GitHub Pages from the `main` branch: https://anhtester.github.io/AutomationFrameworkSelenium/

### 🔆 Project structure

```
📦AutomationFrameworkSelenium
 ┣ 📂.github
 ┃ ┗ 📂workflows
 ┃ ┃ ┗ 📜maven.yml
 ┣ 📂src
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂anhtester
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotations
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FrameworkAnnotation.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂constants
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FrameworkConstants.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂driver
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DriverEventListener.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DriverManager.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TargetFactory.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Browser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FailureHandling.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Target.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exceptions
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FrameworkException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HeadlessNotSupportedException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidPathForExcelException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidPathForExtentReportFileException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidPathForFilesException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidRemoteWebDriverURLException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TargetNotValidException.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂helpers
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CaptureHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DatabaseHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DiagnosticHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExcelHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FileHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JsonHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PropertiesHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ScreenRecorderHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SystemHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂keywords
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜README.md
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebUI.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mail
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailAttachmentsSender.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜EmailConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂reports
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AllureManager.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExtentReportManager.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExtentTestManager.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TelegramManager.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂utils
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserInfoUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DataFakerUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DataGenerateUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DateUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DecodeUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailSendUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IconUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JsonUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LanguageUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LocalStorageUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LogUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MaskUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ObjectUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ZipUtils.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📂META-INF
 ┃ ┃ ┃ ┃ ┗ 📂services
 ┃ ┃ ┃ ┃ ┃ ┗ 📜io.qameta.allure.listener.TestLifecycleListener
 ┃ ┃ ┃ ┗ 📜log4j2.xml
 ┃ ┗ 📂test
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂anhtester
 ┃ ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dataprovider
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DataProviderAddProduct.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂listeners
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AllureListener.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AnnotationTransformer.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Retry.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestListener.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂projects
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂cms
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂brands
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BrandPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂category
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CategoryPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂logins
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginPageCMS.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂products
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddProductPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂testcases
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddProductTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductInfoTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProfileTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂users
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dashboard
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DashboardPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂products
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProductInfoPageCMS.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂profiles
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProfilePage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseTestCMS.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommonPageCMS.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂demo
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestHandle.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestSimpleCode.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┣ 📂allure
 ┃ ┃ ┃ ┃ ┃ ┣ 📜categories.json
 ┃ ┃ ┃ ┃ ┃ ┣ 📜environment.xml
 ┃ ┃ ┃ ┃ ┃ ┗ 📜executor.json
 ┃ ┃ ┃ ┃ ┣ 📜allure.properties
 ┃ ┃ ┃ ┃ ┣ 📜config.json
 ┃ ┃ ┃ ┃ ┣ 📜config.properties
 ┃ ┃ ┃ ┃ ┗ 📜data.properties
 ┃ ┃ ┃ ┣ 📂datajson
 ┃ ┃ ┃ ┃ ┣ 📜arraysample.json
 ┃ ┃ ┃ ┃ ┣ 📜book.json
 ┃ ┃ ┃ ┃ ┣ 📜store.json
 ┃ ┃ ┃ ┃ ┗ 📜tools.json
 ┃ ┃ ┃ ┣ 📂META-INF
 ┃ ┃ ┃ ┃ ┗ 📂services
 ┃ ┃ ┃ ┃ ┃ ┗ 📜org.testng.ITestNGListener
 ┃ ┃ ┃ ┣ 📂objects
 ┃ ┃ ┃ ┃ ┗ 📜locators.properties
 ┃ ┃ ┃ ┣ 📂suites
 ┃ ┃ ┃ ┃ ┣ 📂CMS
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AddProductCMS.xml
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginTestCMS.xml
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderProductCMS.xml
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductInfoCMS.xml
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProfileCMS.xml
 ┃ ┃ ┃ ┃ ┃ ┗ 📜RegressionCMS.xml
 ┃ ┃ ┃ ┃ ┗ 📜SuiteAll.xml
 ┃ ┃ ┃ ┣ 📂testdata
 ┃ ┃ ┃ ┃ ┣ 📜DOCX_File_01.docx
 ┃ ┃ ┃ ┃ ┣ 📜LoginCSV.csv
 ┃ ┃ ┃ ┃ ┗ 📜TxtFileData.txt
 ┃ ┃ ┃ ┣ 📂testdataCMS
 ┃ ┃ ┃ ┃ ┣ 📜Book1.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜CMS_DATA.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜Cosy.png
 ┃ ┃ ┃ ┃ ┣ 📜GetProductInfo.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜gio-qua-1.jpg
 ┃ ┃ ┃ ┃ ┣ 📜gio-qua-2.jpg
 ┃ ┃ ┃ ┃ ┣ 📜gio-qua-3.jpg
 ┃ ┃ ┃ ┃ ┣ 📜gio-qua-4.jpg
 ┃ ┃ ┃ ┃ ┗ 📜Login.xlsx
 ┃ ┃ ┃ ┗ 📜pdf-config.json
 ┣ 📜.gitignore
 ┣ 📜CHANGELOG.txt
 ┣ 📜docker-compose.yml
 ┣ 📜pom.xml
 ┗ 📜README.md
```

## ⭐️ I will support your company if needed

## Copyright 2022-2026 Anh Tester

> Anh Tester Blog: https://anhtester.com/

[![Anh Tester](https://anhtester.com/storage/photos/shares/Configs/anhtester_logo_512.png "Anh Tester Automation Testing")](https://anhtester.com/)
