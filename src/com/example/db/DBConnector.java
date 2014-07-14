package com.example.db;

import java.lang.annotation.Retention;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.sun.org.glassfish.gmbal.ParameterNames;

public class DBConnector {
	// The JDBC Connector Class.
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/android";
	private Connection c;
	private String name, pass;
	Properties p;
	private final String subjectsId = "ID";
	private final String userTableId = "ID";
	private final String ssStudentId = "studentId";
	private final String ssSubjectId = "subjectId";
	private final String ssMark = "mark";
	private final String ssfinished = "finished";

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
			String query = "select id from students where " + "(email = '"
					+ email + "' AND " + "pass = '" + pass + "');";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (!rs.first()) {
				sb.append("-1");
				c.close();
				return sb.toString();
			}
			sb.append(rs.getInt(userTableId));
			sb.append('#');
			ArrayList<String> subjects = getSubjectsFor(rs.getInt(userTableId));
			for(String s : subjects)
				sb.append(s);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public ArrayList<String> getSubjectsFor(int studentId) {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> arr = new ArrayList<String>();
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "Select * from studentSubject where (" + 
			ssStudentId	+ " = " + studentId + ");";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				sb.append(getSubjectInfo(rs.getInt(ssSubjectId)));
				sb.append(" ");
				sb.append(rs.getInt(ssMark));
				sb.append(" ");
				sb.append(rs.getInt(ssfinished));
				arr.add(sb.toString());
				sb = new StringBuilder();
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	/**
	 * @param subjectId - id of a subject to select
	 * @return string with pattern "id name credit"
	 */
	public String getSubjectInfo(int subjectId) {
		String info = "";
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "Select * from subjects where (" + subjectsId
					+ " = " + subjectId + ");";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				info = rs.getInt(1) + " " + rs.getString(2) +
						" " + rs.getInt(3);
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return info;
	}

	public void addStudentSubject(int studentId, int subjectId, int greade,
			int finished) {
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "insert into studentSubject values (" + studentId
					+ ", " + subjectId + ", " + greade + ", " + finished + ");";
			System.out.println(query);
			st.execute(query);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		DBConnector dbc = new DBConnector("root", "root");
		for(String s : dbc.getSubjectsFor(2)){
			System.out.println(s);
		}
		/*dbc.addStudentSubject(2, 1, 89, 1);
		dbc.addStudentSubject(2, 2, 91, 0);*/
	}
}
