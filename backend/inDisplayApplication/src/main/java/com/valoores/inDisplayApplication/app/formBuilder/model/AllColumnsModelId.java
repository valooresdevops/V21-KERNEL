package com.valoores.inDisplayApplication.app.formBuilder.model;

import java.io.Serializable;

import lombok.Data;
@Data
public class AllColumnsModelId implements Serializable {
    private String owner;
    private String tableName;
	private String columnName;

    
}
