package net.javaci.dbsample.springdatajpa1.dao.impl;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
	
	@Override
	public boolean applicationReallyExists(String name, String owner) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Application> cq = cb.createQuery(Application.class);
		
		Root<Application> root = cq.from(Application.class);

		cq.select(root).where( 
			cb.and( 
				cb.equal(root.<Set<String>>get("name"), name),
				cb.equal(root.<Set<String>>get("owner"), owner)
			)
		);
		
		int count = entityManager.createQuery(cq).getResultList().size();
		return count > 0;
	}

	@Override
	public boolean updateNameAndOwnerById(Integer id, String newName, String newOwner) {
		
		Application app = getApplicationById(id);
		app.setName(newName);
		app.setOwner(newOwner);
		entityManager.flush();
		return true;
	}
	
}
