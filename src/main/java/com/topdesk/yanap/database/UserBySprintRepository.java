package com.topdesk.yanap.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@Deprecated
@RepositoryRestResource
public interface UserBySprintRepository extends CrudRepository<UserBySprint, Long> {

}
