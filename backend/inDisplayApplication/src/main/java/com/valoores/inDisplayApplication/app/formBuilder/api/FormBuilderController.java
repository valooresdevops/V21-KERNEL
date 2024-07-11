package com.valoores.inDisplayApplication.app.formBuilder.api;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.formBuilder.dto.AdvancedResultDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.AfterSaveDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.ButtonDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.CustomAPIDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.FieldOrderNo;
import com.valoores.inDisplayApplication.app.formBuilder.dto.FieldSetDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.FormBuilderDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.FormBuilderTableDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.InsertCustomField;
import com.valoores.inDisplayApplication.app.formBuilder.dto.ProcedureDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.RelationBetweenTablesDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.V21FilesDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.V21SignatureDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm.DynamicFormDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm.DynamicFormGet;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicSearch.DynamicSearchDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto.QbeIdDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto.QueryFormDto;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgColumnConfigModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgColumnGroupModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgObjectDefMenus;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgObjectDefModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.QbeUserQueryModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder.RuleBuilderComboModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder.TechDynamicRuleBuilder;
import com.valoores.inDisplayApplication.app.formBuilder.service.IFormBuilderService;
import com.valoores.inDisplayApplication.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Form Builder Apis", description = "Form Builder exposed APIs")
@RestController
@RequestMapping("/api")
public class FormBuilderController {

	@Autowired
	private IFormBuilderService iFormBuilderService;

	@Operation(summary = "Get All tables Name")
	@PostMapping("/getAllTables")
	public List<ObjectNode> getAllTables() {
		return iFormBuilderService.getAllTables();
	}
	

	@Operation(summary = "Get All tables Name")
	@PostMapping("/getTabTables/{objectId}")
	public List<ObjectNode> getTabTables(@PathVariable long objectId) {
		return iFormBuilderService.getTabTables(objectId);
	}
	
	@Operation(summary = "Get All tables Names for combo")
	@GetMapping("/getTabTables2/{objectId}")
	public List<ObjectNode> getTabTables2(@PathVariable long objectId) {
		return iFormBuilderService.getTabTables2(objectId);
	}
	
	@Operation(summary = "Get All tables Name")
	@PostMapping("/getTabTablesFormRecords/{objectId}/{tableId}")
	public List<ObjectNode> getTabTablesFormRecords(@PathVariable("objectId") long objectId,@PathVariable("tableId") long tableId) {
		return iFormBuilderService.getTabTablesFormRecords(objectId,tableId);
	}

	@Operation(summary = "Get all Menus Name")
	@PostMapping("/getAllMenus")
	public List<CfgObjectDefModel> getAllMenus() {
		return iFormBuilderService.getAllMenus();
	}
	
	@Operation(summary = "Add new tables to be used")
	@PostMapping("/createFormBuilder")
	public CustomResponse createFormBuilder(@RequestBody List<FormBuilderDto> formBuilderDto) {
		return iFormBuilderService.createFormBuilder(formBuilderDto);
	}
	
	@Operation(summary = "Get Columns Name for a table")
	@GetMapping("/getAllColumns/{objectId}")
	public List<ObjectNode> getAllColumns(@PathVariable long objectId) {
		return iFormBuilderService.getAllColumns(objectId);
	}
	
	@Operation(summary = "Get buttons Name for a table")
	@GetMapping("/getAllButtons/{objectId}")
	public List<ObjectNode> getAllButtons(@PathVariable long objectId) {
		return iFormBuilderService.getAllButtons(objectId);
	}
	
	@Operation(summary = "Get Non Suspended Columns for a table")
	@GetMapping("/getAllColumnsNotSuspended/{objectId}")
	public List<ObjectNode> getAllColumnsSuspended(@PathVariable long objectId) {
		return iFormBuilderService.getAllColumnsSuspended(objectId);
	}
	
	@Operation(summary = "Delete existing Menus")
	@DeleteMapping("/deleteFormBuilder/{objectId}")
	public CustomResponse deleteFormBuilder(@PathVariable("objectId") long objectId) {
		return iFormBuilderService.deleteFormBuilder(objectId);
	}

	@Operation(summary = "Get MenuName for all Tabs provided menu id")
	@GetMapping(path = "/getMenuName/{objectId}")
	public List<CfgObjectDefModel> findByObjectPId(@PathVariable("objectId") long objectId) {
		return iFormBuilderService.findByObjectPId(objectId);
	}

