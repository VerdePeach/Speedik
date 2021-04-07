package com.in726.app.unit.email;

import com.in726.app.email.EmailDataReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class EmailDataReaderTest {
    @Test
    public void readAccessDataPositive() throws IOException {
        var email = "world@gmail.com";
        var password = "world";
        var emailAccount = EmailDataReader.readAccessData("src/main/test_resources/speedikEmailTestData.txt");

        Assert.assertEquals(email, emailAccount.getEmail());
        Assert.assertEquals(password, emailAccount.getPassword());
    }
}
