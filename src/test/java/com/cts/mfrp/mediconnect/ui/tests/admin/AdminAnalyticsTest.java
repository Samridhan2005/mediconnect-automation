package com.cts.mfrp.mediconnect.ui.tests.admin;

import com.cts.mfrp.mediconnect.ui.pages.admin.AdminAnalytics;
import com.cts.mfrp.mediconnect.ui.tests.base.BaseAdminTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

/** FRD: TC062, TC063 — Admin Analytics & Insights. */
public class AdminAnalyticsTest extends BaseAdminTest {

    // TC062 — Analytics & Insights UI
    @Test
    public void TC062_admin_analytics_insights_ui() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Patient Flow", "Bed Occupancy", "Treatment Success", "Mortality Rate")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC063 — Period + Department filters
    @Test
    public void TC063_admin_analytics_filters() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.periodSelect).size() > 0, "Period dropdown should be visible");
        assertTrue(driver.findElements(page.departmentSelect).size() > 0, "Departments dropdown should be visible");
    }
}
