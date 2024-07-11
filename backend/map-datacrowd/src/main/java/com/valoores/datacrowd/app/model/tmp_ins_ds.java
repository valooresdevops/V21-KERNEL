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
@Table(name = "tmp_ins_ds", schema = "techdba")
public class tmp_ins_ds {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tmp_ins_ds_seq")
    //@SequenceGenerator(name = "tmp_ins_ds_seq", schema = "techdba", sequenceName = "tmp_ins_ds_sequence", allocationSize = 1)
	@Hidden
	@Column(name = "IDSSS")
	private long idsss;
	
//	@Column(name = "fixedElementIds")
//	private byte[] fixedElementIds;

}