package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame implements ActionListener {

    private FlightBookingSystem fbs;
    
    // Sidebar buttons
    private JButton dashboardBtn;
    private JButton flightsBtn;
    private JButton planesBtn;
    private JButton customersBtn;
    private JButton bookingsBtn;
    private JButton exitBtn;
    
    // Content panel
    private JPanel contentPanel;
    private JPanel sidebarPanel;

    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }
    
    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Flight Booking Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create sidebar
        createSidebar();
        
        // Create main content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 245));
        
        // Show dashboard by default
        showDashboard();
        
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));
        sidebarPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Title
        JLabel titleLabel = new JLabel("Flight Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Navigation buttons
        dashboardBtn = createSidebarButton("Dashboard", new Color(52, 152, 219));
        flightsBtn = createSidebarButton("Flights", new Color(46, 204, 113));
        planesBtn = createSidebarButton("Aircraft", new Color(41, 128, 185));
        customersBtn = createSidebarButton("Customers", new Color(155, 89, 182));
        bookingsBtn = createSidebarButton("Bookings", new Color(230, 126, 34));
        
        sidebarPanel.add(dashboardBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(flightsBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(planesBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(customersBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(bookingsBtn);
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Exit button at bottom
        exitBtn = createSidebarButton("Exit", new Color(231, 76, 60));
        sidebarPanel.add(exitBtn);
        
        // Add action listeners
        dashboardBtn.addActionListener(this);
        flightsBtn.addActionListener(this);
        planesBtn.addActionListener(this);
        customersBtn.addActionListener(this);
        bookingsBtn.addActionListener(this);
        exitBtn.addActionListener(this);
    }

    private JButton createSidebarButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setPreferredSize(new Dimension(220, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }

    private void showDashboard() {
        contentPanel.removeAll();
        contentPanel.add(new DashboardPanel(this));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showFlightsPanel() {
        contentPanel.removeAll();
        contentPanel.add(new FlightsPanel(this));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showCustomersPanel() {
        contentPanel.removeAll();
        contentPanel.add(new CustomersPanel(this));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showBookingsPanel() {
        contentPanel.removeAll();
        contentPanel.add(new BookingsPanel(this));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showPlanesPanel() {
        contentPanel.removeAll();
        contentPanel.add(new PlanesPanel(this));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void refreshFlightsPanel() {
        showFlightsPanel();
    }
    
    public void refreshPlanesPanel() {
        showPlanesPanel();
    }
    
    public void refreshCustomersPanel() {
        showCustomersPanel();
    }

    public void refreshBookingsPanel() {
        showBookingsPanel();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == dashboardBtn) {
            showDashboard();
        } else if (ae.getSource() == flightsBtn) {
            showFlightsPanel();
        } else if (ae.getSource() == planesBtn) {
            showPlanesPanel();
        } else if (ae.getSource() == customersBtn) {
            showCustomersPanel();
        } else if (ae.getSource() == bookingsBtn) {
            showBookingsPanel();
        } else if (ae.getSource() == exitBtn) {
            try {
                FlightBookingSystemData.store(fbs);
                JOptionPane.showMessageDialog(this, "Data saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }   
    }
}
