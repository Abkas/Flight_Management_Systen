package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * GUI panel that shows all flights as cards and
 * provides a button to open the AddFlight window.
 *
 * @author Abhis
 */
public class FlightsPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public FlightsPanel(MainWindow mw) {
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
        
        JLabel headerLabel = new JLabel("✈️ Flights Management");
        headerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));

        JButton addFlightBtn = new JButton("+ Add Flight");
        addFlightBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addFlightBtn.setForeground(Color.WHITE);
        addFlightBtn.setBackground(new Color(46, 204, 113));
        addFlightBtn.setFocusPainted(false);
        addFlightBtn.setBorderPainted(false);
        addFlightBtn.setPreferredSize(new Dimension(130, 36));
        addFlightBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addFlightBtn.addActionListener(e -> new AddFlightWindow(mw));

        buttonsPanel.add(addFlightBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Cards panel
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 2, 15, 15));
        cardsPanel.setBackground(new Color(240, 240, 245));
        
        java.util.List<Flight> flightsList = fbs.getFlights();
        for (Flight flight : flightsList) {
            cardsPanel.add(createFlightCard(flight));
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createFlightCard(Flight flight) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Top panel - Icon and Flight Number
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel("✈️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel flightNoLabel = new JLabel(flight.getFlightNumber());
        flightNoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        flightNoLabel.setForeground(new Color(52, 152, 219));
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(flightNoLabel, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel routeLabel = new JLabel(flight.getOrigin() + " → " + flight.getDestination());
        routeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        routeLabel.setForeground(new Color(44, 62, 80));
        
        JLabel dateLabel = new JLabel("📅 " + flight.getDepartureDate().toString());
        dateLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(127, 140, 141));
        
        JLabel passengersLabel = new JLabel("👥 " + flight.getPassengers().size() + " passengers");
        passengersLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        passengersLabel.setForeground(new Color(127, 140, 141));
        
        JLabel planeLabel = new JLabel("Aircraft: " + flight.getPlane().getModel());
        planeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        planeLabel.setForeground(new Color(149, 165, 166));
        
        infoPanel.add(routeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(passengersLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(planeLabel);
        
        // Price panel
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 0, 0, 0)
        ));
        
        JLabel priceLabel = new JLabel("From NPR " + String.format("%,.2f", flight.getEconomyPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(46, 204, 113));
        pricePanel.add(priceLabel);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(pricePanel, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    new EmptyBorder(14, 14, 14, 14)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mw.showFlightDetail(flight);
            }
        });
        
        return card;
    }
}
