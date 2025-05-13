package utils;

import controller.AdminController;
import controller.BookingController;
import controller.MovieController;
import controller.UserController;
import model.Booking;
import model.Cinema;
import model.Movie;
import model.Seat;
import model.Snack;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DataInitializer provides sample data for testing
 */
public class DataInitializer {
    // Singleton instance of each controller
    private static UserController userController;
    private static MovieController movieController;
    private static BookingController bookingController;
    private static AdminController adminController;
    
    // Flag to prevent multiple initializations
    private static boolean initialized = false;
    
    /**
     * Initialize the application data with sample values
     */
    public static void initializeData() {
        if (initialized) return;
        
        // Get controller instances
        userController = new UserController();
        movieController = new MovieController();
        bookingController = new BookingController();
        adminController = new AdminController();
        
        // Set cross-controller references
        adminController.setBookingController(bookingController);
        
        // Initialize sample data
        initializeCinemas();
        initializeMovies();
        initializeUsers();
        initializeBookings();
        
        initialized = true;
    }
    
    /**
     * Initialize sample cinemas
     */
    private static void initializeCinemas() {
        // Create three cinemas in Cagayan de Oro
        Cinema cinema1 = new Cinema(1, "CineBook Centrio", "Premium cinema at Centrio Mall", 100, true);
        Cinema cinema2 = new Cinema(2, "CineBook Limketkai", "Largest cinema at Limketkai Mall", 120, true);
        Cinema cinema3 = new Cinema(3, "CineBook Gaisano", "Family cinema at Gaisano Mall", 80, false);
        
        // Add to controllers
        movieController.addCinema(cinema1);
        movieController.addCinema(cinema2);
        movieController.addCinema(cinema3);
        
        adminController.addCinema(cinema1);
        adminController.addCinema(cinema2);
        adminController.addCinema(cinema3);
        
        // Initialize seat maps
        bookingController.initializeCinemaSeats(cinema1);
        bookingController.initializeCinemaSeats(cinema2);
        bookingController.initializeCinemaSeats(cinema3);
    }
    
