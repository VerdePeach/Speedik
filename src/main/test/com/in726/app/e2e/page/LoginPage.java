package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(xpath = "//*[contains(@id, 'username')]")
    private WebElement usernameInput;

    @FindBy(xpath = "//*[contains(@id, 'password')]")
    private WebElement passwordInput;

    @FindBy(xpath = "//*[contains(@id, 'login')]")
    private WebElement loginBtn;

    @FindBy(xpath = ".//html/body/div[2]/form/h2")
    private WebElement loginHeaderText;

    @FindBy(xpath = "//*[contains(@class, 'loginSing')]")
    private WebElement singUpBtn;

    private WebDriver driver;
    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void setUsernameInput(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void setPasswordInput(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLoginBtn() {
        loginBtn.click();
    }

    public String getLoginHeader() {
       return loginHeaderText.getText();
    }

    public void clickSingUpBtn() {
        singUpBtn.click();
    }
}
