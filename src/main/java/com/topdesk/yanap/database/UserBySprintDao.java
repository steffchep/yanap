package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface UserBySprintDao {
	public static final String CONTEXT_NAME = "UserBySprintDao";

	List<UserBySprint> getAllForSprint(Sprint sprint);
	UserBySprint saveAvailability(long sprintId, long userId, int index, float value);
	void deleteBySprint(long sprint);
	List<UserBySprint> createForSprint(Sprint sprint, List<User> users);
}
