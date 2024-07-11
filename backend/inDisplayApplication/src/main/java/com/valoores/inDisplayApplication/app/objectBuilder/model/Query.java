package com.valoores.inDisplayApplication.app.objectBuilder.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "CFG_QUERY")
public class Query implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -4660049356788824135L;

    @Id
    @Column(name = "QRY_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer qryId;

    @OneToOne
    @JoinColumn(name = "MENU_CODE")
    @NotFound(action = NotFoundAction.IGNORE)
    private Menu menu;

    @Column(name = "QRY_KEYWORD", length = 250)
    private String qryKeyword;

    @Column(name = "QRY_TYPE", length = 1)
    private String qryType;

    @Column(name = "QRY_SHORT_DESC", length = 250)
    private String qryShortDesc;

    @Column(name = "QRY_LONG_DESC", length = 500)
    private String qryLongDesc;

    @Column(name = "QRY_INTERFACE", length = 1)
    private String qryInterface;

    @Column(name = "QRY_PATH")
    private String qryPath;

    @Column(name = "QRY_NAME", length = 250)
    private String qryName;

    @Transient
    private String queryTextValue;

    @Transient
    private MultipartFile queryFile;

    @OneToOne
    @JoinColumn(name = "CREATED_BY")
    @NotFound(action = NotFoundAction.IGNORE)
    private EmployeeCommon createdBy;

    @OneToOne
    @JoinColumn(name = "UPDATED_BY")
    @NotFound(action = NotFoundAction.IGNORE)
    private EmployeeCommon updatedBy;

    @Transient
    private byte[] queryContet;

    public byte[] getQueryContet()
    {
        return queryContet;
    }

    public void setQueryContet(byte[] queryContet)
    {
        this.queryContet = queryContet;
    }

    public EmployeeCommon getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(EmployeeCommon createdBy)
    {
        this.createdBy = createdBy;
    }

    public EmployeeCommon getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(EmployeeCommon updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public MultipartFile getQueryFile()
    {
        return queryFile;
    }

    public void setQueryFile(MultipartFile queryFile)
    {
        this.queryFile = queryFile;
    }

    public String getQueryTextValue()
    {
        return queryTextValue;
    }

    public void setQueryTextValue(String queryTextValue)
    {
        this.queryTextValue = queryTextValue;
    }

    public Integer getQryId()
    {
        return qryId;
    }

    public void setQryId(Integer qryId)
    {
        this.qryId = qryId;
    }

    public Menu getMenu()
    {
        return menu;
    }

    public void setMenu(Menu menu)
    {
        this.menu = menu;
    }

    public String getQryKeyword()
    {
        return qryKeyword;
    }

    public void setQryKeyword(String qryKeyword)
    {
        this.qryKeyword = qryKeyword;
    }

    public String getQryType()
    {
        return qryType;
    }

    public void setQryType(String qryType)
    {
        this.qryType = qryType;
    }

    public String getQryShortDesc()
    {
        return qryShortDesc;
    }

    public void setQryShortDesc(String qryShortDesc)
    {
        this.qryShortDesc = qryShortDesc;
    }

    public String getQryLongDesc()
    {
        return qryLongDesc;
    }

    public void setQryLongDesc(String qryLongDesc)
    {
        this.qryLongDesc = qryLongDesc;
    }

    public String getQryInterface()
    {
        return qryInterface;
    }

    public void setQryInterface(String qryInterface)
    {
        this.qryInterface = qryInterface;
    }

    public String getQryPath()
    {
        return qryPath;
    }

    public void setQryPath(String qryPath)
    {
        this.qryPath = qryPath;
    }

    public String getQryName()
    {
        return qryName;
    }

    public void setQryName(String qryName)
    {
        this.qryName = qryName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (getQryId() ^ (getQryId() >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Query))
            return false;
        Query other = (Query) obj;
        if (getQryId() != other.getQryId())
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "User [id=" + getQryId() + ", User=" + getQryName() + "]";
    }

}
