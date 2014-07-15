package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

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
	private final String subjectMarksStudentId = "studentId";
	private final String subjectMarksSubjectId = "subjectId";

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
				sb.append("-1#");
				c.close();
				return sb.toString();
			}
			sb.append(rs.getInt(userTableId));
			sb.append('#');
			ArrayList<String> subjects = getSubjectsFor(rs.getInt(userTableId));
			for (int i = 0; i < subjects.size(); i++){
				if(i > 0)
					sb.append(',');
				sb.append(subjects.get(i));
			}
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
			String query = "Select * from studentSubject where (" + ssStudentId
					+ " = " + studentId + ");";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				sb.append(getSubjectInfo(rs.getInt(ssSubjectId)));
				sb.append(" ");
				sb.append(rs.getInt(ssMark));
				sb.append(" ");
				sb.append(rs.getInt(ssfinished));
				sb.append(getSubjectMarksFor(studentId, rs.getInt(ssSubjectId)));
				arr.add(sb.toString());
				sb = new StringBuilder();
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public ArrayList<Integer> getAllSubjects(){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "Select ID from subjects;";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				arr.add(rs.getInt(1));
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * 
	 * @param studentId
	 *            - id of student for whom we want to get subject list
	 * @return arraylist of subjects' ids' which he has chosen
	 */
	public ArrayList<Integer> getSubjectIdsFor(int studentId) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "Select " + ssSubjectId
					+ " from studentSubject where (" + ssStudentId + " = "
					+ studentId + ");";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				arr.add(rs.getInt(ssSubjectId));
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public String getSubjectMarksFor(int studentId, int subjectId) {
		StringBuilder marks = new StringBuilder();
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "SELECT * FROM subjectMarks where ("
					+ subjectMarksSubjectId + " = " + subjectId + " AND "
					+ subjectMarksStudentId + " = " + studentId + ");";
			System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				marks.append(" ");
				marks.append(rs.getString("workName"));
				marks.append(" ");
				marks.append(rs.getInt("mark"));
			}
			if (marks.length() == 0)
				return "";
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return marks.toString();
	}

	/**
	 * @param subjectId
	 *            - id of a subject to select
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
				info = rs.getInt(1) + " " + rs.getString(2) + " "
						+ rs.getInt(3);
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return info;
	}

	public ArrayList<Integer> getPreReqs() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "Select * from preReqs;";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				arr.add(rs.getInt(1));
				arr.add(rs.getInt(2));
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public void addStudentSubject(int studentId, int subjectId, int grade,
			int finished) {
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "insert into studentSubject values (" + studentId
					+ ", " + subjectId + ", " + grade + ", " + finished + ");";
			System.out.println(query);
			st.execute(query);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addPreReq(int preId, int postId) {
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "insert into preReqs values (" + preId + ", "
					+ postId + ");";
			System.out.println(query);
			st.execute(query);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addSubjectMark(int studentId, int subjectId, String workName,
			int mark) {
		try {
			c = DriverManager.getConnection(CONNECTION, p);
			Statement st = c.createStatement();
			String query = "insert into subjectMarks values (" + studentId
					+ ", " + subjectId + ", '" + workName + "', " + mark + ");";
			System.out.println(query);
			st.execute(query);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		DBConnector dbc = new DBConnector("root", "root");
		for (String s : dbc.getSubjectsFor(2)) {
			System.out.println(s);
		}
		//dbc.addPreReq(2, 4);
		/*
		 * dbc.addSubjectMark(2, 1, "qvizi1", 56); 
		 */
		// dbc.addStudentSubject(2, 2, 91, 0);
		//dbc.addStudentSubject(2, 3, 91, 1);
		//dbc.addSubjectMark(2, 3, "qvizi1", 0);
		//dbc.addSubjectMark(2, 3, "finaluri", 4);
	}
}
