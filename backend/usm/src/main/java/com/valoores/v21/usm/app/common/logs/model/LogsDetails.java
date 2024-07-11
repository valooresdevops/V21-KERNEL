package com.valoores.v21.usm.app.common.logs.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USM_USER_LOG_DETAILS", schema = USMDBA)
@Data
@NoArgsConstructor
public class LogsDetails {

	@Id
	@Column(name = "LOG_D_ID")
	private long logDId;
	
	@Hidden
	@Column(name = "LOG_ID")
	private long logId;
	
	@Column(name = "OPERATION_TYPE")
	private String operationType;
	
	@Column(name = "OPERATION_DATE")
	private Date operationDate;
	
	@Column(name = "OPERATION_HINT")
	private String operationDescription;
	
	@Column(name="OPERATION_PROCESS")
	private String operationProcess;
	
	
	
}
