package com.in726.app.unit.model.sub_functional_model;

import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Letter;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

public class LetterTest {
    @Test
    public void getTtingAndSettingPropertiesPositive() {

        var id = 1;
        var subject = " Hi bro";
        var body = "Hello world";
        var user = new User();

        var letter = new Letter();
        letter.setId(id);
        letter.setSubject(subject);
        letter.setBody(body);
        letter.setUser(user);

        Assert.assertTrue(id == letter.getId());
        Assert.assertEquals(subject, letter.getSubject());
        Assert.assertEquals(body, letter.getBody());
        Assert.assertEquals(user, letter.getUser());
    }
}
