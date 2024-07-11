package com.valoores.v21.usm.app.securitymanagement.accessrights.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMMultiMiscDto;
import com.valoores.v21.usm.backend.CustomResponse;

public interface IUSMAccessService {

    //public List<USMAccessDto> getAllUSMAccess(long id,String menuvar,long appName,String appMenu,long subLevel,long inColumn);

    List<ObjectNode> getAllUSMAccess(long id,String userRoleId,String menuvar,long appName,String appMenu,String subLevel,String inColumn);

   // public CustomResponse updateUSMAccess(ArrayList<USMAccessDto> usmAccessDtoObjList, String menuPaths, Integer userLogedId);

    public CustomResponse AddUSMApplication(USMMultiMiscDto usmMultiMiscDto);
	public CustomResponse DeleteUSMApplication(long id, String menupaths, String menu);

    	
	List<ObjectNode> comboAppType();

	List<ObjectNode> getAccessRightsGrid(String menuPCode);

}
