package repository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * [ABSTRACTION] Abstract base for CSV repositories that persist a list of entities.
 * Subclasses provide file path, header, and line parsing/serialization; this class
 * implements the common load/save file I/O. [INHERITANCE] Concrete repos extend and
 * implement the abstract hooks.
 */
public abstract class AbstractCsvListRepository<T> {

    /**
     * [ABSTRACTION] Subclass returns the CSV file path (e.g. "csv/employees.csv").
     */
    protected abstract String getFilePath();

    /**
     * [ABSTRACTION] Subclass returns the header line (e.g. "EmployeeNumber,LastName,...").
     */
    protected abstract String getHeader();

    /**
     * [ABSTRACTION] Subclass parses one CSV line (already split) into an entity; returns null to skip.
     */
    protected abstract T parseLine(String[] parts);

    /**
     * [ABSTRACTION] Subclass converts one entity to CSV column values for writing.
     */
    protected abstract String[] toCsvRow(T item);

    /**
     * [ABSTRACTION] [INHERITANCE] Splits a raw line into parts; default is comma. Override for quoted CSV.
     */
    protected String[] splitLine(String line) {
        return line.split(",", -1);
    }

    /**
     * [ABSTRACTION] [INHERITANCE] Template method: reads file, skips header, parses each line via parseLine.
     * Not final so subclasses can override to implement interface and delegate to super.
     */
    public List<T> load() {
        List<T> list = new ArrayList<>();
        String path = getFilePath();
        if (!Files.exists(Paths.get(path))) return list;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitLine(line);
                T item = parseLine(parts);
                if (item != null) list.add(item);
            }
        } catch (IOException e) {
            // return empty
        }
        return list;
    }

    /**
     * [ABSTRACTION] [INHERITANCE] Template method: writes header then each entity via toCsvRow.
     * Not final so subclasses can override to implement interface and delegate to super.
     */
    public void save(List<T> items) {
        if (items == null) return;
        try (PrintWriter w = new PrintWriter(new FileWriter(getFilePath()))) {
            w.println(getHeader());
            for (T item : items) {
                w.println(String.join(",", toCsvRow(item)));
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
