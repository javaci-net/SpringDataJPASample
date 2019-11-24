package net.javaci.dbsample.springdatajpa1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import net.javaci.dbsample.springdatajpa1.dao.impl.ReleaseDAOImpl;
import net.javaci.dbsample.springdatajpa1.dao.impl.TicketDAOImpl;
import net.javaci.dbsample.springdatajpa1.dao.springdata.ApplicationDAOCrudRepositoryImpl;
import net.javaci.dbsample.springdatajpa1.entity.Application;
import net.javaci.dbsample.springdatajpa1.entity.Release;
import net.javaci.dbsample.springdatajpa1.entity.Ticket;
import net.javaci.dbsample.springdatajpa1.entity.dto.ApplicationDTO;
import net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO;

@SpringBootApplication
public class AppMain implements CommandLineRunner {

	@PersistenceContext private EntityManager entityManager;
	
	@Autowired private TicketDAOImpl ticketDAO;
	
	@Autowired private ApplicationDAOCrudRepositoryImpl applicationDAO;
	
	@Autowired private ReleaseDAOImpl releaseDAO;
	
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
		
		testReadWithDTO();
		
		testUpdate();
		
		testRemove();
		
		testSpringDataMethods();
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
		Application facebookAdminApp = new Application("Facebook Admin App", "Facebook Admin", "dogancan");
		log.info("** Adding facebookAdminApp: {}", facebookAdminApp);
		applicationDAO.addApplication(facebookAdminApp);
		Application demoApp = new Application("Temporary Demo Application", "Demo App", "ozkan");
		log.info("** Adding demoApp: {}", demoApp);
		applicationDAO.addApplication(demoApp);
		
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
		// Application app1 = applicationDAO.getApplicationById(1);
		
		Application app1 = applicationDAO.getApplicationWithTicketsAndReleases(1);
		
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
	
