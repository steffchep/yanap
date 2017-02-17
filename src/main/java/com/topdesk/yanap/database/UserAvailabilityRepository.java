package com.topdesk.yanap.database;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface UserAvailabilityRepository extends CrudRepository<UserAvailability, UUID> {
	Collection<UserAvailability> findByUserAndDayBetween(User user, LocalDate from, LocalDate to);
	UserAvailability findByUserAndDay(User user, LocalDate day);
}
