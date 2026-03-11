package service;

import model.LeaveRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * [INTERFACE] Contract for leave request management: load and CRUD.
 * Implementations can be swapped for testing or different backends.
 */
public interface ILeaveService {
    /** [INTERFACE] Reloads leave requests from storage. */
    void loadLeaveRequestsFromCSV();
    /** [INTERFACE] Returns a copy of all leave requests. */
    List<LeaveRequest> getAllLeaveRequests();
    /** [INTERFACE] Returns leave requests for the given employee. */
    List<LeaveRequest> getLeaveRequestsByEmployee(String employeeId);
    /** [INTERFACE] Adds a leave request if valid. */
    void addLeaveRequest(LeaveRequest request);
    /** [INTERFACE] Updates status of the matching leave request. */
    void updateLeaveRequestStatus(String employeeId, LocalDate startDate, String newStatus);
    /** [INTERFACE] Returns true if the employee already has a leave request overlapping [start, end]. */
    boolean hasOverlappingLeaveRequest(String employeeId, LocalDate start, LocalDate end);
    /** [INTERFACE] Deletes the leave request matching employeeId and startDate. */
    void deleteLeaveRequest(String employeeId, LocalDate startDate);
}
