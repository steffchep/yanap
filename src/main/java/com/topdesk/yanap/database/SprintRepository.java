package com.topdesk.yanap.database;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SprintRepository extends PagingAndSortingRepository<Sprint, Long> {
}
