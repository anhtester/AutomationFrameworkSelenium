/*
 * Copyright (c) 2022.
 * Automation Framework Selenium - Anh Tester
 */

package com.anhtester.mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * https://www.codejava.net/java-ee/javamail/send-e-mail-with-attachment-in-java
 */
public class EmailAttachmentsSender {

    public static void sendEmailWithAttachments(String host, String port, final String userName, final String password,
                                                String[] toAddress, String subject, String message, String... attachFiles)
            throws AddressException, MessagingException {
        // sets SMTP server properties

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));

        InternetAddress[] addressTo = new InternetAddress[toAddress.length];
        for (int i = 0; i < toAddress.length; i++)
            addressTo[i] = new InternetAddress(toAddress[i]);
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        /*
         * InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
         * msg.setRecipients(Message.RecipientType.TO, toAddresses);
         */
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }

        // sets the multi-part as e-mail's content
        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg);
    }

}
