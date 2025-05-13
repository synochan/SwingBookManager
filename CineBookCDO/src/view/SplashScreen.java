package view;

import utils.DataInitializer;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SplashScreen displays a loading screen on application startup
 */
public class SplashScreen extends JPanel {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private MainFrame parentFrame;
    
    public SplashScreen(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Set up constraints for center alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Add logo
        JLabel logoLabel = createLogoLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        contentPanel.add(logoLabel, gbc);
        
        // Add app name
        JLabel nameLabel = new JLabel("CineBook CDO");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 32));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        contentPanel.add(nameLabel, gbc);
        
        // Add tagline
        JLabel taglineLabel = new JLabel("Your Premier Cinema Booking Experience in Cagayan de Oro");
        taglineLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        taglineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        contentPanel.add(taglineLabel, gbc);
        
        // Add progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(400, 25));
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(progressBar, gbc);
        
        // Add status label
        statusLabel = new JLabel("Initializing...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 10, 10);
        contentPanel.add(statusLabel, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Start the loading process
        startLoading();
    }
    
    /**
     * Create a logo label with either an SVG icon or text fallback
     */
    private JLabel createLogoLabel() {
        // Create a placeholder for logo (in a real app, load an SVG icon)
        JLabel logoLabel = new JLabel();
        
        // Create a basic placeholder logo
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Set antialiasing for smoother rendering
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw a rounded rectangle as background
                g2d.setColor(new Color(0, 123, 255));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                
                String text = "CDC";
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                
                g2d.drawString(text, (getWidth() - textWidth) / 2, 
                    (getHeight() - textHeight) / 2 + fm.getAscent());
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(150, 150);
            }
        };
        
        logoPanel.setOpaque(false);
        logoLabel.add(logoPanel);
        logoLabel.setPreferredSize(new Dimension(150, 150));
        
        return logoLabel;
    }
    
    /**
     * Start the loading process in a background thread
     */
    private void startLoading() {
        // Create a timer to simulate loading process
        Timer timer = new Timer(50, new ActionListener() {
            private int progress = 0;
            private String[] loadingMessages = {
                "Initializing application...",
                "Loading cinema data...",
                "Loading movie information...",
                "Setting up seating arrangements...",
                "Preparing user interface...",
                "Almost ready..."
            };
            
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 2;
                
                if (progress <= 100) {
                    progressBar.setValue(progress);
                    
                    // Update loading message at certain intervals
                    int messageIndex = progress / 20; // Change message every 20%
                    if (messageIndex < loadingMessages.length) {
                        statusLabel.setText(loadingMessages[messageIndex]);
                    }
                    
                    // When progress is 30%, actually initialize the data
                    if (progress == 30) {
                        // Initialize data in a separate thread to avoid UI freezing
                        new Thread(() -> {
                            DataInitializer.initializeData();
                        }).start();
                    }
                } else {
                    // Stop the timer
                    ((Timer)e.getSource()).stop();
                    
                    // Initialize the main application
                    parentFrame.initializeApplication();
                }
            }
        });
        
        // Start the timer
        timer.start();
    }
}
