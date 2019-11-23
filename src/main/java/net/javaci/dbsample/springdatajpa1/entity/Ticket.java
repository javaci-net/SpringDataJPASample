package net.javaci.dbsample.springdatajpa1.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	/*--
	// @JoinTable(name = "ticket_application")
	@JoinTable(
			name = "ticket_application",
			joinColumns =  @JoinColumn(name="ticket_fk"),
			inverseJoinColumns = @JoinColumn(name = "application_fk")
	)
	*/
	@ManyToOne(fetch = FetchType.LAZY)
	private Application application;
	
	@Transient
	private int initialLengthDescription;

	public Ticket() {
		super();
	}
	
	public Ticket(String description, String status, String title, LocalDate createDate, LocalDateTime createDateTime) {
		super();
		this.description = description;
		this.status = status;
		this.title = title;
		this.createDate = createDate;
		this.createDateTime = createDateTime;
	}
	
	public Ticket(String description, String status, String title, LocalDate createDate, LocalDateTime createDateTime,
			Application application) {
		super();
		this.description = description;
		this.status = status;
		this.title = title;
		this.createDate = createDate;
		this.createDateTime = createDateTime;
		this.application = application;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public int getInitialLengthDescription() {
		return initialLengthDescription;
	}

	public void setInitialLengthDescription(int initialLengthDescription) {
		this.initialLengthDescription = initialLengthDescription;
	}
	
}
