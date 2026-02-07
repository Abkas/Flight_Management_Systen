package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
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
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));

        JButton addCustomerBtn = new JButton("+ Add");
        addCustomerBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addCustomerBtn.setForeground(Color.WHITE);
        addCustomerBtn.setBackground(new Color(52, 152, 219));
        addCustomerBtn.setFocusPainted(false);
        addCustomerBtn.setBorderPainted(false);
        addCustomerBtn.setPreferredSize(new Dimension(90, 36));
        addCustomerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCustomerBtn.addActionListener(e -> new AddCustomerWindow(mw));

        JButton editCustomerBtn = new JButton("Edit");
        editCustomerBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editCustomerBtn.setForeground(Color.WHITE);
        editCustomerBtn.setBackground(new Color(241, 196, 15));
        editCustomerBtn.setFocusPainted(false);
        editCustomerBtn.setBorderPainted(false);
        editCustomerBtn.setPreferredSize(new Dimension(90, 36));
        editCustomerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editCustomerBtn.addActionListener(e -> new EditCustomerWindow(mw));

        JButton deleteCustomerBtn = new JButton("Delete");
        deleteCustomerBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deleteCustomerBtn.setForeground(Color.WHITE);
        deleteCustomerBtn.setBackground(new Color(231, 76, 60));
        deleteCustomerBtn.setFocusPainted(false);
        deleteCustomerBtn.setBorderPainted(false);
        deleteCustomerBtn.setPreferredSize(new Dimension(90, 36));
        deleteCustomerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteCustomerBtn.addActionListener(e -> new DeleteCustomerWindow(mw));

        buttonsPanel.add(addCustomerBtn);
        buttonsPanel.add(editCustomerBtn);
        buttonsPanel.add(deleteCustomerBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
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
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setGridColor(new Color(210, 210, 210));
        table.setShowVerticalLines(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(235, 235, 235));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 30));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(header);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
