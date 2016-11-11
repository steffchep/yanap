package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface SprintDao {
	public static final String CONTEXT_NAME = "SprintDao";

	List<Sprint> getAll();
	Sprint getById(long id);
	List<Sprint> getByTeam(String team);
	Sprint update(Sprint sprint);
	void delete(long id);
	Sprint create(Sprint newSprint);
}
