package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import java.io.Serializable;
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
@Table(name="CFG_OBJECT_CHART_FIELD", schema=SUITEDBA)
@Data
public class AppChartField implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3142246312727112451L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OBJECT_CHART_FIELD")
    @SequenceGenerator(name = "S_OBJECT_CHART_FIELD", schema = "SUITEDBA", sequenceName = "S_OBJECT_CHART_FIELD", allocationSize = 1)
	@Column(name="OBJECT_CHART_FIELD_ID")
	private long fieldId;
	
	@Column(name="OBJECT_CHART_ID")
	private long chartId;
	
	@Column(name="FIELD_NAME")
	private String fieldName;
	
	@Column(name="TECHNICAL_FIELD_NAME")
	private String queryFieldName;

	@Column(name="FIELD_COLOR")
	private String fieldColor;
	
	@Column(name="IS_SERIE")
	private String isSerie;
	
	@Column(name="CHART_FIELD_TYPE")
	private Integer serieType;
	
	@Column(name="IS_DRILLDOWN")
	private String drilldown;
	
	@Column(name="DRILLDOWN_TYPE")
	private Integer drilldownType;
	
	 @Column(name = "CREATION_DATE")
	 private Date creationDate;
	 
	 @Column(name = "CREATED_BY")
	    private long CREATED_BY;
	 
	 @Column(name = "UPDATED_BY")
	    private long UPDATED_BY;
	 
	 @Column(name = "UPDATE_DATE")
	    private Date UPDATE_DATE;	
	/*
	 * @Transient private String serieTypeName;
	 * 
	 * @Transient private String drilldownTypeName;
	 */
	
	/*
	 * public String getSerieTypeName() { return serieTypeName; }
	 * 
	 * public void setSerieTypeName(String serieTypeName) { this.serieTypeName =
	 * serieTypeName; }
	 * 
	 * public Integer getFieldId() { return fieldId; }
	 * 
	 * public void setFieldId(Integer fieldId) { this.fieldId = fieldId; }
	 * 
	 * public AppChart getAppChart() { return appChart; }
	 * 
	 * public void setAppChart(AppChart appChart) { this.appChart = appChart; }
	 * 
	 * public String getFieldName() { return fieldName; }
	 * 
	 * public void setFieldName(String fieldName) { this.fieldName = fieldName; }
	 * 
	 * public String getQueryFieldName() { return queryFieldName; }
	 * 
	 * public void setQueryFieldName(String queryFieldName) { this.queryFieldName =
	 * queryFieldName; }
	 * 
	 * 
	 * public Boolean getIsSerie() { return isSerie; }
	 * 
	 * public String getFieldColor() { return fieldColor; }
	 * 
	 * public void setFieldColor(String fieldColor) { this.fieldColor = fieldColor;
	 * }
	 * 
	 * public Integer getDrilldownType() { return drilldownType; }
	 * 
	 * public void setIsSerie(Boolean isSerie) { this.isSerie = isSerie; }
	 * 
	 * public Integer getSerieType() { return serieType; }
	 * 
	 * public void setSerieType(Integer serieType) { // if(serieType!=null) // { //
	 * if(serieType == ConstantInvia.CHART_FIELD_COLUMN) // { //
	 * setSerieTypeName(ConstantInvia.CHART_FIELD_COLUMN_NAME); // } // else
	 * if(serieType == 2) // { //
	 * setSerieTypeName(ConstantInvia.CHART_FIELD_ROW_NAME); // } // }
	 * this.serieType = serieType; }
	 * 
	 * public Integer getDrilldown() { return drilldown; }
	 * 
	 * public void setDrilldown(Integer drilldown) { this.drilldown = drilldown; }
	 * 
	 * public String getDrilldownTypeName() { return drilldownTypeName; }
	 * 
	 * public void setDrilldownTypeName(String drilldownTypeName) {
	 * this.drilldownTypeName = drilldownTypeName; }
	 * 
	 * public void setDrilldownType(Integer drilldownType) { //
	 * if(drilldownType!=null) // { // if(drilldownType ==
	 * ConstantInvia.INVIA_CHART) //
	 * setDrilldownTypeName(ConstantInvia.INVIA_CHART_NAME); // if(drilldownType ==
	 * ConstantInvia.INVIA_GRID) //
	 * setDrilldownTypeName(ConstantInvia.INVIA_GRID_NAME); // } this.drilldownType
	 * = drilldownType; }
	 */

	/*
	 * @Override public boolean equals(Object obj) { if (obj == null) return false;
	 * if (obj == this) return true; if (!(obj instanceof AppChartField)) return
	 * false;
	 * 
	 * AppChartField other = (AppChartField) obj; return
	 * getFieldId().equals(other.getFieldId()) &&
	 * getFieldName().equals(other.getFieldName()); }
	 * 
	 * @Override public int hashCode() { return new
	 * HashCodeBuilder().append(getFieldId()) .append(getFieldName()) .toHashCode();
	 * }
	 * 
	 * @Override public String toString() { return "AppChartField [ID=" +
	 * getFieldId() + ", Name=" + getFieldName() +"]"; }
	 */
}
