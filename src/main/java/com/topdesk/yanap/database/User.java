package com.topdesk.yanap.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "isDeveloper", nullable = false)
	private boolean developer;

	@Column(name = "team")
	private String team;
}
