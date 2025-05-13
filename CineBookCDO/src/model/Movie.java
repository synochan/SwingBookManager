package model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Movie class representing a film that can be booked in the cinema
 */
public class Movie {
    private int id;
    private String title;
    private String genre;
    private int durationMinutes;
    private String director;
    private String synopsis;
    private String posterImage; // Image filename or path reference
    private String rating; // G, PG, PG-13, R, etc.
    private List<LocalDateTime> schedules;
    private Cinema cinema;
    private boolean isActive;
    
    public Movie(int id, String title, String genre, int durationMinutes, String director, 
                String synopsis, String posterImage, String rating, Cinema cinema) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.director = director;
        this.synopsis = synopsis;
        this.posterImage = posterImage;
        this.rating = rating;
        this.cinema = cinema;
        this.schedules = new ArrayList<>();
        this.isActive = true;
    }
    
    // Add a schedule for this movie
    public void addSchedule(LocalDateTime schedule) {
        schedules.add(schedule);
    }
    
    // Remove a schedule
    public void removeSchedule(LocalDateTime schedule) {
        schedules.remove(schedule);
    }
    
    // Clear all schedules
    public void clearSchedules() {
        schedules.clear();
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getSynopsis() {
        return synopsis;
    }
    
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    
    public String getPosterImage() {
        return posterImage;
    }
    
    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }
    
    public String getRating() {
        return rating;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
    
    public List<LocalDateTime> getSchedules() {
        return new ArrayList<>(schedules); // Return a copy to prevent external modification
    }
    
    public Cinema getCinema() {
        return cinema;
    }
    
    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return title + " (" + genre + ") - " + durationMinutes + " mins";
    }
}
