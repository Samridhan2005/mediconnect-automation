package com.cts.mfrp.mediconnect.constants;

public final class AppConstants {

    private AppConstants() {
    }

    public static final class Endpoints {
        public static final String AUTH_REGISTER = "/api/auth/register";
        public static final String AUTH_LOGIN    = "/api/auth/login";

        public static final String HOSPITALS         = "/api/hospitals";
        public static final String HOSPITAL_BY_ID    = "/api/hospitals/{id}";

        public static final String PATIENTS          = "/api/patients";
        public static final String PATIENT_BY_ID     = "/api/patients/{id}";

        public static final String DOCTORS                  = "/api/doctors";
        public static final String DOCTOR_BY_ID             = "/api/doctors/{id}";
        public static final String DOCTORS_BY_HOSPITAL      = "/api/doctors/hospital/{hospitalId}";
        public static final String DOCTORS_BY_SPECIALIZATION = "/api/doctors/specialization/{spec}";

        public static final String APPOINTMENTS              = "/api/appointments";
        public static final String APPOINTMENT_BY_ID         = "/api/appointments/{id}";
        public static final String APPOINTMENTS_BY_PATIENT   = "/api/appointments/patient/{patientId}";
        public static final String APPOINTMENTS_BY_DOCTOR    = "/api/appointments/doctor/{doctorId}";

        public static final String MEDICAL_RECORDS            = "/api/medical-records";
        public static final String MEDICAL_RECORD_BY_ID       = "/api/medical-records/{id}";
        public static final String MEDICAL_RECORDS_BY_PATIENT = "/api/medical-records/patient/{patientId}";

        public static final String LAB_REPORTS              = "/api/lab-reports";
        public static final String LAB_REPORT_BY_ID         = "/api/lab-reports/{id}";
        public static final String LAB_REPORTS_BY_PATIENT   = "/api/lab-reports/patient/{patientId}";

        public static final String BEDS               = "/api/beds";
        public static final String BED_BY_ID          = "/api/beds/{id}";
        public static final String BEDS_BY_HOSPITAL   = "/api/beds/hospital/{hospitalId}";
        public static final String BEDS_BY_STATUS     = "/api/beds/status/{status}";

        public static final String INVENTORY              = "/api/inventory";
        public static final String INVENTORY_BY_ID        = "/api/inventory/{id}";
        public static final String INVENTORY_BY_HOSPITAL  = "/api/inventory/hospital/{hospitalId}";

        public static final String NOTIFICATIONS                  = "/api/notifications";
        public static final String NOTIFICATION_BY_ID             = "/api/notifications/{id}";
        public static final String NOTIFICATIONS_BY_USER          = "/api/notifications/user/{userId}";
        public static final String NOTIFICATIONS_BY_USER_UNREAD   = "/api/notifications/user/{userId}/unread";
    }

    public static final class Roles {
        public static final String PATIENT = "PATIENT";
        public static final String DOCTOR  = "DOCTOR";
        public static final String ADMIN   = "ADMIN";
    }

    public static final class AppointmentStatus {
        public static final String SCHEDULED = "SCHEDULED";
        public static final String CANCELLED = "CANCELLED";
        public static final String COMPLETED = "COMPLETED";
    }

    public static final class AppointmentType {
        public static final String IN_PERSON = "IN_PERSON";
        public static final String ONLINE    = "ONLINE";
    }

    public static final class BedStatus {
        public static final String AVAILABLE = "AVAILABLE";
        public static final String OCCUPIED  = "OCCUPIED";
    }

    public static final class DoctorAvailability {
        public static final String AVAILABLE     = "AVAILABLE";
        public static final String NOT_AVAILABLE = "NOT_AVAILABLE";
    }
}
