package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface UserBySprintDao {
	String CONTEXT_NAME = "UserBySprintDao";
	List<UserBySprint> getAll(Sprint sprint);
	void deleteBySprint(Sprint sprint);
	List<UserBySprint> createForSprint(long sprintId, List<User> users);
}
