package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.LinkedHashSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;

    private JComboBox<Customer> customerCombo;
    private JComboBox<Flight> flightCombo;
    private JComboBox<BookingClass> classCombo;

    private JPanel seatGridPanel = new JPanel();
    private Set<String> selectedSeatNumbers = new LinkedHashSet<>();

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
        setSize(600, 450);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top selection panel: customer, flight, class
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 8));

        FlightBookingSystem fbs = mw.getFlightBookingSystem();
        java.util.List<Customer> customers = fbs.getCustomers();
        java.util.List<Flight> flights = fbs.getFlights();

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

        flightCombo = new JComboBox<>(flights.toArray(new Flight[0]));
        flightCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Flight) {
                    Flight f = (Flight) value;
                    value = f.getId() + " - " + f.getFlightNumber() + " (" + f.getOrigin() + " → " + f.getDestination() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        classCombo = new JComboBox<>(BookingClass.values());

        selectionPanel.add(new JLabel("Customer:", SwingConstants.RIGHT));
        selectionPanel.add(customerCombo);
        selectionPanel.add(new JLabel("Flight:", SwingConstants.RIGHT));
        selectionPanel.add(flightCombo);
        selectionPanel.add(new JLabel("Class:", SwingConstants.RIGHT));
        selectionPanel.add(classCombo);

        // Seat grid panel
        JPanel seatContainer = new JPanel(new BorderLayout(5, 5));
        JLabel seatHeader = new JLabel("Seat Map", SwingConstants.CENTER);
        seatHeader.setFont(seatHeader.getFont().deriveFont(Font.BOLD, 14f));
        seatContainer.add(seatHeader, BorderLayout.NORTH);

        seatGridPanel.setLayout(new GridLayout(1, 1));
        seatGridPanel.add(new JLabel("Select a customer, flight and class to view seats.", SwingConstants.CENTER));

        JScrollPane seatScroll = new JScrollPane(seatGridPanel);
        seatScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        seatContainer.add(seatScroll, BorderLayout.CENTER);

        customerCombo.addActionListener(e -> refreshSeatGrid());
        flightCombo.addActionListener(e -> refreshSeatGrid());
        classCombo.addActionListener(e -> refreshSeatGrid());

        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        mainPanel.add(seatContainer, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);

        bookBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        add(mainPanel, BorderLayout.CENTER);
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
            Customer customer = (Customer) customerCombo.getSelectedItem();
            Flight flight = (Flight) flightCombo.getSelectedItem();
            BookingClass bookingClass = (BookingClass) classCombo.getSelectedItem();

            if (customer == null || flight == null) {
                JOptionPane.showMessageDialog(this, "Please select both a customer and a flight.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedSeatNumbers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat from the seat map.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Set<String> requestedSeats = new LinkedHashSet<>(selectedSeatNumbers);
            Set<String> occupied = flight.getOccupiedSeats(bookingClass);

            for (String seat : requestedSeats) {
                if (occupied.contains(seat)) {
                    JOptionPane.showMessageDialog(this, "Seat " + seat + " is already occupied", 
                                                "Seat Unavailable", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            int capacity = flight.getCapacity(bookingClass);
            if (occupied.size() + requestedSeats.size() > capacity) {
                JOptionPane.showMessageDialog(this, "Not enough free seats available in " + bookingClass + " class.", 
                                            "Full", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (String seat : requestedSeats) {
                Booking booking = new Booking(customer, flight,
                        mw.getFlightBookingSystem().getSystemDate(),
                        bookingClass, seat);
                customer.addBooking(booking);
                flight.addPassenger(customer);
            }

            String seatList = String.join(", ", requestedSeats);
            JOptionPane.showMessageDialog(this, "Bookings created successfully for seat(s) " + seatList + "!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            
            this.setVisible(false);
            mw.refreshBookingsPanel();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshSeatGrid() {
        selectedSeatNumbers.clear();

        Flight flight = (Flight) flightCombo.getSelectedItem();
        BookingClass bookingClass = (BookingClass) classCombo.getSelectedItem();

        if (flight == null || bookingClass == null) {
            showSeatMessage("Select a customer, flight and class to view seats.");
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

        seatGridPanel.setLayout(new GridLayout(rows, cols, 4, 4));

        final Color availableColor = new Color(200, 255, 200);
        final Color selectedColor = new Color(46, 204, 113);

        for (int r = 1; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                char colLetter = (char)('A' + c);
                String seatCode = r + String.valueOf(colLetter);

                JButton seatBtn = new JButton(seatCode);
                seatBtn.setMargin(new Insets(2, 4, 2, 4));
                seatBtn.setFocusPainted(false);
                seatBtn.setOpaque(true);
                seatBtn.setContentAreaFilled(true);
                seatBtn.setBorderPainted(false);

                if (occupied.contains(seatCode)) {
                    seatBtn.setBackground(new Color(231, 76, 60));
                    seatBtn.setForeground(Color.WHITE);
                    seatBtn.setEnabled(false);
                    seatBtn.setToolTipText("Occupied");
                } else {
                    seatBtn.setBackground(availableColor);
                    seatBtn.setForeground(Color.BLACK);
                    seatBtn.setToolTipText("Available");
                    seatBtn.addActionListener(e -> {
                        if (selectedSeatNumbers.contains(seatCode)) {
                            selectedSeatNumbers.remove(seatCode);
                            seatBtn.setBackground(availableColor);
                            seatBtn.setForeground(Color.BLACK);
                        } else {
                            selectedSeatNumbers.add(seatCode);
                            seatBtn.setBackground(selectedColor);
                            seatBtn.setForeground(Color.WHITE);
                        }
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
