# UI Test Case Consolidation — Mapping for Excel/Zephyr

**Date**: 2026-05-20
**Before**: 279 UI @Test methods across 33 files
**After**: 128 UI @Test methods across 33 files (slightly under the 140-150 target — methods reflect natural scenario boundaries)
**API tests**: untouched (not in scope)

---

## How to read this

Each merged method represents ONE test case in Zephyr/Excel. The "Original TC IDs absorbed" column lists the granular sub-tests that are now part of that merged method. In Zephyr:

- **Add** every NEW TC ID from the "Merged TC ID" column that isn't already in your Excel.
- **Remove** (or mark as sub-scenarios of the merged TC) all the granular IDs in the "Original TC IDs absorbed" column that won't exist as separate scripts anymore.
- TC IDs marked "(kept as-is)" need no change.

---

## File-by-file mapping

### 1. LoginTest.java — 12 → 7

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC001 (kept as-is) | TC001 | URL loads MediConnect site |
| TC002 (kept as-is) | TC002 | Login page UI elements visible |
| TC003_014_login_role_selector_tabs | TC003 + TC014 | Role selector tabs switch form + only one tab active |
| TC004 (kept as-is) | TC004 | Doctor login fields and validation |
| TC005_005d_forgot_password_flow | TC005 + TC005a + TC005b + TC005c + TC005d | Forgot Password link, reset form, empty rejected, valid confirmation |
| TC006 (kept as-is) | TC006 | Login validation for empty/invalid inputs |
| TC072 (kept as-is) | TC072 | Invalid credentials show error |

### 2. HomePageTest.java — 9 → 4 (NEW — no TC IDs before)

| Merged TC ID | Original methods absorbed | Coverage |
|---|---|---|
| home_url_loads_and_hero_visible | home_url_loads + home_hero_heading | Landing page loads + hero h1 |
| home_shows_all_ctas | home_shows_header_ctas + home_shows_hero_ctas + home_shows_cta_section_buttons | All CTAs visible |
| home_get_started_routes_to_login | home_header_get_started + home_hero_get_started_free | Get Started routes to /login |
| home_admin_portal_routes_to_admin_login | home_header_admin_portal + home_hero_admin_portal | Admin Portal routes to /admin/login |

### 3. PatientRegisterTest.java — 1 → 1
| Merged TC ID | Original | Coverage |
|---|---|---|
| TP001 (kept as-is) | TP001 | Patient registration lands on dashboard |

### 4. DoctorRegisterTest.java — 1 → 1
| Merged TC ID | Original | Coverage |
|---|---|---|
| TD001 (kept as-is) | TD001 | Doctor registration lands on dashboard |

### 5. CommonTest.java — 8 → 8 (unchanged)
TC064, TC065, TC066, TC067, TC068, TC069, TC070, TC071 — all kept as-is.

### 6. AdminLoginTest.java — 12 → 6

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC016 (kept as-is) | TC016 | Admin login positive |
| TC017_019_admin_login_ui_and_password_masking | TC017 + TC018 + TC019 | UI elements + password masked + visibility toggle |
| TC020_021_admin_login_empty_and_invalid_format | TC020 + TC021 | Empty fields + invalid email format |
| TC022_024_admin_login_wrong_credentials | TC022 + TC023 + TC024 | Wrong email + wrong password + both wrong |
| TC025_026_admin_login_back_home_and_register_links | TC025 + TC026 | Back to Home + Create admin links |
| TC027 (kept as-is) | TC027 | Admin sign-out |

### 7. AdminRegisterTest.java — 4 → 2 (NEW — no TC IDs before)

| Merged TC ID | Original methods absorbed | Coverage |
|---|---|---|
| admin_register_page_loads_and_back_link | admin_register_page_loads + admin_register_page_has_back_to_login_link | Page loads + Back-to-login link |
| admin_register_form_fields_and_submit | admin_register_form_has_basic_fields + admin_register_form_has_submit_button | Form fields + submit button |

### 8. AdminPatientsTest.java — 15 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC054_055_admin_patient_management_ui_and_tiles | TC054 (header) + TC054 (management UI) + TC055 (tile labels) | Patient Management header + UI + tile labels |
| TC055_admin_patient_ai_summarize (kept as-is) | TC055 (AI summarize) | AI Summarize button |
| TC056_060_admin_patient_tile_values | TC056 + TC057 + TC058 + TC059 + TC060 | Tile values + Total/Inpatient/Outpatient/Critical tiles |
| TC061_066_admin_patient_table_and_pagination | TC061 + TC062 + TC063 + TC064 + TC065 + TC066 | Tabs + list card + columns + rows + pagination + search |
| TC080 (kept as-is) | TC080 | Export CSV button |

