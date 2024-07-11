package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.HRDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HR_EMPLOYEE", schema = HRDBA)
@Data
@NoArgsConstructor
public class QbeUser {
	@Id
	@Column(name = "EMP_ID")
	private long EMP_ID;
	
	@Column(name = "EMP_NAME")
	private String EMP_NAME;
	
}