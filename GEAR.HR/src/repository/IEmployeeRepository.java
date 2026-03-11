package repository;

import model.Employee;

import java.util.List;

/**
 * [INTERFACE] Contract for loading and saving employee data.
 * Implementations (e.g. CSV) can be swapped for testing or different storage.
 */
public interface IEmployeeRepository {
    /**
     * [INTERFACE] Loads all employees from storage.
     * @return list of employees (empty if none or error)
     */
    List<Employee> load();

    /**
     * [INTERFACE] Saves the given list of employees to storage.
     * @param employees list to save (null is ignored)
     */
    void save(List<Employee> employees);
}
