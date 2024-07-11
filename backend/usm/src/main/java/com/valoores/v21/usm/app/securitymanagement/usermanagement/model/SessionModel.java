package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.TECHDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "V21_SESSIONS_DATA", schema = TECHDBA)
@Getter
@Setter
public class SessionModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "v21_sessions_data_session_id_seq")
	@SequenceGenerator(name = "v21_sessions_data_session_id_seq", schema = "TECHDBA", sequenceName = "v21_sessions_data_session_id_seq", allocationSize = 1)
	@Column(name = "SESSION_ID")
	private long SESSION_ID;

	@Column(name = "SESSION_SERIAL")
	private String SESSION_SERIAL;

	@Column(name = "SESSION_ATTRIBUTE")
	private String SESSION_ATTRIBUTE;

	@Column(name = "SESSION_VALUE")
	private byte[] SESSION_VALUE;

	@Column(name = "CREATED_BY")
	private long CREATED_BY;

	@Column(name = "CREATION_DATE")
	private Date CREATION_DATE;

	@Column(name = "UPDATABLE")
	private int UPDATABLE;
}
