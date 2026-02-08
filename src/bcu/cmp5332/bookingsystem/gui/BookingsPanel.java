package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;

public class BookingsPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public BookingsPanel(MainWindow mw) {
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
        
        JLabel headerLabel = new JLabel("🎫 Bookings Management");
        headerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton addBookingBtn = createButton("+ New Booking", new Color(230, 126, 34));
        JButton editBookingBtn = createButton("Edit Booking", new Color(52, 152, 219));
        JButton cancelBookingBtn = createButton("Cancel Booking", new Color(231, 76, 60));
        
        addBookingBtn.addActionListener(e -> new AddBookingWindow(mw));
        editBookingBtn.addActionListener(e -> new EditBookingWindow(mw));
        cancelBookingBtn.addActionListener(e -> new CancelBookingWindow(mw));
        
        buttonPanel.add(addBookingBtn);
        buttonPanel.add(editBookingBtn);
        buttonPanel.add(cancelBookingBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        String[] columns = {"ID", "Customer", "Flight", "Date", "Class", "Seat"};
        List<Booking> allBookings = new ArrayList<>();
        // Only show non-cancelled bookings
        for (Customer c : fbs.getCustomers()) {
            for (Booking b : c.getBookings()) {
                if (!b.isCancelled()) {
                    allBookings.add(b);
                }
            }
        }
        
        Object[][] data = new Object[allBookings.size()][6];
        for (int i = 0; i < allBookings.size(); i++) {
            Booking b = allBookings.get(i);
            data[i][0] = i + 1;
            data[i][1] = b.getCustomer().getName() + " (#" + b.getCustomer().getId() + ")";
            data[i][2] = b.getFlight().getFlightNumber() + " (#" + b.getFlight().getId() + ")";
            data[i][3] = b.getBookingDate();
            data[i][4] = b.getBookingClass();
            data[i][5] = b.getSeatNumber() != null ? b.getSeatNumber() : "N/A";
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
    
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
