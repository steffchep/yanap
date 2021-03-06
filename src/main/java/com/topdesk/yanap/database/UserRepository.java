package com.topdesk.yanap.database;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
	Collection<User> findByTeam(@Param("team") String teamName);
	Collection<User> findByNameStartsWith(@Param("prefix") String name);
}