	@Operation(summary = "Get MenuName provided menu id")
	@GetMapping(path = "/getNewMenuName/{objectId}")
	public CfgObjectDefModel findByMenuId(@PathVariable("objectId") long objectId) {
		@SuppressWarnings("unused")
		CfgObjectDefModel menuModel = new CfgObjectDefModel();
		menuModel = iFormBuilderService.findByMenuId(objectId);
		return iFormBuilderService.findByMenuId(objectId);
	}

	@Operation(summary = "Get AllTabs provided menu id")
	@GetMapping(path = "/getAllTabs/{objectId}")
	public List<CfgObjectDefModel> getAllTabs(@PathVariable("objectId") long objectId) {
		return iFormBuilderService.getAllTabs(objectId);
	}

	@Operation(summary = "Add new tab to be used")
	@PostMapping("/createTab")
	public CustomResponse createTab(@RequestBody List<FormBuilderDto> formBuilderDto) {
		return iFormBuilderService.createTab(formBuilderDto);
	}

	@Operation(summary = "Update existing tab providing an Object ID")
	@PostMapping("/updateTab/{objectId}")
	public CustomResponse updateTab(@PathVariable("objectId") long objectId,
			@RequestBody List<FormBuilderDto> formBuilderDto) {
		return iFormBuilderService.updateTab(objectId, formBuilderDto);
	}

	@Operation(summary = "fetch tab configuration")
	@GetMapping("/getTabConfiguration/{objectId}")
	public List<ObjectNode> getTabConfiguration(@PathVariable("objectId") long objectId) {
		return iFormBuilderService.getTabConfiguration(objectId);
	}

	@Operation(summary = "Delete existing Tab")
	@DeleteMapping("/deleteTab/{objectId}/{userId}")
	public CustomResponse deleteTab(@PathVariable("objectId") long objectId,@PathVariable("userId") long userId) {
		return iFormBuilderService.deleteTab(objectId,userId);
	}

	@Operation(summary = "fetch QBE")
	@PostMapping("/getSourceQuery")
	public List<QbeUserQueryModel> getSourceQuery() {
		return iFormBuilderService.getSourceQuery();
	}
	
	@PostMapping("/insertDynQuery")
	public CustomResponse insertDynQuery(@RequestBody List<DynamicFormGet> dynamicFormGet) {
		return iFormBuilderService.insertDynQuery(dynamicFormGet);
	}

	@Operation(summary = "Get Grouping tables Name")
	@PostMapping("/getGroupingTables")
	public List<ObjectNode> getGroupingTables() {

		return iFormBuilderService.getGroupingTables();
	}

	@Operation(summary = "Update existing grid an Object ID")
	@PostMapping("/updateGrid/{objectId}")
	public CustomResponse updateGrid(@PathVariable("objectId") long objectId,
			@RequestBody List<FormBuilderTableDto> formBuilderTableDto) {
		System.out.println("formBuilderTableDto >>>>>>>> "+formBuilderTableDto);
		return iFormBuilderService.updateGrid(objectId, formBuilderTableDto);
	}

	@Operation(summary = "get selected rows")
	@GetMapping("/getSelectedRows/{objectId}")
	public List<ObjectNode> getSelectedRows(@PathVariable("objectId") long objectId) {
		return iFormBuilderService.getSelectedRows(objectId);
	}

	@Operation(summary = "Update existing column")
	@PostMapping("/columnModifier/{columnId}/{userId}")
	public CustomResponse columnModifier(@PathVariable("columnId") long columnId,
			@RequestBody List<FormBuilderDto> formBuilderDto,@PathVariable("userId") long userId) {
		System.out.println("formBuilderDto -----------"+formBuilderDto);
		return iFormBuilderService.columnModifier(columnId, formBuilderDto,userId);
	}

	@Operation(summary = "fetch columns configuration")
	@GetMapping("/getColumnConfiguration/{columnId}")
	public List<ObjectNode> getColumnConfiguration(@PathVariable("columnId") long columnId) {
		return iFormBuilderService.getColumnConfiguration(columnId);
	}

	@Operation(summary = "Dynamic Grid Headers")
	@PostMapping("/getDynamicGridHeaders")
	public String getDynamicGridHeaders(@RequestBody List<DynamicFormGet> dynamicGridQuery) {
		return iFormBuilderService.getDynamicGridHeaders(dynamicGridQuery);
	}

