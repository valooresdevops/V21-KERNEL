package com.valoores.inDisplayApplication.app.formBuilder.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdvancedResult {

	private List<BeginCondition> beginCondition;
    private int field;
    private int condition;
    private String value;
    private int operator;
    private int endCondition;
}
