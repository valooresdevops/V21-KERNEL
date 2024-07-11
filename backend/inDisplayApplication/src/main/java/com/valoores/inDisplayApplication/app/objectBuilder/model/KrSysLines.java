package com.valoores.inDisplayApplication.app.objectBuilder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Immutable;


@Entity
@Table(name = "REF_SYS_LINES")
@Immutable
public class KrSysLines implements Serializable
{
    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -1706708746815279305L;

    @Id
    @Column(name = "LIN_CODE", unique = false, nullable = false, length = 5)
    private Integer id;
    
    @Column(name = "LIN_NAME", unique = false, nullable = false, length = 200)
    private String name;
    
    @Column(name = "HEA_CODE", unique = false, nullable = false, length = 5)
    private Integer heaCode;
    
    /*
    @Id
    @Formula("concat(heaCode,'_', id)")
    private String ids;
    */

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getHeaCode()
    {
        return heaCode;
    }

    public void setHeaCode(Integer heaCode)
    {
        this.heaCode = heaCode;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this) 
            return true;
        if (!(obj instanceof KrSysLines))
            return false;

        KrSysLines other = (KrSysLines) obj;
        return new EqualsBuilder().append(getId(),       other.getId())
                                  .append(getHeaCode(),  other.getHeaCode())
                                  .isEquals();
    }
    
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getHeaCode())
                                    .append(getId())
                                    .append(getName())
                                    .toHashCode();   
    }
    
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ConstantUSM.TO_STRING_STYLE);
    }   
}