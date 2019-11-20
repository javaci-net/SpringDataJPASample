package net.javaci.dbsample.springdatajpa1.dao;

import java.util.List;

import net.javaci.dbsample.springdatajpa1.entity.Ticket;

public interface TicketDAO {

	void addTicket(Ticket ticket);

	Ticket getTicketById(int ticketId);

	boolean removeTickets(List<Ticket> tickets);

	boolean removeTicket(Ticket ticket);
	
}
