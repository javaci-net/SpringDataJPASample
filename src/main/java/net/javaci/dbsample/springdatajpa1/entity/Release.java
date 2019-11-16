package net.javaci.dbsample.springdatajpa1.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_release") //  release is a special keyword in mysql @see https://forums.mysql.com/read.php?101,665004,665004
public class Release {

	@Id
    private Integer id;
}
