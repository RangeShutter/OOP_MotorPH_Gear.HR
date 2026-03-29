package repository;

import model.UserCredential;

import java.util.List;

/**
 * CSV persistence for user credentials. Load/save only; no business logic.
 * [INTERFACE] Implements IUserCredentialRepository.
 * [INHERITANCE] Extends AbstractCsvListRepository for shared CSV load/save; provides UserCredential-specific parse/serialize.
 * [POLYMORPHISM] Can be used as IUserCredentialRepository by callers.
 */
public class UserCredentialRepository extends AbstractCsvListRepository<UserCredential> implements IUserCredentialRepository {
    private static final String FILE = "csv/user_credentials.csv";
    private static final String HEADER = "userId,password,role,email";

    /** [ABSTRACTION] [INHERITANCE] Returns file path for user credentials CSV. */
    @Override
    protected String getFilePath() {
        return FILE;
    }

    /** [ABSTRACTION] [INHERITANCE] Returns CSV header line. */
    @Override
    protected String getHeader() {
        return HEADER;
    }

    /** [ABSTRACTION] [INHERITANCE] Parses one CSV row into a UserCredential; null if invalid. */
    @Override
    protected UserCredential parseLine(String[] parts) {
        if (parts == null || parts.length < 4) return null;
        return new UserCredential(parts[0], parts[1], parts[2], parts[3]);
    }

    /** [ABSTRACTION] [INHERITANCE] Converts UserCredential to CSV column values. */
    @Override
    protected String[] toCsvRow(UserCredential item) {
        return new String[]{
            item.getUserId(),
            item.getPassword(),
            item.getRole(),
            item.getEmail()
        };
    }

    /** [INTERFACE] Implements IUserCredentialRepository.load. [INHERITANCE] Delegates to AbstractCsvListRepository.load. */
    @Override
    public List<UserCredential> load() {
        return super.load();
    }

    /** [INTERFACE] Implements IUserCredentialRepository.save. [INHERITANCE] Delegates to AbstractCsvListRepository.save. */
    @Override
    public void save(List<UserCredential> credentials) {
        super.save(credentials);
    }
}
