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
        if (line == null) return new String[0];
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // Handle escaped quote inside quoted field ("")
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                parts.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString().trim());
        return parts.toArray(new String[0]);
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
