package model;

/**
 * Snack class representing food and drinks available for purchase
 */
public class Snack {
    private int id;
    private String name;
    private String description;
    private double price;
    private String category; // "FOOD" or "DRINK" or "COMBO"
    private String imageFile; // Image filename or path reference
    private boolean isAvailable;
    
    public Snack(int id, String name, String description, double price, String category, String imageFile) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageFile = imageFile;
        this.isAvailable = true;
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
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getImageFile() {
        return imageFile;
    }
    
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    @Override
    public String toString() {
        return name + " - ₱" + String.format("%.2f", price);
    }
}
