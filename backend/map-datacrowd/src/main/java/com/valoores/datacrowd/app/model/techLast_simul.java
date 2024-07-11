package com.valoores.datacrowd.app.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "techLast_simul", schema = "techdba")
public class techLast_simul {

	@Id
	@Hidden
	@Column(name = "Simul_ID")
	private long simulid;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "CREATION_DATE")
	private Date CreationDate;
	
	@Column(name = "CREATED_BY")
	private int  CreatedBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;
	



	
 
	 

}
