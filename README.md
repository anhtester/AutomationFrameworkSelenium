![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java CI with Maven on Windows](https://github.com/anhtester/AutomationFrameworkSelenium/actions/workflows/maven.yml/badge.svg)](https://github.com/anhtester/AutomationFrameworkSelenium/actions/workflows/maven.yml)

## Test Automation Framework Selenium Java with TestNG building by Anh Tester

🔆 **SOME FEATURES IN FRAMEWORK**

1. Run the parallel test case
2. Read Config from Properties file
3. Extent Report
4. Allure Report
5. Send Mail after the run test (Report information and HTML file attachment)
6. Write Log to file
7. Record video and Screenshot test case
8. Read data test from Excel file (xlsx, csv, json,...)
9. Base function in the package: utils, helpers
10. Read data test from Json file
11. Main Keyword: WebUI (call common function)
12. Sample test all function in WebUI keyword
13. Send message/report to Telegram Bot
14. Run Selenium Grid (remote)
15. Use DataFaker and JavaFaker to generate data
16. Retry Failed Test in TestNG with IRetryAnalyzer and IAnnotationTransformer
17. Javadoc for this source

### ✳️ **SYSTEM REQUIREMENTS**

- Install JDK (recommend JDK >=11)
- Install Chrome Browser, Edge Browser, Firefox Browser
- Run well on the **Windows** platform
- Setup **Allure ENV**:
  https://mvnrepository.com/artifact/io.qameta.allure/allure-java-commons
  or
  https://anhtester.com/blog/selenium-java/selenium-java-tai-nguyen-cai-dat-moi-truong)
  Download jar and setting Variable Environment as Java JDK

  ![image](https://user-images.githubusercontent.com/87883620/161661705-b8706957-5a26-4faf-8ddf-2f9aef78418e.png)

- Use **IntelliJ IDEA** is the best choice (easy change the JDK version)

![image](https://user-images.githubusercontent.com/87883620/161707184-7ad558f2-0d7d-4851-bfd6-2796d4e46593.png)

### ✳️ **HOW TO USE**

**1. Run parallel the test case**

- Run test case in suite XML (**src/test/resources/suites/**)
- Run test case from Maven pom.xml file
  (**mvn clean test**)

  ![image](https://user-images.githubusercontent.com/87883620/161658761-5040e527-b410-46b3-8697-3298523e201d.png)

**2. Read Config from Properties file**

![image](https://user-images.githubusercontent.com/87883620/194396141-eb0e2750-9b8d-42bb-803d-43b7b5199a73.png)

**3. Extent Report**

- Insert "FrameworkAnnotation" as sample or None:

![image](https://user-images.githubusercontent.com/87883620/161657646-3dd652c6-a310-4006-a0cb-de63e2e87e36.png)

- The base value read from Enums (src/main/java/anhtester/com/enums)
- Setup on TestListener and BaseTest

![image](https://user-images.githubusercontent.com/87883620/161657754-c29b1ee9-f2fb-44b3-bee0-9f425a7cab6f.png)

- **Pdf Report**

![image](https://user-images.githubusercontent.com/87883620/194396654-5b01cb68-161a-4caf-824d-b57cd6091586.png)
![image](https://user-images.githubusercontent.com/87883620/194396689-ac5381cc-8689-4ce0-8c7f-d2487047faec.png)
![image](https://user-images.githubusercontent.com/87883620/194396707-ed1308a0-e4bf-4cac-b5b6-0fde44c81091.png)

**4. Allure Report**

- Open Terminal: **_allure serve target/allure-results_**

![image](https://user-images.githubusercontent.com/87883620/161662507-9e4dc698-e452-4b43-a4f5-9808c81419a2.png)

- Insert **@Step("title/message")** above **_@Test_** or any **_Method_** in the project
- (As sample picture above step 3)

![image](https://user-images.githubusercontent.com/87883620/161657680-af29973d-4e52-451f-b1d6-40b12d182845.png)

![image](https://user-images.githubusercontent.com/87883620/161657689-10365747-ed8f-4ca8-9d84-8060514f216b.png)

**5. Send Mail after the run test**

- Config **true/false** in config.properties
  (**_src/test/resources/config/config.properties_**)
- send_email_to_users=**true** is enable send mail
- Config mail with email and password in **_src/main/java/anhtester/com/mail/EmailConfig.java_**
- Note: if Gmail, you use App Password

![image](https://user-images.githubusercontent.com/87883620/161658851-2aa41091-ac99-45d9-a79f-aaa828052efb.png)

![image](https://user-images.githubusercontent.com/87883620/161659238-88337f69-b742-4cd7-87f2-76670519c8dd.png)

**6. Write Log to file**

- Call class: Log.info , Log.pass, Log.error,... (**Log** is a custom global class from Log4j2)
  (**_import utils.com.anhtester.LogUtils.java_**)

![image](https://user-images.githubusercontent.com/87883620/161657858-d333ac1d-9e7b-4c1b-baac-151a237a1fa0.png)

**7. Record video and Screenshot**

- Setup in **_config.properties_** file
  (**_src/test/resources/config/config.properties_**)
- screenshot_passed_steps=yes or no
- screenshot_failed_steps=yes or no
- screenshot_skipped_steps=yes or no
- screenshot_all_steps=yes or no

  ![image](https://user-images.githubusercontent.com/87883620/161657881-5235139a-9982-43c0-ac37-09f22fff1206.png)

**8. Read data test from Excel file**

- Create function with annotaion **DataProvider** on *
  *src/test/java/anhtester/com/projects/website/crm/dataprovider/DataProviderManager.java**
- Read excel with Map and Hashtable

**9. Base function in the package**

- **_src/main/java/anhtester/com/utils_**
- **_src/main/java/anhtester/com/helpers_**

**10. Read data test from JSON file**

- **JsonUtils** class select the json file path and call **"get"** method with **key**

**11. Main Keyword: WebUI**

- WebUI class is main keyword in Framework. It contains common functions
- How to use: WebUI.function_name
- Example: WebUI.setWindowSize(1024, 768), WebUI.screenshotElement(By by, String elementName),...

**12. Call function to using sample**

- All in one package: src/test/java/anhtester/com/projects/website/crm/testcases

```
+ ClientTest
+ SignInTest
+ TestHandle
+ TestSimpleCode
```

**13. Send message/report to Telegram Bot**

- Setup in src/main/java/anhtester/com/report/TelegramManager.java
- Example: src/test/java/anhtester/com/projects/website/crm/testcases/TestSimpleCode.java
- Call in TestListener at onFinish TelegramManager.sendReportPath()

===How to get Token and start Bot===

- Read blog: https://blog.devgenius.io/automation-of-reporting-2abe7f101801
- Copy Token of your Bot => Paste to TelegramManager class
- Click your Bot => input **/start** to start your Bot

===How to get ChatID===

- After starting your Bot, your use Postman and using your Token:
  Get: https://api.telegram.org/bot{token}/getUpdates => chat.id
- Example: https://api.telegram.org/bot19468772:AAHtlc_BH8zlJAGDHuTJy3J72XumY5LxWcE/getUpdates

```
"chat": {
    "id": 123456789,
    "first_name": "Anh Tester",
    "username": "anhtester",
    "type": "private"
}
```

**14. Use Selenium Grid**

### Download and Install

1. Download Selenium Grid 4: https://www.selenium.dev/downloads/

(tải bản Latest stable version)

**selenium-server-4.9.0.jar** (updated 21/04/2023)

2. Set PATH for driver in Environment variables:

Follow with link:
https://www.selenium.dev/documentation/webdriver/getting_started/install_drivers/#2-the-path-environment-variable

🔆 Đặt file **selenium-server-4.9.0.jar** vào thư mục nào đó và mở CMD tại thư mục đó lên

### Run default 1 node

✅ Mở 1 Hub với 1 Node mặc định (port 4444)

> java -jar selenium-server-4.9.0.jar standalone

### Run multi Node

✅ Mở 1 Hub với 3 Node: (chạy 4 lệnh mở 4 CMD nhé)

> java -jar selenium-server-4.9.0.jar hub

> java -jar selenium-server-4.9.0.jar node --port 5556

> java -jar selenium-server-4.9.0.jar node --port 6667

> java -jar selenium-server-4.9.0.jar node --port 7778


**📝 NOTE: Thực thi nhiều lệnh thì mở nhiều CMD**

### Edit Grid in Config.properties

> TARGET=remote

> REMOTE_URL=192.168.1.13 (url Grid của bạn)

> REMOTE_PORT=4444 (port của Grid)

![image](https://user-images.githubusercontent.com/87883620/200506141-b738104c-b012-4ced-8335-4dadfecd6f66.png)

![image](https://user-images.githubusercontent.com/87883620/200506279-e1496849-70ab-4602-be44-0f44926ab826.png)

![image](https://user-images.githubusercontent.com/87883620/200506218-dbe10f1c-7e65-47e4-a7ac-f29a2b2db43b.png)

**15. Use DataFaker and JavaFaker to generate data**

Document DataFaker: https://www.datafaker.net/documentation/getting-started/

### 🔆 Project structure

```
📦AutomationFrameworkSelenium
 ┣ 📂.github
 ┃ ┗ 📂workflows
 ┃ ┃ ┗ 📜maven.yml
 ┣ 📂src
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂anhtester
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotations
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FrameworkAnnotation.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ConfigFactory.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Configuration.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂constants
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FrameworkConstants.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂driver
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DriverManager.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TargetFactory.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Browser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FailureHandling.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Platform.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Project.java
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
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExcelHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FileHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Helpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PropertiesHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ScreenRecoderHelpers.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂keywords
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebUI.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mail
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailAttachmentsSender.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜EmailConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂report
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
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ObjectUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ZipUtils.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📂META-INF
 ┃ ┃ ┃ ┃ ┗ 📂services
 ┃ ┃ ┃ ┃ ┃ ┗ 📜io.qameta.allure.listener.TestLifecycleListener
 ┃ ┃ ┃ ┗ 📜log4j2.properties
 ┃ ┗ 📂test
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂anhtester
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dataprovider
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DataProviderAddProduct.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DataProviderManager.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂listeners
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AllureListener.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestListener.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂projects
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂cms
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂model
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
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dashboard
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DashboardPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂logins
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂products
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProductInfoPageCMS.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂profiles
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProfilePage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommonPageCMS.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂crm
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂models
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ClientModel.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SignInModel.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Clients
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ClientPageCRM.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Dashboard
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DashboardPageCRM.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Projects
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProjectPageCRM.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂SignIn
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SignInPageCRM.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Tasks
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskPage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommonPageCRM.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂testcases
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ClientTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SignInTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestHandle.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestSimpleCode.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┣ 📜allure.properties
 ┃ ┃ ┃ ┃ ┣ 📜config.json
 ┃ ┃ ┃ ┃ ┣ 📜config.properties
 ┃ ┃ ┃ ┃ ┗ 📜data.properties
 ┃ ┃ ┃ ┣ 📂objects
 ┃ ┃ ┃ ┃ ┗ 📜crm_locators.properties
 ┃ ┃ ┃ ┣ 📂suites
 ┃ ┃ ┃ ┃ ┣ 📜Clients-parallel.xml
 ┃ ┃ ┃ ┃ ┣ 📜Clients-simple.xml
 ┃ ┃ ┃ ┃ ┣ 📜Clients-testAddClient.xml
 ┃ ┃ ┃ ┃ ┣ 📜Clients-testSearch.xml
 ┃ ┃ ┃ ┃ ┣ 📜SignIn-parallel-methods.xml
 ┃ ┃ ┃ ┃ ┣ 📜SignIn-simple.xml
 ┃ ┃ ┃ ┃ ┗ 📜SuiteAll.xml
 ┃ ┃ ┃ ┣ 📂testdataCMS
 ┃ ┃ ┃ ┃ ┣ 📜Book1.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜ChocoPie.jpg
 ┃ ┃ ┃ ┃ ┣ 📜CMS_DATA.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜CocaCola.png
 ┃ ┃ ┃ ┃ ┣ 📜Cosy.png
 ┃ ┃ ┃ ┃ ┣ 📜GetProductInfo.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜Login.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜Nabati.jpg
 ┃ ┃ ┃ ┃ ┗ 📜quatet.jpg
 ┃ ┃ ┃ ┣ 📂testdataCRM
 ┃ ┃ ┃ ┃ ┣ 📜ClientsDataExcel.xlsx
 ┃ ┃ ┃ ┃ ┣ 📜DOCX_File_01.docx
 ┃ ┃ ┃ ┃ ┣ 📜LoginCSV.csv
 ┃ ┃ ┃ ┃ ┗ 📜TxtFileData.txt
 ┃ ┃ ┃ ┗ 📜pdf-config.json
 ┣ 📂target
 ┃ ┣ 📂classes
 ┃ ┃ ┣ 📂anhtester
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┣ 📂annotations
 ┃ ┃ ┃ ┃ ┃ ┗ 📜FrameworkAnnotation.class
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ConfigFactory.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜Configuration.class
 ┃ ┃ ┃ ┃ ┣ 📂constants
 ┃ ┃ ┃ ┃ ┃ ┗ 📜FrameworkConstants.class
 ┃ ┃ ┃ ┃ ┣ 📂driver
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory$1.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory$2.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory$3.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory$4.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserFactory.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DriverManager.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TargetFactory.class
 ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthorType.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Browser.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryType.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜FailureHandling.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Platform.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Project.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜Target.class
 ┃ ┃ ┃ ┃ ┣ 📂exceptions
 ┃ ┃ ┃ ┃ ┃ ┣ 📜FrameworkException.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜HeadlessNotSupportedException.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidPathForExcelException.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidPathForExtentReportFileException.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidPathForFilesException.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidRemoteWebDriverURLException.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TargetNotValidException.class
 ┃ ┃ ┃ ┃ ┣ 📂helpers
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CaptureHelpers.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DatabaseHelpers.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ExcelHelpers.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜FileHelpers.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Helpers.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜PropertiesHelpers.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ScreenRecoderHelpers.class
 ┃ ┃ ┃ ┃ ┣ 📂keywords
 ┃ ┃ ┃ ┃ ┃ ┗ 📜WebUI.class
 ┃ ┃ ┃ ┃ ┣ 📂mail
 ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailAttachmentsSender$1.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailAttachmentsSender.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜EmailConfig.class
 ┃ ┃ ┃ ┃ ┣ 📂report
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AllureManager.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ExtentReportManager.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ExtentTestManager.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TelegramManager.class
 ┃ ┃ ┃ ┃ ┗ 📂utils
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BrowserInfoUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DataFakerUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DataGenerateUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DateUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DecodeUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailSendUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜IconUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JsonUtils$1.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JsonUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LanguageUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LocalStorageUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜LogUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ObjectUtils.class
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ReportUtils.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ZipUtils.class
 ┃ ┃ ┣ 📂META-INF
 ┃ ┃ ┃ ┗ 📂services
 ┃ ┃ ┃ ┃ ┗ 📜io.qameta.allure.listener.TestLifecycleListener
 ┃ ┃ ┗ 📜log4j2.properties
 ┃ ┣ 📂generated-sources
 ┃ ┃ ┗ 📂annotations
 ┃ ┣ 📂generated-test-sources
 ┃ ┃ ┗ 📂test-annotations
 ┃ ┗ 📂test-classes
 ┃ ┃ ┣ 📂anhtester
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTest.class
 ┃ ┃ ┃ ┃ ┣ 📂dataprovider
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DataProviderAddProduct.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜DataProviderManager.class
 ┃ ┃ ┃ ┃ ┣ 📂listeners
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AllureListener.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TestListener.class
 ┃ ┃ ┃ ┃ ┗ 📂projects
 ┃ ┃ ┃ ┃ ┃ ┣ 📂cms
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂brands
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BrandPage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂category
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CategoryPage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂logins
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginPageCMS.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂products
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AddProductPage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂testcases
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddProductTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CategoryTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductInfoTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProfileTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂users
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dashboard
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DashboardPage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂logins
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OrderPage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂products
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProductInfoPageCMS.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂profiles
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProfilePage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommonPageCMS.class
 ┃ ┃ ┃ ┃ ┃ ┗ 📂crm
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂models
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ClientModel.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SignInModel.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂pages
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Clients
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ClientPageCRM.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Dashboard
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DashboardPageCRM.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Projects
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProjectPageCRM.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂SignIn
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SignInPageCRM.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Tasks
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskPage.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommonPageCRM.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂testcases
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ClientTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SignInTest.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestHandle.class
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestSimpleCode.class
 ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┣ 📜allure.properties
 ┃ ┃ ┃ ┣ 📜config.json
 ┃ ┃ ┃ ┣ 📜config.properties
 ┃ ┃ ┃ ┗ 📜data.properties
 ┃ ┃ ┣ 📂objects
 ┃ ┃ ┃ ┗ 📜crm_locators.properties
 ┃ ┃ ┣ 📂suites
 ┃ ┃ ┃ ┣ 📜Clients-parallel.xml
 ┃ ┃ ┃ ┣ 📜Clients-simple.xml
 ┃ ┃ ┃ ┣ 📜Clients-testAddClient.xml
 ┃ ┃ ┃ ┣ 📜Clients-testSearch.xml
 ┃ ┃ ┃ ┣ 📜SignIn-parallel-methods.xml
 ┃ ┃ ┃ ┣ 📜SignIn-simple.xml
 ┃ ┃ ┃ ┗ 📜SuiteAll.xml
 ┃ ┃ ┣ 📂testdataCMS
 ┃ ┃ ┃ ┣ 📜Book1.xlsx
 ┃ ┃ ┃ ┣ 📜ChocoPie.jpg
 ┃ ┃ ┃ ┣ 📜CMS_DATA.xlsx
 ┃ ┃ ┃ ┣ 📜CocaCola.png
 ┃ ┃ ┃ ┣ 📜Cosy.png
 ┃ ┃ ┃ ┣ 📜GetProductInfo.xlsx
 ┃ ┃ ┃ ┣ 📜Login.xlsx
 ┃ ┃ ┃ ┣ 📜Nabati.jpg
 ┃ ┃ ┃ ┗ 📜quatet.jpg
 ┃ ┃ ┣ 📂testdataCRM
 ┃ ┃ ┃ ┣ 📜ClientsDataExcel.xlsx
 ┃ ┃ ┃ ┣ 📜DOCX_File_01.docx
 ┃ ┃ ┃ ┣ 📜LoginCSV.csv
 ┃ ┃ ┃ ┗ 📜TxtFileData.txt
 ┃ ┃ ┗ 📜pdf-config.json
 ┣ 📜.gitignore
 ┣ 📜pom.xml
 ┗ 📜README.md
```

### I shall write document for my Framework. Coming soon...

## Copyright 2022 Anh Tester

> Anh Tester Blog: https://anhtester.com/

![Alt text](https://anhtester.com/uploads/logo/anhtester_logo_512.png?raw=true "Anh Tester - Automation Testing")
