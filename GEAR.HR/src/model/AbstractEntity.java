package model;

/**
 * [ABSTRACTION] Abstract base class that defines shared characteristics of domain entities
 * that have an identifier. Subclasses (Employee, AttendanceRecord, LeaveRequest, PayrollResult)
 * extend this class and provide their own validation logic.
 * [INTERFACE] Implements Identifiable and Validatable.
 */
public abstract class AbstractEntity implements Identifiable, Validatable {

    /** Shared attribute: unique identifier for this entity. */
    protected final String entityId;

    /**
     * [ABSTRACTION] Protected constructor for subclasses; stores the entity id (null-safe).
     */
    protected AbstractEntity(String id) {
        this.entityId = id != null ? id.trim() : "";
    }

    /**
     * [INTERFACE] Returns the unique identifier (Implements Identifiable.getId).
     */
    @Override
    public String getId() {
        return entityId;
    }

    /**
     * [INTERFACE] Subclasses must implement validation (Validatable.isValid).
     */
    @Override
    public abstract boolean isValid();
}
