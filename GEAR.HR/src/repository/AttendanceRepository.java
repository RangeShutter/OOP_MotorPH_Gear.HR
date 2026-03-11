package repository;

import model.AttendanceRecord;

import java.util.List;

/**
 * CSV persistence for attendance records. Load/save only; no business logic.
 * [INTERFACE] Implements IAttendanceRepository.
 * [INHERITANCE] Extends AbstractCsvListRepository for shared CSV load/save; provides AttendanceRecord-specific parse/serialize.
 * [POLYMORPHISM] Can be used as IAttendanceRepository by callers.
 */
public class AttendanceRepository extends AbstractCsvListRepository<AttendanceRecord> implements IAttendanceRepository {
    private static final String FILE = "csv/attendance_records.csv";
    private static final String HEADER = "EmployeeID,Date,Status,TimeIn,TimeOut";

    /** [ABSTRACTION] [INHERITANCE] Returns file path for attendance CSV. */
    @Override
    protected String getFilePath() {
        return FILE;
    }

    /** [ABSTRACTION] [INHERITANCE] Returns CSV header line. */
    @Override
    protected String getHeader() {
        return HEADER;
    }

    /** [ABSTRACTION] [INHERITANCE] Parses one CSV row into an AttendanceRecord; null if invalid. */
    @Override
    protected AttendanceRecord parseLine(String[] parts) {
        if (parts == null || parts.length < 5) return null;
        return new AttendanceRecord(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    /** [ABSTRACTION] [INHERITANCE] Converts AttendanceRecord to CSV column values. */
    @Override
    protected String[] toCsvRow(AttendanceRecord r) {
        return new String[]{
            r.getEmployeeId(), r.getDate(), r.getStatus(),
            r.getTimeIn(), r.getTimeOut()
        };
    }

    /** [INTERFACE] Implements IAttendanceRepository.load. [INHERITANCE] Delegates to AbstractCsvListRepository.load. */
    @Override
    public List<AttendanceRecord> load() {
        return super.load();
    }

    /** [INTERFACE] Implements IAttendanceRepository.save. [INHERITANCE] Delegates to AbstractCsvListRepository.save. */
    @Override
    public void save(List<AttendanceRecord> records) {
        super.save(records);
    }
}
