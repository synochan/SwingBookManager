package controller;

import model.Booking;
import model.Cinema;
import model.Movie;
import model.Seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AdminController handles administrative operations
 */
public class AdminController {
    private List<Movie> movies;
    private List<Cinema> cinemas;
    private List<Booking> bookings;
    private int nextMovieId;
    
    // Reference to other controllers
    private BookingController bookingController;
    
    public AdminController() {
        this.movies = new ArrayList<>();
        this.cinemas = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.nextMovieId = 1;
        this.bookingController = new BookingController();
    }
    
    /**
     * Set the booking controller reference
     * @param bookingController The booking controller
     */
    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
    
    /**
     * Get all movies
     * @return List of all movies
     */
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }
    
    /**
     * Get all cinemas
     * @return List of all cinemas
     */
    public List<Cinema> getAllCinemas() {
        return new ArrayList<>(cinemas);
    }
    
    /**
     * Get all bookings
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingController.getAllBookings();
    }
    
    /**
     * Add a new movie
     * @param title The movie title
     * @param genre The movie genre
     * @param durationMinutes The duration in minutes
     * @param director The director
     * @param synopsis The synopsis
     * @param rating The movie rating
     * @param cinema The cinema
     * @param isActive Whether the movie is active
     * @return The created movie
     */
    public Movie addMovie(String title, String genre, int durationMinutes, String director, 
                          String synopsis, String rating, Cinema cinema, boolean isActive) {
        
        Movie movie = new Movie(nextMovieId++, title, genre, durationMinutes, director, 
                                synopsis, "movie_placeholder.svg", rating, cinema);
        movie.setActive(isActive);
        
        movies.add(movie);
        cinema.addMovie(movie);
        
        return movie;
    }
    
    /**
     * Update an existing movie
     * @param movie The movie to update
     * @return true if successful, false otherwise
     */
    public boolean updateMovie(Movie movie) {
        // In a real application, we might need to find and update the movie
        // Since we're using references, the movie is already updated
        
        return true;
    }
    
    /**
     * Delete a movie
     * @param movie The movie to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteMovie(Movie movie) {
        // Remove from cinema's movies list
        movie.getCinema().removeMovie(movie);
        
        // Remove from movies list
        return movies.remove(movie);
    }
    
    /**
     * Add a schedule to a movie
     * @param movie The movie
     * @param schedule The schedule to add
     * @return true if successful, false otherwise
     */
    public boolean addSchedule(Movie movie, LocalDateTime schedule) {
        // Check for conflicts (you could implement more sophisticated checks)
        for (LocalDateTime existing : movie.getSchedules()) {
            if (existing.equals(schedule)) {
                return false; // Schedule already exists
            }
        }
        
        movie.addSchedule(schedule);
        return true;
    }
    
    /**
     * Remove a schedule from a movie
     * @param movie The movie
     * @param schedule The schedule to remove
     * @return true if successful, false otherwise
     */
    public boolean removeSchedule(Movie movie, LocalDateTime schedule) {
        movie.removeSchedule(schedule);
        return true;
    }
    
    /**
     * Get bookings by date
     * @param date The date to filter by
     * @return List of bookings on the specified date
     */
    public List<Booking> getBookingsByDate(LocalDate date) {
        List<Booking> result = new ArrayList<>();
        
        for (Booking booking : getAllBookings()) {
            LocalDateTime bookingDateTime = booking.getSchedule();
            if (bookingDateTime.toLocalDate().equals(date)) {
                result.add(booking);
            }
        }
        
        return result;
    }
    
    /**
     * Generate a sales report for a date range
     * @param fromDate Start date
     * @param toDate End date
     * @return Formatted sales report as string
     */
    public String generateSalesReport(LocalDate fromDate, LocalDate toDate) {
        StringBuilder report = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        
        report.append("SALES REPORT\n");
        report.append("============================================\n");
        report.append("Period: ").append(fromDate.format(dateFormatter))
              .append(" to ").append(toDate.format(dateFormatter)).append("\n");
        report.append("Generated: ").append(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"))).append("\n");
        report.append("============================================\n\n");
        
        // Get bookings in date range
        List<Booking> periodBookings = new ArrayList<>();
        for (Booking booking : getAllBookings()) {
            LocalDateTime bookingDateTime = booking.getBookingTime();
            LocalDate bookingDate = bookingDateTime.toLocalDate();
            
            if ((bookingDate.isEqual(fromDate) || bookingDate.isAfter(fromDate)) && 
                (bookingDate.isEqual(toDate) || bookingDate.isBefore(toDate))) {
                periodBookings.add(booking);
            }
        }
        
        // Summary statistics
        int totalBookings = periodBookings.size();
        double totalSales = 0.0;
        int totalSeats = 0;
        int totalSnacks = 0;
        
        for (Booking booking : periodBookings) {
            totalSales += booking.getTotalAmount();
            totalSeats += booking.getSelectedSeats().size();
            totalSnacks += booking.getSelectedSnacks().size();
        }
        
        report.append("SUMMARY STATISTICS\n");
        report.append("--------------------------------------------\n");
        report.append("Total Bookings: ").append(totalBookings).append("\n");
        report.append("Total Revenue: ₱").append(String.format("%.2f", totalSales)).append("\n");
        report.append("Total Seats Sold: ").append(totalSeats).append("\n");
        report.append("Total Snack Items Sold: ").append(totalSnacks).append("\n\n");
        
        // Sales by cinema
        report.append("SALES BY CINEMA\n");
        report.append("--------------------------------------------\n");
        
        Map<Cinema, Double> cinemaSales = new HashMap<>();
        
        for (Booking booking : periodBookings) {
            Cinema cinema = booking.getMovie().getCinema();
            cinemaSales.put(cinema, cinemaSales.getOrDefault(cinema, 0.0) + booking.getTotalAmount());
        }
        
        for (Map.Entry<Cinema, Double> entry : cinemaSales.entrySet()) {
            report.append(entry.getKey().getName())
                  .append(": ₱")
                  .append(String.format("%.2f", entry.getValue()))
                  .append("\n");
        }
        
        report.append("\n");
        
        // Sales by movie
        report.append("SALES BY MOVIE\n");
        report.append("--------------------------------------------\n");
        
        Map<Movie, Double> movieSales = new HashMap<>();
        Map<Movie, Integer> movieSeats = new HashMap<>();
        
        for (Booking booking : periodBookings) {
            Movie movie = booking.getMovie();
            double amount = booking.getTotalAmount();
            int seats = booking.getSelectedSeats().size();
            
            movieSales.put(movie, movieSales.getOrDefault(movie, 0.0) + amount);
            movieSeats.put(movie, movieSeats.getOrDefault(movie, 0) + seats);
        }
        
        for (Map.Entry<Movie, Double> entry : movieSales.entrySet()) {
            Movie movie = entry.getKey();
            report.append(movie.getTitle())
                  .append(" (").append(movie.getCinema().getName()).append(")")
                  .append("\n   Revenue: ₱").append(String.format("%.2f", entry.getValue()))
                  .append("\n   Tickets: ").append(movieSeats.get(movie))
                  .append("\n\n");
        }
        
        // Daily sales breakdown
        report.append("DAILY SALES BREAKDOWN\n");
        report.append("--------------------------------------------\n");
        
        Map<LocalDate, Double> dailySales = new HashMap<>();
        
        for (Booking booking : periodBookings) {
            LocalDate bookingDate = booking.getBookingTime().toLocalDate();
            dailySales.put(bookingDate, dailySales.getOrDefault(bookingDate, 0.0) + booking.getTotalAmount());
        }
        
        // Sort by date
        List<Map.Entry<LocalDate, Double>> sortedDailySales = dailySales.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toList());
        
        for (Map.Entry<LocalDate, Double> entry : sortedDailySales) {
            report.append(entry.getKey().format(dateFormatter))
                  .append(": ₱")
                  .append(String.format("%.2f", entry.getValue()))
                  .append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Add a cinema (for data initialization)
     * @param cinema The cinema to add
     */
    public void addCinema(Cinema cinema) {
        cinemas.add(cinema);
    }
    
    /**
     * Add a movie (for data initialization)
     * @param movie The movie to add
     */
    public void addMovie(Movie movie) {
        // Update nextMovieId if necessary
        if (movie.getId() >= nextMovieId) {
            nextMovieId = movie.getId() + 1;
        }
        
        movies.add(movie);
    }
}
