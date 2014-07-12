package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnector {
	// The JDBC Connector Class.
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/android";
	private Connection c;
	private String name, pass;
	Properties p;

	public DBConnector(String name, String pass) {
		this.name = name;
		this.pass = pass;
		p = new Properties();
		p.put("user", this.name);
		p.put("password", this.pass);
		try {
			Class.forName(dbClassName);
		} catch (ClassNotFoundException e) {
			System.out.println("Wrong db name or pass");
			e.printStackTrace();
		}
	}

	public String getListFor(String email, String pass) {
		StringBuilder sb = new StringBuilder();
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "select id from students where "
					+ "(email = '" + email + "' AND " + "pass = '" + 
					pass + "');";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if(!rs.first()){
				sb.append("-1");
				c.close();
				return sb.toString();
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public void addStudentSubject(int studentId, int subjectId, int greade, int finished){
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "insert into studentSubject values (" + studentId + ", " + 
					subjectId + ", " + greade + ", " + finished + ");";
			System.out.println(query);
			st.execute(query);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
