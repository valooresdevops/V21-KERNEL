package com.valoores.v21.usm.app.securitymanagement.accessrights.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserMulti;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usm_user_misc_info", schema = USMDBA)
@Data
@NoArgsConstructor
public class USMUser {
	
	public USMUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_ID")
	@SequenceGenerator(name = "USER_SEQ_ID", schema = "USMDBA", sequenceName = "S_USER_MISC_INFO", allocationSize = 1)
	@Column(name = "USER_ID")
	private long id;
	
	@Column(name = "USER_LOGIN", unique = true)
	@NotBlank(message = "Username cannot be empty!")
	private String username;
	
	@Column(name = "USER_PWD")
	@NotBlank(message = "Password cannot be empty!")
	private String password;
	
	@Column(name = "IS_PWD_LDAP_AUTHENTIFIED")
	private String isPwdLdapAuth;
	
	@Column(name = "LAN_ID")
	private Integer lanId;
	
	@Column(name = "USER_SETTING_ID")
	private Integer numberSetting;
	
	@Column(name = "CUR_ID")
	private Integer currency;
	
	@Column(name = "USER_CIVILITY")
	private Integer civilStatus;
	
	@Column(name = "CHL1_TYPE_CODE")
	@JsonIgnore
	private Integer emailCode;
	
	@Column(name = "CHL1_TYPE_VALUE")
	private String email;
	
	@Column(name = "CHL2_TYPE_CODE")
	@JsonIgnore
	private Integer mobileCode;
	
	@Column(name = "CHL2_TYPE_VALUE")
	private String mobile;
	
	@Column(name = "CHL3_TYPE_CODE")
	@JsonIgnore
	private Integer phoneCode;
	
	@Column(name = "CHL3_TYPE_VALUE")
	private String phone;
	
	@Column(name = "FIRST_ADR_DESC")
	private String firstAddress;
	
	@Column(name = "SCND_ADR_DESC")
	private String secondAddress;
	
	@Column(name = "USER_IMAGE")
	private byte[] media;
	
	@Column(name = "USERD_DOB")
	private Date dateOfBirth;
	
	@Column(name = "USER_SEX")
	private String gender;
	
	@Column(name = "USER_FIRST_NAME")
	private String firstName;
	
	@Column(name = "USER_LAST_NAME")
	private String lastName;
	
	@Column(name = "USER_FULL_NAME")
	private String fullName;
	
	@Column(name = "POSTAL_CODE")
	private String postalCode;
	
	@Column(name = "STATUS_CODE")
	private Integer status;
	
	@Column(name = "PWD_EXP_DATE")
	private Date pwdExpDate;
	
	@Column(name = "PWD_PERIOD_NBR")
	private Integer pwdExpPrdNbr;
	
	@Column(name = "PWD_PERIOD_TYPE")
	private Integer pwdExpPrd;
	
	@Column(name = "PWD_UPDATE_DATE")
	private Date pwdUpdateDate;
	
	@Column(name = "IS_PWD_TO_CHANGE")
	private String changePassword;
//	
//	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
//	@JoinTable(name = "usm_user_multi_misc_info", schema = USMDBA, joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
//			@JoinColumn(name = "ROLE_ID") })
//	private Set<USMRole> usmRoles;
//	
//	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
//	@JoinTable(name = "usm_user_multi_misc_info", schema = USMDBA, joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
//			@JoinColumn(name = "BSN_GROUP_ID") })
//	private Set<USMMdmBsnUnitGroup> usmMdmBsnUnitGroup;
//	
//	
//    @OneToOne(mappedBy = "usmUser")
//    private USMStatus usmStatus;
	
   @OneToMany(mappedBy="usmUser")
    private Set<USMUserMulti> usmUserMulti;
	  
	@Column(name = "CREATION_DATE")
	@Hidden
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private long createdBy;
	
	@Column(name = "UPDATE_DATE")
	@Hidden
	private Date updateDate;
	
	@Column(name = "UPDATED_BY")
	@Hidden
	private long updatedBy;
	
}
