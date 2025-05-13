package model;

/**
 * Seat class representing a seat in the cinema
 */
public class Seat {
    public enum SeatType {
        STANDARD("Standard", 200.0),
        DELUXE("Deluxe", 300.0);
        
        private final String label;
        private final double price;
        
        SeatType(String label, double price) {
            this.label = label;
            this.price = price;
        }
        
        public String getLabel() {
            return label;
        }
        
        public double getPrice() {
            return price;
        }
    }
    
    private String seatNumber; // e.g., A1, B5, etc.
    private SeatType type;
    private boolean isOccupied;
    private Cinema cinema;
    private Movie movie;
    private java.time.LocalDateTime schedule;
    
    public Seat(String seatNumber, SeatType type, Cinema cinema) {
        this.seatNumber = seatNumber;
        this.type = type;
        this.isOccupied = false;
        this.cinema = cinema;
    }
    
    // Copy constructor to create a new seat for a specific movie and schedule
    public Seat(Seat templateSeat, Movie movie, java.time.LocalDateTime schedule) {
        this.seatNumber = templateSeat.getSeatNumber();
        this.type = templateSeat.getType();
        this.isOccupied = false;
        this.cinema = templateSeat.getCinema();
        this.movie = movie;
        this.schedule = schedule;
    }
    
    // Getters and setters
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public SeatType getType() {
        return type;
    }
    
    public void setType(SeatType type) {
        this.type = type;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
    
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    
    public double getPrice() {
        return type.getPrice();
    }
    
    public Cinema getCinema() {
        return cinema;
    }
    
    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    public java.time.LocalDateTime getSchedule() {
        return schedule;
    }
    
    public void setSchedule(java.time.LocalDateTime schedule) {
        this.schedule = schedule;
    }
    
    @Override
    public String toString() {
        return seatNumber + " (" + type.getLabel() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Seat seat = (Seat) obj;
        
        if (!seatNumber.equals(seat.seatNumber)) return false;
        if (cinema != null ? !cinema.equals(seat.cinema) : seat.cinema != null) return false;
        if (movie != null ? !movie.equals(seat.movie) : seat.movie != null) return false;
        return schedule != null ? schedule.equals(seat.schedule) : seat.schedule == null;
    }
    
    @Override
    public int hashCode() {
        int result = seatNumber.hashCode();
        result = 31 * result + (cinema != null ? cinema.hashCode() : 0);
        result = 31 * result + (movie != null ? movie.hashCode() : 0);
        result = 31 * result + (schedule != null ? schedule.hashCode() : 0);
        return result;
    }
}
