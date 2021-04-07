package com.in726.app.unit.model.sub_functional_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.model.sub_functional_model.Word;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

public class WordTest {
    @Test
    public void gettingAndSettingPropertiesPositive() {

        var id = 1;
        var value = "World";
        var link = new Link();

        var word = new Word();
        word.setId(id);
        word.setValue(value);
        word.setLink(link);

        Assert.assertTrue(id == word.getId());
        Assert.assertEquals(value, word.getValue());
        Assert.assertEquals(link, word.getLink());
    }
}
