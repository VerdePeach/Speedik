package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewServerPage {

    @FindBy(xpath = "//*[contains(@key, 'publicKey')]")
    private WebElement publicKeyInput;

    @FindBy(xpath = "//*[contains(@id, 'secretKey')]")
    private WebElement secretKeyInput;

    @FindBy(xpath = "//*[contains(@id, 'serverCreate')]")
    private WebElement serverCreateBtn;

    @FindBy(xpath = "//*[contains(@key, 'mainPage')]")
    private WebElement mainPageBtn;

    @FindBy(xpath = "//h2")
    private WebElement pageTitle;

    private WebDriver driver;

    public NewServerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void setPublicKeyInput(String publicKey) {
        publicKeyInput.clear();
        publicKeyInput.sendKeys(publicKey);
    }

    public void setSecretKeyInput(String secretKey) {
        secretKeyInput.clear();
        secretKeyInput.sendKeys(secretKey);
    }

    public void clickServerCreateBtn() {
        serverCreateBtn.click();
    }

    public void clickMainPageBtn() {
        mainPageBtn.click();
    }

    public String getTextMainPageBtn() {
        return mainPageBtn.getText();
    }

    public String getTextPageTitle() {
        return pageTitle.getText();
    }

    public String getTextServerCreateBtn() {
        return serverCreateBtn.getText();
    }

    public String getTextSecretKeyInput() {
        return secretKeyInput.getAttribute("placeholder");
    }

    public String getTextPublicKeyInput() {
        return publicKeyInput.getAttribute("placeholder");
    }
}
