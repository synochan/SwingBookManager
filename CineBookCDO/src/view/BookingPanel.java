package view;

import controller.BookingController;
import model.Booking;
import model.Movie;
import model.User;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Panel to manage the booking process, serving as the main container for other booking-related panels
 */
public class BookingPanel extends JPanel {
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel footerPanel;
    private JButton nextButton;
    private JButton backButton;
    private JButton cancelButton;
    
    private CardLayout cardLayout;
    private SeatSelectionPanel seatSelectionPanel;
    private SnackSelectionPanel snackSelectionPanel;
    private PaymentPanel paymentPanel;
    private TicketSummaryPanel ticketSummaryPanel;
    
    private MainFrame parentFrame;
    private BookingController bookingController;
    private User currentUser;
    private Movie selectedMovie;
    private LocalDateTime selectedSchedule;
    private Booking currentBooking;
    
    // Keep track of the current step
    private int currentStep = 0;
    private final String[] STEPS = {"Select Seats", "Add Snacks", "Payment", "Ticket Summary"};
    
    public BookingPanel(MainFrame parentFrame, BookingController bookingController, 
                        User currentUser, Movie selectedMovie, LocalDateTime selectedSchedule) {
        this.parentFrame = parentFrame;
        this.bookingController = bookingController;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedSchedule = selectedSchedule;
        
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Initialize the booking
        this.currentBooking = bookingController.createBooking(currentUser, selectedMovie, selectedSchedule);
        
        // Initialize UI components
        initComponents();
    }
    
    private void initComponents() {
        // Header panel with movie info
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(0, 0, 10, 0)
        ));
        
        // Movie title and schedule
        JPanel movieInfoPanel = new JPanel(new GridLayout(3, 1));
        
        JLabel titleLabel = new JLabel(selectedMovie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        movieInfoPanel.add(titleLabel);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        JLabel scheduleLabel = new JLabel(selectedSchedule.format(formatter));
        movieInfoPanel.add(scheduleLabel);
        
        JLabel cinemaLabel = new JLabel(selectedMovie.getCinema().getName());
        movieInfoPanel.add(cinemaLabel);
        
        headerPanel.add(movieInfoPanel, BorderLayout.WEST);
        
        // Step indicator
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel stepLabel = new JLabel("Step 1 of " + STEPS.length + ": " + STEPS[0]);
        stepPanel.add(stepLabel);
        headerPanel.add(stepPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with card layout for different booking steps
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Initialize all step panels
        seatSelectionPanel = new SeatSelectionPanel(bookingController, currentBooking);
        snackSelectionPanel = new SnackSelectionPanel(bookingController, currentBooking);
        paymentPanel = new PaymentPanel(bookingController, currentBooking);
        ticketSummaryPanel = new TicketSummaryPanel(currentBooking);
        
        // Add all panels to card layout
        contentPanel.add(seatSelectionPanel, STEPS[0]);
        contentPanel.add(snackSelectionPanel, STEPS[1]);
        contentPanel.add(paymentPanel, STEPS[2]);
        contentPanel.add(ticketSummaryPanel, STEPS[3]);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer panel with navigation buttons
        footerPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        backButton = new JButton("Back");
        backButton.setEnabled(false); // Disabled at first step
        
        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(0, 123, 255));
        nextButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel Booking");
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        // Add event listeners
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextStep();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousStep();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    BookingPanel.this,
                    "Are you sure you want to cancel this booking?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (choice == JOptionPane.YES_OPTION) {
                    // Cancel booking and return to movie listing
                    bookingController.cancelBooking(currentBooking);
                    parentFrame.showMovieListingPanel();
                }
            }
        });
    }
    
    private void nextStep() {
        // Validate current step before proceeding
        if (!validateCurrentStep()) {
            return;
        }
        
        if (currentStep < STEPS.length - 1) {
            currentStep++;
            updateStepUI();
            cardLayout.show(contentPanel, STEPS[currentStep]);
            
            // Update button text for last step
            if (currentStep == STEPS.length - 1) {
                nextButton.setText("Finish");
            }
        } else {
            // This is the last step - complete booking
            finishBooking();
        }
    }
    
    private void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateStepUI();
            cardLayout.show(contentPanel, STEPS[currentStep]);
            nextButton.setText("Next");
        }
    }
    
    private void updateStepUI() {
        // Update the step indicator in the header
        JLabel stepLabel = (JLabel) ((JPanel) headerPanel.getComponent(1)).getComponent(0);
        stepLabel.setText("Step " + (currentStep + 1) + " of " + STEPS.length + ": " + STEPS[currentStep]);
        
        // Enable/disable back button based on current step
        backButton.setEnabled(currentStep > 0);
    }
    
    private boolean validateCurrentStep() {
        switch (currentStep) {
            case 0: // Seat Selection
                if (currentBooking.getSelectedSeats().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please select at least one seat",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                
                if (currentBooking.getSelectedSeats().size() > 6) {
                    JOptionPane.showMessageDialog(this,
                        "Maximum 6 seats allowed per booking",
                        "Too Many Seats", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
                
            case 1: // Snack Selection
                // No validation needed, snacks are optional
                return true;
                
            case 2: // Payment
                if (!paymentPanel.validatePayment()) {
                    JOptionPane.showMessageDialog(this,
                        "Please complete payment information",
                        "Payment Required", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                
                // Process the payment
                if (!paymentPanel.processPayment()) {
                    JOptionPane.showMessageDialog(this,
                        "Payment processing failed. Please try again.",
                        "Payment Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                // If payment was successful, update the booking receipt
                ticketSummaryPanel.updateTicketInfo();
                return true;
                
            default:
                return true;
        }
    }
    
    private void finishBooking() {
        // Complete the booking process
        boolean success = bookingController.finalizeBooking(currentBooking);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Booking completed successfully!\n" +
                "Your confirmation code is: " + currentBooking.getConfirmationCode() + "\n" +
                "A confirmation has been sent to your email.",
                "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            
            // Simulate sending email (just print to console for simplicity)
            System.out.println("------ CONFIRMATION EMAIL SENT ------");
            System.out.println("To: " + currentUser.getEmail());
            System.out.println("Subject: Your CineBook CDO Ticket Confirmation - " + currentBooking.getConfirmationCode());
            System.out.println("Movie: " + selectedMovie.getTitle());
            System.out.println("Schedule: " + selectedSchedule.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
            System.out.println("Cinema: " + selectedMovie.getCinema().getName());
            System.out.println("Seats: " + currentBooking.getSelectedSeats().size() + " seat(s)");
            System.out.println("Total Amount: ₱" + String.format("%.2f", currentBooking.getTotalAmount()));
            System.out.println("------------------------------------");
            
            // Return to movie listing
            parentFrame.showMovieListingPanel();
        } else {
            JOptionPane.showMessageDialog(this,
                "There was a problem finalizing your booking. Please try again.",
                "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
