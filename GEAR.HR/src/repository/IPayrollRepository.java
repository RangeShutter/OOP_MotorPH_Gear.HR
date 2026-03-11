package repository;

import model.PayrollData;

import java.util.Map;

/**
 * [INTERFACE] Contract for loading and saving payroll data (employeeId -> PayrollData).
 * Implementations (e.g. CSV) can be swapped for testing or different storage.
 */
public interface IPayrollRepository {
    /**
     * [INTERFACE] Loads all payroll data from storage.
     * @return map of employeeId to PayrollData (empty if none or error)
     */
    Map<String, PayrollData> load();

    /**
     * [INTERFACE] Saves the given payroll data map to storage.
     * @param data map to save (null is ignored)
     */
    void save(Map<String, PayrollData> data);
}
