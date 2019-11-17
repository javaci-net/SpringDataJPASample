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
    
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
			name = "ticket_application",
			joinColumns =  @JoinColumn(name="application_fk"),
			inverseJoinColumns = @JoinColumn(name = "ticket_fk")
	)
    public List<Ticket> tickets = new ArrayList<Ticket>();
    
    // On the target side, we only have to provide 
 	// the name , which maps the relationship
 	@ManyToMany(mappedBy = "deployedApplications")
 	private Set<Release> releasesToDeploy;

 	public Application() {
 		super();
 	}
 	
 	public Application(String description, String name, String owner) {
		super();
		this.description = description;
		this.name = name;
		this.owner = owner;
	}
 	
	public Application(String description, String name, String owner, List<Ticket> tickets,
			Set<Release> releasesToDeploy) {
		super();
		this.description = description;
		this.name = name;
		this.owner = owner;
		this.tickets = tickets;
		this.releasesToDeploy = releasesToDeploy;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Set<Release> getReleasesToDeploy() {
		return releasesToDeploy;
	}

	public void setReleasesToDeploy(Set<Release> releasesToDeploy) {
		this.releasesToDeploy = releasesToDeploy;
	}

}
