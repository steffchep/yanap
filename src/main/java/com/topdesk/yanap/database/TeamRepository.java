package com.topdesk.yanap.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TeamRepository extends CrudRepository<Team, Long> {
}
