package banking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class bank {
    JFrame loginFrame, dashboardFrame;
    JTextField usernameField;
    JPasswordField passwordField;
    int loggedAccount = -1;

    public bank() {
        showLoginPage();
    }

    private void showLoginPage() {
        loginFrame = new JFrame("Bank Login");
        loginFrame.setSize(700, 700);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton createBtn = new JButton("Create Account");

        loginFrame.add(userLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginBtn);
        loginFrame.add(createBtn);

        loginFrame.setVisible(true);

   
        loginBtn.addActionListener(e -> {
            try {
                String name = usernameField.getText();
                int pass = Integer.parseInt(new String(passwordField.getPassword()));
                if (bankManagement.loginAccount(name, pass)) {
                    try {
                        Connection con = connection.getConnection();
                        PreparedStatement ps = con.prepareStatement("SELECT ac_no FROM customer WHERE cname=? AND pass_code=?");
                        ps.setString(1, name);
                        ps.setInt(2, pass);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            loggedAccount = rs.getInt("ac_no");
                            loginFrame.dispose();
                            showDashboard(name);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Enter valid username/password!");
            }
        });

        // --- CREATE ACCOUNT BUTTON ---
        createBtn.addActionListener(e -> {
            try {
                String name = usernameField.getText();
                int pass = Integer.parseInt(new String(passwordField.getPassword()));
                if (bankManagement.createAccount(name, pass)) {
                    JOptionPane.showMessageDialog(null, "Account Created. Please Login!");
                } else {
                    JOptionPane.showMessageDialog(null, "Account Creation Failed!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Enter valid data!");
            }
        });
    }

    // ---------------- DASHBOARD ----------------
    private void showDashboard(String userName) {
        dashboardFrame = new JFrame("Welcome " + userName);
        dashboardFrame.setSize(400, 300);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setLayout(new GridLayout(3, 1, 10, 10));

        JButton transferBtn = new JButton("💸 Transfer Money");
        JButton balanceBtn = new JButton("💰 View Balance");
        JButton logoutBtn = new JButton("🚪 Logout");

        dashboardFrame.add(transferBtn);
        dashboardFrame.add(balanceBtn);
        dashboardFrame.add(logoutBtn);

        dashboardFrame.setVisible(true);

        // --- ACTIONS ---
        transferBtn.addActionListener(e -> transferMoneyDialog());
        balanceBtn.addActionListener(e -> viewBalanceDialog());
        logoutBtn.addActionListener(e -> {
            dashboardFrame.dispose();
            showLoginPage();
        });
    }

    // ---------------- TRANSFER MONEY ----------------
    private void transferMoneyDialog() {
        JTextField receiverField = new JTextField();
        JTextField amountField = new JTextField();

        Object[] fields = {
            "Receiver Account No:", receiverField,
            "Amount:", amountField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Transfer Money", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int receiver = Integer.parseInt(receiverField.getText());
                int amount = Integer.parseInt(amountField.getText());
                if (bankManagement.transferMoney(loggedAccount, receiver, amount)) {
                    JOptionPane.showMessageDialog(null, "Money Transferred Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Transfer Failed!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Enter valid data!");
            }
        }
    }

    // ---------------- VIEW BALANCE ----------------
    private void viewBalanceDialog() {
        try {
            Connection con = connection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM customer WHERE ac_no=?");
            ps.setInt(1, loggedAccount);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Your Balance: ₹" + rs.getInt("balance"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new bank();
    }
}
