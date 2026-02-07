package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
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
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel headerLabel = new JLabel("Dashboard Overview");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(new Color(44, 62, 80));
        add(headerLabel, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(240, 240, 245));
        
        int totalFlights = fbs.getFlights().size();
        int totalCustomers = fbs.getCustomers().size();
        int totalBookings = 0;
        for (Customer customer : fbs.getCustomers()) {
            totalBookings += customer.getBookings().size();
        }
        
        statsPanel.add(createStatCard("Total Flights", String.valueOf(totalFlights), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Customers", String.valueOf(totalCustomers), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Bookings", String.valueOf(totalBookings), new Color(230, 126, 34)));
        
        add(statsPanel, BorderLayout.CENTER);
        
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        actionsPanel.setBackground(new Color(240, 240, 245));
        
        JButton addFlightBtn = createActionButton("+ Add Flight", new Color(46, 204, 113));
        JButton addCustomerBtn = createActionButton("+ Add Customer", new Color(52, 152, 219));
        JButton addBookingBtn = createActionButton("+ New Booking", new Color(230, 126, 34));
        
        addFlightBtn.addActionListener(e -> new AddFlightWindow(mw));
        addCustomerBtn.addActionListener(e -> new AddCustomerWindow(mw));
        actionsPanel.add(addFlightBtn);
        actionsPanel.add(addCustomerBtn);
        actionsPanel.add(addBookingBtn);
        
        add(actionsPanel, BorderLayout.SOUTH);
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
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
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
