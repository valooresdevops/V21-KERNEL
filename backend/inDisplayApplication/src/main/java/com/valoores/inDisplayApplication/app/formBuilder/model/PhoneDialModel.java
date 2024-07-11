package com.valoores.inDisplayApplication.app.formBuilder.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "STD_LIST_INTL_DIRECT_DIALLING", schema = "ISODBA")
@Data
public class PhoneDialModel {
	@Id
	@Column(name="INTL_DIRECT_DIALLING_CODE")
	private String intlDirectDiallingCode;
	
	@Column(name="COU_CODE")
	private String couCode;
	
	
//	@Column(name="QUERY_NAME")
//	private String name;
//	
//	@Column(name="CREATION_DATE")
//	private String creationDate;
//	
//	@Column(name="CREATED_BY")
//	private long createdBy;
}





	
	

