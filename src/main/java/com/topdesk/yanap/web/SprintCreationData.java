package com.topdesk.yanap.web;

import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.User;

/**
 * Created by stephaniep on 10.06.2016.
 */
@Getter
@EqualsAndHashCode
@ToString
public class SprintCreationData {
	String name;
	String team;
	Date startDate;
	Date endDate;
	int pointsPlanned;
	int pointsCompleted;
	List<User> users;

	public Sprint getSprint() {
		Sprint sprint = new Sprint();
		sprint.setName(name);
		sprint.setEndDate(endDate);
		sprint.setStartDate(startDate);
		sprint.setStatus(1);
		sprint.setPointsPlanned(pointsPlanned);
		sprint.setPointsCompleted(pointsCompleted);
		sprint.setTeam(team);

		return sprint;
	}
}
