package com.valoores.v21.usm.app.common.menucombo.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USM_MENU", schema = USMDBA)
@Getter
@Setter	
@NoArgsConstructor
public class USMMenu {
	
	@Id
	@Column(name = "MENU_CODE")
	private String menuCode;
	
	@Column(name = "MENU_NAME")
	private String menuName;
	
	@Column(name = "MENU_VARIABLE")
	private String menuVar;
	
	@Column(name = "MENU_P_CODE")
	private String menuPCode;
	
	@Column(name = "MENU_MANAGED")
	private String menuManaged;
	
	@Column(name = "MENU_TYPE")
	private Integer menuType;
	
	@Column(name = "MENU_ORDER")
	private Integer menuOrder;
	
	@Column(name = "MENU_ACCEPT_REPORT")
	private String menuAcceptReport;
	
	@Column(name = "MENU_VERSION")
	private String menuVersion;
	
	@Column(name="MENU_ICON_DESC")
	private String menuIcon;
	
	@Column(name = "CAN_BE_LOGIN_PAGE")
	private String canBeLogInPage;
	
	@Column(name = "CREATION_DATE")
	@Transient
	private Date creationDate;
	
	@Column(name= "TECH_MENU_PATH")
	private String menuPath; 
	
	
	
	
	
}
	
	
	