	@Operation(summary = "Dynamic Grid Data")
	@PostMapping("/getDynamicGridData")
	public List<ObjectNode> getDynamicGridData(@RequestBody List<DynamicFormGet> dynamicFormGet) {
		return iFormBuilderService.getDynamicGridData(dynamicFormGet);
	}
	
	@Operation(summary = "Get Columns Name for a table")
	@PostMapping("/getColumnId")
	public CustomResponse getColumnId(@RequestBody String tableName) {
		return iFormBuilderService.getColumnId(tableName);
	}
	
	@Operation(summary = "fetch columns data")
	@PostMapping("/getDynamicFormData")
	public String getDynamicFormData(@RequestBody List<DynamicFormGet> dynamicFormGet) {
		System.out.println("iFormBuilderService.getDynamicFormData(dynamicFormGet)="+iFormBuilderService.getDynamicFormData(dynamicFormGet));
		return iFormBuilderService.getDynamicFormData(dynamicFormGet);
//		return iFormBuilderService.buildDynamicQuery(dynamicFormGet.get(0));
	}
	
	@Operation(summary = "Update dynamic form")
	@PostMapping("/updateDynForm")
	public CustomResponse updateDynForm(@RequestBody List<DynamicFormGet> dynamicFormGet) {
		System.out.println("hello dynamicFormGet ------------ > : " + dynamicFormGet);
		return iFormBuilderService.updateDynForm(dynamicFormGet);
	}
	
	@Operation(summary = "Dynamic Delete")
	@PostMapping("/dynamicDeleteFormBuilder")
	public CustomResponse dynamicDeleteFormBuilder(@RequestBody List<DynamicFormGet> dynamicFormGet) {
		return iFormBuilderService.dynamicDeleteFormBuilder(dynamicFormGet);
	}
	
	@Operation(summary = "get QBE id")
	@PostMapping("/getQbeId/{qbeId}/{queryType}")
	public List<ObjectNode> getQbeId(@PathVariable("qbeId") long qbeId, @PathVariable("queryType") long queryType, @RequestBody List<QbeIdDto> qbeIdDto) {
//		System.out.println(" qbe id ------ " + qbeId);
//		System.out.println(" ooooooooooooooooooooooooooooooooooooooooooooooooooooo     "+ qbeIdDto);
		return iFormBuilderService.getQbeId(qbeId, queryType, qbeIdDto);
	}
	public IFormBuilderService getiFormBuilderService() {
		return iFormBuilderService;
	}
	
	@Operation(summary = "GetCol")
	@GetMapping("/GetCol/{tableName}")
	public List<String> GetCol(@PathVariable String tableName) {
		return iFormBuilderService.GetCol(tableName);
	}
	
	@Operation(summary = "Get Sequence")
	@PostMapping("/GetSequence")
	public String GetSequence(@RequestBody DynamicFormDto inserDynamicDto) {
		return iFormBuilderService.GetSequence(inserDynamicDto);
	}
	
	@GetMapping("/getParamsName/{queryId}")
    public String getParamsName(@PathVariable("queryId") long queryId){

        return iFormBuilderService.getParamsName(queryId);
    } 
	
	@Operation(summary = "Add new fieldSet to be used")
	@PostMapping("/addFieldSet")
	public CustomResponse addFieldSet(@RequestBody List<FieldSetDto> fieldSetDto) {
		return iFormBuilderService.addFieldSet(fieldSetDto);
	}
	
	@Operation(summary = "Update existing fieldSet")
	@PostMapping("/updateFieldSet")
	public CustomResponse updateFieldSet(@RequestBody List<FieldSetDto> fieldSetDto) {
		return iFormBuilderService.updateFieldSet(fieldSetDto);
	}
	
	@Operation(summary = "Fetch a specific fieldset info")
	@GetMapping("/getFieldSetData/{objectId}/{fieldsetId}")
	public List<CfgColumnGroupModel> getFieldSetData(@PathVariable("objectId") long objectId, @PathVariable("fieldsetId") long fieldsetId) {
		return iFormBuilderService.getFieldSetData(objectId, fieldsetId);
	}
	
	@GetMapping("/getAllFieldSets/{objectId}")
	public List<CfgColumnGroupModel> getAllFieldSetsCombo(@PathVariable("objectId") long objectId) {
		return iFormBuilderService.getAllFieldSetsCombo(objectId);
	}
	
