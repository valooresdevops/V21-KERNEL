package com.valoores.datacrowd.app.model;

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
@Table(name = "agent_table_123", schema = "SSDX_TMP")
public class agent_table_123 {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tmp_ins_ds_seq")
    //@SequenceGenerator(name = "tmp_ins_ds_seq", schema = "techdba", sequenceName = "tmp_ins_ds_sequence", allocationSize = 1)
	@Hidden
	@Column(name = "ARRAY_CLOB")
	private String arrayclob;
	
//	@Column(name = "fixedElementIds")
//	private byte[] fixedElementIds;

}