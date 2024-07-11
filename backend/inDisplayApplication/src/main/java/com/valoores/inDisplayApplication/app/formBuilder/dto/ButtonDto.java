package com.valoores.inDisplayApplication.app.formBuilder.dto;

import lombok.Data;

@Data
public class ButtonDto {
	private long buttonId;
	private String buttonName;
	private long position;
	private long order;
	private long fieldSet;
	private long createdBy;
	private String objectButtonId;
    private long buttonAction;
	private long updatedBy;

}
