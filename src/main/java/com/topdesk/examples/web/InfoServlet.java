package com.topdesk.examples.web;

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
public class InfoServlet extends HttpServlet {
	private static final long serialVersionUID = -8290236007841458135L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType("application/json; charset=utf8");
			writer.write("{\n" +
				"\t \tname : \"My Awesome Sprint\",\n" +
				"\t\tdevelopers : [\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Dawid\",\n" +
				"\t\t\t\tdays : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Jana\",\n" +
				"\t\t\t\tdays : [ 1, 1, 3, 3, 1, 1, 1, 1, 1, 1 ]\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Jessica\",\n" +
				"\t\t\t\tdays : [ 1, 2, 1, 1, 1, 1, 1, 0.5, 3, 3 ]\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Michael\",\n" +
				"\t\t\t\tdays : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Rauf\",\n" +
				"\t\t\t\tdays : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Steffi\",\n" +
				"\t\t\t\tdays : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]\n" +
				"\t\t\t}\n" +
				"\t\t],\n" +
				"\t\tnondevelopers : [\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Tom\",\n" +
				"\t\t\t\tdays : [ 1, 1, 1, 1, 1, 3, 3, 3, 3, 3 ]\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\tname : \"Phil\",\n" +
				"\t\t\t\tdays : [ 1, 1, 1, 1, 1, 3, 3, 3, 3, 3 ]\n" +
				"\t\t\t}\n" +
				"\t\t]\n" +
				"\t }");
		}
	}

}
