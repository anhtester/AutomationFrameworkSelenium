/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package anhtester.com.mail;

import static anhtester.com.constants.FrameworkConstants.REPORT_TITLE;

/**
 * Data for Sending email after execution
 */
public class EmailConfig {

    //Nhớ bật chế độ Gmail dùng cho ứng dụng kém an toàn mới gửi được nhen
    public static final String SERVER = "smtp.gmail.com";
    public static final String PORT = "587";

    public static final String FROM = "******@gmail.com";
    public static final String PASSWORD = "******";

    public static final String[] TO = {"thaian.it15@gmail.com"};
    public static final String SUBJECT = REPORT_TITLE;
}