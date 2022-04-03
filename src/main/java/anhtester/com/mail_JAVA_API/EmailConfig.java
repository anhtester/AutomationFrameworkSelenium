package anhtester.com.mail_JAVA_API;

import static anhtester.com.constants.FrameworkConstants.PROJECT_NAME;

/**
 * Data for Sending email after execution
 */
public class EmailConfig {

    public static final String SERVER = "smtp.gmail.com";
    public static final String PORT = "587";

    public static final String FROM = "anhtestersharing@gmail.com";
    public static final String PASSWORD = "AnhTester@2022";

    public static final String[] TO = {"thaian.it15@gmail.com"};
    public static final String SUBJECT = PROJECT_NAME;
}