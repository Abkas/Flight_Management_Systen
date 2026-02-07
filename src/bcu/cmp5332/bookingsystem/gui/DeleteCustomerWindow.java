package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeleteCustomerWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Customer> customerCombo;
    private JButton deleteBtn = new JButton("Delete Customer");
    private JButton cancelBtn = new JButton("Cancel");

    public DeleteCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Delete Customer");
        setSize(450, 250);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Delete Customer", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(new Color(192, 57, 43));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        gbc.gridy = 0;
        gbc.gridx = 0;
        centerPanel.add(new JLabel("Select Customer:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        customerCombo.setPreferredSize(new Dimension(220, 28));
        centerPanel.add(customerCombo, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        deleteBtn.setPreferredSize(new Dimension(140, 34));
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
            deleteCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void deleteCustomer() {
        Customer customer = (Customer) customerCombo.getSelectedItem();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            mw.getFlightBookingSystem().removeCustomer(customer.getId());
            JOptionPane.showMessageDialog(this, "Customer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshCustomersPanel();
            mw.refreshBookingsPanel();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
