package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import lombok.Data;

@Entity
@Table(name = "USM_DASHBOARD_OBJECT_GRID", schema =USMDBA )
@Data
public class CfgDashboardGridModel {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DASHBOARD_OBJECT_GRID_ID")
    private long dashboardGridId;

	 @Column(name = "OBJECT_GRID_ID")
	    private long gridId;
	 
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

