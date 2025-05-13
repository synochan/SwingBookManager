package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingController handles all booking-related operations
 */
public class BookingController {
    private List<Booking> bookings;
    private List<Snack> availableSnacks;
    
    // Maps to keep track of seat availability
    private Map<String, Map<LocalDateTime, List<Seat>>> cinemaSeats;
    
    public BookingController() {
        this.bookings = new ArrayList<>();
        this.availableSnacks = new ArrayList<>();
        this.cinemaSeats = new HashMap<>();
        
        // Initialize snacks
        initializeSnacks();
    }
    
    /**
     * Initialize available snacks
     */
    private void initializeSnacks() {
        availableSnacks.add(new Snack(1, "Regular Popcorn", "Freshly popped corn", 120.0, "FOOD", "popcorn_regular.svg"));
        availableSnacks.add(new Snack(2, "Large Popcorn", "Extra large serving of popcorn", 180.0, "FOOD", "popcorn_large.svg"));
        availableSnacks.add(new Snack(3, "Caramel Popcorn", "Sweet caramel glazed popcorn", 150.0, "FOOD", "popcorn_caramel.svg"));
        availableSnacks.add(new Snack(4, "Cheese Popcorn", "Cheesy flavored popcorn", 150.0, "FOOD", "popcorn_cheese.svg"));
        
        availableSnacks.add(new Snack(5, "Regular Soda", "16oz soda drink", 80.0, "DRINK", "soda_regular.svg"));
        availableSnacks.add(new Snack(6, "Large Soda", "24oz soda drink", 110.0, "DRINK", "soda_large.svg"));
        availableSnacks.add(new Snack(7, "Bottled Water", "500ml purified water", 50.0, "DRINK", "water_bottle.svg"));
        availableSnacks.add(new Snack(8, "Iced Tea", "16oz sweet iced tea", 90.0, "DRINK", "iced_tea.svg"));
        
        availableSnacks.add(new Snack(9, "Nachos", "Crispy nachos with cheese dip", 150.0, "FOOD", "nachos.svg"));
        availableSnacks.add(new Snack(10, "Hotdog Sandwich", "Classic hotdog sandwich", 120.0, "FOOD", "hotdog.svg"));
        
        availableSnacks.add(new Snack(11, "Movie Combo 1", "Regular popcorn + Regular soda", 180.0, "COMBO", "combo_1.svg"));
        availableSnacks.add(new Snack(12, "Movie Combo 2", "Large popcorn + 2 Regular sodas", 250.0, "COMBO", "combo_2.svg"));
        availableSnacks.add(new Snack(13, "Family Combo", "2 Large popcorn + 4 Regular sodas + Nachos", 450.0, "COMBO", "combo_family.svg"));
    }
    
    /**
     * Initialize seat maps for a cinema
     * @param cinema The cinema to initialize seats for
     */
    public void initializeCinemaSeats(Cinema cinema) {
        cinemaSeats.put(String.valueOf(cinema.getId()), new HashMap<>());
    }
    
    /**
     * Get all available snacks
     * @return List of available snacks
     */
    public List<Snack> getAvailableSnacks() {
        return new ArrayList<>(availableSnacks);
    }
    
    /**
     * Create a new booking
     * @param user The user making the booking
     * @param movie The movie to book
     * @param schedule The selected schedule
     * @return The created booking
     */
    public Booking createBooking(User user, Movie movie, LocalDateTime schedule) {
        Booking booking = new Booking(user, movie, schedule);
        return booking;
    }
    
    /**
     * Get available seats for a movie and schedule
     * @param movie The movie
     * @param schedule The schedule
     * @return List of available seats
     */
    public List<Seat> getAvailableSeats(Movie movie, LocalDateTime schedule) {
        Cinema cinema = movie.getCinema();
        String cinemaId = String.valueOf(cinema.getId());
        
        // Initialize cinema seats for this schedule if not already done
        if (!cinemaSeats.containsKey(cinemaId)) {
            cinemaSeats.put(cinemaId, new HashMap<>());
        }
        
        Map<LocalDateTime, List<Seat>> scheduleSeats = cinemaSeats.get(cinemaId);
        
        if (!scheduleSeats.containsKey(schedule)) {
            // Initialize seats for this schedule
            List<Seat> seats = new ArrayList<>();
            
            // Get the seat template from the cinema
            List<Seat> templateSeats = cinema.getSeats();
            
            // Create copies of seats for this specific movie and schedule
            for (Seat templateSeat : templateSeats) {
                Seat seat = new Seat(templateSeat, movie, schedule);
                seats.add(seat);
            }
            
            scheduleSeats.put(schedule, seats);
        }
        
        // Get seats for this schedule
        List<Seat> availableSeats = scheduleSeats.get(schedule);
        
        // Mark seats as occupied if they are already booked
        for (Booking booking : bookings) {
            if (booking.getMovie().getId() == movie.getId() && 
                booking.getSchedule().equals(schedule)) {
                
                for (Seat bookedSeat : booking.getSelectedSeats()) {
                    // Find corresponding seat in availableSeats
                    for (Seat seat : availableSeats) {
                        if (seat.getSeatNumber().equals(bookedSeat.getSeatNumber())) {
                            seat.setOccupied(true);
                        }
                    }
                }
            }
        }
        
        return availableSeats;
    }
    
    /**
     * Finalize a booking
     * @param booking The booking to finalize
     * @return true if successful, false otherwise
     */
    public boolean finalizeBooking(Booking booking) {
        if (booking.isPaid()) {
            // Add the booking to the list
            bookings.add(booking);
            
            // Add the booking to the user
            booking.getUser().addBooking(booking);
            
            // Mark selected seats as occupied
            for (Seat seat : booking.getSelectedSeats()) {
                seat.setOccupied(true);
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Cancel a booking
     * @param booking The booking to cancel
     */
    public void cancelBooking(Booking booking) {
        // Remove from bookings list if it exists
        bookings.remove(booking);
    }
    
    /**
     * Get all bookings
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
    
    /**
     * Add an existing booking (for data initialization)
     * @param booking The booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}
