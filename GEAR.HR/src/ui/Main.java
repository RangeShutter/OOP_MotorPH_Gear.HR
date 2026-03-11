package ui;

import service.ApplicationContext;
import service.RoleGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main class for the Employee Management System. Central dashboard with navigation to modules.
 * [POLYMORPHISM] Opens module screens via ModuleScreen interface so any screen can be shown uniformly.
 */
public class Main {
    private static String currentUserId;
    private static String currentUserRole;
    private static String currentUserEmail;
    private static ApplicationContext appContext;

    private static final Color BACKGROUND_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color BUTTON_ORANGE = new Color(255, 153, 28);
    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);

    public static void showMainScreen(String userId, String role, String email, ApplicationContext ctx) {
        appContext = ctx;
        currentUserId = userId;
        currentUserRole = role;
        currentUserEmail = email;

        RoleGroup group = RoleGroup.fromRole(role);
        JFrame mainFrame = createMainFrame();
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createSidebarPanel(mainFrame, userId, role, email, group, ctx), BorderLayout.WEST);
        mainPanel.add(createContentPanel(mainFrame, userId, role, email, ctx), BorderLayout.CENTER);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private static JFrame createMainFrame() {
        JFrame mainFrame = new JFrame("Employee Management System");
        mainFrame.setSize(1000, 750);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        try {
            mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {}
        return mainFrame;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

    private static void handleLogout(JFrame mainFrame) {
        int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mainFrame.dispose();
            User.showLoginScreen(null, appContext);
        }
    }

    private static JPanel createSidebarPanel(JFrame mainFrame, String userId, String role, String email, RoleGroup group, ApplicationContext ctx) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(HEADER_DARK);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Garet", Font.BOLD, 12));
        emailLabel.setForeground(TEXT_WHITE);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Garet", Font.PLAIN, 10));
        roleLabel.setForeground(TEXT_WHITE);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(emailLabel);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(roleLabel);
        sidebar.add(Box.createVerticalStrut(16));

        switch (group) {
            case HR:
                sidebar.add(createPersonalAccountCollapsible(mainFrame, userId, role, ctx));
                sidebar.add(Box.createVerticalStrut(8));
                sidebar.add(createHrDirectivesCollapsible(mainFrame, userId, role, group, ctx));
                break;
            case PAYROLL:
                sidebar.add(createPersonalAccountCollapsible(mainFrame, userId, role, ctx));
                sidebar.add(Box.createVerticalStrut(8));
                sidebar.add(createPayrollDirectivesCollapsible(mainFrame, userId, role, group, ctx));
                break;
            case IT_ADMIN:
                sidebar.add(createPersonalAccountCollapsible(mainFrame, userId, role, ctx));
                sidebar.add(Box.createVerticalStrut(8));
                sidebar.add(createItAdminDirectivesCollapsible(mainFrame, userId, role, group, ctx));
                break;
            default: // NORMAL
                /* [POLYMORPHISM] Each button opens a ModuleScreen via the same show(ctx) contract. */
                sidebar.add(createSidebarButton("My Attendance", e ->
                    AttendanceScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));
                sidebar.add(Box.createVerticalStrut(8));
                sidebar.add(createSidebarButton("My Profile", e ->
                    EmployeeProfile.INSTANCE.show(mainFrame, userId, role, group, ctx)));
                sidebar.add(Box.createVerticalStrut(8));
                sidebar.add(createSidebarButton("My Payroll", e ->
                    EmployeeProfile.INSTANCE.show(mainFrame, userId, role, group, ctx)));
                sidebar.add(Box.createVerticalStrut(8));
                sidebar.add(createSidebarButton("My Leave", e ->
                    LeaveManagementScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));
                break;
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createSidebarButton("Logout", e -> handleLogout(mainFrame)));
        return sidebar;
    }

    private static JButton createSidebarButton(String text, ActionListener action) {
        return createSidebarButton(text, action, 32);
    }

    private static JButton createSidebarButton(String text, ActionListener action, int height) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(BUTTON_ORANGE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Garet", Font.BOLD, 11));
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(BUTTON_ORANGE);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, height));
        btn.addActionListener(action);
        return btn;
    }

    private static final String COLLAPSED = " \u25B6";
    private static final String EXPANDED = " \u25BC";

    private static JPanel createCollapsibleSection(String title, boolean startExpanded, JPanel contentPanel) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(HEADER_DARK);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean[] expanded = { startExpanded };
        contentPanel.setVisible(startExpanded);

        JButton header = new JButton(title + (startExpanded ? EXPANDED : COLLAPSED));
        header.setFont(new Font("Garet", Font.BOLD, 11));
        header.setForeground(TEXT_WHITE);
        header.setBackground(HEADER_DARK);
        header.setBorderPainted(false);
        header.setFocusPainted(false);
        header.setContentAreaFilled(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.addActionListener(e -> {
            expanded[0] = !expanded[0];
            contentPanel.setVisible(expanded[0]);
            header.setText(title + (expanded[0] ? EXPANDED : COLLAPSED));
            section.revalidate();
            section.repaint();
        });

        section.add(header);
        section.add(contentPanel);
        return section;
    }

    private static JPanel createPersonalAccountCollapsible(JFrame mainFrame, String userId, String role, ApplicationContext ctx) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(HEADER_DARK);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        /* [POLYMORPHISM] ModuleScreen.show(mainFrame, userId, role, group, ctx) for each module. */
        content.add(createSidebarButton("My Attendance", e ->
            AttendanceScreen.INSTANCE.show(mainFrame, userId, role, RoleGroup.NORMAL, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("My Profile", e ->
            EmployeeProfile.INSTANCE.show(mainFrame, userId, role, RoleGroup.NORMAL, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("My Payroll", e ->
            EmployeeProfile.INSTANCE.show(mainFrame, userId, role, RoleGroup.NORMAL, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("My Leave", e ->
            LeaveManagementScreen.INSTANCE.show(mainFrame, userId, role, RoleGroup.NORMAL, ctx)));

        return createCollapsibleSection("Personal Account", false, content);
    }

    private static JPanel createHrDirectivesCollapsible(JFrame mainFrame, String userId, String role, RoleGroup group, ApplicationContext ctx) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(HEADER_DARK);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(createSidebarButton("Attendance Management", e ->
            AttendanceScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("Employee Profile", e ->
            EmployeeProfile.INSTANCE.show(mainFrame, userId, role, group, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("Leave Management", e ->
            LeaveManagementScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));

        return createCollapsibleSection("Directives", false, content);
    }

    private static JPanel createPayrollDirectivesCollapsible(JFrame mainFrame, String userId, String role, RoleGroup group, ApplicationContext ctx) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(HEADER_DARK);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(createSidebarButton("Payroll Management", e ->
            EmployeeProfile.INSTANCE.show(mainFrame, userId, role, group, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("View Attendance", e ->
            AttendanceScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("View Leave Requests", e ->
            LeaveManagementScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));

        return createCollapsibleSection("Directives", false, content);
    }

    private static JPanel createItAdminDirectivesCollapsible(JFrame mainFrame, String userId, String role, RoleGroup group, ApplicationContext ctx) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(HEADER_DARK);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(createSidebarButton("Attendance Management", e ->
            AttendanceScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("<html><center>Employee Profile &<br>Payroll Management</center></html>", e ->
            EmployeeProfile.INSTANCE.show(mainFrame, userId, role, group, ctx), 48));
        content.add(Box.createVerticalStrut(6));
        content.add(createSidebarButton("Leave Management", e ->
            LeaveManagementScreen.INSTANCE.show(mainFrame, userId, role, group, ctx)));

        return createCollapsibleSection("Directives", false, content);
    }

    private static JPanel createContentPanel(JFrame mainFrame, String userId, String role, String email, ApplicationContext ctx) {
        JPanel contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START, getWidth(), 0, GRADIENT_END);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel centerContent = new JPanel(new BorderLayout());
        centerContent.setOpaque(false);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        JLabel logoLabel = createContentLogoLabel();
        logoPanel.add(logoLabel);
        centerContent.add(logoPanel, BorderLayout.NORTH);

        JLabel copyrightLabel = new JLabel("© 2026 GEAR.HR - All rights reserved");
        copyrightLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        copyrightLabel.setForeground(TEXT_WHITE);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(false);
        southPanel.add(copyrightLabel);
        centerContent.add(southPanel, BorderLayout.SOUTH);

        contentPanel.add(centerContent, BorderLayout.CENTER);
        return contentPanel;
    }

    private static JLabel createContentLogoLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/3.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(290, 90, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(resizedLogo));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return logoLabel;
        } catch (Exception e) {
            JLabel logoLabel = new JLabel("GEAR.HR");
            logoLabel.setFont(new Font("Garet", Font.BOLD, 32));
            logoLabel.setForeground(TEXT_WHITE);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return logoLabel;
        }
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ApplicationContext();
        SplashScreen.showSplash(() -> User.showLoginScreen(null, ctx));
    }
}
