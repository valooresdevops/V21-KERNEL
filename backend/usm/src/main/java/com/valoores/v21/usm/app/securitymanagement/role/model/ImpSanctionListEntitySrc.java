package com.valoores.v21.usm.app.securitymanagement.role.model;

import static com.valoores.v21.usm.utils.Schemas.IMPDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "IMP_SANCTION_LIST_ENTITY_SRC", schema = IMPDBA)
@Getter
@Setter
public class ImpSanctionListEntitySrc {
	@Id
	@Column(name = "ENTITY_SOURCE_ID")
	private long entitySourceId;	

	@Column(name = "ENTITY_SOURCE_NAME_ABBREV")
	private String entitySourceNameAbbrev;
	
	@Column(name = "ENTITY_SOURCE_NAME")
	private String entitySourceName;
	
	@Column(name="CREATED_BY")
	private String CREATED_BY;
	
	@Column (name ="ENTITY_SOURCE_CODE")
	private long  entitySourceCode;

}