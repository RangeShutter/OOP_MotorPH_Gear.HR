package util;

import model.Employee;

import java.util.regex.Pattern;

/**
 * Format validation for employee profile fields (Employee Profile Validation Plan).
 * Each method returns null if valid, or an error message describing the parameter and required format.
 */
public final class EmployeeValidationUtil {

    private static final Pattern EMPLOYEE_NUMBER = Pattern.compile("^[0-9]{4}$");
    private static final Pattern SSS = Pattern.compile("^[0-9]{2}-[0-9]{7}-[0-9]{1}$");
    private static final Pattern PHIL_HEALTH = Pattern.compile("^[0-9]{12}$");
    private static final Pattern TIN = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{3}-[0-9]{3}$");
    private static final Pattern PAG_IBIG = Pattern.compile("^[0-9]{12}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");
    private static final Pattern PHONE = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{3}$");

    private EmployeeValidationUtil() {}

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validateEmployeeNumber(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "Employee Number is required. Must be exactly 4 digits (e.g. 1001).";
        if (!EMPLOYEE_NUMBER.matcher(s).matches()) return "Employee Number must be exactly 4 digits (e.g. 1001).";
        return null;
    }

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validateSss(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "SSS is required. Must be in format 12-2345679-1 (2 digits, hyphen, 7 digits, hyphen, 1 digit).";
        if (!SSS.matcher(s).matches()) return "SSS must be in format 12-2345679-1 (2 digits, hyphen, 7 digits, hyphen, 1 digit).";
        return null;
    }

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validatePhilHealth(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "PhilHealth is required. Must be exactly 12 digits (e.g. 123456789012).";
        if (!PHIL_HEALTH.matcher(s).matches()) return "PhilHealth must be exactly 12 digits (e.g. 123456789012).";
        return null;
    }

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validateTin(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "TIN is required. Must be 12 digits in format 000-000-000-000 (e.g. 123-456-789-000).";
        if (!TIN.matcher(s).matches()) return "TIN must be 12 digits in format 000-000-000-000 (e.g. 123-456-789-000).";
        return null;
    }

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validatePagIbig(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "Pag-ibig is required. Must be exactly 12 digits (e.g. 123456789012).";
        if (!PAG_IBIG.matcher(s).matches()) return "Pag-ibig must be exactly 12 digits (e.g. 123456789012).";
        return null;
    }

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validateEmail(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "Email is required. Use a valid email format (e.g. name@example.com).";
        if (!EMAIL.matcher(s).matches()) return "Email must be a valid email format (e.g. name@example.com).";
        return null;
    }

    /** Returns null if valid; otherwise error message with parameter and required format. */
    public static String validatePhone(String value) {
        String s = value != null ? value.trim() : "";
        if (s.isEmpty()) return "Phone is required. Must be 9 digits in format 000-000-000 (e.g. 555-123-456).";
        if (!PHONE.matcher(s).matches()) return "Phone must be 9 digits in format 000-000-000 (e.g. 555-123-456).";
        return null;
    }

    /**
     * Validates all required and format fields of an employee.
     * Returns null if the employee is valid; otherwise the first validation error message.
     * Used by the model layer (Employee.isValid) and for consistent validation at persistence boundaries.
     */
    public static String validateEmployee(Employee emp) {
        if (emp == null) return "Employee is required.";
        String err = validateEmployeeNumber(emp.getEmployeeNumber());
        if (err != null) return err;
        err = validateSss(emp.getSssNumber());
        if (err != null) return err;
        err = validatePhilHealth(emp.getPhilHealthNumber());
        if (err != null) return err;
        err = validateTin(emp.getTin());
        if (err != null) return err;
        err = validatePagIbig(emp.getPagIbigNumber());
        if (err != null) return err;
        err = validateEmail(emp.getEmail());
        if (err != null) return err;
        err = validatePhone(emp.getPhone());
        if (err != null) return err;
        return null;
    }
}
