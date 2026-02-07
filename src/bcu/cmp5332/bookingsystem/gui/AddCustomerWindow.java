package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

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

        setTitle("Add a New Customer");
        setSize(400, 300);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(5, 2, 5, 5));
        
        topPanel.add(new JLabel("Name : "));
        topPanel.add(nameText);
        topPanel.add(new JLabel("Phone : "));
        topPanel.add(phoneText);
        topPanel.add(new JLabel("Gender (optional) : "));
        topPanel.add(genderText);
        topPanel.add(new JLabel("Age (optional) : "));
        topPanel.add(ageText);
        topPanel.add(new JLabel("Email (optional) : "));
        topPanel.add(emailText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
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
