package net.javaci.dbsample.springdatajpa1.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Application {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	@Column(length = 2000)
    private String description;
	
	@Column(name = "app_name", nullable = false)
    private String name;

    private String owner;
}
