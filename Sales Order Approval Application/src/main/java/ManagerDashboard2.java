import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDashboard2 extends JFrame {

    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private int userID;

    public ManagerDashboard2(int userID) {
        this.userID = userID;
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
        tableModel.addColumn("Customer Name");
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Item Quantity");
        tableModel.addColumn("Requested price");
        tableModel.addColumn("Reference price");
        tableModel.addColumn("Status");

        ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 16));
        ordersTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        pendingOrdersPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(pendingOrdersPanel, BorderLayout.CENTER);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 18));
        refreshButton.addActionListener(e -> refreshPendingOrders(userID)); // Pass managerID to refresh method

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.addActionListener(e -> dispose());

        // Select Button
        JButton selectButton = new JButton("Select");
        selectButton.setFont(new Font("Arial", Font.BOLD, 18));
        selectButton.addActionListener(e -> selectPendingOrders());

        JButton closedButton = new JButton("Closed");
        closedButton.setFont(new Font("Arial", Font.BOLD, 18));
        closedButton.addActionListener(e -> closedOrders(userID));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(backButton);
        buttonPanel.add(closedButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        refreshPendingOrders(userID); // Load initial pending orders count for the manager
    }

    private void refreshPendingOrders(int userID) {
        DataBaseManagerClass dbManager = new DataBaseManagerClass();
        try {
            ResultSet subordinatesPendingOrders = dbManager.retrieveMyPendingOrders(userID);
            tableModel.setRowCount(0); // Clear existing data

            while (subordinatesPendingOrders.next()) {
                int salesRep = subordinatesPendingOrders.getInt("sales_rep_id");
                String cust_name = subordinatesPendingOrders.getString("cust_name");
                String item_name = subordinatesPendingOrders.getString("item_name");
                int item_quantity = subordinatesPendingOrders.getInt("item_quantity");
                int price_per_unit = subordinatesPendingOrders.getInt("price_per_unit");
                int reference_price = subordinatesPendingOrders.getInt("price");
                String status = subordinatesPendingOrders.getString("status");

                tableModel.addRow(new Object[]{salesRep, cust_name, item_name, item_quantity, price_per_unit, reference_price, status});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching subordinates' pending orders: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void closedOrders(int userID) {
        DataBaseManagerClass dbManager = new DataBaseManagerClass();
        try {
            ResultSet subordinatesPendingOrders = dbManager.retrieveMyClosedOrders(userID);
            tableModel.setRowCount(0); // Clear existing data

            while (subordinatesPendingOrders.next()) {
                int salesRep = subordinatesPendingOrders.getInt("sales_rep_id");
                String cust_name = subordinatesPendingOrders.getString("cust_name");
                String item_name = subordinatesPendingOrders.getString("item_name");
                int item_quantity = subordinatesPendingOrders.getInt("item_quantity");
                int price_per_unit = subordinatesPendingOrders.getInt("price_per_unit");
                int reference_price = subordinatesPendingOrders.getInt("price");
                String status = subordinatesPendingOrders.getString("status");

                tableModel.addRow(new Object[]{salesRep, cust_name, item_name, item_quantity, price_per_unit, reference_price, status});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching subordinates' pending orders: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void selectPendingOrders() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            int salesRepID = (int) ordersTable.getValueAt(selectedRow, 0);
            String custName = (String) ordersTable.getValueAt(selectedRow, 1);
            String itemName = (String) ordersTable.getValueAt(selectedRow, 2);
            int itemQuantity = (int) ordersTable.getValueAt(selectedRow, 3);
            int pricePerUnit = (int) ordersTable.getValueAt(selectedRow, 4);

            SalesOrderDetails dialog = new SalesOrderDetails(this, salesRepID, custName, itemName, itemQuantity, pricePerUnit);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
        }
    }


    public static void main(String[] args) {
        // For testing purposes, assume managerID is obtained during login/authentication
        int managerID = 6; // Replace 123 with actual manager ID
        SwingUtilities.invokeLater(() -> new ManagerDashboard2(managerID));
    }
}

    /*public static void main(String[] args) {
        // For testing purposes, assume managerID is obtained during login/authentication
        int managerID = 6; // Replace 123 with actual manager ID
        SwingUtilities.invokeLater(() -> new ManagerDashboard(managerID));
    }*/

