package com.in726.app.unit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.enums.Roles;
import com.in726.app.enums.TariffPlan;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.model.Agent;
import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Letter;
import com.in726.app.model.sub_functional_model.Link;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserTest {
    @Test
    public void gettingAndSettingProperties() {

        var id = 1;
        var username = "World";
        var password = "WorldPassword123";
        var email = "WorldEmail";
        var role = Roles.USER;
        var confirm = YesNoStatus.YES;
        var tariff = TariffPlan.FREE;
        var agents = new ArrayList<Agent>();
        var links = new ArrayList<Link>();
        var letters = new ArrayList<Letter>();
        var lastActive = new Date();

        var user = new User();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);
        user.setConfirm(confirm);
        user.setConfirm(confirm);
        user.setTariff(tariff);
        user.setAgents(agents);
        user.setLinks(links);
        user.setLetters(letters);
        user.setLastActive(lastActive);

        Assert.assertTrue(id == user.getId());
        Assert.assertEquals(username, user.getUsername());
        Assert.assertEquals(password, user.getPassword());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals(role, user.getRole());
        Assert.assertEquals(confirm, user.getConfirm());
        Assert.assertEquals(tariff, user.getTariff());
        Assert.assertEquals(agents, user.getAgents());
        Assert.assertEquals(links, user.getLinks());
        Assert.assertEquals(letters, user.getLetters());
        Assert.assertEquals(lastActive, user.getLastActive());
    }
}
