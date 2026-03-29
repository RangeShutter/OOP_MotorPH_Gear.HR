package service;

import model.ItTicket;

import java.util.List;

/**
 * [INTERFACE] Contract for IT ticket use cases: reload, list, create, update status, delete.
 */
public interface IItTicketService {
    /** [INTERFACE] Reloads tickets from the repository into memory. */
    void reload();

    /** [INTERFACE] Returns all tickets sorted by ticket id. */
    List<ItTicket> getAllTickets();

    /** [INTERFACE] Creates a ticket if valid; returns null on success or an error message. */
    String createTicket(String userIdRequestor, String typeOfRequest);

    /** [INTERFACE] Updates status for a ticket id; returns false if not found or invalid status. */
    boolean updateTicketStatus(String ticketId, String newStatus);

    /** [INTERFACE] Deletes a ticket by id; returns true if removed. */
    boolean deleteTicket(String ticketId);
}
