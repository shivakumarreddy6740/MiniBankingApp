package banking;

import javax.swing.*;
import java.sql.*;

public class bankManagement {

    private static final int NULL = 0;
    static Connection con = connection.getConnection();
    static String sql = "";

    // ✅ Create Account
    public static boolean createAccount(String name, int passCode) {
        try {
            if (name.equals("") || passCode == NULL) {
                JOptionPane.showMessageDialog(null, "All fields are required!");
                return false;
            }

            Statement st = con.createStatement();
            sql = "INSERT INTO customer(cname,balance,pass_code) VALUES('" 
                  + name + "',1000," + passCode + ")";

            if (st.executeUpdate(sql) == 1) {
                JOptionPane.showMessageDialog(null, name + ", your account has been created! Now login.");
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Username not available!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Login
    public static boolean loginAccount(String name, int passCode) {
        try {
            if (name.equals("") || passCode == NULL) {
                JOptionPane.showMessageDialog(null, "All fields are required!");
                return false;
            }

            sql = "SELECT * FROM customer WHERE cname='" + name + "' AND pass_code=" + passCode;
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int ac_no = rs.getInt("ac_no"); // You can store or pass this to next screen
                JOptionPane.showMessageDialog(null, "Welcome, " + rs.getString("cname") + " (Account No: " + ac_no + ")");
                return true; // login success
            } else {
                JOptionPane.showMessageDialog(null, "Login failed! Invalid credentials.");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ✅ View Balance
    public static void getBalance(int acNo) {
        try {
            sql = "SELECT * FROM customer WHERE ac_no=" + acNo;
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Account No: " + rs.getInt("ac_no") +
                        "\nName: " + rs.getString("cname") +
                        "\nBalance: " + rs.getInt("balance") + ".00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Transfer Money
    public static boolean transferMoney(int sender_ac, int receiver_ac, int amount) throws SQLException {
        if (receiver_ac == NULL || amount == NULL) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return false;
        }
        try {
            con.setAutoCommit(false);

            sql = "SELECT * FROM customer WHERE ac_no=" + sender_ac;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("balance") < amount) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance!");
                    return false;
                }
            }

            Statement st = con.createStatement();

            // debit
            sql = "UPDATE customer SET balance=balance-" + amount + " WHERE ac_no=" + sender_ac;
            st.executeUpdate(sql);

            // credit
            sql = "UPDATE customer SET balance=balance+" + amount + " WHERE ac_no=" + receiver_ac;
            st.executeUpdate(sql);

            con.commit();
            JOptionPane.showMessageDialog(null, "Money Transferred Successfully!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            con.rollback();
        }
        return false;
    }
}
