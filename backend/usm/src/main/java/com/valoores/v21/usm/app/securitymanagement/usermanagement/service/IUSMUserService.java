package com.valoores.v21.usm.app.securitymanagement.usermanagement.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMUser;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMUserDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMUserGridDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.UserLogsDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMCurrency;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMLanguage;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMRefSysParamLines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMStatus;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMSyslines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserRoles;
import com.valoores.v21.usm.backend.CustomResponse;
import com.valoores.v21.usm.backend.LoginResponse;
import com.valoores.v21.usm.backend.UserCredentials;

public interface IUSMUserService {
	
	public List<ObjectNode> getAllUSMUsers();
	public USMUserDto getUSMUserById(long id);
	public List<USMUser> getUSMUsersUsernameContaining(String username);
	public CustomResponse addUSMUser(USMUserDto usmUserDto);
	public CustomResponse updateUSMUser(USMUserDto usmUserDto);
	public CustomResponse deleteUSMUser(long id);
	//public LoginResponse userLogin(UserCredentials credentials);
	public Object getIdAndCreationDate(long id);
	public List<USMStatus> getStatusCombo();
	public List<USMLanguage> getLanguageCombo();
	public List<USMSyslines> getBugTypeCombo();
	public List<USMRefSysParamLines> getCivilStatusCombo();
	public List<USMCurrency> getCurrencyCombo();
	public List<ObjectNode> getBugNameCombo(String id,Integer userId); 
	public List<USMUserRoles> getRoleCombo();
	//public List<USMUserRoles> getDefaultRoleCombo(USMComboDto usmComboDto);
	
	public LoginResponse userLogin(UserCredentials credentials);
	public void insertCaptchaSession(String captchaSerial,String code);
	public int checkCaptcha(String captchaField,String captchaSerial);
	//public void insertInDisplayLogsData(UserLogsDto userLogsDto);
	public List<ObjectNode> getInDisplayLogs();
    public String getInDisplayLogsDetails(long logId);

	
}