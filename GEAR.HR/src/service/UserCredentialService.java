package service;

import model.UserCredential;
import repository.IUserCredentialRepository;
import util.CredentialValidationUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * [INTERFACE][POLYMORPHISM] Service implementation for credential administration.
 * [ENCAPSULATION] Owns loaded credential collection and persistence lifecycle.
 */
public class UserCredentialService implements IUserCredentialService {
    private static final String DEFAULT_NEW_EMPLOYEE_PASSWORD = "MotorPH123";

    private final IUserCredentialRepository repository;
    private final IAuthenticationService authenticationService;
    private final List<UserCredential> credentials = new ArrayList<>();

    /** [INTERFACE][POLYMORPHISM] Depends on authentication abstraction, not concrete service. */
    public UserCredentialService(IUserCredentialRepository repository, IAuthenticationService authenticationService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        reload();
    }

    /** [INTERFACE] Implements IUserCredentialService.reload. */
    @Override
    public void reload() {
        credentials.clear();
        credentials.addAll(repository.load());
    }

    /** [INTERFACE] Implements IUserCredentialService.getAllCredentials. */
    @Override
    public List<UserCredential> getAllCredentials() {
        return new ArrayList<>(credentials);
    }

    /** [INTERFACE] Implements IUserCredentialService.getDistinctRoles. */
    @Override
    public Set<String> getDistinctRoles() {
        Set<String> roles = new LinkedHashSet<>();
        for (UserCredential credential : credentials) {
            if (credential != null && credential.getRole() != null && !credential.getRole().trim().isEmpty()) {
                roles.add(credential.getRole().trim());
            }
        }
        return roles;
    }

    /** [INTERFACE] Implements IUserCredentialService.findByUserId. */
    @Override
    public UserCredential findByUserId(String userId) {
        if (userId == null) return null;
        String key = userId.trim();
        for (UserCredential credential : credentials) {
            if (credential != null && key.equals(credential.getUserId())) {
                return credential;
            }
        }
        return null;
    }

    /** [INTERFACE] Implements IUserCredentialService.updateCredential. */
    @Override
    public String updateCredential(String userId, String password, String role, String email) {
        UserCredential existing = findByUserId(userId);
        if (existing == null) {
            return "User ID not found.";
        }

        String emailError = CredentialValidationUtil.validateEmail(email);
        if (emailError != null) return emailError;

        String roleError = CredentialValidationUtil.validateRole(role);
        if (roleError != null) return roleError;

        existing.setPassword(password);
        existing.setRole(role);
        existing.setEmail(email);

        String validationError = existing.getValidationError();
        if (validationError != null) return validationError;

        persistAndReloadAuth();
        return null;
    }

    /** [INTERFACE] Implements IUserCredentialService.upsertCredentialForNewEmployee. */
    @Override
    public String upsertCredentialForNewEmployee(String userId, String email, String role) {
        String idError = validateUserId(userId);
        if (idError != null) return idError;

        String emailError = CredentialValidationUtil.validateEmail(email);
        if (emailError != null) return emailError;

        String roleError = CredentialValidationUtil.validateRole(role);
        if (roleError != null) return roleError;

        String key = userId.trim();
        UserCredential existing = findByUserId(key);
        if (existing == null) {
            UserCredential created = new UserCredential(key, DEFAULT_NEW_EMPLOYEE_PASSWORD, role, email);
            String validationError = created.getValidationError();
            if (validationError != null) return validationError;
            credentials.add(created);
        } else {
            existing.setRole(role);
            existing.setEmail(email);
            existing.setPassword(DEFAULT_NEW_EMPLOYEE_PASSWORD);
            String validationError = existing.getValidationError();
            if (validationError != null) return validationError;
        }

        persistAndReloadAuth();
        return null;
    }

    /** [INTERFACE] Implements IUserCredentialService.patchCredentialFromEmployee. */
    @Override
    public String patchCredentialFromEmployee(String userId, String email, String role) {
        String idError = validateUserId(userId);
        if (idError != null) return idError;

        String emailError = CredentialValidationUtil.validateEmail(email);
        if (emailError != null) return emailError;

        String roleError = CredentialValidationUtil.validateRole(role);
        if (roleError != null) return roleError;

        String key = userId.trim();
        UserCredential existing = findByUserId(key);
        if (existing != null) {
            existing.setRole(role);
            existing.setEmail(email);
            String validationError = existing.getValidationError();
            if (validationError != null) return validationError;
        } else {
            UserCredential created = new UserCredential(key, DEFAULT_NEW_EMPLOYEE_PASSWORD, role, email);
            String validationError = created.getValidationError();
            if (validationError != null) return validationError;
            credentials.add(created);
        }

        persistAndReloadAuth();
        return null;
    }

    /** [INTERFACE] Implements IUserCredentialService.deleteCredentialByUserId. */
    @Override
    public void deleteCredentialByUserId(String userId) {
        if (userId == null) return;
        String key = userId.trim();
        if (key.isEmpty()) return;
        boolean removed = credentials.removeIf(c -> c != null && key.equals(c.getUserId()));
        if (removed) {
            persistAndReloadAuth();
        }
    }

    private String validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return "User ID is required.";
        }
        return null;
    }

    private void persistAndReloadAuth() {
        repository.save(credentials);
        if (authenticationService != null) {
            authenticationService.loadUserCredentials();
        }
    }
}
