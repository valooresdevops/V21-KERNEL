package com.valoores.inDisplayApplication.app.dynamicSearch.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_TABLE_CONFIG", schema = SUITEDBA)
@Data
public class TableConfigurationModel {
	@Id
	@Column(name = "TABLE_ID")
	private long tableId;

	@Column(name = "TABLE_NAME")
	private String tableName;
}
