package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EditFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Flight> flightCombo;
    private JTextField flightNumberText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField departureDateText = new JTextField();
    private JTextField ecoRowsText = new JTextField();
    private JTextField ecoColsText = new JTextField();
    private JTextField busRowsText = new JTextField();
    private JTextField busColsText = new JTextField();
    private JTextField fstRowsText = new JTextField();
    private JTextField fstColsText = new JTextField();

    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    public EditFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Edit Flight");
        setSize(550, 520);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Edit Flight", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        // Flight selector
        FlightBookingSystem fbs = mw.getFlightBookingSystem();
        java.util.List<Flight> flights = fbs.getFlights();
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
        flightCombo.addActionListener(e -> loadFlightData());

        gbc.gridx = 0;
        formPanel.add(new JLabel("Select Flight:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        flightCombo.setPreferredSize(new Dimension(280, 26));
        formPanel.add(flightCombo, gbc);
        gbc.gridwidth = 1;

        JLabel infoLabel = new JLabel("(Leave blank to keep current value)", SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC, 11f));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        // Basic fields
        addFormRow(formPanel, gbc, "New Flight Number:", flightNumberText);
        addFormRow(formPanel, gbc, "New Origin:", originText);
        addFormRow(formPanel, gbc, "New Destination:", destinationText);
        addFormRow(formPanel, gbc, "New Date (YYYY-MM-DD):", departureDateText);

        // Seating section header
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JLabel seatHeader = new JLabel("Seat Configuration (enter -1 to keep current):", SwingConstants.LEFT);
        seatHeader.setFont(seatHeader.getFont().deriveFont(Font.BOLD, 12f));
        formPanel.add(seatHeader, gbc);
        gbc.gridwidth = 1;

        // Seating rows
        addSeatRow(formPanel, gbc, "Economy:", ecoRowsText, ecoColsText);
        addSeatRow(formPanel, gbc, "Business:", busRowsText, busColsText);
        addSeatRow(formPanel, gbc, "First Class:", fstRowsText, fstColsText);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        updateBtn.setPreferredSize(new Dimension(110, 32));
        cancelBtn.setPreferredSize(new Dimension(110, 32));
        bottomPanel.add(updateBtn);
        bottomPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        loadFlightData();
        setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel(label, SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        field.setPreferredSize(new Dimension(200, 26));
        panel.add(field, gbc);
        gbc.gridwidth = 1;
    }

    private void addSeatRow(JPanel panel, GridBagConstraints gbc, String label, JTextField rowsField, JTextField colsField) {
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel(label, SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("Rows:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 2;
        rowsField.setPreferredSize(new Dimension(60, 26));
        panel.add(rowsField, gbc);
        gbc.gridx = 3;
        panel.add(new JLabel("Cols:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 4;
        colsField.setPreferredSize(new Dimension(60, 26));
        panel.add(colsField, gbc);
    }

    private void loadFlightData() {
        Flight f = (Flight) flightCombo.getSelectedItem();
        if (f != null) {
            flightNumberText.setToolTipText("Current: " + f.getFlightNumber());
            originText.setToolTipText("Current: " + f.getOrigin());
            destinationText.setToolTipText("Current: " + f.getDestination());
            departureDateText.setToolTipText("Current: " + f.getDepartureDate());
            ecoRowsText.setToolTipText("Current: " + f.getEconomyRows());
            ecoColsText.setToolTipText("Current: " + f.getEconomyColumns());
            busRowsText.setToolTipText("Current: " + f.getBusinessRows());
            busColsText.setToolTipText("Current: " + f.getBusinessColumns());
            fstRowsText.setToolTipText("Current: " + f.getFirstRows());
            fstColsText.setToolTipText("Current: " + f.getFirstColumns());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updateFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void updateFlight() {
        try {
            Flight flight = (Flight) flightCombo.getSelectedItem();
            if (flight == null) {
                JOptionPane.showMessageDialog(this, "Please select a flight.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String flightNumber = flightNumberText.getText().trim();
            String origin = originText.getText().trim();
            String destination = destinationText.getText().trim();
            String dateStr = departureDateText.getText().trim();

            if (!flightNumber.isEmpty()) flight.setFlightNumber(flightNumber);
            if (!origin.isEmpty()) flight.setOrigin(origin);
            if (!destination.isEmpty()) flight.setDestination(destination);
            if (!dateStr.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(dateStr);
                    flight.setDepartureDate(date);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Update seating
            updateSeatField(ecoRowsText, v -> flight.setEconomyRows(v));
            updateSeatField(ecoColsText, v -> flight.setEconomyColumns(v));
            updateSeatField(busRowsText, v -> flight.setBusinessRows(v));
            updateSeatField(busColsText, v -> flight.setBusinessColumns(v));
            updateSeatField(fstRowsText, v -> flight.setFirstRows(v));
            updateSeatField(fstColsText, v -> flight.setFirstColumns(v));

            JOptionPane.showMessageDialog(this, "Flight updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshFlightsPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSeatField(JTextField field, java.util.function.IntConsumer setter) {
        String val = field.getText().trim();
        if (!val.isEmpty()) {
            try {
                int v = Integer.parseInt(val);
                if (v >= 0) setter.accept(v);
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
