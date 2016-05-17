package com.topdesk.yanap.web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by stephaniep on 01.04.2016.
 */
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
			resp.setContentType("application/json; charset=utf8");
			writer.write("POST: work in progress");
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
