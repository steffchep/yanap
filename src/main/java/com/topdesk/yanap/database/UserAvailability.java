package com.topdesk.yanap.database;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
//	@RestResource(exported = false)
	private User user;
	private LocalDate day;
	
	private Presence presence;
	
	private float availability;
	
	private boolean planned;
	
	public enum Presence {
		IN_OFFICE, OOO
	}
}
