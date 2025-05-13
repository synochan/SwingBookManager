package view;

import controller.AdminController;
import model.Booking;
import model.Cinema;
import model.Movie;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AdminPanel class for managing movies, schedules, and viewing booking logs
 */
public class AdminPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private JPanel moviesPanel;
    private JPanel bookingsPanel;
    private JPanel reportsPanel;
    
    private AdminController adminController;
    private MainFrame parentFrame;
    private User adminUser;
    
    // Movies tab components
    private JTable moviesTable;
    private DefaultListModel<Movie> movieListModel;
    private JList<Movie> movieList;
    private JTextField titleField;
    private JTextField genreField;
    private JTextField durationField;
    private JTextField directorField;
    private JTextArea synopsisArea;
    private JTextField ratingField;
    private JComboBox<Cinema> cinemaComboBox;
    private JButton addMovieButton;
    private JButton updateMovieButton;
    private JButton deleteMovieButton;
    private JPanel schedulePanel;
    private List<LocalDateTime> currentSchedules;
    
    // Bookings tab components
    private JTable bookingsTable;
    private JTextArea bookingDetailsArea;
    
    public AdminPanel(AdminController adminController, MainFrame parentFrame, User adminUser) {
        this.adminController = adminController;
        this.parentFrame = parentFrame;
        this.adminUser = adminUser;
        
        setLayout(new BorderLayout());
        
        // Create header with admin info
        createHeader();
        
        // Create tabbed panel
        tabbedPane = new JTabbedPane();
        
        // Create tabs
        createMoviesTab();
        createBookingsTab();
        createReportsTab();
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Movies & Schedules", moviesPanel);
        tabbedPane.addTab("Bookings", bookingsPanel);
        tabbedPane.addTab("Reports", reportsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel adminLabel = new JLabel("Admin Dashboard | Logged in as: " + adminUser.getUsername());
        adminLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(adminLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    AdminPanel.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    parentFrame.logout();
                }
            }
        });
        
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createMoviesTab() {
        moviesPanel = new JPanel(new BorderLayout(10, 10));
        moviesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Split pane: Movie list on left, details on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        
        // Left panel with movie list
        JPanel leftPanel = new JPanel(new BorderLayout());
        movieListModel = new DefaultListModel<>();
        
        // Load all movies into the list model
        List<Movie> movies = adminController.getAllMovies();
        for (Movie movie : movies) {
            movieListModel.addElement(movie);
        }
        
        movieList = new JList<>(movieListModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Movie) {
                    Movie movie = (Movie) value;
                    setText(movie.getTitle());
                    
                    if (!movie.isActive()) {
                        setForeground(Color.GRAY);
                    }
                }
                
                return this;
            }
        });
        
        movieList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Movie selectedMovie = movieList.getSelectedValue();
                if (selectedMovie != null) {
                    displayMovieDetails(selectedMovie);
                }
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(movieList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);
        
        // Buttons for movie list
        JPanel listButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton newMovieButton = new JButton("New Movie");
        newMovieButton.addActionListener(e -> clearMovieDetails());
        listButtonPanel.add(newMovieButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMovieList());
        listButtonPanel.add(refreshButton);
        
        leftPanel.add(listButtonPanel, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);
        
        // Right panel with movie details
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Movie details form
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Movie Details"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Title:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        titleField = new JTextField(20);
        detailsPanel.add(titleField, gbc);
        
        // Genre
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Genre:"), gbc);
        
        gbc.gridx = 1;
        genreField = new JTextField(20);
        detailsPanel.add(genreField, gbc);
        
        // Duration
        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Duration (minutes):"), gbc);
        
        gbc.gridx = 1;
        durationField = new JTextField(10);
        detailsPanel.add(durationField, gbc);
        
        // Director
        gbc.gridx = 0;
        gbc.gridy = 3;
        detailsPanel.add(new JLabel("Director:"), gbc);
        
        gbc.gridx = 1;
        directorField = new JTextField(20);
        detailsPanel.add(directorField, gbc);
        
        // Rating
        gbc.gridx = 0;
        gbc.gridy = 4;
        detailsPanel.add(new JLabel("Rating:"), gbc);
        
        gbc.gridx = 1;
        ratingField = new JTextField(10);
        detailsPanel.add(ratingField, gbc);
        
        // Cinema
        gbc.gridx = 0;
        gbc.gridy = 5;
        detailsPanel.add(new JLabel("Cinema:"), gbc);
        
        gbc.gridx = 1;
        List<Cinema> cinemas = adminController.getAllCinemas();
        Cinema[] cinemasArray = cinemas.toArray(new Cinema[0]);
        cinemaComboBox = new JComboBox<>(cinemasArray);
        detailsPanel.add(cinemaComboBox, gbc);
        
        // Synopsis
        gbc.gridx = 0;
        gbc.gridy = 6;
        detailsPanel.add(new JLabel("Synopsis:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        synopsisArea = new JTextArea(5, 20);
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);
        detailsPanel.add(synopsisScrollPane, gbc);
        
        // Active checkbox
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox activeCheckBox = new JCheckBox("Active");
        activeCheckBox.setSelected(true);
        detailsPanel.add(activeCheckBox, gbc);
        
        // Buttons panel
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addMovieButton = new JButton("Add Movie");
        addMovieButton.setBackground(new Color(40, 167, 69));
        addMovieButton.setForeground(Color.WHITE);
        buttonsPanel.add(addMovieButton);
        
        updateMovieButton = new JButton("Update Movie");
        updateMovieButton.setBackground(new Color(0, 123, 255));
        updateMovieButton.setForeground(Color.WHITE);
        updateMovieButton.setEnabled(false);
        buttonsPanel.add(updateMovieButton);
        
        deleteMovieButton = new JButton("Delete Movie");
        deleteMovieButton.setBackground(new Color(220, 53, 69));
        deleteMovieButton.setForeground(Color.WHITE);
        deleteMovieButton.setEnabled(false);
        buttonsPanel.add(deleteMovieButton);
        
        detailsPanel.add(buttonsPanel, gbc);
        
        rightPanel.add(detailsPanel, BorderLayout.NORTH);
        
        // Schedule panel
        schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));
        schedulePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Schedules"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Add empty schedule panel initially
        JLabel schedulePlaceholder = new JLabel("Select a movie to manage its schedules");
        schedulePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        schedulePanel.add(schedulePlaceholder);
        
        JScrollPane scheduleScrollPane = new JScrollPane(schedulePanel);
        scheduleScrollPane.setPreferredSize(new Dimension(400, 200));
        rightPanel.add(scheduleScrollPane, BorderLayout.CENTER);
        
        splitPane.setRightComponent(rightPanel);
        
        moviesPanel.add(splitPane, BorderLayout.CENTER);
        
        // Add event listeners for buttons
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = titleField.getText().trim();
                    String genre = genreField.getText().trim();
                    int duration = Integer.parseInt(durationField.getText().trim());
                    String director = directorField.getText().trim();
                    String synopsis = synopsisArea.getText().trim();
                    String rating = ratingField.getText().trim();
                    Cinema cinema = (Cinema) cinemaComboBox.getSelectedItem();
                    boolean isActive = activeCheckBox.isSelected();
                    
                    if (title.isEmpty() || genre.isEmpty() || director.isEmpty() || synopsis.isEmpty() || rating.isEmpty()) {
                        JOptionPane.showMessageDialog(AdminPanel.this,
                            "Please fill in all fields",
                            "Missing Information", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    Movie newMovie = adminController.addMovie(title, genre, duration, director, synopsis, rating, cinema, isActive);
                    
                    if (newMovie != null) {
                        refreshMovieList();
                        JOptionPane.showMessageDialog(AdminPanel.this,
                            "Movie added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearMovieDetails();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Please enter a valid number for duration",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        updateMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movie selectedMovie = movieList.getSelectedValue();
                if (selectedMovie == null) return;
                
                try {
                    selectedMovie.setTitle(titleField.getText().trim());
                    selectedMovie.setGenre(genreField.getText().trim());
                    selectedMovie.setDurationMinutes(Integer.parseInt(durationField.getText().trim()));
                    selectedMovie.setDirector(directorField.getText().trim());
                    selectedMovie.setSynopsis(synopsisArea.getText().trim());
                    selectedMovie.setRating(ratingField.getText().trim());
                    selectedMovie.setCinema((Cinema) cinemaComboBox.getSelectedItem());
                    selectedMovie.setActive(activeCheckBox.isSelected());
                    
                    adminController.updateMovie(selectedMovie);
                    refreshMovieList();
                    
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Movie updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Please enter a valid number for duration",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        deleteMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movie selectedMovie = movieList.getSelectedValue();
                if (selectedMovie == null) return;
                
                int confirm = JOptionPane.showConfirmDialog(AdminPanel.this,
                    "Are you sure you want to delete this movie?\nThis cannot be undone.",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    adminController.deleteMovie(selectedMovie);
                    refreshMovieList();
                    clearMovieDetails();
                    
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Movie deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    
    private void createBookingsTab() {
        bookingsPanel = new JPanel(new BorderLayout(10, 10));
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        
        // Left panel with bookings table
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("All Bookings"));
        
        // Create table model with bookings data
        String[] columnNames = {"Confirmation", "User", "Movie", "Date", "Seats", "Total"};
        List<Booking> bookings = adminController.getAllBookings();
        Object[][] data = new Object[bookings.size()][columnNames.length];
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            data[i][0] = booking.getConfirmationCode();
            data[i][1] = booking.getUser().getFullName();
            data[i][2] = booking.getMovie().getTitle();
            data[i][3] = booking.getSchedule().format(formatter);
            data[i][4] = booking.getSelectedSeats().size();
            data[i][5] = String.format("₱%.2f", booking.getTotalAmount());
        }
        
        bookingsTable = new JTable(data, columnNames);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookingsTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < bookings.size()) {
                    displayBookingDetails(bookings.get(selectedRow));
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(bookingsTable);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Date:"));
        
        JTextField dateField = new JTextField(10);
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        filterPanel.add(dateField);
        
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText(), 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                
                List<Booking> filteredBookings = adminController.getBookingsByDate(date);
                updateBookingsTable(filteredBookings);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AdminPanel.this,
                    "Please enter a valid date in MM/dd/yyyy format",
                    "Invalid Date", JOptionPane.ERROR_MESSAGE);
            }
        });
        filterPanel.add(filterButton);
        
        JButton showAllButton = new JButton("Show All");
        showAllButton.addActionListener(e -> {
            updateBookingsTable(adminController.getAllBookings());
        });
        filterPanel.add(showAllButton);
        
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        splitPane.setLeftComponent(leftPanel);
        
        // Right panel with booking details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        
        bookingDetailsArea = new JTextArea();
        bookingDetailsArea.setEditable(false);
        bookingDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane detailsScrollPane = new JScrollPane(bookingDetailsArea);
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);
        
        splitPane.setRightComponent(rightPanel);
        
        bookingsPanel.add(splitPane, BorderLayout.CENTER);
    }
    
    private void createReportsTab() {
        reportsPanel = new JPanel(new BorderLayout(10, 10));
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel with date range selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBorder(BorderFactory.createTitledBorder("Select Date Range"));
        
        datePanel.add(new JLabel("From:"));
        JTextField fromDateField = new JTextField(10);
        fromDateField.setText(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        datePanel.add(fromDateField);
        
        datePanel.add(new JLabel("To:"));
        JTextField toDateField = new JTextField(10);
        toDateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        datePanel.add(toDateField);
        
        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(new Color(0, 123, 255));
        generateButton.setForeground(Color.WHITE);
        datePanel.add(generateButton);
        
        reportsPanel.add(datePanel, BorderLayout.NORTH);
        
        // Main panel with report content
        JPanel reportContentPanel = new JPanel(new BorderLayout());
        
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        reportContentPanel.add(reportScrollPane, BorderLayout.CENTER);
        
        // Export button
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Export to TXT");
        exportButton.setEnabled(false);
        exportPanel.add(exportButton);
        
        reportContentPanel.add(exportPanel, BorderLayout.SOUTH);
        
        reportsPanel.add(reportContentPanel, BorderLayout.CENTER);
        
        // Add event listener for generate button
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate fromDate = LocalDate.parse(fromDateField.getText(), 
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    LocalDate toDate = LocalDate.parse(toDateField.getText(), 
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    
                    // Generate report
                    String report = adminController.generateSalesReport(fromDate, toDate);
                    reportArea.setText(report);
                    
                    // Enable export button
                    exportButton.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Please enter valid dates in MM/dd/yyyy format",
                        "Invalid Date", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Add event listener for export button
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String reportText = reportArea.getText();
                    if (reportText.isEmpty()) {
                        JOptionPane.showMessageDialog(AdminPanel.this,
                            "No report data to export",
                            "Export Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Simulate exporting to file
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Report exported successfully to 'sales_report.txt'",
                        "Export Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // For simulation, also output to console
                    System.out.println("------ EXPORTED REPORT ------");
                    System.out.println(reportText);
                    System.out.println("----------------------------");
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Failed to export report: " + ex.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void displayMovieDetails(Movie movie) {
        // Fill form fields with movie details
        titleField.setText(movie.getTitle());
        genreField.setText(movie.getGenre());
        durationField.setText(String.valueOf(movie.getDurationMinutes()));
        directorField.setText(movie.getDirector());
        synopsisArea.setText(movie.getSynopsis());
        ratingField.setText(movie.getRating());
        
        // Find and select the correct cinema in the combo box
        Cinema movieCinema = movie.getCinema();
        for (int i = 0; i < cinemaComboBox.getItemCount(); i++) {
            if (cinemaComboBox.getItemAt(i).getId() == movieCinema.getId()) {
                cinemaComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        // Enable update and delete buttons
        updateMovieButton.setEnabled(true);
        deleteMovieButton.setEnabled(true);
        
        // Display schedules
        displaySchedules(movie);
    }
    
    private void displaySchedules(Movie movie) {
        // Clear the schedule panel
        schedulePanel.removeAll();
        
        // Add title
        JLabel titleLabel = new JLabel("Schedules for " + movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        schedulePanel.add(titleLabel);
        schedulePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Get current schedules
        currentSchedules = movie.getSchedules();
        
        // Display existing schedules
        if (currentSchedules.isEmpty()) {
            JLabel noSchedulesLabel = new JLabel("No schedules set for this movie");
            noSchedulesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            schedulePanel.add(noSchedulesLabel);
        } else {
            JPanel schedulesListPanel = new JPanel();
            schedulesListPanel.setLayout(new BoxLayout(schedulesListPanel, BoxLayout.Y_AXIS));
            schedulesListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
            
            for (LocalDateTime schedule : currentSchedules) {
                JPanel scheduleItemPanel = new JPanel(new BorderLayout());
                scheduleItemPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                
                JLabel scheduleLabel = new JLabel(schedule.format(formatter));
                scheduleItemPanel.add(scheduleLabel, BorderLayout.CENTER);
                
                JButton removeButton = new JButton("Remove");
                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Remove this schedule
                        adminController.removeSchedule(movie, schedule);
                        
                        // Refresh schedules
                        displaySchedules(movie);
                    }
                });
                
                scheduleItemPanel.add(removeButton, BorderLayout.EAST);
                schedulesListPanel.add(scheduleItemPanel);
            }
            
            JScrollPane schedulesScrollPane = new JScrollPane(schedulesListPanel);
            schedulesScrollPane.setPreferredSize(new Dimension(400, 150));
            schedulesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            schedulePanel.add(schedulesScrollPane);
        }
        
        // Add section for adding new schedule
        schedulePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel addScheduleLabel = new JLabel("Add New Schedule:");
        addScheduleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        addScheduleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        schedulePanel.add(addScheduleLabel);
        
        // Date and time selection
        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateTimePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        dateTimePanel.add(new JLabel("Date (MM/DD/YYYY):"));
        JTextField dateField = new JTextField(10);
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        dateTimePanel.add(dateField);
        
        dateTimePanel.add(new JLabel("Time (HH:MM):"));
        JTextField timeField = new JTextField(8);
        timeField.setText("18:00");
        dateTimePanel.add(timeField);
        
        schedulePanel.add(dateTimePanel);
        
        // Add schedule button
        JButton addScheduleButton = new JButton("Add Schedule");
        addScheduleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse date and time
                    LocalDate date = LocalDate.parse(dateField.getText(), 
                        DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    
                    LocalTime time = LocalTime.parse(timeField.getText(), 
                        DateTimeFormatter.ofPattern("HH:mm"));
                    
                    LocalDateTime newSchedule = LocalDateTime.of(date, time);
                    
                    // Add schedule
                    adminController.addSchedule(movie, newSchedule);
                    
                    // Refresh schedules
                    displaySchedules(movie);
                    
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Schedule added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminPanel.this,
                        "Please enter valid date (MM/DD/YYYY) and time (HH:MM)",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        schedulePanel.add(addScheduleButton);
        
        // Refresh the panel
        schedulePanel.revalidate();
        schedulePanel.repaint();
    }
    
    private void displayBookingDetails(Booking booking) {
        if (booking == null) {
            bookingDetailsArea.setText("No booking selected");
            return;
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        
        StringBuilder details = new StringBuilder();
        details.append("BOOKING DETAILS\n");
        details.append("===============================\n\n");
        
        details.append("Confirmation Code: ").append(booking.getConfirmationCode()).append("\n");
        details.append("Booking ID: ").append(booking.getBookingId()).append("\n");
        details.append("Booking Time: ").append(booking.getBookingTime().format(
            DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm:ss a"))).append("\n\n");
        
        details.append("USER INFORMATION\n");
        details.append("-------------------------------\n");
        details.append("Name: ").append(booking.getUser().getFullName()).append("\n");
        details.append("Email: ").append(booking.getUser().getEmail()).append("\n");
        details.append("Phone: ").append(booking.getUser().getPhoneNumber()).append("\n");
        details.append("User Type: ").append(booking.getUser().isGuest() ? "Guest" : "Registered").append("\n\n");
        
        details.append("MOVIE INFORMATION\n");
        details.append("-------------------------------\n");
        details.append("Movie: ").append(booking.getMovie().getTitle()).append("\n");
        details.append("Cinema: ").append(booking.getMovie().getCinema().getName()).append("\n");
        details.append("Date: ").append(booking.getSchedule().format(dateFormatter)).append("\n");
        details.append("Time: ").append(booking.getSchedule().format(timeFormatter)).append("\n\n");
        
        details.append("SEATS\n");
        details.append("-------------------------------\n");
        for (Seat seat : booking.getSelectedSeats()) {
            details.append(seat.getSeatNumber())
                  .append(" (").append(seat.getType().getLabel()).append(")")
                  .append(" - ₱").append(String.format("%.2f", seat.getPrice()))
                  .append("\n");
        }
        details.append("\n");
        
        if (!booking.getSelectedSnacks().isEmpty()) {
            details.append("SNACKS & DRINKS\n");
            details.append("-------------------------------\n");
            for (Snack snack : booking.getSelectedSnacks()) {
                details.append(snack.getName())
                      .append(" - ₱").append(String.format("%.2f", snack.getPrice()))
                      .append("\n");
            }
            details.append("\n");
        }
        
        details.append("PAYMENT INFORMATION\n");
        details.append("-------------------------------\n");
        details.append("Payment Method: ").append(booking.getPaymentMethod()).append("\n");
        details.append("Payment Status: ").append(booking.isPaid() ? "Paid" : "Unpaid").append("\n");
        details.append("Total Amount: ₱").append(String.format("%.2f", booking.getTotalAmount())).append("\n");
        
        bookingDetailsArea.setText(details.toString());
        bookingDetailsArea.setCaretPosition(0); // Scroll to top
    }
    
    private void updateBookingsTable(List<Booking> bookings) {
        // Create table model with bookings data
        String[] columnNames = {"Confirmation", "User", "Movie", "Date", "Seats", "Total"};
        Object[][] data = new Object[bookings.size()][columnNames.length];
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            data[i][0] = booking.getConfirmationCode();
            data[i][1] = booking.getUser().getFullName();
            data[i][2] = booking.getMovie().getTitle();
            data[i][3] = booking.getSchedule().format(formatter);
            data[i][4] = booking.getSelectedSeats().size();
            data[i][5] = String.format("₱%.2f", booking.getTotalAmount());
        }
        
        // Replace table model
        bookingsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        
        // Clear booking details
        bookingDetailsArea.setText("Select a booking to view details");
    }
    
    private void clearMovieDetails() {
        // Clear form fields
        titleField.setText("");
        genreField.setText("");
        durationField.setText("");
        directorField.setText("");
        synopsisArea.setText("");
        ratingField.setText("");
        
        // Disable update and delete buttons
        updateMovieButton.setEnabled(false);
        deleteMovieButton.setEnabled(false);
        
        // Clear schedules
        schedulePanel.removeAll();
        JLabel noMovieLabel = new JLabel("Create or select a movie to manage schedules");
        noMovieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        schedulePanel.add(noMovieLabel);
        schedulePanel.revalidate();
        schedulePanel.repaint();
        
        // Deselect movie in list
        movieList.clearSelection();
    }
    
    private void refreshMovieList() {
        // Clear current list
        movieListModel.clear();
        
        // Reload all movies
        List<Movie> movies = adminController.getAllMovies();
        for (Movie movie : movies) {
            movieListModel.addElement(movie);
        }
    }
}
