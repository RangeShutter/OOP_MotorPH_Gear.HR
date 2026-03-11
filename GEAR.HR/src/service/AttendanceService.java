package service;

import model.AttendanceRecord;
import repository.IAttendanceRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages attendance records; persistence delegated to repository (OOP redesign - GEAR.HR).
 * [INTERFACE] Implements IAttendanceService.
 * [POLYMORPHISM] Can be used as IAttendanceService by callers; depends on IAttendanceRepository.
 */
public class AttendanceService implements IAttendanceService {
    /** [POLYMORPHISM] Holds any implementation of IAttendanceRepository. */
    private final IAttendanceRepository repository;
    private final Map<String, AttendanceRecord> attendanceRecords = new LinkedHashMap<>();

    public AttendanceService(IAttendanceRepository repository) {
        this.repository = repository;
        for (AttendanceRecord r : repository.load()) {
            String key = r.getEmployeeId() + "|" + r.getDate();
            attendanceRecords.put(key, r);
        }
    }

    /** [INTERFACE] Implements IAttendanceService.loadAttendanceRecordsFromCSV. */
    @Override
    public void loadAttendanceRecordsFromCSV() {
        attendanceRecords.clear();
        for (AttendanceRecord r : repository.load()) {
            String key = r.getEmployeeId() + "|" + r.getDate();
            attendanceRecords.put(key, r);
        }
    }

    private void save() {
        repository.save(new ArrayList<>(attendanceRecords.values()));
    }

    /** [INTERFACE] Implements IAttendanceService.getAllRecords. */
    @Override
    public List<AttendanceRecord> getAllRecords() {
        return new ArrayList<>(attendanceRecords.values());
    }

    /** [INTERFACE] Implements IAttendanceService.hasRecord. */
    @Override
    public boolean hasRecord(String employeeId, String date) {
        return attendanceRecords.containsKey(employeeId + "|" + date);
    }

    /** [INTERFACE] Implements IAttendanceService.addRecord. Rejects invalid records (Validatable.isValid). */
    @Override
    public void addRecord(AttendanceRecord record) {
        if (record == null || !record.isValid()) return;
        String key = record.getEmployeeId() + "|" + record.getDate();
        attendanceRecords.put(key, record);
        save();
    }

    /** [INTERFACE] Implements IAttendanceService.removeAttendanceRecords. */
    @Override
    public void removeAttendanceRecords(String employeeId) {
        attendanceRecords.entrySet().removeIf(entry -> entry.getKey().startsWith(employeeId + "|"));
        save();
    }

    /** [INTERFACE] Implements IAttendanceService.clearAll. */
    @Override
    public void clearAll() {
        attendanceRecords.clear();
        save();
    }
}
