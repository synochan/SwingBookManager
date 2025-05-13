package view;

import model.Booking;
import model.Seat;
import model.Snack;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for displaying the ticket summary after successful booking
 */
public class TicketSummaryPanel extends JPanel {
    private JPanel ticketPanel;
    private JPanel receiptPanel;
    private Booking currentBooking;
    
    public TicketSummaryPanel(Booking currentBooking) {
        this.currentBooking = currentBooking;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize UI components
        initComponents();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("Booking Confirmation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content with ticket and receipt
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Equal split
        
        // E-Ticket panel (left side)
        ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("E-Ticket"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Placeholder for ticket content
        JLabel ticketPlaceholder = new JLabel("Ticket information will appear here after payment");
        ticketPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        ticketPanel.add(ticketPlaceholder, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(new JScrollPane(ticketPanel));
        
        // Receipt panel (right side)
        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Receipt"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Placeholder for receipt content
        JLabel receiptPlaceholder = new JLabel("Receipt details will appear here after payment");
        receiptPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        receiptPanel.add(receiptPlaceholder, BorderLayout.CENTER);
        
        splitPane.setRightComponent(new JScrollPane(receiptPanel));
        
        add(splitPane, BorderLayout.CENTER);
        
        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel infoLabel = new JLabel("<html><i>A confirmation email will be sent to your registered email address.</i></html>");
        infoPanel.add(infoLabel);
        
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    public void updateTicketInfo() {
        // Clear placeholder content
        ticketPanel.removeAll();
        receiptPanel.removeAll();
        
        // Create ticket panel content
        JPanel ticketContent = new JPanel();
        ticketContent.setLayout(new BoxLayout(ticketContent, BoxLayout.Y_AXIS));
        ticketContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a ticket-like design
        JPanel ticketCard = new JPanel();
        ticketCard.setLayout(new BoxLayout(ticketCard, BoxLayout.Y_AXIS));
        ticketCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        ticketCard.setBackground(new Color(240, 248, 255));
        
        // Cinema logo/name
        JLabel cinemaLabel = new JLabel("CineBook CDO");
        cinemaLabel.setFont(new Font("Arial", Font.BOLD, 24));
        cinemaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(cinemaLabel);
        
        // Confirmation code
        JLabel confirmationLabel = new JLabel("Confirmation: " + currentBooking.getConfirmationCode());
        confirmationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        confirmationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(confirmationLabel);
        
        ticketCard.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Movie title
        JLabel movieLabel = new JLabel(currentBooking.getMovie().getTitle());
        movieLabel.setFont(new Font("Arial", Font.BOLD, 18));
        movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(movieLabel);
        
        // Movie details
        JLabel cinemaLocationLabel = new JLabel(currentBooking.getMovie().getCinema().getName());
        cinemaLocationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(cinemaLocationLabel);
        
        // Format date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        
        JLabel dateLabel = new JLabel(currentBooking.getSchedule().format(formatter));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(dateLabel);
        
        JLabel timeLabel = new JLabel(currentBooking.getSchedule().format(timeFormatter));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(timeLabel);
        
        ticketCard.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Seats
        List<Seat> seats = currentBooking.getSelectedSeats();
        StringBuilder seatsText = new StringBuilder("Seats: ");
        for (int i = 0; i < seats.size(); i++) {
            if (i > 0) seatsText.append(", ");
            seatsText.append(seats.get(i).getSeatNumber());
        }
        
        JLabel seatsLabel = new JLabel(seatsText.toString());
        seatsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        seatsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketCard.add(seatsLabel);
        
        // Add barcode-like element (just for visual effect)
        JPanel barcodePanel = new JPanel();
        barcodePanel.setPreferredSize(new Dimension(200, 60));
        barcodePanel.setMaximumSize(new Dimension(200, 60));
        barcodePanel.setBackground(Color.WHITE);
        barcodePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        barcodePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel barcodeLabel = new JLabel(currentBooking.getConfirmationCode());
        barcodeLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        barcodePanel.add(barcodeLabel);
        
        ticketCard.add(Box.createRigidArea(new Dimension(0, 15)));
        ticketCard.add(barcodePanel);
        
        // Add ticket card to content panel
        ticketContent.add(ticketCard);
        ticketPanel.add(new JScrollPane(ticketContent), BorderLayout.CENTER);
        
        // Create receipt panel content
        JPanel receiptContent = new JPanel();
        receiptContent.setLayout(new BoxLayout(receiptContent, BoxLayout.Y_AXIS));
        receiptContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Receipt header
        JLabel receiptHeaderLabel = new JLabel("CineBook CDO - Receipt");
        receiptHeaderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        receiptContent.add(receiptHeaderLabel);
        
        // Transaction details
        JLabel transactionIdLabel = new JLabel("Transaction ID: " + currentBooking.getBookingId());
        receiptContent.add(transactionIdLabel);
        
        DateTimeFormatter bookingFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm:ss a");
        JLabel bookingTimeLabel = new JLabel("Booking Time: " + 
            currentBooking.getBookingTime().format(bookingFormatter));
        receiptContent.add(bookingTimeLabel);
        
        JLabel paymentMethodLabel = new JLabel("Payment Method: " + currentBooking.getPaymentMethod());
        receiptContent.add(paymentMethodLabel);
        
        receiptContent.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Items purchased
        JLabel itemsHeaderLabel = new JLabel("Items Purchased:");
        itemsHeaderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        receiptContent.add(itemsHeaderLabel);
        
        // Movie tickets
        JLabel ticketsLabel = new JLabel("Movie Tickets (" + seats.size() + "):");
        receiptContent.add(ticketsLabel);
        
        double ticketsTotal = 0;
        for (Seat seat : seats) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 5));
            
            itemPanel.add(new JLabel(currentBooking.getMovie().getTitle() + " - " + 
                seat.getSeatNumber() + " (" + seat.getType().getLabel() + ")"), 
                BorderLayout.WEST);
            
            JLabel priceLabel = new JLabel("₱" + String.format("%.2f", seat.getPrice()));
            itemPanel.add(priceLabel, BorderLayout.EAST);
            
            receiptContent.add(itemPanel);
            ticketsTotal += seat.getPrice();
        }
        
        JPanel ticketsTotalPanel = new JPanel(new BorderLayout());
        ticketsTotalPanel.setBorder(BorderFactory.createEmptyBorder(2, 20, 5, 5));
        ticketsTotalPanel.add(new JLabel("Tickets Subtotal:"), BorderLayout.WEST);
        ticketsTotalPanel.add(new JLabel("₱" + String.format("%.2f", ticketsTotal)), 
            BorderLayout.EAST);
        
        receiptContent.add(ticketsTotalPanel);
        
        // Snacks, if any
        List<Snack> snacks = currentBooking.getSelectedSnacks();
        if (!snacks.isEmpty()) {
            receiptContent.add(Box.createRigidArea(new Dimension(0, 10)));
            
            JLabel snacksLabel = new JLabel("Snacks & Drinks:");
            receiptContent.add(snacksLabel);
            
            double snacksTotal = 0;
            for (Snack snack : snacks) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 5));
                
                itemPanel.add(new JLabel(snack.getName()), BorderLayout.WEST);
                
                JLabel priceLabel = new JLabel("₱" + String.format("%.2f", snack.getPrice()));
                itemPanel.add(priceLabel, BorderLayout.EAST);
                
                receiptContent.add(itemPanel);
                snacksTotal += snack.getPrice();
            }
            
            JPanel snacksTotalPanel = new JPanel(new BorderLayout());
            snacksTotalPanel.setBorder(BorderFactory.createEmptyBorder(2, 20, 5, 5));
            snacksTotalPanel.add(new JLabel("Snacks Subtotal:"), BorderLayout.WEST);
            snacksTotalPanel.add(new JLabel("₱" + String.format("%.2f", snacksTotal)), 
                BorderLayout.EAST);
            
            receiptContent.add(snacksTotalPanel);
        }
        
        // Total amount
        receiptContent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        
        JLabel totalLabel = new JLabel("TOTAL AMOUNT:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(totalLabel, BorderLayout.WEST);
        
        JLabel totalAmountLabel = new JLabel("₱" + String.format("%.2f", currentBooking.getTotalAmount()));
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(totalAmountLabel, BorderLayout.EAST);
        
        receiptContent.add(totalPanel);
        
        // Thank you message
        receiptContent.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel thankYouLabel = new JLabel("Thank you for choosing CineBook CDO!");
        thankYouLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        thankYouLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptContent.add(thankYouLabel);
        
        receiptPanel.add(new JScrollPane(receiptContent), BorderLayout.CENTER);
        
        // Refresh display
        revalidate();
        repaint();
    }
}
