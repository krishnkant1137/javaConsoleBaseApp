package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OnlyConnection {
	 private final String URL = "jdbc:mysql://localhost:3306/lost_found_tracker";
	    private final String USER = "root";
	    private final String PASS = "@Kana1137";

	    public Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(URL, USER, PASS);
	    }
}
