package utils;

import javax.swing.*;
import java.awt.*;

/**
 * UIHelper provides utility methods for UI creation and styling
 */
public class UIHelper {
    // Color scheme
    public static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color INFO_COLOR = new Color(23, 162, 184);
    public static final Color LIGHT_COLOR = new Color(248, 249, 250);
    public static final Color DARK_COLOR = new Color(52, 58, 64);
    
    /**
     * Create a styled button with background and foreground colors
     * @param text Button text
     * @param bgColor Background color
     * @param fgColor Foreground color
     * @return Styled JButton
     */
    public static JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        return button;
    }
    
    /**
     * Create a primary action button
     * @param text Button text
     * @return Styled primary button
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR, Color.WHITE);
    }
    
    /**
     * Create a secondary action button
     * @param text Button text
     * @return Styled secondary button
     */
    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, SECONDARY_COLOR, Color.WHITE);
    }
    
    /**
     * Create a success action button
     * @param text Button text
     * @return Styled success button
     */
    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, SUCCESS_COLOR, Color.WHITE);
    }
    
    /**
     * Create a danger/warning action button
     * @param text Button text
     * @return Styled danger button
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, DANGER_COLOR, Color.WHITE);
    }
    
    /**
     * Create a styled heading label
     * @param text Label text
     * @param fontSize Font size
     * @return Styled heading label
     */
    public static JLabel createHeading(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        return label;
    }
    
    /**
     * Create a form input field with label
     * @param labelText Label text
     * @param field Input field component
     * @return Panel with label and field
     */
    public static JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        JLabel label = new JLabel(labelText);
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a panel with padding
     * @param component The component to add
     * @param top Top padding
     * @param left Left padding
     * @param bottom Bottom padding
     * @param right Right padding
     * @return Panel with padding
     */
    public static JPanel createPaddedPanel(Component component, int top, int left, int bottom, int right) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        panel.add(component, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create an empty separator with specified height
     * @param height Height of the separator in pixels
     * @return Panel with specified height
     */
    public static Component createVerticalSpacer(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }
    
    /**
     * Center a component within its parent container
     * @param component The component to center
     * @return Panel with centered component
     */
    public static JPanel centerComponent(Component component) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(component);
        
        return panel;
    }
}
