package com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicSearch;

import java.util.List;

import lombok.Data;

@Data
public class DynamicSearchDto {
private String tableName;
private String condition;
private List<DynamicFieldDto> columns;
}
