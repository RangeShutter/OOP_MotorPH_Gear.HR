package service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Role-based access: classifies user role string into HR, Payroll, IT/Admin, or Normal Employee group.
 * Used by Main and screens to show the correct homepage and enforce access restrictions.
 */
public enum RoleGroup {
    HR,
    PAYROLL,
    IT_ADMIN,
    NORMAL;

    private static final Set<String> HR_ROLES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "HR Manager",
        "HR Team Leader",
        "HR Rank and File"
    )));

    private static final Set<String> PAYROLL_ROLES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "Payroll Manager",
        "Payroll Team Leader",
        "Payroll Rank and File",
        "Account Team Leader",
        "Account Rank and File"
    )));

    private static final Set<String> IT_ADMIN_ROLES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "IT",
        "IT Operations and Systems"
    )));

    /**
     * Maps the role string (from user_credentials.csv) to the corresponding group.
     * All roles not in HR, Payroll, or IT/Admin sets are NORMAL.
     */
    public static RoleGroup fromRole(String role) {
        if (role == null) return NORMAL;
        String r = role.trim();
        if (HR_ROLES.contains(r)) return HR;
        if (PAYROLL_ROLES.contains(r)) return PAYROLL;
        if (IT_ADMIN_ROLES.contains(r)) return IT_ADMIN;
        return NORMAL;
    }
}
