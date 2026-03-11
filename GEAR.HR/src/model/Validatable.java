package model;

/**
 * [INTERFACE] Defines entities that can validate their own state.
 * Implemented by AbstractEntity subclasses and by PayrollData.
 */
public interface Validatable {
    /**
     * [INTERFACE] Returns true if this entity's state is valid according to its business rules.
     */
    boolean isValid();
}
