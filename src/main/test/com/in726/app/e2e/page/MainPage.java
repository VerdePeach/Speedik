package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class MainPage {

    @FindBy(id  = "logout")
    private WebElement logoutBtn;

    @FindBy(xpath = "//*[contains(@key, 'newServer')]")
    private WebElement newServerBtn;

    @FindBy(xpath = "//*[contains(@key, 'urlMonitor')]")
    private WebElement newUrlBtn;

    @FindBy(xpath = "//*[contains(@id, 'premium')]")
    private WebElement tariffPageBtn;

    @FindBy(xpath = "//*[contains(@id, 'adminPage')]")
    private WebElement adminPageBtn;

    @FindBy(xpath = "//*[contains(@key, 'changePassword')]")
    private WebElement changePasswordBtn;

    @FindBy(id = "en")
    private WebElement englishLanguageBtn;

    @FindBy(id = "ua")
    private WebElement ukraineLanguageBtn;

    @FindBy(xpath = "//*[contains(@id, 'username')]")
    private WebElement usernameText;

    @FindBy(xpath = "//*[contains(@id, 'currTariff')]")
    private WebElement userTariffText;

    @FindBy(xpath = "//*[contains(@id, 'serverName')]")
    private WebElement serverNameWebElementSelect;

    @FindBy(xpath = "//*[contains(@id, 'cpuChart')]")
    private WebElement cpuChart;

    @FindBy(xpath = "//*[contains(@id, 'diskChart')]")
    private WebElement diskChart;

    @FindBy(xpath = "//*[contains(@id, 'memoryChart')]")
    private WebElement memoryChart;

    @FindBy(xpath = "//*[contains(@key, 'privatPolicy')]")
    private WebElement privatPolicyLink;

    @FindBy(xpath = "//*[contains(@key, 'forDevelopers')]")
    private WebElement forDevelopersLink;

    @FindBy(xpath = "//*[contains(@href, 'mailto:t6@tss2020.repositoryhosting.com')]")
    private WebElement speedikEmail;

    private WebDriver driver;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void clickLogoutBtn() {
        logoutBtn.click();
    }

    public String getUsernameText() {
        return usernameText.getText();
    }

    public String getUserTariffText() {
        return userTariffText.getText();
    }

    public void setServerNameSelect(String serverName) {
        var serverSelect = new Select(serverNameWebElementSelect);
        serverSelect.selectByValue(serverName);
    }

    public boolean isDisplayedCpuChart() {
        return cpuChart.isDisplayed();
    }

    public boolean isDisplayedDiskChart() {
        return diskChart.isDisplayed();
    }

    public boolean isDisplayedMemoryChart() {
        return memoryChart.isDisplayed();
    }

    public void clickNewServerBtn() {
        newServerBtn.click();
    }

    public boolean isDisplayedPrivatPolicyLink() {
        return privatPolicyLink.isDisplayed();
    }

    public boolean isDisplayedForDevelopersLink() {
        return forDevelopersLink.isDisplayed();
    }

    public boolean isDisplayedSpeedikEmail() {
        return speedikEmail.isDisplayed();
    }

    public void clickNewUrlBtn() {
        newUrlBtn.click();
    }

    public void clickAdminPageBtn() {
        adminPageBtn.click();
    }

    public void clickTariffPageBtn() {
        tariffPageBtn.click();
    }

    public void clickChangePasswordBtn() {
        changePasswordBtn.click();
    }

    public void clickEnglishLanguageBtn() {
        englishLanguageBtn.click();
    }
    public void clickUkraineLanguageBtn() {
        ukraineLanguageBtn.click();
    }



    public String getTextOfLogoutBtn() {
        return logoutBtn.getText();
    }

    public String getTextOfNewServerBtn() {
        return newServerBtn.getText();
    }

    public String getTextOfNewUrlBtn() {
        return newUrlBtn.getText();
    }

    public String getTextOfTariffPageBtn() {
        return tariffPageBtn.getText();
    }

    public String getTextOfAdminPageBtn() {
        return adminPageBtn.getText();
    }

    public String getTextOfChangePasswordBtn() {
        return changePasswordBtn.getText();
    }

    public String getTextOfPrivatPolicyLink() {
        return privatPolicyLink.getText();
    }

    public String getTextOfForDevelopersLink() {
        return forDevelopersLink.getText();
    }
}
