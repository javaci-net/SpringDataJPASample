package net.javaci.dbsample.springdatajpa1.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.ApplicationDAO;

@Transactional
@Repository
public class ApplicationDAOImpl implements ApplicationDAO {

	@PersistenceContext
    private EntityManager entityManager;
}
