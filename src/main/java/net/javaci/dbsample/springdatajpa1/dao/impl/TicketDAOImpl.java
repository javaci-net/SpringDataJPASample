package net.javaci.dbsample.springdatajpa1.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.TicketDAO;

@Transactional
@Repository
public class TicketDAOImpl implements TicketDAO {

	@PersistenceContext
    private EntityManager entityManager;
}
