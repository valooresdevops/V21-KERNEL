package com.valoores.inDisplayApplication.app.ScreenBuilder.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.ScreenBuilder.dto.ScreenDto;
import com.valoores.inDisplayApplication.backend.CustomResponse;

public interface ScreenBuilderService {

	
	public List<ObjectNode> fetchGridsTableData();

	public List<ObjectNode> fetchApplicationList();

	public List<ObjectNode> fetchParentMenuList(String menuCode);

	public int addScreen(ScreenDto screenDto);

	public List<ObjectNode> getAllScreens();

	public CustomResponse deleteScreen(long id);

	public List<ObjectNode> getScreenData(long screenId);

	public int updateScreen(ScreenDto screenDto);

	public List<ObjectNode> getScreenPreviewData(String menuVariable);


}

