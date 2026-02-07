package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EditBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField bookingIdText = new JTextField();
    private JTextField newFlightIdText = new JTextField();
    private JComboBox<String> classCombo = new JComboBox<>(new String[]{"ECONOMY", "BUSINESS", "FIRST"});
    private JTextField newSeatText = new JTextField();
    private JButton updateBtn = new JButton("Update Booking");
    private JButton cancelBtn = new JButton("Cancel");

    public EditBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Edit Booking");
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        formPanel.add(new JLabel("Customer ID:"));
        formPanel.add(customerIdText);
        formPanel.add(new JLabel("Current Flight ID:"));
        formPanel.add(bookingIdText);
        formPanel.add(new JLabel("New Flight ID:"));
        formPanel.add(newFlightIdText);
        formPanel.add(new JLabel("New Class:"));
        formPanel.add(classCombo);
        formPanel.add(new JLabel("New Seat Number:"));
        formPanel.add(newSeatText);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updateBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void updateBooking() {
        try {
            int customerId = Integer.parseInt(customerIdText.getText());
            int oldFlightId = Integer.parseInt(bookingIdText.getText());
            int newFlightId = Integer.parseInt(newFlightIdText.getText());
            BookingClass newClass = BookingClass.valueOf((String) classCombo.getSelectedItem());
            String newSeat = newSeatText.getText().trim().toUpperCase();
            
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            Flight newFlight = mw.getFlightBookingSystem().getFlightByID(newFlightId);
            
            Booking oldBooking = null;
            for (Booking b : customer.getBookings()) {
                if (b.getFlight().getId() == oldFlightId) {
                    oldBooking = b;
                    break;
                }
            }
            
            if (oldBooking == null) {
                JOptionPane.showMessageDialog(this, "Booking not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newFlight.getOccupiedSeats(newClass).contains(newSeat)) {
                JOptionPane.showMessageDialog(this, "Seat " + newSeat + " is already occupied", 
                                            "Seat Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Flight oldFlight = oldBooking.getFlight();
            
            customer.removeBooking(oldBooking);
            boolean stillOnOldFlight = false;
            for (Booking b : customer.getBookings()) {
                if (b.getFlight().equals(oldFlight)) {
                    stillOnOldFlight = true;
                    break;
                }
            }
            if (!stillOnOldFlight) {
                oldFlight.removePassenger(customer);
            }
            
            Booking newBooking = new Booking(customer, newFlight, 
                                           mw.getFlightBookingSystem().getSystemDate(), 
                                           newClass, newSeat);
            customer.addBooking(newBooking);
            newFlight.addPassenger(customer);
            
            JOptionPane.showMessageDialog(this, "Booking updated successfully!", 
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
