package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface UserBySprintDao {
	List<UserBySprint> getAll();
	void deleteBySprint(Sprint sprint);
	List<UserBySprint> createForSprint(long sprintId, List<User> users);
}
