package util;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;
import java.util.Set;

/**
 * Installs header-click sorting on a {@link JTable} with at most one sort column at a time.
 * Numeric columns compare parsed numbers; other columns compare case-insensitive strings.
 * Unparseable numeric cells sort last (treated as positive infinity when ascending).
 * [ABSTRACTION] Hides {@link TableRowSorter} setup behind a single install method.
 * [ENCAPSULATION] Non-instantiable utility type.
 */
public final class TableColumnSortUtil {

    /** [ENCAPSULATION] Prevents instantiation. */
    private TableColumnSortUtil() {}

    /**
     * Attaches a {@link TableRowSorter} so only one column can be sorted; toggles asc/desc on header clicks.
     *
     * @param table                  non-null table displaying {@code model}
     * @param model                  same model as {@code table.getModel()}
     * @param numericColumnIndices   columns that should sort by parsed number (long preferred, else double)
     */
    public static void install(JTable table, TableModel model, Set<Integer> numericColumnIndices) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setMaxSortKeys(1);

        Comparator<Object> stringComparator = (a, b) ->
            String.CASE_INSENSITIVE_ORDER.compare(String.valueOf(a), String.valueOf(b));

        Comparator<Object> numericComparator = (a, b) -> {
            double na = parseNumeric(a);
            double nb = parseNumeric(b);
            return Double.compare(na, nb);
        };

        int colCount = model.getColumnCount();
        for (int c = 0; c < colCount; c++) {
            if (numericColumnIndices != null && numericColumnIndices.contains(c)) {
                sorter.setComparator(c, numericComparator);
            } else {
                sorter.setComparator(c, stringComparator);
            }
        }

        table.setRowSorter(sorter);
        table.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Parses a cell for numeric sorting. Commas stripped. Invalid/blank maps to {@link Double#POSITIVE_INFINITY}
     * so those rows sort last when ascending (and first when descending).
     */
    private static double parseNumeric(Object o) {
        if (o == null) return Double.POSITIVE_INFINITY;
        String s = String.valueOf(o).trim().replace(",", "");
        if (s.isEmpty()) return Double.POSITIVE_INFINITY;
        try {
            if (s.matches("-?\\d+")) {
                return Long.parseLong(s);
            }
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return Double.POSITIVE_INFINITY;
        }
    }
}
