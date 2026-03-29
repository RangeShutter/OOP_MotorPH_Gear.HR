package repository;

import model.ItTicket;

import java.util.List;

/**
 * [INTERFACE] Contract for loading and saving IT support tickets.
 * Implementations (e.g. CSV) can be swapped for testing or different storage.
 */
public interface IItTicketRepository {
    /** [INTERFACE] Loads all IT tickets from storage. */
    List<ItTicket> load();

    /** [INTERFACE] Persists the given ticket list to storage. */
    void save(List<ItTicket> tickets);
}
