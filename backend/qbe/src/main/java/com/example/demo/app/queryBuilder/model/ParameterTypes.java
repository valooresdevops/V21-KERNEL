package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.SDEDBA;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="REF_SYS_LINES", schema =SDEDBA)
@Getter
@Setter
public class ParameterTypes {

	@Id
	@Column(name = "LIN_CODE")
	private int LIN_CODE;
	
	
	@Column(name = "HEA_CODE")
	private int HEA_CODE;
	
	@Column(name = "LIN_NAME")
	private String LIN_NAME;
	
	@Column(name = "LIN_NAME2")
	private String LIN_NAME2;

	
}
