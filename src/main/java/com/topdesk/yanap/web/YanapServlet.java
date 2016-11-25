package com.topdesk.yanap.web;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.SprintDao;
import com.topdesk.yanap.database.Team;
import com.topdesk.yanap.database.TeamDao;
import com.topdesk.yanap.database.User;
import com.topdesk.yanap.database.UserBySprint;
import com.topdesk.yanap.database.UserBySprintDao;
import com.topdesk.yanap.database.UserDao;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YanapServlet {

	private final SprintDao sprintDao;
	private final UserDao userDao;
	private final TeamDao teamDao;
	private final UserBySprintDao userBySprintDao;

	@RequestMapping(value = "/boards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Sprint> doGetAllSprints()  {
		log.info("Get all Sprints");

		return sprintDao.getAll();
	}

	@RequestMapping(value = "boards/users/{team}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> doGetUserList(@PathVariable String team) {

		log.info("Get user list for: {}", team);

		return userDao.getByTeam(team);
	}

	@RequestMapping(value = "/boards/teams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Team> doGetTeamList() {

		log.info("Get team list");

		return teamDao.getAll();
	}

	@RequestMapping(value = "/boards/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SprintAndUsers doGetSingleSprint(@PathVariable long id) {

		log.info("Get single sprint: " + id);
		
		List<UserBySprint> users = userBySprintDao.getAllForSprint(sprintDao.getById(id));
		return new SprintAndUsers(users);
	}

	@RequestMapping(value = "/boards/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public User doSaveUser(@RequestBody User sprintAndUsers) {
		userDao.update(sprintAndUsers);

		return sprintAndUsers;
	}

	@RequestMapping(value = "/boards", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public SprintAndUsers doSaveSprint(@RequestBody SprintAndUsers sprintAndUsers) {
		sprintDao.update(sprintAndUsers.getSprint());

		return sprintAndUsers;
	}

	@RequestMapping(value = "/boards/{sprintId}/availability", method = RequestMethod.POST)
	public StatusUpdateData doSaveProperty(@PathVariable long sprintId, @RequestBody StatusUpdateData updateData) {

		log.info("Save Property for sprint " + sprintId + ": " + updateData);
		userBySprintDao.saveAvailability(sprintId, updateData.getUserId(), updateData.getDayIndex(), updateData.getValue());

		return updateData;
	}

	@RequestMapping(value = "/boards", method = RequestMethod.PUT)
	public Sprint doCreateSprint(@RequestBody SprintCreationData newSprint) {
		log.info("Create Sprint: {}", newSprint);
		Sprint sprint = sprintDao.create(newSprint.getSprint());
		userBySprintDao.createForSprint(sprint, newSprint.getUsers());

		return sprint;
	}

	@RequestMapping(value = "/boards/users", method = RequestMethod.PUT)
	public User doCreateUser(@RequestBody User newUser) {
		log.info("Create User {}", newUser);
		userDao.create(newUser);

		return newUser;
	}

	@RequestMapping(value = "/boards/teams", method = RequestMethod.PUT)
	public void doCreateTeam(@RequestBody Team newTeam) {
		log.info("Create Team {}", newTeam);

		teamDao.create(newTeam);
	}

	@RequestMapping(value = "/boards/teams/{id}", method = RequestMethod.DELETE)
	public Team doDeleteTeam(@PathVariable long id, @RequestBody Team deleteme) {
		log.info("Delete Team with id {}", id);
		teamDao.delete(id);
		return deleteme;
	}
	
	@RequestMapping(value = "/boards/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void doDeleteSprint(@PathVariable long id) {
		log.info("Delete Sprint with id {}", id);

		userBySprintDao.deleteBySprint(id);
		sprintDao.delete(id);
	}

}
