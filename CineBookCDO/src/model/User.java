package model;

import java.util.ArrayList;
import java.util.List;

/**
 * User class representing a customer who can book tickets
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean isAdmin;
    private List<Booking> bookings;
    
    // Constructor for registered users
    public User(int id, String username, String password, String fullName, 
                String email, String phoneNumber, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
        this.bookings = new ArrayList<>();
    }
    
    // Constructor for guest users (no username/password)
    public User(int id, String fullName, String email, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isAdmin = false;
        this.bookings = new ArrayList<>();
    }
    
    // Add a booking to this user
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
    
    // Get all bookings for this user
    public List<Booking> getBookings() {
        return new ArrayList<>(bookings); // Return a copy to prevent external modification
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public boolean isGuest() {
        return username == null || username.isEmpty();
    }
    
    @Override
    public String toString() {
        return fullName + " (" + (isGuest() ? "Guest" : username) + ")";
    }
}
