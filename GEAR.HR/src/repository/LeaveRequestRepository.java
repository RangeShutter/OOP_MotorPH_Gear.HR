package repository;

import model.LeaveRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * CSV persistence for leave requests. Load/save only; no business logic.
 * [INTERFACE] Implements ILeaveRequestRepository.
 * [INHERITANCE] Extends AbstractCsvListRepository for shared CSV load/save; provides LeaveRequest-specific parse/serialize.
 * [POLYMORPHISM] Can be used as ILeaveRequestRepository by callers.
 */
public class LeaveRequestRepository extends AbstractCsvListRepository<LeaveRequest> implements ILeaveRequestRepository {
    private static final String FILE = "csv/leave_requests.csv";
    private static final String HEADER = "EmployeeID,StartDate,EndDate,Reason,Status";

    /** [ABSTRACTION] [INHERITANCE] Returns file path for leave requests CSV. */
    @Override
    protected String getFilePath() {
        return FILE;
    }

    /** [ABSTRACTION] [INHERITANCE] Returns CSV header line. */
    @Override
    protected String getHeader() {
        return HEADER;
    }

    /** [ABSTRACTION] [INHERITANCE] Parses one CSV row into a LeaveRequest; null if invalid. */
    @Override
    protected LeaveRequest parseLine(String[] parts) {
        if (parts == null || parts.length < 5) return null;
        LocalDate start = LeaveRequest.parseDate(parts[1]);
        LocalDate end = LeaveRequest.parseDate(parts[2]);
        if (start == null || end == null) return null;
        return new LeaveRequest(
            parts[0].trim(), start, end, parts[3].trim(), parts[4].trim()
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Converts LeaveRequest to CSV column values. */
    @Override
    protected String[] toCsvRow(LeaveRequest lr) {
        return new String[]{
            lr.getEmployeeId(),
            lr.getStartDate() != null ? lr.getStartDate().toString() : "",
            lr.getEndDate() != null ? lr.getEndDate().toString() : "",
            lr.getReason(),
            lr.getStatus()
        };
    }

    /** [INTERFACE] Implements ILeaveRequestRepository.load. [INHERITANCE] Delegates to AbstractCsvListRepository.load. */
    @Override
    public List<LeaveRequest> load() {
        return super.load();
    }

    /** [INTERFACE] Implements ILeaveRequestRepository.save. [INHERITANCE] Delegates to AbstractCsvListRepository.save. */
    @Override
    public void save(List<LeaveRequest> requests) {
        super.save(requests);
    }
}
