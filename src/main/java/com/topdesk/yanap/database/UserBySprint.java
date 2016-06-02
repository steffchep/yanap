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
	@NamedQuery(name = "UserBySprint.getById", query = "SELECT s FROM UserBySprint s WHERE s.sprint = :sprint ORDER BY user.name")
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

}
