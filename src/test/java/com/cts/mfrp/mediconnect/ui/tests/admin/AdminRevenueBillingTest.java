package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminRevenueBilling;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC060, TC061 — Admin Revenue & Billing. */
public class AdminRevenueBillingTest extends BaseAdminTest {

    // TC060 — Revenue & Billing UI (double-quoted xpath handles apostrophe in "Today's Revenue")
    @Test(groups = {"regression"})
    public void TC060_admin_revenue_billing_ui() {
        AdminRevenueBilling page = new AdminRevenueBilling(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Today's Revenue", "This Month", "Pending Bills", "Rejected Claims")) {
            assertTrue(driver.findElements(By.xpath(
                            "//*[contains(normalize-space(),\"" + tile + "\")]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC061 — Recent Bills + Insurance Claims tables
    @Test(groups = {"regression"})
    public void TC061_admin_revenue_bills_claims_tables() {
        AdminRevenueBilling page = new AdminRevenueBilling(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.recentBillsHeader).size() > 0,
                "Recent Bills table heading should be visible");
        assertTrue(driver.findElements(page.insuranceClaimsHdr).size() > 0,
                "Insurance Claims table heading should be visible");
    }
}
