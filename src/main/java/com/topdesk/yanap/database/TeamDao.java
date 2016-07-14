package com.topdesk.yanap.database;

import java.util.List;

/**
 * Created by stephaniep on 14.07.2016.
 */
public interface TeamDao {
	public static final String CONTEXT_NAME = "TeamDao";
	
	List<Team> getAll();
	Team delete(Team sprint);
	Team create(Team newTeam);
}
