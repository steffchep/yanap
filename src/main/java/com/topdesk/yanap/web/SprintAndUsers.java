package com.topdesk.yanap.web;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SprintAndUsers {
	@Data
	public static class UserAndDays {
		long id;
		String name;
		List<Float> days;
	}
	long id;
	String name;
	String team;
	Date startDate;
	Date endDate;
	int status;
	int pointsPlanned;
	int pointsCompleted;
	List<UserAndDays> developers;
	List<UserAndDays> nondevelopers;

}
