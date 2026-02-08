package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.BookingClass;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookingsPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public BookingsPanel(MainWindow mw) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JLabel headerLabel = new JLabel("🎫 Bookings Management");
        headerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton addBookingBtn = createButton("+ New Booking", new Color(230, 126, 34));
        JButton editBookingBtn = createButton("Edit Booking", new Color(52, 152, 219));
        JButton cancelBookingBtn = createButton("Cancel Booking", new Color(231, 76, 60));
        
        addBookingBtn.addActionListener(e -> new AddBookingWindow(mw));
        editBookingBtn.addActionListener(e -> new EditBookingWindow(mw));
        cancelBookingBtn.addActionListener(e -> new CancelBookingWindow(mw));
        
        buttonPanel.add(addBookingBtn);
        buttonPanel.add(editBookingBtn);
        buttonPanel.add(cancelBookingBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Cards panel
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 2, 15, 15));
        cardsPanel.setBackground(new Color(240, 240, 245));
        
        // Collect all non-cancelled bookings
        List<Booking> allBookings = new ArrayList<>();
        for (Customer c : fbs.getCustomers()) {
            for (Booking b : c.getBookings()) {
                if (!b.isCancelled()) {
                    allBookings.add(b);
                }
            }
        }
        
        if (allBookings.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(60, 60, 60, 60)
            ));
            
            JLabel emptyIcon = new JLabel("🎫");
            emptyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel emptyText = new JLabel("No bookings found");
            emptyText.setFont(new Font("Arial", Font.BOLD, 20));
            emptyText.setForeground(new Color(149, 165, 166));
            emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            emptyPanel.add(emptyIcon);
            emptyPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            emptyPanel.add(emptyText);
            
            cardsPanel.add(emptyPanel);
        } else {
            for (Booking booking : allBookings) {
                cardsPanel.add(createBookingCard(booking));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createBookingCard(Booking booking) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Top panel - Ticket icon and booking class badge
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel("🎫");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JPanel classBadge = createClassBadge(booking.getBookingClass());
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(classBadge, BorderLayout.EAST);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        // Flight info
        JLabel flightLabel = new JLabel("✈️ " + booking.getFlight().getFlightNumber());
        flightLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        flightLabel.setForeground(new Color(52, 152, 219));
        
        JLabel routeLabel = new JLabel(booking.getFlight().getOrigin() + " → " + booking.getFlight().getDestination());
        routeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        routeLabel.setForeground(new Color(44, 62, 80));
        
        // Customer info
        JLabel customerLabel = new JLabel("👤 " + booking.getCustomer().getName());
        customerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        customerLabel.setForeground(new Color(127, 140, 141));
        
        // Seat and date
        JLabel seatLabel = new JLabel("💺 Seat " + (booking.getSeatNumber() != null ? booking.getSeatNumber() : "N/A"));
        seatLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        seatLabel.setForeground(new Color(127, 140, 141));
        
        JLabel dateLabel = new JLabel("📅 " + booking.getBookingDate().toString());
        dateLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(149, 165, 166));
        
        infoPanel.add(flightLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(routeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(customerLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(seatLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(dateLabel);
        
        // Bottom panel - Price
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 0, 0, 0)
        ));
        
        JLabel priceLabel = new JLabel("NPR " + String.format("%,.2f", booking.getBookedPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(46, 204, 113));
        
        bottomPanel.add(priceLabel, BorderLayout.WEST);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createClassBadge(BookingClass bookingClass) {
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        
        Color bgColor, fgColor;
        String text;
        
        switch (bookingClass) {
            case ECONOMY:
                bgColor = new Color(52, 152, 219);
                fgColor = Color.WHITE;
                text = "Economy";
                break;
            case BUSINESS:
                bgColor = new Color(155, 89, 182);
                fgColor = Color.WHITE;
                text = "Business";
                break;
            case FIRST:
                bgColor = new Color(241, 196, 15);
                fgColor = Color.WHITE;
                text = "First Class";
                break;
            default:
                bgColor = new Color(149, 165, 166);
                fgColor = Color.WHITE;
                text = "Unknown";
        }
        
        badge.setBackground(bgColor);
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(fgColor);
        
        badge.add(label);
        return badge;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
