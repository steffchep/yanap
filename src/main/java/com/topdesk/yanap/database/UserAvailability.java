package com.topdesk.yanap.database;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(
		name = "availability",
		uniqueConstraints= {
				@UniqueConstraint(columnNames = {"user_id", "day"})
		})
public class UserAvailability {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private LocalDate day;
	
	private Presence presence;
	
	private float availability;
	
	private boolean planned;
	
	public enum Presence {
		IN_OFFICE, OOO
	}
}
