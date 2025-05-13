package view;

import controller.MovieController;
import model.Cinema;
import model.Movie;
import model.User;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for displaying movie listings and allowing users to select a movie
 */
public class MovieListingPanel extends JPanel {
    private JComboBox<Cinema> cinemaComboBox;
    private JComboBox<String> genreComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel moviesPanel;
    private JScrollPane scrollPane;
    
    private MovieController movieController;
    private MainFrame parentFrame;
    private User currentUser;
    
    public MovieListingPanel(MovieController movieController, MainFrame parentFrame, User currentUser) {
        this.movieController = movieController;
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Initialize UI components
        initComponents();
        
        // Load all movies initially
        displayMovies(movieController.getAllActiveMovies());
    }
    
    private void initComponents() {
        // Header panel with title and user info
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("CineBook CDO - Movie Listings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFullName());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        
        userPanel.add(userLabel);
        userPanel.add(logoutButton);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Movies"));
        
        // Cinema filter
        JLabel cinemaLabel = new JLabel("Cinema:");
        filterPanel.add(cinemaLabel);
        
        List<Cinema> cinemas = movieController.getAllCinemas();
        Cinema[] cinemasArray = cinemas.toArray(new Cinema[0]);
        cinemaComboBox = new JComboBox<>(cinemasArray);
        cinemaComboBox.setPreferredSize(new Dimension(150, 25));
        cinemaComboBox.insertItemAt(new Cinema(0, "All Cinemas", "", 0, false), 0);
        cinemaComboBox.setSelectedIndex(0);
        filterPanel.add(cinemaComboBox);
        
        // Genre filter
        JLabel genreLabel = new JLabel("Genre:");
        filterPanel.add(genreLabel);
        
        String[] genres = {"All Genres", "Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation"};
        genreComboBox = new JComboBox<>(genres);
        genreComboBox.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(genreComboBox);
        
        // Search filter
        JLabel searchLabel = new JLabel("Search:");
        filterPanel.add(searchLabel);
        
        searchField = new JTextField(15);
        filterPanel.add(searchField);
        
        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        filterPanel.add(searchButton);
        
        add(filterPanel, BorderLayout.NORTH);
        
        // Movies panel (grid layout)
        moviesPanel = new JPanel();
        moviesPanel.setLayout(new BoxLayout(moviesPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Add event listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        
        cinemaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        
        genreComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    MovieListingPanel.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    parentFrame.logout();
                }
            }
        });
    }
    
    private void applyFilters() {
        Cinema selectedCinema = (Cinema) cinemaComboBox.getSelectedItem();
        String selectedGenre = (String) genreComboBox.getSelectedItem();
        String searchTerm = searchField.getText().trim();
        
        List<Movie> filteredMovies = movieController.filterMovies(selectedCinema, selectedGenre, searchTerm);
        displayMovies(filteredMovies);
    }
    
    private void displayMovies(List<Movie> movies) {
        moviesPanel.removeAll();
        
        if (movies.isEmpty()) {
            JLabel noMoviesLabel = new JLabel("No movies found matching your criteria");
            noMoviesLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noMoviesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            moviesPanel.add(noMoviesLabel);
        } else {
            for (Movie movie : movies) {
                JPanel movieCard = createMovieCard(movie);
                movieCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
                moviesPanel.add(movieCard);
                moviesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        revalidate();
        repaint();
    }
    
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        // Left panel for movie poster
        JPanel posterPanel = new JPanel();
        posterPanel.setPreferredSize(new Dimension(120, 180));
        posterPanel.setBackground(new Color(240, 240, 240));
        posterPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Use movie title as placeholder for poster
        JLabel posterLabel = new JLabel(movie.getTitle());
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterPanel.add(posterLabel);
        
        card.add(posterPanel, BorderLayout.WEST);
        
        // Center panel for movie details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detailsPanel.add(titleLabel);
        
        JLabel genreLabel = new JLabel("Genre: " + movie.getGenre());
        detailsPanel.add(genreLabel);
        
        JLabel durationLabel = new JLabel("Duration: " + movie.getDurationMinutes() + " minutes");
        detailsPanel.add(durationLabel);
        
        JLabel directorLabel = new JLabel("Director: " + movie.getDirector());
        detailsPanel.add(directorLabel);
        
        JLabel cinemaLabel = new JLabel("Cinema: " + movie.getCinema().getName());
        detailsPanel.add(cinemaLabel);
        
        JLabel ratingLabel = new JLabel("Rating: " + movie.getRating());
        detailsPanel.add(ratingLabel);
        
        // Show available schedules
        JPanel schedulesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        schedulesPanel.setBackground(Color.WHITE);
        JLabel schedulesLabel = new JLabel("Schedules: ");
        schedulesPanel.add(schedulesLabel);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        
        List<LocalDateTime> schedules = movie.getSchedules();
        if (schedules.isEmpty()) {
            schedulesPanel.add(new JLabel("No schedules available"));
        } else {
            JComboBox<String> scheduleComboBox = new JComboBox<>();
            
            for (LocalDateTime schedule : schedules) {
                scheduleComboBox.addItem(schedule.format(formatter));
            }
            
            schedulesPanel.add(scheduleComboBox);
        }
        
        detailsPanel.add(schedulesPanel);
        
        // Synopsis with scroll pane
        JLabel synopsisLabel = new JLabel("Synopsis:");
        detailsPanel.add(synopsisLabel);
        
        JTextArea synopsisArea = new JTextArea(movie.getSynopsis());
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setEditable(false);
        synopsisArea.setBackground(Color.WHITE);
        synopsisArea.setRows(3);
        
        JScrollPane synopsisScroll = new JScrollPane(synopsisArea);
        synopsisScroll.setPreferredSize(new Dimension(400, 60));
        detailsPanel.add(synopsisScroll);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        
        // Right panel for booking button
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton bookButton = new JButton("Book Now");
        bookButton.setBackground(new Color(40, 167, 69));
        bookButton.setForeground(Color.WHITE);
        bookButton.setPreferredSize(new Dimension(100, 30));
        
        actionPanel.add(bookButton, BorderLayout.NORTH);
        
        card.add(actionPanel, BorderLayout.EAST);
        
        // Add event listener for booking
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (schedules.isEmpty()) {
                    JOptionPane.showMessageDialog(MovieListingPanel.this,
                        "No schedules available for this movie",
                        "Booking Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Get selected schedule
                JComboBox scheduleComboBox = (JComboBox) schedulesPanel.getComponent(1);
                int selectedIndex = scheduleComboBox.getSelectedIndex();
                
                if (selectedIndex >= 0) {
                    LocalDateTime selectedSchedule = schedules.get(selectedIndex);
                    parentFrame.startBookingProcess(movie, selectedSchedule);
                }
            }
        });
        
        return card;
    }
}
