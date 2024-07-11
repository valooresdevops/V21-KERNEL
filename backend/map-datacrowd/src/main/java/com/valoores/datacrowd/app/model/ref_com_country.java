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
//@NoArgsConstructor
@Entity
@Table(name = "ref_com_country", schema = "SDEDBA")
public class ref_com_country {

	@Id
	@Hidden
	@Column(name = "COU_ID")
	private long COU_ID;

	@Column(name = "COU_NAME")
	private String COU_NAME;

	@Column(name = "ISO_COU_CODE_NUM")
	private String ISO_COU_CODE_NUM;

	@Column(name = "ISO_COU_CODE_ALPHA")
	private String ISO_COU_CODE_ALPHA;

}
