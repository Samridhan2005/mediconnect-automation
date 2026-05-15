package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminOverview;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** FRD: TC016 — Admin login positive flow. */
public class AdminLoginTest extends BaseAdminTest {

    @Test
    public void TC016_admin_login_positive() {
        AdminOverview admin = new AdminOverview(driver);
        assertTrue(admin.isLoaded(), "Admin should land on System Overview");
        assertTrue(admin.sidebar().isAdminControlLabelVisible(),
                "ADMIN CONTROL label should be visible in sidebar");
    }
}
