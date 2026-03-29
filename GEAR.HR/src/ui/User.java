package ui;

import model.Employee;
import service.AuthContext;
import service.ApplicationContext;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * User class handles login UI; authentication delegated to AuthenticationService (OOP redesign).
 * [ABSTRACTION] Delegates authentication to {@link service.IAuthenticationService} via {@link ApplicationContext}.
 */
public class User {

    // Application color scheme
    private static final Color BACKGROUND_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);
    private static final Color TEXT_BLACK = Color.BLACK;
    private static final Color ACCENT_GREY = new Color(120, 120, 120);
    private static final Color LOGIN_BUTTON_ORANGE = new Color(255, 153, 28);
    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);

    /**
     * Displays the main login screen. Requires ApplicationContext for authentication (DI).
     */
    public static void showLoginScreen(JFrame parentFrame, ApplicationContext ctx) {
        JFrame loginFrame = createLoginFrame(parentFrame);
        JPanel mainPanel = createMainPanel();
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel(loginFrame, ctx);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }

    private static JFrame createLoginFrame(JFrame parentFrame) {
        JFrame loginFrame = new JFrame("GEAR.HR - Login");
        loginFrame.setSize(1000, 750);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(parentFrame);
        loginFrame.setResizable(false);
        try {
            loginFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {}
        return loginFrame;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setPreferredSize(new Dimension(1000, 750));
        return mainPanel;
    }

    private static JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout()) {
            /** [INHERITANCE] Overrides JComponent.paintComponent to draw gradient background. */
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
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        leftPanel.add(createLogoLabel(), BorderLayout.CENTER);
        leftPanel.add(createHeaderLabel(), BorderLayout.NORTH);
        leftPanel.add(createFooterLabel(), BorderLayout.SOUTH);
        return leftPanel;
    }

    private static JLabel createLogoLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/Icon.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
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

    private static JLabel createHeaderLabel() {
        JLabel headerLabel = new JLabel("Welcome to GEAR.HR");
        headerLabel.setFont(new Font("Garet", Font.BOLD, 22));
        headerLabel.setForeground(TEXT_WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return headerLabel;
    }

    private static JLabel createFooterLabel() {
        JLabel footerLabel = new JLabel("© 2026 GEAR.HR    |    Secure Login");
        footerLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        footerLabel.setForeground(TEXT_WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return footerLabel;
    }

    private static JPanel createRightPanel(JFrame loginFrame, ApplicationContext ctx) {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BACKGROUND_WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        rightPanel.add(createTitleLabel(), constraints);
        constraints.gridy++;
        rightPanel.add(createSubtitleLabel(), constraints);
        constraints.gridy++;
        constraints.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = createFieldLabel("User ID:");
        rightPanel.add(usernameLabel, constraints);
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 20, 0);
        JTextField usernameField = createTextField();
        rightPanel.add(usernameField, constraints);
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.gridy++;
        JLabel passwordLabel = createFieldLabel("Password:");
        rightPanel.add(passwordLabel, constraints);
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 20, 0);
        JPasswordField passwordField = createPasswordField();
        rightPanel.add(passwordField, constraints);
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.gridy++;
        constraints.gridwidth = 2;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        actionPanel.setOpaque(false);
        JButton loginButton = createLoginButton(loginFrame, usernameField, passwordField, ctx);
        JButton forgetPasswordButton = createForgetPasswordButton(loginFrame, ctx);
        actionPanel.add(loginButton);
        actionPanel.add(forgetPasswordButton);
        rightPanel.add(actionPanel, constraints);
        return rightPanel;
    }

    private static JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    private static JLabel createSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Please enter your credentials");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        subtitleLabel.setForeground(ACCENT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    private static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Garet", Font.BOLD, 14));
        label.setForeground(TEXT_BLACK);
        return label;
    }

    private static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Garet", Font.PLAIN, 14));
        textField.setBackground(BACKGROUND_WHITE);
        textField.setForeground(TEXT_BLACK);
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        textField.setPreferredSize(new Dimension(260, 32));
        return textField;
    }

    private static JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Garet", Font.PLAIN, 14));
        passwordField.setBackground(BACKGROUND_WHITE);
        passwordField.setForeground(TEXT_BLACK);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        passwordField.setPreferredSize(new Dimension(260, 32));
        return passwordField;
    }

    private static JButton createLoginButton(JFrame loginFrame, JTextField usernameField, JPasswordField passwordField, ApplicationContext ctx) {
        JButton loginButton = createStyledButton("Sign In", LOGIN_BUTTON_ORANGE, TEXT_WHITE);
        loginButton.setPreferredSize(new Dimension(120, 36));
        loginButton.addActionListener(e -> authenticateUser(loginFrame, usernameField, passwordField, ctx));
        return loginButton;
    }

    private static JButton createForgetPasswordButton(JFrame loginFrame, ApplicationContext ctx) {
        JButton button = createStyledButton("Forget Password", ACCENT_GREY, TEXT_WHITE);
        button.setPreferredSize(new Dimension(160, 36));
        button.addActionListener(e -> showSendTicketDialog(loginFrame, ctx));
        return button;
    }

    private static void authenticateUser(JFrame loginFrame, JTextField usernameField, JPasswordField passwordField, ApplicationContext ctx) {
        String userId = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        Employee authenticated = ctx.getAuthenticationService().authenticate(userId, password);
        if (authenticated != null) {
            AuthContext authContext = ctx.getAuthenticationService().getAuthContext(userId);
            loginFrame.dispose();
            Main.showMainScreen(userId, authContext.getRole(), authContext.getEmail(), ctx);
        } else {
            JOptionPane.showMessageDialog(loginFrame,
                "Invalid credentials! Please check your User ID and Password.",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showSendTicketDialog(JFrame parentFrame, ApplicationContext ctx) {
        JTextField userIdField = createTextField();
        JComboBox<String> requestTypeCombo = new JComboBox<>(new String[]{"Forgot Password"});

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("UserID Requestor"));
        form.add(userIdField);
        form.add(new JLabel("Type of Request"));
        form.add(requestTypeCombo);

        int result = JOptionPane.showConfirmDialog(
            parentFrame,
            form,
            "Sending ticket",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String error = ctx.getItTicketService().createTicket(
            userIdField.getText().trim(),
            String.valueOf(requestTypeCombo.getSelectedItem())
        );
        if (error != null) {
            JOptionPane.showMessageDialog(parentFrame, error, "Ticket Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(parentFrame, "Ticket submitted successfully with Pending status.");
    }

    private static JButton createStyledButton(String text, Color backgroundColor, Color foregroundColor) {
        JButton button = new JButton(text) {
            /** [INHERITANCE] Overrides JComponent.paintComponent for custom rounded button appearance. */
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = getHeight();
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Garet", Font.BOLD, 14));
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

}
