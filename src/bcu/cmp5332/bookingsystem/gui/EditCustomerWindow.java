package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Dialog window in the GUI for updating an existing customer's details.
 * 
 * @author Abhis
 */
public class EditCustomerWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JComboBox<Customer> customerCombo;
    private JTextField nameText = new JTextField();
    private JTextField phoneText = new JTextField();
    private JTextField genderText = new JTextField();
    private JTextField ageText = new JTextField();
    private JTextField emailText = new JTextField();

    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    public EditCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Edit Customer");
        setSize(500, 380);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Edit Customer", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        // Customer selector
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
        customerCombo.addActionListener(e -> loadCustomerData());

        gbc.gridx = 0;
        formPanel.add(new JLabel("Select Customer:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        customerCombo.setPreferredSize(new Dimension(220, 26));
        formPanel.add(customerCombo, gbc);

        // Editable fields
        JLabel infoLabel = new JLabel("(Leave blank to keep current value)", SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC, 11f));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        String[] labels = {"New Name:", "New Phone:", "New Gender:", "New Age:", "New Email:"};
        JTextField[] fields = {nameText, phoneText, genderText, ageText, emailText};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            formPanel.add(new JLabel(labels[i], SwingConstants.RIGHT), gbc);
            gbc.gridx = 1;
            fields[i].setPreferredSize(new Dimension(220, 26));
            formPanel.add(fields[i], gbc);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        updateBtn.setPreferredSize(new Dimension(110, 32));
        cancelBtn.setPreferredSize(new Dimension(110, 32));
        bottomPanel.add(updateBtn);
        bottomPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    private void loadCustomerData() {
        Customer c = (Customer) customerCombo.getSelectedItem();
        if (c != null) {
            nameText.setToolTipText("Current: " + c.getName());
            phoneText.setToolTipText("Current: " + c.getPhone());
            genderText.setToolTipText("Current: " + (c.getGender() != null ? c.getGender() : "N/A"));
            ageText.setToolTipText("Current: " + (c.getAge() > 0 ? c.getAge() : "N/A"));
            emailText.setToolTipText("Current: " + (c.getEmail() != null ? c.getEmail() : "N/A"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updateCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void updateCustomer() {
        try {
            Customer customer = (Customer) customerCombo.getSelectedItem();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String name = nameText.getText().trim();
            String phone = phoneText.getText().trim();
            String gender = genderText.getText().trim();
            String ageStr = ageText.getText().trim();
            String email = emailText.getText().trim();

            if (!name.isEmpty()) customer.setName(name);
            if (!phone.isEmpty()) customer.setPhone(phone);
            if (!gender.isEmpty()) customer.setGender(gender);
            if (!ageStr.isEmpty()) {
                try {
                    int age = Integer.parseInt(ageStr);
                    if (age > 0) customer.setAge(age);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Age must be a number.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            if (!email.isEmpty()) customer.setEmail(email);

            JOptionPane.showMessageDialog(this, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshCustomersPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
