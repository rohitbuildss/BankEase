package dao;
import database.DBConnection;
import model.Account;
import model.Transaction;

import java.sql.*;

public class AccountDAO {

    public int insertAccount(Account account) throws SQLException {

        String query = "insert into accounts(accounttype,balance,customerid,password) values(?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pt = con.prepareStatement(query,
                     java.sql.Statement.RETURN_GENERATED_KEYS);
        ) {
            pt.setString(1, account.getAccountType());
            pt.setDouble(2, account.getBalance());
            pt.setInt(3, account.getCustomerId());
            pt.setString(4, account.getPassword());

            pt.executeUpdate();

            try(ResultSet rs = pt.getGeneratedKeys()){
                while(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Account findAccount(int accountNumber) throws SQLException {

        String query = "SELECT accountNumber, accountType, balance, customerId, password\n" +
                "FROM accounts\n" +
                "WHERE accountNumber = ?";

        try(Connection con = DBConnection.getConnection();
        PreparedStatement pt = con.prepareStatement(query);
        ){

            pt.setInt(1,accountNumber);

           try( ResultSet rs = pt.executeQuery();) {

               while (rs.next()) {
                   int accountNumber1 = rs.getInt("accountNumber");
                   String accountType = rs.getString("accountType");
                   double balance = rs.getDouble("balance");
                   int customerId = rs.getInt("customerId");
                   String password = rs.getString("password");

                   return new Account(accountNumber1, accountType, balance, customerId, password);
               }
           }
        }

      return null;
    }

    public double getBalance(int accountNumber) throws SQLException {

        String query = "SELECT balance FROM accounts WHERE accountNumber = ?";

        try(Connection con = DBConnection.getConnection();
           PreparedStatement pt = con.prepareStatement(query);
        ){
            pt.setInt(1,accountNumber);
            try(ResultSet rs = pt.executeQuery();){
                if(rs.next()){
                    return rs.getDouble("balance");
                }
            }
        }
        return 0.0;
    }

    public void deposit(int accountNumber , double amount) throws SQLException {

        String query = "UPDATE accounts SET balance = balance + ? WHERE accountNumber = ?";

        try(Connection con = DBConnection.getConnection();
        PreparedStatement pt = con.prepareStatement(query);
        ){
            pt.setInt(2,accountNumber);
            pt.setDouble(1,amount);
            pt.executeUpdate();
        }
    }

    public void deposit(Connection con, int accountNumber, double amount) throws SQLException{

        String query = "UPDATE accounts SET balance = balance + ? WHERE accountNumber = ?";

        try(PreparedStatement pt = con.prepareStatement(query)){
            pt.setDouble(1,amount);
            pt.setInt(2,accountNumber);
            pt.executeUpdate();
        }

    }

    public void withdraw(int accountNumber, double amount) throws SQLException {

        String query = "UPDATE accounts SET balance = balance - ? WHERE accountNumber = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pt = con.prepareStatement(query);
        ){
            pt.setDouble(1, amount);
            pt.setInt(2, accountNumber);

            pt.executeUpdate();
        }
    }

    public int withdraw(Connection con, int accountNumber, double amount) throws SQLException {

        String query = "UPDATE accounts SET balance = balance - ? WHERE accountNumber = ?";

        try (PreparedStatement pt = con.prepareStatement(query)) {

            pt.setDouble(1, amount);
            pt.setInt(2, accountNumber);

            return pt.executeUpdate();
        }
    }

    public void updatePassword(String password , int accountNumber) throws SQLException {

        String query = "UPDATE accounts SET password = ? WHERE accountNumber = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pt = con.prepareStatement(query);
        ){
            pt.setString(1,password);
            pt.setInt(2,accountNumber);
            pt.executeUpdate();

        }

    }
}