	@Operation(summary = "Add form button")
	@PostMapping("/createFormButton/{objectId}")
	public CustomResponse createFormButton(@PathVariable("objectId") long objectId, @RequestBody ButtonDto buttonDto) {
		System.out.println("buttonnnnnnnnnnn >>>>>> "+buttonDto);
		return iFormBuilderService.createFormButton(objectId, buttonDto); 
	}
	
	@Operation(summary = "Update form button")
	@PostMapping("/updateFormButton/{objectId}")
	public CustomResponse updateFormButton(@PathVariable("objectId") long objectId, @RequestBody ButtonDto buttonDto) {
		return iFormBuilderService.updateFormButton(objectId, buttonDto);
	}
	
	@Operation(summary = "Delete Existing Button")
	@DeleteMapping("/deleteButton/{buttonId}/{objectId}/{userId}")
	public CustomResponse deleteButton(@PathVariable("buttonId") long buttonId,@PathVariable("objectId") long objectId,@PathVariable("userId") long userId) {
		return iFormBuilderService.deleteButton(buttonId,objectId,userId);
	}
	
	@Operation(summary = "Get Button Info")
	@GetMapping("/getButtonData/{buttonId}")
	public CfgColumnConfigModel getButtonData(@PathVariable("buttonId") long buttonId) {
		return iFormBuilderService.getButtonData(buttonId);
	}   
	
	@Operation(summary = "Get Button Action")
	@GetMapping("/getMenusButton")
	public List<CfgObjectDefMenus> getMenusButton() {
		return iFormBuilderService.getMenusButton();
	}
	
	@Operation(summary = "Delete existing FieldSet")
	@DeleteMapping("/deleteFieldSet/{fieldSetId}")
	public CustomResponse deleteFieldSet(@PathVariable("fieldSetId") long fieldSetId) {
		return iFormBuilderService.deleteFieldSet(fieldSetId);
	}
	
	@Operation(summary = "Dynamic Search")
	@PostMapping("/dynamicSearch")
	public List<String> dynamicSearch(@RequestBody DynamicSearchDto dynamicSearchDto) {
		return iFormBuilderService.dynamicSearch(dynamicSearchDto);
	}
	
	@Operation(summary = "Get Dynamic Search")
	@PostMapping("/getDynamicGridDataInsert")
	public List<String> getDynamicGridDataInsert(@RequestBody List<DynamicFormDto> DynamicSearchDto) {
		return iFormBuilderService.getDynamicGridDataInsert(DynamicSearchDto);
	}
	
	
	@Operation(summary = "GetCol")
	@GetMapping("/GetColVal/{tableName}")
	public List<ObjectNode> GetColVal(@PathVariable("tableName") String TableName) {
		return iFormBuilderService.GetColVal(TableName);
	}
	
	@Operation(summary = "GetColValLkp")
	@PostMapping("/GetColValLkp/{tableName}")
	public List<ObjectNode> GetColValLkp(@PathVariable("tableName") String TableName) {
		return iFormBuilderService.GetColValLkp(TableName);
	}

	@Operation(summary = "checkColumnsExist")
	@GetMapping("/checkColumnsExist/{objectId}/{tableId}")
	public List<ObjectNode> checkColumnsExist(@PathVariable long objectId,@PathVariable long tableId) {
		return iFormBuilderService.checkColumnsExist(objectId,tableId);
	}
	
	@Operation(summary = "getTableInfo")
	@GetMapping("/getTableInfo/{objectId}/{tableName}/{ownerName}")
	public Integer getTableInfo(@PathVariable long objectId,@PathVariable String tableName,@PathVariable String ownerName) {
		List<ObjectNode> checkTableIfExist = iFormBuilderService.getTableInfo(objectId, tableName, ownerName, null);
		Integer tableId = checkTableIfExist.get(0).get("tableId").asInt();
		return tableId;
	}
	
	//getTableInfo
	@Operation(summary = "Get All tables Name")
	@PostMapping("/getAllTablesCombo")
	public List<ObjectNode> getAllTablesVal() {
		return iFormBuilderService.getAllTablesVal();
	}
	
	@Operation(summary = "Get All tables Name")
	@GetMapping("/getOneTables/{objectId}/{tableId}")
	public List<ObjectNode> getOneTables(@PathVariable long objectId,@PathVariable long tableId) {
		return iFormBuilderService.getOneTables(objectId,tableId);
	}
	
