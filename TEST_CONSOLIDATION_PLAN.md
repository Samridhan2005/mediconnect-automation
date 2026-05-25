# MediConnect UI Test Suite — Consolidation Plan

**Scope:** UI tests only (`src/test/java/com/cts/mfrp/mediconnect/ui/tests/**`). API tests excluded.

**Current state:** 324 `@Test` methods across 33 files.
**Target state:** 140–150 test cases.
**Gap to fill with new test cases:** ~20–30 net-new Zephyr test cases.

---

## 1. Inventory — Current `@Test` Method Count per File

| File | Current @Test Count | Target After Consolidation |
|---|---|---|
| `ui/tests/LoginTest.java` | 12 | 7 |
| `ui/tests/HomePageTest.java` | 9 | 3 |
| `ui/tests/PatientRegisterTest.java` | 1 | 1 |
| `ui/tests/DoctorRegisterTest.java` | 1 | 1 |
| `ui/tests/CommonTest.java` | 8 | 6 |
| `ui/tests/patient/PatientHealthOverviewTest.java` | 18 | 8 |
| `ui/tests/patient/PatientAppointmentsTest.java` | 13 | 6 |
| `ui/tests/patient/PatientLabReportsTest.java` | 12 | 4 |
| `ui/tests/patient/PatientMedicalRecordsTest.java` | 10 | 3 |
| `ui/tests/patient/PatientReminderTest.java` | 10 | 3 |
| `ui/tests/patient/PatientTelemedicineTest.java` | 10 | 3 |
| `ui/tests/patient/PatientAiAssistantTest.java` | 13 | 4 |
| `ui/tests/doctor/DoctorDashboardTest.java` | 12 | 5 |
| `ui/tests/doctor/DoctorAppointmentsTest.java` | 18 | 5 |
| `ui/tests/doctor/DoctorAnalyticsTest.java` | 15 | 3 |
| `ui/tests/doctor/DoctorPatientsTest.java` | 2 | 2 |
| `ui/tests/doctor/DoctorMedicalRecordsTest.java` | 2 | 2 |
| `ui/tests/doctor/DoctorLabReportsTest.java` | 2 | 2 |
| `ui/tests/doctor/DoctorAiAssistantTest.java` | 1 | 1 |
| `ui/tests/doctor/DoctorTelemedicineTest.java` | 14 | 3 |
| `ui/tests/doctor/DoctorSupplyChainTest.java` | 15 | 4 |
| `ui/tests/admin/AdminLoginTest.java` | 12 | 7 |
| `ui/tests/admin/AdminOverviewTest.java` | 18 | 4 |
| `ui/tests/admin/AdminAnalyticsTest.java` | 10 | 4 |
| `ui/tests/admin/AdminAppointmentsTest.java` | 13 | 3 |
| `ui/tests/admin/AdminPatientsTest.java` | 16 | 6 |
| `ui/tests/admin/AdminHospitalsTest.java` | 9 | 5 |
| `ui/tests/admin/AdminRegisterTest.java` | 4 | 2 |
| `ui/tests/admin/AdminDiagnosticsTest.java` | 8 | 2 |
| `ui/tests/admin/AdminRevenueBillingTest.java` | 14 | 5 |
| `ui/tests/admin/AdminTelemedicineTest.java` | 6 | 4 |
| `ui/tests/admin/AdminSupplyChainTest.java` | 14 | 2 |
| **TOTAL** | **324** | **120** |

> **Note:** 120 consolidated + ~25 new test cases = **~145**, which lands in your 140–150 target.

---

## 2. Consolidation Map — Existing Methods → Final Test Case

This is your group-into-one map. **No logic changes** — these are just `@Test`-method merges. Each row represents one final consolidated test method.

