package com.example.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.db.DBConnector;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnector dbc;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Main() {
		super();
		dbc = new DBConnector("root", "root");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuilder resp = new StringBuilder();
		if (request.getParameter("type").equals("2")) {
			System.out.println(request.getParameter("userID"));
			for(int i = 0; true; i++){
				if(request.getParameter("ind" + i) != null){
					dbc.addStudentSubject(Integer.parseInt(request.getParameter("userID")),
					Integer.parseInt(request.getParameter("ind" + i)), 0, 0);
					response.getWriter().print("archeulia");
					response.getWriter().flush();
					response.getWriter().close();
				}
			}
		} else {
			String email = request.getParameter("email");
			String pass = request.getParameter("pass");
			resp.append(dbc.getListFor(email, pass));
			resp.append('|');
			int myId = Integer.parseInt(resp.substring(0, resp.indexOf("#")));
			if (myId != -1) {
				ArrayList<Integer> arr = dbc.getPreReqs();
				ArrayList<Integer> mySubs = dbc.getSubjectIdsFor(myId, 0);
				ArrayList<Integer> learnedSubs = dbc.getSubjectIdsFor(myId, 1);
				mySubs.addAll(learnedSubs);
				ArrayList<Integer> allSubs = dbc.getAllSubjects();
				boolean[] subs = new boolean[600];
				boolean[] learned = new boolean[600];
				boolean[] failed = new boolean[600];
				for(int i : learnedSubs)
					learned[i] = true;
				for (int i : mySubs)
					subs[i] = true;
				for (int i = 0; i < arr.size(); i += 2) {
					if (!learned[arr.get(i)])
						failed[arr.get(i + 1)] = true;
				}
				boolean started = false;
				for (int i = 0; i < allSubs.size(); i++) {
					if (!failed[allSubs.get(i)] && !subs[allSubs.get(i)]) {
						if(started)
							resp.append(',');
						resp.append(dbc.getSubjectInfo(allSubs.get(i)));
						started = true;
					}
				}
			}
		}
		response.getWriter().print(resp);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
