package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ChangePasswordPage {

    @FindBy(xpath = "//*[contains(@key, 'mainPage')]")
    private WebElement mainPageBtn;

    @FindBy(xpath = "//*[contains(@id, 'oldPassword')]")
    private WebElement oldPasswordInput;

    @FindBy(xpath = "//*[contains(@id, 'newPassword')]")
    private WebElement newPasswordInput;

    @FindBy(xpath = "//*[contains(@id, 'confpass')]")
    private WebElement confpassInput;

    @FindBy(id = "changePassword")
    private WebElement changePasswordBtn;

    @FindBy(xpath = "//h2")
    private WebElement pageTitle;

    private WebDriver driver;

    public ChangePasswordPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void setOldPasswordInput(String oldPass) {
        oldPasswordInput.clear();
        oldPasswordInput.sendKeys(oldPass);
    }

    public void setNewPasswordInput(String newPass) {
        newPasswordInput.clear();
        newPasswordInput.sendKeys(newPass);
    }

    public void setConfpassInput(String confPass) {
        confpassInput.clear();
        confpassInput.sendKeys(confPass);
    }

    public void clickMainPageBtn() {
        mainPageBtn.click();
    }

    public void changePasswordBtn() {
        changePasswordBtn.click();
    }

    public String getTextPageTitle() {
        return pageTitle.getText();
    }
    public String getTextMainPageBtn() {
        return mainPageBtn.getText();
    }
    public String getTextOldPasswordInput() {
        return oldPasswordInput.getAttribute("placeholder");
    }
    public String getTextNewPasswordInput() {
        return newPasswordInput.getAttribute("placeholder");
    }
    public String getTextConfPassInput() {
        return confpassInput.getAttribute("placeholder");
    }
    public String getTextChangePasswordBtn() {
        return changePasswordBtn.getText();
    }

}
