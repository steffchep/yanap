package com.topdesk.yanap.web;

import java.time.LocalDate;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.SprintRepository;
import com.topdesk.yanap.database.User;
import com.topdesk.yanap.database.UserAvailabilityRepository;
import com.topdesk.yanap.database.UserRepository;
import com.topdesk.yanap.misc.Mapper;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YanapServlet {
	
	private final SprintRepository sprints;
	private final UserAvailabilityRepository availabilities;
	private final UserRepository users;
	private final Mapper mapper;

	@ResponseBody
	@RequestMapping(value = "/boards/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SprintAndUsers doGetSingleSprint(@PathVariable long id) {

		log.info("Get single sprint: " + id);
		
		Sprint sprint = sprints.findOne(id);
		if (sprint == null) {
			throw new ResourceNotFoundException("no sprint with id " + id);
		}
		return mapSprintToSprintAndUsers(sprint);
	}

	@ResponseBody
	@RequestMapping(value = "/removeUserFromSprint", method = RequestMethod.POST)
	public void removeUserFromSprint(@RequestParam(name = "sprint") long sprintId, @RequestParam(name = "user") long userId) {
		Sprint sprint = sprints.findOne(sprintId);
		if (sprint == null) {
			throw new ResourceNotFoundException("sprint " + sprintId + " not found");
		}
		sprint.getUsers().removeIf(user -> user.getId() == userId);
		sprints.save(sprint);
		log.info("removed {} from sprint {}", userId, sprintId);
	}

	@RequestMapping(value = "/addUserToSprint", method = RequestMethod.POST)
	public String addUserToSprint(@RequestParam(name = "user") String userUri, @RequestParam(name = "sprint") long sprintId) {
		Sprint sprint = sprints.findOne(sprintId);
		if (sprint == null) {
			throw new ResourceNotFoundException("sprint " + sprintId + " not found");
		}
		long userId = Long.parseLong(userUri.replaceAll("^.*/(?=\\d*$)", ""));
		User user = users.findOne(userId);
		if (user == null) {
			throw new ResourceNotFoundException("user " + userId + " not found");
		}
		if (!sprint.getUsers().contains(user)) {
			sprint.getUsers().add(user);
			sprints.save(sprint);
		}
		return "redirect:/board.html?id=" + sprintId;
	}

	private SprintAndUsers mapSprintToSprintAndUsers(Sprint sprint) {
		SprintAndUsers sprintAndUsers = new SprintAndUsers();
		sprintAndUsers.setId(sprint.getId());
		sprintAndUsers.setName(sprint.getName());
		sprintAndUsers.setStartDate(sprint.getStartDate());
		sprintAndUsers.setEndDate(sprint.getEndDate());
		sprintAndUsers.setStatus(sprint.getStatus());
		sprintAndUsers.setTeam(sprint.getTeam());
		sprintAndUsers.setPointsPlanned(sprint.getPointsPlanned());
		sprintAndUsers.setPointsCompleted(sprint.getPointsCompleted());
		sprintAndUsers.setDevelopers(mapper.mapUsersToUserAndDays(
				sprint.getUsers().stream().filter(User::isDeveloper).collect(Collectors.toList()),
				mapper.mapDateToLocalDate(sprint.getStartDate()),
				mapper.mapDateToLocalDate(sprint.getEndDate())));
		sprintAndUsers.setNondevelopers(mapper.mapUsersToUserAndDays(
				sprint.getUsers().stream().filter(user -> !user.isDeveloper()).collect(Collectors.toList()),
				mapper.mapDateToLocalDate(sprint.getStartDate()),
				mapper.mapDateToLocalDate(sprint.getEndDate())));
		return sprintAndUsers;
	}

	@ResponseBody
	@RequestMapping(value = "/boards", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void doCreateSprint(@RequestBody Sprint newSprint) {
		log.info("Create Sprint: {}", newSprint);
		newSprint.setStatus(1);
		sprints.save(newSprint);
	}

	@ResponseBody
	@RequestMapping(value = "/boards/{sprintId}/availability", method = RequestMethod.POST)
	public StatusUpdateData doSaveProperty(@PathVariable long sprintId, @RequestBody StatusUpdateData updateData) {
		log.info("Save Property for sprint " + sprintId + ": " + updateData);
		Sprint sprint = sprints.findOne(sprintId);
		if (sprint == null) {
			throw new ResourceNotFoundException("no sprint with id " + sprintId);
		}
		User user = users.findOne(updateData.getUserId());
		if (user == null) {
			throw new ResourceNotFoundException("no user with id " + updateData.getUserId());
		}
		LocalDate day = mapper.mapDateToLocalDate(sprint.getStartDate());
		for (int i = 0; i < updateData.getDayIndex(); i++) {
			day = mapper.nextWorkDay(day);
		}
		availabilities.save(mapper.mapToUserAvailability(user, day, updateData.getValue()));

		return updateData;
	}

}
