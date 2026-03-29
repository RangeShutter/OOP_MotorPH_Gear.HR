package util;

import model.UserCredential;

import java.util.regex.Pattern;

/**
 * [ENCAPSULATION] Utility class; validates credentials via static helpers and private constructor.
 */
public final class CredentialValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    /** [ENCAPSULATION] Prevents instantiation. */
    private CredentialValidationUtil() {}

    public static String validateEmail(String email) {
        String value = email != null ? email.trim() : "";
        if (value.isEmpty()) {
            return "Email is required.";
        }
        if (!EMAIL.matcher(value).matches()) {
            return "Email must be a valid format (e.g. name@example.com).";
        }
        return null;
    }

    public static String validateRole(String role) {
        String value = role != null ? role.trim() : "";
        if (value.isEmpty()) {
            return "Role is required.";
        }
        return null;
    }

    public static String validateUserCredential(UserCredential credential) {
        if (credential == null) return "Credential is required.";
        if (credential.getUserId() == null || credential.getUserId().trim().isEmpty()) {
            return "User ID is required.";
        }
        String emailError = validateEmail(credential.getEmail());
        if (emailError != null) return emailError;
        String roleError = validateRole(credential.getRole());
        if (roleError != null) return roleError;
        return null;
    }
}
