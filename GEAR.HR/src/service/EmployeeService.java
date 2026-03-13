package service;

import model.Employee;
import repository.IEmployeeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages employee data; persistence delegated to repository (OOP redesign - GEAR.HR).
 * [INTERFACE] Implements IEmployeeService.
 * [POLYMORPHISM] Can be used as IEmployeeService by callers; depends on IEmployeeRepository.
 */
public class EmployeeService implements IEmployeeService {
    /** [POLYMORPHISM] Holds any implementation of IEmployeeRepository. */
    private final IEmployeeRepository repository;
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService(IEmployeeRepository repository) {
        this.repository = repository;
        List<Employee> loaded = repository.load();
        employees.addAll(loaded);
    
    }

    /** [INTERFACE] Implements IEmployeeService.loadEmployeesFromCSV. */
    @Override
    public void loadEmployeesFromCSV() {
        employees.clear();
        employees.addAll(repository.load());
    }

    /** [INTERFACE] Implements IEmployeeService.saveEmployeesToCSV. */
    @Override
    public void saveEmployeesToCSV() {
        repository.save(new ArrayList<>(employees));
    }


    /** [INTERFACE] Implements IEmployeeService.getAllEmployees. */
    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeById. */
    @Override
    public Employee findEmployeeById(String empNumber) {
        if (empNumber == null) return null;
        String key = empNumber.trim();
        for (Employee emp : employees) {
            if (key.equals(emp.getEmployeeNumber() != null ? emp.getEmployeeNumber().trim() : "")) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeBySss. */
    @Override
    public Employee findEmployeeBySss(String sss) {
        if (sss == null) return null;
        String key = sss.trim();
        for (Employee emp : employees) {
            String v = emp.getSssNumber();
            if (v != null && v.trim().equals(key)) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeByPhilHealth. */
    @Override
    public Employee findEmployeeByPhilHealth(String philHealth) {
        if (philHealth == null) return null;
        String key = philHealth.trim();
        for (Employee emp : employees) {
            String v = emp.getPhilHealthNumber();
            if (v != null && v.trim().equals(key)) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeByTin. */
    @Override
    public Employee findEmployeeByTin(String tin) {
        if (tin == null) return null;
        String key = tin.trim();
        for (Employee emp : employees) {
            String v = emp.getTin();
            if (v != null && v.trim().equals(key)) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeByPagIbig. */
    @Override
    public Employee findEmployeeByPagIbig(String pagIbig) {
        if (pagIbig == null) return null;
        String key = pagIbig.trim();
        for (Employee emp : employees) {
            String v = emp.getPagIbigNumber();
            if (v != null && v.trim().equals(key)) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeByEmail. */
    @Override
    public Employee findEmployeeByEmail(String email) {
        if (email == null) return null;
        String key = email.trim();
        for (Employee emp : employees) {
            String v = emp.getEmail();
            if (v != null && v.trim().equals(key)) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.findEmployeeByPhone. */
    @Override
    public Employee findEmployeeByPhone(String phone) {
        if (phone == null) return null;
        String key = phone.trim();
        for (Employee emp : employees) {
            String v = emp.getPhone();
            if (v != null && v.trim().equals(key)) return emp;
        }
        return null;
    }

    /** [INTERFACE] Implements IEmployeeService.addEmployee. Rejects invalid entities (Validatable.isValid). */
    @Override
    public void addEmployee(Employee emp) {
        if (emp == null || !emp.isValid()) return;
        if (findEmployeeById(emp.getEmployeeNumber()) != null) return;
        employees.add(emp);
        saveEmployeesToCSV();
    }

    /** [INTERFACE] Implements IEmployeeService.updateEmployee. Rejects updates that would leave the entity invalid. */
    @Override
    public void updateEmployee(Employee emp) {
        if (emp == null) return;
        Employee existing = findEmployeeById(emp.getEmployeeNumber());
        if (existing == null) return;
        existing.setLastName(emp.getLastName());
        existing.setFirstName(emp.getFirstName());
        existing.setSssNumber(emp.getSssNumber());
        existing.setPhilHealthNumber(emp.getPhilHealthNumber());
        existing.setTin(emp.getTin());
        existing.setPagIbigNumber(emp.getPagIbigNumber());
        existing.setEmail(emp.getEmail());
        existing.setPosition(emp.getPosition());
        existing.setAddress(emp.getAddress());
        existing.setPhone(emp.getPhone());
        if (!existing.isValid()) return;
        saveEmployeesToCSV();
    }

    /** [INTERFACE] Implements IEmployeeService.deleteEmployee. */
    @Override
    public void deleteEmployee(String empNumber) {
        employees.removeIf(e -> empNumber != null && empNumber.equals(e.getEmployeeNumber()));
        saveEmployeesToCSV();
    }
}
