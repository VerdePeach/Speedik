package com.in726.app.e2e.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class UrlMonitorPage {

    @FindBy(id = "urlName")
    private WebElement urlNameInput;

    @FindBy(id = "url")
    private WebElement urlInput;

    @FindBy(xpath = "//*[contains(@id, 'seconds')]")
    private WebElement selectSecondsToCheckWebElement;

    @FindBy(xpath = "//*[contains(@id, 'linkSelect')]")
    private WebElement linkSelectWebElement;

    @FindBy(xpath = "//*[contains(@id, 'words')]")
    private WebElement wordsTextArea;

    @FindBy(xpath = "//*[contains(@id, 'urlsTables')]")
    private WebElement urlsTable;

    @FindBy(xpath = "//*[contains(@key, 'urlAdd')]")
    private WebElement urlAddBtn;

    @FindBy(xpath = "//*[contains(@key, 'mainPage')]")
    private WebElement mainPageBtn;


    @FindBy(xpath = "//h2")
    private WebElement pageTitle;

    @FindBy(xpath = "//*[contains(@key, 'check')]")
    private WebElement checkWord;

    @FindBy(xpath = "//*[contains(@key, 'keyWords')]")
    private WebElement keyWords;

    @FindBy(xpath = "//*[contains(@key, 'separete')]")
    private WebElement separateWord;

    private WebDriver driver;
    public UrlMonitorPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void setUrlNameInput(String urlName) {
        urlNameInput.clear();
        urlNameInput.sendKeys(urlName);
    }

    public void setUrlInput(String url) {
        urlInput.clear();
        urlInput.sendKeys(url);
    }

    public void setSelectSecondsToCheckByIndex(String val) {
        var selectSecondsToCheck = new Select(selectSecondsToCheckWebElement);
        selectSecondsToCheck.selectByValue(val);
    }

    public void setWordsTextArea(String words) {
        wordsTextArea.clear();
        wordsTextArea.sendKeys(words);
    }

    public void clickUrlAddBtn() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", urlAddBtn);
        Thread.sleep(500);
        urlAddBtn.click();
    }

    public void clickMainPageBtn() throws InterruptedException {
        mainPageBtn.click();
    }

    public void linkSelectByVisual(String val) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", linkSelectWebElement);
        var linkSelect = new Select(linkSelectWebElement);
        Thread.sleep(500);
        linkSelect.selectByVisibleText(val);
    }

    public boolean isDisplayedUrlsTable() {
        return urlsTable.isDisplayed();
    }

    public String getTextMainPageBtn() {
        return mainPageBtn.getText();
    }

    public String getTextPageTitle() {
        return pageTitle.getText();
    }

    public String getTextCheckWord() {
        return checkWord.getText();
    }

    public String getTextKeyWordsWord() {
        return keyWords.getText();
    }

    public String getTextSeparateWord() {
        return separateWord.getText();
    }

    public String getTextUrlNameInput() {
        return urlNameInput.getAttribute("placeholder");
    }

    public String getTextUrlInput() {
        return urlInput.getAttribute("placeholder");
    }

    public String getTextUrlAddBtn() {
        return urlAddBtn.getText();
    }

}
