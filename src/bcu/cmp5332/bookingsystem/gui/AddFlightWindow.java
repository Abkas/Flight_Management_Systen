package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Plane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class AddFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();
    private JComboBox<Plane> planeCombo;

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

        setTitle("Add New Flight");
        setSize(500, 280);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Add New Flight", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel flightNoLabel = new JLabel("Flight No:", SwingConstants.RIGHT);
        JLabel originLabel = new JLabel("Origin:", SwingConstants.RIGHT);
        JLabel destinationLabel = new JLabel("Destination:", SwingConstants.RIGHT);
        JLabel dateLabel = new JLabel("Departure Date (YYYY-MM-DD):", SwingConstants.RIGHT);
        JLabel planeLabel = new JLabel("Aircraft:", SwingConstants.RIGHT);

        flightNoText.setPreferredSize(new Dimension(220, 26));
        originText.setPreferredSize(new Dimension(220, 26));
        destinationText.setPreferredSize(new Dimension(220, 26));
        depDateText.setPreferredSize(new Dimension(220, 26));

        gbc.gridx = 0;
        formPanel.add(flightNoLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(flightNoText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(originLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(originText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(destinationLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(destinationText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(depDateText, gbc);

        // Plane selection
        java.util.List<Plane> planes = mw.getFlightBookingSystem().getPlanes();
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
        planeCombo.setPreferredSize(new Dimension(220, 26));

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(planeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(planeCombo, gbc);

        JPanel bottomPanel = new JPanel();
        addBtn.setPreferredSize(new Dimension(110, 32));
        cancelBtn.setPreferredSize(new Dimension(110, 32));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
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
            
            Plane plane = (Plane) planeCombo.getSelectedItem();
            if (plane == null) {
                throw new FlightBookingSystemException("Please select an aircraft");
            }
            
            Command addFlight = new AddFlight(flightNumber, origin, destination, departureDate, plane.getId());
            addFlight.execute(mw.getFlightBookingSystem());
            
            JOptionPane.showMessageDialog(this, "Flight added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshFlightsPanel();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for rows and columns", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
