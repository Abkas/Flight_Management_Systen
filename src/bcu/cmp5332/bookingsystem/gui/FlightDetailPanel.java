package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.BookingClass;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Set;

public class FlightDetailPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    private Flight flight;
    
    public FlightDetailPanel(MainWindow mw, Flight flight) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        this.flight = flight;
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JButton backBtn = new JButton("← Back to Flights");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setForeground(new Color(52, 152, 219));
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            new EmptyBorder(8, 15, 8, 15)
        ));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> mw.refreshFlightsPanel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton editBtn = new JButton("Edit Flight");
        editBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBackground(new Color(241, 196, 15));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setPreferredSize(new Dimension(110, 36));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> new EditFlightWindow(mw));
        
        JButton deleteBtn = new JButton("Delete Flight");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setPreferredSize(new Dimension(120, 36));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> new DeleteFlightWindow(mw));
        
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        
        JLabel titleLabel = new JLabel("Flight Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 245));
        
        // Flight details card
        contentPanel.add(createDetailsCard());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Seat maps for each class
        contentPanel.add(createSeatMapSection());
        
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
        
        // Icon and Flight Number
        JLabel icon = new JLabel("✈️");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        card.add(icon, gbc);
        
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        JLabel flightNoLabel = new JLabel("Flight " + flight.getFlightNumber());
        flightNoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        flightNoLabel.setForeground(new Color(44, 62, 80));
        card.add(flightNoLabel, gbc);
        
        gbc.gridy++;
        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        routeLabel.setForeground(new Color(52, 73, 94));
        card.add(routeLabel, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        card.add(Box.createRigidArea(new Dimension(0, 15)), gbc);
        
        // Flight information
        gbc.gridy++;
        addDetailRow(card, gbc, "📅 Departure Date:", flight.getDepartureDate().toString());
        addDetailRow(card, gbc, "✈️ Aircraft:", flight.getPlane().getModel() + " (" + flight.getPlane().getRegistrationNumber() + ")");
        addDetailRow(card, gbc, "👥 Passengers:", String.valueOf(flight.getPassengers().size()));
        
        int totalCapacity = flight.getCapacity(BookingClass.ECONOMY) + 
                            flight.getCapacity(BookingClass.BUSINESS) + 
                            flight.getCapacity(BookingClass.FIRST);
        addDetailRow(card, gbc, "💺 Total Capacity:", String.valueOf(totalCapacity));
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        card.add(Box.createRigidArea(new Dimension(0, 15)), gbc);
        
        // Pricing information
        gbc.gridy++;
        JLabel pricingLabel = new JLabel("💰 Pricing");
        pricingLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        pricingLabel.setForeground(new Color(44, 62, 80));
        card.add(pricingLabel, gbc);
        
        addDetailRow(card, gbc, "Economy Class:", "NPR " + String.format("%,.2f", flight.getEconomyPrice()));
        addDetailRow(card, gbc, "Business Class:", "NPR " + String.format("%,.2f", flight.getBusinessPrice()));
        addDetailRow(card, gbc, "First Class:", "NPR " + String.format("%,.2f", flight.getFirstClassPrice()));
        
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
    
    private JPanel createSeatMapSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(240, 240, 245));
        
        JLabel sectionTitle = new JLabel("💺 Seat Maps");
        sectionTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        sectionTitle.setForeground(new Color(44, 62, 80));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Create seat map for each class
        if (flight.getCapacity(BookingClass.ECONOMY) > 0) {
            section.add(createClassSeatMap(BookingClass.ECONOMY, "Economy Class", new Color(52, 152, 219)));
            section.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        if (flight.getCapacity(BookingClass.BUSINESS) > 0) {
            section.add(createClassSeatMap(BookingClass.BUSINESS, "Business Class", new Color(155, 89, 182)));
            section.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        if (flight.getCapacity(BookingClass.FIRST) > 0) {
            section.add(createClassSeatMap(BookingClass.FIRST, "First Class", new Color(241, 196, 15)));
        }
        
        return section;
    }
    
    private JPanel createClassSeatMap(BookingClass bookingClass, String className, Color themeColor) {
        JPanel classPanel = new JPanel(new BorderLayout(10, 10));
        classPanel.setBackground(Color.WHITE);
        classPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        classPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel classLabel = new JLabel(className);
        classLabel.setFont(new Font("Arial", Font.BOLD, 18));
        classLabel.setForeground(themeColor);
        
        int rows = flight.getRows(bookingClass);
        int cols = flight.getColumns(bookingClass);
        int capacity = flight.getCapacity(bookingClass);
        Set<String> occupied = flight.getOccupiedSeats(bookingClass);
        int available = capacity - occupied.size();
        
        JLabel statsLabel = new JLabel(occupied.size() + " / " + capacity + " booked (" + available + " available)");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(127, 140, 141));
        
        headerPanel.add(classLabel, BorderLayout.WEST);
        headerPanel.add(statsLabel, BorderLayout.EAST);
        
        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        legendPanel.setBackground(Color.WHITE);
        
        legendPanel.add(createLegendItem("Available", new Color(200, 255, 200)));
        legendPanel.add(createLegendItem("Booked", new Color(231, 76, 60)));
        
        // Seat grid
        JPanel seatGrid = new JPanel(new GridLayout(rows, cols, 4, 4));
        seatGrid.setBackground(Color.WHITE);
        
        for (int r = 1; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                char colLetter = (char)('A' + c);
                String seatCode = r + String.valueOf(colLetter);
                
                JPanel seatBtn = new JPanel();
                seatBtn.setLayout(new BorderLayout());
                seatBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                
                JLabel seatLabel = new JLabel(seatCode, SwingConstants.CENTER);
                seatLabel.setFont(new Font("Arial", Font.BOLD, 11));
                
                if (occupied.contains(seatCode)) {
                    seatBtn.setBackground(new Color(231, 76, 60));
                    seatLabel.setForeground(Color.WHITE);
                } else {
                    seatBtn.setBackground(new Color(200, 255, 200));
                    seatLabel.setForeground(Color.BLACK);
                }
                
                seatBtn.add(seatLabel, BorderLayout.CENTER);
                seatGrid.add(seatBtn);
            }
        }
        
        JScrollPane seatScrollPane = new JScrollPane(seatGrid);
        seatScrollPane.setBorder(null);
        seatScrollPane.setPreferredSize(new Dimension(600, Math.min(300, rows * 25 + 20)));
        
        classPanel.add(headerPanel, BorderLayout.NORTH);
        classPanel.add(legendPanel, BorderLayout.SOUTH);
        classPanel.add(seatScrollPane, BorderLayout.CENTER);
        
        return classPanel;
    }
    
    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setBackground(Color.WHITE);
        
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        
        item.add(colorBox);
        item.add(label);
        
        return item;
    }
}
