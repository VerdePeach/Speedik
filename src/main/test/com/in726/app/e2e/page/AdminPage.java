package com.in726.app.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdminPage {

    @FindBy(xpath = "//*[contains(@id, 'activeUser')]")
    private WebElement activeUserText;

    @FindBy(xpath = "//*[contains(@id, 'unactiveUser')]")
    private WebElement inactiveUserText;

    @FindBy(xpath = "//*[contains(@id, 'activeAgent')]")
    private WebElement activeAgentText;

    @FindBy(xpath = "//*[contains(@id, 'unactiveAgent')]")
    private WebElement inactiveAgentText;

    @FindBy(xpath = "//*[contains(@id, 'lettersCount')]")
    private WebElement lettersCountText;

    @FindBy(xpath = "//*[contains(@id, 'checksCounter')]")
    private WebElement checksCounterText;

    @FindBy(xpath = "//*[contains(@key, 'back')]")
    private WebElement mainPageBtn;



    @FindBy(xpath = "//h1")
    private WebElement adminPageTitle;

    @FindBy(xpath = "//*[contains(@key, 'newUser')]")
    private WebElement activeUsersWord;

    @FindBy(xpath = "//*[contains(@key, 'unactiveUser')]")
    private WebElement inactiveUsersWord;

    @FindBy(xpath = "//*[contains(@key, 'checks')]")
    private WebElement urlCheckWord;

    @FindBy(xpath = "//*[contains(@key, 'actAgent')]")
    private WebElement activeAgentsWord;

    @FindBy(xpath = "//*[contains(@key, 'unactAgent')]")
    private WebElement inactiveAgentsWord;

    @FindBy(xpath = "//*[contains(@key, 'letters')]")
    private WebElement lettersWord;

    @FindBy(xpath = "//*[contains(@class, 'navbar-toggle')]")
    private WebElement burgerBtn;

    private WebDriver driver;
    public AdminPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void clickMainPageBtn() {
        mainPageBtn.click();
    }

    public String getChecksCounterText() {
        return checksCounterText.getText();
    }

    public String getLettersCountText() {
        return lettersCountText.getText();
    }

    public String getInactiveAgentText() {
        return inactiveAgentText.getText();
    }

    public String getActiveAgentText() {
        return activeAgentText.getText();
    }

    public String getInactiveUserText() {
        return inactiveUserText.getText();
    }

    public String getActiveUserText() {
        return activeUserText.getText();
    }

    public String getTextAdminPageTitle() {
        return adminPageTitle.getText();
    }
    public String getTextActiveUsersWord() {
        return activeUsersWord.getText();
    }
    public String getTextInactiveUsersWord() {
        return inactiveUsersWord.getText();
    }
    public String getTextUrlCheckWord() {
        return urlCheckWord.getText();
    }
    public String getTextActiveAgentsWord() {
        return activeAgentsWord.getText();
    }
    public String getTextInactiveAgentsWord() {
        return inactiveAgentsWord.getText();
    }
    public String getTextLettersWord() {
        return lettersWord.getText();
    }

    public void clickBurgerBtn() {
        burgerBtn.click();
    }

}
