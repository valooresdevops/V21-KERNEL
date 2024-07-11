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
@Table(name="CFG_OBJECT_GRID_FIELD", schema=SUITEDBA)
@Data
public class AppGridField
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OBJECT_GRID_FIELD")
    @SequenceGenerator(name = "S_OBJECT_GRID_FIELD", schema = "SUITEDBA", sequenceName = "S_OBJECT_GRID_FIELD", allocationSize = 1)
	@Column(name = "OBJECT_CHART_FIELD_ID")
    private Integer fieldId;
    
    @Column(name = "OBJECT_GRID_ID", nullable = false)
    private long gridId;
    
    @Column(name = "FIELD_NAME", nullable = true)
    private String fieldName;
    
    @Column(name = "TECHNICAL_FIELD_NAME", nullable = true)
    private String qryFieldName;
    
    @Column(name = "HAS_DRILLDOWN", nullable = true)
    private Integer drilldown;
    
    @Column(name = "CREATION_DATE")
    private Date creationDate;
 
	 @Column(name = "CREATED_BY")
	    private long createdBy;
	 
	 @Column(name = "UPDATED_BY")
	    private long updatedBy;
	 
	 @Column(name = "UPDATE_DATE")
	    private Date updateDate;
    
    
	/*
	 * public Integer getFieldId() { return fieldId; } public void
	 * setFieldId(Integer fieldId) { this.fieldId = fieldId; } public String
	 * getFieldName() { return fieldName; } public void setFieldName(String
	 * fieldName) { this.fieldName = fieldName; } public String getQryFieldName() {
	 * return qryFieldName; } public void setQryFieldName(String qryFieldName) {
	 * this.qryFieldName = qryFieldName; } public Integer getDrilldown() { return
	 * drilldown; } public void setDrilldown(Integer drilldown) { this.drilldown =
	 * drilldown; }
	 */
    
    
}
