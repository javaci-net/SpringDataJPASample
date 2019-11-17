package net.javaci.dbsample.springdatajpa1.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.ReleaseDAO;
import net.javaci.dbsample.springdatajpa1.entity.Release;

@Transactional
@Repository
public class ReleaseDAOImpl implements ReleaseDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addRelease(Release release) {
		entityManager.persist(release);
	}
	
	@Override
    public Release getReleaseById(int releaseId) {
        return entityManager.find(Release.class, releaseId);
    }
}
