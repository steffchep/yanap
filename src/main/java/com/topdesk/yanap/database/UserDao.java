package com.topdesk.yanap.database;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface UserDao {
	User getById(long id);
	User delete(User user);
	User create(String name, boolean isDeveloper);
}
