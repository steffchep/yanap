package com.topdesk.yanap.database;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import com.topdesk.yanap.misc.Mapper;

public interface UserAvailabilityRepository extends CrudRepository<UserAvailability, UUID> {
	
	Collection<UserAvailability> findByUserAndDayBetween(
			@Param("user") User user,
			@Param("from") @DateTimeFormat(pattern = Mapper.LENIENT_ISO_DATE_PATTERN) LocalDate from,
			@Param("to") @DateTimeFormat(pattern = Mapper.LENIENT_ISO_DATE_PATTERN) LocalDate to);

	Collection<UserAvailability> findByUserInAndDayBetween(
			Collection<User> user,
			LocalDate from,
			LocalDate to);

	UserAvailability findByUserAndDay(User user, LocalDate day);
}
