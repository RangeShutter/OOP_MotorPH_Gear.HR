package service;

import model.AttendanceRecord;

import java.util.List;

/**
 * [INTERFACE] Contract for attendance record management: load and CRUD.
 * Implementations can be swapped for testing or different backends.
 */
public interface IAttendanceService {
    /** [INTERFACE] Reloads attendance records from storage. */
    void loadAttendanceRecordsFromCSV();
    /** [INTERFACE] Returns a copy of all attendance records. */
    List<AttendanceRecord> getAllRecords();
    /** [INTERFACE] Returns true if a record exists for the given employee and date. */
    boolean hasRecord(String employeeId, String date);
    /** [INTERFACE] Adds or replaces an attendance record. */
    void addRecord(AttendanceRecord record);
    /** [INTERFACE] Removes all attendance records for the given employee. */
    void removeAttendanceRecords(String employeeId);
    /** [INTERFACE] Removes all records and persists. */
    void clearAll();
}
