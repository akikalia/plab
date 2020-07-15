package main.model;
import java.sql.*;

//import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnector {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String MYSQL_USERNAME = "";
    private static final String MYSQL_PASSWORD = "";
    private static final String MYSQL_DATABASE_SERVER = "";
    private static final String MYSQL_DATABASE_NAME = "";
	
	private static Connection con;
	
	static {
		try {
			Class.forName(JDBC_DRIVER);
			String url = "jdbc:mysql://" + MYSQL_DATABASE_SERVER + "/" + MYSQL_DATABASE_NAME;
			con = (Connection) DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return con;
	}
	
	public static void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
