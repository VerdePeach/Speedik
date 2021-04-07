package com.in726.app.unit.security;

import com.in726.app.enums.Roles;
import com.in726.app.enums.TariffPlan;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.model.sub_functional_model.Word;
import com.in726.app.security.UserDataValidator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UserDataValidatorTest {
    @Test
    public void isUsernameTestPositive() {
        Assert.assertTrue(UserDataValidator.isUsername("World"));
    }

    @Test
    public void isUsernameTestNegative() {
        Assert.assertTrue(!UserDataValidator.isUsername("World&"));
    }

    @Test
    public void isEmailPositive() {
        Assert.assertTrue(UserDataValidator.isEmail("world@gmail.com"));
    }

    @Test
    public void isEmailNegative() {
        Assert.assertTrue(!UserDataValidator.isEmail("worldGmail.com"));
    }

    @Test
    public void isLinkUserTariffFreePositive() {
        var user = new User();
        user.setConfirm(YesNoStatus.YES);
        user.setTariff(TariffPlan.FREE);
        user.setRole(Roles.USER);

        var link = new Link();
        link.setUser(user);

        var word1 = new Word();
        word1.setValue("world1");
        var word2 = new Word();
        word2.setValue("world2");
        link.setWords(Arrays.asList(word1, word2));

        link.setSecondsToCheck(300);
        link.setUrl("http://t6.tss2020.site");
        link.setName("my world link");

        Assert.assertTrue(UserDataValidator.isLink(link, user));
    }

    @Test
    public void isLinkUserTariffPremiumPositive() {
        var user = new User();
        user.setConfirm(YesNoStatus.YES);
        user.setTariff(TariffPlan.PREMIUM);
        user.setRole(Roles.USER);

        var link = new Link();
        link.setUser(user);

        var word1 = new Word();
        word1.setValue("world1");
        var word2 = new Word();
        word2.setValue("world2");
        var word3 = new Word();
        word3.setValue("world3");
        var word4 = new Word();
        word4.setValue("world4");
        var word5 = new Word();
        word5.setValue("world5");
        var word6 = new Word();
        word6.setValue("world6");
        link.setWords(Arrays.asList(word1, word2, word3, word4, word5, word6));

        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");
        link.setName("my world link");

        Assert.assertTrue(UserDataValidator.isLink(link, user));
    }

    @Test
    public void isLinkUserTariffAdminPositive() {
        var user = new User();
        user.setConfirm(YesNoStatus.YES);
        user.setTariff(TariffPlan.ADMIN);
        user.setRole(Roles.ADMIN);

        var link = new Link();
        link.setUser(user);

        var word1 = new Word();
        word1.setValue("world1");
        var word2 = new Word();
        word2.setValue("world2");
        link.setWords(Arrays.asList(word1, word2));

        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");
        link.setName("my world link");

        Assert.assertTrue(UserDataValidator.isLink(link, user));
    }

    @Test
    public void isLinkWrongWordsPositive() {
        var user = new User();
        user.setConfirm(YesNoStatus.YES);
        user.setTariff(TariffPlan.ADMIN);
        user.setRole(Roles.ADMIN);

        var link = new Link();
        link.setUser(user);

        var word1 = new Word();
        word1.setValue("wor@#$%^&*(ld1");
        var word2 = new Word();
        word2.setValue("world2");
        link.setWords(Arrays.asList(word1, word2));

        link.setSecondsToCheck(60);
        link.setUrl("http://t6.tss2020.site");
        link.setName("my world link");

        Assert.assertTrue(!UserDataValidator.isLink(link, user));
    }

    @Test
    public void isLinkNullNegative() {
        Assert.assertTrue(!UserDataValidator.isLink(null, null));
    }

    @Test
    public void isAgentWithKeysPositive() {
        Assert.assertTrue(UserDataValidator.isAgentWithKeys("publicWorld", "secretWorld"));
    }

    @Test
    public void isAgentWithKeysNegative() {
        Assert.assertTrue(!UserDataValidator.isAgentWithKeys("publi##$&cWorld", "secretWorld"));
    }

}
