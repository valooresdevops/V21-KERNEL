package com.valoores.inDisplayApplication.app.formBuilder.dto;



import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Data
public class FormBuilderDto {
	@Hidden
	private long tableId;
	
	private String ownerName;
	private String tableName;
	private String creationDate;
	private long createdBy;
	private String updateDate;
	private String updatedBy;
	private long menuId;
	private long objectType;
	private long userId;
	private String menuName;
	private long isMain;
	private long columnId;
	private String columnName;
	private String columnDescription;
	private long objectPId;
	private long orderNo;
	private String isGrid;
	private String isQueryForm;
	private String isDynamicReport;
	private String isSuspended;
	private String isUnique;
	private String isHidden;
	private String isReadOnly;
	private long query;
	private String sizeField;
	private String isAdvancedSearch;
	private long sourceQuery;
	private long canAdd;
	private long canModify;
	private long canDelete;
	private long condition;
	private String rowSlectedStatus;
	private String isMandatory;
	private long columnType;
	private long columnLength;
	private String isMultiple;
	private long destinationField;
	private String isLink;
	private String isGridHidden;

	private long qbeReadOnly;
	private long defaultValue;
	private Long fieldSet;	
	private Long menus;
	private long isSave;
	private String isExcluded;
	private String isArabic;
	private String isEditable;
	private long dependencyDefaultValue;
	private long readOnlyQbeId;
	private long mandatoryQuery;
	private String advancedSearchProcedureName;
	private String isQueryFormSelectedButtons;

	
}
