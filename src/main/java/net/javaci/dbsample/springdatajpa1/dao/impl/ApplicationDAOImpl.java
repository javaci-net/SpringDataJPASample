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
}
