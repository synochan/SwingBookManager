import view.MainFrame;
import utils.UIHelper;

import javax.swing.*;

/**
 * Main application entry point
 */
public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            // Use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start application on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}
