package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.EditFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Dialog window in the GUI for updating an existing flight and its prices.
 * 
 * @author Abhis
 */
public class EditFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Flight> flightCombo;
    private JTextField flightNumberText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField departureDateText = new JTextField();
    private JTextField economyPriceText = new JTextField();
    private JTextField businessPriceText = new JTextField();
    private JTextField firstClassPriceText = new JTextField();
    private JComboBox<Plane> planeCombo;

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
        setSize(550, 500);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Edit Flight", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
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
        flightCombo.setPreferredSize(new Dimension(300, 28));
        formPanel.add(flightCombo, gbc);

        JLabel infoLabel = new JLabel("(Leave blank to keep current value)", SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC, 11f));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        // Basic fields
        addFormRow(formPanel, gbc, "New Flight Number:", flightNumberText);
        addFormRow(formPanel, gbc, "New Origin:", originText);
        addFormRow(formPanel, gbc, "New Destination:", destinationText);
        addFormRow(formPanel, gbc, "New Date (YYYY-MM-DD):", departureDateText);
        addFormRow(formPanel, gbc, "New Economy Price (NPR):", economyPriceText);
        addFormRow(formPanel, gbc, "New Business Price (NPR):", businessPriceText);
        addFormRow(formPanel, gbc, "New First Class Price (NPR):", firstClassPriceText);

        // Plane selection
        java.util.List<Plane> planes = fbs.getPlanes();
        planeCombo = new JComboBox<>(planes.toArray(new Plane[0]));
        planeCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Plane) {
                    Plane p = (Plane) value;
                    value = p.getId() + " - " + p.getModel() + " (" + p.getRegistrationNumber() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        planeCombo.setPreferredSize(new Dimension(300, 28));
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Aircraft:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        formPanel.add(planeCombo, gbc);

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
        field.setPreferredSize(new Dimension(300, 28));
        panel.add(field, gbc);
    }

    private void loadFlightData() {
        Flight f = (Flight) flightCombo.getSelectedItem();
        if (f != null) {
            flightNumberText.setToolTipText("Current: " + f.getFlightNumber());
            originText.setToolTipText("Current: " + f.getOrigin());
            destinationText.setToolTipText("Current: " + f.getDestination());
            departureDateText.setToolTipText("Current: " + f.getDepartureDate());
            economyPriceText.setToolTipText("Current: NPR " + String.format("%.2f", f.getEconomyPrice()));
            businessPriceText.setToolTipText("Current: NPR " + String.format("%.2f", f.getBusinessPrice()));
            firstClassPriceText.setToolTipText("Current: NPR " + String.format("%.2f", f.getFirstClassPrice()));
            
            // Set current plane as selected
            if (f.getPlane() != null) {
                planeCombo.setSelectedItem(f.getPlane());
                planeCombo.setToolTipText("Current: " + f.getPlane().getModel() + " (" + f.getPlane().getRegistrationNumber() + ")");
            }
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
            
            LocalDate departureDate = null;
            if (!dateStr.isEmpty()) {
                try {
                    departureDate = LocalDate.parse(dateStr);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Parse prices (-1 means no change)
            double economyPrice = -1.0;
            double businessPrice = -1.0;
            double firstClassPrice = -1.0;
            
            String ecoStr = economyPriceText.getText().trim();
            if (!ecoStr.isEmpty()) {
                try {
                    economyPrice = Double.parseDouble(ecoStr);
                    if (economyPrice < 0) {
                        JOptionPane.showMessageDialog(this, "Economy price must be a positive number.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid economy price.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            String busStr = businessPriceText.getText().trim();
            if (!busStr.isEmpty()) {
                try {
                    businessPrice = Double.parseDouble(busStr);
                    if (businessPrice < 0) {
                        JOptionPane.showMessageDialog(this, "Business price must be a positive number.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid business price.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            String fstStr = firstClassPriceText.getText().trim();
            if (!fstStr.isEmpty()) {
                try {
                    firstClassPrice = Double.parseDouble(fstStr);
                    if (firstClassPrice < 0) {
                        JOptionPane.showMessageDialog(this, "First class price must be a positive number.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid first class price.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Get selected plane (0 means no change in EditFlight command)
            Plane selectedPlane = (Plane) planeCombo.getSelectedItem();
            int planeId = (selectedPlane != null) ? selectedPlane.getId() : 0;

            EditFlight command = new EditFlight(flight.getId(), flightNumber, origin, destination, departureDate, planeId, economyPrice, businessPrice, firstClassPrice);
            command.execute(mw.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Flight updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshFlightsPanel();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
