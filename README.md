# Automation Framework Selenium Java with TestNG building by Anh Tester (VERSION 1.0)

**MỘT SỐ TÍNH NĂNG CHÍNH THAM KHẢO TẠI VERSION 1.0**
1. Run the parallel test case
2. Read Object web element from a properties file
3. Extent Report
4. Allure Report
5. Send Mail after the run test (Report information and HTML file attachment)
6. Logfile
7. Record video and Screenshot test case for failed
8. Read data test form Excel file (xlsx, csv, json,...) with class define fields (param: Client client)
9. Base function in the library (WebUI, Utils,...)
   ...etc.. (wait, dynamic xpath,...)
   
   
## **YÊU CẦU HỆ THỐNG**
- **JDK 11, 15, 16** (not run with 8, 17, 18)
- Chrome Browser, Edge Browser
- Setup **Allure**:
   https://mvnrepository.com/artifact/io.qameta.allure/allure-java-commons
   or
   https://anhtester.com/blog/selenium-java/selenium-java-tai-nguyen-cai-dat-moi-truong) 
   Download jar and setting Variable Environment as Java JDK
   
   ![image](https://user-images.githubusercontent.com/87883620/161661705-b8706957-5a26-4faf-8ddf-2f9aef78418e.png)

- **IntelliJ** is the best choice (to change JDK version)

![image](https://user-images.githubusercontent.com/87883620/161707184-7ad558f2-0d7d-4851-bfd6-2796d4e46593.png)



## **HƯỚNG DẪN SỬ DỤNG CƠ BẢN**


**1. Run parallel the test case**
- Run test case in suite XML (**src/test/resources/suites/**)
- Run test case from Maven pom.xml file
  (**mvn clean test**)
  
  ![image](https://user-images.githubusercontent.com/87883620/161658761-5040e527-b410-46b3-8697-3298523e201d.png)



**2. Read Object web element from a properties file**

![image](https://user-images.githubusercontent.com/87883620/161658681-58f3a183-cbfd-433d-853a-927e4373f51c.png)



**3. Extent Report**
- Insert Framework Annotation as sample:

![image](https://user-images.githubusercontent.com/87883620/161657646-3dd652c6-a310-4006-a0cb-de63e2e87e36.png)

- Value from Enums (src/main/java/anhtester/com/enums)
- Setup on TestListener and BaseTest

![image](https://user-images.githubusercontent.com/87883620/161657754-c29b1ee9-f2fb-44b3-bee0-9f425a7cab6f.png)



**4. Allure Report**

- Open Terminal: ***allure serve target/allure-results***

![image](https://user-images.githubusercontent.com/87883620/161662507-9e4dc698-e452-4b43-a4f5-9808c81419a2.png)


- Insert **@Step("title/message")** above ***@Test*** or any ***Method*** in the project
- (As sample picture above step 3)

![image](https://user-images.githubusercontent.com/87883620/161657680-af29973d-4e52-451f-b1d6-40b12d182845.png)

![image](https://user-images.githubusercontent.com/87883620/161657689-10365747-ed8f-4ca8-9d84-8060514f216b.png)



**5. Send Mail after the run test**

- Config **true/false** in config.properties
  (***src/test/resources/config/config.properties***)
- send_email_to_users=**true** is enable send mail
- Config mail with email and password in ***src/main/java/anhtester/com/mail/EmailConfig.java***
Note: enabled Allow less secure apps

![image](https://user-images.githubusercontent.com/87883620/161658851-2aa41091-ac99-45d9-a79f-aaa828052efb.png)

![image](https://user-images.githubusercontent.com/87883620/161659238-88337f69-b742-4cd7-87f2-76670519c8dd.png)

![image](https://user-images.githubusercontent.com/87883620/161710836-8f3eee7c-19ba-410d-8498-5f314e6289a5.png)



**6. Log file**

- Call: Log.info , Log.pass, Log.error,...
 (***import anhtester.com.utils.Log.java***)
 
 ![image](https://user-images.githubusercontent.com/87883620/161657858-d333ac1d-9e7b-4c1b-baac-151a237a1fa0.png)



**7. Record video and Screenshot**

- Setup in config.properties file
(***src/test/resources/config/config.properties***)
  screenshot_passed_steps=yes or no
  screenshot_failed_steps=yes or no
  screenshot_skipped_steps=yes or no
  
  ![image](https://user-images.githubusercontent.com/87883620/161657881-5235139a-9982-43c0-ac37-09f22fff1206.png)



**8. Read data test form Excel file**

- Create **DataSupplier** on ***src/main/java/anhtester/com/utils/DataProviderUtils.java***
- Call the name of **DataSupplier** in test cases **@Test

**9. Base function in library**

- **WebUI.** call any method you need =))
- And ..etc.. on ***src/main/java/anhtester/com***
(It is static method)


# Copyright 2022 Anh Tester

> Anh Tester Blog: https://anhtester.com/

![Alt text](https://anhtester.com/uploads/logo/anhtester_logo_512.png?raw=true "Anh Tester - Automation Testing")
