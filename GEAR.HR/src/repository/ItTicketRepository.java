package repository;

import model.ItTicket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * CSV persistence for IT support tickets. Load/save only; no business logic.
 * [INTERFACE] Implements IItTicketRepository.
 * [INHERITANCE] Extends AbstractCsvListRepository for shared CSV load/save; provides ItTicket-specific parse/serialize.
 * [POLYMORPHISM] Can be used as IItTicketRepository by callers.
 */
public class ItTicketRepository extends AbstractCsvListRepository<ItTicket> implements IItTicketRepository {
    private static final String FILE = "csv/ITtickets.csv";
    private static final String HEADER = "ticketId,userIdRequestor,typeOfRequest,status";

    public ItTicketRepository() {
        ensureFileWithHeader();
    }

    /** [ABSTRACTION] [INHERITANCE] Returns file path for IT tickets CSV. */
    @Override
    protected String getFilePath() {
        return FILE;
    }

    /** [ABSTRACTION] [INHERITANCE] Returns CSV header line. */
    @Override
    protected String getHeader() {
        return HEADER;
    }

    /** [ABSTRACTION] [INHERITANCE] Parses one CSV row into an ItTicket; null if invalid. */
    @Override
    protected ItTicket parseLine(String[] parts) {
        if (parts == null || parts.length < 4) return null;
        return new ItTicket(parts[0], parts[1], parts[2], parts[3]);
    }

    /** [ABSTRACTION] [INHERITANCE] Converts ItTicket to CSV column values. */
    @Override
    protected String[] toCsvRow(ItTicket item) {
        return new String[]{
            item.getTicketId(),
            item.getUserIdRequestor(),
            item.getTypeOfRequest(),
            item.getStatus()
        };
    }

    /** [INTERFACE] Implements IItTicketRepository.load. [INHERITANCE] Ensures file exists then delegates to super.load. */
    @Override
    public List<ItTicket> load() {
        ensureFileWithHeader();
        return super.load();
    }

    /** [INTERFACE] Implements IItTicketRepository.save. [INHERITANCE] Ensures file exists then delegates to super.save. */
    @Override
    public void save(List<ItTicket> tickets) {
        ensureFileWithHeader();
        super.save(tickets);
    }

    private void ensureFileWithHeader() {
        try {
            Path path = Paths.get(FILE);
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(path)) {
                Files.write(path, (HEADER + System.lineSeparator()).getBytes());
            }
        } catch (IOException e) {
            // ignore and allow callers to continue gracefully
        }
    }
}
