import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UserDashboard extends JFrame {

    private Map<String, DefaultTableModel> userTables;

    public UserDashboard() {
        setTitle("User Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel headerLabel = new JLabel("User Dashboard");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Initialize user tables map
        userTables = new HashMap<>();

        setVisible(true);
    }

    // Method to add or update a user's pending orders
    public void addUserOrders(String username, Object[][] orders) {
        DefaultTableModel tableModel = userTables.get(username);
        if (tableModel == null) {
            tableModel = new DefaultTableModel();
            tableModel.addColumn("OrderID");
            tableModel.addColumn("Product");
            tableModel.addColumn("Amount");
            tableModel.addColumn("Price");
            userTables.put(username, tableModel);

            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.GRAY)));
            userPanel.setBackground(Color.WHITE);

            JLabel userLabel = new JLabel(username + " Pending Orders");
            userLabel.setFont(new Font("Arial", Font.BOLD, 18));
            userPanel.add(userLabel, BorderLayout.NORTH);

            JTable ordersTable = new JTable(tableModel);
            ordersTable.setFont(new Font("Arial", Font.PLAIN, 16));
            ordersTable.getTableHeader().setReorderingAllowed(false);
            JScrollPane scrollPane = new JScrollPane(ordersTable);
            userPanel.add(scrollPane, BorderLayout.CENTER);

            add(userPanel, BorderLayout.CENTER);
        }

        for (Object[] order : orders) {
            tableModel.addRow(order);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserDashboard dashboard = new UserDashboard();
            // Example: Adding orders for two users
            Object[][] ordersUser1 = {
                    {1, "Product A", 5, 10.0},
                    {2, "Product B", 3, 15.0},
                    {3, "Product C", 2, 20.0}
            };
            dashboard.addUserOrders("JohnDoe", ordersUser1);

            Object[][] ordersUser2 = {
                    {4, "Product D", 4, 12.0},
                    {5, "Product E", 2, 18.0}
            };
            dashboard.addUserOrders("Saratha", ordersUser2);
        });
    }
}