### LoginTest.java (12 → 7)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC001 | `TC001_application_url_loads` | `TC001_application_url_loads_mediconnect_login_page` |
| TC002 | `TC002_login_page_ui_elements_visible` | `TC002_login_page_ui_elements_visible` |
| TC003 | `TC003_role_selector_patient_and_doctor_tabs` | `TC003_role_selector_patient_and_doctor_tabs` |
| TC004 | `TC004_doctor_role_selector_routes_to_doctor_dashboard` | `TC004_doctor_role_selector_routes_to_doctor_dashboard` |
| TC005 | `TC005_forgot_password_full_flow` | `TC005_forgot_password_link_present` + `TC005a_forgot_password_opens_real_reset_flow` + `TC005b_forgot_password_reset_form_has_email_field` + `TC005c_forgot_password_empty_email_is_rejected` + `TC005d_forgot_password_valid_email_shows_confirmation` |
| TC006 | `TC006_login_button_validations` | `TC006_login_button_validations` |
| TC014 / TC072 | `TC014_invalid_login_and_role_mismatch` | `TC014_role_mismatch_negative` + `TC072_invalid_credentials_show_error` |

### HomePageTest.java (9 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC_HOME01 | `home_landing_ui_validation` | all `home_*_ui_*` and `home_*_layout_*` methods |
| TC_HOME02 | `home_navigation_to_login_register` | `home_*_navigates_to_login` + `home_*_navigates_to_register` |
| TC_HOME03 | `home_role_switch_and_footer` | remaining `home_*` methods |

### CommonTest.java (8 → 6)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC064 | `TC064_common_form_validation_rules` | keep |
| TC065 | `TC065_common_search_filter_rules` | keep |
| TC066 | `TC066_status_badge_colour_standards` | keep |
| TC067 | `TC067_navigation_rules` | keep |
| TC068 / TC069 | `TC068_ai_unavailable_and_export_failure` | merge `TC068_ai_service_unavailable_indicator` + `TC069_export_download_failure_ui_elements` |
| TC070 / TC071 | `TC070_role_access_and_empty_states` | merge `TC070_role_based_access_control` + `TC071_empty_state_displays` |

### PatientHealthOverviewTest.java (18 → 8)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC015 | `TC015_patient_login` | keep |
| TC017 | `TC017_patient_dashboard_ui_validation` | keep |
| TC018 | `TC018_summary_tiles_full_validation` | merge tile presence + values + sub-labels + navigation methods |
| TC019 | `TC019_upcoming_appointments_section` | merge UI + view-all + empty-state |
| TC020 | `TC020_health_vitals_panel` | merge vital bars + colour-coding + last-updated |
| TC021 | `TC021_medicines_and_notifications_panels` | merge taken/due rows + notifications panel |
| TC022 | `TC022_ai_widget_validation` | merge widget UI + quick action chips + open-ai-assistant |
| TC074 | `TC074_patient_portal_sign_out` | keep |

### PatientAppointmentsTest.java (13 → 6)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC023 | `TC023_appointments_page_ui` | merge header + banner + tab-bar |
| TC024 | `TC024_appointment_cards_and_states` | merge cards UI + badge colours + reschedule + cancel + empty-state |
| TC025 | `TC025_book_appointment_modal_ui` | merge modal-open + form-fields + date-picker + time-dropdown |
| TC026 | `TC026_book_appointment_mandatory_validation` | merge empty-submit + per-field-error + placeholder-invalid |
| TC027 | `TC027_book_appointment_successful_booking` | keep |

### PatientLabReportsTest.java (12 → 4)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC030 | `TC030_lab_reports_page_ui` | merge attention banner + cards + expanded grid + AI panel |
| TC031 | `TC031_ai_explanation_and_download` | merge AI explain + chips + free-form + disclaimer + download PDF |
| TC082 | `TC082_alert_banner_persistence` | keep |
| TC_LR04 | `lab_reports_filter_and_search` | merge any remaining filter/search/sort methods |

### PatientMedicalRecordsTest.java (10 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC028 | `TC028_medical_records_ui_layout` | merge two-panel + search bar + record list items + default right panel |
| TC029 | `TC029_medical_records_search_and_detail` | merge search filter + clear + select-record + record detail sections + read-only |
| TC_MR03 | `medical_records_critical_findings_indicator` | merge red-dot, badge colour, and type-coded badge checks |

