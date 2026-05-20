package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminDiagnostics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC057 — Admin Diagnostics Module. */
public class AdminDiagnosticsTest extends BaseAdminTest {

    @Test(groups = {"regression"})
    public void TC057_admin_diagnostics_ui_tabs() {
        AdminDiagnostics page = new AdminDiagnostics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tab : List.of("Lab Reports", "Radiology", "Imaging")) {
            assertTrue(driver.findElements(By.xpath("//*[normalize-space()='" + tab + "']")).size() > 0,
                    "Tab missing: " + tab);
        }
    }
}
