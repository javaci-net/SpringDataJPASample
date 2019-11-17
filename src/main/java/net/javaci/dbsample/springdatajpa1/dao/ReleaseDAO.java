package net.javaci.dbsample.springdatajpa1.dao;

import net.javaci.dbsample.springdatajpa1.entity.Release;

public interface ReleaseDAO {

	void addRelease(Release release);

	Release getReleaseById(int releaseId);
	
}
