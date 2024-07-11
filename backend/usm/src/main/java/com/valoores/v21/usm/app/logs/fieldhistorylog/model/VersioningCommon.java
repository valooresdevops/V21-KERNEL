package com.valoores.v21.usm.app.logs.fieldhistorylog.model;
import static com.valoores.v21.usm.utils.Schemas.AUDDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AUD_DATA_VERSIONING_COMMON", schema = AUDDBA)
@Getter
@Setter

public class VersioningCommon {
	@Id
	@Column(name="AUD_DATA_VERSION_COMMON_ID")
	private long id;
	
	@Column(name="data_versioning_id")
	private long data_versioning_id;
	
	@Column(name="pk_elem_data_value")
	private String pk_elem_data_value;
	
	@Column(name= "elem_data_old_value")
	private String elem_data_old_value;
	
	@Column(name= "elem_data_new_value")
	private String elem_data_new_value;
	
	@Column(name= "creation_date")
	private Date creation_date;
	
	@Column(name= "created_by")
	private long created_by;

}
