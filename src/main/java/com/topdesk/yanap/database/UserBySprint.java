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
	@NamedQuery(name = "UserBySprint.getById", query = "SELECT s FROM UserBySprint s WHERE s.sprint = :sprint")
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
	private int day01;
	@Column(name="day02")
	private int day02;
	@Column(name="day03")
	private int day03;
	@Column(name="day04")
	private int day04;
	@Column(name="day05")
	private int day05;
	@Column(name="day06")
	private int day06;
	@Column(name="day07")
	private int day07;
	@Column(name="day08")
	private int day08;
	@Column(name="day09")
	private int day09;
	@Column(name="day10")
	private int day10;

}