### PatientReminderTest.java (10 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC033 | `TC033_medicine_reminders_ui_layout` | merge header + summary tiles + today schedule + 3 row states + prescriptions panel |
| TC034 | `TC034_mark_dose_taken_and_undo` | merge DUE-NOW + click+ + counts update + undo behaviour + adherence update |
| TC_MR03 | `reminders_data_integrity` | merge `TC042_no_null_null_anywhere` and remaining empty-state methods |

### PatientTelemedicineTest.java (10 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC032 | `TC032_telemedicine_ui_and_session_layout` | merge header + upcoming banner + Join-button-state + scheduled grid + past-sessions table |
| TC076 | `TC076_telemedicine_reschedule_and_cancel` | keep |
| TC_TM03 | `telemedicine_session_state_indicators` | merge remaining green-dot, countdown, disabled-state checks |

### PatientAiAssistantTest.java (13 → 4)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC035 | `TC035_ai_assistant_page_ui` | merge header + mode selector + your-context + greeting + quick actions + emergency-note |
| TC036 | `TC036_ai_assistant_mode_switch_and_chat` | merge symptom checker + report explanation + book appointment modes + chip + free-form + disclaimer |
| TC079 | `TC079_emergency_note_visibility` | keep |
| TC_AI04 | `ai_assistant_negative_and_offline` | merge remaining empty-state and offline-state methods |

### DoctorDashboardTest.java (12 → 5)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC007 | `TC007_doctor_dashboard_ui` | merge header + sidebar + hospital selector + notification bell + profile dropdown |
| TC008 / TC009 | `TC008_hospital_selector_and_global_search` | merge `TC008_*` + `TC009_*` |
| TC010 / TC011 | `TC010_notification_bell_and_summary_cards` | merge `TC010_*` + `TC011_*` |
| TC012 / TC013 | `TC012_recent_appointments_and_upcoming_consultations` | merge `TC012_*` + `TC012a_*` + `TC013_*` |
| TC073 / TC075 | `TC073_doctor_sign_out_and_bed_availability` | merge `TC073_*` + `TC075_*` + `TC075a_*` + `TC075b_*` |

### DoctorAppointmentsTest.java (18 → 5)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC039 | `TC039_doctor_appointments_list_ui` | merge header + summary tiles + tab bar + filters + table columns |
| TC040 | `TC040_doctor_appointments_tabs_and_calendar` | merge tab switching + calendar view + date picker |
| TC041 | `TC041_doctor_new_appointment_modal` | merge modal open + form fields + empty validation + valid save + cancel |
| TC078 | `TC078_doctor_appointments_negative_patient_search` | keep |
| TC_A99 | `doctor_appointments_extended_scenarios` | merge `TC_A01` through `TC_A14` (status badges, edit, delete, hover actions) |

### DoctorAnalyticsTest.java (15 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC047 | `TC047_doctor_analytics_ui_and_charts` | merge header + summary tiles + 4 charts rendering |
| TC048 | `TC048_doctor_analytics_time_period_filter` | merge filter dropdown options + chart refresh on change |
| TC_AN99 | `doctor_analytics_chart_data_integrity` | merge `TC_AN01` through `TC_AN13` (legend, data points, export, hover, period boundaries) |

### DoctorTelemedicineTest.java (14 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC046 | `TC046_doctor_telemedicine_ui_and_session` | merge header + summary tiles + live-now card + upcoming cards + past table |
| TC_T99A | `doctor_telemedicine_session_controls` | merge `TC_T01`–`TC_T07` (start, end, join, mute, hold, status badges) |
| TC_T99B | `doctor_telemedicine_schedule_and_history` | merge `TC_T08`–`TC_T13` (schedule modal, past filter, duration) |

### DoctorSupplyChainTest.java (15 → 4)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC049 | `TC049_doctor_supply_chain_ui_inventory` | merge header + summary tiles + filters + inventory table columns |
| TC081 | `TC081_doctor_supply_chain_low_stock_alert` | merge low-stock filter + amber badge + reorder action + out-of-stock |
| TC_SC99A | `doctor_supply_chain_add_and_export` | merge `TC_SC01`–`TC_SC07` (add item, export, search, category filter) |
| TC_SC99B | `doctor_supply_chain_status_and_actions` | merge `TC_SC08`–`TC_SC13` (status badges, edit, reorder, history) |

