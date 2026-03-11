package repository;

import model.Employee;

import java.util.List;

/**
 * CSV persistence for employees (employees.csv). Load/save only; no business logic.
 * [INTERFACE] Implements IEmployeeRepository.
 * [INHERITANCE] Extends AbstractCsvListRepository for shared CSV load/save; provides Employee-specific parse/serialize.
 * [POLYMORPHISM] Can be used as IEmployeeRepository by callers.
 */
public class EmployeeRepository extends AbstractCsvListRepository<Employee> implements IEmployeeRepository {
    private static final String FILE = "csv/employees.csv";
    private static final String HEADER = "EmployeeNumber,LastName,FirstName,SSS,PhilHealth,TIN,PagIBIG,Email,Position,Address,Phone";

    /** [ABSTRACTION] [INHERITANCE] Overrides split to support quoted CSV fields. */
    @Override
    protected String[] splitLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    /** [ABSTRACTION] [INHERITANCE] Returns file path for employees CSV. */
    @Override
    protected String getFilePath() {
        return FILE;
    }

    /** [ABSTRACTION] [INHERITANCE] Returns CSV header line. */
    @Override
    protected String getHeader() {
        return HEADER;
    }

    /** [ABSTRACTION] [INHERITANCE] Parses one CSV row into an Employee; null if invalid. */
    @Override
    protected Employee parseLine(String[] parts) {
        if (parts == null || parts.length < 11) return null;
        return new Employee(
            parts[0], parts[1], parts[2], parts[3], parts[4],
            parts[5], parts[6], parts[7], parts[8], parts[9], parts[10]
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Converts Employee to CSV column values. */
    @Override
    protected String[] toCsvRow(Employee emp) {
        return new String[]{
            emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(),
            emp.getSssNumber(), emp.getPhilHealthNumber(), emp.getTin(),
            emp.getPagIbigNumber(), emp.getEmail(), emp.getPosition(),
            emp.getAddress(), emp.getPhone()
        };
    }

    /** [INTERFACE] Implements IEmployeeRepository.load. [INHERITANCE] Delegates to AbstractCsvListRepository.load. */
    @Override
    public List<Employee> load() {
        return super.load();
    }

    /** [INTERFACE] Implements IEmployeeRepository.save. [INHERITANCE] Delegates to AbstractCsvListRepository.save. */
    @Override
    public void save(List<Employee> employees) {
        super.save(employees);
    }
}
