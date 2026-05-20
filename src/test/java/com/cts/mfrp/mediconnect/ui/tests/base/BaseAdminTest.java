package com.cts.mfrp.mediconnect.ui.tests.base;

import com.cts.mfrp.mediconnect.ui.base.UiBaseTest;
import com.cts.mfrp.mediconnect.ui.pages.auth.AdminLogin;
import com.cts.mfrp.mediconnect.utils.ConfigReader;
import org.testng.annotations.BeforeMethod;

/** Shared @BeforeMethod that logs in as an Admin (Super Admin) before each test. */
public abstract class BaseAdminTest extends UiBaseTest {

    protected long loggedInUserId;

    // FIX: removed `dependsOnMethods = "uiSetup"` — see BasePatientTest for details.
    // Parent UiBaseTest.uiSetup() runs first by inheritance order; no explicit dep needed.
    @BeforeMethod(alwaysRun = true)
    public void loginAsAdmin() {
        new AdminLogin(driver).open()
                .enterEmail(ConfigReader.get("admin.email"))
                .enterPassword(ConfigReader.get("admin.password"))
                .submit();
        wait.until(d -> d.getCurrentUrl().matches(".*/admin/\\d+/.*"));
        loggedInUserId = Long.parseLong(driver.getCurrentUrl().replaceAll(".*/admin/(\\d+)/.*", "$1"));
    }
}