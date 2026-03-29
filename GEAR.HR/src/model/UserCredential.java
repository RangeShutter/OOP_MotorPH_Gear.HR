package model;

import util.CredentialValidationUtil;

/**
 * Domain model for user login credentials.
 * [INHERITANCE] Child class extends AbstractEntity.
 */
public class UserCredential extends AbstractEntity {
    private final String userId;
    private String password;
    private String role;
    private String email;

    public UserCredential(String userId, String password, String role, String email) {
        super(userId != null ? userId.trim() : "");
        this.userId = userId != null ? userId.trim() : "";
        this.password = password != null ? password : "";
        this.role = role != null ? role.trim() : "";
        this.email = email != null ? email.trim() : "";
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password != null ? password : this.password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null ? role.trim() : this.role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : this.email;
    }

    /** [INTERFACE] Implements Validatable.isValid. [INHERITANCE] Overrides AbstractEntity.isValid. */
    @Override
    public boolean isValid() {
        return getValidationError() == null;
    }

    public String getValidationError() {
        return CredentialValidationUtil.validateUserCredential(this);
    }
}
