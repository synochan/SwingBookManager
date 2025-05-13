package model;

/**
 * PaymentMethod enum representing available payment options
 */
public enum PaymentMethod {
    GCASH("GCash", "Pay using GCash e-wallet"),
    PAYMAYA("PayMaya", "Pay using PayMaya e-wallet"),
    CREDIT_CARD("Credit Card", "Pay using Visa, MasterCard, or AmEx");
    
    private final String name;
    private final String description;
    
    PaymentMethod(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
