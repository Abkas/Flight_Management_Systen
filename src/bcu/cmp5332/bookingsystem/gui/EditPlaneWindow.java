package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.EditPlane;
import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Dialog window in the GUI for updating an existing aircraft and its seat map.
 * 
 * @author Abhis
 */
public class EditPlaneWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Plane> planeCombo;
    private JTextField modelText = new JTextField();
    private JTextField registrationText = new JTextField();
    private JTextField ecoRowsText = new JTextField();
    private JTextField ecoColsText = new JTextField();
    private JTextField busRowsText = new JTextField();
    private JTextField busColsText = new JTextField();
    private JTextField fstRowsText = new JTextField();
    private JTextField fstColsText = new JTextField();

    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    public EditPlaneWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Edit Aircraft");
        setSize(500, 450);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Edit Aircraft", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        FlightBookingSystem fbs = mw.getFlightBookingSystem();
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
        planeCombo.addActionListener(e -> loadPlaneData());

        gbc.gridx = 0;
        formPanel.add(new JLabel("Select Aircraft:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        planeCombo.setPreferredSize(new Dimension(250, 26));
        formPanel.add(planeCombo, gbc);

        JLabel infoLabel = new JLabel("(Leave blank to keep current value)", SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC, 11f));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        // Model and Registration
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Model:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        modelText.setPreferredSize(new Dimension(200, 26));
        formPanel.add(modelText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("New Registration:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        registrationText.setPreferredSize(new Dimension(200, 26));
        formPanel.add(registrationText, gbc);

        // Seating section
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel seatHeader = new JLabel("Seat Configuration (-1 to keep current):", SwingConstants.CENTER);
        seatHeader.setFont(seatHeader.getFont().deriveFont(Font.BOLD, 12f));
        formPanel.add(seatHeader, gbc);
        gbc.gridwidth = 1;

        // Economy
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Economy Rows:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        ecoRowsText.setPreferredSize(new Dimension(80, 26));
        formPanel.add(ecoRowsText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Economy Cols:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        ecoColsText.setPreferredSize(new Dimension(80, 26));
        formPanel.add(ecoColsText, gbc);

        // Business
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Business Rows:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        busRowsText.setPreferredSize(new Dimension(80, 26));
        formPanel.add(busRowsText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Business Cols:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        busColsText.setPreferredSize(new Dimension(80, 26));
        formPanel.add(busColsText, gbc);

        // First
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("First Rows:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        fstRowsText.setPreferredSize(new Dimension(80, 26));
        formPanel.add(fstRowsText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("First Cols:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        fstColsText.setPreferredSize(new Dimension(80, 26));
        formPanel.add(fstColsText, gbc);

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
        loadPlaneData();
        setVisible(true);
    }

    private void loadPlaneData() {
        Plane plane = (Plane) planeCombo.getSelectedItem();
        if (plane != null) {
            modelText.setText("");
            registrationText.setText("");
            ecoRowsText.setText("");
            ecoColsText.setText("");
            busRowsText.setText("");
            busColsText.setText("");
            fstRowsText.setText("");
            fstColsText.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updatePlane();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void updatePlane() {
        try {
            Plane plane = (Plane) planeCombo.getSelectedItem();
            if (plane == null) {
                JOptionPane.showMessageDialog(this, "Please select an aircraft.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String model = modelText.getText().trim();
            String registration = registrationText.getText().trim();
            
            int ecoRows = parseIntOrDefault(ecoRowsText.getText(), -1);
            int ecoCols = parseIntOrDefault(ecoColsText.getText(), -1);
            int busRows = parseIntOrDefault(busRowsText.getText(), -1);
            int busCols = parseIntOrDefault(busColsText.getText(), -1);
            int fstRows = parseIntOrDefault(fstRowsText.getText(), -1);
            int fstCols = parseIntOrDefault(fstColsText.getText(), -1);

            EditPlane command = new EditPlane(plane.getId(), model, registration,
                                             ecoRows, ecoCols, busRows, busCols, fstRows, fstCols);
            command.execute(mw.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Aircraft updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshPlanesPanel();
            mw.refreshFlightsPanel();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseIntOrDefault(String text, int defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
