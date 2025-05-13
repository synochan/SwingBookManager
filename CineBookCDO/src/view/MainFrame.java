package view;

import controller.AdminController;
import controller.BookingController;
import controller.MovieController;
import controller.UserController;
import model.Movie;
import model.User;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

/**
 * MainFrame is the main window of the application
 * It manages all panels and handles navigation between them
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Controllers
    private UserController userController;
    private MovieController movieController;
    private BookingController bookingController;
    private AdminController adminController;
    
    // Panels
    private SplashScreen splashScreen;
    private LoginPanel loginPanel;
    private MovieListingPanel movieListingPanel;
    private AdminPanel adminPanel;
    
    // Currently logged in user
    private User loggedInUser;
    
    public MainFrame() {
        // Initialize controllers
        userController = new UserController();
        movieController = new MovieController();
        bookingController = new BookingController();
        adminController = new AdminController();
        
        // Set up the frame
        setTitle("CineBook CDO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Center on screen
        
        // Use card layout for switching between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Initialize first panel (splash screen)
        splashScreen = new SplashScreen(this);
        cardPanel.add(splashScreen, "splash");
        
        // Add card panel to the frame
        add(cardPanel);
        
        // Add window listener to confirm exit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }
    
    /**
     * Initialize all panels after data is loaded
     */
    public void initializeApplication() {
        // Create login panel
        loginPanel = new LoginPanel(userController, this);
        cardPanel.add(loginPanel, "login");
        
        // Show login panel
        cardLayout.show(cardPanel, "login");
    }
    
    /**
     * Set the currently logged in user
     * @param user The logged in user
     */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    
    /**
     * Get the currently logged in user
     * @return The logged in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
    
    /**
     * Show the movie listing panel for customer bookings
     */
    public void showMovieListingPanel() {
        // Check if the panel already exists
        if (movieListingPanel == null) {
            movieListingPanel = new MovieListingPanel(movieController, this, loggedInUser);
            cardPanel.add(movieListingPanel, "movieListing");
        } else {
            // If user changed, recreate the panel
            cardPanel.remove(movieListingPanel);
            movieListingPanel = new MovieListingPanel(movieController, this, loggedInUser);
            cardPanel.add(movieListingPanel, "movieListing");
        }
        
        cardLayout.show(cardPanel, "movieListing");
    }
    
    /**
     * Show the admin panel
     * @param adminUser The admin user
     */
    public void showAdminPanel(User adminUser) {
        if (!adminUser.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Only administrators can access this panel.",
                "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        setLoggedInUser(adminUser);
        
        // Create new admin panel instance
        adminPanel = new AdminPanel(adminController, this, adminUser);
        cardPanel.add(adminPanel, "admin");
        
        cardLayout.show(cardPanel, "admin");
    }
    
    /**
     * Start the booking process for a selected movie and schedule
     * @param movie The selected movie
     * @param schedule The selected schedule
     */
    public void startBookingProcess(Movie movie, LocalDateTime schedule) {
        BookingPanel bookingPanel = new BookingPanel(this, bookingController, loggedInUser, movie, schedule);
        cardPanel.add(bookingPanel, "booking");
        cardLayout.show(cardPanel, "booking");
    }
    
    /**
     * Log out the current user and return to login screen
     */
    public void logout() {
        this.loggedInUser = null;
        
        // Show login panel
        cardLayout.show(cardPanel, "login");
        
        // Clear any sensitive data
        if (movieListingPanel != null) {
            cardPanel.remove(movieListingPanel);
            movieListingPanel = null;
        }
        
        if (adminPanel != null) {
            cardPanel.remove(adminPanel);
            adminPanel = null;
        }
    }
    
    /**
     * Confirm application exit
     */
    private void confirmExit() {
        int response = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit CineBook CDO?",
            "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}
