package com.in726.app.unit.model.sub_functional_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.CheckLink;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.model.sub_functional_model.Word;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LinkTest {
    @Test
    public void gettingAndSettingProperties() {

        var id = 1;
        var name = "World";
        var url = "http://world.com/hi";
        var secondsToCheck = 300;
        var checks = new ArrayList<CheckLink>();
        var words = new ArrayList<Word>();
        var created = new Date();
        var updated = new Date();
        var user = new User();

        var link = new Link();
        link.setId(id);
        link.setName(name);
        link.setUrl(url);
        link.setSecondsToCheck(secondsToCheck);
        link.setChecks(checks);
        link.setWords(words);
        link.setCreated(created);
        link.setUpdated(updated);
        link.setUser(user);

        Assert.assertTrue(id == link.getId());
        Assert.assertTrue(secondsToCheck == link.getSecondsToCheck());
        Assert.assertEquals(name, link.getName());
        Assert.assertEquals(url, link.getUrl());
        Assert.assertEquals(checks, link.getChecks());
        Assert.assertEquals(words, link.getWords());
        Assert.assertEquals(created, link.getCreated());
        Assert.assertEquals(updated, link.getUpdated());
        Assert.assertEquals(user, link.getUser());

    }
}
