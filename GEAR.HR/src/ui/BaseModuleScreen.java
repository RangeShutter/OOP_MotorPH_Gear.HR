package ui;

import javax.swing.*;
import java.awt.*;

/**
 * [ABSTRACTION] Abstract base for module screens; provides shared colors and layout helpers.
 * [INHERITANCE] Subclasses use protected colors and createFrame/createHeaderPanel/createFooterPanel
 * to avoid duplication. Content is built by each subclass.
 */
public abstract class BaseModuleScreen {

    /** [ABSTRACTION] [INHERITANCE] Shared color constants for module screens. */
    protected static final Color BACKGROUND_WHITE = Color.WHITE;
    protected static final Color HEADER_DARK = new Color(34, 34, 34);
    protected static final Color CARD_WHITE = Color.WHITE;
    protected static final Color BORDER_GREY = new Color(68, 68, 68);
    protected static final Color TEXT_WHITE = Color.WHITE;
    protected static final Color TEXT_GREY = new Color(180, 180, 180);
    protected static final Color TEXT_BLACK = Color.BLACK;
    protected static final Color ACCENT_GREY = new Color(120, 120, 120);
    protected static final Color BUTTON_ORANGE = new Color(255, 153, 28);
    protected static final Color GRADIENT_START = new Color(93, 224, 230);
    protected static final Color GRADIENT_END = new Color(0, 74, 173);

    /**
     * [ABSTRACTION] [INHERITANCE] Creates a standard module JFrame (icon, size, dispose on close).
     */
    protected static JFrame createFrame(JFrame parent, String title, int width, int height) {
        JFrame f = new JFrame(title);
        f.setSize(width, height);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLocationRelativeTo(parent);
        f.setResizable(false);
        try {
            f.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) { /* ignore */ }
        return f;
    }

    /**
     * [ABSTRACTION] [INHERITANCE] Creates a standard header panel with title and subtitle.
     */
    protected static JPanel createHeaderPanel(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(HEADER_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Garet", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        subLabel.setForeground(TEXT_GREY);
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(titleLabel, BorderLayout.NORTH);
        top.add(subLabel, BorderLayout.CENTER);
        p.add(top, BorderLayout.CENTER);
        return p;
    }

    /**
     * [ABSTRACTION] [INHERITANCE] Creates a simple footer panel with the given text.
     * Subclasses may override in their own class for custom footers (e.g. gradient).
     */
    protected static JPanel createFooterPanel(String footerText) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(HEADER_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel lab = new JLabel(footerText);
        lab.setFont(new Font("Garet", Font.PLAIN, 12));
        lab.setForeground(TEXT_WHITE);
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lab, BorderLayout.CENTER);
        return p;
    }
}