	@Operation(summary = "Combo Rule Builder")
	@GetMapping("/getDBRCombo/{objectId}/{columnId}")
	public List<RuleBuilderComboModel> getDBRCombo(@PathVariable long objectId, @PathVariable long columnId) {
		System.out.println("column id ------ " + objectId + " columnId ------------ " + columnId);
		return iFormBuilderService.getDBRCombo(objectId, columnId);
	}
	
//	@Operation(summary = "Dynamic Rule Builder - Get dynamic rule builder grid")
//	@PostMapping("/getDBRGrid/{objectId}/{columnId}")
//	public List<TechDynamicRuleBuilder> getDBRGrid(@PathVariable long objectId, @PathVariable long columnId) {
//		return iFormBuilderService.getDBRGrid(objectId, columnId);
//	}
	
	@Operation(summary = "Dynamic Rule Builder - Get dynamic rule builder grid")
	@PostMapping("/getDBRGrid/{objectId}/{columnId}")
	public List<ObjectNode> getDBRGrid(@PathVariable long objectId, @PathVariable long columnId) {
		return iFormBuilderService.getDBRGrid(objectId, columnId);
	}
	
	
	@Operation(summary = "Dynamic Rule Builder - Get dynamic rule builder grid #2")
	@PostMapping("/getDBRGridByRuleActionAndObjectId/{objectId}/{ruleAction}/{columnId}")
	public List<TechDynamicRuleBuilder> getDBRGridByRuleActionAndObjectId(@PathVariable long objectId, @PathVariable long ruleAction, @PathVariable long columnId) {
		return iFormBuilderService.getDBRGridByRuleActionAndObjectId(objectId, ruleAction, columnId);
	}
	
	@Operation(summary = "Dynamic Rule Builder - Get dynamic rule builder grid #2")
	@PostMapping("/getDBRGridByRuleActionAndRuleId/{objectId}/{ruleAction}/{ruleId}")
	public List<TechDynamicRuleBuilder> getDBRGridByRuleActionAndRuleId(@PathVariable long objectId, @PathVariable long ruleAction, @PathVariable long ruleId) {
		return iFormBuilderService.getDBRGridByRuleActionAndRuleId(objectId, ruleAction, ruleId);
	}
	
	@Operation(summary = "Dynamic Rule Builder - Get rule information")
	@GetMapping("/getDBRInfo/{ruleId}")
	public List<TechDynamicRuleBuilder> getDBRInfo(@PathVariable long ruleId) {
		return iFormBuilderService.getDBRInfo(ruleId);
	}
	
	@Operation(summary = "Dynamic Rule Builder - Save New Rule")
	@PostMapping("/saveDRBRule")
	public CustomResponse saveDRBRule(@RequestBody List<TechDynamicRuleBuilder> techDynamicRuleBuilder) {
		System.out.println("techDynamicRuleBuilder >>>>>>>> "+techDynamicRuleBuilder);
		return iFormBuilderService.saveDRBRule(techDynamicRuleBuilder);
	}
	
	@Operation(summary = "Calling an Api  --After Save A Screen")
	@PostMapping("/callApi")
	public String callApi(@RequestBody AfterSaveDto AfterSaveDto) {
		return iFormBuilderService.callApi(AfterSaveDto);
	}
	
	@Operation(summary = "Calling a Procedure  --After Save A Screen")
	@PostMapping("/callProcedure")
	public CustomResponse callProcedure(@RequestBody ProcedureDto ProcuderDto) {
		return iFormBuilderService.callProcedure(ProcuderDto);
	}
	
	@PostMapping("/callProcedurev21Files")
	public void callProcedurev21Files(@RequestBody V21FilesDto v21FilesDto) {
		 iFormBuilderService.callProcedurev21Files(v21FilesDto);
	}
	
	@Operation(summary = "Dynamic Rule Builder - Update Rule")
	@PostMapping("/updateDRBRule/{ruleId}")
	public CustomResponse updateDRBRule(@PathVariable long ruleId, @RequestBody List<TechDynamicRuleBuilder> techDynamicRuleBuilder) {
		return iFormBuilderService.updateDRBRule(ruleId, techDynamicRuleBuilder);
	}
	
	@Operation(summary = "Dynamic Rule Builder - Delete Rule")
	@PostMapping("/deleteDRBRule/{ruleId}/{userId}")
	public CustomResponse deleteDRBRule(@PathVariable long ruleId,@PathVariable long userId) {
		return iFormBuilderService.deleteDRBRule(ruleId,userId);
	}
	
