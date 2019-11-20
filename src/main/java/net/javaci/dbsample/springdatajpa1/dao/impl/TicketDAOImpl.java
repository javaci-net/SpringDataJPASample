package net.javaci.dbsample.springdatajpa1.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.TicketDAO;
import net.javaci.dbsample.springdatajpa1.entity.Ticket;

@Transactional
@Repository
public class TicketDAOImpl implements TicketDAO {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
    public void addTicket(Ticket ticket) {
        entityManager.persist(ticket);
    }
	
	@Override
    public Ticket getTicketById(int ticketId) {
        return entityManager.find(Ticket.class, ticketId);
    }

	@Override
	public boolean removeTickets(List<Ticket> tickets) {
		for (Ticket ticket : tickets) {
			removeTicket(ticket);
		}
		return true;
	}

	@Override
	public boolean removeTicket(Ticket ticket) {
		entityManager.remove(entityManager.merge(ticket));
		return true;
	}
}
