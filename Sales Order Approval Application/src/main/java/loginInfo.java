import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginInfo extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private DataBaseManagerClass dbManager;

    public loginInfo() {
        dbManager = new DataBaseManagerClass();

        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        add(usernameLabel);

        usernameField = new JTextField();
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");

        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButtonClicked();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButtonClicked();
            }
        });

        add(loginButton);
        add(registerButton);

        setVisible(true);
    }

    private void loginButtonClicked() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        boolean authenticated = dbManager.authenticateUser(username, password);
        boolean manager = dbManager.authenticateManager(username, password);

        if (authenticated) {
            if (manager) {
                // Retrieve the manager's ID from the database or any other source
                int managerID = dbManager.retrieveUserID(username);
                ManagerDashboard(managerID);
            } else {
                openSalesOrderForm(username);
            }
        }
    }

    private void registerButtonClicked() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert user into the database
        dbManager.insertUser(username, password);
        JOptionPane.showMessageDialog(this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSalesOrderForm(String username) {
        SwingUtilities.invokeLater(() -> {
            SalesRepSubmitRej salesOrderApprovalGUI = new SalesRepSubmitRej(username);
            salesOrderApprovalGUI.setVisible(true);
            dispose();
        });
    }

    /*private void openManagerGUI() {
        SwingUtilities.invokeLater(() -> {
            ConfirmationGui confirmationGui = new ConfirmationGui();
            confirmationGui.setVisible(true);
            dispose();
        });
    }*/
    private void ManagerDashboard(int managerID){
        SwingUtilities.invokeLater(() -> {
            ManagerDashboard managerDashboard = new ManagerDashboard(managerID);
            managerDashboard.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(loginInfo::new);
    }
}