### 9. AdminOverviewTest.java — 18 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC051_OV01_OV02_admin_overview_ui_and_filters | TC051 + TC_OV01 + TC_OV02 | Page UI + title/subtitle + filter chips |
| TC052_OV03_OV05_admin_overview_stat_tiles | TC052 + TC_OV03 + TC_OV04 + TC_OV05 | Filters + tile labels + values + sub-labels |
| TC_OV06_OV09_admin_overview_charts | TC_OV06 + TC_OV07 + TC_OV08 + TC_OV09 | All 4 charts (Appointment Inflow, Disease Distribution, Bed Occupancy, Revenue) |
| TC084_OV10_OV11_admin_overview_map_and_ai_insights | TC084 + TC_OV10 + TC_OV11 | Hospital Branch Map + AI System Insights |
| TC_OV12_OV15_admin_overview_notifications_and_profile | TC_OV12 + TC_OV13 + TC_OV14 + TC_OV15 | Notification panel + items + admin profile + logout |

### 10. AdminAnalyticsTest.java — 10 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC062_063_admin_analytics_ui_and_filters | TC062 + TC063 | Analytics header + tiles + filter dropdowns |
| TC064_065_admin_analytics_charts | TC064 + TC065 | Heatmap + secondary charts |
| TC066_068_admin_analytics_dropdown_options | TC066 + TC067 + TC068 | Period + Departments + Hospitals dropdown options |
| TC069_071_admin_analytics_filter_refresh | TC069 + TC070 + TC071 | Filter changes refresh dashboard |

### 11. AdminAppointmentsTest.java — 3 → 3 (kept as-is, fixed compile errors)
TC056, TC083, Admin_AppointmentsValidation. The teammate-added method had broken locator references (`page.filter`, `page.recentAppointments`, `page.app`) — fixed to use existing locators (`filtersHeading`, `recentApptsHeading`, `allApptsHeading`).

### 12. AdminDiagnosticsTest.java — 8 → 3

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC057_057a_057b_admin_diagnostics_tabs_and_tiles | TC057 + TC057a + TC057b | Tabs visible + tab switching + summary tiles |
| TC057c_057d_admin_diagnostics_filters_and_search | TC057c + TC057d | Lab Reports filters/table + search filter |
| TC057e_057f_057g_admin_diagnostics_actions_badges_ai | TC057e + TC057f + TC057g | Action buttons + status badges + AI summary panel |

### 13. AdminSupplyChainTest.java — 14 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC058_058a_058c_admin_supply_chain_ui_and_header_buttons | TC058 + TC058a + TC058b + TC058c | Page UI + header buttons + new order modal + export CSV |
| TC058d_058f_admin_supply_chain_charts_and_ai | TC058d + TC058e + TC058f | Drug Usage + Stock Consumption charts + AI insights |
| TC059_059a_admin_supply_chain_inventory_heading | TC059 + TC059a | Inventory table + heading and count |
| TC059b_059f_admin_supply_chain_inventory_filters_search_badges | TC059b + TC059c + TC059d + TC059e + TC059f | All inventory filters + search + status badges |

### 14. AdminHospitalsTest.java — 9 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC053_077_admin_hospitals_ui_and_occupancy | TC053 + TC077 | Hospitals page UI + occupancy colors |
| TC086 (kept as-is) | TC086 | Sidebar nav to Hospitals page |
| TC087_089_admin_add_hospital_modal_and_submit | TC087 + TC089 | Add Hospital opens + submit creates row |
| TC088_091_admin_hospitals_row_data_and_details | TC088 + TC091 | Every row has non-empty name + Details expanded view |
| TC090_092_admin_hospitals_edit_modal_and_save | TC090 + TC092 | Edit prefilled + Save persists |

### 15. AdminTelemedicineTest.java — 6 → 3

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC093_094_095_admin_telemedicine_ui_and_sections | TC093 + TC094 + TC095 | Header + tiles + section headings |
| TC096 (kept as-is) | TC096 | Join button opens video session (BUG-009) |
| TC097_098_admin_telemedicine_schedule_modal | TC097 + TC098 | Schedule Session modal opens + has fields |