	@Operation(summary = "getOrders")
	@GetMapping("/getOrders/{objectId}/{orderNo}")
	public String getOrders(@PathVariable long objectId,@PathVariable String orderNo,@PathVariable String type) {
		return iFormBuilderService.getOrders(objectId,orderNo,type);
	}
	
	@Operation(summary = "Relation Between 2 Tables")
	@PostMapping("/getRelationBetween2tables")
	public CustomResponse getRelationBetween2tables(@RequestBody RelationBetweenTablesDto relationTablesDto) {
		return iFormBuilderService.getRelationBetween2tables(relationTablesDto);
	}
	
	@Operation(summary = "Fetch fields with their order")
	@PostMapping("/fieldOrderManagement/{objectId}")
	public List<ObjectNode> fieldOrderManagement(@PathVariable long objectId) {
		return iFormBuilderService.fieldOrderManagement(objectId);
	}
	
	@Operation(summary = "Update Order No")
	@PostMapping("/updateFieldOrder/")
	public CustomResponse updateFieldOrder(@RequestBody List<FieldOrderNo> fieldOrderNo) {
		return iFormBuilderService.updateFieldOrder(fieldOrderNo);
	}
	
	@Operation(summary = "Get All Proc And Packages")
	@PostMapping("/getAllProcAndPack/")
	public List<ObjectNode> getAllProcAndPack() {
		return iFormBuilderService.getAllProcAndPack();
	}
	
	@Operation(summary = "Get Advanced Result")
	@PostMapping("/getAdvancedResult")
	public List<AdvancedResultDto> getAdvancedResult(@RequestBody List<AdvancedResultDto> addList) {
		return iFormBuilderService.getAdvancedResult(addList);
	}
	
	@Operation(summary = "Insert Custom Field")
	@PostMapping("/insertCustomField")
	public CustomResponse insertCustomField(@RequestBody List<InsertCustomField> insertCustomField) {
		return iFormBuilderService.insertCustomField(insertCustomField);
	}
	
	@Operation(summary = "Send For Approval")
	@PostMapping("/sendForApproval")
	public String sendForApproval(@RequestBody ProcedureDto procedureDto) {
		return iFormBuilderService.sendForApproval(procedureDto);
	}
	
	@Operation(summary = "Get All Dependencies from Form")
	@PostMapping("/getFieldDependencies/{objectId}")
	public List<ObjectNode> getFieldDependencies(@PathVariable long objectId) {
		return iFormBuilderService.getFieldDependencies(objectId);
	}
	
	@Operation(summary = "Get Columns")
	@PostMapping("/getColumnsSelected/{tableId}")
	public List<ObjectNode> getColumnsSelected(@PathVariable int tableId) {
		return iFormBuilderService.getColumnsSelected(tableId);
	}
	
	@Operation(summary = "Get ColName and ColId")
	@PostMapping("/getColNameAndColId")
	public List<ObjectNode> getColNameAndColId(@RequestBody List<Integer> columnId) {
		return iFormBuilderService.getColNameAndColId(columnId);
	}
	
	@Operation(summary = "Call Check Existing Initiation")
	@PostMapping("/checkExisting")
	public CustomResponse CallCheckExisting(@RequestBody CustomAPIDto parameters) {		
		System.out.println("parameters >>>>>>>>> "+parameters);
		return iFormBuilderService.CallCheckExisting(parameters);
	}
	
	@Operation(summary = "Get tables linked to form")
	@GetMapping("/getTablesLinkedToForm/{objectId}")
	public List<ObjectNode> getTablesLinkedToForm(@PathVariable int objectId) {		
		return iFormBuilderService.getTablesLinkedToForm(objectId);
	}
	
	@Operation(summary = "Get Column Name By Column Id")
	@GetMapping("/getColNameByColumnId/{columnId}")
	public String getColNameByColumnId(@PathVariable int columnId) {		
		return iFormBuilderService.getColNameByColumnId(columnId);
	}
	
	@Operation(summary = "Validate")
	@PostMapping("/Validate")
	public CustomResponse Validate(@RequestBody CustomAPIDto parameters) {	
		return iFormBuilderService.Validate(parameters);
	}
	
//	@Operation(summary = "ValidateCheckExisting")
//	@PostMapping("/ValidateCheckExisting")
//	public CustomResponse ValidateCheckExisting(@RequestBody CustomAPIDto parameters) {		
//		return iFormBuilderService.ValidateCheckExisting(parameters);
//	}
	
