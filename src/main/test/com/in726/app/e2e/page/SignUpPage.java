package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {

    @FindBy(xpath = "//*[contains(@id, 'username')]")
    private WebElement usernameInput;

    @FindBy(xpath = "//*[contains(@id, 'email')]")
    private WebElement emailInput;

    @FindBy(xpath = "//*[contains(@id, 'password')]")
    private WebElement passwordInput;

    @FindBy(xpath = "//*[contains(@id, 'confpass')]")
    private WebElement confPassInput;

    @FindBy(xpath = "//*[contains(@id, 'singup')]")
    private WebElement signUpBtn;

    private WebDriver driver;
    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void setUsernameInput(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void setEmailInput(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void setPasswordInput(String pass) {
        passwordInput.clear();
        passwordInput.sendKeys(pass);
    }

    public void setConfPassInput(String confPass) {
        confPassInput.clear();
        confPassInput.sendKeys(confPass);
    }

    public void clickSignUpBtn() {
        signUpBtn.click();
    }
}
