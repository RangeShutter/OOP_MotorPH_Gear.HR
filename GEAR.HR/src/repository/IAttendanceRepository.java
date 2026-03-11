package repository;

import model.AttendanceRecord;

import java.util.List;

/**
 * [INTERFACE] Contract for loading and saving attendance records.
 * Implementations (e.g. CSV) can be swapped for testing or different storage.
 */
public interface IAttendanceRepository {
    /**
     * [INTERFACE] Loads all attendance records from storage.
     * @return list of records (empty if none or error)
     */
    List<AttendanceRecord> load();

    /**
     * [INTERFACE] Saves the given list of attendance records to storage.
     * @param records list to save (null is ignored)
     */
    void save(List<AttendanceRecord> records);
}
