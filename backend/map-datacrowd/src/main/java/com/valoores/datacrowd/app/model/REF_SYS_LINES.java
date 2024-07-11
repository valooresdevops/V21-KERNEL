package com.valoores.datacrowd.app.model;




import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Hidden;

@Entity
@Table(name = "REF_SYS_LINES", schema = "SDEDBA")
public class REF_SYS_LINES {

	@Id
	@Hidden
	@NotBlank(message = "HEA_CODE cannot be empty!")
	@Column(name = "HEA_CODE")
	private long heacode;

	@NotBlank(message = "LIN_CODE cannot be empty!")
	@Column(name = "LIN_CODE")
	private int lincode;
	
	@NotBlank(message = "LIN_NAME cannot be empty!")
	@Column(name = "LIN_NAME")
	private String linname;
	
	@Column(name = "LIN_NAME2")
	private String linname2;
	
	@Column(name = "LIN_SNAME")
	private String linsname;

	
	@Column(name = "LIN_SNAME2")
	private String linsname2;
	

	@Column(name = "LIN_ORDERING")
	private int linordering;
	
	
	@NotBlank(message = "CREATION_DATE cannot be empty!")
	@Column(name = "CREATION_DATE")
	private Date CreationDate;
	
	@Column(name = "CREATED_BY")
	private int CreatedBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;


	
	@Column(name = "BUISNESS_FLAG")
	private Date buisness_flag;
	

	@Column(name = "SHOWRECORD")
	private int showrecord;
	


}