	@Operation(summary = "Call Check Existing General Info")
	@PostMapping("/checkExistingGeneralInfo")
	public CustomResponse CallCheckExistingGeneralInfo(@RequestBody CustomAPIDto parameters) {		
		return iFormBuilderService.CallCheckExistingGeneralInfo(parameters);
	}
	
	@Operation(summary = "Get All Phone Prefix")
	@GetMapping("/fetchDialNums")
	public List<ObjectNode> fetchDialNums() {		
		return iFormBuilderService.fetchDialNums();
	}
	
	@PostMapping("/callProcedurev21Signature")
	public void callProcedurev21Signature(@RequestBody V21SignatureDto v21SignatureDto) {
		 iFormBuilderService.callProcedurev21Signature(v21SignatureDto);
	}
	
	
	 @PostMapping("/insertQueryForm")
		public CompletableFuture<Integer> insertQueryForm(@RequestBody QueryFormDto queryFormDto) {
	    	
			return iFormBuilderService.insertQueryForm(queryFormDto);
	    }
	 
	 @GetMapping("/getQueryForm/{ruleCode}")
		public List<List<ObjectNode>> getQueryForm(@PathVariable long ruleCode) {
	    	
			return iFormBuilderService.getQueryForm(ruleCode);
	    }
	 
	 @GetMapping("/getQbeName/{qbeId}")
		public String getQbeName(@PathVariable long qbeId) {
	    	
			return iFormBuilderService.getQbeName(qbeId);
	    }

	 
	 @GetMapping("/checkIfConditionExists/{ruleCode}")
		public int checkIfConditionExists(@PathVariable long ruleCode) {
	    	
			return iFormBuilderService.checkIfConditionExists(ruleCode);
	    }
	 
	 @PostMapping("/updateQueryForm")
		public int updateQueryForm(@RequestBody QueryFormDto queryFormDto) {
	    	
			return iFormBuilderService.updateQueryForm(queryFormDto);
	    }


	 @GetMapping("/getColumnsQuery/{queryId}")
		public List<ObjectNode> getColumnsQuery(@PathVariable long queryId) {
	    	
			return iFormBuilderService.getColumnsQuery(queryId);
	    }
	 
	 @GetMapping("/getQueryFormButton/{objectId}")
		public String getQueryFormButton(@PathVariable long objectId) {
	    	
			return iFormBuilderService.getQueryFormButton(objectId);
	    }

	 @GetMapping("/getQueryFormButtonJSON/{objectId}")
		public List<ObjectNode> getQueryFormButtonJSON(@PathVariable long objectId) {
	    	
			return iFormBuilderService.getQueryFormButtonJSON(objectId);
	    }
	 
		@GetMapping("/getQueryParams/{queryId}")
		public List<ObjectNode> getQueryParams(@PathVariable("queryId") long queryId){
			return iFormBuilderService.getQueryParams(queryId);
		}
	 
//		@PostMapping("/getDynReports")
//		public List<ObjectNode> getDynReports(){
//			return iFormBuilderService.getDynReports();
//		}
		 @PostMapping("/insertDynamicReportForm/{userId}/{reportName}")
			public int insertDynamicReportForm(@PathVariable("userId") long userId,@PathVariable("reportName") String reportName,@RequestBody List<DynamicFormGet> dynamicFormGet) {
		    	
				return iFormBuilderService.insertDynamicReportForm(userId,reportName,dynamicFormGet);
		    }
		 
		 @GetMapping("/insertDynamicReportGrid/{userId}/{reportName}/{selectedRows}")
			public int insertDynamicReportGrid(@PathVariable("userId") long userId,@PathVariable("reportName") String reportName,@PathVariable("selectedRows") String selectedRows) {
		    	
				return iFormBuilderService.insertDynamicReportGrid(userId,reportName,selectedRows);
		    }
		 
		 @PostMapping("/updateDynamicReportForm/{userId}/{reportId}")
			public int updateDynamicReportForm(@PathVariable("userId") long userId,@PathVariable("reportId") long reportId,@RequestBody List<DynamicFormGet> dynamicFormGet) {
		    	
				return iFormBuilderService.updateDynamicReportForm(userId,reportId,dynamicFormGet);
		    }
		 
		 @PostMapping("/updateDynamicReportGrid/{userId}/{reportId}/{selectedRows}")
			public int updateDynamicReportGrid(@PathVariable("userId") long userId,@PathVariable("reportId") long reportId,@PathVariable("selectedRows") String selectedRows) {
		    	
				return iFormBuilderService.updateDynamicReportGrid(userId,reportId,selectedRows);
		    }
		 
