package com.topdesk.yanap.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.SprintDao;
import com.topdesk.yanap.database.Team;
import com.topdesk.yanap.database.TeamDao;
import com.topdesk.yanap.database.User;
import com.topdesk.yanap.database.UserBySprint;
import com.topdesk.yanap.database.UserBySprintDao;
import com.topdesk.yanap.database.UserDao;

public class YanapServlet extends HttpServlet {
	private static final long serialVersionUID = -8290236007841458135L;

	private static final String ROOT_URL = "/boards";
	private static final String USER_BY_TEAM_URL = ROOT_URL + "/users";
	private static final String TEAM_URL = ROOT_URL + "/teams";
	private static final String AVAILABILITY_SUB_URL = "/availability";
	private static final String JSON_TYPE = "application/json; charset=utf8";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().equals(req.getContextPath() + ROOT_URL)) {
			doGetAllSprints(req, resp);
		} else if (req.getRequestURI().startsWith(req.getContextPath() + USER_BY_TEAM_URL)) {
			doGetUserList(req, resp);
		} else if (req.getRequestURI().startsWith(req.getContextPath() + TEAM_URL)) {
			doGetTeamList(req, resp);
		} else {
			doGetSingleSprint(req, resp);
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		System.err.println("URI: " + uri);

		if (uri.contains(AVAILABILITY_SUB_URL)) {
			doSaveProperty(req, resp);
		} else if (uri.startsWith(req.getContextPath() + USER_BY_TEAM_URL)) {
			doSaveUser(req, resp);
		} else if (uri.startsWith(req.getContextPath() + ROOT_URL)) {
			doSaveSprint(req, resp);
		}
	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().equals(req.getContextPath() + ROOT_URL)) {
			doCreateSprint(req, resp);
		} else if (req.getRequestURI().equals(req.getContextPath() + USER_BY_TEAM_URL)) {
			doCreateUser(req, resp);
		} else if (req.getRequestURI().equals(req.getContextPath() + TEAM_URL)) {
			doCreateTeam(req, resp);
		} else {
			try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
				resp.setContentType(JSON_TYPE);
				resp.setStatus(400);
				writer.write("not recognized");
			}
		}
	}
	
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().startsWith(req.getContextPath() + TEAM_URL)) {
			doDeleteTeam(req, resp);
		} else if (req.getRequestURI().startsWith(req.getContextPath() + ROOT_URL)) {
			doDeleteSprint(req, resp);
		} else {
			try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
				resp.setContentType(JSON_TYPE);
				resp.setStatus(400);
				writer.write("not recognized");
			}
		}
	}

	private void doGetAllSprints(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
		System.err.println("Get all Sprints");
		SprintDao dao = (SprintDao) getServletContext().getAttribute(SprintDao.CONTEXT_NAME);

		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			new Gson().toJson(dao.getAll(), writer);
		}
	}

	private void doGetUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDao userDao = (UserDao) getServletContext().getAttribute(UserDao.CONTEXT_NAME);

		String team = req.getRequestURI().replace(req.getContextPath() + USER_BY_TEAM_URL + "/", "");
		System.err.println("Get user list for: " + team);

		List<User> userList = userDao.getByTeam(team);

		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			new Gson().toJson(userList, writer);
		}
	}
	
	private void doGetTeamList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TeamDao teamDao = (TeamDao) getServletContext().getAttribute(TeamDao.CONTEXT_NAME);
		
		System.err.println("Get team list");
		
		List<Team> userList = teamDao.getAll();
		
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			new Gson().toJson(userList, writer);
		}
	}
	
	private void doGetSingleSprint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SprintDao sprintDao = (SprintDao) getServletContext().getAttribute(SprintDao.CONTEXT_NAME);
		UserBySprintDao userBySprintDao = (UserBySprintDao) getServletContext().getAttribute(UserBySprintDao.CONTEXT_NAME);

		String sprintIdFromRequest = getNumberFromString(req.getRequestURI());
		long numericSprintId = Long.parseLong(sprintIdFromRequest); // NFE will result in empty display, that's fine

		System.err.println("Get single sprint: " + numericSprintId);

		List<UserBySprint> userList = userBySprintDao.getAllForSprint(sprintDao.getById(numericSprintId));

		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			new Gson().toJson(new SprintAndUsers(userList), writer);
		}
	}
	
	private void doSaveUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserDao userDao = (UserDao) getServletContext().getAttribute(UserDao.CONTEXT_NAME);
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);

			String responseBody = getResponseBodyAsString(req);

			Gson gson = new GsonBuilder().create();
			User sprintAndUsers = new Gson().fromJson(responseBody, User.class);
			userDao.update(sprintAndUsers);

			writer.write(responseBody);
		}
	}

	private void doSaveSprint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SprintDao sprintDao = (SprintDao) getServletContext().getAttribute(SprintDao.CONTEXT_NAME);
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);

			String responseBody = getResponseBodyAsString(req);

			Gson gson = new GsonBuilder().create();
			SprintAndUsers sprintAndUsers = new Gson().fromJson(responseBody, SprintAndUsers.class);
			sprintDao.update(sprintAndUsers.getSprint());

			writer.write(responseBody);
		}
	}

	private void doSaveProperty(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserBySprintDao userBySprintDao = (UserBySprintDao) getServletContext().getAttribute(UserBySprintDao.CONTEXT_NAME);
		String sprintId = getNumberFromString(req.getRequestURI());

		// currently only has availability

		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);

			String responseBody = getResponseBodyAsString(req);

			StatusUpdateData updateData = new Gson().fromJson(responseBody, StatusUpdateData.class);
			System.err.println("Save Property for sprint " + sprintId + ": " + updateData);
			userBySprintDao.saveAvailability(Long.parseLong(sprintId),
				updateData.getUserId(), updateData.getDayIndex(), updateData.getValue());

			writer.write(responseBody);
		}
	}
	
	private void doCreateSprint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SprintDao sprintDao = (SprintDao) getServletContext().getAttribute(SprintDao.CONTEXT_NAME);
		UserBySprintDao userBySprintDao = (UserBySprintDao) getServletContext().getAttribute(UserBySprintDao.CONTEXT_NAME);
		UserDao userDao = (UserDao) getServletContext().getAttribute(UserDao.CONTEXT_NAME);

		System.err.println("Create Sprint");

		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			
			String responseBody = getResponseBodyAsString(req);

			Gson gson = new GsonBuilder().create();
			SprintCreationData newSprint = new Gson().fromJson(responseBody, SprintCreationData.class);
			System.err.println(newSprint);
			Sprint sprint = sprintDao.create(newSprint.getSprint());
			userBySprintDao.createForSprint(sprint, newSprint.getUsers());

			new Gson().toJson(sprint, writer);
		}
	}
	
	private void doCreateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDao userDao = (UserDao) getServletContext().getAttribute(UserDao.CONTEXT_NAME);

		System.err.println("Create User");

		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);

			String responseBody = getResponseBodyAsString(req);

			Gson gson = new GsonBuilder().create();
			System.err.println(responseBody);
			User newUser = new Gson().fromJson(responseBody, User.class);
			userDao.create(newUser);

			new Gson().toJson(newUser, writer);
		}
	}
	
	private void doCreateTeam(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TeamDao teamDao = (TeamDao) getServletContext().getAttribute(TeamDao.CONTEXT_NAME);
		
		System.err.println("Create Team");
		
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			
			String responseBody = getResponseBodyAsString(req);
			
			Gson gson = new GsonBuilder().create();
			System.err.println(responseBody);
			Team newTeam = new Gson().fromJson(responseBody, Team.class);
			teamDao.create(newTeam);
			
			new Gson().toJson(newTeam, writer);
		}
	}
	
	private void doDeleteTeam(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TeamDao teamDao = (TeamDao) getServletContext().getAttribute(TeamDao.CONTEXT_NAME);
		
		String id = getNumberFromString(req.getRequestURI());
		System.err.println("Delete Team with id" + id);
		
		try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), Charset.forName("UTF-8"))) {
			resp.setContentType(JSON_TYPE);
			
			String responseBody = getResponseBodyAsString(req);
			
			Gson gson = new GsonBuilder().create();
			System.err.println(responseBody);
			Team deleteme = new Gson().fromJson(responseBody, Team.class);
			teamDao.delete(Long.parseLong(id));
			
			new Gson().toJson(deleteme, writer);
		}
	}
	private void doDeleteSprint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserBySprintDao userBySprintDao = (UserBySprintDao) getServletContext().getAttribute(UserBySprintDao.CONTEXT_NAME);
		SprintDao sprintDao = (SprintDao) getServletContext().getAttribute(SprintDao.CONTEXT_NAME);
		
		long id = Long.parseLong(getNumberFromString(req.getRequestURI()));
		System.err.println("Delete Sprint with id" + id);
		
		userBySprintDao.deleteBySprint(id);
		sprintDao.delete(id);
		
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	private String getNumberFromString(String string) {
		return string.replaceAll("\\D", "");
	}

	private String getResponseBodyAsString(HttpServletRequest req) {
		StringBuilder jb = new StringBuilder();
		String line;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /*report an error*/ }
		return jb.toString();
	}

}
