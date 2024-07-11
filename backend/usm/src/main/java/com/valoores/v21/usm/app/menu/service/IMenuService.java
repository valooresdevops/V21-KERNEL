package com.valoores.v21.usm.app.menu.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.menu.dto.USMAccessTypeDto;

public interface IMenuService {
	
	List<ObjectNode> getMenusByParentCode(String menuParentCode,String userId);

	String isManaged(String menuName);

	USMAccessTypeDto getAccessRight(String menuName, Integer userId);

}
