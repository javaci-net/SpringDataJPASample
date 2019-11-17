package net.javaci.dbsample.springdatajpa1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.javaci.dbsample.springdatajpa1.dao.ApplicationDAO;
import net.javaci.dbsample.springdatajpa1.dao.ReleaseDAO;
import net.javaci.dbsample.springdatajpa1.dao.TicketDAO;
import net.javaci.dbsample.springdatajpa1.entity.Application;
import net.javaci.dbsample.springdatajpa1.entity.Release;
import net.javaci.dbsample.springdatajpa1.entity.Ticket;

@SpringBootApplication
public class AppMain implements CommandLineRunner {

	@PersistenceContext private EntityManager entityManager;
	
	@Autowired private TicketDAO ticketDAO;
	
	@Autowired private ApplicationDAO applicationDAO;
	
	@Autowired private ReleaseDAO releaseDAO;
	
	private static final Logger log = LoggerFactory.getLogger(AppMain.class);
	
	public static void main(String[] args) {
		SpringApplication.run(AppMain.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		printDBInfo();
		
		testPersist();
		
		testRead();
		
	}

	private void testPersist() {
		
		Application facebookWebApp = new Application("Facebook Web App", "Facebook.com", "volkan");
		applicationDAO.addApplication(facebookWebApp);
		Application facebookCoreSystemApp = new Application("Facebook Core System", "Facebook Core", "koray");
		applicationDAO.addApplication(facebookCoreSystemApp);
		Application facebookMobileApp = new Application("Facebook Mobile App", "Facebook Mobile", "dogancan");
		applicationDAO.addApplication(facebookMobileApp);
		
		Ticket ticket1 = new Ticket("Login failed when empty", "OPEN", "Login Bug", LocalDate.now(), LocalDateTime.now(), facebookWebApp);
		ticketDAO.addTicket(ticket1);
		Ticket ticket2 = new Ticket("Password reminder not working", "OPEN", "Login Bug", LocalDate.now(), LocalDateTime.now(), facebookWebApp);
		ticketDAO.addTicket(ticket2);
		
		Set<Application> deployedApplications1 = new HashSet<Application>();
		deployedApplications1.add(facebookWebApp);
		deployedApplications1.add(facebookCoreSystemApp);
		Release release1 = new Release("v1", LocalDateTime.now().plusDays(10), deployedApplications1);
		releaseDAO.addRelease(release1);
		
		Set<Application> deployedApplications2 = new HashSet<Application>();
		deployedApplications2.add(facebookWebApp);
		deployedApplications2.add(facebookMobileApp);
		Release release2 = new Release("v2", LocalDateTime.now().plusDays(10), deployedApplications2);
		releaseDAO.addRelease(release2);
	}

	private void testRead() {
		
		Application app1 = applicationDAO.getApplicationById(1);
		log.info("Name: {}", app1.getName());
		List<Ticket> tickets = app1.getTickets();
		tickets.forEach(t->log.info("Ticket Title: {}", t.getTitle()));
		Set<Release> releasesToDeploy = app1.getReleasesToDeploy();
		releasesToDeploy.forEach(r->log.info("Release name: {}", r.getName()));
	}
	
	private void printDBInfo() {
		EntityManagerFactory emf = entityManager.getEntityManagerFactory();     
		Map<String, Object> emfPropertiesMap = emf.getProperties();
		log.info( ">> EMF PROPERTIES >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		for (Map.Entry<String, Object> entry : emfPropertiesMap.entrySet()) {
			log.info(entry.getKey() + " : " + entry.getValue());
		}
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}

}
