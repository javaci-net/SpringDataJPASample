package net.javaci.dbsample.springdatajpa1;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMain implements CommandLineRunner {

	@PersistenceContext
    private EntityManager entityManager;
	
	private static final Logger log = LoggerFactory.getLogger(AppMain.class);
	
	public static void main(String[] args) {
		SpringApplication.run(AppMain.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		printDBInfo();
	}

	private void printDBInfo() {
		EntityManagerFactory emf = entityManager.getEntityManagerFactory();     
		Map<String, Object> emfPropertiesMap = emf.getProperties();
		log.info( ">> EMF PROPERTIES >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		for (Map.Entry<String, Object> entry : emfPropertiesMap.entrySet()) {
			log.trace(entry.getKey()+ " : "+entry.getValue());
		}
		log.info( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	}

}
