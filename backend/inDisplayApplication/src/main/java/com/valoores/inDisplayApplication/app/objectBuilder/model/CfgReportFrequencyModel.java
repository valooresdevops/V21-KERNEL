package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_REPORT_FREQUENCY", schema =SUITEDBA )
@Data
public class CfgReportFrequencyModel {
	@Id
    @Column(name = "REPORT_ID")
    private long REPORT_ID;

	 @Column(name = "REPORT_DESC")
	    private String REPORT_DESC;
	 
	 @Column(name = "REPORT_KEYWORD")
	    private String REPORT_KEYWORD;
	 
	 @Column(name = "REPORT_NAME")
	    private String REPORT_NAME;
	 
	 @Column(name = "REPORT_PATH")
	    private String REPORT_PATH;
	 
	 @Column(name = "REPORT_INTERNAL_CODE")
	    private long REPORT_INTERNAL_CODE;
	 
	 @Column(name = "REPORT_REFERENCE_CODE")
	    private long REPORT_REFERENCE_CODE;
	 
	 
	 @Column(name = "CREATION_DATE")
	    private String CREATION_DATE;
	 
	 @Column(name = "CREATED_BY")
	    private String CREATED_BY;
	 
	 @Column(name = "UPDATED_BY")
	    private String UPDATED_BY;
	 
	 @Column(name = "UPDATE_DATE")
	    private String UPDATE_DATE;
}

