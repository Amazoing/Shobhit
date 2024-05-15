import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Userspanel extends JFrame {
    private JTable usersTable;
    private DefaultTableModel usersTableModel;

    public Userspanel() {
        setTitle("Users Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        usersTableModel = new DefaultTableModel(new String[]{"ID", "Username", "Password", "Is Manager", "Manager ID", "Manager Name"}, 0);
        usersTable = new JTable(usersTableModel);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // GridLayout with 1 row and 2 column

        JButton addButton = new JButton("Add User");
        JButton deleteButton = new JButton("Delete User");

        addButton.addActionListener(this::addUser);
        deleteButton.addActionListener(this::deleteUser);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        loadUsersData();
    }

    private void loadUsersData() {
        usersTableModel.setRowCount(0); // Clear current table content
        String url = "jdbc:sqlite:approvalapp.db";
        String query = "SELECT * FROM users";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int isManager = rs.getInt("ismanager");
                int managerID = rs.getInt("managerID");
                String managerName = getManagerName(managerID);

                usersTableModel.addRow(new Object[]{id, username, password, isManager, managerID, managerName});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getManagerName(int managerID) {
        String managerName = "";
        if (managerID != 0) {
            String url = "jdbc:sqlite:approvalapp.db";
            String query = "SELECT username FROM users WHERE user_id = ?";

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, managerID);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    managerName = rs.getString("username");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return managerName;
    }

    private void addUser(ActionEvent e) {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");
        int isManager = JOptionPane.showConfirmDialog(null, "Is this user a manager?", "Is Manager", JOptionPane.YES_NO_OPTION);
        int managerID = isManager == JOptionPane.YES_OPTION ? 0 : -1; // Default manager ID for managers is 0
        String managerName = "";

        if (isManager == JOptionPane.NO_OPTION) {
            managerName = getManagerNameFromUser();
            if (managerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Manager not found or not a manager.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            managerID = getManagerID(managerName);
            if (managerID == -1) {
                JOptionPane.showMessageDialog(this, "Manager not found or not a manager.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String url = "jdbc:sqlite:approvalapp.db";
        String query = "INSERT INTO users (username, password, ismanager, managerID) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, isManager == JOptionPane.YES_OPTION ? 1 : 0);
            preparedStatement.setInt(4, managerID);
            preparedStatement.executeUpdate();
            loadUsersData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getManagerNameFromUser() {
        String managerName = "";
        boolean isValidManager = false;
        String url = "jdbc:sqlite:approvalapp.db";
        String query = "SELECT username FROM users WHERE ismanager = 1";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                managerName = rs.getString("username");
                isValidManager = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (isValidManager) {
            managerName = JOptionPane.showInputDialog("Enter Manager Name:");
        }

        return managerName;
    }

    private int getManagerID(String managerName) {
        int managerID = -1;
        String url = "jdbc:sqlite:approvalapp.db";
        String query = "SELECT user_id FROM users WHERE username = ? AND ismanager = 1";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, managerName);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                managerID = rs.getInt("user_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return managerID;
    }

    private void deleteUser(ActionEvent e) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) usersTableModel.getValueAt(selectedRow, 0);

        String url = "jdbc:sqlite:approvalapp.db";
        String query = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            loadUsersData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Userspanel().setVisible(true));
    }
}
