package com.valoores.v21.usm.app.securitymanagement.usermanagement.dto;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMUserDto {
	
	@Hidden
	private long id;
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private String gender;
	private Integer civilStatus;
	private String isPwdLdapAuth;
	private Integer lanId ;
	private String dateOfBirth;
	private Integer numberSetting;
	private Integer currency;
	private String firstAddress;
	private String secondAddress;
	private String phone;
	private String mobile;
	private String postalCode;
	private long userId;
	private String application;
	
	@JsonIgnore
	private MultipartFile media;
	
	private byte[] userImage;
	private Integer defaultRole;
	private Set<String> usmRoles = new HashSet<>();
	private Set<String> usmMdmBsnUnitGroup = new HashSet<>();
	private Integer status ;
	private Integer bugType;
	private Integer pwdExpPrdNbr;
	private String pwdExpPrd;
	private String pwdExpDate;
	private String changePassword;
	@Hidden
	private String creationDate;

}
