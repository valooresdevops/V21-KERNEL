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

@Entity
@Table(name="CFG_OBJECT_GRID" , schema=SUITEDBA)
@Data
public class AppGrid implements Serializable
{
    /**
     * 
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_object_grid")
    @SequenceGenerator(name = "s_object_grid", schema = "SUITEDBA", sequenceName = "s_object_grid", allocationSize = 1)

    @Column(name = "OBJECT_GRID_ID")
    private long gridId;

    @Column(name = "OBJECT_KPI_ID")
    private long objectKpiId;
    
    @Column(name="QBE_ID")
    	private Integer query;
    
    @Column(name = "GRID_NAME")
    	private String gridName;
    
    @Column(name = "CREATION_DATE")
    	private Date creationDate;
 
	 @Column(name = "CREATED_BY")
	    private long createdBy;
	 
	 @Column(name = "UPDATED_BY")
	    private long updatedBy;
	 
	 @Column(name = "UPDATE_DATE")
	    private Date updatedDate;
    
	/*
	 * @Column(name = "UPDATE_DATE", unique = true, nullable = true)
	 * 
	 * @DateTimeFormat(pattern = "MM/dd/yyyy") private Calendar updateDate;
	 */
    
	/*
	 * @Column(name = "CREATION_DATE", unique = true, nullable = true) private
	 * String creationDate;
	 */
  
	/*
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "CREATED_BY") private EmployeeCommon createdBy;
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "UPDATED_BY") private EmployeeCommon updatedBy;
	 */
	/*
	 * public Integer getGridId() { return gridId; }
	 * 
	 * public void setGridId(Integer gridId) { this.gridId = gridId; }
	 * 
	 * public AppBuilder getGlInfo() { return glInfo; }
	 * 
	 * public void setGlInfo(AppBuilder glInfo) { this.glInfo = glInfo; }
	 * 
	 * public String getGridName() { return gridName; }
	 * 
	 * public void setGridName(String gridName) { this.gridName = gridName; }
	 * 
	 * public Calendar getUpdateDate() { return updateDate; }
	 * 
	 * public void setUpdateDate(Calendar updateDate) { this.updateDate =
	 * updateDate; }
	 * 
	 * public Calendar getCreationDate() { return creationDate; }
	 * 
	 * public void setCreationDate(Calendar creationDate) { this.creationDate =
	 * creationDate; }
	 * 
	 * public EmployeeCommon getCreatedBy() { return createdBy; }
	 * 
	 * public void setCreatedBy(EmployeeCommon createdBy) { this.createdBy =
	 * createdBy; }
	 * 
	 * public EmployeeCommon getUpdatedBy() { return updatedBy; }
	 * 
	 * public void setUpdatedBy(EmployeeCommon updatedBy) { this.updatedBy =
	 * updatedBy; }
	 * 
	 * public Query getQuery() { return query; }
	 * 
	 * public void setQuery(Query query) { this.query = query; }
	 */
	/*
	 * @Override public boolean equals(Object obj) { if (obj == null) return false;
	 * if (obj == this) return true; if (!(obj instanceof AppGrid)) return false;
	 * 
	 * AppGrid other = (AppGrid) obj; return
	 * getOBJECT_GRID_ID().equals(other.getOBJECT_GRID_ID()) &&
	 * getGRID_NAME().equals(other.getGRID_NAME()); }
	 * 
	 * @Override public int hashCode() { return new
	 * HashCodeBuilder().append(getOBJECT_GRID_ID()) .append(getGRID_NAME())
	 * .toHashCode(); }
	 * 
	 * @Override public String toString() { return "Grid [ID=" + getOBJECT_GRID_ID()
	 * + ", Name=" + getGRID_NAME() +"]"; }
	 */
}
