package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeleteFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Flight> flightCombo;
    private JButton deleteBtn = new JButton("Delete Flight");
    private JButton cancelBtn = new JButton("Cancel");

    public DeleteFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Delete Flight");
        setSize(500, 250);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Delete Flight", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(new Color(192, 57, 43));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        flightCombo.addActionListener(e -> updateInfo());

        gbc.gridy = 0;
        gbc.gridx = 0;
        centerPanel.add(new JLabel("Select Flight:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        flightCombo.setPreferredSize(new Dimension(280, 28));
        centerPanel.add(flightCombo, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        deleteBtn.setPreferredSize(new Dimension(140, 34));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(110, 34));
        bottomPanel.add(deleteBtn);
        bottomPanel.add(cancelBtn);

        deleteBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteBtn) {
            deleteFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void deleteFlight() {
        Flight flight = (Flight) flightCombo.getSelectedItem();
        if (flight == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            mw.getFlightBookingSystem().removeFlight(flight.getId());
            JOptionPane.showMessageDialog(this, "Flight deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshFlightsPanel();
            mw.refreshBookingsPanel();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
