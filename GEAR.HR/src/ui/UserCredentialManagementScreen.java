package ui;

import model.ItTicket;
import model.UserCredential;
import service.ApplicationContext;
import service.IItTicketService;
import service.IUserCredentialService;
import service.RoleGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Admin UI for credentials and IT tickets; services from {@link ApplicationContext} (DI).
 * [INTERFACE] Implements ModuleScreen. [INHERITANCE] Extends BaseModuleScreen for shared colors and helpers.
 * [POLYMORPHISM] INSTANCE can be used as ModuleScreen by Main.
 */
public class UserCredentialManagementScreen extends BaseModuleScreen implements ModuleScreen {
    public static final ModuleScreen INSTANCE = new UserCredentialManagementScreen();

    private static IUserCredentialService userCredentialService;
    private static IItTicketService itTicketService;
    private static JTable credentialTable;
    private static DefaultTableModel credentialModel;
    private static JTable ticketTable;
    private static DefaultTableModel ticketModel;

    /** [INTERFACE] Implements ModuleScreen.show; obtains credential and ticket services from ctx and builds UI. */
    @Override
    public void show(JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx) {
        userCredentialService = ctx.getUserCredentialService();
        itTicketService = ctx.getItTicketService();

        JFrame frame = createFrame(parentFrame, "User Credential Management", 1200, 850);
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_WHITE);
        main.add(createHeaderPanel("User Credential Management", "Manage account credentials and IT support tickets"), BorderLayout.NORTH);
        main.add(createContentPanel(frame), BorderLayout.CENTER);
        main.add(createFooterPanel("© 2026 GEAR.HR - IT/Admin"), BorderLayout.SOUTH);
        frame.add(main);
        frame.setVisible(true);
    }

    private static JTabbedPane createContentPanel(JFrame frame) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Garet", Font.BOLD, 13));
        tabs.addTab("User Credentials", createCredentialsTab(frame));
        tabs.addTab("IT Tickets", createTicketsTab(frame));
        return tabs;
    }

    private static JPanel createCredentialsTab(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        credentialModel = new DefaultTableModel(new String[]{"UserID", "Password", "Role", "Email"}, 0) {
            /** [INHERITANCE] Overrides DefaultTableModel.isCellEditable to make rows read-only. */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        credentialTable = new JTable(credentialModel);
        credentialTable.setRowHeight(24);
        panel.add(new JScrollPane(credentialTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateButton = createActionButton("Update User Credentials");
        JButton refreshButton = createActionButton("Refresh");
        updateButton.addActionListener(e -> showUpdateCredentialDialog(frame));
        refreshButton.addActionListener(e -> refreshCredentialsTable());
        buttons.add(updateButton);
        buttons.add(refreshButton);
        panel.add(buttons, BorderLayout.SOUTH);

        refreshCredentialsTable();
        return panel;
    }

    private static JPanel createTicketsTab(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ticketModel = new DefaultTableModel(new String[]{"TicketID", "UserID Requestor", "Type of Request", "Status"}, 0) {
            /** [INHERITANCE] Overrides DefaultTableModel.isCellEditable to make rows read-only. */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ticketTable = new JTable(ticketModel);
        ticketTable.setRowHeight(24);
        panel.add(new JScrollPane(ticketTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateStatusButton = createActionButton("Update Status");
        JButton deleteButton = createActionButton("Delete Ticket");
        JButton refreshButton = createActionButton("Refresh");

        updateStatusButton.addActionListener(e -> showUpdateTicketStatusDialog(frame));
        deleteButton.addActionListener(e -> deleteSelectedTicket(frame));
        refreshButton.addActionListener(e -> refreshTicketsTable());

        buttons.add(updateStatusButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);
        panel.add(buttons, BorderLayout.SOUTH);

        refreshTicketsTable();
        return panel;
    }

    private static JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Garet", Font.BOLD, 12));
        button.setBackground(BUTTON_ORANGE);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private static void refreshCredentialsTable() {
        credentialModel.setRowCount(0);
        userCredentialService.reload();
        List<UserCredential> list = userCredentialService.getAllCredentials();
        for (UserCredential credential : list) {
            credentialModel.addRow(new Object[]{
                credential.getUserId(),
                credential.getPassword(),
                credential.getRole(),
                credential.getEmail()
            });
        }
    }

    private static void refreshTicketsTable() {
        ticketModel.setRowCount(0);
        itTicketService.reload();
        List<ItTicket> list = itTicketService.getAllTickets();
        for (ItTicket ticket : list) {
            ticketModel.addRow(new Object[]{
                ticket.getTicketId(),
                ticket.getUserIdRequestor(),
                ticket.getTypeOfRequest(),
                ticket.getStatus()
            });
        }
    }

    private static void showUpdateCredentialDialog(JFrame frame) {
        int selectedRow = credentialTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a user credential row first.");
            return;
        }

        String userId = String.valueOf(credentialModel.getValueAt(selectedRow, 0));
        String password = String.valueOf(credentialModel.getValueAt(selectedRow, 1));
        String role = String.valueOf(credentialModel.getValueAt(selectedRow, 2));
        String email = String.valueOf(credentialModel.getValueAt(selectedRow, 3));

        JTextField userIdField = new JTextField(userId);
        userIdField.setEditable(false);
        JTextField passwordField = new JTextField(password);
        JTextField emailField = new JTextField(email);

        Set<String> distinctRoles = userCredentialService.getDistinctRoles();
        List<String> roleOptions = new ArrayList<>(distinctRoles);
        if (roleOptions.isEmpty() || !roleOptions.contains(role)) {
            roleOptions.add(role);
        }
        JComboBox<String> roleCombo = new JComboBox<>(roleOptions.toArray(new String[0]));
        roleCombo.setSelectedItem(role);

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("UserID"));
        form.add(userIdField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(new JLabel("Role"));
        form.add(roleCombo);
        form.add(new JLabel("Email"));
        form.add(emailField);

        int result = JOptionPane.showConfirmDialog(frame, form, "Update User Credentials", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String error = userCredentialService.updateCredential(
            userIdField.getText().trim(),
            passwordField.getText(),
            String.valueOf(roleCombo.getSelectedItem()),
            emailField.getText().trim()
        );

        if (error != null) {
            JOptionPane.showMessageDialog(frame, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshCredentialsTable();
        JOptionPane.showMessageDialog(frame, "User credentials updated successfully.");
    }

    private static void showUpdateTicketStatusDialog(JFrame frame) {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select an IT ticket row first.");
            return;
        }

        String ticketId = String.valueOf(ticketModel.getValueAt(selectedRow, 0));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{ItTicket.STATUS_PENDING, ItTicket.STATUS_RESOLVED});
        statusCombo.setSelectedItem(String.valueOf(ticketModel.getValueAt(selectedRow, 3)));

        int result = JOptionPane.showConfirmDialog(frame, statusCombo, "Update Status", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        boolean updated = itTicketService.updateTicketStatus(ticketId, String.valueOf(statusCombo.getSelectedItem()));
        if (!updated) {
            JOptionPane.showMessageDialog(frame, "Unable to update ticket status.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshTicketsTable();
    }

    private static void deleteSelectedTicket(JFrame frame) {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select an IT ticket row first.");
            return;
        }

        String ticketId = String.valueOf(ticketModel.getValueAt(selectedRow, 0));
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete selected ticket " + ticketId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean deleted = itTicketService.deleteTicket(ticketId);
        if (!deleted) {
            JOptionPane.showMessageDialog(frame, "Unable to delete selected ticket.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshTicketsTable();
    }
}
