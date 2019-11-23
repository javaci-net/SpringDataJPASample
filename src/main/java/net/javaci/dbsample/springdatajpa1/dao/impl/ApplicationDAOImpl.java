package net.javaci.dbsample.springdatajpa1.dao.impl;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.ApplicationDAO;
import net.javaci.dbsample.springdatajpa1.entity.Application;
import net.javaci.dbsample.springdatajpa1.entity.Ticket;
import net.javaci.dbsample.springdatajpa1.entity.dto.ApplicationDTO;
import net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO;

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
    public Application getApplicationWithTicketsAndReleases(int applicationId) {
        
		String jpql = "SELECT a from Application a "
				+ "INNER JOIN FETCH a.tickets t "
				+ "INNER JOIN FETCH a.releasesToDeploy r "
				+ "WHERE a.id=?";
		
		// with fetch only one select is executed
		// https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/fetch-join.html
		
		Application result = entityManager
        		.createQuery(jpql, Application.class)
        		.setParameter(0, applicationId)
        		.getSingleResult();
		
		// Real select:
		/*-- 
		 * select 
		 *  ... 
		 * from application A 
		 *  inner join ticket T 
		 *     on A.id=T.application_id 
		 *  inner join apprelease_application AR 
		 *     on A.id=AR.application_fk 
		 *  inner join apprelease R 
		 *     on AR.release_fk=R.id 
		 * where A.id=?
		 */

		return result;
    }
	
	@Override
    public Application getApplicationWithTicketsAndReleasesV2(int applicationId) {
        
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Application> cq = cb.createQuery(Application.class);
		
		Root<Application> root = cq.from(Application.class);
		root.fetch("tickets", JoinType.INNER);
		root.fetch("releasesToDeploy", JoinType.INNER);
		
		cq.where( cb.equal(root.<Set<Integer>>get("id"), applicationId) );
		
		Application result = entityManager
        		.createQuery(cq)
        		.getSingleResult();
		
		// Real select:
		/*-- 
		 * select 
		 *   ...
		 * from 
		 *   application A 
		 *   inner join ticket T 
		 *      on A.id=T.application_id 
		 *   inner join apprelease_application AR 
		 *      on A.id=AR.application_fk 
		 *   inner join apprelease R 
		 *      on AR.release_fk=R.id 
		 * where 
		 *    A.id=1
		 */

		return result;
    }
	
	@Override
    public ApplicationDTO getApplicationWithTicketsAndReleasesV3(int applicationId) {
		
		String jpql = "SELECT"
				+ " new " + ApplicationDTO.class.getCanonicalName() + "( "
				+ "    a.name, a.owner, max(t.id), max(r.id) "
				+ " )"
				+ " FROM " + Application.class.getSimpleName() + " a"
				+ "    INNER JOIN a.tickets t "
				+ "    INNER JOIN a.releasesToDeploy r "
				+ " GROUP BY a.id, a.name, a.owner"
				+ " HAVING a.id=?";
		
		ApplicationDTO result = entityManager
        		.createQuery(jpql, ApplicationDTO.class)
        		.setParameter(0, applicationId)
        		.getSingleResult();

		return result;
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
