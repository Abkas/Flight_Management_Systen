package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PlanesPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public PlanesPanel(MainWindow mw) {
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
        
        JLabel headerLabel = new JLabel("Aircraft Fleet");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));

        JButton addPlaneBtn = new JButton("+ Add Aircraft");
        addPlaneBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addPlaneBtn.setForeground(Color.WHITE);
        addPlaneBtn.setBackground(new Color(46, 204, 113));
        addPlaneBtn.setFocusPainted(false);
        addPlaneBtn.setBorderPainted(false);
        addPlaneBtn.setPreferredSize(new Dimension(140, 36));
        addPlaneBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addPlaneBtn.addActionListener(e -> new AddPlaneWindow(mw));

        buttonsPanel.add(addPlaneBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Create cards panel
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(0, 3, 20, 20));
        cardsPanel.setBackground(new Color(240, 240, 245));
        cardsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        java.util.List<Plane> planesList = fbs.getPlanes();
        for (Plane plane : planesList) {
            cardsPanel.add(createPlaneCard(plane));
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createPlaneCard(Plane plane) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 248, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    new EmptyBorder(19, 19, 19, 19)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mw.showPlaneDetail(plane);
            }
        });
        
        // Top section - Icon and ID
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("✈");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        iconLabel.setForeground(new Color(52, 152, 219));
        
        JLabel idLabel = new JLabel("#" + plane.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setForeground(new Color(149, 165, 166));
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(idLabel, BorderLayout.EAST);
        
        // Middle section - Model and Registration
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setOpaque(false);
        middlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel modelLabel = new JLabel(plane.getModel());
        modelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        modelLabel.setForeground(new Color(44, 62, 80));
        modelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel regLabel = new JLabel(plane.getRegistrationNumber());
        regLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        regLabel.setForeground(new Color(127, 140, 141));
        regLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        middlePanel.add(modelLabel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        middlePanel.add(regLabel);
        
        // Bottom section - Capacity info
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 0, 0, 0)
        ));
        
        bottomPanel.add(createInfoLabel("Total Capacity", String.valueOf(plane.getTotalCapacity()) + " seats"));
        bottomPanel.add(createInfoLabel("Economy", String.valueOf(plane.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.ECONOMY))));
        bottomPanel.add(createInfoLabel("Business", String.valueOf(plane.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.BUSINESS))));
        bottomPanel.add(createInfoLabel("First Class", String.valueOf(plane.getCapacity(bcu.cmp5332.bookingsystem.model.BookingClass.FIRST))));
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(middlePanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createInfoLabel(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        titleLabel.setForeground(new Color(149, 165, 166));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 13));
        valueLabel.setForeground(new Color(44, 62, 80));
        
        panel.add(titleLabel);
        panel.add(valueLabel);
        
        return panel;
    }
}
