package com.topdesk.yanap.misc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.SprintRepository;
import com.topdesk.yanap.database.UserAvailabilityRepository;
import com.topdesk.yanap.database.UserBySprint;
import com.topdesk.yanap.database.UserBySprintRepository;

@SuppressWarnings("deprecation")
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataMigrator implements ApplicationListener<ContextRefreshedEvent> {
	
	private final UserBySprintRepository userBySprintRepo;
	private final SprintRepository sprintRepository;
	private final UserAvailabilityRepository userAvailabilityRepo;
	private final Mapper mapper;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("yanap started");
		long availabilityRepoCount = userAvailabilityRepo.count();
		if (availabilityRepoCount == 0) {
			long userSprintEntryCount = userBySprintRepo.count();
			if (userSprintEntryCount > 0) {
				log.info("migrating {} userBySprint entries", userSprintEntryCount);
				migrateEntries();
			} else {
				log.info("nothing to convert");
			}
		} else {
			log.info("already new-style data found, not converting anything");
		}
	}
	
	private void migrateEntries() {
		int migrationCounter = 0;
		for (UserBySprint userBySprint : userBySprintRepo.findAll()) {
			Sprint sprint = userBySprint.getSprint();
			sprint.getUsers().add(userBySprint.getUser());
			sprintRepository.save(sprint);
			LocalDate currentDay = mapper.mapDateToLocalDate(sprint.getStartDate()).minus(1, ChronoUnit.DAYS);
			for (Float av : mapper.mapUserBySprintToListOfFloats(userBySprint)) {
				currentDay = mapper.nextWorkDay(currentDay);
				if (av >0) {
					userAvailabilityRepo.save(mapper.mapToUserAvailability(userBySprint.getUser(), currentDay, av));
					migrationCounter++;
				}
			}
		}
		log.info("created {} availability entries", migrationCounter);
	}
	
}