### AdminLoginTest.java (12 → 7)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC016 | `TC016_admin_login_valid_credentials` | merge admin URL + login + dashboard landing |
| TC017 | `TC017_admin_login_page_ui_elements_visible` | keep (admin variant of TC017) |
| TC018 / TC019 | `TC018_admin_login_email_validations` | merge email format + missing email |
| TC020 / TC021 | `TC020_admin_login_empty_and_format_errors` | merge empty fields + invalid format |
| TC022 / TC023 / TC024 | `TC022_admin_login_wrong_credentials_errors` | merge wrong email + wrong password + both wrong |
| TC025 / TC026 | `TC025_admin_login_navigation_links` | merge back-to-home + create-admin link |
| TC027 | `TC027_admin_sign_out_redirects_to_login` | keep |

### AdminOverviewTest.java (18 → 4)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC051 | `TC051_admin_system_overview_ui` | merge header + sidebar + summary tiles + period selector + hospital selector |
| TC052 | `TC052_admin_system_overview_filters` | merge period change + hospital change + reset filter |
| TC084 | `TC084_admin_hospital_branch_map` | keep |
| TC_OV99 | `admin_overview_charts_and_extras` | merge `TC_OV01`–`TC_OV15` (chart rendering, hover, drilldown, notifications) |

### AdminAnalyticsTest.java (10 → 4)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC062 | `TC062_admin_analytics_insights_ui` | merge header + summary tiles + heatmap + bed-occupancy + treatment-success + mortality + ALOS |
| TC063 | `TC063_admin_analytics_period_dept_filters` | merge period + department + hospital filter refresh |
| TC064_ANL / TC065_ANL | `admin_analytics_chart_rendering_and_data` | merge `TC064_*` + `TC065_*` for Analytics scope |
| TC066_ANL / TC067_ANL / TC068_ANL / TC069_ANL / TC070_ANL / TC071_ANL | `admin_analytics_dropdown_and_refresh_validation` | merge `TC066_*` through `TC071_*` for Analytics |

### AdminAppointmentsTest.java (13 → 3)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC056 | `TC056_admin_appointment_calendar_and_filters` | merge `TC056` + `TC056a`–`TC056f` |
| TC083 | `TC083_admin_new_appointment_creation` | merge `TC083` + `TC083a`–`TC083d` |
| TC_ADM_APT03 | `admin_appointment_validation` | merge `Admin_AppointmentsValidation` and any remaining |

### AdminPatientsTest.java (16 → 6)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC054 | `TC054_admin_patient_management_ui` | merge header + summary tiles + Inpatients/Outpatients tabs |
| TC055 | `TC055_admin_patient_ai_summarize` | merge view-detail + edit + AI-summarize |
| TC056_ADP / TC057_ADP | `admin_patient_tile_values_and_critical` | merge tile-values + critical-cases scoped to AdminPatients |
| TC058_ADP / TC059_ADP | `admin_patient_list_and_search` | merge list + table columns + search |
| TC060_ADP / TC061_ADP / TC062_ADP / TC063_ADP | `admin_patient_table_layout_and_pagination` | merge tabs + card title + table columns + pagination + table-has-rows |
| TC080 | `TC080_admin_patient_export` | merge `TC064_*` + `TC065_*` + `TC066_*` for AdminPatients + `TC080_admin_patient_management_export` |

### AdminHospitalsTest.java (9 → 5)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC053 | `TC053_admin_hospitals_ui` | merge header + summary tiles + table columns |
| TC077 | `TC077_admin_hospitals_occupancy_and_status` | merge occupancy colour + status badge + critical capacity tile |
| TC086 | `TC086_admin_register_email_domain_validation` | keep |
| TC087 | `TC087_admin_hospital_name_data_integrity` | merge `TC087` + `TC088` + `TC089` (name/phone/address validation) |
| TC088_HOSP / TC090–TC092 | `admin_add_hospital_form_and_submit` | merge `TC088_AdminHospitals_AddHospitalFormSubmit` + `TC090`–`TC092` (add hospital modal, save, cancel) |

