package com.topdesk.yanap.database;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserBySprintRepository extends CrudRepository<UserBySprint, Long> {
	@Query("select users from UserBySprint users where users.sprint.team = :team and users.sprint.endDate >= current_timestamp and users.sprint.startDate <= current_timestamp ")
	Collection<UserBySprint> findCurrent(@Param("team") String team);
}
