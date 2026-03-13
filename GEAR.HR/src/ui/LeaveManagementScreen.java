package ui;

import model.Employee;
import model.LeaveRequest;
import service.ApplicationContext;
import service.IEmployeeService;
import service.ILeaveService;
import service.RoleGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Leave management UI; view, create, and update leave request status. Services injected (DI).
 * [INTERFACE] Implements ModuleScreen. [INHERITANCE] Extends BaseModuleScreen for shared colors and helpers.
 * [POLYMORPHISM] INSTANCE can be used as ModuleScreen by Main.
 */
public class LeaveManagementScreen extends BaseModuleScreen implements ModuleScreen {

    /** [POLYMORPHISM] Single instance used when opening this screen via ModuleScreen. */
    public static final ModuleScreen INSTANCE = new LeaveManagementScreen();

    private static ILeaveService leaveService;
    private static IEmployeeService employeeService;
    private static RoleGroup roleGroup;
    private static String currentUserId;

    private static JTable leaveTable;
    private static DefaultTableModel tableModel;
    private static JComboBox<String> employeeComboBox;
    private static JTextField startDateField;
    private static JTextField endDateField;
    private static JTextField reasonField;
    private static JComboBox<String> statusComboBox;

    /** [INTERFACE] Implements ModuleScreen.show; obtains services from ctx and builds UI. */
    @Override
    public void show(JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx) {
        leaveService = ctx.getLeaveService();
        employeeService = ctx.getEmployeeService();
        roleGroup = group != null ? group : RoleGroup.NORMAL;
        currentUserId = userId;
        JFrame frame = createFrame(parentFrame, "Leave Management", 1000, 750);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        mainPanel.add(createHeaderPanel("Leave Management", "View and manage leave requests"), BorderLayout.NORTH);
        JPanel content = createContentPanel(frame);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.add(content, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel("© Year 2026 GEAR.HR - Leave Management"), BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /** Legacy entry point; callers should use ModuleScreen.show instead. */
    public static void showLeaveScreen(JFrame parentFrame, String userId, String role, RoleGroup group,
                                       ILeaveService lsvc, IEmployeeService empSvc) {
        leaveService = lsvc;
        employeeService = empSvc;
        roleGroup = group != null ? group : RoleGroup.NORMAL;
        currentUserId = userId;
        JFrame frame = createFrame(parentFrame, "Leave Management", 1000, 750);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        mainPanel.add(createHeaderPanel("Leave Management", "View and manage leave requests"), BorderLayout.NORTH);
        JPanel content = createContentPanel(frame);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.add(content, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel("© Year 2026 GEAR.HR - Leave Management"), BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /** [INHERITANCE] Builds main content for this module (screen-specific). */
    private static JPanel createContentPanel(JFrame frame) {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_WHITE);
        if (roleGroup != RoleGroup.PAYROLL) {
            content.add(createFormPanel(frame), BorderLayout.NORTH);
        }
        content.add(createTablePanel(frame), BorderLayout.CENTER);
        return content;
    }

    private static JPanel createFormPanel(JFrame frame) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD_WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        c.anchor = GridBagConstraints.WEST;

        employeeComboBox = createEmployeeComboBox();
        if (roleGroup == RoleGroup.NORMAL && currentUserId != null) {
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
        startDateField = new JTextField(LocalDate.now().toString(), 12);
        styleField(startDateField);
        endDateField = new JTextField(LocalDate.now().toString(), 12);
        styleField(endDateField);
        reasonField = new JTextField(20);
        styleField(reasonField);

        int row = 0;
        addRow(form, c, row++, "Employee:", employeeComboBox);
        addRow(form, c, row++, "Start (yyyy-MM-dd):", startDateField);
        addRow(form, c, row++, "End (yyyy-MM-dd):", endDateField);
        addRow(form, c, row++, "Reason:", reasonField);

        JButton submit = createStyledButton("Submit Request", BUTTON_ORANGE);
        submit.addActionListener(e -> handleSubmit(frame));
        c.gridx = 1;
        c.gridy = row;
        form.add(submit, c);
        return form;
    }

    private static void addRow(JPanel p, GridBagConstraints c, int row, String labelText, JComponent comp) {
        JLabel lab = new JLabel(labelText);
        lab.setFont(new Font("Garet", Font.BOLD, 12));
        lab.setForeground(TEXT_BLACK);
        c.gridx = 0;
        c.gridy = row;
        p.add(lab, c);
        c.gridx = 1;
        p.add(comp, c);
    }

    private static void styleField(JTextField f) {
        f.setFont(new Font("Garet", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private static JComboBox<String> createEmployeeComboBox() {
        List<String> opts = new ArrayList<>();
        opts.add("Select Employee");
        if (employeeService != null) {
            for (Employee e : employeeService.getAllEmployees()) {
                opts.add(e.getEmployeeNumber() + " - " + e.getFirstName() + " " + e.getLastName());
            }
        }
        JComboBox<String> cb = new JComboBox<>(opts.toArray(new String[0]));
        cb.setFont(new Font("Garet", Font.PLAIN, 12));
        cb.setPreferredSize(new Dimension(220, 28));
        return cb;
    }

    private static JPanel createTablePanel(JFrame frame) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        String[] cols = {"Employee ID", "Start Date", "End Date", "Reason", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int col) { return false; }
        };
        leaveTable = new JTable(tableModel);
        leaveTable.setFont(new Font("Garet", Font.PLAIN, 12));
        leaveTable.setRowHeight(28);
        leaveTable.getTableHeader().setFont(new Font("Garet", Font.BOLD, 12));
        leaveTable.getTableHeader().setBackground(BUTTON_ORANGE);
        leaveTable.getTableHeader().setForeground(TEXT_WHITE);
        leaveTable.setSelectionBackground(BUTTON_ORANGE);
        leaveTable.setSelectionForeground(TEXT_WHITE);

        statusComboBox = new JComboBox<>(new String[]{
            LeaveRequest.STATUS_PENDING, LeaveRequest.STATUS_APPROVED, LeaveRequest.STATUS_REJECTED
        });
        statusComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        JButton updateStatusBtn = createStyledButton("Update Status", BUTTON_ORANGE);
        updateStatusBtn.addActionListener(e -> handleUpdateStatus(frame));
        JButton deleteBtn = createStyledButton("Delete", BUTTON_ORANGE);
        deleteBtn.addActionListener(e -> handleDeleteLeaveRequest(frame));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JLabel title = new JLabel("Leave Requests");
        title.setFont(new Font("Garet", Font.BOLD, 16));
        title.setForeground(TEXT_BLACK);
        top.add(title);
        if (roleGroup == RoleGroup.HR || roleGroup == RoleGroup.IT_ADMIN) {
            top.add(Box.createHorizontalStrut(20));
            top.add(deleteBtn);
            top.add(Box.createHorizontalStrut(20));
            top.add(new JLabel("New status:"));
            top.add(statusComboBox);
            top.add(updateStatusBtn);
        }

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(leaveTable), BorderLayout.CENTER);
        refreshTable();
        return p;
    }

    private static void refreshTable() {
        tableModel.setRowCount(0);
        if (leaveService == null) return;
        List<LeaveRequest> list = roleGroup == RoleGroup.NORMAL && currentUserId != null
            ? leaveService.getAllLeaveRequests().stream().filter(lr -> currentUserId.equals(lr.getEmployeeId())).collect(Collectors.toList())
            : leaveService.getAllLeaveRequests();
        for (LeaveRequest lr : list) {
            tableModel.addRow(new Object[]{
                lr.getEmployeeId(),
                lr.getStartDate() != null ? lr.getStartDate().toString() : "",
                lr.getEndDate() != null ? lr.getEndDate().toString() : "",
                lr.getReason(),
                lr.getStatus()
            });
        }
    }

    private static void handleSubmit(JFrame frame) {
        String sel = (String) employeeComboBox.getSelectedItem();
        if (sel == null || "Select Employee".equals(sel)) {
            JOptionPane.showMessageDialog(frame, "Please select an employee.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String employeeId = sel.split(" - ")[0];
        LocalDate start = LeaveRequest.parseDate(startDateField.getText().trim());
        LocalDate end = LeaveRequest.parseDate(endDateField.getText().trim());
        String reason = reasonField.getText().trim();
        if (start == null || end == null) {
            JOptionPane.showMessageDialog(frame, "Enter valid dates (yyyy-MM-dd).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Reason is required. Please enter a reason for the leave request.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LeaveRequest req = new LeaveRequest(employeeId, start, end, reason, LeaveRequest.STATUS_PENDING);
        if (!req.isValid()) {
            JOptionPane.showMessageDialog(frame, "Start date must be on or before end date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (leaveService.hasOverlappingLeaveRequest(employeeId, start, end)) {
            JOptionPane.showMessageDialog(frame,
                "You already have a leave request that overlaps these dates. Please choose different dates or delete the existing request.",
                "Overlapping Request", JOptionPane.ERROR_MESSAGE);
            return;
        }
        leaveService.addLeaveRequest(req);
        refreshTable();
        JOptionPane.showMessageDialog(frame, "Leave request submitted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        startDateField.setText(LocalDate.now().toString());
        endDateField.setText(LocalDate.now().toString());
        reasonField.setText("");
        employeeComboBox.setSelectedIndex(0);
    }

    private static void handleUpdateStatus(JFrame frame) {
        int row = leaveTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Select a leave request first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String employeeId = (String) tableModel.getValueAt(row, 0);
        String startStr = (String) tableModel.getValueAt(row, 1);
        LocalDate start = LeaveRequest.parseDate(startStr);
        if (start == null) {
            JOptionPane.showMessageDialog(frame, "Could not parse start date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String newStatus = (String) statusComboBox.getSelectedItem();
        leaveService.updateLeaveRequestStatus(employeeId, start, newStatus);
        refreshTable();
        JOptionPane.showMessageDialog(frame, "Status updated.", "Done", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void handleDeleteLeaveRequest(JFrame frame) {
        int row = leaveTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Select a leave request first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String employeeId = (String) tableModel.getValueAt(row, 0);
        String startStr = (String) tableModel.getValueAt(row, 1);
        LocalDate start = LeaveRequest.parseDate(startStr);
        if (start == null) {
            JOptionPane.showMessageDialog(frame, "Could not parse start date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete this leave request?", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        leaveService.deleteLeaveRequest(employeeId, start);
        refreshTable();
        JOptionPane.showMessageDialog(frame, "Leave request deleted.", "Done", JOptionPane.INFORMATION_MESSAGE);
    }

    private static JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Garet", Font.BOLD, 14));
        b.setForeground(TEXT_WHITE);
        b.setBackground(bg);
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

}
