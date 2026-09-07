package dao;
import database.DBConnection;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public int insertCustomer(Customer customer) throws SQLException {

        String query = "INSERT INTO customers (name, age, address, gender, phone) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pt = con.prepareStatement(query,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            pt.setString(1, customer.getName());
            pt.setInt(2, customer.getAge());
            pt.setString(3, customer.getAddress());
            pt.setString(4, customer.getGender());
            pt.setString(5, customer.getPhone());

            pt.executeUpdate();

            try (ResultSet rs = pt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    public Customer findCustomer(int customerId) throws SQLException {

        String query = "SELECT * FROM customers WHERE customerId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pt = con.prepareStatement(query)) {

            pt.setInt(1, customerId);

            try (ResultSet rs = pt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("customerId");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String address = rs.getString("address");
                    String gender = rs.getString("gender");
                    String phone = rs.getString("phone");
                    return new Customer(id, name, age, phone, address, gender);
                }
            }
        }
        return null;
    }

    public void updateCustomerDetails(int customerId, String name, String phone, String address, String gender) throws SQLException {

        String query = "UPDATE customers SET name = ?, phone = ?, address = ?, gender = ? WHERE customerId = ?";

        try(Connection con = DBConnection.getConnection();
        PreparedStatement pt = con.prepareStatement(query);
        ){
            pt.setString(1,name);
            pt.setString(2,phone);
            pt.setString(3,address);
            pt.setString(4,gender);
            pt.setInt(5,customerId);

            pt.executeUpdate();

        }
    }

}

