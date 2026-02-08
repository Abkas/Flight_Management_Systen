package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public DashboardPanel(MainWindow mw) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header with date
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JLabel headerLabel = new JLabel("📊 Dashboard Overview");
        headerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JLabel dateLabel = new JLabel("📅 " + fbs.getSystemDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(127, 140, 141));
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        // Main content with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 245));
        
        // Stats cards section
        contentPanel.add(createStatsSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Quick info section
        contentPanel.add(createQuickInfoSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Recent activity section
        contentPanel.add(createRecentActivitySection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Quick actions
        contentPanel.add(createQuickActionsSection());
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createStatsSection() {
        JPanel section = new JPanel(new GridLayout(1, 3, 15, 0));
        section.setBackground(new Color(240, 240, 245));
        section.setMaximumSize(new Dimension(950, 150));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        int totalFlights = fbs.getFlights().size();
        int totalCustomers = fbs.getCustomers().size();
        
        int totalBookings = 0;
        int activeBookings = 0;
        
        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                totalBookings++;
                if (!booking.isCancelled()) {
                    activeBookings++;
                }
            }
        }
        
        section.add(createStatCard("✈️ Flights", String.valueOf(totalFlights), new Color(52, 152, 219)));
        section.add(createStatCard("👥 Customers", String.valueOf(totalCustomers), new Color(155, 89, 182)));
        section.add(createStatCard("🎫 Active Bookings", String.valueOf(activeBookings), new Color(46, 204, 113)));
        
        return section;
    }
    
    private JPanel createQuickInfoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(240, 240, 245));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel sectionTitle = new JLabel("Quick Statistics");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(44, 62, 80));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 2x2 grid now that we only show 4 quick stats
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        cardsPanel.setBackground(new Color(240, 240, 245));
        cardsPanel.setMaximumSize(new Dimension(950, 200));
        
        // Calculate statistics
        int totalPlanes = fbs.getPlanes().size();
        int upcomingFlights = 0;
        int totalCapacity = 0;
        int occupiedSeats = 0;
        
        for (Flight flight : fbs.getFlights()) {
            if (flight.getDepartureDate().isAfter(fbs.getSystemDate()) || 
                flight.getDepartureDate().equals(fbs.getSystemDate())) {
                upcomingFlights++;
            }
            totalCapacity += flight.getCapacity(BookingClass.ECONOMY) + 
                            flight.getCapacity(BookingClass.BUSINESS) + 
                            flight.getCapacity(BookingClass.FIRST);
            occupiedSeats += flight.getOccupiedSeats(BookingClass.ECONOMY).size() +
                            flight.getOccupiedSeats(BookingClass.BUSINESS).size() +
                            flight.getOccupiedSeats(BookingClass.FIRST).size();
        }
        
        cardsPanel.add(createMiniStatCard("🛩️ Aircraft", String.valueOf(totalPlanes)));
        cardsPanel.add(createMiniStatCard("🚀 Upcoming Flights", String.valueOf(upcomingFlights)));
        cardsPanel.add(createMiniStatCard("💺 Total Capacity", String.valueOf(totalCapacity)));
        cardsPanel.add(createMiniStatCard("✅ Occupied Seats", String.valueOf(occupiedSeats)));
        
        section.add(cardsPanel);
        
        return section;
    }
    
    private JPanel createRecentActivitySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(240, 240, 245));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel sectionTitle = new JLabel("Recent Bookings");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(44, 62, 80));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Get recent bookings
        List<Booking> recentBookings = new ArrayList<>();
        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled()) {
                    recentBookings.add(booking);
                }
            }
        }
        
        // Sort by booking date (most recent first)
        recentBookings.sort((b1, b2) -> b2.getBookingDate().compareTo(b1.getBookingDate()));
        
        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        activityPanel.setMaximumSize(new Dimension(950, 300));
        
        if (recentBookings.isEmpty()) {
            JLabel emptyLabel = new JLabel("No recent bookings");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setForeground(new Color(149, 165, 166));
            activityPanel.add(emptyLabel);
        } else {
            int count = 0;
            for (Booking booking : recentBookings) {
                if (count >= 5) break; // Show only 5 most recent
                activityPanel.add(createActivityItem(booking));
                if (count < 4 && count < recentBookings.size() - 1) {
                    activityPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                    separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    activityPanel.add(separator);
                    activityPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
                count++;
            }
        }
        
        section.add(activityPanel);
        return section;
    }
    
    private JPanel createActivityItem(Booking booking) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setBackground(Color.WHITE);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel iconLabel = new JLabel("🎫");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel mainLabel = new JLabel(booking.getCustomer().getName() + " → " + booking.getFlight().getFlightNumber());
        mainLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainLabel.setForeground(new Color(44, 62, 80));
        
        JLabel subLabel = new JLabel(booking.getBookingClass() + " • Seat " + booking.getSeatNumber() + " • " + booking.getBookingDate());
        subLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subLabel.setForeground(new Color(127, 140, 141));
        
        infoPanel.add(mainLabel);
        infoPanel.add(subLabel);
        
        JLabel priceLabel = new JLabel(String.format("NPR %,.0f", booking.getBookedPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(46, 204, 113));
        
        item.add(iconLabel, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);
        item.add(priceLabel, BorderLayout.EAST);
        
        return item;
    }
    
    private JPanel createQuickActionsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(240, 240, 245));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel sectionTitle = new JLabel("Quick Actions");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(44, 62, 80));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionsPanel.setBackground(new Color(240, 240, 245));
        actionsPanel.setMaximumSize(new Dimension(950, 50));
        
        JButton addFlightBtn = createActionButton("+ Add Flight", new Color(52, 152, 219));
        JButton addCustomerBtn = createActionButton("+ Add Customer", new Color(155, 89, 182));
        JButton addBookingBtn = createActionButton("+ New Booking", new Color(230, 126, 34));
        
        addFlightBtn.addActionListener(e -> new AddFlightWindow(mw));
        addCustomerBtn.addActionListener(e -> new AddCustomerWindow(mw));
        addBookingBtn.addActionListener(e -> new AddBookingWindow(mw));
        
        actionsPanel.add(addFlightBtn);
        actionsPanel.add(addCustomerBtn);
        actionsPanel.add(addBookingBtn);
        
        section.add(actionsPanel);
        return section;
    }
    
    private int getTotalActiveBookings() {
        int count = 0;
        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                if (!booking.isCancelled()) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        titleLabel.setForeground(new Color(127, 140, 141));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(color);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(valueLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createMiniStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(44, 62, 80));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        
        return card;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
