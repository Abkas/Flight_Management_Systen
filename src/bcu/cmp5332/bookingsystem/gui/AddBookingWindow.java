package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AddBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField flightIdText = new JTextField();
    private JComboBox<String> classCombo;
    private JTextField seatNumberText = new JTextField();
    private JButton bookBtn = new JButton("Create Booking");
    private JButton cancelBtn = new JButton("Cancel");

    public AddBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Create New Booking");
        setSize(400, 250);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        formPanel.add(new JLabel("Customer ID:"));
        formPanel.add(customerIdText);
        formPanel.add(new JLabel("Flight ID:"));
        formPanel.add(flightIdText);
        formPanel.add(new JLabel("Class:"));
        
        String[] classes = {"ECONOMY", "BUSINESS", "FIRST"};
        classCombo = new JComboBox<>(classes);
        formPanel.add(classCombo);
        
        formPanel.add(new JLabel("Seat Number (e.g., 12A):"));
        formPanel.add(seatNumberText);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);

        bookBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bookBtn) {
            createBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void createBooking() {
        try {
            int customerId = Integer.parseInt(customerIdText.getText());
            int flightId = Integer.parseInt(flightIdText.getText());
            BookingClass bookingClass = BookingClass.valueOf((String) classCombo.getSelectedItem());
            String seatNumber = seatNumberText.getText().trim().toUpperCase();
            
            if (seatNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a seat number", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            Flight flight = mw.getFlightBookingSystem().getFlightByID(flightId);
            
            if (flight.getOccupiedSeats(bookingClass).contains(seatNumber)) {
                JOptionPane.showMessageDialog(this, "Seat " + seatNumber + " is already occupied", 
                                            "Seat Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!flight.hasSpace(bookingClass)) {
                JOptionPane.showMessageDialog(this, "No space available in " + bookingClass + " class", 
                                            "Full", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Booking booking = new Booking(customer, flight, 
                                        mw.getFlightBookingSystem().getSystemDate(), 
                                        bookingClass, seatNumber);
            customer.addBooking(booking);
            flight.addPassenger(customer);
            
            JOptionPane.showMessageDialog(this, "Booking created successfully for seat " + seatNumber + "!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            
            this.setVisible(false);
            mw.refreshBookingsPanel();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric IDs", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
