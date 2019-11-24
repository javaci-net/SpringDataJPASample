package net.javaci.dbsample.springdatajpa1.dao.net.javaci.dbsample.springdatajpa1.dao.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaci.dbsample.springdatajpa1.dao.ReleaseDAO;
import net.javaci.dbsample.springdatajpa1.entity.Release;

public interface ReleaseDAOCrudRepositoryImpl extends ReleaseDAO, JpaRepository<Release, Integer> {

	/** @deprecated Use save */
	@Deprecated
	@Override
	default void addRelease(Release release) {
		save(release);
	}

	/** @deprecated Use findById */
	@Deprecated
	@Override
	default Release getReleaseById(int releaseId) {
		return findById(releaseId).get();
	}

}
