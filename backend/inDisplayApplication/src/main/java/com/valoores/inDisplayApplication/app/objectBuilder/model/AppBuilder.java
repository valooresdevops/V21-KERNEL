package com.valoores.inDisplayApplication.app.objectBuilder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.format.annotation.DateTimeFormat;

//import com.valoores.invia.model.query.Query;
//import com.valoores.usm.model.common.EmployeeCommon;
//import com.valoores.usm.model.common.Menu;

@Entity
@Table(name="CFG_GLOBAL_INFO")
public class AppBuilder implements Serializable
{

	private static final long serialVersionUID = 1282123829884305185L;

	@Id
    @Column(name = "GL_INFO_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer glInfoId;
    
//    @OneToOne
//    @JoinColumn(name = "MENU_CODE",referencedColumnName="MENU_CODE")
//    private Menu menu;
    
    @Column(name = "GL_INFO_TITLE", length = 250)
    private String glInfoTitle;
    
    
    @Column(name = "GL_INFO_DESC", length = 300)
    private String glInfoDesc;
    
    
    @Column(name = "GL_INFO_SEARCHKEY", length = 300)
    private String glInfoSearchKey;
    
    @Column(name = "GL_INFO_BOXTYPE", length = 1)
    private String glInfoBoxType;
    
    @Column(name = "GL_INFO_DISP", length = 6)
    private String glInfoDisplay;
    
    @Column(name = "GL_INFO_FONTSIZE", length = 1)
    private String glInfoFontSize;
    
    @Column(name = "GL_INFO_FONTTYPE", length = 1)
    private String glInfoFontType;
    
    @Column(name = "GL_INFO_COLOR", length = 1)
    private String glInfoColor;
    
//    @OneToOne
//    @JoinColumn(name = "QRY_ID")
//    private Query query;
//    
//    @OneToOne
//    @JoinColumn(name = "CREATED_BY")
//    private EmployeeCommon createdBy;
//    
//    
//    @OneToOne
//    @JoinColumn(name = "UPDATED_BY")
//    private EmployeeCommon updatedBy;


    @Column(name = "IS_PUBLISH", length = 1)
    private Boolean isPublish;

    
    @Column(name = "CREATION_DATE", nullable = false)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date creationDate;

   
    @Column(name = "UPDATE_DATE", nullable = false)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date updateDate;
    
    @Transient
    private Boolean hasChart;
    @Transient
    private Boolean hasGrid;
    @Transient
    private Boolean hasReport;
    @Transient
    private Boolean hasPivot;
    @Transient
    private Boolean hasTree;
    @Transient
    private Boolean hasSpreadSheet;
    
    @PostLoad
    public void test()
    {
    	System.out.println("\n\n\n\n\n\n\n============in post load");
    }
    
//	public EmployeeCommon getCreatedBy()
//    {
//        return createdBy;
//    }


//    public void setCreatedBy(EmployeeCommon createdBy)
//    {
//        this.createdBy = createdBy;
//    }
//
//
//    public EmployeeCommon getUpdatedBy()
//    {
//        return updatedBy;
//    }
//
//
//    public void setUpdatedBy(EmployeeCommon updatedBy)
//    {
//        this.updatedBy = updatedBy;
//    }


    public Date getCreationDate()
    {
        return creationDate;
    }


    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }


    public Date getUpdateDate()
    {
        return updateDate;
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }


    public Boolean getHasChart()
    {
		return hasChart;
	}


	public void setHasChart(Boolean hasChart)
	{
		this.hasChart = hasChart;
	}


	public Boolean getHasGrid()
	{
		return hasGrid;
	}


	public void setHasGrid(Boolean hasGrid) 
	{
		this.hasGrid = hasGrid;
	}


	public Boolean getHasReport()
	{
		return hasReport;
	}


	public void setHasReport(Boolean hasReport) 
	{
		this.hasReport = hasReport;
	}


	public Boolean getHasPivot()
	{
		return hasPivot;
	}


	public void setHasPivot(Boolean hasPivot) 
	{
		this.hasPivot = hasPivot;
	}


	public Boolean getHasTree() 
	{
		return hasTree;
	}


	public void setHasTree(Boolean hasTree) 
	{
		this.hasTree = hasTree;
	}


	public Boolean getHasSpreadSheet() 
	{
		return hasSpreadSheet;
	}


	public void setHasSpreadSheet(Boolean hasSpreadSheet) 
	{
		this.hasSpreadSheet = hasSpreadSheet;
	}


	public String getGlInfoFontSize() 
	{
		return glInfoFontSize;
	}


	public void setGlInfoFontSize(String glInfoFontSize)
	{
		this.glInfoFontSize = glInfoFontSize;
	}


	public String getGlInfoFontType()
	{
		return glInfoFontType;
	}


	public void setGlInfoFontType(String glInfoFontType)
	{
		this.glInfoFontType = glInfoFontType;
	}


	public String getGlInfoColor() 
	{
		return glInfoColor;
	}


	public void setGlInfoColor(String glInfoColor)
	{
		this.glInfoColor = glInfoColor;
	}


	public String getGlInfoBoxType() 
	{
		return glInfoBoxType;
	}


	public void setGlInfoBoxType(String glInfoBoxType) 
	{
		this.glInfoBoxType = glInfoBoxType;
	}


	public String getGlInfoDisplay()
	{
		return glInfoDisplay;
	}


	public void setGlInfoDisplay(String glInfoDisplay) 
	{
		this.glInfoDisplay = glInfoDisplay;
	}


	public Integer getGlInfoId()
	{
		return glInfoId;
	}


	public void setGlInfoId(Integer glInfoId)
	{
		this.glInfoId = glInfoId;
	}


//	public Menu getMenu() 
//	{
//		return menu;
//	}
//
//
//	public void setMenu(Menu menu) 
//	{
//		this.menu = menu;
//	}


	public String getGlInfoTitle()
	{
		return glInfoTitle;
	}


	public void setGlInfoTitle(String glInfoTitle) 
	{
		this.glInfoTitle = glInfoTitle;
	}


	public String getGlInfoDesc()
	{
		return glInfoDesc;
	}


	public void setGlInfoDesc(String glInfoDesc) 
	{
		this.glInfoDesc = glInfoDesc;
	}


	public String getGlInfoSearchKey()
	{
		return glInfoSearchKey;
	}


	public void setGlInfoSearchKey(String glInfoSearchKey) 
	{
		this.glInfoSearchKey = glInfoSearchKey;
	}


//	public Query getQuery()
//	{
//		return query;
//	}
//
//
//	public void setQuery(Query query) 
//	{
//		this.query = query;
//	}


	

	public Boolean getIsPublish() 
	{
		return isPublish;
	}


	public void setIsPublish(Boolean isPublish) 
	{
		this.isPublish = isPublish;
	}


	
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof AppBuilder))
            return false;

        AppBuilder other = (AppBuilder) obj;
        return getGlInfoId().equals(other.getGlInfoId());
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getGlInfoId()).append(getGlInfoTitle()).toHashCode();
    }

    @Override
    public String toString()
    {
        return "AppChart [ID=" + getGlInfoId() + ", Name=" + getGlInfoTitle() + "]";
    }

    
}
