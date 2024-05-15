import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesRepSubmitRej extends JFrame implements ActionListener{

    private String username;

    private int userID;
    priceRequest pr;
    private JTextArea priceField;
    private JTextArea custIDField;
    private JTextArea custNameField;
    private JTextArea itemIDField;
    private JTextArea itemNameField;
    private JTextArea itemQuantityField;
    private JButton submitButton;
    private JButton myOrderButton;

    private DataBaseManagerClass dbManager;
    private EmailSender email;

    SalesRepSubmitRej(String username) {
        this.username = username;
        pr = new priceRequest();
        dbManager = new DataBaseManagerClass();
        email = new EmailSender();
        userID = dbManager.retrieveUserID(username);

        setTitle("Sales Order Approval System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);


        JLabel priceLabel = new JLabel("Price per unit:");
        priceLabel.setBounds(20, 20, 100, 20);
        add(priceLabel);

        JLabel custIDLabel = new JLabel("Customer ID:");
        custIDLabel.setBounds(20, 50, 100, 20);
        add(custIDLabel);

        custIDField = new JTextArea();
        custIDField.setBounds(130, 50, 200, 20);
        add(custIDField);

        JLabel custNameLabel = new JLabel("Customer Name:");
        custNameLabel.setBounds(20, 80, 100, 20);
        add(custNameLabel);

        custNameField = new JTextArea();
        custNameField.setBounds(130, 80, 200, 20);
        add(custNameField);

        JLabel itemIDLabel = new JLabel("Item ID:");
        itemIDLabel.setBounds(20, 110, 100, 20);
        add(itemIDLabel);

        itemIDField = new JTextArea();
        itemIDField.setBounds(130, 110, 200, 20);
        add(itemIDField);

        JLabel itemNameLabel = new JLabel("Item Name:");
        itemNameLabel.setBounds(20, 140, 100, 20);
        add(itemNameLabel);

        itemNameField = new JTextArea();
        itemNameField.setBounds(130, 140, 200, 20);
        add(itemNameField);

        JLabel itemQuantityLabel = new JLabel("Item Quantity:");
        itemQuantityLabel.setBounds(20, 170, 100, 20);
        add(itemQuantityLabel);

        itemQuantityField = new JTextArea();
        itemQuantityField.setBounds(130, 170, 200, 20);
        add(itemQuantityField);

        priceField = new JTextArea();
        priceField.setBounds(130, 20, 200, 20);
        add(priceField);

        submitButton = new JButton("Send for Approval");
        submitButton.setBounds(100, 220, 100, 30);
        submitButton.addActionListener(this);
        add(submitButton);

        myOrderButton = new JButton("My orders");
        myOrderButton.setBounds(200, 220, 100, 30);
        myOrderButton.addActionListener(this);
        add(myOrderButton);

        setVisible(true);

        System.out.println(username);
        System.out.println(userID);
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getSource() == submitButton) {
            pr.setApproval(true);

            String s1 = new String("comment");
            pr.setComment(s1);

            try {
                // Create a PriceRequest object and set its fields
                priceRequest priceRequest = new priceRequest();
                priceRequest.setCustID(Integer.parseInt(custIDField.getText()));
                priceRequest.setCustName(custNameField.getText());
                priceRequest.setItemID(Integer.parseInt(itemIDField.getText()));
                priceRequest.setItemName(itemNameField.getText());
                priceRequest.setItemQ(Integer.parseInt(itemQuantityField.getText()));
                priceRequest.setPricePerUnit(Double.parseDouble(priceField.getText()));
                priceRequest.setSalesRepId(userID);
                priceRequest.setApproval(true); // Assuming always approved
                priceRequest.setComment(""); // No comment for now

                // Insert the sales order into the database
                dbManager.insertSalesOrder(priceRequest);
                email.managerNotification(dbManager.pendingOrders());

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if(arg0.getSource() == myOrderButton) {
            SwingUtilities.invokeLater(() -> {
                MyOrders myOrders = new MyOrders(userID);
                myOrders.setVisible(true);
                dispose();
            });
        }
    }

    /*public static void main(String[] args) {
        SalesRepSubmitRej cg = new SalesRepSubmitRej("test");

        // make a example class object
        // priceRequest pr1 = cg.new priceRequest();
    }*/
}

