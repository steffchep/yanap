package com.topdesk.yanap.web;

import java.time.LocalDate;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.SprintRepository;
import com.topdesk.yanap.database.User;
import com.topdesk.yanap.database.UserAvailabilityRepository;
import com.topdesk.yanap.database.UserRepository;
import com.topdesk.yanap.misc.Mapper;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YanapServlet {
	
	private final SprintRepository sprints;
	private final UserAvailabilityRepository availabilities;
	private final UserRepository users;
	private final Mapper mapper;

	@RequestMapping(value = "/boards/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SprintAndUsers doGetSingleSprint(@PathVariable long id) {

		log.info("Get single sprint: " + id);
		
		Sprint sprint = sprints.findOne(id);
		if (sprint == null) {
			throw new ResourceNotFoundException("no sprint with id " + id);
		}
		return mapSprintToSprintAndUsers(sprint);
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
		sprintAndUsers.setDevelopers(sprint.getUsers().stream()
				.filter(User::isDeveloper)
				.map(user -> mapper.mapUserToUserAndDays(user, mapper.mapDateToLocalDate(sprint.getStartDate()), mapper.mapDateToLocalDate(sprint.getEndDate())))
				.collect(Collectors.toList()));
		sprintAndUsers.setNondevelopers(sprint.getUsers().stream()
				.filter(user -> !user.isDeveloper())
				.map(user -> mapper.mapUserToUserAndDays(user, mapper.mapDateToLocalDate(sprint.getStartDate()), mapper.mapDateToLocalDate(sprint.getEndDate())))
				.collect(Collectors.toList()));
		return sprintAndUsers;
	}
	
	@RequestMapping(value = "/boards", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void doCreateSprint(@RequestBody Sprint newSprint) {
		log.info("Create Sprint: {}", newSprint);
		newSprint.setStatus(1);
		sprints.save(newSprint);
	}
	
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
