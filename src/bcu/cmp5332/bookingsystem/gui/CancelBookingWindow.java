package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class CancelBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Customer> customerCombo;
    private JButton loadBookingsBtn = new JButton("Load Customer Bookings");
    private JPanel bookingsListPanel = new JPanel();
    private JButton confirmCancelBtn = new JButton("Cancel Selected Booking");
    private JButton exitBtn = new JButton("Exit");
    
    private Customer selectedCustomer;
    private JList<Booking> bookingJList;
    private DefaultListModel<Booking> listModel = new DefaultListModel<>();

    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Cancel Booking");
        setSize(500, 500);
        setLayout(new BorderLayout(10, 10));

        FlightBookingSystem fbs = mw.getFlightBookingSystem();
        java.util.List<Customer> customers = fbs.getCustomers();
        customerCombo = new JComboBox<>(customers.toArray(new Customer[0]));
        customerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Customer) {
                    Customer c = (Customer) value;
                    value = c.getId() + " - " + c.getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.add(new JLabel("Customer:"));
        topPanel.add(customerCombo);
        topPanel.add(new JLabel(""));
        topPanel.add(loadBookingsBtn);

        bookingsListPanel.setLayout(new BorderLayout());
        bookingsListPanel.setBorder(BorderFactory.createTitledBorder("Customer's Active Bookings"));
        bookingJList = new JList<>(listModel);
        bookingJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Booking) {
                    Booking b = (Booking) value;
                    String text = "Flight " + b.getFlight().getFlightNumber() + " | Date: " + b.getBookingDate() + " | Seat: " + b.getSeatNumber();
                    return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        bookingsListPanel.add(new JScrollPane(bookingJList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(confirmCancelBtn);
        bottomPanel.add(exitBtn);

        loadBookingsBtn.addActionListener(this);
        confirmCancelBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        confirmCancelBtn.setEnabled(false);

        add(topPanel, BorderLayout.NORTH);
        add(bookingsListPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loadBookingsBtn) {
            loadBookings();
        } else if (ae.getSource() == confirmCancelBtn) {
            cancelBooking();
        } else if (ae.getSource() == exitBtn) {
            this.setVisible(false);
        }
    }

    private void loadBookings() {
        selectedCustomer = (Customer) customerCombo.getSelectedItem();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        listModel.clear();
        List<Booking> bookings = selectedCustomer.getBookings();
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "This customer has no active bookings.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            confirmCancelBtn.setEnabled(false);
        } else {
            for (Booking b : bookings) {
                listModel.addElement(b);
            }
            confirmCancelBtn.setEnabled(true);
        }
    }

    private void cancelBooking() {
        Booking toCancel = bookingJList.getSelectedValue();
        if (toCancel == null) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel", "Selection Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel booking for Flight " + toCancel.getFlight().getFlightNumber() + "?", 
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
            
        if (response == JOptionPane.YES_OPTION) {
            try {
                selectedCustomer.removeBooking(toCancel);
                
                boolean hasOtherBookings = false;
                for (Booking b : selectedCustomer.getBookings()) {
                    if (b.getFlight().equals(toCancel.getFlight())) {
                        hasOtherBookings = true;
                        break;
                    }
                }
                if (!hasOtherBookings) {
                    toCancel.getFlight().removePassenger(selectedCustomer);
                }
                
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookings();
                mw.refreshBookingsPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error during cancellation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
