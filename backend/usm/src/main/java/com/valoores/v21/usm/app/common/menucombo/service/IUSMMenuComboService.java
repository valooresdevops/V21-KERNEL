package com.valoores.v21.usm.app.common.menucombo.service;



import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;



public interface IUSMMenuComboService {
	public List<USMMenu> getMenuName();
	public List<ObjectNode> getMenuCombo(String menuCode);
	public List<ObjectNode> getMenuCombowithSubMenu(String menuCode);
	
}
