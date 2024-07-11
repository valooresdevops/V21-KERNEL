package com.example.demo.app.queryBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "qbe_user_query", schema = "qbedba")
@Data

public class QbeUserModel
{
	@Id
	@Column(name="qbe_id")
	private long qbeId;
	
	@Column(name="usr_code")
	private long usrCode;
}

