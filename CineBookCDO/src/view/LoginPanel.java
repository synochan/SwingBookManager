package view;

import controller.UserController;
import model.User;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for user login and registration
 */
public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton guestButton;
    private JPanel loginFormPanel;
    private JPanel registerFormPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private UserController userController;
    private MainFrame parentFrame;
    
    public LoginPanel(UserController userController, MainFrame parentFrame) {
        this.userController = userController;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create card layout for switching between login and register
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Initialize UI components
        initComponents();
        
        // Add the card panel to the main panel
        add(cardPanel, BorderLayout.CENTER);
    }
    
    private void initComponents() {
        // Create login form
        loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to CineBook CDO");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginFormPanel.add(welcomeLabel, gbc);
        
        // Login title
        JLabel loginLabel = new JLabel("Login to your account");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        loginFormPanel.add(loginLabel, gbc);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginFormPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        loginFormPanel.add(usernameField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginFormPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginFormPanel.add(passwordField, gbc);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 122, 204));
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginFormPanel.add(loginButton, gbc);
        
        // Register link
        JButton goToRegisterButton = new JButton("Don't have an account? Register");
        goToRegisterButton.setBorderPainted(false);
        goToRegisterButton.setContentAreaFilled(false);
        goToRegisterButton.setForeground(new Color(0, 122, 204));
        gbc.gridy = 5;
        loginFormPanel.add(goToRegisterButton, gbc);
        
        // Guest button
        guestButton = new JButton("Continue as Guest");
        guestButton.setBackground(new Color(150, 150, 150));
        guestButton.setForeground(Color.WHITE);
        gbc.gridy = 6;
        loginFormPanel.add(guestButton, gbc);
        
        // Admin login link
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setBorderPainted(false);
        adminLoginButton.setContentAreaFilled(false);
        adminLoginButton.setForeground(new Color(100, 100, 100));
        gbc.gridy = 7;
        loginFormPanel.add(adminLoginButton, gbc);
        
        // Create registration form
        registerFormPanel = new JPanel(new GridBagLayout());
        registerFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Register title
        JLabel registerTitleLabel = new JLabel("Create a New Account");
        registerTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        registerTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerFormPanel.add(registerTitleLabel, gbc);
        
        // Name field
        JLabel nameLabel = new JLabel("Full Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registerFormPanel.add(nameLabel, gbc);
        
        nameField = new JTextField(20);
        gbc.gridx = 1;
        registerFormPanel.add(nameField, gbc);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerFormPanel.add(emailLabel, gbc);
        
        emailField = new JTextField(20);
        gbc.gridx = 1;
        registerFormPanel.add(emailField, gbc);
        
        // Phone field
        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        registerFormPanel.add(phoneLabel, gbc);
        
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        registerFormPanel.add(phoneField, gbc);
        
        // Username field for registration
        JLabel regUsernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerFormPanel.add(regUsernameLabel, gbc);
        
        JTextField regUsernameField = new JTextField(20);
        gbc.gridx = 1;
        registerFormPanel.add(regUsernameField, gbc);
        
        // Password field for registration
        JLabel regPasswordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        registerFormPanel.add(regPasswordLabel, gbc);
        
        JPasswordField regPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        registerFormPanel.add(regPasswordField, gbc);
        
        // Register button
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 122, 204));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        registerFormPanel.add(registerButton, gbc);
        
        // Back to login link
        JButton backToLoginButton = new JButton("Already have an account? Login");
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setForeground(new Color(0, 122, 204));
        gbc.gridy = 7;
        registerFormPanel.add(backToLoginButton, gbc);
        
        // Add panels to card layout
        cardPanel.add(loginFormPanel, "login");
        cardPanel.add(registerFormPanel, "register");
        
        // Show login form by default
        cardLayout.show(cardPanel, "login");
        
        // Add event listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Please enter both username and password", 
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User user = userController.login(username, password);
                if (user != null) {
                    if (user.isAdmin()) {
                        // Show admin panel
                        parentFrame.showAdminPanel(user);
                    } else {
                        // Show movie listing
                        parentFrame.setLoggedInUser(user);
                        parentFrame.showMovieListingPanel();
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Invalid username or password", 
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(LoginPanel.this, 
                    "Enter admin username:", "Admin Login", JOptionPane.PLAIN_MESSAGE);
                
                if (username != null && !username.isEmpty()) {
                    String password = JOptionPane.showInputDialog(LoginPanel.this, 
                        "Enter admin password:", "Admin Login", JOptionPane.PLAIN_MESSAGE);
                    
                    if (password != null && !password.isEmpty()) {
                        User admin = userController.login(username, password);
                        if (admin != null && admin.isAdmin()) {
                            parentFrame.showAdminPanel(admin);
                        } else {
                            JOptionPane.showMessageDialog(LoginPanel.this, 
                                "Invalid admin credentials", 
                                "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        
        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show guest checkout dialog
                JPanel guestPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                JTextField guestNameField = new JTextField(20);
                JTextField guestEmailField = new JTextField(20);
                JTextField guestPhoneField = new JTextField(20);
                
                guestPanel.add(new JLabel("Full Name:"));
                guestPanel.add(guestNameField);
                guestPanel.add(new JLabel("Email:"));
                guestPanel.add(guestEmailField);
                guestPanel.add(new JLabel("Phone:"));
                guestPanel.add(guestPhoneField);
                
                int result = JOptionPane.showConfirmDialog(LoginPanel.this, 
                    guestPanel, "Guest Information", JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    String name = guestNameField.getText();
                    String email = guestEmailField.getText();
                    String phone = guestPhoneField.getText();
                    
                    if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        JOptionPane.showMessageDialog(LoginPanel.this, 
                            "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Create guest user
                    User guestUser = userController.createGuestUser(name, email, phone);
                    
                    // Continue to movie listing
                    parentFrame.setLoggedInUser(guestUser);
                    parentFrame.showMovieListingPanel();
                }
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String username = regUsernameField.getText();
                String password = new String(regPasswordField.getPassword());
                
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
                    username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Please fill in all fields", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User newUser = userController.registerUser(username, password, name, email, phone);
                if (newUser != null) {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Registration successful! Please login.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear fields and switch to login panel
                    nameField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    regUsernameField.setText("");
                    regPasswordField.setText("");
                    cardLayout.show(cardPanel, "login");
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                        "Username already exists. Please choose another username.", 
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        goToRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "register");
            }
        });
        
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "login");
            }
        });
    }
}
