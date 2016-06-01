package com.topdesk.yanap.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

public class YanapServlet extends HttpServlet {
	private static final long serialVersionUID = -8290236007841458135L;
	private static final String SPRINT_PATH = "src/main/webapp/single-sprint.json";
	private static final String ALLSPRINTS_PATH = "src/main/webapp/allsprints.json";

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
		String uri = req.getRequestURI();
		if (uri.startsWith("/boards")) {
			doSaveSprint(req, resp);
		} else if (uri.startsWith("/sprint")) {
			doSaveProperty(req, resp);
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
			String tempReturn = FileUtils.readFileToString(new File(ALLSPRINTS_PATH));
			resp.setContentType("application/json; charset=utf8");
			writer.write(tempReturn);
		}
	}
	private void doGetSingleSprint(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
		String sprintId = req.getRequestURI().replace("/boards/", "");
		System.err.println("Get single sprint: " + sprintId);
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			String tempReturn = FileUtils.readFileToString(new File(SPRINT_PATH));
			resp.setContentType("application/json; charset=utf8");
			writer.write(tempReturn);
		}
	}

	private void doSaveSprint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sprintId = req.getRequestURI().replace("/boards/", "");
		System.err.println("Saving: " + sprintId);
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json");

			StringBuilder jb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) { /*report an error*/ }

			PrintWriter fileWriter = new PrintWriter(SPRINT_PATH, "UTF-8");
			fileWriter.print(jb.toString());
			fileWriter.close();

			writer.write(jb.toString());
		}
	}

	private void doSaveProperty(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json; charset=utf8");
			writer.write("doSaveProperty work in progress");
		}
	}

}
