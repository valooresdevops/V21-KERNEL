package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ref_com_language", schema = 	SDEDBA)
@Data
@NoArgsConstructor
public class USMLanguage {
	
	@Id
	@SequenceGenerator(name = "LANGUAGE_ID", sequenceName = "S_COM_LANGUAGE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LANGUAGE_ID")
	@Column(name = "LAN_ID")
	@Hidden
	private long id;
	
	@Column(name = "LAN_NAME")
	private String name;
	
	@Column(name = "CREATION_DATE")
	@Hidden
	private String creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private String createdBy;
	
	@Column(name = "UPDATE_DATE")
	@Hidden
	private String updateDate;
	
	@Column(name = "UPDATED_BY")
	@Hidden
	private String updatedBy;
	
}
