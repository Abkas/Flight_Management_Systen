package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Dialog window in the GUI for creating a new customer.
 * 
 * @author Abhis
 */
public class AddCustomerWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField nameText = new JTextField();
    private JTextField phoneText = new JTextField();
    private JTextField genderText = new JTextField();
    private JTextField ageText = new JTextField();
    private JTextField emailText = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    public AddCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Add New Customer");
        setSize(450, 320);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Add New Customer", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JLabel phoneLabel = new JLabel("Phone:", SwingConstants.RIGHT);
        JLabel genderLabel = new JLabel("Gender (optional):", SwingConstants.RIGHT);
        JLabel ageLabel = new JLabel("Age (optional):", SwingConstants.RIGHT);
        JLabel emailLabel = new JLabel("Email (optional):", SwingConstants.RIGHT);

        nameText.setPreferredSize(new Dimension(220, 26));
        phoneText.setPreferredSize(new Dimension(220, 26));
        genderText.setPreferredSize(new Dimension(220, 26));
        ageText.setPreferredSize(new Dimension(220, 26));
        emailText.setPreferredSize(new Dimension(220, 26));

        gbc.gridx = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(phoneText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(genderText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(ageText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailText, gbc);

        JPanel bottomPanel = new JPanel();
        addBtn.setPreferredSize(new Dimension(110, 32));
        cancelBtn.setPreferredSize(new Dimension(110, 32));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addCustomer() {
        try {
            String name = nameText.getText();
            String phone = phoneText.getText();
            String gender = genderText.getText().trim();
            String email = emailText.getText().trim();
            
            int age = 0;
            if (!ageText.getText().trim().isEmpty()) {
                age = Integer.parseInt(ageText.getText().trim());
            }

            Command addCustomer = new AddCustomer(name, phone, gender, age, email);
            addCustomer.execute(mw.getFlightBookingSystem());
            
            JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshCustomersPanel();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for age", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
