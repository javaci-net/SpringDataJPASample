package net.javaci.dbsample.springdatajpa1.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "apprelease") //  release is a special keyword in mysql @see https://forums.mysql.com/read.php?101,665004,665004
public class Release {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	private String name;
	
	private LocalDateTime releaseDateTime;
	
	@ManyToMany
	@JoinTable(name = "apprelease_application", 
		joinColumns = @JoinColumn(name = "release_fk"), 
		inverseJoinColumns = @JoinColumn(name = "application_fk")
	)
	private Set<Application> deployedApplications;

	public Release() {
		super();
	}
	
	public Release(String name, LocalDateTime releaseDateTime) {
		super();
		this.name = name;
		this.releaseDateTime = releaseDateTime;
	}
	
	public Release(String name, LocalDateTime releaseDateTime, Set<Application> deployedApplications) {
		super();
		this.name = name;
		this.releaseDateTime = releaseDateTime;
		this.deployedApplications = deployedApplications;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getReleaseDateTime() {
		return releaseDateTime;
	}

	public void setReleaseDateTime(LocalDateTime releaseDateTime) {
		this.releaseDateTime = releaseDateTime;
	}

	public Set<Application> getDeployedApplications() {
		return deployedApplications;
	}

	public void setDeployedApplications(Set<Application> deployedApplications) {
		this.deployedApplications = deployedApplications;
	}
	
	
}
