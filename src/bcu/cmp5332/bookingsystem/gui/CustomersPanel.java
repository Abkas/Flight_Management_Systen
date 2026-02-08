package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CustomersPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public CustomersPanel(MainWindow mw) {
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
        
        JLabel headerLabel = new JLabel("👥 Customers Management");
        headerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));

        JButton addCustomerBtn = new JButton("+ Add Customer");
        addCustomerBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addCustomerBtn.setForeground(Color.WHITE);
        addCustomerBtn.setBackground(new Color(52, 152, 219));
        addCustomerBtn.setFocusPainted(false);
        addCustomerBtn.setBorderPainted(false);
        addCustomerBtn.setPreferredSize(new Dimension(140, 36));
        addCustomerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCustomerBtn.addActionListener(e -> new AddCustomerWindow(mw));

        buttonsPanel.add(addCustomerBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Cards grid
        JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        cardsPanel.setBackground(new Color(240, 240, 245));
        
        java.util.List<Customer> customersList = fbs.getCustomers();
        for (Customer customer : customersList) {
            cardsPanel.add(createCustomerCard(customer));
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createCustomerCard(Customer customer) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon and ID
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel idLabel = new JLabel("#" + customer.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setForeground(new Color(127, 140, 141));
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(idLabel, BorderLayout.EAST);
        
        // Customer info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(customer.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(44, 62, 80));
        
        String demographics = "";
        if (customer.getGender() != null && !customer.getGender().isEmpty()) {
            demographics += customer.getGender();
        }
        if (customer.getAge() > 0) {
            demographics += (demographics.isEmpty() ? "" : ", ") + customer.getAge() + " yrs";
        }
        if (!demographics.isEmpty()) {
            JLabel demoLabel = new JLabel(demographics);
            demoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            demoLabel.setForeground(new Color(95, 95, 95));
            infoPanel.add(demoLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel phoneLabel = new JLabel(customer.getPhone());
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        phoneLabel.setForeground(new Color(52, 73, 94));
        
        String email = customer.getEmail() != null && !customer.getEmail().isEmpty() 
            ? customer.getEmail() : "No email";
        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(52, 73, 94));
        
        infoPanel.add(phoneLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(emailLabel);
        
        // Booking count badge
        JPanel bookingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bookingsPanel.setBackground(Color.WHITE);
        
        JLabel bookingsLabel = new JLabel("  " + customer.getBookings().size() + " Bookings  ");
        bookingsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        bookingsLabel.setForeground(Color.WHITE);
        bookingsLabel.setBackground(new Color(46, 204, 113));
        bookingsLabel.setOpaque(true);
        bookingsLabel.setBorder(new EmptyBorder(5, 8, 5, 8));
        
        bookingsPanel.add(bookingsLabel);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(bookingsPanel, BorderLayout.SOUTH);
        
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
                mw.showCustomerDetail(customer);
            }
        });
        
        return card;
    }
}
