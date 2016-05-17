package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface UserDao {
	List<User> getForSprint(Sprint sprint);
	User delete(User user);
	User create(String name, boolean isDeveloper);
}
