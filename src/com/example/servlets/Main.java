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
		if (request.getParameter("type") != null
				&& request.getParameter("type").equals(2)) {
		} else {
			String email = request.getParameter("email");
			String pass = request.getParameter("pass");
			resp.append(dbc.getListFor(email, pass));
			resp.append('|');
			System.out.println(resp);
			int myId = Integer.parseInt(resp.substring(0, resp.indexOf("#")));
			if (myId != -1) {
				ArrayList<Integer> arr = dbc.getPreReqs();
				ArrayList<Integer> mySubs = dbc.getSubjectIdsFor(myId);
				ArrayList<Integer> allSubs = dbc.getAllSubjects();
				for (int i = 0; i < arr.size(); i += 2)
					System.out.println(arr.get(i) + " " + arr.get(i + 1));
				System.out.println("--------------------------");
				for (int i = 0; i < mySubs.size(); i++)
					System.out.println(mySubs.get(i));
				boolean[] subs = new boolean[600];
				boolean[] failed = new boolean[600];
				for (int i : mySubs)
					subs[i] = true;
				for (int i = 0; i < arr.size(); i += 2) {
					if (!subs[arr.get(i)])
						failed[arr.get(i + 1)] = true;
				}
				for (int i = 0; i < allSubs.size(); i++) {
					if (!failed[allSubs.get(i)] && !subs[allSubs.get(i)]) {
						resp.append(dbc.getSubjectInfo(allSubs.get(i)));
						resp.append(',');
					}
				}
			}
		}
		System.out.println(resp);
		response.getWriter().println(resp);
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
