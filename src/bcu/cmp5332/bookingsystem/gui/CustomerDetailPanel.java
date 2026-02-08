package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CustomerDetailPanel extends JPanel {
    
    private MainWindow mw;
    private Customer customer;
    private FlightBookingSystem fbs;
    
    public CustomerDetailPanel(MainWindow mw, Customer customer) {
        this.mw = mw;
        this.customer = customer;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JButton backBtn = new JButton("← Back to Customers");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setForeground(new Color(52, 152, 219));
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            new EmptyBorder(8, 15, 8, 15)
        ));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> mw.refreshCustomersPanel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton editBtn = new JButton("Edit Customer");
        editBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBackground(new Color(241, 196, 15));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setPreferredSize(new Dimension(130, 36));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> new EditCustomerWindow(mw));
        
        JButton deleteBtn = new JButton("Delete Customer");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setPreferredSize(new Dimension(140, 36));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> new DeleteCustomerWindow(mw));
        
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        
        JLabel titleLabel = new JLabel("Customer Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 245));
        
        // Customer details card
        contentPanel.add(createDetailsCard());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Booking history
        contentPanel.add(createBookingsSection());
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Icon and ID
        JLabel icon = new JLabel("👤");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        card.add(icon, gbc);
        
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        JLabel idLabel = new JLabel("👤 Customer #" + customer.getId());
        idLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        idLabel.setForeground(new Color(44, 62, 80));
        card.add(idLabel, gbc);
        
        gbc.gridy++;
        JLabel nameLabel = new JLabel(customer.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        nameLabel.setForeground(new Color(52, 73, 94));
        card.add(nameLabel, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        card.add(Box.createRigidArea(new Dimension(0, 15)), gbc);
        
        // Contact information
        gbc.gridy++;
        addDetailRow(card, gbc, "📱 Phone:", customer.getPhone());
        
        String email = customer.getEmail() != null && !customer.getEmail().isEmpty() 
            ? customer.getEmail() : "Not provided";
        addDetailRow(card, gbc, "✉️ Email:", email);
        
        String gender = customer.getGender() != null && !customer.getGender().isEmpty() 
            ? customer.getGender() : "Not specified";
        addDetailRow(card, gbc, "👤 Gender:", gender);
        
        String age = customer.getAge() > 0 ? customer.getAge() + " years" : "Not specified";
        addDetailRow(card, gbc, "🎂 Age:", age);
        
        addDetailRow(card, gbc, "📚 Total Bookings:", String.valueOf(customer.getBookings().size()));
        
        return card;
    }
    
    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        labelComp.setForeground(new Color(127, 140, 141));
        panel.add(labelComp, gbc);
        
        gbc.gridx = 1;
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 14));
        valueComp.setForeground(new Color(44, 62, 80));
        panel.add(valueComp, gbc);
    }
    
    private JPanel createBookingsSection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setBackground(new Color(240, 240, 245));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        JLabel sectionTitle = new JLabel("Booking History");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(44, 62, 80));
        
        JPanel bookingsContainer = new JPanel();
        bookingsContainer.setLayout(new BoxLayout(bookingsContainer, BoxLayout.Y_AXIS));
        bookingsContainer.setBackground(new Color(240, 240, 245));
        
        java.util.List<Booking> bookings = customer.getBookings();
        
        if (bookings.isEmpty()) {
            JLabel noBookings = new JLabel("No bookings made yet");
            noBookings.setFont(new Font("Arial", Font.ITALIC, 14));
            noBookings.setForeground(new Color(127, 140, 141));
            noBookings.setAlignmentX(Component.LEFT_ALIGNMENT);
            bookingsContainer.add(noBookings);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            
            for (Booking booking : bookings) {
                bookingsContainer.add(createBookingCard(booking, formatter));
                bookingsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        section.add(sectionTitle, BorderLayout.NORTH);
        section.add(bookingsContainer, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createBookingCard(Booking booking, DateTimeFormatter formatter) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Flight icon
        JLabel flightIcon = new JLabel("✈️");
        flightIcon.setFont(new Font("Arial", Font.PLAIN, 32));
        
        // Flight details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        Flight flight = booking.getFlight();
        
        JLabel flightLabel = new JLabel("Flight " + flight.getFlightNumber() + " - " + flight.getOrigin() + " → " + flight.getDestination());
        flightLabel.setFont(new Font("Arial", Font.BOLD, 16));
        flightLabel.setForeground(new Color(44, 62, 80));
        
        JLabel dateLabel = new JLabel("🗓️ Departure: " + flight.getDepartureDate().format(formatter));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(52, 73, 94));
        
        JLabel bookedLabel = new JLabel("📅 Booked on: " + booking.getBookingDate().format(formatter));
        bookedLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        bookedLabel.setForeground(new Color(127, 140, 141));
        
        JLabel classLabel = new JLabel("💺 Class: " + booking.getBookingClass() + " | Seat: " + booking.getSeatNumber() + " | Price: NPR " + String.format("%.2f", booking.getBookedPrice()));
        classLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        classLabel.setForeground(new Color(52, 73, 94));
        
        detailsPanel.add(flightLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        detailsPanel.add(bookedLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        detailsPanel.add(classLabel);
        
        // Status badge
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statusPanel.setBackground(Color.WHITE);
        
        String statusText = booking.isCancelled() ? "Cancelled" : "Active";
        Color statusColor = booking.isCancelled() ? new Color(231, 76, 60) : new Color(46, 204, 113);
        
        JLabel statusLabel = new JLabel("  " + statusText + "  ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(statusColor);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        statusPanel.add(statusLabel);
        
        card.add(flightIcon, BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);
        
        return card;
    }
}
