package service;

import repository.IAttendanceRepository;
import repository.IEmployeeRepository;
import repository.ILeaveRequestRepository;
import repository.IPayrollRepository;
import repository.IUserCredentialRepository;
import repository.IItTicketRepository;
import repository.AttendanceRepository;
import repository.EmployeeRepository;
import repository.LeaveRequestRepository;
import repository.PayrollRepository;
import repository.UserCredentialRepository;
import model.ItTicket;
import java.util.ArrayList;
import java.util.List;

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
    private final IUserCredentialRepository userCredentialRepository = new UserCredentialRepository();
    private final IItTicketRepository itTicketRepository = createItTicketRepository();

    private final IAuthenticationService authenticationService = new AuthenticationService();
    /** [POLYMORPHISM] Services typed as service interfaces. Credential service before employee service for DI. */
    private final IUserCredentialService userCredentialService = new UserCredentialService(userCredentialRepository, authenticationService);
    private final IEmployeeService employeeService = new EmployeeService(employeeRepository, userCredentialService);
    private final IAttendanceService attendanceService = new AttendanceService(attendanceRepository);
    private final PayrollProcessor payrollProcessor = new PayrollProcessor(payrollRepository, attendanceService);
    private final ILeaveService leaveService = new LeaveService(leaveRequestRepository);
    private final IItTicketService itTicketService = new ItTicketService(itTicketRepository, authenticationService);

    /** [INTERFACE][POLYMORPHISM] Exposes authentication via interface contract. */
    public IAuthenticationService getAuthenticationService() {
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

    /** [POLYMORPHISM] Exposes PayrollProcessor for payroll UI and services. */
    public PayrollProcessor getPayrollProcessor() {
        return payrollProcessor;
    }

    /** [POLYMORPHISM] Exposes ILeaveService so callers can use abstraction. */
    public ILeaveService getLeaveService() {
        return leaveService;
    }

    /** [POLYMORPHISM] Exposes IUserCredentialService so callers can use abstraction. */
    public IUserCredentialService getUserCredentialService() {
        return userCredentialService;
    }

    /** [POLYMORPHISM] Exposes IItTicketService so callers can use abstraction. */
    public IItTicketService getItTicketService() {
        return itTicketService;
    }

    /**
     * [ABSTRACTION] Resolves IT ticket repository implementation without hard compile dependency.
     * [POLYMORPHISM] Fallback is an anonymous {@link IItTicketRepository} (empty load, no-op save).
     */
    private static IItTicketRepository createItTicketRepository() {
        try {
            Class<?> repoClass = Class.forName("repository.ItTicketRepository");
            Object instance = repoClass.getDeclaredConstructor().newInstance();
            if (instance instanceof IItTicketRepository) {
                return (IItTicketRepository) instance;
            }
        } catch (Exception ignored) {
            // fallback below
        }
        return new IItTicketRepository() {
            /** [INTERFACE] Implements IItTicketRepository.load (no-op empty list). */
            @Override
            public List<ItTicket> load() {
                return new ArrayList<>();
            }

            /** [INTERFACE] Implements IItTicketRepository.save (no-op). */
            @Override
            public void save(List<ItTicket> items) {
                // no-op fallback
            }
        };
    }
}
