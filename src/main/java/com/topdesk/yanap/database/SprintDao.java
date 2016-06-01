package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 17.05.2016.
 */
public interface SprintDao {
	String CONTEXT_NAME = "SprintDao";

	List<Sprint> getAll();
	Sprint getById(long id);
	Sprint delete(Sprint sprint);
	Sprint create(Sprint newSprint);
}