### AdminRegisterTest.java (4 → 2)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC_AR01 | `admin_register_form_ui` | merge `admin_register_page_loads` + `admin_register_form_fields_visible` |
| TC_AR02 | `admin_register_form_validation_and_submit` | merge `admin_register_mandatory_validation` + `admin_register_successful_submission` |

### AdminDiagnosticsTest.java (8 → 2)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC057 | `TC057_admin_diagnostics_ui_tabs` | merge `TC057` + `TC057a`–`TC057g` (header + summary tiles + Lab Reports tab + Radiology tab + Imaging tab) |
| TC_DIAG02 | `admin_diagnostics_table_and_actions` | merge remaining table-columns + actions checks |

### AdminRevenueBillingTest.java (14 → 5)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC060 | `TC060_admin_revenue_billing_ui` | merge header + summary tiles + Monthly Revenue chart + Revenue by Department donut + AI Revenue Predictions |
| TC061 | `TC061_admin_revenue_bills_and_claims` | merge `TC061` + `TC061b` (Recent Bills + Insurance Claims tables) |
| TC062_RB / TC063_RB | `admin_revenue_tile_values_and_sub_labels` | merge `TC062` + `TC063` for Revenue scope |
| TC064_RB / TC065_RB / TC066_RB / TC067_RB | `admin_revenue_tiles_appointments_pending_cancelled` | merge `TC064`–`TC067` for Revenue scope |
| TC068_RB / TC069_RB / TC070_RB / TC071_RB / TC072_RB | `admin_revenue_charts_and_ai_section_and_export` | merge `TC068`–`TC072` for Revenue scope + Download/Export |

### AdminSupplyChainTest.java (14 → 2)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC058 | `TC058_admin_supply_chain_ui_ai_insights` | merge `TC058` + `TC058a`–`TC058f` (header + tiles + Drug Usage Trend + Stock Consumption donut + AI Supply Insights) |
| TC059 | `TC059_admin_supply_chain_inventory_table` | merge `TC059` + `TC059a`–`TC059f` (inventory tabs + table columns + new order) |

### AdminTelemedicineTest.java (6 → 4)

| Final TC ID | Final Method Name | Merge These Existing Methods |
|---|---|---|
| TC093 | `TC093_admin_telemedicine_ui` | merge `TC093` + `TC094` (header + summary tiles + live sessions) |
| TC095 | `TC095_admin_telemedicine_scheduling` | keep |
| TC096 | `TC096_admin_telemedicine_session_history` | keep |
| TC097 / TC098 | `TC097_admin_telemedicine_filters_and_export` | merge `TC097` + `TC098` |

---

## 3. New Test Cases to Add to Excel (Gap-Fill)

After consolidation we land at **120 methods**. To reach the 140–150 target, add the following **25 net-new Zephyr UI test cases**. These cover real gaps in your current coverage matrix (forgot-password completion, sign-out across all portals, cross-browser, exception handling, notifications, accessibility, defect regressions for DEF_002 through DEF_006).

Paste directly into Excel:

