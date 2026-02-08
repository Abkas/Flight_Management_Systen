package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;

public class PlaneDetailPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    private Plane plane;
    
    public PlaneDetailPanel(MainWindow mw, Plane plane) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        this.plane = plane;
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JButton backBtn = new JButton("← Back to Fleet");
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setForeground(new Color(52, 152, 219));
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(true);
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> mw.refreshPlanesPanel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton editBtn = new JButton("Edit Aircraft");
        editBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBackground(new Color(241, 196, 15));
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setPreferredSize(new Dimension(130, 36));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> new EditPlaneWindow(mw));
        
        JButton deleteBtn = new JButton("Delete Aircraft");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setPreferredSize(new Dimension(140, 36));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> new DeletePlaneWindow(mw));
        
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        
        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 245));
        
        // Plane details card
        JPanel detailsCard = createDetailsCard();
        detailsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        // Flights section
        JPanel flightsSection = createFlightsSection();
        
        contentPanel.add(detailsCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(flightsSection);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new BorderLayout(20, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        // Left side - Icon and basic info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("✈️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        iconLabel.setForeground(new Color(52, 152, 219));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel idLabel = new JLabel("✈️ Aircraft #" + plane.getId());
        idLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        idLabel.setForeground(new Color(149, 165, 166));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(idLabel);
        
        // Right side - Detailed info
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        JLabel modelLabel = new JLabel(plane.getModel());
        modelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        modelLabel.setForeground(new Color(44, 62, 80));
        
        JLabel regLabel = new JLabel("Registration: " + plane.getRegistrationNumber());
        regLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        regLabel.setForeground(new Color(127, 140, 141));
        
        rightPanel.add(modelLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(regLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Capacity grid
        JPanel capacityGrid = new JPanel(new GridLayout(2, 2, 20, 15));
        capacityGrid.setOpaque(false);
        
        capacityGrid.add(createCapacityBox("Total Capacity", 
            plane.getTotalCapacity() + " seats", new Color(52, 152, 219)));
        capacityGrid.add(createCapacityBox("Economy Class", 
            plane.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.ECONOMY) + " seats (" + 
            plane.getEconomyRows() + "×" + plane.getEconomyColumns() + ")", new Color(46, 204, 113)));
        capacityGrid.add(createCapacityBox("Business Class", 
            plane.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.BUSINESS) + " seats (" + 
            plane.getBusinessRows() + "×" + plane.getBusinessColumns() + ")", new Color(241, 196, 15)));
        capacityGrid.add(createCapacityBox("First Class", 
            plane.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.FIRST) + " seats (" + 
            plane.getFirstRows() + "×" + plane.getFirstColumns() + ")", new Color(155, 89, 182)));
        
        rightPanel.add(capacityGrid);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createCapacityBox(String title, String value, Color accentColor) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(248, 249, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setForeground(new Color(44, 62, 80));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        box.add(titleLabel);
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        box.add(valueLabel);
        
        return box;
    }
    
    private JPanel createFlightsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        
        // Section header
        JLabel headerLabel = new JLabel("Scheduled Flights");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(new Color(44, 62, 80));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(headerLabel);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Get flights using this plane
        List<Flight> scheduledFlights = new ArrayList<>();
        for (Flight flight : fbs.getFlights()) {
            if (flight.getPlane() != null && flight.getPlane().getId() == plane.getId()) {
                scheduledFlights.add(flight);
            }
        }
        
        if (scheduledFlights.isEmpty()) {
            JPanel emptyCard = new JPanel();
            emptyCard.setLayout(new BoxLayout(emptyCard, BoxLayout.Y_AXIS));
            emptyCard.setBackground(Color.WHITE);
            emptyCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(40, 40, 40, 40)
            ));
            emptyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            emptyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            
            JLabel emptyIcon = new JLabel("📅");
            emptyIcon.setFont(new Font("Arial", Font.PLAIN, 48));
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel emptyLabel = new JLabel("No flights scheduled for this aircraft");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(149, 165, 166));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            emptyCard.add(emptyIcon);
            emptyCard.add(Box.createRigidArea(new Dimension(0, 10)));
            emptyCard.add(emptyLabel);
            
            section.add(emptyCard);
        } else {
            for (Flight flight : scheduledFlights) {
                section.add(createFlightCard(flight));
                section.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        return section;
    }
    
    private JPanel createFlightCard(Flight flight) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Left - Flight number and route
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel flightNumLabel = new JLabel("Flight " + flight.getFlightNumber());
        flightNumLabel.setFont(new Font("Arial", Font.BOLD, 16));
        flightNumLabel.setForeground(new Color(44, 62, 80));
        flightNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        routeLabel.setForeground(new Color(127, 140, 141));
        routeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(flightNumLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(routeLabel);
        
        // Center - Date and passengers
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);
        
        JLabel dateLabel = new JLabel("📅 " + flight.getDepartureDate().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(52, 152, 219));
        
        JLabel passengersLabel = new JLabel("👥 " + flight.getPassengers().size() + " passengers");
        passengersLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        passengersLabel.setForeground(new Color(46, 204, 113));
        
        centerPanel.add(dateLabel);
        centerPanel.add(passengersLabel);
        
        // Right - ID badge
        JLabel idBadge = new JLabel("#" + flight.getId());
        idBadge.setFont(new Font("Arial", Font.BOLD, 14));
        idBadge.setForeground(new Color(149, 165, 166));
        idBadge.setOpaque(true);
        idBadge.setBackground(new Color(236, 240, 241));
        idBadge.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(idBadge, BorderLayout.EAST);
        
        return card;
    }
}
