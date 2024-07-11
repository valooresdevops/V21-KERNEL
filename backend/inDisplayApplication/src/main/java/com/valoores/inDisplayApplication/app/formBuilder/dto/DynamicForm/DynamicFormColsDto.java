package com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm;




import lombok.Data;

@Data
public class DynamicFormColsDto {
    private String colName;
    private String colValue;
    private String colType;
    private String orderNo;
    private String isLink;
    private String linkedObjectId;
    private long colId;
    private long colTypeInputCode;


}
