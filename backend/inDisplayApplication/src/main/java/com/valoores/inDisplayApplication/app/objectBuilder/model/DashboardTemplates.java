package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USM_DASHBOARD_TEMPLATES", schema = USMDBA)
@Data
public class DashboardTemplates {	
	@Id
	@SequenceGenerator(name = "DASH_SEQ_TEMPLATE",  schema = "USMDBA",sequenceName = "DASH_SEQ_TEMPLATE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="DASH_SEQ_TEMPLATE")
	@Column(name = "TEMPLATE_ID", nullable = false)
	private long templateID;

	@Column(name = "TEMPLATE_NAME")
	private String templateName;

	@Column(name = "USER_IDS")
	private String userIds;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "CREATED_BY")
	private long createdBy;

	@Column(name = "UPDATED_BY")
	private long updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "ROLE_NAMES")
	private String roleNames;

	@Column(name = "USER_NAMES")
	private String userNames;

	@Column(name = "PREF_DATA")
	private String PREF_DATA;

	@Column(name = "TEMPLATE_P_ID" , nullable = true)
	private Integer templatePId;

}
