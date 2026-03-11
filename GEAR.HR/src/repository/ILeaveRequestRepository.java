package repository;

import model.LeaveRequest;

import java.util.List;

/**
 * [INTERFACE] Contract for loading and saving leave requests.
 * Implementations (e.g. CSV) can be swapped for testing or different storage.
 */
public interface ILeaveRequestRepository {
    /**
     * [INTERFACE] Loads all leave requests from storage.
     * @return list of requests (empty if none or error)
     */
    List<LeaveRequest> load();

    /**
     * [INTERFACE] Saves the given list of leave requests to storage.
     * @param requests list to save (null is ignored)
     */
    void save(List<LeaveRequest> requests);
}
