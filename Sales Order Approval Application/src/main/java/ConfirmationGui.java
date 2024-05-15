import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmationGui extends JFrame implements ActionListener{

    priceRequest pr;
    private JTextArea priceField;
    private JTextArea custIDField;
    private JTextArea custNameField;
    private JTextArea itemIDField;
    private JTextArea itemNameField;
    private JTextArea itemQuantityField;
    private JButton approveButton;
    private JButton rejectButton;

    ConfirmationGui() {
        pr = new priceRequest();

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

        approveButton = new JButton("Approve");
        approveButton.setBounds(100, 220, 100, 30);
        add(approveButton);

        rejectButton = new JButton("Reject");
        rejectButton.setBounds(220, 220, 100, 30);
        add(rejectButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getSource() == approveButton) {
            pr.setApproval(true);
            // prompt to pop up another JTextField for comments
            // assume this s1 is the comment
            String s1 = new String("comment");
            pr.setComment(s1);

            // pass it to next line of decision
        }

        if(arg0.getSource() == rejectButton) {
            pr.setApproval(false);
            // prompt to pop up another JTextField for comments
            // assume this s1 is the comment
            String s1 = new String("comment");
            pr.setComment(s1);

            // log everything and delete this
        }
    }

    public static void main(String[] args) {
    }
}


