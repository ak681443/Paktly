package com.paktly.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHandler {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/paktly";
	static final Logger LOGGER = Logger.getLogger(DBHandler.class.getName());

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "toor";
	Connection conn = null;
	PreparedStatement preparedStatement = null;

	public DBHandler() {
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
		}catch(Exception e){
			LOGGER.log(Level.WARNING, "Error while making the JDBC connection");
			e.printStackTrace();
		}
	}

	public void close() {
		try{
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		catch(Exception e){
			LOGGER.log(Level.WARNING, "Error while making the JDBC connection");
			e.printStackTrace();
		}
	}

	public ResultSet execute(String query, String...args) {
		ResultSet rs = null;
		try{
			preparedStatement = conn.prepareStatement(query);
			for (int i = 1; i <= args.length; ++i) {
				preparedStatement.setString(i, args[i-1]);
			}
			rs = preparedStatement.executeQuery();
		}catch(Exception e){
			LOGGER.log(Level.WARNING, "Error while making the JDBC connection");
			e.printStackTrace();
		}
		return rs;
	}

	public int update(String query, String...args) {
		int c = 0;
		try{
			preparedStatement = conn.prepareStatement(query);
			for (int i = 1; i <= args.length; ++i) {
				preparedStatement.setString(i, args[i-1]);
			}
			c = preparedStatement.executeUpdate();
		}catch(Exception e){
			LOGGER.log(Level.WARNING, "Error while making the JDBC connection");
			e.printStackTrace();
		}
		return c;
	}

}