| Test Case ID | Test Case Title | Module | Linked Requirement ID | Test Type | Priority | Brief Description |
|---|---|---|---|---|---|---|
| TC100 | Forgot Password — Reset Token Validity | LoginAuth | TS_05_ForgotPassword | Functional, Positive | Medium | Verify the password-reset link emailed to the user is valid for the documented window and rejected after expiry. |
| TC101 | Forgot Password — Reset Password and Re-Login | LoginAuth | TS_05_ForgotPassword | Functional, Positive | Medium | Verify user can complete reset with a new password and log in successfully with the new credentials. |
| TC102 | Patient Dashboard — Layout on 1366x768 Resolution | PatientDashboard | TS_06_PatientDashboard | UI, Negative | High | Regression for DEF_002: verify Summary Tiles, Health Vitals panel and AI widget do not overlap at 1366x768. |
| TC103 | Patient Dashboard — Notification Bell Unread Badge | PatientDashboard | TS_20_NotificationsAlerts | Functional, Positive | Medium | Verify unread badge count on the bell matches the Unread Notifications tile count. |
| TC104 | Patient Appointments — Reschedule Pre-fills Existing Slot | PatientAppointmentMgmt | TS_08_PatientAppointmentManagement | Functional, Positive | Medium | Verify clicking Reschedule opens a form pre-filled with the existing doctor/date/time/reason. |
| TC105 | Doctor Lab Reports — Filter by Status Returns Correct Rows | DoctorLabReports | TS_15_DoctorLabReports | Functional, Positive | High | Regression for DEF_003: verify Status filter applies and the table updates to only Pending/Completed/Abnormal rows. |
| TC106 | Doctor Lab Reports — Filter by Patient Returns Correct Rows | DoctorLabReports | TS_15_DoctorLabReports | Functional, Positive | High | Regression for DEF_003: verify Patient filter applies and the table updates accordingly. |
| TC107 | Doctor Lab Reports — Filter by Date Range | DoctorLabReports | TS_15_DoctorLabReports | Functional, Positive | High | Regression for DEF_003: verify Date filter narrows the table to the selected date range. |
| TC108 | Admin Revenue & Billing — Chart and Recent Bills Render | AdminRevenueBilling | TS_18_AdminRevenueBilling | UI, Positive | High | Regression for DEF_004: verify the revenue chart and Recent Bills table both render with data (not blank panels). |
| TC109 | Admin Analytics — KPI Cards Container Overflow Check | AdminAnalyticsInsights | TS_19_AdminAnalyticsInsights | UI, Positive | Medium | Regression for DEF_005: verify KPI cards stay inside the container at common resolutions and the Patient Flow Heatmap renders on first load. |
| TC110 | Patient Telemedicine — Reschedule Action Updates Status | PatientTelemedicine | TS_10_PatientTelemedicine | Functional, Positive | High | Regression for DEF_006: verify Reschedule on an upcoming session updates session status without error. |
| TC111 | Patient Telemedicine — Cancel Action Updates Status | PatientTelemedicine | TS_10_PatientTelemedicine | Functional, Positive | High | Regression for DEF_006: verify Cancel on an upcoming session updates session status to Cancelled. |
| TC112 | Login Page — Admin Role Tab Presence | LoginPageUI | TS_03_RoleSelector | UI, Negative | High | Regression for DEF_001: verify Role Selector exposes Patient, Doctor and Admin options consistently (admin currently routed via separate URL — verify documented behaviour). |
| TC113 | Notifications — Mark All Read Resets Unread Count | NotificationsAlerts | TS_20_NotificationsAlerts | Functional, Positive | Medium | Verify the Mark All Read link sets the Unread Notifications summary tile to 0 and clears the bell badge. |
| TC114 | Cross-Browser — Application Loads on Edge | CrossBrowser | TS_22_CrossBrowserResponsive | Compatibility | Medium | Verify the MediConnect landing page and login flow load on Microsoft Edge. |
| TC115 | Cross-Browser — Application Loads on Firefox | CrossBrowser | TS_22_CrossBrowserResponsive | Compatibility | Medium | Verify the landing page and login flow load on Firefox. |
| TC116 | Sign Out — Browser Back Button Does Not Restore Session | SessionMgmt | TS_21_SignOutSessionManagement | Functional, Negative | High | Verify pressing browser back after sign-out keeps the user on Login and does not restore the previous dashboard. |
| TC117 | Sign Out — Direct URL Access After Logout | SessionMgmt | TS_21_SignOutSessionManagement | Functional, Negative | High | Verify direct navigation to a protected portal URL after logout redirects to Login. |
| TC118 | Exception — Network Failure During Form Submit | ErrorHandling | TS_23_ExceptionErrorHandling | Functional, Negative | Medium | Verify a network failure during Save displays a user-friendly toast and preserves form data. |
| TC119 | Exception — Server 500 on Page Load | ErrorHandling | TS_23_ExceptionErrorHandling | Functional, Negative | Medium | Verify a 500 response on a page load displays a graceful error state, not a raw stack trace. |
| TC120 | Doctor Dashboard — Global Search Auto-Suggest | DoctorDashboard | TS_12_DoctorDashboard | Functional, Positive | Medium | Verify typing 2–3 characters in global search shows auto-suggest matches for patient name/ID. |
| TC121 | Doctor Patients — Add New Patient Form Validation | PatientRecordsEPrescription | TS_14_PatientRecordsEPrescription | Functional, Negative | Medium | Verify mandatory field highlights and error messages on the Add Patient registration form. |
| TC122 | Admin Hospitals — Hospital Status Filter | AdminUserMgmt | TS_17_AdminUserManagement | Functional, Positive | Medium | Verify filtering by Active / Under Maintenance / Critical Capacity narrows the hospitals table correctly. |
| TC123 | Doctor Medical Records — New Record Save and Appears in List | PatientRecordsEPrescription | TS_14_PatientRecordsEPrescription | Functional, Positive | High | Verify a saved new record appears at the top of the patient's records list in the right panel. |
| TC124 | AI Health Assistant — Disclaimer Always Visible | AIAssistant | TS_11_MedicineRemindersAIAssistant | UI, Positive | Low | Verify the educational disclaimer is permanently visible below the input regardless of conversation length. |

