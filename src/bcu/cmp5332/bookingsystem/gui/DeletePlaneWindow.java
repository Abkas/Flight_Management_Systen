package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeletePlaneWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Plane> planeCombo;
    private JButton deleteBtn = new JButton("Delete Aircraft");
    private JButton cancelBtn = new JButton("Cancel");

    public DeletePlaneWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Delete Aircraft");
        setSize(500, 250);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Delete Aircraft", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(new Color(192, 57, 43));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        gbc.gridy = 0;
        gbc.gridx = 0;
        centerPanel.add(new JLabel("Select Aircraft:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        planeCombo.setPreferredSize(new Dimension(280, 28));
        centerPanel.add(planeCombo, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        deleteBtn.setPreferredSize(new Dimension(150, 34));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.BLACK);
        deleteBtn.setFont(deleteBtn.getFont().deriveFont(Font.BOLD));
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
            deletePlane();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void deletePlane() {
        Plane plane = (Plane) planeCombo.getSelectedItem();
        if (plane == null) {
            JOptionPane.showMessageDialog(this, "Please select an aircraft.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            mw.getFlightBookingSystem().removePlane(plane.getId());
            JOptionPane.showMessageDialog(this, "Aircraft deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshPlanesPanel();
            mw.refreshFlightsPanel();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
