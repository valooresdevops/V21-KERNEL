package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_DASHBOARD_REPORT", schema =SUITEDBA )
@Data
public class CfgDahboardReportModel {
	@Id
    @Column(name = "DASH_REPORT_ID")
    private long DASH_REPORT_ID;

	 @Column(name = "REPORT_ID")
	    private long REPORT_ID;
	 
	 @Column(name = "DASHBOARD_ID")
	    private long DASHBOARD_ID;

	 @Column(name = "VIEW_ORDER")
		private String VIEW_ORDER;
	 
	 @Column(name = "VIEW_SIZE")
	    private String VIEW_SIZE;
}
