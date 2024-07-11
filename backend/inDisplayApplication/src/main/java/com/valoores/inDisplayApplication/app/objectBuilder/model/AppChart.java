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

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.valoores.invia.model.appbuilder.AppBuilder;
//import com.valoores.invia.model.query.Query;
//import com.valoores.usm.model.common.EmployeeCommon;
//import com.valoores.usm.model.common.KrSysLines;

@Entity
@Table(name = "CFG_OBJECT_CHART", schema =SUITEDBA )
@Data
public class AppChart implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6313790008194583597L;

    @Id   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OBJECT_CHART")
    @SequenceGenerator(name = "S_OBJECT_CHART", schema = "SUITEDBA", sequenceName = "S_OBJECT_CHART", allocationSize = 1)
    @Column(name = "OBJECT_CHART_ID")
    private long chartId;

    @Column(name = "CHART_NAME")
    private String chartName;

    @Column(name = "OBJECT_KPI_ID")
    private long objectKpiId;

    @Column(name = "QBE_ID")
    private long query;

    @Column(name = "CHART_SIZE")
    private Integer chartSize;

    @Column(name = "CHART_H_TITLE")
    private String chartHTitle;
    
    @Column(name = "CHART_V_TITLE")
    private String chartVTitle;
    
    @Column(name = "IS_SHOW_LEGEND_ENABLED")
    private String showLegend;
    
    @Column(name = "IS_3D")
    private String is3d;
    
    
    @Column(name = "IS_HORIZONTAL")
    private String isHorizontal;
    
    @Column(name = "CREATION_DATE")
    private Date CREATION_DATE;
 
	 @Column(name = "CREATED_BY")
	    private long CREATED_BY;
	 
	 @Column(name = "UPDATED_BY")
	    private long UPDATED_BY;
	 
	 @Column(name = "UPDATE_DATE")
	    private Date UPDATE_DATE;
	 
	 @Column(name = "OBJECT_CHART_TYPE")
	    private Integer chartType;
	/*
	 * @JsonIgnore
	 * 
	 * @OneToMany(mappedBy = "appChart", cascade = CascadeType.ALL,fetch =
	 * FetchType.LAZY) private Set<AppChartField> chartFields = new
	 * HashSet<AppChartField>();
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "CHART_TYPE", nullable = true) private KrSysLines
	 * chartType;
	 *
	 *
	 * @Column(name = "UPDATE_DATE", unique = true, nullable = true)
	 * 
	 * @DateTimeFormat(pattern = "MM/dd/yyyy") private Calendar updateDate;
	 * 
	 * @Column(name = "CREATION_DATE", unique = true, nullable =
	 * true,insertable=false)
	 * 
	 * @DateTimeFormat(pattern = "MM/dd/yyyy") private Calendar CREATION_DATE;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "CREATED_BY") private EmployeeCommon CREATED_BY;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "UPDATED_BY") private EmployeeCommon updatedBy;
	 *
	*
	 * public Integer getChartId() { return chartId; }
	 * 
	 * public void setChartId(Integer chartId) { this.chartId = chartId; }
	 * 
	 * public String getChartName() { return chartName; }
	 * 
	 * public void setChartName(String chartName) { this.chartName = chartName; }
	 * public AppBuilder getObjectKpiId() { return objectKpiId; }
	 * 
	 * public void setObjectKpiId(AppBuilder objectKpiId) { this.objectKpiId =
	 * objectKpiId; }
	 * 
	 * public Query getQuery() { return query; }
	 * 
	 * public void setQuery(Query query) { this.query = query; }
	 * 
	 * public String getChartSize() { return chartSize; }
	 * 
	 * public void setChartSize(String chartSize) { this.chartSize = chartSize; }
	 * 
	 * 
	 * public String getChartHTitle() { return chartHTitle; }
	 * 
	 * public void setChartHTitle(String chartHTitle) { this.chartHTitle =
	 * chartHTitle; }
	 * 
	 * public String getChartVTitle() { return chartVTitle; }
	 * 
	 * public void setChartVTitle(String chartVTitle) { this.chartVTitle =
	 * chartVTitle; }
	 */
//	public Set<AppChartField> getChartFields()
//    {
//        return chartFields;
//    }
//
//    public void setChartFields(Set<AppChartField> chartFields)
//    {
//        this.chartFields = chartFields;
//    }
//
//    public KrSysLines getChartType()
//    {
//        return chartType;
//    }
//
//    public void setChartType(KrSysLines chartType)
//    {
//        this.chartType = chartType;
//    }

	/*
	 * public Calendar getUpdateDate() { return updateDate; }
	 * 
	 * public void setUpdateDate(Calendar updateDate) { this.updateDate =
	 * updateDate; }
	 * 
	 * public Calendar getCreationDate() { return creationDate; }
	 * 
	 * public void setCreationDate(Calendar creationDate) { this.creationDate =
	 * creationDate; }
	 */

//    public EmployeeCommon getCreatedBy()
//    {
//        return createdBy;
//    }
//
//    public void setCreatedBy(EmployeeCommon createdBy)
//    {
//        this.createdBy = createdBy;
//    }
//
//    public EmployeeCommon getUpdatedBy()
//    {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(EmployeeCommon updatedBy)
//    {
//        this.updatedBy = updatedBy;
//    }
    
	/*
	 * public Boolean getShowLegend() { return showLegend; }
	 * 
	 * public void setShowLegend(Boolean showLegend) { this.showLegend = showLegend;
	 * }
	 * 
	 * public Boolean getIs3d() { return is3d; }
	 * 
	 * public void setIs3d(Boolean is3d) { this.is3d = is3d; }
	 * 
	 * public Boolean getIsHorizontal() { return isHorizontal; }
	 * 
	 * public void setIsHorizontal(Boolean isHorizontal) { this.isHorizontal =
	 * isHorizontal; }
	 * 
	 * @Override public boolean equals(Object obj) { if (obj == null) return false;
	 * if (obj == this) return true; if (!(obj instanceof AppChart)) return false;
	 * 
	 * AppChart other = (AppChart) obj; return
	 * getChartId().equals(other.getChartId()) &&
	 * getChartName().equals(other.getChartName()); }
	 * 
	 * @Override public int hashCode() { return new
	 * HashCodeBuilder().append(getChartId()).append(getChartName()).toHashCode(); }
	 * 
	 * @Override public String toString() { return "AppChart [ID=" + getChartId() +
	 * ", Name=" + getChartName() + "]"; }
	 */

}
