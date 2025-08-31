package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

public class UserDAO {
	
    public User findOrCreateUser(String name, String contact) throws  SQLException {
    	   OnlyConnection connProvider = new OnlyConnection();

        try (Connection con = connProvider.getConnection()) {
            // Step 1: Check if user exists
            String query = "SELECT * FROM users WHERE name=? AND mobile=?";
            PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setString(2, contact);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("user_id"), name, contact);
            }

            // Step 2: Insert new user
            String insert = "INSERT INTO users (name, mobile) VALUES (?, ?)";
            PreparedStatement insertPst = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            insertPst.setString(1, name);
            insertPst.setString(2, contact);
            insertPst.executeUpdate();

            ResultSet keys = insertPst.getGeneratedKeys();
            if (keys.next()) {
                return new User(keys.getInt(1), name, contact);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }  

}
