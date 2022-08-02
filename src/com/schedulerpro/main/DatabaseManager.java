package com.schedulerpro.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

	private final String URL = "jdbc:sqlite:resources/data.sqlite";
//	private final String URL = "jdbc:mysql://52.206.157.109:3306/U04EEe";
	private final String USERNAME = "U04EEe";
	private final String PASSWORD = "53688214675";

	private Connection conn;

	public DatabaseManager() {
		connect();
	}

	/**
	 * Connect to database
	 */
	public void connect() {
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			/*SQLiteConfig sqLiteConfig = new SQLiteConfig();
			Properties properties = sqLiteConfig.toProperties();
			properties.setProperty(Pragma.DATE_STRING_FORMAT.pragmaName, "yyyy-MM-dd HH:mm:ss.sss");
			conn = DriverManager.getConnection(URL, properties);*/
			System.out.println("Connection to Database has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Close connection to database
	 */
	public void close() throws SQLException {
		if (conn != null) {
			conn.close();
			System.out.println("Connection to Database has been closed.");
		}
	}

	/**
	 * Return instance of the database connection
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return conn;
	}

}
