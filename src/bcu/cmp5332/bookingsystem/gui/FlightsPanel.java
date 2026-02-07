package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
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
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));

        JButton addFlightBtn = new JButton("+ Add");
        addFlightBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addFlightBtn.setForeground(Color.WHITE);
        addFlightBtn.setBackground(new Color(46, 204, 113));
        addFlightBtn.setFocusPainted(false);
        addFlightBtn.setBorderPainted(false);
        addFlightBtn.setPreferredSize(new Dimension(90, 36));
        addFlightBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addFlightBtn.addActionListener(e -> new AddFlightWindow(mw));

        JButton editFlightBtn = new JButton("Edit");
        editFlightBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editFlightBtn.setForeground(Color.WHITE);
        editFlightBtn.setBackground(new Color(241, 196, 15));
        editFlightBtn.setFocusPainted(false);
        editFlightBtn.setBorderPainted(false);
        editFlightBtn.setPreferredSize(new Dimension(90, 36));
        editFlightBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editFlightBtn.addActionListener(e -> new EditFlightWindow(mw));

        JButton deleteFlightBtn = new JButton("Delete");
        deleteFlightBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deleteFlightBtn.setForeground(Color.WHITE);
        deleteFlightBtn.setBackground(new Color(231, 76, 60));
        deleteFlightBtn.setFocusPainted(false);
        deleteFlightBtn.setBorderPainted(false);
        deleteFlightBtn.setPreferredSize(new Dimension(90, 36));
        deleteFlightBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteFlightBtn.addActionListener(e -> new DeleteFlightWindow(mw));

        buttonsPanel.add(addFlightBtn);
        buttonsPanel.add(editFlightBtn);
        buttonsPanel.add(deleteFlightBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
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
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setGridColor(new Color(210, 210, 210));
        table.setShowVerticalLines(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(235, 235, 235));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 30));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(header);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
