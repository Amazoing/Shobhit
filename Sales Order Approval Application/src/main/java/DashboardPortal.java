import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardPortal extends JFrame {

    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public DashboardPortal() {
        setTitle("Sales Order Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel headerLabel = new JLabel("Sales Order Dashboard");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Pending Orders Panel
        JPanel pendingOrdersPanel = new JPanel();
        pendingOrdersPanel.setBorder(BorderFactory.createTitledBorder("Pending Orders"));
        pendingOrdersPanel.setLayout(new BorderLayout());
        pendingOrdersPanel.setBackground(Color.WHITE);
        pendingOrdersPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.GRAY)));

        // Create the table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Customer");
        tableModel.addColumn("Item");
        tableModel.addColumn("Quantity");

        ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 16));
        ordersTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        pendingOrdersPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(pendingOrdersPanel, BorderLayout.CENTER);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 18));
        refreshButton.addActionListener(e -> refreshPendingOrders());

        JButton selectButton = new JButton("Select");
        selectButton.setFont(new Font("Arial", Font.BOLD, 18));
        // selectButton.addActionListener(e -> refreshPendingOrders());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        buttonPanel.add(selectButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        refreshPendingOrders(); // Load initial pending orders count
    }

    private void refreshPendingOrders() {
        DataBaseManagerClass dbManager = new DataBaseManagerClass();
        try {
            ResultSet pendingOrders = dbManager.retrievePendingOrders();
            tableModel.setRowCount(0); // Clear existing data

            while (pendingOrders.next()) {
                String itemName = pendingOrders.getString("item_name");
                String customerName = pendingOrders.getString("cust_name");
                int itemQuantity = pendingOrders.getInt("item_quantity");

                tableModel.addRow(new Object[]{customerName, itemName, itemQuantity});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching pending orders: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardPortal::new);
    }
}

