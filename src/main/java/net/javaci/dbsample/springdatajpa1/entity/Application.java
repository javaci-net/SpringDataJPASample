package net.javaci.dbsample.springdatajpa1.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Application {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	@Column(length = 2000)
	public String description;
	
	@Column(name = "app_name", nullable = false)
	public String name;

	public String owner;
    
    
	/*--
	@OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
			name = "ticket_application",
			joinColumns =  @JoinColumn(name="application_fk"),
			inverseJoinColumns = @JoinColumn(name = "ticket_fk")
	)
	*/
	@OneToMany(mappedBy = "application")
    public List<Ticket> tickets = new ArrayList<Ticket>();
    
    // On the target side, we only have to provide 
 	// the name , which maps the relationship
 	@ManyToMany(mappedBy = "deployedApplications")
 	private Set<Release> releasesToDeploy;
}