### 16. AdminRevenueBillingTest.java — 14 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC060_063_admin_revenue_ui_and_tile_labels | TC060 + TC061 + TC062 + TC063 | Page UI + tile labels + values + sub-labels |
| TC064_067_admin_revenue_individual_tiles | TC064 + TC065 + TC066 + TC067 | Total Appointments + Confirmed + Pending + Cancelled tiles |
| TC068_070_admin_revenue_charts_and_ai | TC068 + TC069 + TC070 | Appointments Over Time + Breakdown + AI Insights |
| TC071_072_admin_revenue_recent_bills_table | TC071 + TC072 | Recent Bills heading + columns |
| TC061b (kept as-is) | TC061b | Bills + Claims tables (teammate's test) |

### 17. DoctorDashboardTest.java — 12 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC007_010_doctor_dashboard_header_and_nav | TC007 + TC008 + TC009 + TC010 | Page title + sidebar links + hospital selector + notification bell |
| TC011 (kept as-is) | TC011 | Stat cards labels + values |
| TC012_012a_doctor_dashboard_todays_appointments | TC012 + TC012a | Today's appointments section + row data |
| TC013_073_doctor_dashboard_consultations_and_signout | TC013 + TC073 | Upcoming consultations + Sign Out button |
| TC075_075a_075b_doctor_dashboard_side_sections | TC075 + TC075a + TC075b | Bed availability + Lab reports + Supply chain alerts |

### 18. DoctorPatientsTest.java — 2 → 2 (unchanged)
TC037, TC038 kept as-is.

### 19. DoctorAppointmentsTest.java — 17 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC039_A01_A04_doctor_appointments_header_and_buttons | TC039 + TC_A01 + TC_A02 + TC_A03 + TC_A04 | Header + title + subtitle + Calendar view + New Appointment buttons |
| TC040_A05_A08_doctor_appointments_tabs_and_stat_cards | TC040 + TC_A05 + TC_A06 + TC_A07 + TC_A08 | Tab bar (Today active) + stat card labels/values/sub-labels |
| TC041 (kept as-is) | TC041 | New Appointment modal opens |
| TC_A09_A10_A13_doctor_appointments_toolbar_columns_pagination | TC_A09 + TC_A10 + TC_A13 | Toolbar filters + table columns + pagination label |
| TC078_A11_A12_A14_doctor_appointments_rows_and_detail_panel | TC078 + TC_A11 + TC_A12 + TC_A14 | Table rows + row data + detail panel opens on click |

### 20. DoctorLabReportsTest.java — 2 → 2 (unchanged)
TC042, TC043 kept as-is.

### 21. DoctorMedicalRecordsTest.java — 2 → 2 (unchanged)
TC044, TC045 kept as-is.

### 22. DoctorAnalyticsTest.java — 15 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC047 (kept as-is) | TC047 | Analytics page UI |
| TC048_AN01_AN04_analytics_header_and_filter | TC048 + TC_AN01 + TC_AN02 + TC_AN03 + TC_AN04 | Time period filter + title + subtitle + Export + range select |
| TC_AN05_AN07_analytics_stat_cards | TC_AN05 + TC_AN06 + TC_AN07 | Stat card labels + values + sub-labels |
| TC_AN08_AN13_analytics_charts_and_diagnoses | TC_AN08 + TC_AN09 + TC_AN10 + TC_AN11 + TC_AN12 + TC_AN13 | All charts + top diagnoses + charts grid |

### 23. DoctorSupplyChainTest.java — 15 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC049_SC01_SC04_doctor_supply_chain_ui_and_header_buttons | TC049 + TC_SC01 + TC_SC02 + TC_SC03 + TC_SC04 | Page UI + header tiles + title + subtitle + Export + Add item |
| TC081_SC08_doctor_supply_chain_filters_and_toolbar | TC081 + TC_SC08 | Low Stock filter + toolbar (search + 2 dropdowns) |
| TC_SC05_SC07_supply_chain_stat_cards | TC_SC05 + TC_SC06 + TC_SC07 | Stat card labels + values + sub-labels |
| TC_SC09_SC13_supply_chain_table_and_pagination | TC_SC09 + TC_SC10 + TC_SC11 + TC_SC12 + TC_SC13 | Columns + rows + row data + pagination label + pagination buttons |

### 24. DoctorTelemedicineTest.java — 14 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC046 (kept as-is) | TC046 | Telemedicine main UI overview |
| TC_T01_T03_telemedicine_header_elements | TC_T01 + TC_T02 + TC_T03 | Title + subtitle + Schedule session button |
| TC_T04_T06_telemedicine_stat_cards | TC_T04 + TC_T05 + TC_T06 | Stat card labels + values + sub-labels |
| TC_T07_T13_telemedicine_sections_and_past_table | TC_T07 + TC_T08 + TC_T09 + TC_T10 + TC_T11 + TC_T12 + TC_T13 | Live/Upcoming + Past sessions + columns + rows + row data + past-card |

### 25. DoctorAiAssistantTest.java — 1 → 1 (unchanged)
TC050 kept as-is.

### 26. PatientHealthOverviewTest.java — 18 → 7

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC015 (kept as-is) | TC015 | Patient login lands on Health Overview |
| TC017_130_132_patient_dashboard_header_and_sidebar | TC017 + TC130 + TC131 + TC132 | UI validation + page header + top-right + sidebar profile |
| TC018_133_134_patient_summary_tiles_and_banner | TC018 + TC133 + TC134 | Summary tiles + info chips + sub-labels |
| TC019_135_upcoming_appointments_and_health_score | TC019 + TC135 | Upcoming appointments + Health score label |
| TC020_136_137_patient_health_vitals_and_activity | TC020 + TC136 + TC137 | Health Vitals + 4 metrics + Recent Activity |
| TC021_022_138_patient_medicines_notifications_and_ai | TC021 + TC022 + TC138 | Medicines + notifications + AI widget + AI status |
| TC074_139_patient_signout_and_no_nulls | TC074 + TC139 | Sign-out + no null values |

### 27. PatientAppointmentsTest.java — 13 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC023_150_152_patient_appointments_ui_header_sidebar | TC023 + TC150 + TC151 + TC152 | Page UI + sub-label + chips + sidebar |
| TC024_156_patient_appointments_cards_or_empty_state | TC024 + TC156 | Cards or empty state + empty message |
| TC025_026_027_patient_book_appointment_flow | TC025 + TC026 + TC027 | Modal UI + mandatory validation + success |
| TC153_154_155_patient_appointments_banner_and_tabs | TC153 + TC154 + TC155 | Consultation banner + Upcoming tab count + tab switching |
| TC157 (kept as-is) | TC157 | No null values |

### 28. PatientLabReportsTest.java — 12 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC030_082_158_159_patient_lab_reports_ui_and_banner | TC030 + TC082 + TC158 + TC159 | Lab Reports UI + alert banner + sub-label + chip/bell |
| TC031 (kept as-is) | TC031 | AI explanation and download |
| TC160_164_patient_lab_reports_ai_panel | TC160 + TC161 + TC162 + TC163 + TC164 | AI panel + help text + question chips + ask input + disclaimer |
| TC165_166_patient_lab_reports_sidebar_and_no_nulls | TC165 + TC166 | Sidebar profile + no nulls |

### 29. PatientMedicalRecordsTest.java — 10 → 4

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC028_167_168_patient_medical_records_ui_and_header | TC028 + TC167 + TC168 | Medical Records UI + sub-label + chip/bell |
| TC029_172_patient_medical_records_search | TC029 + TC172 | Search + search input visible |
| TC169_170_171_patient_medical_records_panels_and_empty_state | TC169 + TC170 + TC171 | Two-panel layout + empty state + right panel default |
| TC173_174_patient_medical_records_sidebar_and_no_nulls | TC173 + TC174 | Sidebar + no nulls |

### 30. PatientReminderTest.java — 10 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC033_035_036_patient_reminders_ui_header | TC033 + TC035 + TC036 | Reminders UI + sub-label + chip/bell |
| TC034 (kept as-is) | TC034 | Mark dose as taken |
| TC037_038_patient_reminders_stat_tiles | TC037 + TC038 | Stat tile labels + sub-labels |
| TC039_040_041_patient_reminders_schedule_and_prescriptions | TC039 + TC040 + TC041 | Today's Schedule + All Prescriptions + empty states |
| TC042 (kept as-is) | TC042 | No null values |

### 31. PatientTelemedicineTest.java — 10 → 3

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC032_076_175_176_patient_telemedicine_ui_and_header | TC032 + TC076 + TC175 + TC176 | Telemedicine UI + sessions/empty + sub-label + chip/bell |
| TC177_180_patient_telemedicine_sections_and_empty_state | TC177 + TC178 + TC179 + TC180 | Upcoming + Scheduled + Past sections + empty messages |
| TC181_182_patient_telemedicine_sidebar_and_no_nulls | TC181 + TC182 | Sidebar + no nulls |

### 32. PatientAiAssistantTest.java — 13 → 5

| Merged TC ID | Original TC IDs absorbed | Coverage |
|---|---|---|
| TC035_036_183_185_patient_ai_assistant_ui_and_modes | TC035 + TC036 + TC183 + TC184 + TC185 | AI page UI + mode switching + sub-label + chip/bell + mode sub-labels |
| TC079_186_187_patient_ai_emergency_and_context | TC079 + TC186 + TC187 | Emergency Note + YOUR CONTEXT + AI greeting bubble |
| TC188_189_patient_ai_ask_input_and_disclaimer | TC188 + TC189 | Ask input/send + AI disclaimer |
| TC190_191_patient_ai_quick_actions_and_common_questions | TC190 + TC191 | QUICK ACTIONS + COMMON QUESTIONS sections |
| TC192 (kept as-is) | TC192 | No null values |

---

## Excel updates needed

### A) NEW TC IDs to ADD to your Excel (74 new merged TC IDs)

These are the merged TC IDs not present in the original Excel. Add one row per ID with the coverage description above.

**LoginTest**: `TC003_014_login_role_selector_tabs`, `TC005_005d_forgot_password_flow`

**HomePageTest** (NEW page — none of these were in Excel): `home_url_loads_and_hero_visible`, `home_shows_all_ctas`, `home_get_started_routes_to_login`, `home_admin_portal_routes_to_admin_login`

**AdminLoginTest**: `TC017_019_admin_login_ui_and_password_masking`, `TC020_021_admin_login_empty_and_invalid_format`, `TC022_024_admin_login_wrong_credentials`, `TC025_026_admin_login_back_home_and_register_links`

**AdminRegisterTest** (NEW page): `admin_register_page_loads_and_back_link`, `admin_register_form_fields_and_submit`

**AdminPatientsTest**: `TC054_055_admin_patient_management_ui_and_tiles`, `TC056_060_admin_patient_tile_values`, `TC061_066_admin_patient_table_and_pagination`

**AdminOverviewTest**: `TC051_OV01_OV02_admin_overview_ui_and_filters`, `TC052_OV03_OV05_admin_overview_stat_tiles`, `TC_OV06_OV09_admin_overview_charts`, `TC084_OV10_OV11_admin_overview_map_and_ai_insights`, `TC_OV12_OV15_admin_overview_notifications_and_profile`

**AdminAnalyticsTest**: `TC062_063_admin_analytics_ui_and_filters`, `TC064_065_admin_analytics_charts`, `TC066_068_admin_analytics_dropdown_options`, `TC069_071_admin_analytics_filter_refresh`

**AdminDiagnosticsTest**: `TC057_057a_057b_admin_diagnostics_tabs_and_tiles`, `TC057c_057d_admin_diagnostics_filters_and_search`, `TC057e_057f_057g_admin_diagnostics_actions_badges_ai`

**AdminSupplyChainTest**: `TC058_058a_058c_admin_supply_chain_ui_and_header_buttons`, `TC058d_058f_admin_supply_chain_charts_and_ai`, `TC059_059a_admin_supply_chain_inventory_heading`, `TC059b_059f_admin_supply_chain_inventory_filters_search_badges`

**AdminHospitalsTest**: `TC053_077_admin_hospitals_ui_and_occupancy`, `TC087_089_admin_add_hospital_modal_and_submit`, `TC088_091_admin_hospitals_row_data_and_details`, `TC090_092_admin_hospitals_edit_modal_and_save`

**AdminTelemedicineTest**: `TC093_094_095_admin_telemedicine_ui_and_sections`, `TC097_098_admin_telemedicine_schedule_modal`

**AdminRevenueBillingTest**: `TC060_063_admin_revenue_ui_and_tile_labels`, `TC064_067_admin_revenue_individual_tiles`, `TC068_070_admin_revenue_charts_and_ai`, `TC071_072_admin_revenue_recent_bills_table`

**AdminAppointmentsTest**: `Admin_AppointmentsValidation` (no TC prefix — give it one if you wish)

**DoctorDashboardTest**: `TC007_010_doctor_dashboard_header_and_nav`, `TC012_012a_doctor_dashboard_todays_appointments`, `TC013_073_doctor_dashboard_consultations_and_signout`, `TC075_075a_075b_doctor_dashboard_side_sections`

**DoctorAppointmentsTest**: `TC039_A01_A04_doctor_appointments_header_and_buttons`, `TC040_A05_A08_doctor_appointments_tabs_and_stat_cards`, `TC_A09_A10_A13_doctor_appointments_toolbar_columns_pagination`, `TC078_A11_A12_A14_doctor_appointments_rows_and_detail_panel`

**DoctorAnalyticsTest**: `TC048_AN01_AN04_analytics_header_and_filter`, `TC_AN05_AN07_analytics_stat_cards`, `TC_AN08_AN13_analytics_charts_and_diagnoses`

**DoctorSupplyChainTest**: `TC049_SC01_SC04_doctor_supply_chain_ui_and_header_buttons`, `TC081_SC08_doctor_supply_chain_filters_and_toolbar`, `TC_SC05_SC07_supply_chain_stat_cards`, `TC_SC09_SC13_supply_chain_table_and_pagination`

**DoctorTelemedicineTest**: `TC_T01_T03_telemedicine_header_elements`, `TC_T04_T06_telemedicine_stat_cards`, `TC_T07_T13_telemedicine_sections_and_past_table`

**PatientHealthOverviewTest**: `TC017_130_132_patient_dashboard_header_and_sidebar`, `TC018_133_134_patient_summary_tiles_and_banner`, `TC019_135_upcoming_appointments_and_health_score`, `TC020_136_137_patient_health_vitals_and_activity`, `TC021_022_138_patient_medicines_notifications_and_ai`, `TC074_139_patient_signout_and_no_nulls`

**PatientAppointmentsTest**: `TC023_150_152_patient_appointments_ui_header_sidebar`, `TC024_156_patient_appointments_cards_or_empty_state`, `TC025_026_027_patient_book_appointment_flow`, `TC153_154_155_patient_appointments_banner_and_tabs`

**PatientLabReportsTest**: `TC030_082_158_159_patient_lab_reports_ui_and_banner`, `TC160_164_patient_lab_reports_ai_panel`, `TC165_166_patient_lab_reports_sidebar_and_no_nulls`

**PatientMedicalRecordsTest**: `TC028_167_168_patient_medical_records_ui_and_header`, `TC029_172_patient_medical_records_search`, `TC169_170_171_patient_medical_records_panels_and_empty_state`, `TC173_174_patient_medical_records_sidebar_and_no_nulls`

**PatientReminderTest**: `TC033_035_036_patient_reminders_ui_header`, `TC037_038_patient_reminders_stat_tiles`, `TC039_040_041_patient_reminders_schedule_and_prescriptions`

**PatientTelemedicineTest**: `TC032_076_175_176_patient_telemedicine_ui_and_header`, `TC177_180_patient_telemedicine_sections_and_empty_state`, `TC181_182_patient_telemedicine_sidebar_and_no_nulls`

**PatientAiAssistantTest**: `TC035_036_183_185_patient_ai_assistant_ui_and_modes`, `TC079_186_187_patient_ai_emergency_and_context`, `TC188_189_patient_ai_ask_input_and_disclaimer`, `TC190_191_patient_ai_quick_actions_and_common_questions`

### B) TC IDs to REMOVE from Excel (or mark as sub-scenarios)

All the granular TC IDs listed under "Original TC IDs absorbed" above that are NOT in the "kept as-is" rows.

Notable ranges to remove (the "ignored" tests you flagged — they're now part of merged tests):
- **TC_SC01 through TC_SC13** → now part of DoctorSupplyChainTest merged methods
- **TC_AN01 through TC_AN13** → now part of DoctorAnalyticsTest merged methods
- **TC_T01 through TC_T13** → now part of DoctorTelemedicineTest merged methods
- **TC_OV01 through TC_OV15** → now part of AdminOverviewTest merged methods
- **TC_A01 through TC_A14** → now part of DoctorAppointmentsTest merged methods

### C) Tests now run in regression suite (previously skipped)

All 76 methods that previously had no `groups = {...}` declaration now have `groups = {"regression"}`. This includes everything in the TC_OV*, TC_A*, TC_AN*, TC_SC*, TC_T* ranges, plus several others. They will now pass-or-fail in every regression run instead of being silently skipped.

---

## Compile status

`mvn test-compile` → **BUILD SUCCESS**. All 128 UI test methods compile without errors.

One pre-existing teammate-merge bug was fixed during this work: `AdminAppointmentsTest.Admin_AppointmentsValidation` referenced `page.filter`, `page.recentAppointments`, `page.app` — none of which exist on the AdminAppointments page object. Replaced with the correct existing locators (`filtersHeading`, `recentApptsHeading`, `allApptsHeading`).
