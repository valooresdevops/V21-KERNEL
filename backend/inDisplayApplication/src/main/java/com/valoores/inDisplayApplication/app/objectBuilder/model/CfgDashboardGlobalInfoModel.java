package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_DASHBOARD_GLOBAL", schema =USMDBA )
@Data
public class CfgDashboardGlobalInfoModel {
	@Id
    @Column(name = "DASH_GLOBAL_INFO_ID")
    private long DASH_GLOBAL_INFO_ID;

	 @Column(name = "GLOBAL_INFO_ID")
	    private long GLOBAL_INFO_ID;
	 
	 @Column(name = "DASHBOARD_ID")
	    private long DASHBOARD_ID;

	 @Column(name = "VIEW_ORDER")
		private String VIEW_ORDER;
	 
	 @Column(name = "VIEW_SIZE")
	    private String VIEW_SIZE;
}
