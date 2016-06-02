package com.topdesk.yanap.database;

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

	@Column(name="day01")
	private float day01;
	@Column(name="day02")
	private float day02;
	@Column(name="day03")
	private float day03;
	@Column(name="day04")
	private float day04;
	@Column(name="day05")
	private float day05;
	@Column(name="day06")
	private float day06;
	@Column(name="day07")
	private float day07;
	@Column(name="day08")
	private float day08;
	@Column(name="day09")
	private float day09;
	@Column(name="day10")
	private float day10;

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
		}
	}
}
