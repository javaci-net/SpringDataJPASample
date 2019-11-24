package net.javaci.dbsample.springdatajpa1.dao.net.javaci.dbsample.springdatajpa1.dao.springdata;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.ApplicationDAO;
import net.javaci.dbsample.springdatajpa1.entity.Application;
import net.javaci.dbsample.springdatajpa1.entity.dto.ApplicationDTO;

@Transactional
@Repository
@Primary
// @RepositoryRestResource(collectionResourceRel = "application", path = "application")
public interface ApplicationDAOCrudRepositoryImpl extends ApplicationDAO, JpaRepository<Application, Integer> {

	long countByOwner(String owner);
	Application findFirstByOrderByIdDesc();
	List<Application> findTop2ByOrderByIdDesc();
	List<Application> findByOwner(String owner);
	List<Application> findByOwner(String owner, Sort sort);
	Page<Application> findByOwner(String owner, Pageable pageable);
	List<Application> findByOwnerOrderByNameAsc(String owner);
	List<Application> findDistinctByOwner(String owner);
	List<Application> findByOwnerIgnoreCase(String owner);
	List<Application> findByOwnerOrNameAllIgnoreCase(String owner, String name);
	Application findFirstByOwnerOrNameAllIgnoreCaseOrderByIdDesc(String owner, String name);
	Application findByName(String name);
	long deleteByName(String name);
	
	boolean existsByNameAndOwner(String name, String owner);
	
	@Query("SELECT count(a)>0 FROM Application a WHERE a.name = :name AND a.owner = :owner")
	boolean checkApplicationExistsByNameAndOwner(@Param("name") String name, @Param("owner") String owner);
	
	@EntityGraph(attributePaths = { "tickets", "releasesToDeploy" } , type = EntityGraphType.LOAD)
	Application findWithTicketsAndReleasesById(Integer applicationId);
	
	@Query("SELECT a FROM Application a INNER JOIN FETCH a.tickets INNER JOIN FETCH a.releasesToDeploy WHERE a.id = :id")
	Application findByIdWithTicketsAndReleasesV2(@Param("id") Integer applicationId);
	
	@Query("SELECT new "
			+ "net.javaci.dbsample.springdatajpa1.entity.dto.ApplicationDTO("
			+ " a.name, a.owner, max(t.id), max(r.id)"
			+ ")"
			+ " FROM Application a "
			+ "INNER JOIN a.tickets t INNER JOIN a.releasesToDeploy r "
			+ "GROUP BY a.id, a.name, a.owner "
			+ "HAVING a.id = :id")
	ApplicationDTO findByIdWithTicketsAndReleasesV3(@Param("id") Integer applicationId);
	
	/*-- SELECT new net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO
	 *    ( t.status, count(t.id), min(t.createDateTime), max(t.createDateTime)) 
	 *    FROM Ticket t GROUP BY t.status
	 */
	
	<T> T findById(int applicationId, Class<T> type);
	
	@Modifying
	@Query("update Application a set a.name = ?2, a.owner = ?3 where a.id = ?1")
	void setNameAndOwnerById(Integer applicationId, String newName, String newOwner);
	
	/** @deprecated Use save */
	@Override
	default void addApplication(Application application) {
		save(application);
	}

	/** @deprecated Use findById */
	@Deprecated
	@Override
	default Application getApplicationById(int applicationId) {
		return findById(applicationId).get();
	}

	/** @deprecated Use checkApplicationExistsByNameAndOwner */
	@Deprecated
	@Override
	default boolean applicationExists(String name, String owner) {
		return checkApplicationExistsByNameAndOwner(name, owner);
	}


	/** @deprecated Use existsByNameAndOwner */
	@Deprecated
	@Override
	default boolean applicationReallyExists(String name, String owner) {
		return existsByNameAndOwner(name, owner);
	}

	/** @deprecated Use setNameAndOwnerById */
	@Deprecated
	@Override
	default boolean updateNameAndOwnerById(Integer id, String newName, String newOwner) {
		Optional<Application> instance = findById(id);
		if (!instance.isPresent()) {
			return false;
		}
		
		Application application = instance.get();
		application.setOwner(newName);
		application.setOwner(newOwner);
		
		return true;
	}

	/** @deprecated Use findByIdWithTicketsAndReleases */
	@Deprecated
	@Override
	default Application getApplicationWithTicketsAndReleases(int applicationId) {
		return findWithTicketsAndReleasesById(applicationId);
	}

	/** @deprecated Use findByIdWithTicketsAndReleasesV2 */
	@Deprecated
	@Override
	default Application getApplicationWithTicketsAndReleasesV2(int applicationId) {
		return findByIdWithTicketsAndReleasesV2(applicationId);
	}

	@Override
	default ApplicationDTO getApplicationWithTicketsAndReleasesV3(int applicationId) {
		return findByIdWithTicketsAndReleasesV3(applicationId);
	}

}
