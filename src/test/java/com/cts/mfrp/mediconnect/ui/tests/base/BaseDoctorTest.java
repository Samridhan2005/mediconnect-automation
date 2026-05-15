package com.cts.mfrp.mediconnect.ui.tests.base;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.BeforeMethod;

/** Shared @BeforeMethod that logs in as a Doctor before each test. */
public abstract class BaseDoctorTest extends UiBaseTest {

    protected long loggedInUserId;

    @BeforeMethod(dependsOnMethods = "uiSetup")
    public void loginAsDoctor() {
        new Login(driver).open()
                .selectDoctorTab()
                .enterEmail(ConfigReader.get("doctor.email"))
                .enterPassword(ConfigReader.get("doctor.password"))
                .submit();
        wait.until(d -> d.getCurrentUrl().matches(".*/doctor/\\d+/.*"));
        loggedInUserId = Long.parseLong(driver.getCurrentUrl().replaceAll(".*/doctor/(\\d+)/.*", "$1"));
    }
}
