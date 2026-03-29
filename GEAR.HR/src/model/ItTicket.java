package model;

/**
 * Domain model for IT support tickets.
 * [INHERITANCE] Child class extends AbstractEntity.
 */
public class ItTicket extends AbstractEntity {
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_RESOLVED = "Resolved";

    private final String ticketId;
    private String userIdRequestor;
    private String typeOfRequest;
    private String status;

    public ItTicket(String ticketId, String userIdRequestor, String typeOfRequest, String status) {
        super(ticketId != null ? ticketId.trim() : "");
        this.ticketId = ticketId != null ? ticketId.trim() : "";
        this.userIdRequestor = userIdRequestor != null ? userIdRequestor.trim() : "";
        this.typeOfRequest = typeOfRequest != null ? typeOfRequest.trim() : "";
        this.status = status != null ? status.trim() : STATUS_PENDING;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getUserIdRequestor() {
        return userIdRequestor;
    }

    public void setUserIdRequestor(String userIdRequestor) {
        this.userIdRequestor = userIdRequestor != null ? userIdRequestor.trim() : this.userIdRequestor;
    }

    public String getTypeOfRequest() {
        return typeOfRequest;
    }

    public void setTypeOfRequest(String typeOfRequest) {
        this.typeOfRequest = typeOfRequest != null ? typeOfRequest.trim() : this.typeOfRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status.trim() : this.status;
    }

    /** [INTERFACE] Implements Validatable.isValid. [INHERITANCE] Overrides AbstractEntity.isValid. */
    @Override
    public boolean isValid() {
        if (ticketId.isEmpty() || userIdRequestor.isEmpty() || typeOfRequest.isEmpty() || status.isEmpty()) {
            return false;
        }
        return STATUS_PENDING.equals(status) || STATUS_RESOLVED.equals(status);
    }
}