		 @PostMapping("/getDynamicReportResult/{reportId}")
			public List<List<ObjectNode>> getDynamicReportResult(@PathVariable("reportId") long reportId,@RequestBody List<DynamicFormGet> dynamicFormGet) {
		    	
				return iFormBuilderService.getDynamicReportResult(reportId,dynamicFormGet);
		    }
		 
		 @GetMapping("/getDynamicReportData/{reportId}")
			public List<ObjectNode> getDynamicReportData(@PathVariable("reportId") long reportId) {
		    	
				return iFormBuilderService.getDynamicReportData(reportId);
		    }
		 
		 
			@Operation(summary = "Get All Cust Document")
			@PostMapping("/GetAllCustDocumnet/{Customer_id}")
			public String GetAllCustDocumnet(@PathVariable long Customer_id) {
				return iFormBuilderService.GetAllCustDocumnet(Customer_id);
			}
			
			@Operation(summary = "After Save Schedule")
			@PostMapping("/GetScheduleApi")
			public CustomResponse GetScheduleApi(@RequestBody String array) throws RemoteException {
				
				System.out.println("activity_id -------"+array);
				//6340
				List<ObjectNode> list = new ArrayList<>();

				try {
			        ObjectMapper objectMapper = new ObjectMapper();
		            JsonNode rootNode = objectMapper.readTree("["+array+"]");
		            for (JsonNode node : rootNode) {
		                ObjectNode objectNode = objectMapper.convertValue(node, ObjectNode.class);
		                list.add(objectNode);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				String columnsIdsString=list.get(0).get("Parameters").toString().replace("[\"","'").replace("\"]","'");
				columnsIdsString=columnsIdsString.replace("\",\"","','");
				List<ObjectNode> paramColNames=iFormBuilderService.getApiColumnName(columnsIdsString);
				List<ObjectNode> ruleDataList = new ArrayList<>();
				String ruleDataString=list.get(0).get("ruleCallApiData").toString().substring(1, list.get(0).get("ruleCallApiData").toString().length() - 1).replace("\\\"", "\"");
				System.out.println("RULE DATA STRING>>>>>>"+ruleDataString);

				try {
			        ObjectMapper objectMapperData = new ObjectMapper();
		            JsonNode rootNodeData = objectMapperData.readTree(ruleDataString);
		            for (JsonNode node : rootNodeData) {
		                ObjectNode objectNode = objectMapperData.convertValue(node, ObjectNode.class);
		                ruleDataList.add(objectNode);
		            }
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				
				System.out.println("RULE DATA LIST>>>>>>>>>"+ruleDataList);
				
				String colValue="";
				for(int i=0;i<ruleDataList.size();i++) {
					for(int j=0;j<paramColNames.size();j++) {
						if(ruleDataList.get(i).get("colname").toString().toUpperCase().equals(paramColNames.get(j).get("columnName").toString().toUpperCase())) {
							colValue=ruleDataList.get(i).get("colvalue").toString().replace("\"","");
						}
					}
				}
				System.out.println("ACTIVITY NAME>>>>>>>>>"+colValue);
				long activity_id = Long.parseLong(colValue);
				System.out.println("activity_id 2 --------- " + activity_id);
				return iFormBuilderService.GetScheduleApi(activity_id);
//				return null;
			}
			
			
			@Operation(summary = "run button")
			@PostMapping("/GetRunApi")
			public CustomResponse GetRunApi(@RequestBody String ruleId) throws RemoteException{	
				return iFormBuilderService.GetRunApi(ruleId);
			}
			
			
			
			@PostMapping("/exportRules/{ruleIds}")
			public List<ObjectNode> exportRules(@PathVariable("ruleIds") String ruleIds) {
				return iFormBuilderService.exportRules(ruleIds);
			}
		 
		    @PostMapping("/importDynamicRules/{objectId}")
			public int importDynamicRules(@PathVariable("objectId") long objectId,@RequestBody List<ObjectNode> dynamicRulesData) {
				return iFormBuilderService.importDynamicRules(objectId,dynamicRulesData);
			}

			@Operation(summary = "jason builder")
			@PostMapping("/JasonBuilder")
			public CustomResponse JasonBuilder(@RequestBody String Jbuilder) throws RemoteException{	
				return iFormBuilderService.GetRunApi(Jbuilder);
			}
		 


		}