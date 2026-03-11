package model;

/**
 * Domain model for a single attendance entry (OOP redesign - GEAR.HR).
 * [INHERITANCE] Child class extends AbstractEntity.
 * Validates time-out after time-in and non-negative hours worked.
 */
public class AttendanceRecord extends AbstractEntity {

    private final String date;
    private final String status;
    private final String timeIn;
    private final String timeOut;

    /** [INHERITANCE] Calls super(employeeId) to set AbstractEntity.entityId. */
    public AttendanceRecord(String employeeId, String date, String status, String timeIn, String timeOut) {
        super(employeeId != null ? employeeId : "");
        this.date = date != null ? date : "";
        this.status = status != null ? status : "";
        this.timeIn = timeIn != null ? timeIn : "";
        this.timeOut = timeOut != null ? timeOut : "";
    }

    /** [INTERFACE] Identifiable: entity id is employeeId. */
    public String getEmployeeId() { return getId(); }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getTimeIn() { return timeIn; }
    public String getTimeOut() { return timeOut; }

    /**
     * Hours worked as "H:MM" or "N/A" if invalid.
     */
    public String getHoursWorked() {
        if (timeIn.isEmpty() || timeOut.isEmpty()) return "N/A";
        try {
            String[] inParts = timeIn.split(":");
            String[] outParts = timeOut.split(":");
            int inHour = Integer.parseInt(inParts[0].trim());
            int inMin = inParts.length > 1 ? Integer.parseInt(inParts[1].trim()) : 0;
            int outHour = Integer.parseInt(outParts[0].trim());
            int outMin = outParts.length > 1 ? Integer.parseInt(outParts[1].trim()) : 0;
            int totalInMinutes = inHour * 60 + inMin;
            int totalOutMinutes = outHour * 60 + outMin;
            int diffMinutes = totalOutMinutes - totalInMinutes;
            if (diffMinutes < 0) return "N/A";
            int hours = diffMinutes / 60;
            int minutes = diffMinutes % 60;
            return String.format("%d:%02d", hours, minutes);
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Total minutes worked; negative if time-out is before time-in.
     */
    public int getMinutesWorked() {
        if (timeIn.isEmpty() || timeOut.isEmpty()) return 0;
        try {
            String[] inParts = timeIn.split(":");
            String[] outParts = timeOut.split(":");
            int inHour = Integer.parseInt(inParts[0].trim());
            int inMin = inParts.length > 1 ? Integer.parseInt(inParts[1].trim()) : 0;
            int outHour = Integer.parseInt(outParts[0].trim());
            int outMin = outParts.length > 1 ? Integer.parseInt(outParts[1].trim()) : 0;
            return (outHour * 60 + outMin) - (inHour * 60 + inMin);
        } catch (Exception e) {
            return 0;
        }
    }

    /** [INTERFACE] Implements Validatable.isValid. [INHERITANCE] Overrides AbstractEntity.isValid. */
    @Override
    public boolean isValid() {
        return getMinutesWorked() >= 0;
    }

    /** [POLYMORPHISM - Overloading] Same method name, different parameter: returns total minutes as String. */
    public String getHoursWorked(boolean asMinutes) {
        return asMinutes ? String.valueOf(getMinutesWorked()) : getHoursWorked();
    }
}
