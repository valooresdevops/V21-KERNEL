package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_DASHBOARD_PIVOT", schema =SUITEDBA )
@Data
public class CfgDashboardPivotModel {
	@Id
    @Column(name = "DASH_PIVOT_ID")
    private long DASH_PIVOT_ID;

	 @Column(name = "Pivot_ID")
	    private long Pivot_ID;
	 
	 @Column(name = "DASHBOARD_ID")
	    private long DASHBOARD_ID;

	 @Column(name = "VIEW_ORDER")
		private String VIEW_ORDER;
	 
	 @Column(name = "VIEW_SIZE")
	    private String VIEW_SIZE;
}
