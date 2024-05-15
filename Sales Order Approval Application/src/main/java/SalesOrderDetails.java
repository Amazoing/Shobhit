import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SalesOrderDetails extends JDialog {

    private int salesRepID;
    private String custName;
    private String itemName;
    private int itemQuantity;
    private int pricePerUnit;
    private JTextArea rejectionReasonsTextArea; // Added JTextArea for rejection reasons

    public SalesOrderDetails(Frame owner, int salesRepID, String custName, String itemName, int itemQuantity, int pricePerUnit) {
        super(owner, "Sales Order Details", true);
        this.salesRepID = salesRepID;
        this.custName = custName;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.pricePerUnit = pricePerUnit;

        // Adjusted size to avoid extra space
        setSize(400, 230); // Reduced height to accommodate JTextArea and buttons without extra space
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2)); // Reduced rows to accommodate JTextArea and buttons

        add(new JLabel("Sales Rep ID:"));
        add(new JLabel(String.valueOf(salesRepID)));

        add(new JLabel("Customer Name:"));
        add(new JLabel(custName));

        add(new JLabel("Item Name:"));
        add(new JLabel(itemName));

        add(new JLabel("Item Quantity:"));
        add(new JLabel(String.valueOf(itemQuantity)));

        add(new JLabel("Price Per Unit:"));
        add(new JLabel(String.valueOf(pricePerUnit)));

        JButton approveButton = new JButton("Approve");
        approveButton.addActionListener(e -> {
            try {
                updateOrderStatus("approved", itemName, null); // Pass null for rejection reasons
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(SalesOrderDetails.this, "Sales order approved.");
            dispose();
        });
        add(approveButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e -> {
            showRejectionReasonsDialog(); // Show rejection reasons dialog
        });
        add(rejectButton);

        setLocationRelativeTo(owner);
    }

    private void showRejectionReasonsDialog() {
        JDialog rejectionReasonsDialog = new JDialog(this, "Rejection Reasons", true);
        rejectionReasonsDialog.setSize(300, 150); // Reduced size to fit JTextArea and button
        rejectionReasonsDialog.setLayout(new BorderLayout());

        JTextArea rejectionReasonsInput = new JTextArea();
        rejectionReasonsDialog.add(new JScrollPane(rejectionReasonsInput), BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rejectionReasons = rejectionReasonsInput.getText();
                if (!rejectionReasons.isEmpty()) {
                    try {
                        updateOrderStatus("rejected", itemName, rejectionReasons); // Update order status with rejection reasons
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(SalesOrderDetails.this, "Sales order rejected.");
                    rejectionReasonsDialog.dispose();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(rejectionReasonsDialog, "Please enter rejection reasons.");
                }
            }
        });
        rejectionReasonsDialog.add(submitButton, BorderLayout.SOUTH);

        rejectionReasonsDialog.setLocationRelativeTo(this);
        rejectionReasonsDialog.setVisible(true);
    }

    private void updateOrderStatus(String status, String itemName, String rejectionReasons) throws SQLException {
        DataBaseManagerClass dbManager = new DataBaseManagerClass();
        dbManager.updateOrderStatus(salesRepID, custName, itemName, itemQuantity, status, rejectionReasons); // Pass rejection reasons to update method
    }
}
