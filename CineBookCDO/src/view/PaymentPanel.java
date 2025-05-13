package view;

import controller.BookingController;
import model.Booking;
import model.PaymentMethod;
import model.Seat;
import model.Snack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for payment processing
 */
public class PaymentPanel extends JPanel {
    private JPanel paymentDetailsPanel;
    private JPanel orderSummaryPanel;
    private ButtonGroup paymentMethodGroup;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JTextField cvvField;
    private JTextField accountNameField;
    private JTextField accountNumberField;
    private JPanel cardDetailsPanel;
    private JPanel eWalletDetailsPanel;
    
    private BookingController bookingController;
    private Booking currentBooking;
    private PaymentMethod selectedPaymentMethod;
    
    public PaymentPanel(BookingController bookingController, Booking currentBooking) {
        this.bookingController = bookingController;
        this.currentBooking = currentBooking;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize UI components
        initComponents();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("Payment Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel with payment details and order summary
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6); // 60% for payment details, 40% for order summary
        
        // Payment details panel (left side)
        paymentDetailsPanel = new JPanel();
        paymentDetailsPanel.setLayout(new BoxLayout(paymentDetailsPanel, BoxLayout.Y_AXIS));
        paymentDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Payment method selection
        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(new BoxLayout(methodPanel, BoxLayout.Y_AXIS));
        methodPanel.setBorder(BorderFactory.createTitledBorder("Select Payment Method"));
        
        paymentMethodGroup = new ButtonGroup();
        
        // Create radio buttons for each payment method
        for (PaymentMethod method : PaymentMethod.values()) {
            JRadioButton radioButton = new JRadioButton(method.getName());
            radioButton.setActionCommand(method.name());
            
            // Add tooltip with description
            radioButton.setToolTipText(method.getDescription());
            
            // Add to button group
            paymentMethodGroup.add(radioButton);
            
            // Add to panel
            methodPanel.add(radioButton);
            methodPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            
            // Add event listener
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedPaymentMethod = PaymentMethod.valueOf(e.getActionCommand());
                    updatePaymentDetailsPanel();
                }
            });
        }
        
        paymentDetailsPanel.add(methodPanel);
        paymentDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Card details panel
        cardDetailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        cardDetailsPanel.setBorder(BorderFactory.createTitledBorder("Credit Card Details"));
        
        cardDetailsPanel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField();
        cardDetailsPanel.add(cardNumberField);
        
        cardDetailsPanel.add(new JLabel("Expiry Date (MM/YY):"));
        expiryField = new JTextField();
        cardDetailsPanel.add(expiryField);
        
        cardDetailsPanel.add(new JLabel("CVV:"));
        cvvField = new JTextField();
        cardDetailsPanel.add(cvvField);
        
        cardDetailsPanel.setVisible(false); // Hidden by default
        
        paymentDetailsPanel.add(cardDetailsPanel);
        
        // E-wallet details panel (for GCash and PayMaya)
        eWalletDetailsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        eWalletDetailsPanel.setBorder(BorderFactory.createTitledBorder("E-Wallet Details"));
        
        eWalletDetailsPanel.add(new JLabel("Account Name:"));
        accountNameField = new JTextField();
        eWalletDetailsPanel.add(accountNameField);
        
        eWalletDetailsPanel.add(new JLabel("Account Number:"));
        accountNumberField = new JTextField();
        eWalletDetailsPanel.add(accountNumberField);
        
        eWalletDetailsPanel.setVisible(false); // Hidden by default
        
        paymentDetailsPanel.add(eWalletDetailsPanel);
        
        // Add info text
        JLabel infoLabel = new JLabel("<html><i>Note: This is a simulation. No actual payment will be processed.</i></html>");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
        paymentDetailsPanel.add(infoLabel);
        
        // Add filler to push components to the top
        paymentDetailsPanel.add(Box.createVerticalGlue());
        
        splitPane.setLeftComponent(new JScrollPane(paymentDetailsPanel));
        
        // Order summary panel (right side)
        orderSummaryPanel = new JPanel();
        orderSummaryPanel.setLayout(new BorderLayout());
        orderSummaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        
        // Create the order summary content
        updateOrderSummary();
        
        splitPane.setRightComponent(new JScrollPane(orderSummaryPanel));
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void updatePaymentDetailsPanel() {
        // Hide all detail panels first
        cardDetailsPanel.setVisible(false);
        eWalletDetailsPanel.setVisible(false);
        
        // Show appropriate panel based on selected payment method
        if (selectedPaymentMethod == PaymentMethod.CREDIT_CARD) {
            cardDetailsPanel.setVisible(true);
        } else if (selectedPaymentMethod == PaymentMethod.GCASH || 
                   selectedPaymentMethod == PaymentMethod.PAYMAYA) {
            eWalletDetailsPanel.setVisible(true);
            
            // Update title based on selected e-wallet
            ((javax.swing.border.TitledBorder) eWalletDetailsPanel.getBorder()).setTitle(
                selectedPaymentMethod.getName() + " Details");
        }
        
        revalidate();
        repaint();
    }
    
    private void updateOrderSummary() {
        // Clear current content
        orderSummaryPanel.removeAll();
        
        // Use a panel with BoxLayout for the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Movie details
        JLabel movieLabel = new JLabel("Movie: " + currentBooking.getMovie().getTitle());
        movieLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(movieLabel);
        
        // Format date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        JLabel scheduleLabel = new JLabel("Schedule: " + 
            currentBooking.getSchedule().format(formatter));
        contentPanel.add(scheduleLabel);
        
        JLabel cinemaLabel = new JLabel("Cinema: " + currentBooking.getMovie().getCinema().getName());
        contentPanel.add(cinemaLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Seats
        List<Seat> seats = currentBooking.getSelectedSeats();
        contentPanel.add(new JLabel("Selected Seats:"));
        
        double seatsTotal = 0;
        for (Seat seat : seats) {
            JPanel seatPanel = new JPanel(new BorderLayout());
            seatPanel.setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 0));
            
            seatPanel.add(new JLabel(seat.getSeatNumber() + " - " + seat.getType().getLabel()), 
                BorderLayout.WEST);
            seatPanel.add(new JLabel("₱" + String.format("%.2f", seat.getPrice())), 
                BorderLayout.EAST);
            
            contentPanel.add(seatPanel);
            seatsTotal += seat.getPrice();
        }
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Snacks
        List<Snack> snacks = currentBooking.getSelectedSnacks();
        if (!snacks.isEmpty()) {
            contentPanel.add(new JLabel("Snacks:"));
            
            double snacksTotal = 0;
            for (Snack snack : snacks) {
                JPanel snackPanel = new JPanel(new BorderLayout());
                snackPanel.setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 0));
                
                snackPanel.add(new JLabel(snack.getName()), BorderLayout.WEST);
                snackPanel.add(new JLabel("₱" + String.format("%.2f", snack.getPrice())), 
                    BorderLayout.EAST);
                
                contentPanel.add(snackPanel);
                snacksTotal += snack.getPrice();
            }
            
            JPanel snacksTotalPanel = new JPanel(new BorderLayout());
            snacksTotalPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
            
            snacksTotalPanel.add(new JLabel("Snacks Subtotal:"), BorderLayout.WEST);
            snacksTotalPanel.add(new JLabel("₱" + String.format("%.2f", snacksTotal)), 
                BorderLayout.EAST);
            
            contentPanel.add(snacksTotalPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 0, 5, 0)
        ));
        
        totalPanel.add(new JLabel("TOTAL:"), BorderLayout.WEST);
        totalPanel.add(new JLabel("₱" + String.format("%.2f", currentBooking.getTotalAmount())), 
            BorderLayout.EAST);
        
        contentPanel.add(totalPanel);
        
        // Add the content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        orderSummaryPanel.add(scrollPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    public boolean validatePayment() {
        // Check if a payment method is selected
        if (selectedPaymentMethod == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a payment method",
                "Payment Required", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate payment details based on selected method
        if (selectedPaymentMethod == PaymentMethod.CREDIT_CARD) {
            // Validate credit card details
            if (cardNumberField.getText().trim().isEmpty() ||
                expiryField.getText().trim().isEmpty() ||
                cvvField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "Please fill in all credit card details",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Basic validation of card number format (16 digits)
            String cardNumber = cardNumberField.getText().trim().replaceAll("\\s+", "");
            if (!cardNumber.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid 16-digit card number",
                    "Invalid Card Number", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Basic validation of expiry date format (MM/YY)
            if (!expiryField.getText().trim().matches("\\d{2}/\\d{2}")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter expiry date in MM/YY format",
                    "Invalid Expiry Date", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Basic validation of CVV (3 digits)
            if (!cvvField.getText().trim().matches("\\d{3}")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid 3-digit CVV",
                    "Invalid CVV", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
        } else if (selectedPaymentMethod == PaymentMethod.GCASH || 
                   selectedPaymentMethod == PaymentMethod.PAYMAYA) {
            // Validate e-wallet details
            if (accountNameField.getText().trim().isEmpty() ||
                accountNumberField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "Please fill in all " + selectedPaymentMethod.getName() + " details",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Basic validation of account number (11 digits for mobile number)
            if (!accountNumberField.getText().trim().matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid 11-digit mobile number",
                    "Invalid Account Number", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    public boolean processPayment() {
        // In a real app, this would connect to a payment gateway
        // For this simulation, we'll just show a success message
        
        // Set the payment method on the booking
        currentBooking.processPayment(selectedPaymentMethod.getName());
        
        // Simulate payment processing
        try {
            // Show processing message
            JOptionPane.showMessageDialog(this,
                "Processing payment...",
                "Please Wait", JOptionPane.INFORMATION_MESSAGE);
            
            // Simulate delay
            Thread.sleep(1500);
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Payment successful!",
                "Payment Confirmed", JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
