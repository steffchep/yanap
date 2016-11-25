package com.topdesk.yanap.web;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.SprintRepository;
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

	private final SprintRepository sprintRepository;
	private final UserDao userDao;
	private final TeamDao teamDao;
	private final UserBySprintDao userBySprintDao;

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
		
		List<UserBySprint> users = userBySprintDao.getAllForSprint(id);
		return new SprintAndUsers(users);
	}
	
	@RequestMapping(value = "/boards", method = RequestMethod.POST)
	public void doCreateSprint(@RequestBody SprintCreationData newSprint) {
		log.info("Create Sprint: {}", newSprint);
		Sprint sprint = sprintRepository.save(newSprint.getSprint());
		userBySprintDao.createForSprint(sprint, newSprint.getUsers());
	}
	
	@RequestMapping(value = "/boards/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public User doSaveUser(@RequestBody User sprintAndUsers) {
		userDao.update(sprintAndUsers);

		return sprintAndUsers;
	}

	@RequestMapping(value = "/boards/{sprintId}/availability", method = RequestMethod.POST)
	public StatusUpdateData doSaveProperty(@PathVariable long sprintId, @RequestBody StatusUpdateData updateData) {

		log.info("Save Property for sprint " + sprintId + ": " + updateData);
		userBySprintDao.saveAvailability(sprintId, updateData.getUserId(), updateData.getDayIndex(), updateData.getValue());

		return updateData;
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
	
}
