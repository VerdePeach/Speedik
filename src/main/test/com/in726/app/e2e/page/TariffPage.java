package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class TariffPage {

    @FindBy(xpath = "//*[contains(@key, 'mainPage')]")
    private WebElement mainPageBtn;

    @FindBy(xpath = "//*[contains(@id, 'currTariff')]")
    private WebElement currTariffText;

    @FindBy(xpath = "//*[contains(@id, 'tariffSelect')]")
    private WebElement tariffSelectWebElement;

    @FindBy(xpath = "//*[contains(@id, 'tariffBtn')]")
    private WebElement tariffChangeBtn;

    private WebDriver driver;
    public TariffPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void clickMainPageBtn() {
        mainPageBtn.click();
    }

    public String getCurrTariffText() {
        return currTariffText.getText();
    }

    public void setTariffSelect(String val) {
        var tariffSelect = new Select(tariffSelectWebElement);
        tariffSelect.selectByValue(val);
    }

    public void clickTariffChangeBtn() {
        tariffChangeBtn.click();
    }
}
