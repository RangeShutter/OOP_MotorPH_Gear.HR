package service;

import model.Employee;

import java.util.List;

/**
 * [INTERFACE] Contract for employee management: load/save and CRUD.
 * Implementations can be swapped for testing or different backends.
 */
public interface IEmployeeService {
    /** [INTERFACE] Reloads employees from storage. */
    void loadEmployeesFromCSV();
    /** [INTERFACE] Persists current in-memory employees to storage. */
    void saveEmployeesToCSV();
    /** [INTERFACE] Returns a copy of all employees. */
    List<Employee> getAllEmployees();
    /** [INTERFACE] Finds employee by employee number. */
    Employee findEmployeeById(String empNumber);
    /** [INTERFACE] Finds employee by SSS number. */
    Employee findEmployeeBySss(String sss);
    /** [INTERFACE] Finds employee by PhilHealth number. */
    Employee findEmployeeByPhilHealth(String philHealth);
    /** [INTERFACE] Finds employee by TIN. */
    Employee findEmployeeByTin(String tin);
    /** [INTERFACE] Finds employee by Pag-IBIG number. */
    Employee findEmployeeByPagIbig(String pagIbig);
    /** [INTERFACE] Finds employee by email. */
    Employee findEmployeeByEmail(String email);
    /** [INTERFACE] Finds employee by phone. */
    Employee findEmployeeByPhone(String phone);
    /** [INTERFACE] Adds an employee if not already present. */
    void addEmployee(Employee emp);
    /** [INTERFACE] Updates existing employee by employee number. */
    void updateEmployee(Employee emp);
    /** [INTERFACE] Removes employee by employee number. */
    void deleteEmployee(String empNumber);
}
