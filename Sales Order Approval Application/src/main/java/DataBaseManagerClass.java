import java.io.File;
import java.sql.*;

public class DataBaseManagerClass {
    private Connection connection;

    public DataBaseManagerClass() {
        try {
            File temp = new File("approvalapp.db");
            String URL = "jdbc:sqlite:" + temp.getAbsolutePath().replace("\\","\\\\");
            // Establish database connection
            connection = DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle connection failure
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void executeQuery(String sqlQuery) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle query execution failure
        }
    }

    public void insertSalesOrder(priceRequest request) {
        String sqlQuery = "INSERT INTO sales_orders (price_per_unit, cust_id, cust_name, item_id, item_name, item_quantity, sales_rep_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setDouble(1, request.getPricePerUnit());
            statement.setInt(2, request.getCustID());
            statement.setString(3, request.getCustName());
            statement.setInt(4, request.getItemID());
            statement.setString(5, request.getItemName());
            statement.setInt(6, request.getItemQ());
            statement.setInt(7, request.getSalesRepId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle query execution failure
        }
    }

    public boolean authenticateUser(String username, String password) {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean authenticateManager(String username, String password) {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int i = result.getInt("isManager");
                if(i == 0){
                    return false;
                }
                else return true;
            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void insertUser(String username, String password) {

//            int nextUserId = 1;
//            String idQuery = "SELECT MAX(id) FROM users";
//            try (PreparedStatement idStatement = connection.prepareStatement(idQuery);){
//            ResultSet idResultSet = idStatement.executeQuery();
//            if (idResultSet.next()) {
//                nextUserId = idResultSet.getInt(1) + 1;
//            }
        
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the statement to insert the user into the database
            statement.executeUpdate();


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet retrievePendingOrders() {
        String query = "SELECT cust_name, item_name, item_quantity FROM sales_orders WHERE status = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "pending");
            return statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int pendingOrders() {
        String countQuery = "SELECT COUNT(*) FROM sales_orders";
        int rowCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(countQuery)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle query execution failure
        }
        return rowCount;
    }

    public int retrieveUserID(String username) {
        String idQuery = "SELECT user_id FROM users WHERE username = ?";

        int userID = 0;

        try (PreparedStatement statement = connection.prepareStatement(idQuery)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                userID = result.getInt("user_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userID;
    }
    public ResultSet retrievePendingOrdersForManager(int managerID) throws SQLException{
        String query = "SELECT u.user_id AS sales_rep_id, u.username AS sales_rep_username, COUNT(*) AS pending_orders " +
                "FROM sales_orders s " +
                "INNER JOIN users u ON s.sales_rep_id = u.user_id " +
                "WHERE s.status = ? AND u.managerID = ? " +
                "GROUP BY u.user_id, u.username";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, "pending");
        statement.setInt(2, managerID);
        return statement.executeQuery();
    }

    public ResultSet retrieveMyOrders(int userID) throws SQLException {
        String query = "SELECT sales_rep_id, cust_name, item_name, item_quantity, price_per_unit FROM sales_orders WHERE sales_rep_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        //statement.setString(1, "approved");
        return statement.executeQuery();
    }

    public ResultSet retrieveMyPendingOrders(int userID) throws SQLException {
        String query = "SELECT so.sales_rep_id, so.cust_name, so.item_name, so.item_quantity, so.price_per_unit, i.price, so.status " +
                "FROM sales_orders so " +
                "LEFT JOIN items i ON so.item_name = i.item_name " +
                "WHERE so.sales_rep_id = ? AND so.status = 'pending'";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        //statement.setString(1, "approved");
        return statement.executeQuery();
    }
    public ResultSet retrieveMyClosedOrders(int userID) throws SQLException {
        String query = "SELECT so.sales_rep_id, so.cust_name, so.item_name, so.item_quantity, so.price_per_unit, i.price, so.status " +
                "FROM sales_orders so " +
                "LEFT JOIN items i ON so.item_name = i.item_name " +
                "WHERE so.sales_rep_id = ? AND so.status != 'pending'";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        //statement.setString(1, "approved");
        return statement.executeQuery();
    }
    public void updateOrderStatus(int salesRepID, String custName, String itemName, int itemQuantity, String status, String rejectionReasons) {
        String sql = "UPDATE sales_orders SET status = ?, rejection_reasons = ? WHERE sales_rep_id = ? AND cust_name = ? AND item_name = ? AND item_quantity = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setString(2, rejectionReasons); // Set rejection reasons parameter
            statement.setInt(3, salesRepID);
            statement.setString(4, custName);
            statement.setString(5, itemName);
            statement.setInt(6, itemQuantity);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

}
