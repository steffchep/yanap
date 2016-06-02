package com.topdesk.yanap.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.UserBySprint;

/**
 * Created by stephaniep on 01.06.2016.
 */
@Getter
public class SprintAndUsers {
	@Getter
	class UserAndDays {
		long id;
		String name;
		List<Float> days;
	}
	long id;
	String name;
	Date startDate;
	Date endDate;
	int status;
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
				if (user.getUser().isDeveloper()) {
					developers.add(userAndDays);
				} else {
					nondevelopers.add(userAndDays);
				}
			}
		}
	}
}