	private void testReadWithDTO() {
		
		log.info( ">> TEST READ WITH DTO >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		List<TicketStatsByStatusDTO> ticketStats = ticketDAO.findTicketStats();
		ticketStats.forEach(ts->log.info("** Status: {}, Count: {}, Min Creation: {}, Max Creation: {} ", 
				ts.getStatus(), ts.getCount(), ts.getMaxCreateDateTime(), ts.getMaxCreateDateTime()));
		
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
		
		// Gets lazy init exception
		// Application application = applicationDAO.getApplicationById(1);
		
		ApplicationDTO application = applicationDAO.getApplicationWithTicketsAndReleasesV3(1);
		
		Integer ticketId = application.getLastTicketId();
		ticketDAO.removeTicket(ticketDAO.getTicketById(ticketId));

		Ticket removedTicket = ticketDAO.getTicketById(ticketId);
		log.info("Is removed? {}", removedTicket==null);
		
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}
	
	private void testSpringDataMethods() {
		
		log.info( ">> TEST SPRING DATA METHODS >>>>>>>>>>>>>>>>>>>>>>>>>> ");
		
		log.info("ALL APPLICATIONS: ");
		
		// SQL: select count(*) as col_0_0_ from application applicatio0_
		long countAllApps = applicationDAO.count();
		log.info("--> Count of all apps: {} ", countAllApps);
		
		// SQL: select ... from application applicatio0_
		log.info("--> All Apps in creation order:");
		Iterable<Application> allApplications = applicationDAO.findAll();
		allApplications.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: select ... from application applicatio0_ order by applicatio0_.app_name asc
		log.info("--> All Apps in name order (asc) ");
		Iterable<Application> allApplicationsSorted = applicationDAO.findAll(Sort.by("name").ascending());
		allApplicationsSorted.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: select ... from application applicatio0_ order by applicatio0_.id desc limit ?
		log.info("--> Top 1 Apps Ordered By Id Desc (Last) ");
		Application lastAppCreated = applicationDAO.findFirstByOrderByIdDesc();
		log.info("** Last app created: {} ", lastAppCreated.getName());
				
		// SQL: select ... from application applicatio0_ order by applicatio0_.id desc limit ?
		log.info("--> Top 2 Apps Ordered By Id Desc (Last 2) ");
		Iterable<Application> last2AppsCreated = applicationDAO.findTop2ByOrderByIdDesc();
		last2AppsCreated.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL 1: select ... from application applicatio0_ order by applicatio0_.app_name asc limit ?, ?
		// SQL 2: select count(applicatio0_.id) as col_0_0_ from application applicatio0_
		log.info("--> Only first 2 apps in name order (asc) ");
		Page<Application> allApplicationsSortedFirst2 =  applicationDAO.findAll(PageRequest.of(0, 2, Sort.by("name").ascending()));
		log.info("** Page {} of {}", allApplicationsSortedFirst2.getNumber(), allApplicationsSortedFirst2.getTotalPages());
		allApplicationsSortedFirst2.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: select count(*) as col_0_0_ from application applicatio0_ where applicatio0_.id=?
		boolean existsappWithID1 = applicationDAO.existsById(1);
		log.info("App with ID 1 Exists? {} ", existsappWithID1);
		
		// SQL: select ... from application applicatio0_ where applicatio0_.id=?
		Optional<Application> appWithID1 = applicationDAO.findById(1);
		log.info("App with ID 1 Description: {} ", appWithID1.get().getDescription());
		
		// SQL: select count(applicatio0_.id) as col_0_0_ from application applicatio0_ where applicatio0_.owner=?
		long countAppsOfDogancan = applicationDAO.countByOwner("dogancan");
		log.info("Number of Dogancan's apps: {} ", countAppsOfDogancan);
		
		// SQL: select ... from application applicatio0_ where applicatio0_.owner=?
		log.info("DOGANCAN'S APPLICATIONS: ");
		List<Application> appsOfDogancan1 = applicationDAO.findByOwner("dogancan");
		appsOfDogancan1.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: select ... from application applicatio0_ where applicatio0_.owner=? order by applicatio0_.app_name asc
		log.info("DOGANCAN'S APPLICATIONS (ORDERED BY APP NAME ASC): ");
		List<Application> appsOfDogancan2a = applicationDAO.findByOwnerOrderByNameAsc("dogancan");
		appsOfDogancan2a.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: select ...from application applicatio0_ where applicatio0_.owner=? order by applicatio0_.app_name desc
		log.info("DOGANCAN'S APPLICATIONS (ORDERED BY APP NAME DESC): ");
		List<Application> appsOfDogancan2b = applicationDAO.findByOwner("dogancan", Sort.by("name").descending());
		appsOfDogancan2b.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL 1: select ... from application applicatio0_ where applicatio0_.owner=? order by applicatio0_.app_name desc limit ?, ?
		// SQL 2: select count(applicatio0_.id) as col_0_0_ from application applicatio0_ where applicatio0_.owner=?
		log.info("DOGANCAN'S APPLICATIONS (2nd RECORD AND ORDERED BY APP NAME DESC): ");
		Page<Application> appsOfDogancan2c = applicationDAO.findByOwner("dogancan", PageRequest.of(1, 1, Sort.by("name").descending()) );
		appsOfDogancan2c.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: SELECT distinct applicatio0_.id as id1_0_, ... FROM application applicatio0_ WHERE applicatio0_.owner=?
		log.info("DOGANCAN'S APPLICATIONS (DISTINCT): ");
		List<Application> appsOfDogancan3 = applicationDAO.findDistinctByOwner("dogancan");
		appsOfDogancan3.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: SELECT ... FROM application applicatio0_ WHERE upper(applicatio0_.owner)=upper(?)
		log.info("DOGANCAN'S APPLICATIONS (Ignore Case): ");
		List<Application> appsOfDogancan4 = applicationDAO.findByOwnerIgnoreCase("DOGanCan");
		appsOfDogancan4.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: SELECT ... FROM application applicatio0_ WHERE upper(applicatio0_.owner)=upper(?) or upper(applicatio0_.app_name)=upper(?)
		log.info("DOGANCAN'S APPLICATIONS (Search by Owner or name): ");
		List<Application> appsOfDogancan5 = applicationDAO.findByOwnerOrNameAllIgnoreCase("dogancan", "Personal App");
		appsOfDogancan5.forEach(aod->log.info("** Name: {}", aod.getName() ) );
		
		// SQL: 
		log.info("DOGANCAN'S LAST APPLICATION");
		Application lastAppOfDogancan6 = applicationDAO.findFirstByOwnerOrNameAllIgnoreCaseOrderByIdDesc("dogancan", "Personal App");
		log.info("** Description: {} ", lastAppOfDogancan6.getDescription());
		
		// SQL: select ... from application applicatio0_ where applicatio0_.app_name=?
		log.info("FINDING APPLICATION BY NAME: ");
		Application facebookWebApp = applicationDAO.findByName("Facebook.com");
		log.info("** Facebook.com Description: {} ", facebookWebApp.getDescription());
		
		// delete from application where id=?
		long deletedCount = applicationDAO.deleteByName("Demo App");
		log.info("Demo App Deleted ? {}, Deleted Count: {}", deletedCount >= 1, deletedCount);
		
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
