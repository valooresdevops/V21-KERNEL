package com.valoores.inDisplayApplication.app.objectBuilder.model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="USM_MENU", uniqueConstraints = {@UniqueConstraint(columnNames = {"MENU_CODE", "MENU_VARIABLE"})})

@FilterDef(name="MenuFilter", parameters={   @ParamDef( name="mType"   , type="integer" ),
                                             @ParamDef( name="mManaged", type="string" )})

@Filters({  @Filter(name="MenuFilter", condition="MENU_TYPE    IN (:mType)"),
            @Filter(name="MenuFilter", condition="MENU_MANAGED IN (:mManaged)")
         })
@Proxy(lazy = false)
public class Menu implements Serializable
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5278411623541042118L;

    @Id
    @Column(name = "MENU_CODE", unique = true, nullable = false, length = 50)
    private String menuCode;
    
    @Column(name = "MENU_NAME", nullable = false, length = 100)
    private String menuName;
    
    @Column(name = "MENU_VARIABLE", unique = true, nullable = false, length = 100)
    private String menuVariable;
    
    @Column(name = "MENU_MANAGED", nullable = false, length = 60)
    private String menuManaged;
    
    @Column(name = "MENU_TYPE", nullable = false, length = 5)
    private Integer menuType;
    
    @ManyToOne
    @JoinColumn(name = "MENU_P_CODE", referencedColumnName="MENU_CODE", nullable = false)
    @NotFound(action=NotFoundAction.IGNORE)
    private Menu menuParent;     

    @JsonIgnore
    @OneToMany (mappedBy = "menuParent",cascade= CascadeType.ALL)
    @Filters({  @Filter(name="MenuFilter", condition="MENU_TYPE    IN (:mType)"),
                @Filter(name="MenuFilter", condition="MENU_MANAGED IN (:mManaged)")
     })
    private Set<Menu> childMenus = new HashSet<Menu>();
    
    
    @Formula("(select count(1) from usm_menu  o where o.menu_p_code = MENU_CODE and o.MENU_TYPE = 1)")
    private String isLeaf;
    
    
    
    public String getIsLeaf()
    {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public Set<Menu> getChildMenus()
    {
        return childMenus;
    }

    public void setChildMenus(Set<Menu> childMenus)
    {
        this.childMenus = childMenus;
    }

    public String getMenuCode()
    {
        return menuCode;
    }

    public void setMenuCode(String menuCode)
    {
        this.menuCode = menuCode;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuVariable()
    {
        return menuVariable;
    }

    public void setMenuVariable(String menuVariable)
    {
        this.menuVariable = menuVariable;
    }

    public String getMenuManaged()
    {
        return menuManaged;
    }

    public void setMenuManaged(String menuManaged)
    {
        this.menuManaged = menuManaged;
    }

    public Menu getMenuParent()
    {
        return menuParent;
    }

    public void setMenuParent(Menu menuParent)
    {
        this.menuParent = menuParent;
    }
    
    public Integer getMenuType()
    {
        return menuType;
    }

    public void setMenuType(Integer menuType)
    {
        this.menuType = menuType;
    }

    /*
    public Menu()
    {
        
    }
    
    public Menu(String menuCode, String menuName, String menuVariable, Boolean menuManaged, Menu menuParent, Integer menuType)
    {
        this.menuCode     = menuCode;
        this.menuName     = menuName;
        this.menuVariable = menuVariable;
        this.menuManaged  = menuManaged;
        this.menuParent   = menuParent;
        this.menuType     = menuType;
    }
    */
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Menu))
            return false;
        Menu other = (Menu) obj;
        if (getMenuVariable() != other.getMenuVariable() && getMenuCode() != other.getMenuCode())
            return false;
        return true;
    }
    
    @Override
    public String toString()
    {
        return "Menu [ID=" + getMenuCode() + ", Name=" + getMenuName() +" , Managed="+ getMenuManaged() +"]";
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        int mc = 0;
        try
        {
            mc = (int) Math.floor(SecureRandom.getInstanceStrong().nextInt());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        result = prime * result + (int) (mc ^ (mc >>> 32));
        return result;
    }
}