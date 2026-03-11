package service;

import model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles credential validation and role-based access (OOP redesign - GEAR.HR).
 * Loads user_credentials.csv; isolates auth logic from UI.
 */
public class AuthenticationService {
    // Primary location for credentials is now under csv/; fall back to legacy root file if needed.
    private static final String CREDENTIALS_CSV = "csv/user_credentials.csv";
    private static final String CREDENTIALS_CSV_ALT = "user_credentials.csv";

    private final Map<String, String[]> userCredentials = new HashMap<>();

    public AuthenticationService() {
        loadUserCredentials();
    }

    /**
     * Loads credentials from CSV. Tries CREDENTIALS_CSV then CREDENTIALS_CSV_ALT.
     */
    public void loadUserCredentials() {
        userCredentials.clear();
        Path path = Paths.get(CREDENTIALS_CSV);
        if (!Files.exists(path)) {
            path = Paths.get(CREDENTIALS_CSV_ALT);
        }
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 4) {
                    String userId = data[0].trim();
                    String password = data[1].trim();
                    String role = data[2].trim();
                    String email = data[3].trim();
                    userCredentials.put(userId, new String[]{password, role, email});
                } else if (data.length >= 2) {
                    String userId = data[0].trim();
                    String password = data[1].trim();
                    userCredentials.put(userId, new String[]{password, "", ""});
                }
            }
        } catch (IOException e) {
            // leave map empty on error
        }
    }

    /**
     * Validates credentials and returns an Employee (with employeeNumber set) if successful.
     */
    public Employee authenticate(String userId, String password) {
        if (userId == null || password == null) return null;
        String key = userId.trim();
        String[] cred = userCredentials.get(key);
        if (cred == null || cred.length < 1) return null;
        if (!password.trim().equals(cred[0])) return null;
        // cred[] is { password, role, email }
        return new Employee(key, "", "", "", "", "", "", cred.length > 2 ? cred[2] : "", cred.length > 1 ? cred[1] : "", "", "");
    }

    /**
     * Returns [role, email] for the given userId, or ["", ""] if not found.
     */
    public String[] getRoleAndEmail(String userId) {
        if (userId == null) return new String[]{"", ""};
        String[] cred = userCredentials.get(userId.trim());
        if (cred == null || cred.length < 2) return new String[]{"", ""};
        // cred[] is { password, role, email }
        String role = cred.length > 1 ? cred[1] : "";
        String email = cred.length > 2 ? cred[2] : "";
        return new String[]{role, email};
    }

    public boolean hasUser(String userId) {
        return userId != null && userCredentials.containsKey(userId.trim());
    }
}
