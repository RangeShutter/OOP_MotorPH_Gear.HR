package service;

import model.UserCredential;

import java.util.List;
import java.util.Set;

/**
 * [INTERFACE] Contract for credential administration: reload, query, and update accounts.
 */
public interface IUserCredentialService {
    /** [INTERFACE] Reloads credentials from the repository into memory. */
    void reload();

    /** [INTERFACE] Returns a copy of all credentials. */
    List<UserCredential> getAllCredentials();

    /** [INTERFACE] Returns distinct non-empty role strings from loaded credentials. */
    Set<String> getDistinctRoles();

    /** [INTERFACE] Finds a credential by user id, or null if missing. */
    UserCredential findByUserId(String userId);

    /** [INTERFACE] Updates password/role/email; returns null on success or an error message. */
    String updateCredential(String userId, String password, String role, String email);

    /** [INTERFACE] Upserts login for a new hire: default password, role and email from employee fields. */
    String upsertCredentialForNewEmployee(String userId, String email, String role);

    /** [INTERFACE] Syncs email and role from employee; preserves password if row exists; otherwise inserts with default password. */
    String patchCredentialFromEmployee(String userId, String email, String role);

    /** [INTERFACE] Removes the credential row for the given user id, if present. */
    void deleteCredentialByUserId(String userId);
}
