package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Booking class representing a movie ticket booking
 */
public class Booking {
    private String bookingId; // UUID for unique identification
    private User user;
    private Movie movie;
    private LocalDateTime schedule;
    private List<Seat> selectedSeats;
    private List<Snack> selectedSnacks;
    private LocalDateTime bookingTime;
    private double totalAmount;
    private String paymentMethod;
    private boolean isPaid;
    private String confirmationCode;
    
    public Booking(User user, Movie movie, LocalDateTime schedule) {
        this.bookingId = UUID.randomUUID().toString();
        this.user = user;
        this.movie = movie;
        this.schedule = schedule;
        this.selectedSeats = new ArrayList<>();
        this.selectedSnacks = new ArrayList<>();
        this.bookingTime = LocalDateTime.now();
        this.isPaid = false;
        this.confirmationCode = generateConfirmationCode();
    }
    
    // Generate a random confirmation code
    private String generateConfirmationCode() {
        // Generate a simple 8-character alphanumeric code
        return "CDO" + bookingId.substring(0, 8).toUpperCase();
    }
    
    // Add a seat to this booking
    public void addSeat(Seat seat) {
        if (selectedSeats.size() < 6) { // Maximum 6 seats per booking
            selectedSeats.add(seat);
            calculateTotalAmount();
        }
    }
    
    // Remove a seat from this booking
    public boolean removeSeat(Seat seat) {
        boolean removed = selectedSeats.remove(seat);
        if (removed) {
            calculateTotalAmount();
        }
        return removed;
    }
    
    // Add a snack to this booking
    public void addSnack(Snack snack) {
        selectedSnacks.add(snack);
        calculateTotalAmount();
    }
    
    // Remove a snack from this booking
    public boolean removeSnack(Snack snack) {
        boolean removed = selectedSnacks.remove(snack);
        if (removed) {
            calculateTotalAmount();
        }
        return removed;
    }
    
    // Calculate the total booking amount
    private void calculateTotalAmount() {
        double seatsTotal = 0;
        for (Seat seat : selectedSeats) {
            seatsTotal += seat.getPrice();
        }
        
        double snacksTotal = 0;
        for (Snack snack : selectedSnacks) {
            snacksTotal += snack.getPrice();
        }
        
        this.totalAmount = seatsTotal + snacksTotal;
    }
    
    // Process payment
    public boolean processPayment(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.isPaid = true; // In a real app, this would depend on successful payment processing
        return true;
    }
    
    // Getters and setters
    public String getBookingId() {
        return bookingId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    public LocalDateTime getSchedule() {
        return schedule;
    }
    
    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }
    
    public List<Seat> getSelectedSeats() {
        return new ArrayList<>(selectedSeats); // Return a copy to prevent external modification
    }
    
    public List<Snack> getSelectedSnacks() {
        return new ArrayList<>(selectedSnacks); // Return a copy to prevent external modification
    }
    
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    
    public String getConfirmationCode() {
        return confirmationCode;
    }
    
    @Override
    public String toString() {
        return confirmationCode + " - " + movie.getTitle() + " - " + selectedSeats.size() + " seats";
    }
}
