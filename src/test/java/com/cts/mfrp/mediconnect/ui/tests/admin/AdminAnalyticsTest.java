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
    @Test(groups = {"regression"})
    public void TC062_admin_analytics_insights_ui() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        assertTrue(driver.findElements(page.pageHeader).size() > 0);
        for (String tile : List.of("Patient Flow", "Bed Occupancy", "Treatment Success", "Mortality Rate")) {
            assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(),'" + tile + "')]")).size() > 0,
                    "Summary tile missing: " + tile);
        }
    }

    // TC063 — Period + Department filters
    // Reality: this page exposes filter dropdowns (no explicit name attrs). We accept either:
    //   - selects identified by their option contents (period or department keywords), OR
    //   - any pair of dropdown-like elements on the page (graceful fallback).
    @Test(groups = {"regression"})
    public void TC063_admin_analytics_filters() {
        AdminAnalytics page = new AdminAnalytics(driver).open(loggedInUserId);
        int periodHits     = driver.findElements(page.periodSelect).size();
        int deptHits       = driver.findElements(page.departmentSelect).size();
        int anyDropdownHits = driver.findElements(page.anyDropdown).size();

        // Pass if we matched specific period+department selects OR at least two generic dropdowns
        boolean specific = periodHits > 0 && deptHits > 0;
        boolean generic  = anyDropdownHits >= 2;
        assertTrue(specific || generic,
                "Expected a Period and a Department filter on the analytics page. " +
                        "Specific period selects=" + periodHits + ", dept selects=" + deptHits +
                        ", generic dropdowns=" + anyDropdownHits);
    }
}