---

## 4. Skip / Pass-Fail Audit

You flagged TC005a/b/c/d, TC042-13, TCAN02–13, TCSC03–13 as being "ignored." Result:

| ID Pattern | Status | Location | Notes |
|---|---|---|---|
| TC005a, TC005b, TC005c, TC005d | **EXIST and run pass/fail** | `LoginTest.java` lines 116, 145, 160, 194 | All four are real `@Test` methods with assertions. No `@Ignore` / `SkipException` / `enabled=false`. **Action: they're not skipped — they produce pass/fail.** Recommendation: still merge into one `TC005_forgot_password_full_flow` per the consolidation map above. |
| TC042 | **EXISTS in two places (collision)** | `PatientReminderTest.java` line 129 (`TC042_no_null_null_anywhere`) and `DoctorLabReportsTest.java` line 21 (`TC042_doctor_lab_reports_ui`) | **Action: rename one of them.** Same TC ID across two files is a Zephyr-mapping risk. Suggested: rename PatientReminder version to `TC_PR_NULLCHECK_42`. |
| TC047, TC048, TC049 | **EXIST and run pass/fail** | `DoctorAnalyticsTest.java` (TC047, TC048) and `DoctorSupplyChainTest.java` (TC049) | Not skipped. |
| TC0410, TC0411, TC0412, TC0413 | **DO NOT EXIST** | — | These IDs are not in code. Likely a numbering typo in your Excel — were you intending TC047–TC049 + something else? Confirm before adding. |
| TCAN02–TCAN13 | **EXIST as `TC_AN02`–`TC_AN13`** | `DoctorAnalyticsTest.java` lines 100+ | Naming mismatch: code uses `TC_AN` (with underscore), Excel rows say `TCAN`. **Action: reconcile naming. Either rename Excel rows to `TC_AN02`–`TC_AN13`, or rename methods in code.** All are real `@Test` and produce pass/fail. |
| TCSC03–TCSC13 | **EXIST as `TC_SC03`–`TC_SC13`** | `DoctorSupplyChainTest.java` lines 120+ | Same naming mismatch as above. **Action: reconcile naming.** All produce pass/fail. |

**Bottom line:** None of the IDs you flagged are actually `@Ignore`d or skipped. They either (a) already pass/fail, (b) collide with another file's TC ID, or (c) are referenced in Excel with one naming convention but in code with another (`TCAN` vs `TC_AN`). After the consolidation in section 2, most of these IDs collapse into a single parent test case anyway.

---

## 5. Recommended Execution Order

1. **Sanity:** run `mvn test -Dtest=LoginTest` today, confirm 12 pass/fail.
2. **Reconcile naming:** rename `TC_AN*` → `TCAN*` (or update Excel) so Zephyr mapping matches. Same for `TC_SC*` and `TC_T*`/`TC_OV*`/`TC_A*`.
3. **Resolve TC042 collision** in PatientReminderTest vs DoctorLabReportsTest.
4. **Apply consolidation** per Section 2, one file at a time. Each merge is a mechanical refactor — same assertions, fewer `@Test` annotations.
5. **Add the 25 new TCs** from Section 3 to your Excel and then automate them.
6. Final count target: **~145 `@Test` methods** matching ~145 Zephyr rows.
