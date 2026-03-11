package service;

import model.LeaveRequest;
import repository.ILeaveRequestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages leave requests; persistence delegated to repository (OOP redesign - GEAR.HR).
 * [INTERFACE] Implements ILeaveService.
 * [POLYMORPHISM] Can be used as ILeaveService by callers; depends on ILeaveRequestRepository.
 */
public class LeaveService implements ILeaveService {
    /** [POLYMORPHISM] Holds any implementation of ILeaveRequestRepository. */
    private final ILeaveRequestRepository repository;
    private final List<LeaveRequest> leaveRequests = new ArrayList<>();

    public LeaveService(ILeaveRequestRepository repository) {
        this.repository = repository;
        leaveRequests.addAll(repository.load());
    }

    /** [INTERFACE] Implements ILeaveService.loadLeaveRequestsFromCSV. */
    @Override
    public void loadLeaveRequestsFromCSV() {
        leaveRequests.clear();
        leaveRequests.addAll(repository.load());
    }

    private void save() {
        repository.save(new ArrayList<>(leaveRequests));
    }

    /** [INTERFACE] Implements ILeaveService.getAllLeaveRequests. */
    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    /** [INTERFACE] Implements ILeaveService.getLeaveRequestsByEmployee. */
    @Override
    public List<LeaveRequest> getLeaveRequestsByEmployee(String employeeId) {
        return leaveRequests.stream()
            .filter(lr -> employeeId != null && employeeId.equals(lr.getEmployeeId()))
            .collect(Collectors.toList());
    }

    /** [INTERFACE] Implements ILeaveService.addLeaveRequest. Rejects invalid or overlapping requests. */
    @Override
    public void addLeaveRequest(LeaveRequest request) {
        if (request == null || !request.isValid()) return;
        if (hasOverlappingLeaveRequest(request.getEmployeeId(), request.getStartDate(), request.getEndDate())) return;
        leaveRequests.add(request);
        save();
    }

    /** [INTERFACE] Implements ILeaveService.updateLeaveRequestStatus. Only persists if the request remains valid. */
    @Override
    public void updateLeaveRequestStatus(String employeeId, LocalDate startDate, String newStatus) {
        for (LeaveRequest lr : leaveRequests) {
            if (employeeId.equals(lr.getEmployeeId()) && startDate.equals(lr.getStartDate())) {
                lr.setStatus(newStatus);
                if (lr.isValid()) save();
                return;
            }
        }
    }

    /** [INTERFACE] Implements ILeaveService.hasOverlappingLeaveRequest. */
    @Override
    public boolean hasOverlappingLeaveRequest(String employeeId, LocalDate start, LocalDate end) {
        if (employeeId == null || start == null || end == null) return false;
        LeaveRequest probe = new LeaveRequest(employeeId, start, end, "", LeaveRequest.STATUS_PENDING);
        for (LeaveRequest lr : leaveRequests) {
            if (employeeId.equals(lr.getEmployeeId()) && probe.overlapsWith(lr)) return true;
        }
        return false;
    }

    /** [INTERFACE] Implements ILeaveService.deleteLeaveRequest. */
    @Override
    public void deleteLeaveRequest(String employeeId, LocalDate startDate) {
        leaveRequests.removeIf(lr ->
            employeeId != null && employeeId.equals(lr.getEmployeeId())
                && startDate != null && startDate.equals(lr.getStartDate()));
        save();
    }
}
