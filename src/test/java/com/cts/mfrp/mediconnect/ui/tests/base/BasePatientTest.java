package com.cts.mfrp.mediconnect.ui.tests.base;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.Login;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.BeforeMethod;

/** Shared @BeforeMethod that logs in as a Patient before each test. */
public abstract class BasePatientTest extends UiBaseTest {

    protected long loggedInUserId;



    @BeforeMethod(dependsOnMethods = "uiSetup")
    public void loginAsPatient() {
        new Login(driver).open()
                .selectPatientTab()
                .enterEmail(ConfigReader.get("valid.email"))
                .enterPassword(ConfigReader.get("valid.password"))
                .submit();
        wait.until(d -> d.getCurrentUrl().matches(".*/patient/\\d+/.*"));
        loggedInUserId = Long.parseLong(driver.getCurrentUrl().replaceAll(".*/patient/(\\d+)/.*", "$1"));
    }
}
