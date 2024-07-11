package com.valoores.inDisplayApplication.app.formBuilder.dto;

import java.util.List;

import lombok.Data;

@Data
public class AfterSaveDto {

	private Integer userId;
	private List<String> Parameters;
}
