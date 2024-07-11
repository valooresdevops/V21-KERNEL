package com.valoores.inDisplayApplication.app.objectBuilder.model;


import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;



@Entity
@Table(name = "Usm_User_Misc_Info", schema =USMDBA )
@Data
public class UserModel {
	
	 @Id
	 @Column(name = "USER_ID")
	    private long USER_ID;

	    @Column(name = "LAN_ID")
	    private long LAN_ID;
	    
	    @Column(name = "USER_PWD")
	    private long USER_PWD;
	    
	    @Column(name = "CUR_ID")
	    private long CUR_ID;
	    
	    @Column(name = "USER_CIVILITY")
	    private long USER_CIVILITY;
	    
	    @Column(name = "CHL1_TYPE_CODE")
	    private long CHL1_TYPE_CODE;

	    @Column(name = "USER_LOGIN")
	    private String USER_LOGIN;

	    @Column(name = "IS_PWD_LDAP_AUTHENTIFIED")
	    private String IS_PWD_LDAP_AUTHENTIFIED;

	    @Column(name = "CHL1_TYPE_VALUE")
	    private String CHL1_TYPE_VALUE;

	    @Column(name = "CHL2_TYPE_VALUE")
	    private String CHL2_TYPE_VALUE;
	    
	    @Column(name = "CHL3_TYPE_VALUE")
	    private String CHL3_TYPE_VALUE;
	    
	    @Column(name = "CHL2_TYPE_CODE")
	    private long CHL2_TYPE_CODE;
	    
	    @Column(name = "CHL3_TYPE_CODE")
	    private long CHL3_TYPE_CODE;
	    
	    @Column(name = "FIRST_ADR_DESC")
	    private String FIRST_ADR_DESC;
	    
	    
	    @Column(name = "SCND_ADR_DESC")
	    private String SCND_ADR_DESC;

	    @Column(name = "CREATION_DATE")
	    private Date CREATION_DATE;
	 
		 @Column(name = "CREATED_BY")
		    private long CREATED_BY;
		 
		 @Column(name = "UPDATED_BY")
		    private long UPDATED_BY;
		 
		 @Column(name = "UPDATE_DATE")
		    private Date UPDATE_DATE;

}
