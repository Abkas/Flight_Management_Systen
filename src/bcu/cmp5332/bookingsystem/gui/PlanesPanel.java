package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Plane;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;

public class PlanesPanel extends JPanel {
    
    private MainWindow mw;
    private FlightBookingSystem fbs;
    
    public PlanesPanel(MainWindow mw) {
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
        
        JLabel headerLabel = new JLabel("Aircraft Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(44, 62, 80));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));

        JButton addPlaneBtn = new JButton("+ Add");
        addPlaneBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addPlaneBtn.setForeground(Color.WHITE);
        addPlaneBtn.setBackground(new Color(46, 204, 113));
        addPlaneBtn.setFocusPainted(false);
        addPlaneBtn.setBorderPainted(false);
        addPlaneBtn.setPreferredSize(new Dimension(90, 36));
        addPlaneBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addPlaneBtn.addActionListener(e -> new AddPlaneWindow(mw));

        JButton editPlaneBtn = new JButton("Edit");
        editPlaneBtn.setFont(new Font("Arial", Font.BOLD, 13));
        editPlaneBtn.setForeground(Color.WHITE);
        editPlaneBtn.setBackground(new Color(241, 196, 15));
        editPlaneBtn.setFocusPainted(false);
        editPlaneBtn.setBorderPainted(false);
        editPlaneBtn.setPreferredSize(new Dimension(90, 36));
        editPlaneBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editPlaneBtn.addActionListener(e -> new EditPlaneWindow(mw));

        JButton deletePlaneBtn = new JButton("Delete");
        deletePlaneBtn.setFont(new Font("Arial", Font.BOLD, 13));
        deletePlaneBtn.setForeground(Color.WHITE);
        deletePlaneBtn.setBackground(new Color(231, 76, 60));
        deletePlaneBtn.setFocusPainted(false);
        deletePlaneBtn.setBorderPainted(false);
        deletePlaneBtn.setPreferredSize(new Dimension(90, 36));
        deletePlaneBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deletePlaneBtn.addActionListener(e -> new DeletePlaneWindow(mw));

        buttonsPanel.add(addPlaneBtn);
        buttonsPanel.add(editPlaneBtn);
        buttonsPanel.add(deletePlaneBtn);
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        String[] columns = {"ID", "Model", "Registration", "Total Capacity"};
        java.util.List<Plane> planesList = fbs.getPlanes();
        
        Object[][] data = new Object[planesList.size()][4];
        for (int i = 0; i < planesList.size(); i++) {
            Plane plane = planesList.get(i);
            data[i][0] = plane.getId();
            data[i][1] = plane.getModel();
            data[i][2] = plane.getRegistrationNumber();
            data[i][3] = plane.getTotalCapacity();
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
