package com.in726.app.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class is responsible for creating and sending emails to users;
 */
public class MailUtil {

    private static final String pathToFileWithSignature = "emailConfirmSignature.txt";
    private static final String pathToEmailCredentials = "speedikEmail.txt";
    private static final String algorithmToHashConfirmationCode = "SHA-512";
    private static String emailSignature;
    private static String speedikEmail;
    private static String speedikEmailPassword;

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    static {
        try (Stream<String> stream = Files.lines(Paths.get(pathToFileWithSignature))) {
            emailSignature = stream.limit(1).collect(Collectors.toList()).get(0);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Method sends email.
     *
     * @param recipient   recipient email.
     * @param textMessage message of letter.
     * @param subject     topic of a letter.
     * @throws MessagingException message sending exception.
     */
    public static void sendMail(String recipient, String textMessage, String subject) throws MessagingException {
        setUpEmailAccountAccess(pathToEmailCredentials);
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(speedikEmail, speedikEmailPassword);
            }
        });
        Message message = prepareMessage(session, speedikEmail, recipient, textMessage, subject);

        Transport.send(message);
        logger.info("Message sent by email.");
    }

    /**
     * Method of preparing letter.
     *
     * @param session      Mail session.
     * @param speedikEmail speedik email.
     * @param recipient    recipient email.
     * @param textMessage  message.
     * @param subject      topic of a mail.
     * @return prepared message.
     */
    public static Message prepareMessage(Session session, String speedikEmail, String recipient, String textMessage, String subject) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(speedikEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(textMessage);
        return message;
    }

    /**
     * Method provides data for mailbox access.
     *
     * @param path path to credentials file
     */
    public static void setUpEmailAccountAccess(String path) {
        try {
            var emailAccount = EmailDataReader.readAccessData(path);
            speedikEmail = emailAccount.getEmail();
            speedikEmailPassword = emailAccount.getPassword();
        } catch (IOException e) {
            logger.error("Failed to load speedik email data. " + e.getMessage());
        }
    }

    /**
     * Method creates hash for confirming account and email.
     *
     * @param userString user email
     * @return hash code
     */
    public static String createEmailConfirmHash(String userString) {
        return HashCodeGenerator.createHashCode(userString + emailSignature, algorithmToHashConfirmationCode);
    }
}
