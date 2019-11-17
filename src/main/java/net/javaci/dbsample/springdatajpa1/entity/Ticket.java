package net.javaci.dbsample.springdatajpa1.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

	@Column(name = "ticket_desc", length = 2000, nullable = true)
	private String description;
	private String status;
	private String title;
	private LocalDate createDate;
	private LocalDateTime createDateTime;
	
	@ManyToOne
	// @JoinTable(name = "ticket_application")
	@JoinTable(
			name = "ticket_application",
			joinColumns =  @JoinColumn(name="ticket_fk"),
			inverseJoinColumns = @JoinColumn(name = "application_fk")
	)
	private Application application;
	
	@Transient
	private int initialLengthDescription;
}
