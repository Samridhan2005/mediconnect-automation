package com.cts.mfrp.mediconnect.ui.base;

import com.cts.mfrp.mediconnect.utils.AuthSetup;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import com.cts.mfrp.mediconnect.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public abstract class UiBaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;

    @BeforeSuite(alwaysRun = true)
    public void seedUsersOnce() {
        AuthSetup.ensureSeedUsers();
    }

    @BeforeMethod(alwaysRun = true)
    public void uiSetup() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
        baseUrl = ConfigReader.get("ui.baseUrl");
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void uiTeardown() {
        DriverManager.quitDriver();
    }
}
