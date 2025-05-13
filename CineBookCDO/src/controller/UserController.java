package controller;

import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * UserController handles user management operations
 */
public class UserController {
    private List<User> users;
    private int nextUserId;
    
    public UserController() {
        this.users = new ArrayList<>();
        this.nextUserId = 1;
        
        // Add a default admin user
        User admin = new User(nextUserId++, "admin", "admin123", "System Administrator", 
                "admin@cinebookcdo.com", "09123456789", true);
        users.add(admin);
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Find user by username
     * @param username The username to search for
     * @return The user if found, null otherwise
     */
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Login a user
     * @param username The username
     * @param password The password
     * @return The user if credentials are valid, null otherwise
     */
    public User login(String username, String password) {
        User user = findUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        
        return null;
    }
    
    /**
     * Register a new user
     * @param username The username
     * @param password The password
     * @param fullName The full name
     * @param email The email
     * @param phoneNumber The phone number
     * @return The created user, or null if username already exists
     */
    public User registerUser(String username, String password, String fullName, String email, String phoneNumber) {
        // Check if username already exists
        if (findUserByUsername(username) != null) {
            return null;
        }
        
        // Create new user
        User newUser = new User(nextUserId++, username, password, fullName, email, phoneNumber, false);
        users.add(newUser);
        
        return newUser;
    }
    
    /**
     * Create a guest user (no username/password)
     * @param fullName The full name
     * @param email The email
     * @param phoneNumber The phone number
     * @return The created guest user
     */
    public User createGuestUser(String fullName, String email, String phoneNumber) {
        User guestUser = new User(nextUserId++, fullName, email, phoneNumber);
        users.add(guestUser);
        
        return guestUser;
    }
    
    /**
     * Add existing user to the system (for data initialization)
     * @param user The user to add
     */
    public void addUser(User user) {
        // Update nextUserId if necessary
        if (user.getId() >= nextUserId) {
            nextUserId = user.getId() + 1;
        }
        
        users.add(user);
    }
}
