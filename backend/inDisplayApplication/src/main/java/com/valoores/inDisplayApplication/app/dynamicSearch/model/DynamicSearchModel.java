package com.valoores.inDisplayApplication.app.dynamicSearch.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CFG_COLUMN_CONFIG", schema = SUITEDBA)
@Data
public class DynamicSearchModel {

	@Id
	@Column(name = "COLUMN_ID")
	private long columnId;

	@Column(name = "COLUMN_NAME")
	private String columnName;

	@Column(name = "TABLE_ID")
	private long tableId;

	@Column(name = "COLUMN_TYPE_CODE")
	private long columnTypeCode;

	@Column(name = "COLUMN_DESC")
	private String columnDesc;
	
	@Column(name = "QBE_ID")
	private long qbeId;

}
