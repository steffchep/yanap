package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface UserDao {
	public static final String CONTEXT_NAME = "UserDao";

	User getById(long id);
	List<User> getByTeam(String team);
	List<User> getByIdList(List<Long> userIds);
	User create(User newUser);
}
