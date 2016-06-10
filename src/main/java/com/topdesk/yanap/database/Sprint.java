package com.topdesk.yanap.database;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
@Table(name = "sprints")
@NamedQueries({
	@NamedQuery(name = "Sprint.getAll", query = "SELECT s FROM Sprint s ORDER BY startDate DESC"),
	@NamedQuery(name = "Sprint.getByTeam", query = "SELECT s FROM Sprint s WHERE s.team = :team ORDER BY startDate DESC" )
})
public class Sprint {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "endDate")
	private Date endDate;

	@Column(name = "status")
	private int status;

	@Column(name = "team")
	private String team;

	@Column(name = "pointsplanned")
	private int pointsPlanned;

	@Column(name = "pointscompleted")
	private int pointsCompleted;

}
