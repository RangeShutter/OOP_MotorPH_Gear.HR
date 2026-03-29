package service;

/**
 * [ENCAPSULATION] Immutable authentication context value object.
 */
public final class AuthContext {
    private final String role;
    private final String email;

    /**
     * [ENCAPSULATION] Constructor normalizes null values.
     */
    public AuthContext(String role, String email) {
        this.role = role != null ? role : "";
        this.email = email != null ? email : "";
    }

    /**
     * [ENCAPSULATION] Returns authenticated role.
     */
    public String getRole() {
        return role;
    }

    /**
     * [ENCAPSULATION] Returns authenticated email.
     */
    public String getEmail() {
        return email;
    }
}
