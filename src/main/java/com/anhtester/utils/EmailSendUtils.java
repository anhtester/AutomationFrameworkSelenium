/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.utils;

import com.anhtester.constants.FrameworkConstants;
import com.anhtester.mail.EmailAttachmentsSender;

import jakarta.mail.MessagingException;

import static com.anhtester.constants.FrameworkConstants.REPORT_TITLE;
import static com.anhtester.mail.EmailConfig.*;

public class EmailSendUtils {

    private EmailSendUtils() {
        super();
    }

    public static void sendEmail(int count_totalTCs, int count_passedTCs, int count_failedTCs, int count_skippedTCs) {

        if (FrameworkConstants.SEND_EMAIL_TO_USERS.trim().equalsIgnoreCase(FrameworkConstants.YES)) {
            LogUtils.info("****************************************");
            LogUtils.info("Send Email - START");
            LogUtils.info("****************************************");

            LogUtils.info("File name: " + FrameworkConstants.getExtentReportFilePath());

            String messageBody = getTestCasesCountInFormat(count_totalTCs, count_passedTCs, count_failedTCs,
                    count_skippedTCs);
            //LogUtils.info(messageBody);

            String attachmentFile_ExtentReport = FrameworkConstants.getExtentReportFilePath();

            try {
                EmailAttachmentsSender.sendEmailWithAttachments(SERVER, PORT, FROM, PASSWORD, TO, SUBJECT, messageBody,
                        attachmentFile_ExtentReport);

                LogUtils.info("****************************************");
                LogUtils.info("Email sent successfully.");
                LogUtils.info("Send Email - END");
                LogUtils.info("****************************************");
            } catch (MessagingException e) {
                LogUtils.error("Can not send email from " + FROM + " via " + SERVER + ":" + PORT + " (check EMAIL_FROM / EMAIL_PASSWORD)", e);
            }

        }

    }

    private static String getTestCasesCountInFormat(int count_totalTCs, int count_passedTCs, int count_failedTCs,
                                                    int count_skippedTCs) {
        LogUtils.info("count_totalTCs: " + count_totalTCs);
        LogUtils.info("count_passedTCs: " + count_passedTCs);
        LogUtils.info("count_failedTCs: " + count_failedTCs);
        LogUtils.info("count_skippedTCs: " + count_skippedTCs);

        return "<html>\r\n" + "\r\n" + " \r\n" + "\r\n"
                + "        <body> \r\n<table class=\"container\" align=\"center\" style=\"padding-top:20px\">\r\n<tr align=\"center\"><td colspan=\"4\"><h2>"
                + REPORT_TITLE + "</h2></td></tr>\r\n<tr><td>\r\n\r\n"
                + "       <table style=\"background:#67c2ef;width:120px\" >\r\n"
                + "                     <tr><td style=\"font-size: 36px\" class=\"value\" align=\"center\">"
                + count_totalTCs + "</td></tr>\r\n"
                + "                     <tr><td align=\"center\">Total</td></tr>\r\n" + "       \r\n"
                + "                </table>\r\n" + "                </td>\r\n" + "                <td>\r\n"
                + "               \r\n" + "                 <table style=\"background:#79c447;width:120px\">\r\n"
                + "                     <tr><td style=\"font-size: 36px\" class=\"value\" align=\"center\">"
                + count_passedTCs + "</td></tr>\r\n"
                + "                     <tr><td align=\"center\">Passed</td></tr>\r\n" + "       \r\n"
                + "                </table>\r\n" + "                </td>\r\n" + "                <td>\r\n"
                + "                <table style=\"background:#ff5454;width:120px\">\r\n"
                + "                     <tr><td style=\"font-size: 36px\" class=\"value\" align=\"center\">"
                + count_failedTCs + "</td></tr>\r\n"
                + "                     <tr><td align=\"center\">Failed</td></tr>\r\n" + "       \r\n"
                + "                </table>\r\n" + "                \r\n" + "                </td>\r\n"
                + "                <td>\r\n" + "                <table style=\"background:#fabb3d;width:120px\">\r\n"
                + "                     <tr><td style=\"font-size: 36px\" class=\"value\" align=\"center\">"
                + count_skippedTCs + "</td></tr>\r\n"
                + "                     <tr><td align=\"center\">Skipped</td></tr>\r\n" + "       \r\n"
                + "                </table>\r\n" + "                \r\n" + "                </td>\r\n"
                + "                </tr>\r\n" + "               \r\n" + "                \r\n"
                + "            </table>\r\n" + "       \r\n" + "    </body>\r\n" + "</html>";
    }

}
