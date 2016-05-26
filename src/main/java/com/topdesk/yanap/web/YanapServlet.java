package com.topdesk.yanap.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class YanapServlet extends HttpServlet {
	private static final long serialVersionUID = -8290236007841458135L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().equals("/boards")) {
			doGetAllSprints(req, resp);
		} else {
			doGetSingleSprint(req, resp);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json");
			String fileName = "single-sprint.json";

			StringBuilder jb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) { /*report an error*/ }

			System.err.println("Data: " + jb);
			PrintWriter fileWriter = new PrintWriter("src/main/webapp/single-sprint.json", "UTF-8");
			fileWriter.print(jb.toString());
			fileWriter.close();

			writer.write(jb.toString());
		}
	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json; charset=utf8");
			writer.write("PUT work in progress");
		}
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json; charset=utf8");
			writer.write("DELETE work in progress");
		}
	}

	private void doGetAllSprints(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
		System.err.println("Get all Sprints");
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json; charset=utf8");
			writer.write("work in progress");
		}
	}

	private void doGetSingleSprint(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
		String sprintId = req.getRequestURI().replace("/boards/", "");
		System.err.println("Get for Sprint: " + sprintId);
	}

}
