import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDashboard extends JFrame {

    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private int managerID; // Add a field to store the manager's ID

    public ManagerDashboard(int managerID) {
        this.managerID = managerID; // Initialize the manager's ID
        setTitle("Manager Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel headerLabel = new JLabel("Manager Dashboard");
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

        // table model with two columns: Sales Rep and Pending Orders
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Sales Rep");
        tableModel.addColumn("Pending Orders");

        ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 16));
        ordersTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        pendingOrdersPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(pendingOrdersPanel, BorderLayout.CENTER);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 18));
        refreshButton.addActionListener(e -> refreshPendingOrders(managerID)); // Pass managerID to refresh method

        JButton selectButton = new JButton("Select");
        selectButton.setFont(new Font("Arial", Font.BOLD, 18));
        selectButton.addActionListener(e -> selectPendingOrders());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        buttonPanel.add(selectButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        refreshPendingOrders(managerID); // Load initial pending orders count for the manager
    }

    private void refreshPendingOrders(int managerID) {
        DataBaseManagerClass dbManager = new DataBaseManagerClass();
        try {
            ResultSet subordinatesPendingOrders = dbManager.retrievePendingOrdersForManager(managerID); // Retrieve subordinates' pending orders
            tableModel.setRowCount(0); // Clear existing data

            while (subordinatesPendingOrders.next()) {
                String salesRep = subordinatesPendingOrders.getString("sales_rep_username"); // Retrieve sales rep username
                int pendingOrdersCount = subordinatesPendingOrders.getInt("pending_orders"); // pending_orders

                tableModel.addRow(new Object[]{salesRep, pendingOrdersCount});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching subordinates' pending orders: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void selectPendingOrders() {
        DataBaseManagerClass dbManager = new DataBaseManagerClass();
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String salesRepName = (String) ordersTable.getValueAt(selectedRow, 0);
            int userID = dbManager.retrieveUserID(salesRepName);

            SwingUtilities.invokeLater(() -> {
                ManagerDashboard2 managerDashboard2 = new ManagerDashboard2(userID);
                managerDashboard2.setVisible(true);
                dispose();
            });
        }
        else {
            // error
            JOptionPane.showMessageDialog(this, "Please select a row.",
                    "No Row Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // For testing purposes, assume managerID is obtained during login/authentication
        int managerID = 6; // Replace 123 with actual manager ID
        SwingUtilities.invokeLater(() -> new ManagerDashboard(managerID));
    }
}
