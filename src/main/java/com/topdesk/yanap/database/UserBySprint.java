package com.topdesk.yanap.database;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by stephaniep on 17.05.2016.
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "usersbysprint")
@NamedQueries({
	@NamedQuery(name = "UserBySprint.getBySprint", query = "SELECT s FROM UserBySprint s WHERE s.sprint = :sprint ORDER BY user.name"),
	@NamedQuery(name = "UserBySprint.getSingleBySprint", query = "SELECT s FROM UserBySprint s WHERE s.sprint.id = :sprintId AND s.user.id = :userId")
})
public class UserBySprint {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	@ManyToOne
	@JoinColumn(name = "sprintid")
	private Sprint sprint;
	
	// ugh, I know this is ugly. This is what I get for being lazy on initial design...
	@Column(name="day01")
	private Float day01 = 0.0F;
	@Column(name="day02")
	private Float day02 = 0.0F;
	@Column(name="day03")
	private Float day03 = 0.0F;
	@Column(name="day04")
	private Float day04 = 0.0F;
	@Column(name="day05")
	private Float day05 = 0.0F;
	@Column(name="day06")
	private Float day06 = 0.0F;
	@Column(name="day07")
	private Float day07 = 0.0F;
	@Column(name="day08")
	private Float day08 = 0.0F;
	@Column(name="day09")
	private Float day09 = 0.0F;
	@Column(name="day10")
	private Float day10 = 0.0F;
	@Column(name="day11")
	private Float day11;
	@Column(name="day12")
	private Float day12;
	@Column(name="day13")
	private Float day13;
	@Column(name="day14")
	private Float day14;
	@Column(name="day15")
	private Float day15;
	@Column(name="day16")
	private Float day16;
	@Column(name="day17")
	private Float day17;
	@Column(name="day18")
	private Float day18;
	@Column(name="day19")
	private Float day19;
	@Column(name="day20")
	private Float day20;
	
	public UserBySprint(Sprint sprint, User user) {
		this.sprint = sprint;
		this.user = user;
		setDaysBySprintStartAndEnd();
	}
	
	private void setDaysBySprintStartAndEnd() {
		long days = getWorkDays(sprint.getStartDate(), sprint.getEndDate());
		for (int i = 0; i < days; i++) {
			setDayByIndex(i, 0.0F);
		}
	}
	
	private long getWorkDays(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		
		int workDays = 0;
		
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}
		
		do {
			//excluding start date
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date
		
		return workDays;
	}
	
	public void setDayByIndex(int index, float value) {
		// this ought to be possible to do more simply...
		switch (index) {
			case 0:
				setDay01(value);
				break;
			case 1:
				setDay02(value);
				break;
			case 2:
				setDay03(value);
				break;
			case 3:
				setDay04(value);
				break;
			case 4:
				setDay05(value);
				break;
			case 5:
				setDay06(value);
				break;
			case 6:
				setDay07(value);
				break;
			case 7:
				setDay08(value);
				break;
			case 8:
				setDay09(value);
				break;
			case 9:
				setDay10(value);
				break;
			case 10:
				setDay11(value);
				break;
			case 11:
				setDay12(value);
				break;
			case 12:
				setDay13(value);
				break;
			case 13:
				setDay14(value);
				break;
			case 14:
				setDay15(value);
				break;
			case 15:
				setDay16(value);
				break;
			case 16:
				setDay17(value);
				break;
			case 17:
				setDay18(value);
				break;
			case 18:
				setDay19(value);
				break;
			case 19:
				setDay20(value);
				break;
		}
	}
}
