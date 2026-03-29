package service;

import model.Employee;

/**
 * [INTERFACE][ABSTRACTION] Authentication contract used by callers.
 */
public interface IAuthenticationService {
    /**
     * [ABSTRACTION] Reload credential storage into memory.
     */
    void loadUserCredentials();

    /**
     * [ABSTRACTION] Authenticate a user; returns employee identity when valid.
     */
    Employee authenticate(String userId, String password);

    /**
     * [ABSTRACTION] Returns role/email context for an existing user.
     */
    AuthContext getAuthContext(String userId);

    /**
     * [ABSTRACTION] Returns true when a user id exists in credentials.
     */
    boolean hasUser(String userId);
}
