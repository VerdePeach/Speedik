package com.in726.app.e2e.test.chrome;

import com.in726.app.e2e.page.LoginPage;
import com.in726.app.e2e.page.MainPage;
import com.in726.app.e2e.page.SignUpPage;
import com.in726.app.e2e.page.TariffPage;
import com.in726.app.e2e.util.Util;
import com.in726.app.enums.TariffPlan;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TariffTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static TariffPage tariffPage;
    private static MainPage mainPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", Util.getProperty("chromedriver"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        driver = new ChromeDriver(options);
        tariffPage = new TariffPage(driver);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Util.getProperty("homepage"));
    }

    @Test
    public void singUpUser() throws InterruptedException {
        loginPage.setUsernameInput(Util.getProperty("loginFreeUser"));
        loginPage.setPasswordInput(Util.getProperty("passwordFreeUser"));
        loginPage.clickLoginBtn();
        Thread.sleep(2000);
        mainPage.clickTariffPageBtn();
        Thread.sleep(2000);
        var currTariff = tariffPage.getCurrTariffText();
        var tariffToChange = "";
        if (currTariff.toUpperCase().equals("CURRENT TARIFF: FREE")) {
            tariffToChange = "PREMIUM";
        } else {
            tariffToChange = "FREE";
        }
        tariffPage.setTariffSelect(tariffToChange);
        Util.takeScreenShot(driver);
        tariffPage.clickTariffChangeBtn();
        Thread.sleep(2000);
        var actualTariffChangeMess = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Util.takeScreenShot(driver);

        Assert.assertEquals("Tariff was changed successfuly.", actualTariffChangeMess);
        if (tariffToChange.equals("FREE")) {
            Assert.assertEquals("CURRENT TARIFF: FREE", tariffPage.getCurrTariffText().toUpperCase());
        } else {
            Assert.assertEquals("CURRENT TARIFF: PREMIUM", tariffPage.getCurrTariffText().toUpperCase());
        }


        tariffPage.clickMainPageBtn();
        mainPage.clickLogoutBtn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
