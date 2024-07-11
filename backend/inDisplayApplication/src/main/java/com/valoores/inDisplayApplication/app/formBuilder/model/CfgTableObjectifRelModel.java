package com.valoores.inDisplayApplication.app.formBuilder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_TABLE_OBJECT_REL", schema = "SUITEDBA")
@Data
@IdClass(CfgTableObjectifRelModel.class)
public class CfgTableObjectifRelModel implements Serializable {
	
	@Id
	@Column(name="TABLE_ID")
	private long tableId;
	
	@Id
	@Column(name="OBJECT_ID")
	private long objectId;
	
	@Column(name="CREATED_BY")
	private long createdBy;
	
	@Column(name="ORDER_NO")
	private long orderNo;
}
