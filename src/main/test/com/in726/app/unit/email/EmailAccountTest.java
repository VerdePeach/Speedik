package com.in726.app.unit.email;

import com.in726.app.email.EmailAccount;
import org.junit.Assert;
import org.junit.Test;

public class EmailAccountTest {

    @Test
    public void gettingAndSettingPropertiesPositive() {
        var email = "world@gmail.com";
        var password = "helloWorld";

        var emailAccount = new EmailAccount();
        emailAccount.setEmail(email);
        emailAccount.setPassword(password);

        Assert.assertEquals(email, emailAccount.getEmail());
        Assert.assertEquals(password, emailAccount.getPassword());
    }
}
