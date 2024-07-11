package com.valoores.v21.usm.app.logs.fieldhistorylog.model;
import static com.valoores.v21.usm.utils.Schemas.AUDDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AUD_DATA_VERSIONING", schema = AUDDBA)
@Getter
@Setter

public class Versioning {
	@Id
	@Column(name="DATA_VERSIONING_ID")
	private long id;
	
	@Column(name= "menu_code")
	private String menu_code;
	
	@Column(name= "col_sequence")
	private long col_sequence;

}
