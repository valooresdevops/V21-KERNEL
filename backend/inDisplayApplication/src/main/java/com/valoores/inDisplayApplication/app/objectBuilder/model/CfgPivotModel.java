package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_PIVOT", schema =SUITEDBA )
@Data
public class CfgPivotModel {
	@Id
    @Column(name = "PIVOT_ID")
    private long PIVOT_ID;

	 @Column(name = "GL_INFO_ID")
	    private long GL_INFO_ID;
	 
	 @Column(name = "QUERY_ID")
	    private long QUERY_ID;
	 
	 @Column(name = "PIVOT_NAME")
	    private String PIVOT_NAME;
	 
	 @Column(name = "CREATION_DATE")
	    private String CREATION_DATE;
	 
	 @Column(name = "CREATED_BY")
	    private String CREATED_BY;
	 
	 @Column(name = "UPDATED_BY")
	    private String UPDATED_BY;
	 
	 @Column(name = "UPDATE_DATE")
	    private String UPDATE_DATE;

}
