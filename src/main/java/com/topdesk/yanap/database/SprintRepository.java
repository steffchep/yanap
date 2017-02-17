package com.topdesk.yanap.database;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SprintRepository extends PagingAndSortingRepository<Sprint, Long> {
	@Query("Select sprints from Sprint sprints where sprints.team = :team and sprints.endDate >= current_timestamp and sprints.startDate <= current_timestamp ")
	Collection<Sprint> findCurrent(@Param("team") String team);
}
