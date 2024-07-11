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
@Table(name = "USM_DASHBOARD_OBJECT_CHART", schema =USMDBA )
@Data
public class CfgDashboardChartModel {
	@Id
	@SequenceGenerator(name = "S_DASHBOARD_OBJECT_CHART", schema="USMDBA",sequenceName = "S_DASHBOARD_OBJECT_CHART", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_DASHBOARD_OBJECT_CHART")
    @Column(name = "DASHBOARD_OBJECT_CHART_ID")
    private long dashboardChartId;

	 @Column(name = "OBJECT_CHART_ID")
	    private long chartId;
	 
	 @Column(name = "TEMPLATE_ID")
	    private long templateId;

	 @Column(name = "DISPLAY_ORDER")
		private long order;
	 
	 @Column(name = "DISPLAY_SIZE")
	    private long size;
	 
	 @Column(name = "CREATION_DATE")
	    private Date creationDate;
	 
	 @Column(name = "CREATED_BY")
	    private long createdBy;
	 
	 @Column(name = "UPDATED_BY")
	    private long updatedBy;
	 
	 @Column(name = "UPDATE_DATE")
	    private Date updateDate;
}
