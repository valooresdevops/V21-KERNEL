package com.valoores.v21.usm.app.securitymanagement.accessrights.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "USM_ACCESS_MATRIX", schema = USMDBA)
@Data
@NoArgsConstructor
public class USMAccessMatrix {

	@Id
	@Column(name = "ACCESS_CODE")
	private Integer accessCode;
	@Column(name = "ACCESS_DISPLAY")
	private String isDisplay;
	@Column(name = "ACCESS_ADD")
	private String isAdd;
	@Column(name = "ACCESS_MODIFY")
	private String isModify;
	@Column(name = "ACCESS_DELETE")
	private String isDelete;
	@Column(name = "ACCESS_PRINT")
	private String isPrint;
	@Column(name = "ACCESS_EXPORT")
	private String isExport;
	@Column(name = "ACCESS_TRANSLATE")
	private String isTranslate;
//	
//	@OneToMany(mappedBy="usmAccessMatrix")
//	private Set<USMUserMulti> usmUserMulti;
}

