package com.topdesk.yanap.database;

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
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name = "User.getByTeam", query = "SELECT u FROM User u WHERE u.team = :team OR u.team = '' ORDER BY name"),
	@NamedQuery(name = "User.getAll", query = "SELECT u FROM User u ORDER BY name"),
	@NamedQuery(name = "User.getByIdList", query = "SELECT u FROM User u WHERE u.id in :ids ORDER BY name"),
})
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "isDeveloper")
	private boolean isDeveloper;

	@Column(name = "team")
	private String team;
}
