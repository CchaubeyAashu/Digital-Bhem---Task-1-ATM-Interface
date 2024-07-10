import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Account {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<String> transactionHistory;

    public Account(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: $" + amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
        } else {
            transactionHistory.add("Failed Withdrawal: Insufficient balance");
        }
    }

    public void transfer(Account targetAccount, double amount) {
        if (amount <= balance) {
            balance -= amount;
            targetAccount.deposit(amount);
            transactionHistory.add("Transferred: $" + amount + " to " + targetAccount.getUserId());
        } else {
            transactionHistory.add("Failed Transfer: Insufficient balance");
        }
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }
}

public class ATMInterface extends JFrame {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Account loggedInAccount = null;

    private JTextField userIdField = new JTextField(10);
    private JPasswordField pinField = new JPasswordField(10);
    private JTextArea displayArea = new JTextArea(10, 30);
    private JTextField amountField = new JTextField(10);
    private JTextField targetUserIdField = new JTextField(10);

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public ATMInterface() {
        setTitle("ATM Interface");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create accounts
        accounts.add(new Account("aashu12", "1234", 1000.0));
        accounts.add(new Account("anirudh12", "5678", 2000.0));

        // Create login panel
        JPanel loginPanel = new JPanel();
        loginPanel.add(new JLabel("User ID:"));
        loginPanel.add(userIdField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        // Create main menu panel
        JPanel mainMenuPanel = new JPanel(new GridLayout(6, 1));
        JButton balanceButton = new JButton("Balance Inquiry");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton transactionButton = new JButton("Transaction History");
        JButton logoutButton = new JButton("Logout");
        mainMenuPanel.add(balanceButton);
        mainMenuPanel.add(depositButton);
        mainMenuPanel.add(withdrawButton);
        mainMenuPanel.add(transferButton);
        mainMenuPanel.add(transactionButton);
        mainMenuPanel.add(logoutButton);

        // Create deposit panel
        JPanel depositPanel = new JPanel();
        depositPanel.add(new JLabel("Amount:"));
        depositPanel.add(amountField);
        JButton depositConfirmButton = new JButton("Confirm Deposit");
        depositPanel.add(depositConfirmButton);

        // Create withdraw panel
        JPanel withdrawPanel = new JPanel();
        withdrawPanel.add(new JLabel("Amount:"));
        withdrawPanel.add(amountField);
        JButton withdrawConfirmButton = new JButton("Confirm Withdraw");
        withdrawPanel.add(withdrawConfirmButton);

        // Create transfer panel
        JPanel transferPanel = new JPanel();
        transferPanel.add(new JLabel("Target User ID:"));
        transferPanel.add(targetUserIdField);
        transferPanel.add(new JLabel("Amount:"));
        transferPanel.add(amountField);
        JButton transferConfirmButton = new JButton("Confirm Transfer");
        transferPanel.add(transferConfirmButton);

        // Create display area
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Set up CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(loginPanel, "login");
        mainPanel.add(mainMenuPanel, "mainMenu");
        mainPanel.add(depositPanel, "deposit");
        mainPanel.add(withdrawPanel, "withdraw");
        mainPanel.add(transferPanel, "transfer");

        // Add components to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Action Listeners
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String pin = new String(pinField.getPassword());
                for (Account account : accounts) {
                    if (account.getUserId().equals(userId) && account.getPin().equals(pin)) {
                        loggedInAccount = account;
                        displayArea.setText("Login successful!\n");
                        cardLayout.show(mainPanel, "mainMenu");
                        return;
                    }
                }
                displayArea.setText("Invalid User ID or PIN. Please try again.\n");
            }
        });

        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("Your balance is: $" + loggedInAccount.getBalance() + "\n");
            }
        });

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "deposit");
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "withdraw");
            }
        });

        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "transfer");
            }
        });

        transactionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("Transaction History:\n");
                for (String transaction : loggedInAccount.getTransactionHistory()) {
                    displayArea.append(transaction + "\n");
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loggedInAccount = null;
                displayArea.setText("Logged out successfully!\n");
                cardLayout.show(mainPanel, "login");
            }
        });

        depositConfirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(amountField.getText());
                loggedInAccount.deposit(amount);
                displayArea.setText("Deposit successful!\n");
                cardLayout.show(mainPanel, "mainMenu");
            }
        });

        withdrawConfirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(amountField.getText());
                loggedInAccount.withdraw(amount);
                displayArea.setText("Withdrawal successful!\n");
                cardLayout.show(mainPanel, "mainMenu");
            }
        });

        transferConfirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String targetUserId = targetUserIdField.getText();
                double amount = Double.parseDouble(amountField.getText());
                for (Account account : accounts) {
                    if (account.getUserId().equals(targetUserId)) {
                        loggedInAccount.transfer(account, amount);
                        displayArea.setText("Transfer successful!\n");
                        cardLayout.show(mainPanel, "mainMenu");
                        return;
                    }
                }
                displayArea.setText("Target account not found.\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ATMInterface().setVisible(true);
            }
        });
    }
}
