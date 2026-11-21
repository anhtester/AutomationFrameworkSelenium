/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package com.anhtester.mail;

import com.anhtester.helpers.PropertiesHelpers;

import java.util.Arrays;

import static com.anhtester.constants.FrameworkConstants.REPORT_TITLE;

/**
 * Data for Sending email after execution.
 * <p>
 * Never commit a real password here. Every value can be set with a JVM property or an environment variable
 * (CI secrets), for example: mvn test -DSEND_EMAIL_TO_USERS=yes -DEMAIL_FROM=me@gmail.com -DEMAIL_PASSWORD=app-password
 * <ul>
 *     <li>EMAIL_SERVER (default smtp.gmail.com), EMAIL_PORT (default 587)</li>
 *     <li>EMAIL_FROM, EMAIL_PASSWORD (Gmail: create an App Password)</li>
 *     <li>EMAIL_TO: list of receivers separated by comma</li>
 * </ul>
 * Enable sending in src/test/resources/config/config.properties: SEND_EMAIL_TO_USERS=yes
 */
public class EmailConfig {

    public static final String SERVER = PropertiesHelpers.getValue("EMAIL_SERVER", "smtp.gmail.com");
    public static final String PORT = PropertiesHelpers.getValue("EMAIL_PORT", "587");

    public static final String FROM = PropertiesHelpers.getValue("EMAIL_FROM", "anhtester@email.com");
    public static final String PASSWORD = PropertiesHelpers.getValue("EMAIL_PASSWORD", "");

    public static final String[] TO = Arrays.stream(PropertiesHelpers.getValue("EMAIL_TO", "anhtester@mailinator.com").split(","))
            .map(String::trim).filter(value -> !value.isEmpty()).toArray(String[]::new);
    public static final String SUBJECT = REPORT_TITLE;
}
