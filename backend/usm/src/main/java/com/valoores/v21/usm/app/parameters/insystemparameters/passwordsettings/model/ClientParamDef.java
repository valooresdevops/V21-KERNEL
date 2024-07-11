package com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_CLIENT_PARAM_DEFINITION", schema = USMDBA)
@Getter
@Setter

public class ClientParamDef {
	@Id
	@Column(name="CLIENT_PARAM_ID")
	private long idDef;

	@Column(name= "PARAM_CODE")
	private String ParamCode;
	
	  @Column(name= "HEA_CODE")
		private String HeaCode; 
	  
	@Column(name= "MENU_CODE")
	private String Menucode;
	
	@Column(name= "PARAM_GRP_CODE")
	private String ParamGrCode;
	
	@Column(name= "ROW_NO")
	private String RowNo;
	
	@Column(name= "COLUMN_NO")
	private String ColNo;

	@Column(name= "STRUC_DATA_ID")
	private String StrucDataId;
	
	@Column(name= "EXTENSION_CONDITION")
	private String ExCond;
	
	@Column(name= "CREATION_DATE")
	private String CrDate;
	
	@Column(name= "UPDATE_DATE")
	private String updateDate;
	
	@Column(name= "CREATED_BY")
	private String createdBy;
	
	@Column(name= "UPDATED_BY")
	private String updatedBy;
	
	@Column(name= "IS_OBLIGATORY")
	private String Oblig;
	
	@Column(name= "PARAM_VALUE_MAX_LENGTH")
	private String MaxLength;
	
	@Column(name= "SORT_CONDITION")
	private String SortCond;
	
	@Column(name= "IS_DISPLAYED")
	private String IsDisplayed;
	
}
