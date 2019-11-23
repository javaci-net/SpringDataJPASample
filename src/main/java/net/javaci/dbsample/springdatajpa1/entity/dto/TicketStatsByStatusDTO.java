package net.javaci.dbsample.springdatajpa1.entity.dto;

import java.time.LocalDateTime;

public class TicketStatsByStatusDTO {

	private String status;
	
	private Long count;
	
	private LocalDateTime minCreateDateTime;
	
	private LocalDateTime maxCreateDateTime;

	public TicketStatsByStatusDTO(String status, Long count, LocalDateTime minCreateDateTime,
			LocalDateTime maxCreateDateTime) {
		super();
		this.status = status;
		this.count = count;
		this.minCreateDateTime = minCreateDateTime;
		this.maxCreateDateTime = maxCreateDateTime;
	}

	public String getStatus() {
		return status;
	}

	public Long getCount() {
		return count;
	}

	public LocalDateTime getMinCreateDateTime() {
		return minCreateDateTime;
	}

	public LocalDateTime getMaxCreateDateTime() {
		return maxCreateDateTime;
	}
	
}
