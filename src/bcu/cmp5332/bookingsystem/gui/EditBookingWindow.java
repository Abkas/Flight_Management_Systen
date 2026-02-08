package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EditBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;

    private JComboBox<Customer> customerCombo;
    private JComboBox<Booking> bookingCombo;
    private JLabel currentFlightLabel = new JLabel("-");
    private JComboBox<Flight> newFlightCombo;
    private JComboBox<BookingClass> classCombo;

    private JPanel seatGridPanel = new JPanel();
    private String selectedSeatNumber = null;
    private JButton selectedSeatButton = null;

    private JButton updateBtn = new JButton("Update Booking");
    private JButton cancelBtn = new JButton("Cancel");

    public EditBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Edit Booking");
        setSize(650, 500);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        FlightBookingSystem fbs = mw.getFlightBookingSystem();
        java.util.List<Customer> customers = fbs.getCustomers();
        java.util.List<Flight> flights = fbs.getFlights();

        // Customer combo
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

        // Booking combo (populated when customer changes)
        bookingCombo = new JComboBox<>();
        bookingCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Booking) {
                    Booking b = (Booking) value;
                    value = "Flight " + b.getFlight().getFlightNumber() + " | " + b.getBookingClass() + " | Seat " + b.getSeatNumber() + " | NPR " + String.format("%.2f", b.getBookedPrice());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // New flight combo
        newFlightCombo = new JComboBox<>(flights.toArray(new Flight[0]));
        newFlightCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Flight) {
                    Flight f = (Flight) value;
                    value = f.getId() + " - " + f.getFlightNumber() + " (" + f.getOrigin() + " → " + f.getDestination() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Class combo
        classCombo = new JComboBox<>(BookingClass.values());

        // Top selection panel
        JPanel selectionPanel = new JPanel(new GridLayout(5, 2, 10, 8));
        selectionPanel.add(new JLabel("Customer:", SwingConstants.RIGHT));
        selectionPanel.add(customerCombo);
        selectionPanel.add(new JLabel("Select Booking:", SwingConstants.RIGHT));
        selectionPanel.add(bookingCombo);
        selectionPanel.add(new JLabel("Current Flight:", SwingConstants.RIGHT));
        selectionPanel.add(currentFlightLabel);
        selectionPanel.add(new JLabel("New Flight:", SwingConstants.RIGHT));
        selectionPanel.add(newFlightCombo);
        selectionPanel.add(new JLabel("New Class:", SwingConstants.RIGHT));
        selectionPanel.add(classCombo);

        // Seat grid panel
        JPanel seatContainer = new JPanel(new BorderLayout(5, 5));
        JLabel seatHeader = new JLabel("Seat Map (select one seat)", SwingConstants.CENTER);
        seatHeader.setFont(seatHeader.getFont().deriveFont(Font.BOLD, 14f));
        seatContainer.add(seatHeader, BorderLayout.NORTH);

        seatGridPanel.setLayout(new GridLayout(1, 1));
        seatGridPanel.add(new JLabel("Select a booking, new flight and class to view seats.", SwingConstants.CENTER));

        JScrollPane seatScroll = new JScrollPane(seatGridPanel);
        seatScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        seatContainer.add(seatScroll, BorderLayout.CENTER);

        // Listeners
        customerCombo.addActionListener(e -> loadCustomerBookings());
        bookingCombo.addActionListener(e -> onBookingSelected());
        newFlightCombo.addActionListener(e -> refreshSeatGrid());
        classCombo.addActionListener(e -> refreshSeatGrid());

        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        mainPanel.add(seatContainer, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        loadCustomerBookings();

        setLocationRelativeTo(mw);
        setVisible(true);
    }

    private void loadCustomerBookings() {
        bookingCombo.removeAllItems();
        currentFlightLabel.setText("-");

        Customer customer = (Customer) customerCombo.getSelectedItem();
        if (customer == null) {
            return;
        }

        java.util.List<Booking> bookings = customer.getBookings();
        for (Booking b : bookings) {
            bookingCombo.addItem(b);
        }

        if (bookings.isEmpty()) {
            showSeatMessage("This customer has no bookings to edit.");
        } else {
            onBookingSelected();
        }
    }

    private void onBookingSelected() {
        Booking booking = (Booking) bookingCombo.getSelectedItem();
        if (booking == null) {
            currentFlightLabel.setText("-");
            showSeatMessage("Select a booking to edit.");
            return;
        }

        Flight currentFlight = booking.getFlight();
        currentFlightLabel.setText(currentFlight.getId() + " - " + currentFlight.getFlightNumber() +
                " (" + currentFlight.getOrigin() + " → " + currentFlight.getDestination() + ")");

        // Auto-select the current flight and class in the new selectors
        newFlightCombo.setSelectedItem(currentFlight);
        classCombo.setSelectedItem(booking.getBookingClass());

        refreshSeatGrid();
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
            Customer customer = (Customer) customerCombo.getSelectedItem();
            Booking oldBooking = (Booking) bookingCombo.getSelectedItem();
            Flight newFlight = (Flight) newFlightCombo.getSelectedItem();
            BookingClass newClass = (BookingClass) classCombo.getSelectedItem();

            if (customer == null || oldBooking == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer and a booking.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (newFlight == null) {
                JOptionPane.showMessageDialog(this, "Please select a new flight.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedSeatNumber == null) {
                JOptionPane.showMessageDialog(this, "Please select a seat from the seat map.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if seat is occupied (unless it's the same booking being edited on the same flight/class/seat)
            Set<String> occupied = newFlight.getOccupiedSeats(newClass);
            boolean isSameSeat = newFlight.equals(oldBooking.getFlight()) &&
                                 newClass.equals(oldBooking.getBookingClass()) &&
                                 selectedSeatNumber.equals(oldBooking.getSeatNumber());
            if (occupied.contains(selectedSeatNumber) && !isSameSeat) {
                JOptionPane.showMessageDialog(this, "Seat " + selectedSeatNumber + " is already occupied.",
                        "Seat Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Flight oldFlight = oldBooking.getFlight();
            
            // Calculate price difference
            double oldPrice = oldBooking.getBookedPrice();
            double newPrice = newFlight.getPrice(newClass);
            double priceDifference = newPrice - oldPrice;
            
            // Show price change message
            String priceMessage = "";
            if (priceDifference > 0) {
                priceMessage = "Price Change: You need to pay additional NPR " + String.format("%.2f", priceDifference) + "\n" +
                              "(Old price: NPR " + String.format("%.2f", oldPrice) + " → New price: NPR " + String.format("%.2f", newPrice) + ")\n\n" +
                              "Do you want to continue?";
                int confirm = JOptionPane.showConfirmDialog(this, priceMessage, "Additional Payment Required", 
                                                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            } else if (priceDifference < 0) {
                priceMessage = "Price Change: You will be refunded NPR " + String.format("%.2f", Math.abs(priceDifference)) + "\n" +
                              "(Old price: NPR " + String.format("%.2f", oldPrice) + " → New price: NPR " + String.format("%.2f", newPrice) + ")\n\n" +
                              "Do you want to continue?";
                int confirm = JOptionPane.showConfirmDialog(this, priceMessage, "Refund Applicable", 
                                                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            } else {
                // No price change, just confirm
                int confirm = JOptionPane.showConfirmDialog(this, "No price change. Confirm booking update?", 
                                                            "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Remove old booking
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

            // Create new booking
            Booking newBooking = new Booking(customer, newFlight,
                    mw.getFlightBookingSystem().getSystemDate(),
                    newClass, selectedSeatNumber, newPrice);
            customer.addBooking(newBooking);
            newFlight.addPassenger(customer);

            JOptionPane.showMessageDialog(this, "Booking updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshBookingsPanel();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshSeatGrid() {
        selectedSeatNumber = null;
        selectedSeatButton = null;

        Booking oldBooking = (Booking) bookingCombo.getSelectedItem();
        Flight flight = (Flight) newFlightCombo.getSelectedItem();
        BookingClass bookingClass = (BookingClass) classCombo.getSelectedItem();

        if (flight == null || bookingClass == null) {
            showSeatMessage("Select a new flight and class to view seats.");
            return;
        }

        int rows = flight.getRows(bookingClass);
        int cols = flight.getColumns(bookingClass);

        if (rows <= 0 || cols <= 0) {
            showSeatMessage("This class has no seats for this flight.");
            return;
        }

        seatGridPanel.removeAll();
        Set<String> occupied = flight.getOccupiedSeats(bookingClass);

        // If editing on same flight/class, the old seat should appear available
        String oldSeat = null;
        if (oldBooking != null && flight.equals(oldBooking.getFlight()) && bookingClass.equals(oldBooking.getBookingClass())) {
            oldSeat = oldBooking.getSeatNumber();
        }

        seatGridPanel.setLayout(new GridLayout(rows, cols, 4, 4));

        final Color availableColor = new Color(200, 255, 200);
        final Color selectedColor = new Color(46, 204, 113);
        final String finalOldSeat = oldSeat;

        for (int r = 1; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                char colLetter = (char) ('A' + c);
                String seatCode = r + String.valueOf(colLetter);

                JButton seatBtn = new JButton(seatCode);
                seatBtn.setMargin(new Insets(2, 4, 2, 4));
                seatBtn.setFocusPainted(false);
                seatBtn.setOpaque(true);
                seatBtn.setContentAreaFilled(true);
                seatBtn.setBorderPainted(false);

                boolean isOccupied = occupied.contains(seatCode);
                boolean isOldSeat = seatCode.equals(finalOldSeat);

                if (isOccupied && !isOldSeat) {
                    seatBtn.setBackground(new Color(231, 76, 60));
                    seatBtn.setForeground(Color.WHITE);
                    seatBtn.setEnabled(false);
                    seatBtn.setToolTipText("Occupied");
                } else {
                    seatBtn.setBackground(availableColor);
                    seatBtn.setForeground(Color.BLACK);
                    seatBtn.setToolTipText(isOldSeat ? "Current seat" : "Available");
                    seatBtn.addActionListener(e -> {
                        if (selectedSeatButton != null) {
                            selectedSeatButton.setBackground(availableColor);
                            selectedSeatButton.setForeground(Color.BLACK);
                        }
                        selectedSeatButton = seatBtn;
                        selectedSeatNumber = seatCode;
                        seatBtn.setBackground(selectedColor);
                        seatBtn.setForeground(Color.WHITE);
                    });
                }

                seatGridPanel.add(seatBtn);
            }
        }

        seatGridPanel.revalidate();
        seatGridPanel.repaint();
    }

    private void showSeatMessage(String message) {
        seatGridPanel.removeAll();
        seatGridPanel.setLayout(new GridLayout(1, 1));
        seatGridPanel.add(new JLabel(message, SwingConstants.CENTER));
        seatGridPanel.revalidate();
        seatGridPanel.repaint();
    }
}
