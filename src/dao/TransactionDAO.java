package dao;

import database.DBConnection;
import model.Transaction;
import model.TransactionType;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public int insertTransaction(Transaction transaction) throws SQLException {

        String query = "insert into transactions(senderAccNumber, receiverAccNumber, amount, timeStamp, transactionType) values(?,?,?,?,?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            pt.setInt(1, transaction.getSenderAccountNumber());
            pt.setInt(2, transaction.getReceiverAccountNumber());
            pt.setDouble(3, transaction.getAmount());
            pt.setTimestamp(4, Timestamp.valueOf(transaction.getTimeStamp()));
            pt.setString(5, transaction.getTransactionType().name());

            pt.executeUpdate();

            try(ResultSet rs = pt.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    public int insertTransaction(Connection con , Transaction transaction) throws SQLException {
        String query = "insert into transactions(senderAccNumber, receiverAccNumber, amount, timeStamp, transactionType) values(?,?,?,?,?)";

        try(PreparedStatement pt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            pt.setInt(1, transaction.getSenderAccountNumber());
            pt.setInt(2, transaction.getReceiverAccountNumber());
            pt.setDouble(3, transaction.getAmount());
            pt.setTimestamp(4, Timestamp.valueOf(transaction.getTimeStamp()));
            pt.setString(5, transaction.getTransactionType().name());

            pt.executeUpdate();

            try(ResultSet rs = pt.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<Transaction> findTransactionsByAccount(
            int senderAccNumber,
            int receiverAccNumber) throws SQLException {

        String query = "SELECT * FROM transactions " +
                "WHERE senderAccNumber = ? OR receiverAccNumber = ? " +
                "ORDER BY timeStamp DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pt = con.prepareStatement(query)) {

            pt.setInt(1, senderAccNumber);
            pt.setInt(2, receiverAccNumber);

            try (ResultSet rs = pt.executeQuery()) {

                while (rs.next()) {

                    int transactionId = rs.getInt("transactionId");
                    int senderAccountNumber =
                            rs.getInt("senderAccNumber");
                    int receiverAccountNumber =
                            rs.getInt("receiverAccNumber");

                    double amount = rs.getDouble("amount");

                    LocalDateTime timeStamp =
                            rs.getTimestamp("timeStamp").toLocalDateTime();

                    TransactionType transactionType =
                            TransactionType.valueOf(
                                    rs.getString("transactionType")
                            );

                    Transaction transaction = new Transaction(
                            transactionId,
                            senderAccountNumber,
                            receiverAccountNumber,
                            amount,
                            timeStamp,
                            transactionType
                    );

                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }
}