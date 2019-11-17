package net.javaci.dbsample.springdatajpa1.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	private Integer appId;
	private String description;
	private String status;
	private String title;
	
}
