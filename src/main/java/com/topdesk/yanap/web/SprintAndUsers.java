package com.topdesk.yanap.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.UserBySprint;

/**
 * Created by stephaniep on 01.06.2016.
 */
@Getter
@EqualsAndHashCode
@ToString
public class SprintAndUsers {
	@Getter
	@EqualsAndHashCode
	@ToString
	class UserAndDays {
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

	SprintAndUsers(List<UserBySprint> users) {
		if (!users.isEmpty()) {
			this.developers = new ArrayList<>();
			this.nondevelopers = new ArrayList<>();
			Sprint sprint = users.get(0).getSprint();
			this. id = sprint.getId();
			this.name = sprint.getName();
			this.startDate = sprint.getStartDate();
			this.endDate = sprint.getEndDate();
			this.status = sprint.getStatus();
			this.pointsCompleted = sprint.getPointsCompleted();
			this.pointsPlanned = sprint.getPointsPlanned();
			this.team = sprint.getTeam();

			for (UserBySprint user : users) {
				UserAndDays userAndDays = new UserAndDays();
				userAndDays.id = user.getUser().getId();
				userAndDays.name = user.getUser().getName();
				userAndDays.days = new ArrayList<>(10);
				// yuck:
				userAndDays.days.add(user.getDay01());
				userAndDays.days.add(user.getDay02());
				userAndDays.days.add(user.getDay03());
				userAndDays.days.add(user.getDay04());
				userAndDays.days.add(user.getDay05());
				userAndDays.days.add(user.getDay06());
				userAndDays.days.add(user.getDay07());
				userAndDays.days.add(user.getDay08());
				userAndDays.days.add(user.getDay09());
				userAndDays.days.add(user.getDay10());
				if (user.getDay11() != null) {
					userAndDays.days.add(user.getDay11());
				}
				if (user.getDay12() != null) {
					userAndDays.days.add(user.getDay12());
				}
				if (user.getDay13() != null) {
					userAndDays.days.add(user.getDay13());
				}
				if (user.getDay14() != null) {
					userAndDays.days.add(user.getDay14());
				}
				if (user.getDay15() != null) {
					userAndDays.days.add(user.getDay15());
				}
				if (user.getDay16() != null) {
					userAndDays.days.add(user.getDay16());
				}
				if (user.getDay17() != null) {
					userAndDays.days.add(user.getDay17());
				}
				if (user.getDay18() != null) {
					userAndDays.days.add(user.getDay18());
				}
				if (user.getDay19() != null) {
					userAndDays.days.add(user.getDay19());
				}
				if (user.getDay20() != null) {
					userAndDays.days.add(user.getDay20());
				}
				
				if (user.getUser().isDeveloper()) {
					developers.add(userAndDays);
				} else {
					nondevelopers.add(userAndDays);
				}
			}
		}
	}

	public Sprint getSprint() {
		Sprint sprint = new Sprint();
		sprint.setId(id);
		sprint.setName(name);
		sprint.setEndDate(endDate);
		sprint.setStartDate(startDate);
		sprint.setStatus(status);
		sprint.setPointsPlanned(pointsPlanned);
		sprint.setPointsCompleted(pointsCompleted);
		sprint.setTeam(team);

		return sprint;
	}
}
