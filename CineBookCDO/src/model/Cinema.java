package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cinema class representing a cinema within the movie theater complex
 */
public class Cinema {
    private int id;
    private String name;
    private String description;
    private int seatingCapacity;
    private boolean has3D;
    private List<Seat> seats;
    private List<Movie> movies;
    
    public Cinema(int id, String name, String description, int seatingCapacity, boolean has3D) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seatingCapacity = seatingCapacity;
        this.has3D = has3D;
        this.seats = new ArrayList<>();
        this.movies = new ArrayList<>();
        
        // Initialize seats for this cinema
        initializeSeats();
    }
    
    // Initialize the seat grid layout for this cinema
    private void initializeSeats() {
        // Create a standard seat layout with rows A-J and columns 1-10
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        int columns = 10;
        
        for (char row : rows) {
            for (int col = 1; col <= columns; col++) {
                String seatNumber = row + String.valueOf(col);
                
                // Make the last 3 rows Deluxe seats, others Standard
                Seat.SeatType type = (row >= 'H') ? Seat.SeatType.DELUXE : Seat.SeatType.STANDARD;
                
                seats.add(new Seat(seatNumber, type, this));
            }
        }
    }
    
    // Add a movie to this cinema
    public void addMovie(Movie movie) {
        if (!movies.contains(movie)) {
            movies.add(movie);
        }
    }
    
    // Remove a movie from this cinema
    public boolean removeMovie(Movie movie) {
        return movies.remove(movie);
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getSeatingCapacity() {
        return seatingCapacity;
    }
    
    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }
    
    public boolean isHas3D() {
        return has3D;
    }
    
    public void setHas3D(boolean has3D) {
        this.has3D = has3D;
    }
    
    public List<Seat> getSeats() {
        return new ArrayList<>(seats); // Return a copy to prevent external modification
    }
    
    public List<Movie> getMovies() {
        return new ArrayList<>(movies); // Return a copy to prevent external modification
    }
    
    @Override
    public String toString() {
        return name + (has3D ? " (3D)" : "");
    }
}
