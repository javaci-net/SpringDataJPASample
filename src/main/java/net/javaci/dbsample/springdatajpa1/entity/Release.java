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
}
