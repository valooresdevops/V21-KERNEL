package com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;

@Data
public class QueryFormDto {
	
	int ruleCode;
	int qbeId;
	List<ObjectNode> paramKeysValues;
	int userId;
	long conditionId;
}
