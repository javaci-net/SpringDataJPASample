package net.javaci.dbsample.springdatajpa1.dao;

import net.javaci.dbsample.springdatajpa1.entity.Application;

public interface ApplicationDAO {

	void addApplication(Application application);

	Application getApplicationById(int applicationId);

	boolean applicationExists(String name, String owner);
	
}
