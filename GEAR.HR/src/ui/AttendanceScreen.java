package ui;

import model.AttendanceRecord;
import model.Employee;
import service.ApplicationContext;
import service.IAttendanceService;
import service.IEmployeeService;
import service.RoleGroup;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.table.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ItemEvent;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
// #region agent log
import java.io.FileWriter;
// #endregion

/**
 * Attendance UI screen; services injected (DI). Named AttendanceScreen to avoid confusion with domain/service.
 * [INTERFACE] Implements ModuleScreen. [INHERITANCE] Extends BaseModuleScreen for shared colors and helpers.
 * [POLYMORPHISM] INSTANCE can be used as ModuleScreen by Main.
 */
public class AttendanceScreen extends BaseModuleScreen implements ModuleScreen {

    /** [POLYMORPHISM] Single instance used when opening this screen via ModuleScreen. */
    public static final ModuleScreen INSTANCE = new AttendanceScreen();

    private static IAttendanceService attendanceService;
    private static IEmployeeService employeeService;
    private static RoleGroup roleGroup;
    private static String currentUserId;

    private static JTable attendanceTable;
    private static DefaultTableModel tableModel;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /* Allow optional leading zero in hour so "08:00" and "17:00" both match (was: [0-9] = single digit only). */
    private static final Pattern TIME_PATTERN = Pattern.compile("^(0?[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$");

    private static JComboBox<String> employeeComboBox;
    private static JTextField dateField;
    private static JComboBox<String> statusComboBox;
    private static JTextField timeInField;
    private static JTextField timeOutField;

    /** [INTERFACE] Implements ModuleScreen.show; obtains services from ctx and builds UI. */
    @Override
    public void show(JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx) {
        attendanceService = ctx.getAttendanceService();
        employeeService = ctx.getEmployeeService();
        roleGroup = group != null ? group : RoleGroup.NORMAL;
        currentUserId = userId;
        JFrame attendanceFrame = createFrame(parentFrame, "Attendance Management System", 1200, 1000);
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        JPanel contentPanel = createContentPanel(attendanceFrame, userId, role);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        attendanceFrame.add(mainPanel);
        attendanceFrame.setVisible(true);
    }

