import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class BankGUI extends JFrame {
    private Bank bank;
    private JTextField txtName, txtBalance, txtExtra, txtID, txtAmount;
    private JComboBox<String> cmbType;
    private JTextArea txtOutput;
    private JLabel lblExtra;

    public BankGUI() {
        bank = new Bank();

        setTitle("Group 5 - Bank Account Manager GUI");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(380, 500));

        JPanel pnlCreate = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlCreate.setBorder(BorderFactory.createTitledBorder("Open New Account"));

        pnlCreate.add(new JLabel("Holder Name:"));
        txtName = new JTextField();
        pnlCreate.add(txtName);

        pnlCreate.add(new JLabel("Initial Balance:"));
        txtBalance = new JTextField();
        pnlCreate.add(txtBalance);

        pnlCreate.add(new JLabel("Account Type:"));
        cmbType = new JComboBox<>(new String[]{"Savings", "Checking", "Credit"});
        pnlCreate.add(cmbType);

        lblExtra = new JLabel("Interest Rate (e.g., 0.05):");
        pnlCreate.add(lblExtra);
        txtExtra = new JTextField();
        pnlCreate.add(txtExtra);

        JButton btnCreate = new JButton("Create Account");
        pnlCreate.add(btnCreate);

        cmbType.addActionListener(e -> {
            String selected = (String) cmbType.getSelectedItem();
            if ("Savings".equals(selected)) {
                lblExtra.setText("Interest Rate (e.g., 0.05):");
            } else if ("Checking".equals(selected)) {
                lblExtra.setText("Withdrawal Fee:");
            } else if ("Credit".equals(selected)) {
                lblExtra.setText("Credit Limit (Negative):");
            }
        });

        JPanel pnlAction = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlAction.setBorder(BorderFactory.createTitledBorder("Account Operations"));

        pnlAction.add(new JLabel("Account ID:"));
        txtID = new JTextField();
        pnlAction.add(txtID);

        pnlAction.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        pnlAction.add(txtAmount);

        JButton btnDeposit = new JButton("Deposit");
        JButton btnWithdraw = new JButton("Withdraw");
        pnlAction.add(btnDeposit);
        pnlAction.add(btnWithdraw);

        JButton btnFind = new JButton("Find by ID");
        pnlAction.add(btnFind);

        JPanel pnlGeneral = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlGeneral.setBorder(BorderFactory.createTitledBorder("General Status"));
        JButton btnList = new JButton("List All Accounts");
        JButton btnSummary = new JButton("Bank Summary");
        pnlGeneral.add(btnList);
        pnlGeneral.add(btnSummary);

        controlPanel.add(pnlCreate);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(pnlAction);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(pnlGeneral);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Transaction Log / Console Output"));
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtOutput.setBackground(new Color(245, 245, 245));
        JScrollPane scroll = new JScrollPane(txtOutput);
        outputPanel.add(scroll, BorderLayout.CENTER);

        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        add(mainPanel);

        PrintStream printStream = new PrintStream(new CustomOutputStream(txtOutput));
        System.setOut(printStream);
        System.setErr(printStream);

        btnCreate.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                double balance = Double.parseDouble(txtBalance.getText().trim());
                String type = (String) cmbType.getSelectedItem();
                double extra = Double.parseDouble(txtExtra.getText().trim());

                Account acc = null;
                if ("Savings".equals(type)) {
                    acc = new SavingsAccount(name, balance, extra);
                } else if ("Checking".equals(type)) {
                    acc = new CheckingAccount(name, balance, extra);
                } else if ("Credit".equals(type)) {
                    acc = new CreditAccount(name, balance, extra);
                }

                if (acc != null) {
                    bank.openAccount(acc);
                    System.out.println("[SYSTEM] Successfully created new " + type + " account. ID: " + acc.getId());
                    txtName.setText("");
                    txtBalance.setText("");
                    txtExtra.setText("");
                }
            } catch (Exception ex) {
                System.out.println("[ERROR] Invalid Input: Please ensure all fields are filled correctly!");
            }
        });

        btnList.addActionListener(e -> {
            System.out.println("\n--- CURRENT ACCOUNT LIST ---");
            bank.listAccounts();
        });

        btnSummary.addActionListener(e -> {
            System.out.println("\n--- BANK GENERAL SUMMARY ---");
            bank.printSummary();
        });

        btnDeposit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtID.getText().trim());
                double amount = Double.parseDouble(txtAmount.getText().trim());
                Account acc = bank.findById(id);
                if (acc != null) {
                    acc.deposit(amount);
                    System.out.println("[SUCCESS] Deposited " + amount + " to ID " + id + ". New Balance: " + acc.getBalance());
                    txtAmount.setText("");
                } else {
                    System.out.println("[ERROR] Account with ID " + id + " not found!");
                }
            } catch (Exception ex) {
                System.out.println("[ERROR] Invalid Input: Please check ID and Amount!");
            }
        });

        btnWithdraw.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtID.getText().trim());
                double amount = Double.parseDouble(txtAmount.getText().trim());
                Account acc = bank.findById(id);
                if (acc != null) {
                    boolean success = acc.withdraw(amount);
                    if (success) {
                        System.out.println("[SUCCESS] Withdrew " + amount + " from ID " + id + ". New Balance: " + acc.getBalance());
                        txtAmount.setText("");
                    } else {
                        System.out.println("[ERROR] Transaction Failed: Insufficient funds or limit exceeded!");
                    }
                } else {
                    System.out.println("[ERROR] Account with ID " + id + " not found!");
                }
            } catch (Exception ex) {
                System.out.println("[ERROR] Invalid Input: Please check ID and Amount!");
            }
        });

        btnFind.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtID.getText().trim());
                Account acc = bank.findById(id);
                if (acc != null) {
                    System.out.println("\n[INFO] Account Details (ID: " + id + "):");
                    System.out.println(acc.toString());
                } else {
                    System.out.println("[ERROR] Account with ID " + id + " not found!");
                }
            } catch (Exception ex) {
                System.out.println("[ERROR] Invalid Input: Please enter a valid ID!");
            }
        });
    }

    private static class CustomOutputStream extends OutputStream {
        private final JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankGUI().setVisible(true);
        });
    }
}