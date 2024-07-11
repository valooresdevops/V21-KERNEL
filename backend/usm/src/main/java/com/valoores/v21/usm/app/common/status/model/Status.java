package com.valoores.v21.usm.app.common.status.model;

import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sts_status", schema = SDEDBA)
@Data
@NoArgsConstructor
public class Status {

	@Id
	@SequenceGenerator(name = "STATUS_ID", sequenceName = "S_STATUS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_ID")
	@Column(name = "STATUS_ID")
	@Hidden
	private long id;

	@Column(name = "STATUS_NAME")
	private String name;

	@Column(name = "CREATION_DATE")
	@JsonIgnore
	@Hidden
	private String creationDate;

	@Column(name = "CREATED_BY")
	@JsonIgnore
	@Hidden
	private String createdBy;

	@Column(name = "UPDATE_DATE")
	@JsonIgnore
	@Hidden
	private String updateDate;

	@Column(name = "UPDATED_BY")
	@JsonIgnore
	@Hidden
	private String updatedBy;

}
