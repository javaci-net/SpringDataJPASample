package net.javaci.dbsample.springdatajpa1.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.ApplicationDAO;
import net.javaci.dbsample.springdatajpa1.entity.Application;

@Transactional
@Repository
public class ApplicationDAOImpl implements ApplicationDAO {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
    public void addApplication(Application application) {
        entityManager.persist(application);
    }
	
	@Override
    public Application getApplicationById(int applicationId) {
        return entityManager.find(Application.class, applicationId);
    }
	
	@Override
    public boolean applicationExists(String name, String owner) {
        // note application is the class name; not the table name; 
		// class name is case sensitive; use class field names - column names
        String jpql = "from Application as a WHERE a.name = ? and a.owner = ?";
        int count = entityManager
        		.createQuery(jpql)
        		.setParameter(0, name)
        		.setParameter(1, owner)
        		.getResultList()
        		.size();
        return count > 0;
    }
	
}
