package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name ="CFG_OBJECT_REPORT", schema =USMDBA )
@Data
public class CfgGlobalReportLinkModel {
	@Id
    @Column(name = "OBJECT_REPORT_ID")
    private long OBJECT_REPORT_ID ;

	 @Column(name = "REPORT_ID")
	    private long REPORT_ID;
	 
	 @Column(name = "OBJECT_KPI_ID")
	    private long OBJECT_KPI_ID;
	 
	 @Column(name = "CREATION_DATE")
	    private String CREATION_DATE;
	 
	 @Column(name = "CREATED_BY")
	    private String CREATED_BY;
	 
	 @Column(name = "UPDATED_BY")
	    private String UPDATED_BY;
	 
	 @Column(name = "UPDATE_DATE")
	    private String UPDATE_DATE;
}

