package net.javaci.dbsample.springdatajpa1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
		
		testReadWithIdPrimaryKey();
		
		testReadWithJpql();
		
		testReadWithCriteria();
		
		testUpdate();
		
		testRemove();
		
	}

	private void testPersist() {
		
		log.info( ">> TEST PERSIST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		Application facebookWebApp = new Application("Facebook Web App", "Facebook.com", "volkan");
		log.info("** Adding facebookWebApp: {}", facebookWebApp);
		applicationDAO.addApplication(facebookWebApp);
		Application facebookCoreSystemApp = new Application("Facebook Core System", "Facebook Core", "koray");
		log.info("** Adding facebookCoreSystemApp: {}", facebookCoreSystemApp);
		applicationDAO.addApplication(facebookCoreSystemApp);
		Application facebookMobileApp = new Application("Facebook Mobile App", "Facebook Mobile", "dogancan");
		log.info("** Adding facebookMobileApp: {}", facebookMobileApp);
		applicationDAO.addApplication(facebookMobileApp);
		
		Ticket ticket1 = new Ticket("Login failed when empty", "OPEN", "Login Bug", LocalDate.now(), LocalDateTime.now(), facebookWebApp);
		log.info("** Adding ticket1: {}", ticket1);
		ticketDAO.addTicket(ticket1);
		Ticket ticket2 = new Ticket("Password reminder not working", "CLOSED", "Pwd Remind Bug", LocalDate.now(), LocalDateTime.now(), facebookWebApp);
		log.info("** Adding ticket2: {}", ticket2);
		ticketDAO.addTicket(ticket2);
		
		Set<Application> deployedApplications1 = new HashSet<Application>();
		deployedApplications1.add(facebookWebApp);
		deployedApplications1.add(facebookCoreSystemApp);
		Release release1 = new Release("v1", LocalDateTime.now().plusDays(10), deployedApplications1);
		log.info("** Adding release1: {}", release1);
		releaseDAO.addRelease(release1);
		
		Set<Application> deployedApplications2 = new HashSet<Application>();
		deployedApplications2.add(facebookWebApp);
		deployedApplications2.add(facebookMobileApp);
		Release release2 = new Release("v2", LocalDateTime.now().plusDays(10), deployedApplications2);
		log.info("** Adding release2: {}", release2);
		releaseDAO.addRelease(release2);
		
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}

	private void testReadWithIdPrimaryKey() {
		
		log.info( ">> TEST READ >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		// Gets no lazy exception because spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
		// How to handle Lazy? https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
		Application app1 = applicationDAO.getApplicationById(1);
		log.info("Name: {}", app1.getName());
		List<Ticket> tickets = app1.getTickets();
		tickets.forEach(t->log.info("** Ticket Title: {}", t.getTitle()));
		Set<Release> releasesToDeploy = app1.getReleasesToDeploy();
		releasesToDeploy.forEach(r->log.info("** Release name: {}", r.getName()));
		
		
	}
	
	private void testReadWithJpql() {
		
		log.info( ">> TEST READ WITH JPQL >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		String name = "Facebook.com";
		String owner = "volkan";
		boolean appExists = applicationDAO.applicationExists(name, owner);
		log.info("Is app exists with name {} and owner {} ? {}", name, owner, appExists);
		
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}
	
	
	private void testReadWithCriteria() {
		
		log.info( ">> TEST READ WITH CRITERIA >>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		String name = "Facebook.com";
		String owner = "volkan";
		boolean appExists = applicationDAO.applicationReallyExists(name, owner);
		log.info("Is app exists with name {} and owner {} ? {}", name, owner, appExists);
		
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}
	
	private void testUpdate() {
		
		log.info( ">> TEST UPDATE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		Integer id = 1;
		String newName = "Facebook.com";
		String newOwner = "volkan";
		boolean updateSuccess = applicationDAO.updateNameAndOwnerById(id, newName, newOwner);
		
		Application updated = applicationDAO.getApplicationById(1);
		
		log.info("Is updated? {}, new name: {}, new owner: {}", 
				updateSuccess, updated.getName(), updated.getOwner());
		
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
		
	}
	
	private void testRemove() {
		
		log.info( ">> TEST REMOVE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		Application application = applicationDAO.getApplicationById(1);
		
		List<Ticket> tickets = application.getTickets();
		List<Integer> ticketIdList = tickets.stream().mapToInt(t->t.getId()).boxed().collect(Collectors.toList());
		ticketDAO.removeTickets(tickets);

		boolean stillNotRemoved = false;
		for (Integer ticketId : ticketIdList) {
			Ticket removedTicket = ticketDAO.getTicketById(ticketId);
			stillNotRemoved = (removedTicket==null);
		}
		
		log.info("Is removed? {}", stillNotRemoved);
		
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}
	
	private void printDBInfo() {
		EntityManagerFactory emf = entityManager.getEntityManagerFactory();     
		Map<String, Object> emfPropertiesMap = emf.getProperties();
		log.info( ">> EMF PROPERTIES >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		for (Map.Entry<String, Object> entry : emfPropertiesMap.entrySet()) {
			log.info("** " + entry.getKey() + " : " + entry.getValue());
		}
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}

}
