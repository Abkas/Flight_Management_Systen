package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddPlane;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class AddPlaneWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField modelText = new JTextField();
    private JTextField registrationText = new JTextField();

    private JTextField firstRowsText = new JTextField();
    private JTextField firstColsText = new JTextField();
    private JTextField businessRowsText = new JTextField();
    private JTextField businessColsText = new JTextField();
    private JTextField economyRowsText = new JTextField();
    private JTextField economyColsText = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    public AddPlaneWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Add New Aircraft");
        setSize(500, 380);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Add New Aircraft", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel modelLabel = new JLabel("Model:", SwingConstants.RIGHT);
        JLabel registrationLabel = new JLabel("Registration Number:", SwingConstants.RIGHT);

        modelText.setPreferredSize(new Dimension(220, 26));
        registrationText.setPreferredSize(new Dimension(220, 26));

        gbc.gridx = 0;
        formPanel.add(modelLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(modelText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(registrationLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(registrationText, gbc);

        // Seat configuration section
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel seatConfigLabel = new JLabel("Seat Configuration", SwingConstants.CENTER);
        seatConfigLabel.setFont(seatConfigLabel.getFont().deriveFont(16f));
        formPanel.add(seatConfigLabel, gbc);

        gbc.gridwidth = 1;

        firstRowsText.setPreferredSize(new Dimension(60, 26));
        firstColsText.setPreferredSize(new Dimension(60, 26));
        businessRowsText.setPreferredSize(new Dimension(60, 26));
        businessColsText.setPreferredSize(new Dimension(60, 26));
        economyRowsText.setPreferredSize(new Dimension(60, 26));
        economyColsText.setPreferredSize(new Dimension(60, 26));

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("FIRST: rows, columns", SwingConstants.RIGHT), gbc);
        JPanel firstPanel = new JPanel();
        firstPanel.add(new JLabel("Rows:"));
        firstPanel.add(firstRowsText);
        firstPanel.add(new JLabel("Cols:"));
        firstPanel.add(firstColsText);
        gbc.gridx = 1;
        formPanel.add(firstPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("BUSINESS: rows, columns", SwingConstants.RIGHT), gbc);
        JPanel businessPanel = new JPanel();
        businessPanel.add(new JLabel("Rows:"));
        businessPanel.add(businessRowsText);
        businessPanel.add(new JLabel("Cols:"));
        businessPanel.add(businessColsText);
        gbc.gridx = 1;
        formPanel.add(businessPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("ECONOMY: rows, columns", SwingConstants.RIGHT), gbc);
        JPanel economyPanel = new JPanel();
        economyPanel.add(new JLabel("Rows:"));
        economyPanel.add(economyRowsText);
        economyPanel.add(new JLabel("Cols:"));
        economyPanel.add(economyColsText);
        gbc.gridx = 1;
        formPanel.add(economyPanel, gbc);

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
            addPlane();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addPlane() {
        try {
            String model = modelText.getText();
            String registration = registrationText.getText();
            
            if (model.trim().isEmpty() || registration.trim().isEmpty()) {
                throw new FlightBookingSystemException("Model and Registration Number are required.");
            }
            
            int firstRows = Integer.parseInt(firstRowsText.getText());
            int firstCols = Integer.parseInt(firstColsText.getText());
            int busRows = Integer.parseInt(businessRowsText.getText());
            int busCols = Integer.parseInt(businessColsText.getText());
            int ecoRows = Integer.parseInt(economyRowsText.getText());
            int ecoCols = Integer.parseInt(economyColsText.getText());

            if (firstRows < 0 || firstCols < 0 || busRows < 0 || busCols < 0 || ecoRows < 0 || ecoCols < 0) {
                throw new FlightBookingSystemException("Row and column counts cannot be negative.");
            }

            if (firstRows * firstCols == 0 && busRows * busCols == 0 && ecoRows * ecoCols == 0) {
                throw new FlightBookingSystemException("At least one class must have seats.");
            }
            
            Command addPlane = new AddPlane(model, registration, ecoRows, ecoCols, busRows, busCols, firstRows, firstCols);
            addPlane.execute(mw.getFlightBookingSystem());
            
            JOptionPane.showMessageDialog(this, "Aircraft added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            mw.refreshPlanesPanel();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for rows and columns", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
