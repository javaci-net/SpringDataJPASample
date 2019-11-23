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
import net.javaci.dbsample.springdatajpa1.entity.Application_;
import net.javaci.dbsample.springdatajpa1.entity.Release_;
import net.javaci.dbsample.springdatajpa1.entity.Ticket_;
import net.javaci.dbsample.springdatajpa1.entity.dto.ApplicationDTO;

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
        
		String a_id_param = "a_id_param";
		
		// SELECT a from Application a INNER JOIN FETCH a.tickets t INNER JOIN FETCH a.releasesToDeploy r WHERE a.id=:a_id_param
		String jpql = "SELECT a from " + Application.class.getSimpleName() + " a "
				+ "INNER JOIN FETCH a."	+ Application_.tickets.getName() + " t "
				+ "INNER JOIN FETCH a."	+ Application_.releasesToDeploy.getName() + " r "
				+ "WHERE a." + Application_.id.getName() + "=" + ":" + a_id_param;
		
		// with fetch only one select is executed
		// https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/fetch-join.html
		
		Application result = entityManager
        		.createQuery(jpql, Application.class)
        		.setParameter(a_id_param, applicationId)
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
		root.fetch(Application_.tickets, JoinType.INNER);
		root.fetch(Application_.releasesToDeploy, JoinType.INNER);
		
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
		
		String a_id_param = "a_id_param";
		
		String jpqlTemplate = "SELECT"
				+ " new %s ( " // ApplicationDTO
				+ "    a.%s, a.%s, max(t.%s), max(r.%s) " // a.name, a.owner, max(t.id), max(r.id)
				+ " )"
				+ " FROM %s a" // Application
				+ "    INNER JOIN a.%s t " // a.tickets
				+ "    INNER JOIN a.%s r " // a.releasesToDeploy
				+ " GROUP BY a.%s, a.%s, a.%s" // a.id, a.name, a.owner
				+ " HAVING a.%s= :" + a_id_param; // a.id
		
		String jpql = String.format(jpqlTemplate, 
				ApplicationDTO.class.getCanonicalName(), 
				Application_.name.getName(),
				Application_.owner.getName(),
				Ticket_.id.getName(),
				Release_.id.getName(),
				Application.class.getSimpleName(),
				Application_.tickets.getName(),
				Application_.releasesToDeploy.getName(),
				Application_.id.getName(),
				Application_.name.getName(),
				Application_.owner.getName(),
				Application_.id.getName()
		);
		
		ApplicationDTO result = entityManager
        		.createQuery(jpql, ApplicationDTO.class)
        		.setParameter(a_id_param, applicationId)
        		.getSingleResult();

		return result;
	}
	
	@Override
    public boolean applicationExists(String name, String owner) {
		
		String a_name_param = "a_name_param";
		String a_owner_param = "a_owner_param";
		
        // note application is the class name; not the table name; 
		// class name is case sensitive; use class field names - column names
		String jpql = "from " + Application.class.getSimpleName() + " as a "
        		+ "WHERE "
        		+ "a." + Application_.name.getName() + " = :" + a_name_param + " "
        		+ "and a." + Application_.owner.getName() + " = :" + a_owner_param;
		
		// from Application as a WHERE a.name = ? and a.owner = ?
        int count = entityManager
        		.createQuery(jpql)
        		.setParameter(a_name_param, name)
        		.setParameter(a_owner_param, owner)
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
				cb.equal(root.get(Application_.name), name),
				cb.equal(root.get(Application_.owner), owner)
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
