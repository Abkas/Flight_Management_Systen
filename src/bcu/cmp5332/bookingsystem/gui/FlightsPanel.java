package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        
        JLabel headerLabel = new JLabel("Flights Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JButton addFlightBtn = new JButton("+ Add New Flight");
        addFlightBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addFlightBtn.setForeground(Color.WHITE);
        addFlightBtn.setBackground(new Color(46, 204, 113));
        addFlightBtn.setFocusPainted(false);
        addFlightBtn.setBorderPainted(false);
        addFlightBtn.setPreferredSize(new Dimension(200, 40));
        addFlightBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addFlightBtn.addActionListener(e -> new AddFlightWindow(mw));
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(addFlightBtn, BorderLayout.EAST);
        
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Departure Date", "Passengers"};
        java.util.List<Flight> flightsList = fbs.getFlights();
        
        Object[][] data = new Object[flightsList.size()][6];
        for (int i = 0; i < flightsList.size(); i++) {
            Flight flight = flightsList.get(i);
            data[i][0] = flight.getId();
            data[i][1] = flight.getFlightNumber();
            data[i][2] = flight.getOrigin();
            data[i][3] = flight.getDestination();
            data[i][4] = flight.getDepartureDate();
            data[i][5] = flight.getPassengers().size();
        }
        
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(46, 204, 113));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(46, 204, 113, 100));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
