package net.javaci.dbsample.springdatajpa1.dao.impl;

import static net.javaci.dbsample.springdatajpa1.entity.Ticket_.createDateTime;
import static net.javaci.dbsample.springdatajpa1.entity.Ticket_.id;
import static net.javaci.dbsample.springdatajpa1.entity.Ticket_.status;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.TicketDAO;
import net.javaci.dbsample.springdatajpa1.entity.Ticket;
import net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO;

@Transactional
@Repository
@Primary
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
	public List<TicketStatsByStatusDTO> findTicketStats() {
		
		String t_ = "t";
		String t_name = t_ + "." + id.getName();
		String t_createDateTime = t_ + "." + createDateTime.getName();
		String t_status = t_ + "." + status.getName();
		Class<TicketStatsByStatusDTO> ticketStatsByStatusDTOClass = TicketStatsByStatusDTO.class;
		String ticketStatsByStatusDTO_ = ticketStatsByStatusDTOClass.getCanonicalName();
		String t_class_ = Ticket.class.getSimpleName();
		
		/*-- SELECT new net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO
		 *    ( t.status, count(t.id), min(t.createDateTime), max(t.createDateTime)) 
		 *    FROM Ticket t GROUP BY t.status
		 */
		
		String jpql = 
			"SELECT "
				+ "new " + ticketStatsByStatusDTO_ + "( "
					+ t_status + ", "
					+ "count(" + t_name + "), "
					+ "min(" + t_createDateTime + "), "
					+ "max(" + t_createDateTime + ") "
				+ ") "
			+ "FROM " + t_class_ + " " + t_ + " " // class name; not the table name
			+ "GROUP BY " + t_status;
		
		List<TicketStatsByStatusDTO> resultList = entityManager
				.createQuery(jpql, ticketStatsByStatusDTOClass)
				.getResultList();
				
		return resultList;
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
