package ui;

import javax.swing.*;
import java.awt.*;

/**
 * SplashScreen provides an animated loading screen before the main application loads.
 */
public class SplashScreen {
    private static final int SPLASH_WIDTH = 500;
    private static final int SPLASH_HEIGHT = 350;
    private static final int LOGO_SIZE = 200;
    private static final int DISPLAY_DURATION = 2000;

    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);
    private static final Color TEXT_WHITE = Color.WHITE;

    public static void showSplash(Runnable onFinish) {
        JWindow splashWindow = createSplashWindow();
        JPanel mainPanel = createMainPanel();
        splashWindow.getContentPane().add(mainPanel);
        splashWindow.setVisible(true);
        Timer closeTimer = createCloseTimer(splashWindow, onFinish);
        closeTimer.start();
    }

    private static JWindow createSplashWindow() {
        JWindow splashWindow = new JWindow();
        splashWindow.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
        splashWindow.setLocationRelativeTo(null);
        splashWindow.setAlwaysOnTop(true);
        return splashWindow;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
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
        mainPanel.setOpaque(false);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        contentPanel.add(createLogoLabel(), BorderLayout.CENTER);
        contentPanel.add(createLoadingLabel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private static JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/Icon.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(LOGO_SIZE, LOGO_SIZE, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(resizedLogo));
        } catch (Exception e) {
            logoLabel.setText("GEAR.HR");
            logoLabel.setFont(new Font("Garet", Font.BOLD, 32));
            logoLabel.setForeground(TEXT_WHITE);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        return logoLabel;
    }

    private static JLabel createLoadingLabel() {
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        loadingLabel.setForeground(TEXT_WHITE);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return loadingLabel;
    }

    private static Timer createCloseTimer(JWindow splashWindow, Runnable onFinish) {
        Timer timer = new Timer(DISPLAY_DURATION, e -> {
            splashWindow.setVisible(false);
            splashWindow.dispose();
            if (onFinish != null) onFinish.run();
        });
        timer.setRepeats(false);
        return timer;
    }
}
