package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CustomersPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public CustomersPanel(MainWindow mw) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }
    
    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        
        JLabel headerLabel = new JLabel("Customers Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JButton addCustomerBtn = new JButton("+ Add New Customer");
        addCustomerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addCustomerBtn.setForeground(Color.WHITE);
        addCustomerBtn.setBackground(new Color(52, 152, 219));
        addCustomerBtn.setFocusPainted(false);
        addCustomerBtn.setBorderPainted(false);
        addCustomerBtn.setPreferredSize(new Dimension(200, 40));
        addCustomerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCustomerBtn.addActionListener(e -> new AddCustomerWindow(mw));
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(addCustomerBtn, BorderLayout.EAST);
        
        String[] columns = {"ID", "Name", "Phone", "Email", "Gender", "Age", "Bookings"};
        java.util.List<Customer> customersList = fbs.getCustomers();
        
        Object[][] data = new Object[customersList.size()][7];
        for (int i = 0; i < customersList.size(); i++) {
            Customer customer = customersList.get(i);
            data[i][0] = customer.getId();
            data[i][1] = customer.getName();
            data[i][2] = customer.getPhone();
            data[i][3] = customer.getEmail() != null && !customer.getEmail().isEmpty() ? customer.getEmail() : "N/A";
            data[i][4] = customer.getGender() != null && !customer.getGender().isEmpty() ? customer.getGender() : "N/A";
            data[i][5] = customer.getAge() > 0 ? customer.getAge() : "N/A";
            data[i][6] = customer.getBookings().size();
        }
        
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
