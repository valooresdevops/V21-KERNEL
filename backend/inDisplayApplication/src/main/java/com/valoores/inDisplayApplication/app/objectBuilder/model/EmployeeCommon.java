package com.valoores.inDisplayApplication.app.objectBuilder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name="HR_EMPLOYEE")
@Immutable
public class EmployeeCommon implements Serializable
{
    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -1562994248131807315L;

    @Id
    @Column(name = "EMP_ID", unique = true, nullable = false, length = 10)
    private Integer employeeId;
    
    @Column(name = "EMP_FNAME", nullable = false, length = 35)
    private String fname;
    
    @Column(name = "EMP_NAME", nullable = false, length = 35)
    private String name;
    
    @Formula(value = "concat(EMP_FNAME, EMP_NAME)")
    private String fullName;
    
    public String getFullName() 
    {
		return fullName;
	}

	public void setFullName(String fullName) 
	{
		this.fullName = fullName;
	}

	public static long getSerialversionuid() 
	{
		return serialVersionUID;
	}

	public Integer getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getFname()
    {
        return fname;
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
	@Override
    public boolean equals(Object obj)
    {        
        if (obj == null)
            return false;
        if (obj == this) 
            return true;
        if (!(obj instanceof EmployeeCommon))
            return false;

        EmployeeCommon other = (EmployeeCommon) obj;
        return new EqualsBuilder().append(getFname(),  other.getFname())
                                  .append(getName(),   other.getName())
                                  .isEquals();
    }
    
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getEmployeeId())
                                    .append(getFname())
                                    .append(getName())
                                    .toHashCode();              
    }
    
    @Override
    public String toString()
    {
        return "Employee Common [ID=" + getEmployeeId() +", First Name = "+ getFname() +", Name = "+ getName() +"]";
    }
}