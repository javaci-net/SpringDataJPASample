package net.javaci.dbsample.springdatajpa1.dao;

import java.util.List;

import net.javaci.dbsample.springdatajpa1.entity.Ticket;
import net.javaci.dbsample.springdatajpa1.entity.dto.TicketStatsByStatusDTO;

public interface TicketDAO {

	void addTicket(Ticket ticket);

	Ticket getTicketById(int ticketId);

	List<TicketStatsByStatusDTO> findTicketStats();
	
}