    /**
     * Initialize sample movies
     */
    private static void initializeMovies() {
        List<Cinema> cinemas = movieController.getAllCinemas();
        
        // Create sample movies for Cinema 1
        Movie movie1 = new Movie(1, "The Last Stand", "Action", 120, "Steven Spielberg",
                "A retired special forces colonel fights to protect his border town from a drug cartel leader.",
                "movie1.svg", "PG-13", cinemas.get(0));
        
        Movie movie2 = new Movie(2, "Love in the Moonlight", "Romance", 110, "Sofia Coppola",
                "Two strangers meet on a midnight train and share a magical night in the city.",
                "movie2.svg", "PG", cinemas.get(0));
        
        // Create sample movies for Cinema 2
        Movie movie3 = new Movie(3, "The Haunting of Hill House", "Horror", 145, "James Wan",
                "A family moves into a seemingly peaceful mansion, only to discover terrifying secrets.",
                "movie3.svg", "R", cinemas.get(1));
        
        Movie movie4 = new Movie(4, "Space Odyssey: The Final Frontier", "Sci-Fi", 160, "Christopher Nolan",
                "Astronauts embark on a mission to find a new habitable planet for humanity.",
                "movie4.svg", "PG-13", cinemas.get(1));
        
        // Create sample movies for Cinema 3
        Movie movie5 = new Movie(5, "Laugh Out Loud", "Comedy", 95, "Judd Apatow",
                "A stand-up comedian finds unexpected success through a viral video.",
                "movie5.svg", "PG-13", cinemas.get(2));
        
        Movie movie6 = new Movie(6, "Toy Universe 4", "Animation", 105, "Pete Docter",
                "Beloved toys come to life and embark on an adventure to save their owner.",
                "movie6.svg", "G", cinemas.get(2));
        
        // Add schedules to movies
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfterTomorrow = today.plusDays(2);
        
        // Schedules for movie1
        movie1.addSchedule(LocalDateTime.of(today, LocalTime.of(10, 0)));
        movie1.addSchedule(LocalDateTime.of(today, LocalTime.of(14, 30)));
        movie1.addSchedule(LocalDateTime.of(today, LocalTime.of(19, 0)));
        movie1.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(10, 0)));
        movie1.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(14, 30)));
        
        // Schedules for movie2
        movie2.addSchedule(LocalDateTime.of(today, LocalTime.of(12, 15)));
        movie2.addSchedule(LocalDateTime.of(today, LocalTime.of(17, 0)));
        movie2.addSchedule(LocalDateTime.of(today, LocalTime.of(21, 30)));
        movie2.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(12, 15)));
        
        // Schedules for movie3
        movie3.addSchedule(LocalDateTime.of(today, LocalTime.of(11, 30)));
        movie3.addSchedule(LocalDateTime.of(today, LocalTime.of(15, 45)));
        movie3.addSchedule(LocalDateTime.of(today, LocalTime.of(20, 15)));
        movie3.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(15, 45)));
        
        // Schedules for movie4
        movie4.addSchedule(LocalDateTime.of(today, LocalTime.of(13, 0)));
        movie4.addSchedule(LocalDateTime.of(today, LocalTime.of(17, 30)));
        movie4.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(13, 0)));
        movie4.addSchedule(LocalDateTime.of(dayAfterTomorrow, LocalTime.of(17, 30)));
        
        // Schedules for movie5
        movie5.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(11, 0)));
        movie5.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(15, 15)));
        movie5.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(19, 45)));
        
        // Schedules for movie6
        movie6.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(10, 30)));
        movie6.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(14, 0)));
        movie6.addSchedule(LocalDateTime.of(tomorrow, LocalTime.of(17, 15)));
        
        // Add to controllers
        movieController.addMovie(movie1);
        movieController.addMovie(movie2);
        movieController.addMovie(movie3);
        movieController.addMovie(movie4);
        movieController.addMovie(movie5);
        movieController.addMovie(movie6);
        
        adminController.addMovie(movie1);
        adminController.addMovie(movie2);
        adminController.addMovie(movie3);
        adminController.addMovie(movie4);
        adminController.addMovie(movie5);
        adminController.addMovie(movie6);
    }
    
    /**
     * Initialize sample users
     */
    private static void initializeUsers() {
        // Create sample users
        User user1 = new User(2, "john_doe", "password123", "John Doe", 
                             "john.doe@example.com", "09171234567", false);
        
        User user2 = new User(3, "jane_smith", "password456", "Jane Smith", 
                             "jane.smith@example.com", "09181234567", false);
                             
        // Add to controller (admin user is already added in constructor)
        userController.addUser(user1);
        userController.addUser(user2);
    }
    
    /**
     * Initialize sample bookings
     */
    private static void initializeBookings() {
        // Simulate a few bookings
        try {
            List<Movie> movies = movieController.getAllMovies();
            List<User> users = userController.getAllUsers();
            
            // Booking 1: John books The Last Stand
            Movie movie1 = movies.get(0); // The Last Stand
            User john = users.get(1); // John Doe
            LocalDateTime schedule1 = movie1.getSchedules().get(0); // First schedule
            
            Booking booking1 = new Booking(john, movie1, schedule1);
            
            // Add seats
            List<Seat> availableSeats = bookingController.getAvailableSeats(movie1, schedule1);
            booking1.addSeat(availableSeats.get(10)); // B1
            booking1.addSeat(availableSeats.get(11)); // B2
            
            // Add snacks
            List<Snack> snacks = bookingController.getAvailableSnacks();
            booking1.addSnack(snacks.get(0)); // Regular Popcorn
            booking1.addSnack(snacks.get(4)); // Regular Soda
            
            // Process payment
            booking1.processPayment("Credit Card");
            
            // Finalize booking
            bookingController.addBooking(booking1);
            john.addBooking(booking1);
            
            // Booking 2: Jane books Space Odyssey
            Movie movie4 = movies.get(3); // Space Odyssey
            User jane = users.get(2); // Jane Smith
            LocalDateTime schedule2 = movie4.getSchedules().get(1); // Second schedule
            
            Booking booking2 = new Booking(jane, movie4, schedule2);
            
            // Add seats
            availableSeats = bookingController.getAvailableSeats(movie4, schedule2);
            booking2.addSeat(availableSeats.get(55)); // F6
            booking2.addSeat(availableSeats.get(56)); // F7
            booking2.addSeat(availableSeats.get(57)); // F8
            
            // Add snacks
            booking2.addSnack(snacks.get(10)); // Movie Combo 1
            
            // Process payment
            booking2.processPayment("GCash");
            
            // Finalize booking
            bookingController.addBooking(booking2);
            jane.addBooking(booking2);
            
        } catch (Exception e) {
            System.err.println("Error initializing sample bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get singleton UserController instance
     */
    public static UserController getUserController() {
        if (!initialized) initializeData();
        return userController;
    }
    
    /**
     * Get singleton MovieController instance
     */
    public static MovieController getMovieController() {
        if (!initialized) initializeData();
        return movieController;
    }
    
    /**
     * Get singleton BookingController instance
     */
    public static BookingController getBookingController() {
        if (!initialized) initializeData();
        return bookingController;
    }
    
    /**
     * Get singleton AdminController instance
     */
    public static AdminController getAdminController() {
        if (!initialized) initializeData();
        return adminController;
    }
}
