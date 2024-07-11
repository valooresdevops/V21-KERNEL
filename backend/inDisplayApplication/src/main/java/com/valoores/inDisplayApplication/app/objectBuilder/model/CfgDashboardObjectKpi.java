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
@Table(name = "USM_DASHBOARD_OBJECT_KPI", schema =USMDBA )
@Data
public class CfgDashboardObjectKpi {
	@Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DASHBOARD_OBJECT_KPI")
    @SequenceGenerator(name = "S_DASHBOARD_OBJECT_KPI", schema = "USMDBA", sequenceName = "S_DASHBOARD_OBJECT_KPI", allocationSize = 1)
    @Column(name = "DASHBOARD_OBJECT_KPI_ID")
    private long dashboardKpiId;
	
	@Column(name = "OBJECT_KPI_ID")
    private long kpiId;
	
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
