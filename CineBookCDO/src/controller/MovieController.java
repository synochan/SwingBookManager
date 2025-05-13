package controller;

import model.Cinema;
import model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * MovieController handles movie-related operations for users
 */
public class MovieController {
    private List<Movie> movies;
    private List<Cinema> cinemas;
    
    public MovieController() {
        movies = new ArrayList<>();
        cinemas = new ArrayList<>();
    }
    
    /**
     * Get all movies
     * @return List of all movies
     */
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }
    
    /**
     * Get all active movies
     * @return List of active movies
     */
    public List<Movie> getAllActiveMovies() {
        List<Movie> activeMovies = new ArrayList<>();
        
        for (Movie movie : movies) {
            if (movie.isActive()) {
                activeMovies.add(movie);
            }
        }
        
        return activeMovies;
    }
    
    /**
     * Get all cinemas
     * @return List of all cinemas
     */
    public List<Cinema> getAllCinemas() {
        return new ArrayList<>(cinemas);
    }
    
    /**
     * Filter movies based on criteria
     * @param cinema The cinema filter (null or ID=0 for all)
     * @param genre The genre filter ("All Genres" for all)
     * @param searchTerm The search term (empty for all)
     * @return Filtered list of movies
     */
    public List<Movie> filterMovies(Cinema cinema, String genre, String searchTerm) {
        List<Movie> filteredMovies = new ArrayList<>();
        
        for (Movie movie : movies) {
            // Skip inactive movies
            if (!movie.isActive()) continue;
            
            // Apply cinema filter
            if (cinema != null && cinema.getId() != 0 && 
                movie.getCinema().getId() != cinema.getId()) {
                continue;
            }
            
            // Apply genre filter
            if (genre != null && !genre.equals("All Genres") && 
                !movie.getGenre().equalsIgnoreCase(genre)) {
                continue;
            }
            
            // Apply search term filter
            if (searchTerm != null && !searchTerm.isEmpty() && 
                !movie.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) &&
                !movie.getDirector().toLowerCase().contains(searchTerm.toLowerCase())) {
                continue;
            }
            
            // Movie passed all filters
            filteredMovies.add(movie);
        }
        
        return filteredMovies;
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
        movies.add(movie);
    }
    
    /**
     * Find a movie by ID
     * @param id The movie ID
     * @return The movie if found, null otherwise
     */
    public Movie findMovieById(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }
    
    /**
     * Find a cinema by ID
     * @param id The cinema ID
     * @return The cinema if found, null otherwise
     */
    public Cinema findCinemaById(int id) {
        for (Cinema cinema : cinemas) {
            if (cinema.getId() == id) {
                return cinema;
            }
        }
        return null;
    }
}
