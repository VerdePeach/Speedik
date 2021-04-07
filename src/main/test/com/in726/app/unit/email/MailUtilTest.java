package com.in726.app.unit.email;

import com.in726.app.email.MailUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MailUtilTest {

    @Test
    public void prepareMessagePositive() throws MessagingException, IOException {

        var expectedMessage = new MimeMessage(Session.getInstance(new Properties()));
        expectedMessage.setFrom(new InternetAddress("world@gmail.com"));
        expectedMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("other.world@gmail.com"));
        expectedMessage.setSubject("Good Luck");
        expectedMessage.setText("Huston");

        var actualMessage = MailUtil.prepareMessage(Session.getInstance(new Properties()), "world@gmail.com",
                "other.world@gmail.com", "Huston", "Good Luck");

        Assert.assertArrayEquals(expectedMessage.getAllRecipients(), actualMessage.getAllRecipients());
        Assert.assertEquals(expectedMessage.getContent(), actualMessage.getContent());
        Assert.assertArrayEquals(expectedMessage.getFrom(), actualMessage.getFrom());
        Assert.assertEquals(expectedMessage.getSubject(), actualMessage.getSubject());
    }

    @Test
    public void sendMailTestPositive() throws MessagingException {
        MailUtil.sendMail("speedik.inc@gmail.com",
                "I have run your wonderful tests " + new Date(), "Testing Speedik");
    }

    @Test
    public void setUpEmailAccountAccessPositive() {
        MailUtil.setUpEmailAccountAccess("src/main/test_resources/speedikEmailTestData.txt");
    }

    @Test()
    public void setUpEmailAccountAccessNegative() {
        MailUtil.setUpEmailAccountAccess("world");
    }

    @Test
    public void createEmailConfirmHash() {
        Assert.assertEquals(MailUtil.createEmailConfirmHash("world"), "b4a790291f1327a91b473dcc52f17b10633f652a7cb9278b4a6064cdae83d2e82877cdab227b823210eb4b5cf2c99680876b77d3dce4116a372f62672dc75f8d");
    }
}
