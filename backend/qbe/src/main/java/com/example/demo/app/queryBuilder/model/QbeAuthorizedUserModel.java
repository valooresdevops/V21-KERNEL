package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.QBEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(QbeAuthorizedUserIdModel.class)
@Table(name = "qbe_authorized_user", schema = QBEDBA)
@Getter
@Setter

public class QbeAuthorizedUserModel {
	@Id
	@Column(name = "QBE_ID")
	private long qbeId;
	
	@Id
	@Column(name = "USR_CODE")
	private long usrCode;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Column(name = "UPDATED_BY")
	private long updatedBy;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "HAS_UPDATE")
	private byte hasUpdate;
	
	


}
