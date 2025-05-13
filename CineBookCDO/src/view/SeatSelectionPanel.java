package view;

import controller.BookingController;
import model.Booking;
import model.Seat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for selecting seats for a movie
 */
public class SeatSelectionPanel extends JPanel {
    private JPanel seatGrid;
    private JPanel legendPanel;
    private JPanel selectionSummaryPanel;
    private JLabel selectedSeatsLabel;
    private JLabel totalPriceLabel;
    
    private BookingController bookingController;
    private Booking currentBooking;
    
    // Map to keep track of seat buttons
    private Map<String, JToggleButton> seatButtons = new HashMap<>();
    
    // List to keep track of selected seats
    private List<Seat> selectedSeats = new ArrayList<>();
    
    public SeatSelectionPanel(BookingController bookingController, Booking currentBooking) {
        this.bookingController = bookingController;
        this.currentBooking = currentBooking;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize UI components
        initComponents();
        
        // Load available seats
        loadSeats();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("Select Your Seats (Maximum 6)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Legend panel at the top
        legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Available seat legend
        JPanel availableLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton availableButton = new JButton();
        availableButton.setBackground(Color.WHITE);
        availableButton.setPreferredSize(new Dimension(25, 25));
        availableButton.setEnabled(false);
        availableLegend.add(availableButton);
        availableLegend.add(new JLabel("Available"));
        
        // Selected seat legend
        JPanel selectedLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton selectedButton = new JButton();
        selectedButton.setBackground(new Color(40, 167, 69));
        selectedButton.setPreferredSize(new Dimension(25, 25));
        selectedButton.setEnabled(false);
        selectedLegend.add(selectedButton);
        selectedLegend.add(new JLabel("Selected"));
        
        // Occupied seat legend
        JPanel occupiedLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton occupiedButton = new JButton();
        occupiedButton.setBackground(Color.LIGHT_GRAY);
        occupiedButton.setPreferredSize(new Dimension(25, 25));
        occupiedButton.setEnabled(false);
        occupiedLegend.add(occupiedButton);
        occupiedLegend.add(new JLabel("Occupied"));
        
        // Standard seat legend
        JPanel standardLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton standardButton = new JButton();
        standardButton.setBackground(new Color(220, 220, 220));
        standardButton.setPreferredSize(new Dimension(25, 25));
        standardButton.setEnabled(false);
        standardLegend.add(standardButton);
        standardLegend.add(new JLabel("Standard - ₱200"));
        
        // Deluxe seat legend
        JPanel deluxeLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton deluxeButton = new JButton();
        deluxeButton.setBackground(new Color(255, 193, 7));
        deluxeButton.setPreferredSize(new Dimension(25, 25));
        deluxeButton.setEnabled(false);
        deluxeLegend.add(deluxeButton);
        deluxeLegend.add(new JLabel("Deluxe - ₱300"));
        
        legendPanel.add(availableLegend);
        legendPanel.add(selectedLegend);
        legendPanel.add(occupiedLegend);
        legendPanel.add(standardLegend);
        legendPanel.add(deluxeLegend);
        
        add(legendPanel, BorderLayout.NORTH);
        
        // Center panel with screen at top and seats below
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        
        // Screen panel
        JPanel screenPanel = new JPanel(new BorderLayout());
        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setPreferredSize(new Dimension(400, 30));
        screenLabel.setOpaque(true);
        screenLabel.setBackground(new Color(200, 200, 200));
        screenLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        screenPanel.add(screenLabel, BorderLayout.CENTER);
        centerPanel.add(screenPanel, BorderLayout.NORTH);
        
        // Seat grid
        seatGrid = new JPanel(new GridLayout(10, 10, 5, 5));
        seatGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.add(seatGrid, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Selection summary panel at bottom
        selectionSummaryPanel = new JPanel(new GridLayout(2, 1));
        selectionSummaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        selectedSeatsLabel = new JLabel("Selected Seats: None");
        totalPriceLabel = new JLabel("Total Price: ₱0.00");
        
        selectionSummaryPanel.add(selectedSeatsLabel);
        selectionSummaryPanel.add(totalPriceLabel);
        
        add(selectionSummaryPanel, BorderLayout.SOUTH);
    }
    
    private void loadSeats() {
        // Get all seats for the selected movie and schedule
        List<Seat> availableSeats = bookingController.getAvailableSeats(
            currentBooking.getMovie(), currentBooking.getSchedule());
        
        // Create row labels (A-J)
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        
        // Create columns (1-10)
        int columns = 10;
        
        // Add row labels and seat buttons
        for (char row : rows) {
            for (int col = 1; col <= columns; col++) {
                String seatNumber = row + String.valueOf(col);
                
                JToggleButton seatButton = new JToggleButton(seatNumber);
                seatButton.setPreferredSize(new Dimension(40, 40));
                seatButton.setMargin(new Insets(0, 0, 0, 0));
                seatButton.setFocusPainted(false);
                
                // Find this seat in the available seats list
                Seat seat = findSeatByNumber(availableSeats, seatNumber);
                
                if (seat != null) {
                    // Store the seat with the button
                    seatButton.putClientProperty("seat", seat);
                    
                    // Determine if seat is already occupied
                    if (seat.isOccupied()) {
                        seatButton.setEnabled(false);
                        seatButton.setBackground(Color.LIGHT_GRAY);
                    } else {
                        // Set background color based on seat type
                        if (seat.getType() == Seat.SeatType.STANDARD) {
                            seatButton.setBackground(new Color(220, 220, 220));
                        } else { // DELUXE
                            seatButton.setBackground(new Color(255, 193, 7));
                        }
                        
                        // Add action listener for seat selection
                        seatButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JToggleButton btn = (JToggleButton) e.getSource();
                                Seat selectedSeat = (Seat) btn.getClientProperty("seat");
                                
                                if (btn.isSelected()) {
                                    // Check if already at maximum seats (6)
                                    if (selectedSeats.size() >= 6) {
                                        btn.setSelected(false);
                                        JOptionPane.showMessageDialog(SeatSelectionPanel.this,
                                            "You can only select up to 6 seats per booking",
                                            "Maximum Seats Reached", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                    
                                    // Add seat to selection
                                    selectedSeats.add(selectedSeat);
                                    btn.setBackground(new Color(40, 167, 69)); // Green for selected
                                } else {
                                    // Remove seat from selection
                                    selectedSeats.remove(selectedSeat);
                                    
                                    // Reset color based on seat type
                                    if (selectedSeat.getType() == Seat.SeatType.STANDARD) {
                                        btn.setBackground(new Color(220, 220, 220));
                                    } else { // DELUXE
                                        btn.setBackground(new Color(255, 193, 7));
                                    }
                                }
                                
                                // Update the booking with selected seats
                                updateBookingSeats();
                                
                                // Update summary labels
                                updateSummary();
                            }
                        });
                    }
                } else {
                    // This should not happen if seats are properly initialized
                    seatButton.setEnabled(false);
                    seatButton.setBackground(Color.LIGHT_GRAY);
                }
                
                seatGrid.add(seatButton);
                seatButtons.put(seatNumber, seatButton);
            }
        }
    }
    
    private Seat findSeatByNumber(List<Seat> seats, String seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber().equals(seatNumber)) {
                return seat;
            }
        }
        return null;
    }
    
    private void updateBookingSeats() {
        // Clear current seats in booking
        while (!currentBooking.getSelectedSeats().isEmpty()) {
            currentBooking.removeSeat(currentBooking.getSelectedSeats().get(0));
        }
        
        // Add all selected seats to the booking
        for (Seat seat : selectedSeats) {
            currentBooking.addSeat(seat);
        }
    }
    
    private void updateSummary() {
        if (selectedSeats.isEmpty()) {
            selectedSeatsLabel.setText("Selected Seats: None");
            totalPriceLabel.setText("Total Price: ₱0.00");
        } else {
            StringBuilder seatStr = new StringBuilder("Selected Seats: ");
            double totalPrice = 0.0;
            
            for (int i = 0; i < selectedSeats.size(); i++) {
                Seat seat = selectedSeats.get(i);
                if (i > 0) {
                    seatStr.append(", ");
                }
                seatStr.append(seat.getSeatNumber());
                totalPrice += seat.getPrice();
            }
            
            selectedSeatsLabel.setText(seatStr.toString());
            totalPriceLabel.setText("Total Price: ₱" + String.format("%.2f", totalPrice));
        }
    }
}
