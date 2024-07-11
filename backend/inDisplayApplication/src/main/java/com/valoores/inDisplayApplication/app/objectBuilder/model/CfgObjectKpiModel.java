package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

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
@Table(name = "CFG_OBJECT_KPI", schema =SUITEDBA )
@Data
public class CfgObjectKpiModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_object_kpi")
    @SequenceGenerator(name = "s_object_kpi", schema = "SUITEDBA", sequenceName = "s_object_kpi", allocationSize = 1)
    @Column(name = "OBJECT_KPI_ID")
    private long kpiId ;
	
	@Column(name = "KPI_NAME")
    private String kpiName;
	
	@Column(name = "QBE_ID")
    private long qbeId;

	 @Column(name = "IS_RATIO")
	    private String isRatio;
	 
	 @Column(name = "MAIN_VALUE")
	    private String mainValue;
	 
	 @Column(name = "MAIN_LABEL")
	    private String mainLabel;
	 
	 @Column(name = "EXTRA_LABEL")
	    private String extraLabel;
	 
	 @Column(name = "EXTRA_VALUE")
	    private String extraValue;
	 
	 @Column(name = "IS_PERCENTAGE")
	    private String isPercentage;	 
	 
	 @Column(name = "BACKGROUND_COLOR")
	    private String backgroundColor;
	 
	 @Column(name = "TEXT_COLOR")
	    private String textColor;
	 
	 @Column(name = "HAS_CHART")
	    private String chart;
	 
	 @Column(name = "HAS_GRID")
	    private String grid;
	 
	 @Column(name = "HAS_REPORT")
	    private String report;
	 
	 @Column(name = "CREATION_DATE")
	    private Date creationDate;
	 
	 @Column(name = "CREATED_BY")
	    private long createdBy;
	 
	 @Column(name = "UPDATED_BY")
	    private long updatedBy;
	 
	 @Column(name = "UPDATE_DATE")
	    private Date updateDate;

}
