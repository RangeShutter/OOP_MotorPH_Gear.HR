package service;

import repository.IAttendanceRepository;
import repository.IEmployeeRepository;
import repository.ILeaveRequestRepository;
import repository.IPayrollRepository;
import repository.AttendanceRepository;
import repository.EmployeeRepository;
import repository.LeaveRequestRepository;
import repository.PayrollRepository;

/**
 * Composition root: creates repositories and services; passed into UI (DI).
 * [POLYMORPHISM] Stores and exposes interface types so callers can depend on abstractions.
 */
public class ApplicationContext {
    /** [POLYMORPHISM] Concrete instances typed as repository interfaces. */
    private final IEmployeeRepository employeeRepository = new EmployeeRepository();
    private final IAttendanceRepository attendanceRepository = new AttendanceRepository();
    private final IPayrollRepository payrollRepository = new PayrollRepository();
    private final ILeaveRequestRepository leaveRequestRepository = new LeaveRequestRepository();

    private final AuthenticationService authenticationService = new AuthenticationService();
    /** [POLYMORPHISM] Services typed as service interfaces. */
    private final IEmployeeService employeeService = new EmployeeService(employeeRepository);
    private final IAttendanceService attendanceService = new AttendanceService(attendanceRepository);
    private final PayrollProcessor payrollProcessor = new PayrollProcessor(payrollRepository);
    private final ILeaveService leaveService = new LeaveService(leaveRequestRepository);

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /** [POLYMORPHISM] Exposes IEmployeeService so callers can use abstraction. */
    public IEmployeeService getEmployeeService() {
        return employeeService;
    }

    /** [POLYMORPHISM] Exposes IAttendanceService so callers can use abstraction. */
    public IAttendanceService getAttendanceService() {
        return attendanceService;
    }

    public PayrollProcessor getPayrollProcessor() {
        return payrollProcessor;
    }

    /** [POLYMORPHISM] Exposes ILeaveService so callers can use abstraction. */
    public ILeaveService getLeaveService() {
        return leaveService;
    }
}
