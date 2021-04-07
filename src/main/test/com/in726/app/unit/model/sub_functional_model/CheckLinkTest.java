package com.in726.app.unit.model.sub_functional_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.enums.LinkStatus;
import com.in726.app.enums.YesNoStatus;
import com.in726.app.model.sub_functional_model.CheckLink;
import com.in726.app.model.sub_functional_model.Link;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;
import java.util.Date;

public class CheckLinkTest {

    @Test
    public void gettingAndSettingPropertiesPositive() {
        var id = 1;
        var httpStatus = 200;
        var allWordsFind = true;
        var status = LinkStatus.DOWN;
        var checkDate = new Date();
        var link = new Link();

        var checkLink = new CheckLink();
        checkLink.setId(id);
        checkLink.setHttpStatus(httpStatus);
        checkLink.setAllWordsFind(allWordsFind);
        checkLink.setStatus(status);
        checkLink.setCheckDate(checkDate);
        checkLink.setLink(link);

        Assert.assertTrue(id == checkLink.getId());
        Assert.assertTrue(httpStatus == checkLink.getHttpStatus());
        Assert.assertTrue(allWordsFind == checkLink.isAllWordsFind());
        Assert.assertTrue(status == checkLink.getStatus());
        Assert.assertTrue(checkDate == checkLink.getCheckDate());
        Assert.assertTrue(link == checkLink.getLink());

    }

    @Test
    public void checkLinkConstructorWithParamsPositive() {
        var httpStatus = 200;
        var allWordsFind = true;
        var status = LinkStatus.DOWN;
        var link = new Link();

        var checkLink = new CheckLink(httpStatus, allWordsFind, status, link);
        Assert.assertTrue(httpStatus == checkLink.getHttpStatus());
        Assert.assertTrue(allWordsFind == checkLink.isAllWordsFind());
        Assert.assertTrue(status == checkLink.getStatus());
        Assert.assertTrue(link == checkLink.getLink());
    }
}
