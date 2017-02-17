package com.topdesk.yanap.misc;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topdesk.yanap.database.User;
import com.topdesk.yanap.database.UserAvailability;
import com.topdesk.yanap.database.UserAvailability.Presence;
import com.topdesk.yanap.database.UserAvailabilityRepository;
import com.topdesk.yanap.database.UserBySprint;
import com.topdesk.yanap.web.SprintAndUsers.UserAndDays;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Mapper {
	private static final ZoneId TIME_ZONE = ZoneId.of("Europe/Berlin");
	
	private final UserAvailabilityRepository availabilities;
	
	public Float mapAvailabilityToFloat(UserAvailability availability) {
		if (availability == null) {
			return 0f;
		}
		if (availability.getPresence() == Presence.IN_OFFICE) {
			float rate = availability.getAvailability();
			return rate < 0.1 ? 3f : rate;
		} else {
			if (availability.isPlanned()) {
				return 2f;
			} else {
				return 4f;
			}
		}
	}
	
	public float mapFloatToAvailability(Float av) {
		if (av > .9 && av < 1.1) {
			return 1f;
		} else if (av > 0.4 && av < .6) {
			return 0.5f;
		} else {
			return 0f;
		}
	}
	
	public Presence mapFloatToPresence(Float av) {
		if (av > .4 && av < 1.1 || av > 2.9 && av < 3.1) {
			return Presence.IN_OFFICE;
		} else {
			return Presence.OOO;
		}
	}
	
	public boolean mapFloatToPlanned(Float av) {
		return av < 3.9 || av > 4.1;
	}
	
	public List<Float> mapUserBySprintToListOfFloats(UserBySprint userBySprint) {
		return Stream.of(
				userBySprint.getDay01(), userBySprint.getDay02(), userBySprint.getDay03(), userBySprint.getDay04(),
				userBySprint.getDay05(), userBySprint.getDay06(), userBySprint.getDay07(), userBySprint.getDay08(),
				userBySprint.getDay09(), userBySprint.getDay10(), userBySprint.getDay11(), userBySprint.getDay12(),
				userBySprint.getDay13(), userBySprint.getDay14(), userBySprint.getDay15(), userBySprint.getDay16(),
				userBySprint.getDay17(), userBySprint.getDay19(), userBySprint.getDay18())
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
	
	public LocalDate mapDateToLocalDate(Date date) {
		return date.toInstant().atZone(Mapper.TIME_ZONE).toLocalDate();
	}
	
	public UserAndDays mapUserToUserAndDays(User user, LocalDate sprintStart, LocalDate sprintEnd) {
		UserAndDays userAndDays = new UserAndDays();
		userAndDays.setId(user.getId());
		userAndDays.setName(user.getName());
		ArrayList<Float> days = new ArrayList<>();
		userAndDays.setDays(days);
		
		Map<LocalDate, UserAvailability> availabilities = this.availabilities.findByUserAndDayBetween(user, sprintStart, sprintEnd).stream()
				.collect(Collectors.toMap(UserAvailability::getDay, Function.identity()));
		
		for (LocalDate today = sprintStart; !today.isAfter(sprintEnd); today = nextWorkDay(today)) {
			days.add(mapAvailabilityToFloat(availabilities.get(today)));
		}
		return userAndDays;
	}
	
	public LocalDate nextWorkDay(LocalDate day) {
		LocalDate nextDay = day.plus(1, ChronoUnit.DAYS);
		while (nextDay.getDayOfWeek() == DayOfWeek.SATURDAY || nextDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
			nextDay = nextDay.plus(1, ChronoUnit.DAYS);
		}
		return nextDay;
	}
	
	public UserAvailability mapToUserAvailability(User user, LocalDate day, float value) {
		UserAvailability availability = availabilities.findByUserAndDay(user, day);
		if (availability == null) {
			availability = new UserAvailability();
			availability.setUser(user);
			availability.setDay(day);
		}
		availability.setAvailability(mapFloatToAvailability(value));
		availability.setPresence(mapFloatToPresence(value));
		availability.setPlanned(mapFloatToPlanned(value));
		return availability;
	}
}
