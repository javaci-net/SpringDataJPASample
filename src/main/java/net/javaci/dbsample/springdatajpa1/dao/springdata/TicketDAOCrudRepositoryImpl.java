package net.javaci.dbsample.springdatajpa1.dao.springdata;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaci.dbsample.springdatajpa1.dao.TicketDAO;
import net.javaci.dbsample.springdatajpa1.entity.Ticket;
import net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO;

@Transactional
@Repository
public interface TicketDAOCrudRepositoryImpl extends TicketDAO, JpaRepository<Ticket, Integer> {

	/** @deprecated Use save */
	@Deprecated
	@Override
	default void addTicket(Ticket ticket) {
		save(ticket);
	}

	/** @deprecated Use findById */
	@Deprecated
	@Override
	default Ticket getTicketById(int ticketId) {
		return findById(ticketId).get();
	}

	@Override
	default List<TicketStatsByStatusDTO> findTicketStats() {
		// TODO Auto-generated method stub
		return null;
	}

	/** @deprecated Use deleteInBatch */
	@Deprecated
	@Override
	default boolean removeTickets(List<Ticket> tickets) {
		deleteInBatch(tickets);
		return true;
	}

	/** @deprecated Use deleteById */
	@Deprecated
	@Override
	default boolean removeTicket(Ticket ticket) {
		deleteById(ticket.getId());
		return true;
	}

}
