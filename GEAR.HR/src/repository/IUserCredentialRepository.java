package repository;

import model.UserCredential;

import java.util.List;

/**
 * [INTERFACE] Contract for loading and saving user credentials.
 * Implementations (e.g. CSV) can be swapped for testing or different storage.
 */
public interface IUserCredentialRepository {
    /** [INTERFACE] Loads all credentials from storage. */
    List<UserCredential> load();

    /** [INTERFACE] Persists the given credential list to storage. */
    void save(List<UserCredential> credentials);
}
