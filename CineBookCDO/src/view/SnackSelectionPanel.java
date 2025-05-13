package view;

import controller.BookingController;
import model.Booking;
import model.Snack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for selecting snacks and concessions
 */
public class SnackSelectionPanel extends JPanel {
    private JPanel snackListPanel;
    private JScrollPane scrollPane;
    private JPanel cartPanel;
    private JLabel totalLabel;
    
    private BookingController bookingController;
    private Booking currentBooking;
    
    public SnackSelectionPanel(BookingController bookingController, Booking currentBooking) {
        this.bookingController = bookingController;
        this.currentBooking = currentBooking;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize UI components
        initComponents();
        
        // Load available snacks
        loadSnacks();
        
        // Update cart display
        updateCart();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("Add Snacks & Drinks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7); // 70% for snack list, 30% for cart
        
        // Snack list panel (left side)
        snackListPanel = new JPanel();
        snackListPanel.setLayout(new BoxLayout(snackListPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(snackListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        splitPane.setLeftComponent(scrollPane);
        
        // Cart panel (right side)
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setBorder(BorderFactory.createTitledBorder("Your Cart"));
        
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        
        JScrollPane cartScroll = new JScrollPane(cartPanel);
        cartScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        cartContainer.add(cartScroll, BorderLayout.CENTER);
        
        // Total panel at bottom of cart
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        totalPanel.setPreferredSize(new Dimension(200, 50));
        
        totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        totalPanel.add(totalLabel, BorderLayout.CENTER);
        
        cartContainer.add(totalPanel, BorderLayout.SOUTH);
        
        splitPane.setRightComponent(cartContainer);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Information panel at bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel infoLabel = new JLabel("Snacks are optional. You can proceed without adding any items.");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPanel.add(infoLabel);
        
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private void loadSnacks() {
        // Get all available snacks
        List<Snack> availableSnacks = bookingController.getAvailableSnacks();
        
        // Group snacks by category
        String[] categories = {"FOOD", "DRINK", "COMBO"};
        
        for (String category : categories) {
            // Category header
            JPanel categoryPanel = new JPanel(new BorderLayout());
            categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
            
            JLabel categoryLabel = new JLabel(category.substring(0, 1) + category.substring(1).toLowerCase() + "s");
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
            categoryPanel.add(categoryLabel, BorderLayout.NORTH);
            
            snackListPanel.add(categoryPanel);
            
            // Add snacks in this category
            boolean hasSnacksInCategory = false;
            
            for (Snack snack : availableSnacks) {
                if (snack.getCategory().equals(category) && snack.isAvailable()) {
                    hasSnacksInCategory = true;
                    
                    JPanel snackCard = createSnackCard(snack);
                    snackListPanel.add(snackCard);
                    snackListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
            
            if (!hasSnacksInCategory) {
                JLabel noSnacksLabel = new JLabel("No " + category.toLowerCase() + "s available");
                noSnacksLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
                snackListPanel.add(noSnacksLabel);
            }
            
            // Add some space between categories
            snackListPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
    }
    
    private JPanel createSnackCard(Snack snack) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBackground(Color.WHITE);
        
        // Left panel for snack icon/image
        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(80, 80));
        iconPanel.setBackground(new Color(240, 240, 240));
        
        // Use first letter of snack name as placeholder
        JLabel iconLabel = new JLabel(snack.getName().substring(0, 1));
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        
        // Center panel for snack details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(snack.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        detailsPanel.add(nameLabel);
        
        JLabel descLabel = new JLabel(snack.getDescription());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsPanel.add(descLabel);
        
        JLabel priceLabel = new JLabel("₱" + String.format("%.2f", snack.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        detailsPanel.add(priceLabel);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        
        // Right panel for add button
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(60, 30));
        
        actionPanel.add(addButton, BorderLayout.CENTER);
        
        card.add(actionPanel, BorderLayout.EAST);
        
        // Add event listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add snack to booking
                currentBooking.addSnack(snack);
                
                // Update cart display
                updateCart();
            }
        });
        
        return card;
    }
    
    private void updateCart() {
        // Clear current cart display
        cartPanel.removeAll();
        
        List<Snack> selectedSnacks = currentBooking.getSelectedSnacks();
        
        if (selectedSnacks.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            cartPanel.add(emptyLabel);
        } else {
            for (Snack snack : selectedSnacks) {
                JPanel cartItem = createCartItemPanel(snack);
                cartPanel.add(cartItem);
                cartPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        
        // Update total
        double snacksTotal = 0;
        for (Snack snack : selectedSnacks) {
            snacksTotal += snack.getPrice();
        }
        
        totalLabel.setText("Total: ₱" + String.format("%.2f", snacksTotal));
        
        // Refresh display
        cartPanel.revalidate();
        cartPanel.repaint();
    }
    
    private JPanel createCartItemPanel(Snack snack) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel nameLabel = new JLabel(snack.getName());
        panel.add(nameLabel, BorderLayout.WEST);
        
        JLabel priceLabel = new JLabel("₱" + String.format("%.2f", snack.getPrice()));
        panel.add(priceLabel, BorderLayout.CENTER);
        
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(25, 25));
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        
        panel.add(removeButton, BorderLayout.EAST);
        
        // Add event listener
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove snack from booking
                currentBooking.removeSnack(snack);
                
                // Update cart display
                updateCart();
            }
        });
        
        return panel;
    }
}