    /** Legacy entry point; callers should use ModuleScreen.show instead. */
    public static void showAttendanceScreen(JFrame parentFrame, String userId, String role, RoleGroup group,
                                            IAttendanceService attSvc, IEmployeeService empSvc) {
        attendanceService = attSvc;
        employeeService = empSvc;
        roleGroup = group != null ? group : RoleGroup.NORMAL;
        currentUserId = userId;
        JFrame attendanceFrame = createFrame(parentFrame, "Attendance Management System", 1200, 1000);
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        JPanel contentPanel = createContentPanel(attendanceFrame, userId, role);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        attendanceFrame.add(mainPanel);
        attendanceFrame.setVisible(true);
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(createHeaderTitleLabel(), BorderLayout.NORTH);
        titlePanel.add(createHeaderSubtitleLabel(), BorderLayout.CENTER);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    private static JLabel createHeaderTitleLabel() {
        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    private static JLabel createHeaderSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Track employee attendance and manage time records");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /** [INHERITANCE] Builds main content for this module (screen-specific). */
    private static JPanel createContentPanel(JFrame attendanceFrame, String userId, String role) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        if (roleGroup != RoleGroup.PAYROLL) {
            contentPanel.add(createInputPanel(attendanceFrame), BorderLayout.NORTH);
        }
        JPanel tablePanel = createTablePanel();
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(0, 30, 0, 30)
        ));
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        return contentPanel;
    }

    private static JPanel createInputPanel(JFrame attendanceFrame) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(CARD_WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(30, 30, 90, 30)
        ));
        inputPanel.add(createInputPanelTitleLabel(), BorderLayout.NORTH);
        inputPanel.add(createFormPanel(attendanceFrame), BorderLayout.CENTER);
        inputPanel.add(createButtonPanel(attendanceFrame), BorderLayout.SOUTH);
        return inputPanel;
    }

    private static JLabel createInputPanelTitleLabel() {
        JLabel titleLabel = new JLabel("Record Attendance");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return titleLabel;
    }

    private static JPanel createFormPanel(JFrame attendanceFrame) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 10, 8, 10);
        constraints.anchor = GridBagConstraints.WEST;
        employeeComboBox = createEmployeeComboBox();
        dateField = createDateField();
        statusComboBox = createStatusComboBox();
        timeInField = createTimeField("08:00");
        timeOutField = createTimeField("17:00");
        if (roleGroup == RoleGroup.NORMAL) {
            List<String> selfOnly = new ArrayList<>();
            if (employeeService != null) {
                Employee self = employeeService.getAllEmployees().stream()
                    .filter(emp -> currentUserId.equals(emp.getEmployeeNumber())).findFirst().orElse(null);
                selfOnly.add(self != null ? currentUserId + " - " + self.getFirstName() + " " + self.getLastName() : currentUserId);
            } else {
                selfOnly.add(currentUserId);
            }
            employeeComboBox.setModel(new DefaultComboBoxModel<>(selfOnly.toArray(new String[0])));
            employeeComboBox.setEnabled(false);
        }
        addFormField(formPanel, "Employee:", employeeComboBox, constraints, 0, 0);
        addFormField(formPanel, "Date:", dateField, constraints, 0, 2);
        addFormField(formPanel, "Status:", statusComboBox, constraints, 1, 0);
        addFormField(formPanel, "Time In:", timeInField, constraints, 1, 2);
        addFormField(formPanel, "Time Out:", timeOutField, constraints, 1, 4);
        return formPanel;
    }

    private static JComboBox<String> createEmployeeComboBox() {
        List<String> employeeOptions = getEmployeeOptions();
        JComboBox<String> employeeComboBox = new JComboBox<>(employeeOptions.toArray(new String[0]));
        employeeComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        employeeComboBox.setPreferredSize(new Dimension(200, 30));
        return employeeComboBox;
    }

    private static JTextField createDateField() {
        JTextField dateField = new JTextField(LocalDate.now().format(DATE_FORMAT), 15);
        dateField.setFont(new Font("Garet", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return dateField;
    }

    private static JComboBox<String> createStatusComboBox() {
        String[] statusOptions = {"Present", "Absent", "Late", "On Leave", "Half Day"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        statusComboBox.setPreferredSize(new Dimension(150, 30));
        statusComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && timeInField != null && timeOutField != null) {
                String status = (String) statusComboBox.getSelectedItem();
                boolean noTimeRequired = "On Leave".equals(status) || "Absent".equals(status);
                timeInField.setEnabled(!noTimeRequired);
                timeOutField.setEnabled(!noTimeRequired);
                if (noTimeRequired) {
                    timeInField.setText("");
                    timeOutField.setText("");
                } else {
                    if (timeInField.getText().trim().isEmpty()) timeInField.setText("08:00");
                    if (timeOutField.getText().trim().isEmpty()) timeOutField.setText("17:00");
                }
            }
        });
        return statusComboBox;
    }

    private static JTextField createTimeField(String defaultValue) {
        JTextField timeField = new JTextField(defaultValue, 10);
        timeField.setFont(new Font("Garet", Font.PLAIN, 12));
        timeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return timeField;
    }

    private static void addFormField(JPanel formPanel, String labelText, JComponent component,
                                   GridBagConstraints constraints, int row, int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Garet", Font.BOLD, 12));
        label.setForeground(TEXT_BLACK);
        constraints.gridx = col;
        constraints.gridy = row;
        formPanel.add(label, constraints);
        constraints.gridx = col + 1;
        formPanel.add(component, constraints);
    }

    private static JPanel createButtonPanel(JFrame attendanceFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        JButton recordButton = createStyledButton("Record Attendance", BUTTON_ORANGE);
        JButton clearButton = createStyledButton("Clear", ACCENT_GREY);
        JButton refreshButton = createStyledButton("Refresh", ACCENT_GREY);
        recordButton.addActionListener(e -> handleRecordAttendance(attendanceFrame));
        clearButton.addActionListener(e -> handleClearAllRecords(attendanceFrame));
        refreshButton.addActionListener(e -> handleRefreshData(attendanceFrame));
        buttonPanel.add(recordButton);
        if (roleGroup == RoleGroup.HR || roleGroup == RoleGroup.IT_ADMIN) {
            buttonPanel.add(clearButton);
        }
        buttonPanel.add(refreshButton);
        return buttonPanel;
    }

    private static JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(0, 30, 0, 30)
        ));
        createAttendanceTable();
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 600));
        tablePanel.add(createTableTitleLabel(), BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private static JLabel createTableTitleLabel() {
        JLabel tableTitle = new JLabel("Attendance Records");
        tableTitle.setFont(new Font("Garet", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_BLACK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return tableTitle;
    }

    private static void createAttendanceTable() {
        String[] columnNames = {"Employee ID", "Date", "Status", "Time In", "Time Out", "Hours Worked"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        attendanceTable = new JTable(tableModel);
        styleAttendanceTable();
        updateAttendanceTable();
    }

    private static void styleAttendanceTable() {
        attendanceTable.setFont(new Font("Garet", Font.PLAIN, 12));
        attendanceTable.setRowHeight(30);
        attendanceTable.setGridColor(new Color(220, 220, 220));
        attendanceTable.setSelectionBackground(BUTTON_ORANGE);
        attendanceTable.setSelectionForeground(TEXT_WHITE);
        attendanceTable.setShowGrid(true);
        attendanceTable.setIntercellSpacing(new Dimension(1, 1));
        attendanceTable.getTableHeader().setFont(new Font("Garet", Font.BOLD, 12));
        attendanceTable.getTableHeader().setBackground(BUTTON_ORANGE);
        attendanceTable.getTableHeader().setForeground(TEXT_WHITE);
        attendanceTable.getTableHeader().setBorder(BorderFactory.createLineBorder(BUTTON_ORANGE));
    }

    private static JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Garet", Font.BOLD, 16));
        button.setForeground(TEXT_WHITE);
        button.setBackground(backgroundColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(220, 56));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(HEADER_DARK);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel copyrightLabel = new JLabel("© Year 2026 GEAR.HR - Attendance Tracking");
        copyrightLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        copyrightLabel.setForeground(TEXT_WHITE);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel versionLabel = new JLabel("Version 2.0 - Enhanced UI & Functionality");
        versionLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        versionLabel.setForeground(TEXT_GREY);
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(copyrightLabel, BorderLayout.NORTH);
        footerPanel.add(versionLabel, BorderLayout.SOUTH);
        return footerPanel;
    }

    private static List<String> getEmployeeOptions() {
        List<String> options = new ArrayList<>();
        options.add("Select Employee");
        try {
            if (employeeService != null) {
                List<Employee> employees = employeeService.getAllEmployees();
                options.addAll(employees.stream()
                .map(emp -> emp.getEmployeeNumber() + " - " + emp.getFirstName() + " " + emp.getLastName())
                .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            options.add("1001 - Colin Bactong");
            options.add("1002 - Charlize Bactong");
            options.add("1003 - Angelica");
        }
        return options;
    }

    private static void updateAttendanceTable() {
        tableModel.setRowCount(0);
        List<AttendanceRecord> records = roleGroup == RoleGroup.NORMAL && currentUserId != null
            ? attendanceService.getAllRecords().stream().filter(r -> currentUserId.equals(r.getEmployeeId())).collect(Collectors.toList())
            : attendanceService.getAllRecords();
        for (AttendanceRecord record : records) {
            tableModel.addRow(new Object[]{
                record.getEmployeeId(), record.getDate(), record.getStatus(),
                record.getTimeIn(), record.getTimeOut(), record.getHoursWorked()
            });
        }
    }

    /**
     * Returns true if date is in yyyy-MM-dd format and parseable.
     */
    private static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return false;
        try {
            LocalDate.parse(dateStr.trim(), DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Parses time string (HH:mm) to total minutes since midnight, or -1 if invalid.
     */
    private static int parseTimeToMinutes(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) return -1;
        timeStr = timeStr.trim();
        boolean matches = TIME_PATTERN.matcher(timeStr).matches();
        // #region agent log
        try (FileWriter fw = new FileWriter("debug-5a2820.log", true)) {
            fw.write("{\"sessionId\":\"5a2820\",\"location\":\"AttendanceScreen.parseTimeToMinutes\",\"message\":\"parseTime\",\"data\":{\"timeStr\":\"" + (timeStr != null ? timeStr.replace("\\","\\\\").replace("\"","'") : "null") + "\",\"patternMatches\":" + matches + "},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"H1\"}\n");
        } catch (Exception e) {}
        // #endregion
        if (!matches) return -1;
        String[] parts = timeStr.split(":");
        int result = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        // #region agent log
        try (FileWriter fw = new FileWriter("debug-5a2820.log", true)) {
            fw.write("{\"sessionId\":\"5a2820\",\"location\":\"AttendanceScreen.parseTimeToMinutes\",\"message\":\"parseResult\",\"data\":{\"result\":" + result + "},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"H5\"}\n");
        } catch (Exception e) {}
        // #endregion
        return result;
    }

    private static void handleRecordAttendance(JFrame attendanceFrame) {
        String employeeSelection = (String) employeeComboBox.getSelectedItem();
        String date = dateField.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();
        String timeIn = timeInField.getText().trim();
        String timeOut = timeOutField.getText().trim();
        // #region agent log
        try (FileWriter fw = new FileWriter("debug-5a2820.log", true)) {
            String rawIn = timeInField.getText();
            String rawOut = timeOutField.getText();
            fw.write("{\"sessionId\":\"5a2820\",\"location\":\"AttendanceScreen.handleRecordAttendance\",\"message\":\"timeFields\",\"data\":{\"timeIn\":\"" + (timeIn != null ? timeIn.replace("\\","\\\\").replace("\"","'") : "null") + "\",\"timeOut\":\"" + (timeOut != null ? timeOut.replace("\\","\\\\").replace("\"","'") : "null") + "\",\"timeInLen\":" + (timeIn != null ? timeIn.length() : 0) + ",\"timeOutLen\":" + (timeOut != null ? timeOut.length() : 0) + ",\"rawInLen\":" + (rawIn != null ? rawIn.length() : 0) + ",\"rawOutLen\":" + (rawOut != null ? rawOut.length() : 0) + "},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"H2\"}\n");
        } catch (Exception e) {}
        // #endregion

        if (employeeSelection == null || employeeSelection.equals("Select Employee")) {
            JOptionPane.showMessageDialog(attendanceFrame,
                "Please select an employee from the list.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(attendanceFrame,
                "Date is required. Use Year-Month-Day format (e.g. 2025-03-15).",
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidDateFormat(date)) {
            JOptionPane.showMessageDialog(attendanceFrame,
                "Invalid date format. Date must be Year-Month-Day (yyyy-MM-dd). Example: 2025-03-15.",
                "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean noTimeRequired = "On Leave".equals(status) || "Absent".equals(status);
        if (noTimeRequired) {
            timeIn = "";
            timeOut = "";
        } else {
            if (timeIn.isEmpty() || timeOut.isEmpty()) {
                JOptionPane.showMessageDialog(attendanceFrame,
                    "Time In and Time Out are required for this status. Use 24-hour format (e.g. 08:00, 17:30).",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int inMinutes = parseTimeToMinutes(timeIn);
            int outMinutes = parseTimeToMinutes(timeOut);
            // #region agent log
            try (FileWriter fw = new FileWriter("debug-5a2820.log", true)) {
                fw.write("{\"sessionId\":\"5a2820\",\"location\":\"AttendanceScreen.handleRecordAttendance\",\"message\":\"minutes\",\"data\":{\"inMinutes\":" + inMinutes + ",\"outMinutes\":" + outMinutes + "},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"H3\"}\n");
            } catch (Exception e) {}
            // #endregion
            if (inMinutes < 0 || outMinutes < 0) {
                JOptionPane.showMessageDialog(attendanceFrame,
                    "Invalid time format. Use Hour:Minute in 24-hour format (e.g. 08:00, 17:30).",
                    "Invalid Time", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (inMinutes > outMinutes) {
                JOptionPane.showMessageDialog(attendanceFrame,
                    "Time In must be earlier than or equal to Time Out. Please correct the times.",
                    "Invalid Time Range", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String employeeId = employeeSelection.split(" - ")[0];
        if (attendanceService.hasRecord(employeeId, date)) {
            JOptionPane.showMessageDialog(attendanceFrame,
                "Attendance for this employee on this date already exists. Edit or delete the existing record first.",
                "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AttendanceRecord record = new AttendanceRecord(employeeId, date, status, timeIn, timeOut);
        if (!record.isValid()) {
            JOptionPane.showMessageDialog(attendanceFrame,
                "Invalid attendance: time out must be on or after time in.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        attendanceService.addRecord(record);
        updateAttendanceTable();
        JOptionPane.showMessageDialog(attendanceFrame, "Attendance recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        employeeComboBox.setSelectedIndex(0);
        dateField.setText(LocalDate.now().format(DATE_FORMAT));
        statusComboBox.setSelectedIndex(0);
        timeInField.setText("08:00");
        timeOutField.setText("17:00");
        timeInField.setEnabled(true);
        timeOutField.setEnabled(true);
    }

    private static void handleClearAllRecords(JFrame attendanceFrame) {
        int confirm = JOptionPane.showConfirmDialog(attendanceFrame,
            "Are you sure you want to clear ALL attendance records? This cannot be undone.",
            "Confirm Clear All", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            attendanceService.clearAll();
            updateAttendanceTable();
            JOptionPane.showMessageDialog(attendanceFrame, "All attendance records have been cleared.", "Records Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void handleRefreshData(JFrame attendanceFrame) {
        attendanceService.loadAttendanceRecordsFromCSV();
        updateAttendanceTable();
        JOptionPane.showMessageDialog(attendanceFrame, "Data refreshed successfully", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void removeAttendanceRecords(String employeeId) {
        attendanceService.removeAttendanceRecords(employeeId);
    }
}
