package com.in726.app.e2e.test.chrome;

import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.page.NewServerPage;
import com.in726.app.e2e.util.Util;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainPageTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static MainPage mainPage;
    private static NewServerPage newServerPage;

    @BeforeClass
    public static void setup() {
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        //создание экземпляра драйвера
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        //set driver to pages
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        newServerPage = new NewServerPage(driver);
        //окно разворачивается на полный экран
        driver.manage().window().maximize();
        //задержка на выполнение теста = 10 сек.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //получение ссылки на страницу входа из файла настроек
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void selectServerTest() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.setServerNameSelect(Util.getProperty("serverNameForGraphic"));
        Thread.sleep(5000);
//        Util.takeScreenShot(driver);
        Assert.assertTrue(mainPage.isDisplayedCpuChart());
        Assert.assertTrue(mainPage.isDisplayedDiskChart());
        Assert.assertTrue(mainPage.isDisplayedMemoryChart());

        mainPage.clickLogoutBtn();
    }

    @Test
    public void newServerCreation() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        mainPage.clickNewServerBtn();
        var newPublicKey = "seleniumPublicKey-" + new Random().nextInt();
        var newSecretKey = "seleniumSecretKey-" + new Random().nextInt();
        newServerPage.setPublicKeyInput(newPublicKey);
        newServerPage.setSecretKeyInput(newSecretKey);
        Util.takeScreenShot(driver);
        newServerPage.clickServerCreateBtn();
        Thread.sleep(2000);
        var actualServerCreatedMessage = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assert.assertEquals(Util.getProperty("messageNewServerCreated"), actualServerCreatedMessage);

        newServerPage.clickMainPageBtn();
        mainPage.clickLogoutBtn();
    }

    @Test
    public void footerPart() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("login"));
        loginPage.setPasswordInput(Util.getProperty("password"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);

        Assert.assertTrue(mainPage.isDisplayedForDevelopersLink());
        Assert.assertTrue(mainPage.isDisplayedPrivatPolicyLink());
        Assert.assertTrue(mainPage.isDisplayedSpeedikEmail());

        mainPage.clickLogoutBtn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
