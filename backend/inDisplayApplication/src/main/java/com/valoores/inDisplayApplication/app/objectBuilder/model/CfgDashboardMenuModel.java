package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USM_DASHBOARD_MENU", schema =USMDBA )
@Data
public class CfgDashboardMenuModel {
	@Id
    @Column(name = "DASHBOARD_MENU_ID")
	
    private long dashboardMenuId;

	 @Column(name = "MENU_CODE")
	    private long menuCode;
	 
	 @Column(name = "TEMPLATE_ID")
	    private long templateId;

	 @Column(name = "DISPLAY_ORDER")
		private String order;
	 
	 @Column(name = "DISPLAY_SIZE")
	    private String size;
	 
	 @Column(name = "CREATION_DATE")
	    private String creationDate;
	 
	 @Column(name = "CREATED_BY")
	    private String createdBy;
	 
	 @Column(name = "UPDATED_BY")
	    private String updatedBy;
	 
	 @Column(name = "UPDATE_DATE")
	    private String updateDate;
}
