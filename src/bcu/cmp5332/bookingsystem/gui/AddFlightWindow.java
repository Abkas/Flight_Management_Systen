package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class AddFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();
    private JTextField economySeatsText = new JTextField();
    private JTextField businessSeatsText = new JTextField();
    private JTextField firstSeatsText = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Add a New Flight");
        setSize(450, 320);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(7, 2, 10, 10));
        
        topPanel.add(new JLabel("Flight No:"));
        topPanel.add(flightNoText);
        topPanel.add(new JLabel("Origin:"));
        topPanel.add(originText);
        topPanel.add(new JLabel("Destination:"));
        topPanel.add(destinationText);
        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD):"));
        topPanel.add(depDateText);
        topPanel.add(new JLabel("Economy seats:"));
        topPanel.add(economySeatsText);
        topPanel.add(new JLabel("Business seats:"));
        topPanel.add(businessSeatsText);
        topPanel.add(new JLabel("First class seats:"));
        topPanel.add(firstSeatsText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addFlight() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();
            LocalDate departureDate = null;
            try {
                departureDate = LocalDate.parse(depDateText.getText());
            } catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }
            int ecoSeats = Integer.parseInt(economySeatsText.getText());
            int busSeats = Integer.parseInt(businessSeatsText.getText());
            int firstSeats = Integer.parseInt(firstSeatsText.getText());

            if (ecoSeats < 0 || busSeats < 0 || firstSeats < 0) {
                throw new FlightBookingSystemException("Seat numbers cannot be negative.");
            }

            int ecoCols = 6, busCols = 4, firstCols = 4;
            int ecoRows = (ecoSeats + ecoCols - 1) / ecoCols;
            int busRows = (busSeats + busCols - 1) / busCols;
            int firstRows = (firstSeats + firstCols - 1) / firstCols;
            
            Command addFlight = new AddFlight(flightNumber, origin, destination, departureDate,
                                             ecoRows, ecoCols, busRows, busCols, firstRows, firstCols);
            addFlight.execute(mw.getFlightBookingSystem());
            
            JOptionPane.showMessageDialog(this, "Flight added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshFlightsPanel();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for seats in each class", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
