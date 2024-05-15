import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyOrders extends JFrame {

    private int userID;
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private DataBaseManagerClass dbManager;

    public MyOrders(int userID) {
        dbManager = new DataBaseManagerClass();

        this.userID = userID;
        setTitle("My Orders");
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
        refreshButton.addActionListener(e -> refresh());

        JButton selectButton = new JButton("Select");
        selectButton.setFont(new Font("Arial", Font.BOLD, 18));
        // selectButton.addActionListener(e -> refreshPendingOrders());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        buttonPanel.add(selectButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        refresh();
        //tableModel.addRow(new Object[]{"Value1", "Value2"});

        setVisible(true);
        //refreshPendingOrders(); // Load initial pending orders count
    }
    private void refresh() {
        try {
            ResultSet myOrders = dbManager.retrieveMyOrders(userID);
            tableModel.setRowCount(0);

            // Iterate through the ResultSet and populate tableModel
            while (myOrders.next()) {
                String customer = myOrders.getString("cust_name");
                String item = myOrders.getString("item_name");
                int quantity = myOrders.getInt("item_quantity");

                // Add data to the table model
                tableModel.addRow(new Object[]{customer, item, quantity});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
