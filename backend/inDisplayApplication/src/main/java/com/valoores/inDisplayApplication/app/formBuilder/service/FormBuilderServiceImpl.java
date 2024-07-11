package com.valoores.inDisplayApplication.app.formBuilder.service;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.service.spi.ServiceException;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.common.ObjectToJsonRepository;
import com.valoores.inDisplayApplication.app.dynamicReport.model.CfgReportColumnFilterModel;
import com.valoores.inDisplayApplication.app.dynamicReport.model.CfgReportColumnOutputModel;
import com.valoores.inDisplayApplication.app.dynamicReport.model.CfgReportDynamicConfigModel;
import com.valoores.inDisplayApplication.app.dynamicReport.repository.CfgReportColumnFilterRepo;
import com.valoores.inDisplayApplication.app.dynamicReport.repository.CfgReportColumnOutputRepo;
import com.valoores.inDisplayApplication.app.dynamicReport.repository.CfgReportDynamicConfigRepo;
import com.valoores.inDisplayApplication.app.dynamicReport.repository.CfgTableObjectRelRepo;
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
import com.valoores.inDisplayApplication.app.formBuilder.dto.queryDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm.DynamicFormColsDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm.DynamicFormDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm.DynamicFormGet;
import com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicSearch.DynamicSearchDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto.HiddenQbeDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto.LinkQbeDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto.QbeIdDto;
import com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto.QueryFormDto;
import com.valoores.inDisplayApplication.app.formBuilder.model.AllColumnsModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgColumnConfigModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgColumnGroupModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgFieldsetObjectModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgObjectDefMenus;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgObjectDefModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgTableConfigModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgTableObjectifRelModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.QbeUserQueryModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.QueryFormButtonModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.TechRuleCondition;
import com.valoores.inDisplayApplication.app.formBuilder.model.V21FileModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.V21SignatureModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionActivityCondDModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionVariableModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder.RuleBuilderComboModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder.TechDynamicRuleBuilder;
import com.valoores.inDisplayApplication.app.formBuilder.repository.AllColumnsRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.AllTableRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgColumnConfigRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgColumnGroupRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgFieldsetRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgObjectDefMenusRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgObjectDefRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgTableConfigRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.CfgTableObjectifRelRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.QbeUserQueryRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.QueryFormButtonRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.SqbQueryDetailsRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.TechRuleConditionRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.V21SignatureRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.V21fileRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.WfmConditionActivityCondDRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.WfmConditionRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.WfmConditionVariableRepo;
import com.valoores.inDisplayApplication.app.formBuilder.repository.DynamicRuleBuilder.TechDynamicRuleBuilderRepo;
import com.valoores.inDisplayApplication.app.logs.model.InDisplayLogsModel;
import com.valoores.inDisplayApplication.app.logs.repository.InDisplayLogsRepo;
import com.valoores.inDisplayApplication.app.logs.repository.UsmUserMiscInfoRepo;
import com.valoores.inDisplayApplication.backend.CustomResponse;
import com.valoores.inDisplayApplication.app.formBuilder.client.TestWebServiceImpl;
import com.valoores.inDisplayApplication.app.formBuilder.client.TestWebServiceImplService;
import com.valoores.inDisplayApplication.app.formBuilder.client.TestWebServiceImplServiceLocator;
import org.json.JSONObject;

@Service
public class FormBuilderServiceImpl implements IFormBuilderService {

	
	private String newProcParams;
	
	@SuppressWarnings("unused")
	@Autowired
	private AllTableRepository iTableRepository;

	@Autowired
	private EntityManager entityManagerR;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private CfgObjectDefRepository objectDefRepository;

	@Autowired
	private CfgTableConfigRepository iAddedTableRepository;

	@Autowired
	private CfgTableObjectifRelRepository cfgTableObjectifRelRepository;
	@Autowired
	private AllColumnsRepository allColumnsRepository;
	@Autowired
	private CfgColumnConfigRepository cfgColumnConfigRepository;

	@Autowired
	private QbeUserQueryRepository qbeUserQueryRepository;

	@Autowired
	private SqbQueryDetailsRepository sqbQueryDetailsRepository;

	@Autowired
	private CfgFieldsetRepository cfgFieldsetRepository;

	@Autowired
	private CfgColumnGroupRepository cfgColumnGroupRepository;

	@Autowired
	private CfgObjectDefMenusRepository objectDefMenusRepository;

	@Autowired
	private TechDynamicRuleBuilderRepo techDynamicRuleBuilderRepo;

	@Autowired
	private V21fileRepo v21fileRepo;
	
	@Autowired
	private V21SignatureRepo v21SignatureRepo;

	@Autowired
	private WfmConditionRepo wfmConditionRepo;
	
	@Autowired
	private WfmConditionVariableRepo wfmConditionVariableRepo;
	
	@Autowired
	private TechRuleConditionRepo techRuleConditionRepo;
	
	@Autowired
	private QueryFormButtonRepo queryFormButtonRepo;
	
	@Autowired
	private WfmConditionActivityCondDRepo wfmConditionActivityCondDRepo;
	
	@Autowired
	private CfgReportDynamicConfigRepo cfgReportDynamicConfigRepo;
	
	@Autowired
	private CfgReportColumnFilterRepo 	cfgReportColumnFilterRepo;
	
	@Autowired
	private CfgReportColumnOutputRepo 	cfgReportColumnOutputRepo;
	
	@SuppressWarnings("unused")
	@Autowired
	private CfgTableObjectRelRepo 	cfgTableObjectRelRepo;
	
	@Autowired
	private UsmUserMiscInfoRepo	usmUserMiscInfoRepo;

	@Autowired
	private InDisplayLogsRepo	inDisplayLogsRepo;

	private final JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private String query;

	public FormBuilderServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<CfgObjectDefModel> getAllMenus() {
		return objectDefRepository.getAllMenus();
	}

	@Override
	public List<ObjectNode> getAllTables() {

		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT concat(owner, '.', tableName) as tableName FROM AllTableModel "
						+ "");
		// where (owner) like '%DBA' 
	}

	@Override
	public List<ObjectNode> getTabTables(long objectId) {

		return ObjectToJsonRepository.getJson(entityManagerR,
				" SELECT concat(T.tableOwner, '.', T.tableName) as tableName, " + " T.tableId as id, "
						+ " R.orderNo as orderNo " + " FROM CfgTableConfigModel T, CfgTableObjectifRelModel R"
						+ " WHERE R.objectId = " + objectId + " " + " AND T.tableId = R.tableId "
						+ " AND T.tableName != null " + " order by R.orderNo ASC");

	}

	@Override
	public List<ObjectNode> getTabTablesFormRecords(long objectId, long tableId) {

		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT C.columnId as columnId, C.columnName as columnName, T.tableName as tableName,R.orderNo as orderNo,T.tableOwner as tableOwner FROM CfgTableConfigModel T,CfgTableObjectifRelModel R,CfgColumnConfigModel C"
						+ " WHERE R.objectId = " + objectId + "and T.tableId = " + tableId
						+ " and T.tableId = R.tableId and T.tableId = C.tableId  ");
	}

	@Transactional
	@Override
	public CustomResponse createFormBuilder(List<FormBuilderDto> formBuilderDto) {

		CfgObjectDefModel menuModel = new CfgObjectDefModel();
		ArrayList<Long> tableIDsList = new ArrayList<Long>();

		ArrayList<CfgTableConfigModel> tableList = new ArrayList<CfgTableConfigModel>();
		long objectID;

		try {
			CfgColumnGroupModel columnModel1 = new CfgColumnGroupModel();
			CfgFieldsetObjectModel relationModel = new CfgFieldsetObjectModel();

			menuModel.setMenuName(formBuilderDto.get(0).getMenuName());
			menuModel.setObjectType(16);
			menuModel.setIsMain(1);
			menuModel.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
			menuModel.setIsReadOnly("0");
			menuModel.setIsGrid(1);
			menuModel.setIsQueryForm("0");
			menuModel.setIsDynamicReport("0");
			menuModel.setIsAdvancedSearch(0);  // Set as integer
			menuModel.setAdvancedSearchProcedureName(formBuilderDto.get(0).getAdvancedSearchProcedureName());

			menuModel.setOrderNo(1);
//			menuModel.setIsSave("0");

			Long canAddId = formBuilderDto.get(0).getCanAdd();
			menuModel.setCanAdd(canAddId == 0 ? null : canAddId);

			Long canModifyId = formBuilderDto.get(0).getCanModify();
			menuModel.setCanModify(canModifyId == 0 ? null : canModifyId);

			Long canDeleteId = formBuilderDto.get(0).getCanDelete();
			menuModel.setCanDelete(canDeleteId == 0 ? null : canDeleteId);

			Long sourceQueryId = formBuilderDto.get(0).getSourceQuery();
			menuModel.setSourceQuery(sourceQueryId == 0 ? null : sourceQueryId);

			Long isSave = formBuilderDto.get(0).getIsSave();
			menuModel.setIsSave(isSave == 0 ? null : isSave);
//system.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			objectDefRepository.save(menuModel);
			objectID = menuModel.getObjectId();

			// create a field set when create a form builder
			Date now = new Date();
			columnModel1.setName("fieldSet");
			columnModel1.setOrderNb(0);
			columnModel1.setIsHidden("0");
			columnModel1.setAdvancedSearch("0");
			columnModel1.setIsreadOnly("0");
			columnModel1.setCreationDate(now);
			columnModel1.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
			columnModel1.setGroupCode(columnModel1.getId());
			cfgColumnGroupRepository.save(columnModel1);
			long fieldsetid = columnModel1.getId();

			relationModel.setFieldSetId(fieldsetid);
			relationModel.setObjectId(objectID);
			relationModel.setCreationDate(now);
			relationModel.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
			cfgFieldsetRepository.save(relationModel);
			///////////////

			formBuilderDto.forEach(x -> {
				CfgTableConfigModel tableConfig = new CfgTableConfigModel();
				tableConfig.setCreatedBy(x.getCreatedBy());
				tableConfig.setTableName(x.getTableName());
				tableConfig.setTableOwner(x.getOwnerName());
				iAddedTableRepository.save(tableConfig);

				tableIDsList.add(tableConfig.getTableId());
				tableList.add(tableConfig);
			});

			tableIDsList.forEach(x -> {
				CfgTableObjectifRelModel tableRelationConfig = new CfgTableObjectifRelModel();
				tableRelationConfig.setObjectId(objectID);
				tableRelationConfig.setTableId(x);
				tableRelationConfig.setOrderNo(0);
				tableRelationConfig.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
				cfgTableObjectifRelRepository.save(tableRelationConfig);
			});

			tableList.forEach(x -> {
				int orderNo = 0;
				@SuppressWarnings("unused")
				AllColumnsModel allColumns = new AllColumnsModel();
				@SuppressWarnings("unused")
				long tabId = x.getTableId();
				String tableName = x.getTableName();
				String tableOwner = x.getTableOwner();

				List<AllColumnsModel> listOfColumns = allColumnsRepository.findAllByTableNameAndOwner(tableName,
						tableOwner);
				//system.out.println("LIST OF COLUMNSSS>>>>>>>"+listOfColumns);
				for (AllColumnsModel column : listOfColumns) {
					long columnId = -1;
					CfgColumnConfigModel columnModel = new CfgColumnConfigModel(); // Create a new instance for each row
					orderNo = orderNo + 1;
					//system.out.println("COLUMN>>>>>>>"+column);

					columnModel.setColumnDescription(capitalizeString(column.getColumnName()));
					columnModel.setColumnName(column.getColumnName());
					columnModel.setCreatedBy(x.getCreatedBy());
					columnModel.setTableId(x.getTableId());
					columnModel.setIsLink("0");
					columnModel.setMenus(null);
					columnModel.setOrderNo(orderNo);
					columnModel.setColumnLength(100);
					columnModel.setIsMultiple("0");
					columnModel.setIsSuspended("0");
					columnModel.setSizeField("1");
					columnModel.setGroupId(fieldsetid);
					columnModel.setIsExcluded("0");
					columnModel.setLanguageId(13);

					switch (column.getNullable()) {
					case "N":
						columnModel.setIsMandatory("1");
						break;
					case "":
						columnModel.setIsMandatory("1");
						break;
					case "Y":
						columnModel.setIsMandatory("0");
						break;
					}

					Long qbeReadOnlyId = formBuilderDto.get(0).getQbeReadOnly();
					columnModel.setQbeReadOnly(qbeReadOnlyId == 0 ? null : qbeReadOnlyId);

					Long defaultValueId = formBuilderDto.get(0).getDefaultValue();
					columnModel.setDefaultValue(defaultValueId == 0 ? null : defaultValueId);
					
					Long mandatoryQueryId = formBuilderDto.get(0).getMandatoryQuery();
					columnModel.setMandatoryQuery(mandatoryQueryId == 0 ? null : mandatoryQueryId);

					Long queryId = formBuilderDto.get(0).getQuery();
					columnModel.setQuery(queryId == 0 ? null : queryId);

					Long dependencyDefaultValue = formBuilderDto.get(0).getDependencyDefaultValue();
					columnModel.setDependencyDefaultValue(dependencyDefaultValue == 0 ? null : dependencyDefaultValue);
					
					//system.out.println("COLUMN DATA TYPE>>>>>>>"+column.getDataType());

					switch (column.getDataType()) {
					case "numeric":
						columnId = 2;
						break;
					case "character varying":
						columnId = 1;
						break;
					case "date":
						columnId = 6;
						break;
					case "timestamp without time zone":
						columnId = 6;
						break;
					case "bytea":
						columnId = 11;
						break;
					}

					columnModel.setIsEditable("0");
					columnModel.setColumnType(columnId);
					cfgColumnConfigRepository.save(columnModel);
				}
			});

			return CustomResponse.builder().code("0").status("success").id(menuModel.getObjectId())
					.description("SAVED SUCCESSFULLY").build();
		}

		catch (Exception e) {
			e.printStackTrace();
			return CustomResponse.builder().code("0").status("Fail").id(menuModel.getObjectId())
					.description("Fail SAVED").build();

		}

	}

	@Override
	public CfgObjectDefModel findByMenuId(long objectId) {
		return objectDefRepository.findByMenuId(objectId);
	}

	@Override
	public List<CfgObjectDefModel> getAllTabs(long objectId) {
		return objectDefRepository.getAllTabs(objectId);
	}

	@Override
	public List<ObjectNode> getAllColumns(long objectId) {
		
		//system.out.println("my data is >>>>> : " + objectId);
		try {

			String query = " SELECT concat(upper(T.tableOwner), '.', upper(T.tableName)) as tableName, "
					+ " C.columnId as id, C.isMultiple as isMultiple, C.mandatoryQuery as mandatoryQuery, "
					+ " T.tableId as tableId, C.isExcluded as isExcluded, C.dependencyDefaultValue as dependencyDefaultValue, "
					+ " upper(C.columnName) as name, C.groupId as groupId,R.orderNo as orderNo, "
					+ " C.columnType as columnTypeCode, C.columnDescription as columnDescription, "
					+ " (SELECT lower(name) FROM Syslines WHERE heaCode = 426 AND id = C.columnType) as columnType, "
					+ " C.sizeField as sizeField, C.isMandatory as isMandatory , C.isLink as isLink,C.isGridHidden as isGridHidden, C.qbeReadOnly as qbeReadOnly, C.query as query, "
					+ " C.columnLength as columnLength, C.defaultValue as defaultValue, C.menus as menus,C.isExcluded as isExcluded, "
					+ " C.isSuspended as isSuspended, C.blobFile as blobFile,C.languageId as isArabic,C.isEditable as isEditable"
					+ " FROM CfgTableConfigModel T, CfgColumnConfigModel C, CfgObjectDefModel O, CfgTableObjectifRelModel R, CfgFieldsetObjectModel Y "
					+ " WHERE T.tableId = C.tableId " + " AND O.objectId = R.objectId "
					+ " AND Y.objectId = O.objectId " + " AND Y.fieldSetId = C.groupId " + " AND T.tableId = R.tableId "
					+ " AND O.objectId = " + objectId + " order by C.orderNo ASC ";

			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR, query);
//system.out.println("my result --------------------------- > " + result);
			return result;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<ObjectNode> getAllColumnsSuspended(long objectId) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					" SELECT concat(upper(T.tableOwner), '.', upper(T.tableName) as tableName, " + " C.columnId as id, "
							+ " T.tableId as tableId, "
							+ " upper(C.columnName) as name, C.groupId as groupId,R.orderNo as orderNo, "
							+ " C.columnType as columnTypeCode, C.columnDescription as columnDescription, "
							+ " (SELECT lower(name) FROM Syslines WHERE heaCode = 426 AND id = C.columnType) as columnType, "
							+ " C.sizeField as sizeField, C.isMandatory as isMandatory , C.isLink as isLink, C.qbeReadOnly as qbeReadOnly, C.query as query, "
							+ " C.columnLength as columnLength, C.defaultValue as defaultValue, C.menus as menus, "
							+ " C.isSuspended as isSuspended, C.blobFile as blobFile "
							+ " FROM CfgTableConfigModel T, CfgColumnConfigModel C, CfgObjectDefModel O, CfgTableObjectifRelModel R, CfgFieldsetObjectModel Y "
							+ " WHERE T.tableId = C.tableId " + " AND O.objectId = R.objectId "
							+ " AND Y.objectId = O.objectId " + " AND Y.fieldSetId = C.groupId "
							+ " AND T.tableId = R.tableId " + " AND C.isSuspended = 0 " + " AND O.objectId = "
							+ objectId + " order by C.orderNo ASC ");
			return result;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public CustomResponse deleteFormBuilder(long objectId) {
		List<Long> objectIds = objectDefRepository.getObjectIds(objectId);

		objectIds.forEach(z -> {
			if (objectIds.size() != 0) {
				cfgColumnConfigRepository.deleteByColumnId(z);
				cfgTableObjectifRelRepository.deleteFormBuilder(z);
				iAddedTableRepository.deleteByTableId(z);

				List<Long> fieldSetIds = cfgFieldsetRepository.getFieldSetIds(z);
				if (fieldSetIds.size() != 0) {
					fieldSetIds.forEach(y -> {
						cfgFieldsetRepository.deleteByObjectId(z);
						cfgColumnGroupRepository.deleteByFieldSetId(y);
					});
				}
				objectDefRepository.deleteByObjectId(z);
			}
		});
		return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
	}

	@Transactional
	@Override
	public CustomResponse createTab(List<FormBuilderDto> formBuilderDto) {

		CfgObjectDefModel menuModel = new CfgObjectDefModel();
		ArrayList<Long> tableIDsList = new ArrayList<Long>();

		ArrayList<CfgTableConfigModel> tableList = new ArrayList<CfgTableConfigModel>();
		long objectID;
		@SuppressWarnings("unused")
		long fieldSetId;

		try {
			@SuppressWarnings("unused")
			CfgColumnConfigModel columnModel1 = new CfgColumnConfigModel();

			menuModel.setMenuName(formBuilderDto.get(0).getMenuName());
			menuModel.setObjectType(16);
			menuModel.setIsMain(0);
			menuModel.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
			menuModel.setParentId(formBuilderDto.get(0).getObjectPId());
			menuModel.setIsReadOnly("0");
			menuModel.setIsGrid(0);
			menuModel.setIsQueryForm("0");
			menuModel.setIsDynamicReport("0");
			menuModel.setIsAdvancedSearch(0);
			menuModel.setOrderNo(formBuilderDto.get(0).getOrderNo());
			menuModel.setAdvancedSearchProcedureName(formBuilderDto.get(0).getAdvancedSearchProcedureName());

//			menuModel.setIsSave("0");

			Long canAddId = formBuilderDto.get(0).getCanAdd();
			menuModel.setCanAdd(canAddId == 0 ? null : canAddId);

			Long canModifyId = formBuilderDto.get(0).getCanModify();
			menuModel.setCanModify(canModifyId == 0 ? null : canModifyId);

			Long canDeleteId = formBuilderDto.get(0).getCanDelete();
			menuModel.setCanDelete(canDeleteId == 0 ? null : canDeleteId);

			Long sourceQueryId = formBuilderDto.get(0).getSourceQuery();
			menuModel.setSourceQuery(sourceQueryId == 0 ? null : sourceQueryId);

			Long condition = formBuilderDto.get(0).getCondition();
			menuModel.setCondition(condition == 0 ? null : condition);

			Long isSave = formBuilderDto.get(0).getIsSave();
			menuModel.setIsSave(isSave == 0 ? null : isSave);

			if (formBuilderDto.get(0).getIsGrid().equals("true")) {
				menuModel.setIsGrid(1);
			} else {
				menuModel.setIsGrid(0);
			}
			if (formBuilderDto.get(0).getIsQueryForm().equals("true")) {
				menuModel.setIsQueryForm("1");
			} else {
				menuModel.setIsQueryForm("0");
			}
			if (formBuilderDto.get(0).getIsDynamicReport().equals("true")) {
				menuModel.setIsDynamicReport("1");
			} else {
				menuModel.setIsDynamicReport("0");
			}
//        	if (formBuilderDto.get(0).getIsHidden().equals("true")) {
//        		menuModel.setIsHidden("1");
//        	} else {
//        		menuModel.setIsHidden("0");}

			if (formBuilderDto.get(0).getIsReadOnly().equals("true")) {
				menuModel.setIsReadOnly("1");
			} else {
				menuModel.setIsReadOnly("0");
			}

			if (formBuilderDto.get(0).getIsAdvancedSearch().equals("true")) {
				menuModel.setIsAdvancedSearch(1);
			} else {
				menuModel.setIsAdvancedSearch(0);
			}

//			if (formBuilderDto.get(0).getIsSave().equals("true")) {
//				menuModel.setIsSave("1");
//			} else {
//				menuModel.setIsSave("0");
//			}

			objectDefRepository.save(menuModel);
			objectID = menuModel.getObjectId();

			
			if (formBuilderDto.get(0).getIsQueryForm().equals("true")) {
				QueryFormButtonModel queryFormButtonModel=new QueryFormButtonModel();
				queryFormButtonModel.setObjectId(objectID);
				queryFormButtonModel.setButtonList(formBuilderDto.get(0).getIsQueryFormSelectedButtons());
				queryFormButtonRepo.save(queryFormButtonModel);
			} 
//			else {
//				
//			}
			
			
			// Create default field set in create tab
			CfgColumnGroupModel columnModel2 = new CfgColumnGroupModel();
			columnModel2.setName("fieldSet");
			columnModel2.setOrderNb(0);
			columnModel2.setIsHidden("0");
			columnModel2.setAdvancedSearch("0");
			columnModel2.setIsreadOnly("0");
			columnModel2.setCreationDate(new Date());
			columnModel2.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
			columnModel2.setGroupCode(columnModel2.getId());
			cfgColumnGroupRepository.save(columnModel2);
			long fieldsetid = columnModel2.getId();

			CfgFieldsetObjectModel relationModel = new CfgFieldsetObjectModel();
			relationModel.setFieldSetId(fieldsetid);
			relationModel.setObjectId(objectID);
			relationModel.setCreationDate(new Date());
			relationModel.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
			cfgFieldsetRepository.save(relationModel);
			///////////////

			formBuilderDto.forEach(x -> {
				CfgTableConfigModel tableConfig = new CfgTableConfigModel();
				tableConfig.setCreatedBy(x.getCreatedBy());
				tableConfig.setTableName(x.getTableName());
				tableConfig.setTableOwner(x.getOwnerName());
				iAddedTableRepository.save(tableConfig);

				tableIDsList.add(tableConfig.getTableId());
				tableList.add(tableConfig);
			});

			tableIDsList.forEach(x -> {
				CfgTableObjectifRelModel tableRelationConfig = new CfgTableObjectifRelModel();
				tableRelationConfig.setObjectId(objectID);
				tableRelationConfig.setTableId(x);
				tableRelationConfig.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
				cfgTableObjectifRelRepository.save(tableRelationConfig);
			});

			tableList.forEach(x -> {
				int orderNo = 0;
				@SuppressWarnings("unused")
				AllColumnsModel allColumns = new AllColumnsModel();
				@SuppressWarnings("unused")
				long tabId = x.getTableId();
				String tableName = x.getTableName();
				String tableOwner = x.getTableOwner();

				List<AllColumnsModel> listOfColumns = allColumnsRepository.findAllByTableNameAndOwner(tableName,
						tableOwner);
						//system.out.println("list of columnss>>>>>>>>"+listOfColumns);
				for (AllColumnsModel column : listOfColumns) {
					long columnId = -1;
					CfgColumnConfigModel columnModel = new CfgColumnConfigModel(); // Create a new instance for each row
					orderNo = orderNo + 1;
					//system.out.println("COLUMNNNNNNNNNNNNNNNN>>>>>>>>"+column);

					columnModel.setColumnDescription(capitalizeString(column.getColumnName()));
					columnModel.setColumnName(column.getColumnName());
					columnModel.setCreatedBy(x.getCreatedBy());
					columnModel.setTableId(x.getTableId());
					columnModel.setOrderNo(orderNo);
					columnModel.setColumnLength(100);
					columnModel.setIsMultiple("0");
					columnModel.setIsSuspended("0");
					columnModel.setIsLink("0");
					columnModel.setMenus(null);
					columnModel.setSizeField("1");
					columnModel.setGroupId(fieldsetid);
					// columnModel.setIsMandatory("0");

					switch (column.getNullable()) {
					case "N":
						columnModel.setIsMandatory("1");
						break;
					case "":
						columnModel.setIsMandatory("1");
						break;
					case "Y":
						columnModel.setIsMandatory("0");
						break;
					}

					Long qbeReadOnlyId = formBuilderDto.get(0).getQbeReadOnly();
					columnModel.setQbeReadOnly(qbeReadOnlyId == 0 ? null : qbeReadOnlyId);

					Long defaultValueId = formBuilderDto.get(0).getDefaultValue();
					columnModel.setDefaultValue(defaultValueId == 0 ? null : defaultValueId);
					
					Long mandatoryQueryId = formBuilderDto.get(0).getMandatoryQuery();
					columnModel.setMandatoryQuery(mandatoryQueryId == 0 ? null : mandatoryQueryId);

					Long queryId = formBuilderDto.get(0).getQuery();
					columnModel.setQuery(queryId == 0 ? null : queryId);
					//system.out.println("COLUMN DATA  TYPEEE>>>>>>"+column.getDataType());
					switch (column.getDataType()) {
						case "numeric":
							columnId = 2;
							break;
						case "character varying":
							columnId = 1;
							break;
						case "date":
							columnId = 6;
							break;
						case "timestamp without time zone":
							columnId = 6;
							break;
						case "bytea":
							columnId = 11;
							break;
						}

					columnModel.setColumnType(columnId);
					cfgColumnConfigRepository.save(columnModel);
				}
			});
		///InDisplayLogs

		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

		ObjectMapper logMapper = new ObjectMapper();

		ObjectNode logNodeNew = logMapper.convertValue(menuModel, ObjectNode.class);

		List<ObjectNode> logNode=new ArrayList<>();

		logNode.add(logNodeNew);

		insertInDisplayLogs(methodName,logNode,formBuilderDto.get(0).getUserId());

	   /// 
			return CustomResponse.builder().code("0").status("success").id(menuModel.getObjectId())
					.description("SAVED SUCCESSFULLY").build();
		}

		catch (Exception e) {
			e.printStackTrace();
			return CustomResponse.builder().code("0").status("Fail").id(menuModel.getObjectId())
					.description("Fail SAVED").build();

		}

	}

	@Override
	public CustomResponse updateTab(long objectId, List<FormBuilderDto> formBuilderDto) {
		
		CustomResponse resp = CustomResponse.builder().build();
		CfgObjectDefModel menuModel = objectDefRepository.findByObjectId(objectId);

			//InDisplayLogs

			ObjectMapper logMapper = new ObjectMapper();

			ObjectNode logNodeOld = logMapper.convertValue(menuModel, ObjectNode.class);

			////

		long objectID;

		if (menuModel == null) {

			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("Tab Name with this id  = " + formBuilderDto.get(0).getMenuId() + " not found!");
		} else {
//			menuModel.setParentId(objectId);
			menuModel.setMenuName(formBuilderDto.get(0).getMenuName());
			menuModel.setObjectType(16);
			menuModel.setIsMain(formBuilderDto.get(0).getIsMain());
			menuModel.setOrderNo(formBuilderDto.get(0).getOrderNo());

			Long canAddId = formBuilderDto.get(0).getCanAdd();
			menuModel.setCanAdd(canAddId == 0 ? null : canAddId);

			Long canModifyId = formBuilderDto.get(0).getCanModify();
			menuModel.setCanModify(canModifyId == 0 ? null : canModifyId);

			Long canDeleteId = formBuilderDto.get(0).getCanDelete();
			menuModel.setCanDelete(canDeleteId == 0 ? null : canDeleteId);

			Long readOnlyQbeId = formBuilderDto.get(0).getReadOnlyQbeId();
			menuModel.setReadOnlyQbeId(readOnlyQbeId == 0 ? null : readOnlyQbeId);

			Long sourceQueryId = formBuilderDto.get(0).getSourceQuery();
			menuModel.setSourceQuery(sourceQueryId == 0 ? null : sourceQueryId);

			Long condition = formBuilderDto.get(0).getCondition();
			menuModel.setCondition(condition == 0 ? null : condition);

			Long isSave = formBuilderDto.get(0).getIsSave();
			menuModel.setIsSave(isSave == 0 ? null : isSave);

			if (formBuilderDto.get(0).getIsGrid().equals("true")) {
				menuModel.setIsGrid(1);
			} else {
				menuModel.setIsGrid(0);
			}
			if (formBuilderDto.get(0).getIsQueryForm().equals("true")) {
				menuModel.setIsQueryForm("1");		
				
			} else {
				menuModel.setIsQueryForm("0");
					
			}
			if (formBuilderDto.get(0).getIsDynamicReport().equals("true")) {
				menuModel.setIsDynamicReport("1");		
				
			} else {
				menuModel.setIsDynamicReport("0");
					
			}
//			if (formBuilderDto.get(0).getIsReadOnly().equals("true")) {
//				menuModel.setIsReadOnly("1");
//			} else {
//				menuModel.setIsReadOnly("0");
//			}

			if (formBuilderDto.get(0).getIsAdvancedSearch().equals("true")) {
				menuModel.setIsAdvancedSearch(1);
			} else {
				menuModel.setIsAdvancedSearch(0);
			}

//			if (formBuilderDto.get(0).getIsSave().equals("true")) {
//				menuModel.setIsSave("1");
//			} else {
//				menuModel.setIsSave("0");
//			}
menuModel.setAdvancedSearchProcedureName(formBuilderDto.get(0).getAdvancedSearchProcedureName());

			Date now = new Date();
			menuModel.setUpdateDate(now);
			objectDefRepository.save(menuModel);
			objectID = menuModel.getObjectId();
			if (formBuilderDto.get(0).getIsQueryForm().equals("true")) {
				if(queryFormButtonRepo.checkIfExists(objectID)==0) {
					QueryFormButtonModel queryFormButtonModel=new QueryFormButtonModel();
					queryFormButtonModel.setObjectId(objectID);
					queryFormButtonModel.setButtonList(formBuilderDto.get(0).getIsQueryFormSelectedButtons());
					queryFormButtonRepo.save(queryFormButtonModel);
				}else {
					QueryFormButtonModel queryFormButtonModel=new QueryFormButtonModel();
					queryFormButtonRepo.deleteQueryForm(objectID);

					queryFormButtonModel.setObjectId(objectID);
					queryFormButtonModel.setButtonList(formBuilderDto.get(0).getIsQueryFormSelectedButtons());
					queryFormButtonRepo.save(queryFormButtonModel);
				}
				
				
			} else {
				if(queryFormButtonRepo.checkIfExists(objectID)!=0) {
					queryFormButtonRepo.deleteQueryForm(objectID);
				}
			}

		}
			///InDisplayLogs

			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

			ObjectMapper logMapperNew = new ObjectMapper();
	
			ObjectNode logNodeNew = logMapperNew.convertValue(menuModel, ObjectNode.class);
	
			List<ObjectNode> logNode=new ArrayList<>();
	
			logNode.add(logNodeOld);
	
			logNode.add(logNodeNew);
	
			insertInDisplayLogs(methodName,logNode,formBuilderDto.get(0).getUserId());
	
			/// 
		resp.setCode("0");
		resp.setStatus("success");
		resp.setDescription("updated successfully!");
		return resp;

	}

	@Override
	@Transactional
	public CustomResponse updateGrid(long objectId, List<FormBuilderTableDto> formBuilderTableDto) {
		CustomResponse resp = CustomResponse.builder().build();

		for (int x = 0; x < formBuilderTableDto.size(); x++) {
			@SuppressWarnings("unused")
			ArrayList<CfgTableConfigModel> tableList = new ArrayList<CfgTableConfigModel>();
			long tableId = -1;

			if (formBuilderTableDto.get(x).getRowSlectedStatus().equals("insert")) {
				String checkTableOrder = getOrders(objectId, String.valueOf(formBuilderTableDto.get(x).getOrderNo()),
						String.valueOf(formBuilderTableDto.get(x).getType()));
				if (checkTableOrder.equals("noMatch")) {
					List<ObjectNode> checkTableIfExist = getTableInfo(objectId,
							formBuilderTableDto.get(x).getTableName(), formBuilderTableDto.get(x).getTableOwner(),
							null);

					CfgTableConfigModel tableConfig = new CfgTableConfigModel();

					if (checkTableIfExist.size() == 0) {
						// Create table if not exists already
						tableConfig.setCreatedBy(formBuilderTableDto.get(x).getCreatedBy());
						tableConfig.setTableName(formBuilderTableDto.get(x).getTableName());
						tableConfig.setTableOwner(formBuilderTableDto.get(x).getTableOwner());
						iAddedTableRepository.save(tableConfig);
						tableId = tableConfig.getTableId();

						// Link table to object Id
						CfgTableObjectifRelModel tableRelationConfig = new CfgTableObjectifRelModel();
						tableRelationConfig.setObjectId(objectId);
						tableRelationConfig.setTableId(tableId);
						tableRelationConfig.setCreatedBy(formBuilderTableDto.get(x).getCreatedBy());
						tableRelationConfig.setOrderNo(formBuilderTableDto.get(x).getOrderNo());
						cfgTableObjectifRelRepository.save(tableRelationConfig);

												

						///InDisplayLogs Insert

						String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

						ObjectMapper logMapper = new ObjectMapper();

						ObjectNode logNodeNew = logMapper.convertValue(formBuilderTableDto.get(0), ObjectNode.class);

						List<ObjectNode> logNode=new ArrayList<>();

						//logNode.add(logNodeOld);

						logNode.add(logNodeNew);

						insertInDisplayLogs(methodName+"insert",logNode,objectId);

					   /// 

					   

				   
					} else {
						tableId = checkTableIfExist.get(0).get("tableId").asInt();

											///InDisplayLogs Update

											List<ObjectNode> formBuilderTableCols=getTabTablesFormRecords(objectId,tableId);

											String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
				   
											ObjectMapper logMapper = new ObjectMapper();
				   
											ObjectNode logNodeOld=logMapper.createObjectNode();
				   
											logNodeOld.put("tableName",iAddedTableRepository.findTableNameById(tableId));
				   
											logNodeOld.put("orderNo",cfgTableObjectifRelRepository.getTableOrder(objectId,tableId));
				   
											logNodeOld.put("columns",formBuilderTableCols.toString());
				   
											ObjectNode logNodeNew = logMapper.convertValue(formBuilderTableDto.get(0), ObjectNode.class);
				   
											List<ObjectNode> logNode=new ArrayList<>();
				   
											logNode.add(logNodeOld);
				   
											logNode.add(logNodeNew);
				   
											insertInDisplayLogs(methodName+"update",logNode,objectId);
				   
										   /// 
					}

					// Create new field set on new table creation
					CfgColumnGroupModel columnModel2 = new CfgColumnGroupModel();
					columnModel2.setName("New Columns");
					columnModel2.setOrderNb(0);
					columnModel2.setIsHidden("0");
					columnModel2.setAdvancedSearch("0");
					columnModel2.setIsreadOnly("0");
					columnModel2.setCreationDate(new Date());
					columnModel2.setCreatedBy(formBuilderTableDto.get(x).getCreatedBy());
					columnModel2.setGroupCode(columnModel2.getId());

					cfgColumnGroupRepository.save(columnModel2);
					long fieldsetid = columnModel2.getId();

					CfgFieldsetObjectModel relationModel = new CfgFieldsetObjectModel();
					relationModel.setFieldSetId(fieldsetid);
					relationModel.setObjectId(objectId);
					relationModel.setCreationDate(new Date());
					relationModel.setCreatedBy(formBuilderTableDto.get(x).getCreatedBy());
					cfgFieldsetRepository.save(relationModel);
					long columnTypeId = -1;

					String tblName = formBuilderTableDto.get(x).getTableName();
					String tblOwner = formBuilderTableDto.get(x).getTableOwner();
					// Handle table columns
					for (int i = 0; i < formBuilderTableDto.get(x).getColumns().size(); i++) {

						String colName = formBuilderTableDto.get(x).getColumns().get(i).getColumnName();
						List<AllColumnsModel> columnType = allColumnsRepository
								.findAllByTableNameAndOwnerAndColumnName(tblName, tblOwner, colName);

						CfgColumnConfigModel columnModel = new CfgColumnConfigModel();
						columnModel.setColumnDescription(capitalizeString(colName));
						columnModel.setColumnName(colName);
						columnModel.setCreatedBy(formBuilderTableDto.get(x).getCreatedBy());
						columnModel.setTableId(tableId);

						columnModel.setOrderNo(1);
						columnModel.setColumnLength(100);
						columnModel.setIsMultiple("0");
						columnModel.setIsSuspended("0");
						columnModel.setIsLink("0");
						columnModel.setMenus(null);
						columnModel.setSizeField("1");
						columnModel.setIsExcluded("0");
						columnModel.setGroupId(fieldsetid);
						columnModel.setLanguageId(13);
						columnModel.setIsEditable("0");

						switch (columnType.get(0).getNullable()) {
						case "N":
							columnModel.setIsMandatory("1");
							break;
						case "":
							columnModel.setIsMandatory("1");
							break;
						case "Y":
							columnModel.setIsMandatory("0");
							break;
						}

						Long qbeReadOnlyId = formBuilderTableDto.get(x).getColumns().get(i).getQbeReadOnly();
						columnModel.setQbeReadOnly(qbeReadOnlyId == 0 ? null : qbeReadOnlyId);

						Long defaultValueId = formBuilderTableDto.get(x).getColumns().get(i).getDefaultValue();
						columnModel.setDefaultValue(defaultValueId == 0 ? null : defaultValueId);
						
						Long mandatoryQueryId = formBuilderTableDto.get(x).getColumns().get(i).getMandatoryQuery();
						columnModel.setMandatoryQuery(mandatoryQueryId == 0 ? null : mandatoryQueryId);

						Long queryId = formBuilderTableDto.get(x).getColumns().get(i).getQuery();
						columnModel.setQuery(queryId == 0 ? null : queryId);
						switch (columnType.get(0).getDataType().toUpperCase()) {
						case "NUMBER":
							columnTypeId = 2;
							break;
						case "VARCHAR2":
							columnTypeId = 1;
							break;
						case "DATE":
							columnTypeId = 6;
							break;
						case "TIMESTAMP":
							columnTypeId = 6;
							break;
						case "FILE":
							columnTypeId = 11;
							break;
						case "SIGNATURE":
							columnTypeId = 15;
							break;
						case "BLOB":
							columnTypeId = 11;
							break;
						}

						columnModel.setColumnType(columnTypeId);
						cfgColumnConfigRepository.save(columnModel);

						resp.setCode("100");
						resp.setStatus("success");
						resp.setDescription("Successfully!");
					}

				} else {
					resp.setCode("101");
					resp.setStatus("error");
					resp.setDescription("Please enter another order number!");
				}
			}

			if (formBuilderTableDto.get(x).getRowSlectedStatus().equals("deleted")) {

				for (int i = 0; i < formBuilderTableDto.get(x).getColumns().size(); i++) {
					List<ObjectNode> checkTableIfExist = getTableInfo(objectId,
							formBuilderTableDto.get(x).getTableName(), formBuilderTableDto.get(x).getTableOwner(),
							formBuilderTableDto.get(x).getColumns().get(i).getColumnName());

					tableId = checkTableIfExist.get(0).get("tableId").asInt();
					if (checkTableIfExist.size() > 0) {
						int columnId = checkTableIfExist.get(0).get("columnId").asInt();
						cfgColumnConfigRepository.deleteByColumnId(tableId, columnId);
					}

					List<ObjectNode> checkColumnIfExist = checkColumnsExist(objectId, tableId);

					if (checkColumnIfExist.size() == 0) {
						cfgTableObjectifRelRepository.deleteFormTable(objectId, tableId);
						iAddedTableRepository.deleteTableByTableId(tableId);
											///InDisplayLogs Delete

											String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

											ObjectMapper logMapper = new ObjectMapper();
				   
											ObjectNode logNodeOld = logMapper.convertValue(formBuilderTableDto.get(0), ObjectNode.class);
				   
											List<ObjectNode> logNode=new ArrayList<>();
				   
											logNode.add(logNodeOld);
				   
											//logNode.add(logNodeNew);
				   
											insertInDisplayLogs(methodName+"delete",logNode,objectId);
				   
										   /// 
				   
										   
					}
				}

				@SuppressWarnings("rawtypes")
				List allColIds = formBuilderTableDto.get(0).getColumnsId();
				String Last = "";
				for (int i = 0; i < allColIds.size(); i++) {

					if (i == allColIds.size() - 1) {
						Last += allColIds.get(i);
					} else {
						Last += allColIds.get(i) + ",";
					}
				}

				@SuppressWarnings("unused")
				List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
						"select d.ruleData as ruleData,d.id as ruleId FROM RuleBuilderComboModel d where d.objectId = "
								+ objectId + "");

				// update ruleData
				for (int i = 0; i < allColIds.size(); i++) {
					String query = "UPDATE TECH_DYNAMIC_RULE_BUILDER\r\n"
							+ "					SET rule_data = REPLACE(rule_data, '" + allColIds.get(i) + "', '')\r\n"
							+ "					WHERE object_id =" + objectId + "";
					executeNativeCUDQuery(query);
//					entityManagerR.createNativeQuery(query).executeUpdate();

				}

				// Delete colId
				String StringQuery = "DELETE FROM TECH_DYNAMIC_RULE_BUILDER s where s.object_id =" + objectId
						+ " and s.column_id in (" + Last + ")";
				executeNativeCUDQuery(StringQuery);
//				entityManagerR.createNativeQuery(StringQuery).executeUpdate();
				resp.setCode("100");
				resp.setStatus("success");
				resp.setDescription("Successully!");
			}
		}
		return resp;
	}

//	@Override
//	public CustomResponse updateGrid(long objectId, List<FormBuilderDto> formBuilderDto) {
//		
//		
//		CustomResponse resp = CustomResponse.builder().build();
//		ArrayList<Long> tableIDsList = new ArrayList<Long>();
//		ArrayList<CfgTableConfigModel> tableList = new ArrayList<CfgTableConfigModel>();
//		
//		// Create default field set
//		CfgColumnGroupModel columnModel2 = new CfgColumnGroupModel();
//
//		columnModel2.setName("New Columns");
//		columnModel2.setOrderNb(0);
//		columnModel2.setIsHidden("0");
//		columnModel2.setAdvancedSearch("0");
//		columnModel2.setIsreadOnly("0");
//		columnModel2.setCreationDate(new Date());
//		columnModel2.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
//		columnModel2.setGroupCode(columnModel2.getId());
//		cfgColumnGroupRepository.save(columnModel2);
//		long fieldsetid = columnModel2.getId();
//
//		formBuilderDto.forEach(y -> {
//
//			String isTableFound = "0";
//			Integer tableId = -1;
//			Integer columnId = -1;
//			
//			if (y.getRowSlectedStatus().equals("insert")) {
//				CfgTableConfigModel tableConfig = new CfgTableConfigModel();
//
//				List<ObjectNode> checkTableIfExist = getTableInfo(objectId, y.getTableName(), y.getOwnerName(), null);
//				String getOrders = getOrders(objectId, String.valueOf(y.getOrderNo()));
//				if(getOrders.equals("noMatch")) {
//					if (checkTableIfExist.size() == 0) {
//
//						tableConfig.setCreatedBy(y.getCreatedBy());
//						tableConfig.setTableName(y.getTableName());
//						tableConfig.setTableOwner(y.getOwnerName());
//						iAddedTableRepository.save(tableConfig);
//
//						tableIDsList.add(tableConfig.getTableId());
//						tableList.add(tableConfig);
//						tableIDsList.forEach(z -> {
//							CfgTableObjectifRelModel tableRelationConfig = new CfgTableObjectifRelModel();
//							tableRelationConfig.setObjectId(objectId);
//							tableRelationConfig.setTableId(tableConfig.getTableId());
//							tableRelationConfig.setCreatedBy(y.getCreatedBy());
//							
//							tableRelationConfig.setOrderNo(y.getOrderNo());
//							cfgTableObjectifRelRepository.save(tableRelationConfig);
//						});
//					} else {
//						isTableFound = "1";
//						tableId = checkTableIfExist.get(0).get("tableId").asInt();
////						cfgColumnGroupRepository.deleteById(fieldsetid);
//					}
//
//					CfgFieldsetObjectModel relationModel = new CfgFieldsetObjectModel();
//					relationModel.setFieldSetId(fieldsetid);
//					relationModel.setObjectId(objectId);
//					relationModel.setCreationDate(new Date());
//					relationModel.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
//					cfgFieldsetRepository.save(relationModel);
//					long columnTypeId = -1;
//
//					List<AllColumnsModel> columnType = allColumnsRepository.findAllByTableNameAndOwnerAndColumnName(y.getTableName(), y.getOwnerName(), y.getColumnName());
//
//					CfgColumnConfigModel columnModel = new CfgColumnConfigModel();
//					columnModel.setColumnDescription(capitalizeString(y.getColumnName()));
//					columnModel.setColumnName(y.getColumnName());
//					columnModel.setCreatedBy(y.getCreatedBy());
//					columnModel.setTableId(isTableFound.equals("1") ? tableId : tableConfig.getTableId());
//
//					columnModel.setOrderNo(1);
//					columnModel.setColumnLength(100);
//					columnModel.setIsMultiple("0");
//					columnModel.setIsSuspended("0");
//					columnModel.setSizeField("1");
//					columnModel.setGroupId(fieldsetid);
//
//					switch (columnType.get(0).getNullable()) {
//					case "N":
//						columnModel.setIsMandatory("0");
//						break;
//					case "Y":
//						columnModel.setIsMandatory("1");
//						break;
//					}
//
//					Long qbeReadOnlyId = formBuilderDto.get(0).getQbeReadOnly();
//					columnModel.setQbeReadOnly(qbeReadOnlyId == 0 ? null : qbeReadOnlyId);
//
//					Long defaultValueId = formBuilderDto.get(0).getDefaultValue();
//					columnModel.setDefaultValue(defaultValueId == 0 ? null : defaultValueId);
//
//					Long queryId = formBuilderDto.get(0).getQuery();
//					columnModel.setQuery(queryId == 0 ? null : queryId);
//
//					switch (columnType.get(0).getDataType()) {
//					case "NUMBER":
//						columnTypeId = 2;
//						break;
//					case "VARCHAR2":
//						columnTypeId = 1;
//						break;
//					case "DATE":
//						columnTypeId = 6;
//						break;
//					}
//
//					columnModel.setColumnType(columnTypeId);
//					cfgColumnConfigRepository.save(columnModel);
//
//					resp.setCode("100");
//					resp.setStatus("success");
//					resp.setDescription("Updated Successfully!");
//					
//				}
//				else {
//					
////					cfgColumnGroupRepository.deleteById(fieldsetid);
//					
//					resp.setCode("101");
//					resp.setStatus("error");
//					resp.setDescription("Please enter another order number!");
//				}
//			}
//			
////			if (y.getRowSlectedStatus().equals("update")) {
////				
////				List<ObjectNode> checkTableIfExist = getTableInfo(objectId, y.getTableName(), y.getOwnerName(), null);
////				Integer tableId1 =checkTableIfExist.get(0).get("tableId").asInt(); 
////
////				   if (firstIndex[0] == -1) {
////				        firstIndex[0] = formBuilderDto.indexOf(y);
////				        cfgColumnConfigRepository.deleteColumnsByTableId(tableId1);
////				        
////				   } 
////
////				CfgColumnConfigModel columnModel = new CfgColumnConfigModel();
////				List<AllColumnsModel> columnType = allColumnsRepository
////						.findAllByTableNameAndOwnerAndColumnName(y.getTableName(), y.getOwnerName(), y.getColumnName());
////			    long columnTypeId = -1;
////				columnModel.setColumnDescription(capitalizeString(y.getColumnName()));
////				columnModel.setColumnName(y.getColumnName());
////				columnModel.setCreatedBy(y.getCreatedBy());
////				columnModel.setTableId(tableId1);
////
////				columnModel.setOrderNo(1);
////				columnModel.setColumnLength(100);
////				columnModel.setIsMultiple("0");
////				columnModel.setIsSuspended("0");
////				columnModel.setSizeField("1");
////				columnModel.setGroupId(fieldsetid);
////
////				switch (columnType.get(0).getNullable()) {
////				case "N":
////					columnModel.setIsMandatory("0");
////					break;
////				case "Y":
////					columnModel.setIsMandatory("1");
////					break;
////				}
////
////				Long qbeReadOnlyId = formBuilderDto.get(0).getQbeReadOnly();
////				columnModel.setQbeReadOnly(qbeReadOnlyId == 0 ? null : qbeReadOnlyId);
////
////				Long defaultValueId = formBuilderDto.get(0).getDefaultValue();
////				columnModel.setDefaultValue(defaultValueId == 0 ? null : defaultValueId);
////
////				Long queryId = formBuilderDto.get(0).getQuery();
////				columnModel.setQuery(queryId == 0 ? null : queryId);
////
////				switch (columnType.get(0).getDataType()) {
////				case "NUMBER":
////					columnTypeId = 2;
////					break;
////				case "VARCHAR2":
////					columnTypeId = 1;
////					break;
////				case "DATE":
////					columnTypeId = 6;
////					break;
////				}
////				
////				CfgTableObjectifRelModel tableRelationConfig = new CfgTableObjectifRelModel();
////				tableRelationConfig = cfgTableObjectifRelRepository.findByObjectIdAndTableId(objectId, tableId1);
////				tableRelationConfig.setOrderNo(y.getOrderNo());
////				cfgTableObjectifRelRepository.save(tableRelationConfig);
////				
////				CfgFieldsetObjectModel tt = new CfgFieldsetObjectModel();
////				tt.setFieldSetId(fieldsetid);
////				tt.setObjectId(objectId);
////				tt.setCreationDate(new Date());
////				tt.setCreatedBy(formBuilderDto.get(0).getCreatedBy());
////				cfgFieldsetRepository.save(tt);
////
////
////				columnModel.setColumnType(columnTypeId);
////				cfgColumnConfigRepository.save(columnModel);
////				
////				
////			}
//				
//				
//			if (y.getRowSlectedStatus().equals("deleted")) {
//				
//				List<ObjectNode> checkTableIfExist = getTableInfo(objectId, y.getTableName(), y.getOwnerName(),
//						y.getColumnName());
//				
//				tableId = checkTableIfExist.get(0).get("tableId").asInt();
//				if (checkTableIfExist.size() > 0) {
//					columnId = checkTableIfExist.get(0).get("columnId").asInt();
//					cfgColumnConfigRepository.deleteByColumnId(tableId, columnId);
//				}
//
//				List<ObjectNode> checkColumnIfExist = checkColumnsExist(objectId, tableId);
//				if (checkColumnIfExist.size() == 0) {
//
//					cfgTableObjectifRelRepository.deleteFormTable(objectId, tableId);
//					iAddedTableRepository.deleteTableByTableId(tableId);
////					objectDefRepository.deleteByObjectId((long) objectId);
//				}
//				
//				resp.setCode("100");
//				resp.setStatus("success");
//				resp.setDescription("Deleted Successully!");
//			}
//		});
//		return resp;
//	}

	@Override
	public List<CfgObjectDefModel> findByObjectPId(long objectId) {
		return objectDefRepository.findByObjectPId(objectId);
	}

	@Override
	public List<ObjectNode> getTabConfiguration(long objectId) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					" select o.isMain as isMain, o.isGrid as isGrid, o.isAdvancedSearch as isAdvancedSearch,o.isQueryForm as isQueryForm,o.isDynamicReport as isDynamicReport,"
							+ " o.isReadOnly as isReadOnly, o.menuName as menuName, o.sourceQuery as sourceQuery ,"
							+ " o.orderNo as orderNo, o.canAdd as canAdd,"
							+ " o.canModify as canModify, o.readOnlyQbeId as readOnlyQbeId, "
							+ " o.canDelete as canDelete , o.condition as condition , o.isSave as isSave "
							+ " from CfgObjectDefModel o  where o.objectId = " + objectId);

			return result;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override

	public CustomResponse deleteTab(long objectId,long userId) {

		CfgObjectDefModel cfgObjectDefModel=objectDefRepository.findByObjectId(objectId);
		cfgColumnConfigRepository.deleteByColumnId(objectId);
		cfgTableObjectifRelRepository.deleteFormBuilder(objectId);
		iAddedTableRepository.deleteByTableId(objectId);
		cfgFieldsetRepository.deleteByObjectId(objectId);
		objectDefRepository.deleteByObjectId((long) objectId);
			///InDisplayLogs

			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

			ObjectMapper logMapper = new ObjectMapper();
   
		   ObjectNode logNodeNew = logMapper.convertValue(cfgObjectDefModel, ObjectNode.class);
   
		   List<ObjectNode> logNode=new ArrayList<>();
   
		   logNode.add(logNodeNew);
   
			insertInDisplayLogs(methodName,logNode,userId);
   
		   /// 
		return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
	}

	@Override
	public List<QbeUserQueryModel> getSourceQuery() {
		return qbeUserQueryRepository.getSourceQuery();
	}

	@Override
	public List<ObjectNode> getGroupingTables() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT ownerName as ownerName, tableName as tableName, columnName as columnName FROM DbaTabColsModel "
						+ " where (ownerName) like '%DBA' ");
	}

	@Override
	public List<ObjectNode> getSelectedRows(long objectId) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					"SELECT T.tableOwner as ownerName, T.tableName as tableName, C.columnName as columnName "
							+ " FROM CfgTableConfigModel T, CfgColumnConfigModel C, "
							+ " CfgObjectDefModel O, CfgTableObjectifRelModel R "
							+ " WHERE T.tableId = C.tableId AND O.objectId = R.objectId "
							+ " AND T.tableId = R.tableId  AND O.objectId = " + objectId);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ObjectNode> getTableInfo(long objectId, String tableName, String ownerName, String columnName) {
		try {
			String query = "";

			if (columnName == null) {
				query = "SELECT T.tableId as tableId"
						+ " FROM CfgObjectDefModel O, CfgTableConfigModel T, CfgTableObjectifRelModel R, CfgColumnConfigModel C "
						+ " WHERE O.objectId = R.objectId " + " AND T.tableId = R.tableId "
						+ " AND T.tableId = C.tableId " + " AND O.objectId = " + objectId + " AND T.tableName = '"
						+ tableName + "'" + " AND T.tableOwner = '" + ownerName + "' ";
			} else {
				query = "SELECT T.tableId as tableId, C.columnId as columnId"
						+ " FROM CfgObjectDefModel O, CfgTableConfigModel T, CfgTableObjectifRelModel R, CfgColumnConfigModel C "
						+ " WHERE O.objectId = R.objectId " + " AND T.tableId = R.tableId "
						+ " AND T.tableId = C.tableId " + " AND O.objectId = " + objectId + " AND T.tableName = '"
						+ tableName + "'" + " AND T.tableOwner = '" + ownerName + "'" + " AND C.columnName = '"
						+ columnName + "'";
			}

			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR, query);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Long handleInitializedLongVariables(long variable) {
		Long returnedVal = variable;
		returnedVal = returnedVal == 0 ? null : returnedVal;
		return returnedVal;
	}

	@Override
	public CustomResponse columnModifier(long columnId, List<FormBuilderDto> formBuilderDto,long userId) {

		System.out.println("==============IN COLUMN MODIFIER FUNCTION=============");

		System.out.println("Form Builder Dto>>>>>>"+formBuilderDto);
		CustomResponse resp = CustomResponse.builder().build();
		CfgColumnConfigModel columnModel = cfgColumnConfigRepository.findByColumnId(columnId);

		try {
			if (columnModel == null) {
				resp.setCode("1");
				resp.setStatus("Fail");
				resp.setDescription("Tab Name with this id  = " + formBuilderDto.get(0).getColumnId() + " not found!");
			} else {

//				columnModel.setColumnDescription(capitalizeString(formBuilderDto.get(0).getColumnDescription()));
				columnModel.setColumnDescription(formBuilderDto.get(0).getColumnDescription());
				columnModel.setColumnName(formBuilderDto.get(0).getColumnName());
				columnModel.setColumnType(handleInitializedLongVariables(formBuilderDto.get(0).getColumnType()));
				columnModel.setColumnLength(handleInitializedLongVariables(formBuilderDto.get(0).getColumnLength()));
				columnModel.setOrderNo(handleInitializedLongVariables(formBuilderDto.get(0).getOrderNo()));
				// columnModel.setOrderNo(formBuilderDto.get(0).getOrderNo());
				columnModel.setGroupId(handleInitializedLongVariables(formBuilderDto.get(0).getFieldSet()));

				if (formBuilderDto.get(0).getMenus() != null) {
					columnModel.setMenus(handleInitializedLongVariables(formBuilderDto.get(0).getMenus()));
				}

				columnModel.setDependencyDefaultValue(
						handleInitializedLongVariables(formBuilderDto.get(0).getDependencyDefaultValue()));

				String sizeField = formBuilderDto.get(0).getSizeField();
				columnModel.setSizeField(sizeField == null ? "1" : sizeField);

				Long qbeReadOnlyId = formBuilderDto.get(0).getQbeReadOnly();
				columnModel.setQbeReadOnly(qbeReadOnlyId == 0 ? null : qbeReadOnlyId);

				Long defaultValueId = formBuilderDto.get(0).getDefaultValue();
				columnModel.setDefaultValue(defaultValueId == 0 ? null : defaultValueId);
				
				Long mandatoryQueryId = formBuilderDto.get(0).getMandatoryQuery();
				columnModel.setMandatoryQuery(mandatoryQueryId == 0 ? null : mandatoryQueryId);

				Long queryId = formBuilderDto.get(0).getQuery();
				columnModel.setQuery(queryId == 0 ? null : queryId);

				if (formBuilderDto.get(0).getIsMandatory().equals("true")) {
					columnModel.setIsMandatory("2");
				} else {
					columnModel.setIsMandatory("0");
				}

				if (formBuilderDto.get(0).getIsMultiple().equals("true")) {
					columnModel.setIsMultiple("1");
				} else {
					columnModel.setIsMultiple("0");
				}
				

				if (formBuilderDto.get(0).getIsSuspended().equals("true")) {
					columnModel.setIsSuspended("1");
				} else {
					columnModel.setIsSuspended("0");
				}

				if (formBuilderDto.get(0).getIsLink().equals("true")) {
					columnModel.setIsLink("1");
				} else {
					columnModel.setIsLink("0");
				}

				if (formBuilderDto.get(0).getIsExcluded().equals("true")) {
					columnModel.setIsExcluded("1");
				} else {
					columnModel.setIsExcluded("0");
				}
				if (formBuilderDto.get(0).getIsEditable().equals("true")) {
					columnModel.setIsEditable("1");
				} else {
					columnModel.setIsEditable("0");
				}
				if (formBuilderDto.get(0).getIsArabic().equals("true")) {
					columnModel.setLanguageId(137);
				} else {
					columnModel.setLanguageId(13);
				}

				if (formBuilderDto.get(0).getIsGridHidden().equals("true")) {
					columnModel.setIsGridHidden("1");
				} else {
					columnModel.setIsGridHidden("0");
				}
				
				cfgColumnConfigRepository.save(columnModel);
				System.out.println("zzzzzzzzzzzzzzz>>>>>>"+columnModel);

				///InDisplayLogs Insert

				 String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

				 CfgColumnConfigModel oldColumnConfig=cfgColumnConfigRepository.findByColumnId(columnId);

				 ObjectMapper logMapper = new ObjectMapper();

		         ObjectNode logNodeOld = logMapper.convertValue(oldColumnConfig, ObjectNode.class);

		         ObjectNode logNodeNew = logMapper.convertValue(columnModel, ObjectNode.class);

		         List<ObjectNode> logNode=new ArrayList<>();

		         logNode.add(logNodeOld);

		         logNode.add(logNodeNew);

			     insertInDisplayLogs(methodName,logNode,userId);

				/// 
			}
			resp.setCode("0");
			resp.setStatus("success");
			resp.setDescription("updated successfully!");
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ObjectNode> getColumnConfiguration(long columnId) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					"select c.columnName as columnName, c.columnDescription as columnDescription, concat(t.tableOwner, '.', t.tableName) as tableName,"
							+ " c.columnType as columnType, c.orderNo as orderNo, c.columnLength as columnLength, c.dependencyDefaultValue as dependencyDefaultValue, "
							+ " c.isMandatory as isMandatory, c.qbeReadOnly  as qbeReadOnly, "
							+ " c.isMultiple as isMultiple, c.isSuspended as isSuspended, "
							+ " c.sizeField as sizeField, c.defaultValue as defaultValue, c.mandatoryQuery as mandatoryQuery, "
							+ " c.query as query , c.groupId as fieldSet, c.isLink as isLink, c.menus as menus,c.isExcluded as isExcluded,c.languageId as isArabic,c.isEditable as isEditable"
							+ " from CfgColumnConfigModel c, CfgTableConfigModel t where t.tableId = c.tableId and c.columnId = "
							+ columnId);

			return result;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ObjectNode> checkColumnsExist(long objectId, long tableId) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					"SELECT C.columnId as columnId, C.columnName as columnName"
							+ " FROM CfgObjectDefModel O, CfgTableConfigModel T, CfgTableObjectifRelModel R, CfgColumnConfigModel C "
							+ " WHERE O.objectId = R.objectId " + " AND T.tableId = R.tableId "
							+ " AND T.tableId = C.tableId " + " AND O.objectId = " + objectId + " AND T.tableId = "
							+ tableId);
			return result;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional
	public String getPrimaryKey(String tableName) {

		String sql = "";
		String sqlRes = "";
		String returnValue = "-1";
		String columnName = "-1";

		String TableName = tableName.toUpperCase().split("\\.")[1];
		String OwnerName = tableName.toUpperCase().split("\\.")[0];

		String TabVal = TableName.indexOf(".") != -1 ? TableName.split("\\.")[1] : TableName;
		sql = "select count(1) from SDEDBA.ref_sys_table_sequence where TABLE_NAME in ('" + TabVal + "') AND lower(OWNER) = lower('" + OwnerName + "')";
		sqlRes = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());

		if (!"0".equals(sqlRes) && !"".equals(sqlRes)) {
			sql = "select column_name from SDEDBA.ref_sys_table_sequence where TABLE_NAME in ('" + TabVal + "') AND lower(OWNER) = lower('" + OwnerName + "')  limit 1" ;
			columnName = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());
			returnValue = columnName;
		}
		return returnValue;

	}

	@Override
	@Transactional
	public String GetSequence(DynamicFormDto inserDynamicDto) {

		String sequenceName = "-1";
		int sequenceVal = -1;
		String columnName = "-1";
		String returnValue = "-1";
		String sql = "";
		String sqlRes = "";

		String TableName = inserDynamicDto.getTableName().toUpperCase().split("\\.")[1];
		String OwnerName = inserDynamicDto.getTableName().toUpperCase().split("\\.")[0];
		String TabVal = TableName.indexOf(".") != -1 ? TableName.split("\\.")[1] : TableName;
		sql = "select count(1) from SDEDBA.ref_sys_table_sequence where TABLE_NAME in ('" + TabVal + "') AND lower(OWNER) = lower('"
				+ OwnerName + "')";

				//system.out.println(" sql -------------- "+sql);

		sqlRes = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());

		//system.out.println(" sqlRes -------------- "+sqlRes);


		if (!"0".equals(sqlRes)) {
			sql = "select upper(sequence_name) from SDEDBA.ref_sys_table_sequence where TABLE_NAME in ('" + TabVal
					+ "') AND lower(OWNER) = lower('" + OwnerName + "')" + " LIMIT 1 ";
			sequenceName = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());
			sql = "select upper(column_name) from SDEDBA.ref_sys_table_sequence where upper(sequence_name) in ('" + sequenceName + "')";
			columnName = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());
			// sql = "select " + OwnerName + "." + sequenceName + ".NEXTVAL"; //oracle
			sql = "select nextval('" + OwnerName + "." + sequenceName + "') ";

			sequenceVal = Integer.parseInt(entityManagerR.createNativeQuery(sql).getSingleResult().toString());
			returnValue = columnName + ":" + sequenceVal;
		}

		//system.out.println(" SEQUANCE -------------- "+returnValue);
		return returnValue;
	}

	@Override
	@Transactional
	public String GetSequenceWithStaticTable(String tableName) {

		String sequenceName = "-1";
		int sequenceVal = -1;
		String columnName = "-1";
		String returnValue = "-1";
		String sql = "";
		String sqlRes = "";

		String TableName = tableName.toUpperCase().split("\\.")[1];
		String OwnerName = tableName.toUpperCase().split("\\.")[0];
		String TabVal = TableName.indexOf(".") != -1 ? TableName.split("\\.")[1] : TableName;
		sql = "select count(1) from SDEDBA.ref_sys_table_sequence where TABLE_NAME in ('" + TabVal + "') AND lower(OWNER) = lower('"
				+ OwnerName + "')";
		sqlRes = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());
		if (!"0".equals(sqlRes)) {
			sql = "select upper(sequence_name) from SDEDBA.ref_sys_table_sequence where TABLE_NAME in ('" + TabVal
					+ "') AND lower(OWNER) = lower('" + OwnerName + "')  limit 1";
			sequenceName = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());
			sql = "select upper(column_name) from SDEDBA.ref_sys_table_sequence where upper(sequence_name) in ('" + sequenceName + "')";
			columnName = String.valueOf(entityManagerR.createNativeQuery(sql).getSingleResult());
			// sql = "select " + OwnerName + "." + sequenceName + ".NEXTVAL "; // oracle
			sql = "select nextval('" + OwnerName + "." + sequenceName + "') ";
			sequenceVal = Integer.parseInt(entityManagerR.createNativeQuery(sql).getSingleResult().toString());
			returnValue = columnName + ":" + sequenceVal;
		}
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public List<String> GetCol(String TableName) {
		@SuppressWarnings({ "rawtypes"})
		List<String> MyListC = new ArrayList();
		String TabV = TableName;
		@SuppressWarnings("unused")
		String sqlO = "";

		String sqlT = "select column_name from information_schema.columns  where (table_name) in ('" + TabV + "')";
		MyListC = entityManagerR.createNativeQuery(sqlT).getResultList();
		return MyListC;
	};

	public List<String> GetAllPTablesName(List<String> tableName) {
		@SuppressWarnings("unused")
		List<String> listofTableNameP = new ArrayList<>();
		for (int i = 0; i < tableName.size(); i++) {
			String sql = "      SELECT A.column_name, T.constraint_type"
					+ "  FROM INFORMATION_SCHEMA.constraint_column_usage A, information_schema.table_constraints  T" + "    WHERE T.CONSTRAINT_NAME = A.constraint_name"
					+ "   and T.table_name = A.table_name" + "   and T.r_constraint_name = '" + tableName.get(i) + "'"
					+ "   and T.constraint_type = 'PRIMARY KEY'" + "   and T.TABLE_SCHEMA = A.TABLE_SCHEMA" + "   and T.TABLE_SCHEMA = 'SDEDBA'";
			@SuppressWarnings("unused")
			String result = (String) entityManagerR.createNativeQuery(sql).getSingleResult();

		}
		return null;
	}

	public String getForeignKeyValue(String columnName, String allPrimaryKeyColumns, String allPrimaryKeyValues) {

		String Value = "";

		try {

			String[] primaryKeyColumn = allPrimaryKeyColumns.split(",");
			String[] primaryKeyValues = allPrimaryKeyValues.split(",");

			List<String> primaryKeyColumnList = Arrays.asList(primaryKeyColumn);
			List<String> primaryKeyValuesList = Arrays.asList(primaryKeyValues);

			for (int i = 0; i < primaryKeyColumnList.size(); i++) {
				if (primaryKeyColumnList.get(i).equals(columnName)) {
					Value = primaryKeyValuesList.get(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Value;
	}

	public String getForeignKeyValues(String columnName, String allPrimaryKeyColumns) {

		String Value = "";

		try {

			String[] primaryKeyColumn = allPrimaryKeyColumns.split(",");
//			String[] primaryKeyValues = allPrimaryKeyValues.split(",");

			List<String> primaryKeyColumnList = Arrays.asList(primaryKeyColumn);
//			List<String> primaryKeyValuesList = Arrays.asList(primaryKeyValues);  

			for (int i = 0; i < primaryKeyColumnList.size(); i++) {
				if (primaryKeyColumnList.get(i).equals(columnName)) {
					Value = primaryKeyColumnList.get(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Value;
	}
	
	public String cleanCommonInfo(String commonInfo) {
		String returnValue = "";
		String[] commonInfoAr = commonInfo.split(",");
		for(int i = 0; i < commonInfoAr.length; i ++) {
			if(!returnValue.equals("")) {
				if(returnValue.indexOf(commonInfoAr[i]) == -1) {
					returnValue += commonInfoAr[i] + ",";
				}
			} else {
				returnValue += commonInfoAr[i] + ",";
			}
		}
		
		return returnValue;
	}
	
	@Override
	@Transactional
	public CustomResponse insertDynQuery(List<DynamicFormGet> dynamicFormGet) {
		
		boolean isFile = false;
		boolean isSignature = false;
		V21FileModel v21filemodel = new V21FileModel();
		V21FilesDto v21FilesDto = new V21FilesDto();
		
		V21SignatureModel v21Signaturemodel = new V21SignatureModel();
		V21SignatureDto v21SignatureDto = new V21SignatureDto();
		String ResultGetPrimaryCol = "";
		int foundMandatoryEmptyForeignColumns = 0;
		int hasEnoughColumns = 0;
		Map<String, String> generalInfo = new HashMap<>();
		
		Map<String, String> additionValue = new HashMap<>();
		@SuppressWarnings("unused")
		String tblName = "";
		
		
		String commonInfo = "";
		if (!dynamicFormGet.get(0).getSelectedRowId().equals("-1")) {
			JSONArray selectedRowIds = new JSONArray(new String(dynamicFormGet.get(0).getSelectedRowId()));
			for (int a = 0; a < selectedRowIds.length(); a++) {
				String colName = selectedRowIds.getJSONObject(a).getString("colname").toUpperCase().trim();
				String colVal = selectedRowIds.getJSONObject(a).getString("colvalue");
				
				generalInfo.put(colName,colVal);
				
				commonInfo = commonInfo + colName + "~~" + colVal + ",";
				//system.out.println("commonInfo ------------------- > " + commonInfo	);
			}
			commonInfo = commonInfo.endsWith(",") ? commonInfo.substring(0, commonInfo.length() - 1) : commonInfo;
		}
		//system.out.println("INSRT DYNAMIC  ------------------- > "	);

		commonInfo = cleanCommonInfo(commonInfo);
		CustomResponse resp = CustomResponse.builder().build();
		String tableName = "";
		String query = "";
		try {
	            String tableNames = "";
	            String tableOwners = "";
	            for (int i = 0; i < dynamicFormGet.get(0).getDynamicTable().size(); i++) {
	                if (tableOwners.replace("'", "").equals(dynamicFormGet.get(0).getDynamicTable().get(i).getTableName().split("\\.")[0])) {
	                    tableOwners = tableOwners.replace("'" + tableOwners + "'", "");
	                }
	                tableOwners = tableOwners + "," + "'" + dynamicFormGet.get(0).getDynamicTable().get(i).getTableName().split("\\.")[0] + "'";
	                tableNames = tableNames + "," + "'" + dynamicFormGet.get(0).getDynamicTable().get(i).getTableName().split("\\.")[1] + "'";
	            }
	            tableOwners = tableOwners.indexOf(",") == 0 ? tableOwners.substring(1) : tableOwners;
	            tableNames = tableNames.indexOf(",") == 0 ? tableNames.substring(1) : tableNames;

			@SuppressWarnings("unused")
			String reserveAllColsAndVals = "";
            @SuppressWarnings("unused")
			String getSequence_colName = "";
            @SuppressWarnings("unused")
			String getSequence_colVal = "";
            @SuppressWarnings("unused")
			String getSequence_colName2 = "";
            @SuppressWarnings("unused")
			String getSequence_colVal2 = "";
            for (int x = 0; x < dynamicFormGet.get(0).getDynamicTable().size(); x++) {
		
                DynamicFormDto test = new DynamicFormDto();
                test.setTableName(dynamicFormGet.get(0).getDynamicTable().get(x).getTableName());
		
                String finalSequance = GetSequence(test);
                
                if (!finalSequance.equals("-1")) {
                    if (x == 0) {
                        getSequence_colName2 += finalSequance.split(":")[0];
                        getSequence_colVal2 += finalSequance.split(":")[1];
		
                    } else {
                        getSequence_colName2 += "," + finalSequance.split(":")[0];
                        getSequence_colVal2 += "," + finalSequance.split(":")[1];
                    }
                }
		//system.out.println("getSequence_colVal2 >>>>>>>>>>>>>>>>>>>>>>>>>>> : " + getSequence_colVal2);
            }

			int m = 0;
			String u = "";
			if (!commonInfo.equals("")) {
				String[] k = commonInfo.split(",");
				for (int l = 0; l < k.length; l++) {
					if (l == k.length - 1) {
						u += "'" + k[l].split("~~")[0] + "'";
					} else {
						u += "'" + k[l].split("~~")[0] + "'" + ",";
					}
				}
			} else {
				m = 1;
			}

			String getSequence_colName1 = "";
			String getSequence_colVal1 = "";
			Map<String, String> indexOfCol1 = new HashMap<>();
			for (int y = 0; y < dynamicFormGet.get(0).getDynamicTable().size(); y++) {
				String tableName1 = dynamicFormGet.get(0).getDynamicTable().get(y).getTableName();
				if (!u.equals("")) {
					// Check if table has common columns in order to generate sequence for it if
					// not, not to be used
					String checkIfSeqCanBeUsed = "SELECT cast(COUNT(1) as text) FROM information_schema.columns\r\n"
							+ " WHERE TABLE_NAME = '" + tableName1.split("\\.")[1] + "'\r\n" + " AND COLUMN_NAME IN ("
							+ u + ")" + " AND TABLE_SCHEMA = '" + tableName1.split("\\.")[0] + "'";
					String checkIfSeqCanBeUsedRes = executeNativeSelectQuery(checkIfSeqCanBeUsed);
					if (!checkIfSeqCanBeUsedRes.equals("0")) {
						m = 1;
					} else {
						m = 0;
					}
				}
				
				int hasEnoughColumns_ = 0;
				for (int j = 0; j < dynamicFormGet.get(0).getDynamicTable().get(y).getColumns().size(); j++) {
					hasEnoughColumns_ ++;
				}
				

				DynamicFormDto test1 = new DynamicFormDto();
				test1.setTableName(tableName1);
				
				if(hasEnoughColumns_ >= 5)
					if (!GetSequence(test1).equals("-1") && m == 1) {
						getSequence_colName1 = GetSequence(test1).split(":")[0];
						getSequence_colVal1 = GetSequence(test1).split(":")[1];
						indexOfCol1.put(getSequence_colName1, getSequence_colVal1);
					} else if (!GetSequence(test1).equals("-1")) {
						getSequence_colName1 = GetSequence(test1).split(":")[0];
						getSequence_colVal1 = GetSequence(test1).split(":")[1];
						indexOfCol1.put(getSequence_colName1, getSequence_colVal1);
					}
			}

			for (int i = 0; i < dynamicFormGet.get(0).getDynamicTable().size(); i++) {
				tblName ="";
				foundMandatoryEmptyForeignColumns = 0;
				hasEnoughColumns = 0;

				tableName = dynamicFormGet.get(0).getDynamicTable().get(i).getTableName();
				String columnNames = "";
				String columnValues = "";
				DynamicFormDto test = new DynamicFormDto();
				test.setTableName(tableName);

			//	v21FilesDto.setTableName(tableName);
			//	v21filemodel.setTableName(tableName);

				
				// Get all foreign keys that are not nullable from a given table
				String getAllMandatoryForeigns = "";
				String getAllMandatoryForeignsRes = "";

				// getAllMandatoryForeigns = "SELECT STRING_AGG(COLUMNSS, ',') WITHIN GROUP(ORDER BY 1)\r\n" + 
				getAllMandatoryForeigns = "SELECT \t\t\t\tSTRING_AGG(upper(tbl.COLUMNSS), ',' ORDER BY tbl.COLUMNSS) AS COLUMNSS\r\n" + //
										"  FROM (SELECT DISTINCT A.COLUMN_NAME AS COLUMNSS\r\n" + 
										"          FROM INFORMATION_SCHEMA.constraint_column_usage A, information_schema.table_constraints T, information_schema.columns DTC\r\n" + 
										"         WHERE T.CONSTRAINT_NAME = A.CONSTRAINT_NAME\r\n" + 
										"           AND A.COLUMN_NAME = DTC.COLUMN_NAME\r\n" + 
										"           AND A.TABLE_NAME = DTC.TABLE_NAME\r\n" + 
										"           AND T.TABLE_NAME = A.TABLE_NAME            \r\n" + 
										"           AND lower(T.TABLE_NAME) = lower('"+tableName.split("\\.")[1]+"')\r\n" + 
										"           AND T.CONSTRAINT_TYPE = 'FOREIGN KEY'\r\n" + 
										"           AND T.TABLE_SCHEMA = A.TABLE_SCHEMA\r\n" + 
										"           AND DTC.TABLE_SCHEMA = A.TABLE_SCHEMA\r\n" + 
										"           AND lower(T.TABLE_SCHEMA) = lower('"+tableName.split("\\.")[0]+"')\r\n" + 
										"           AND DTC.IS_NULLABLE = 'NO') as tbl";
				getAllMandatoryForeignsRes = executeNativeSelectQuery(getAllMandatoryForeigns);

				int foundSeqVal = 0;
				for (int j = 0; j < dynamicFormGet.get(0).getDynamicTable().get(i).getColumns().size(); j++) {
					// for (int j = 0; j < 1; j++) {
					isFile=false;
					isSignature=false;
					hasEnoughColumns++;

					foundSeqVal = 0;
					String colVal = dynamicFormGet.get(0).getDynamicTable().get(i).getColumns().get(j).getColValue();
					String colType = dynamicFormGet.get(0).getDynamicTable().get(i).getColumns().get(j).getColType();
					String colName = dynamicFormGet.get(0).getDynamicTable().get(i).getColumns().get(j).getColName();
					
					
					String getPrimaryCol = "SELECT STRING_AGG(Distinct(upper(cols.column_name)), ',') " + " FROM information_schema.table_constraints cons "
	                           + " JOIN INFORMATION_SCHEMA.constraint_column_usage cols ON cons.constraint_name = cols.constraint_name "
	                           + " WHERE lower(cons.TABLE_SCHEMA) = lower('" + tableName.split("\\.")[0] + "') AND lower(cons.table_name) = lower('" + tableName.split("\\.")[1]
	                           + "') AND cons.constraint_type = 'PRIMARY KEY'";

					ResultGetPrimaryCol = executeNativeSelectQuery(getPrimaryCol);
					
//					if (colName.indexOf("~") == -1 && !colName.contains("Custom_") && !colName.isEmpty() && !("'null'").equals(colVal)) {
						
						String hayala ="";
						
						columnNames += colName + ",";
						
						for (Map.Entry<String, String> entry : generalInfo.entrySet()) {
							String keys = entry.getKey();
							String values = entry.getValue();
							
							hayala += keys+"/"+values+"~";
							
						}
						
						
						
for (Map.Entry<String, String> entry : indexOfCol1.entrySet()) {
							
							String key = entry.getKey();
							String value = entry.getValue();
							
							hayala += key+"/"+value+"~";
							
							if (colName.equals(key)) {
								colVal = value;
								reserveAllColsAndVals += colName + "~A~" + colVal + ",";
								foundSeqVal = 1;
							}
							if(("TECH_"+key).equals(colName)) {
								
								colVal = value;
							}
						}
if(hayala.endsWith("~")) {
	hayala = hayala.substring(0, hayala.lastIndexOf("~"));
}
newProcParams = hayala;
					
//system.out.println("hayala >>>>>>>>>>>>>>>>>>>>>>>> : " + hayala);
						String resultGetPrimaryVal="";
						if(ResultGetPrimaryCol != null){
							if(ResultGetPrimaryCol.indexOf(",") != -1) {
							String[] tt1 = ResultGetPrimaryCol.split(",");
							for (int i11 = 0; i11 < tt1.length; i11++) {
								if (colName.equals(tt1[i11])) {
									if(resultGetPrimaryVal =="") {
										resultGetPrimaryVal = colVal;
									}else {
										resultGetPrimaryVal =","+ colVal;
									}
								}
								
							}

							v21FilesDto.setPrimaryVal(resultGetPrimaryVal);
							v21filemodel.setPrimaryVal(resultGetPrimaryVal);
							v21SignatureDto.setPrimaryVal(colVal);
							v21Signaturemodel.setPrimaryVal(colVal);
						}else {
							
						if (colName.equals(ResultGetPrimaryCol)) {
							v21FilesDto.setPrimaryVal(colVal);
							v21filemodel.setPrimaryVal(colVal);
							//////////////////////jp///////////////////////
							v21SignatureDto.setPrimaryVal(colVal);
							v21Signaturemodel.setPrimaryVal(colVal);
							///////////////////////end/////////////////
						}
						}}
						
						// Check if any foreign mandatory column is empty or null to ignore it's table
						if (!"".equals(getAllMandatoryForeignsRes) && getAllMandatoryForeignsRes != null) {
							if (getAllMandatoryForeignsRes.indexOf(",") != -1) {
								String[] getAllMandatoryForeignsAr = executeNativeSelectQuery(getAllMandatoryForeigns).split(",");
								for (String row : getAllMandatoryForeignsAr) {
									if (row.equals(colName) && ("".equals(colVal) || colVal.equals("undefined") || colVal.equals("null")) 
											&& commonInfo.indexOf(row) == -1 
											&& !row.equals(ResultGetPrimaryCol)) {
										foundMandatoryEmptyForeignColumns++;
									}
								}
							} else {

								if (getAllMandatoryForeignsRes.equals(colName)
										&& ("".equals(colVal) || colVal.equals("undefined") || colVal.equals("null") || colVal.equals("NULL") )
										&& commonInfo.indexOf(getAllMandatoryForeignsRes) == -1 
										&& !getAllMandatoryForeignsRes.equals(ResultGetPrimaryCol)) {
									foundMandatoryEmptyForeignColumns++;
								}
							}
						}
						if (foundSeqVal == 1) {
							columnValues += "'" + colVal + "'" + ",";
						} else if (commonInfo.indexOf(colName) != -1) {
							String[] commonInfoAr = commonInfo.split(",");
							for (int h = 0; h < commonInfoAr.length; h++) {
								if (colName.equals(commonInfoAr[h].split("~~")[0])) {
									columnValues += commonInfoAr[h].split("~~")[1] + ",";
									reserveAllColsAndVals += colName + "~A~" + commonInfoAr[h].split("~~")[1];
								}
							}
						}else if( colName.equals("CREATED_BY") || "CREATED_BY".equals(colName) ) {

							columnValues += colVal + ",";
						} 
						else if (("text".equals(colType) && !"".equals(colVal)&&!"null".equals(colVal)) || "textarea".equals(colType)) {
							
							additionValue.put(colName, "'"+colVal+"'");
							columnValues += "'" + colVal + "'" + ",";
							reserveAllColsAndVals += colName + "~A~" + colVal + ",";
						} 
						
						else if ("number".equals(colType) && !"".equals(colVal) && !("null").equals(colVal) ) {

							additionValue.put(colName, colVal);
							if (colVal.equals("undefined")) {
								additionValue.put(colName, null);
								columnValues += null + ",";
							} 
							else {
								columnValues += colVal + ",";
								additionValue.put(colName, colVal);
								reserveAllColsAndVals += colName + "~A~" + colVal + ",";
							}
						} else if ("combo".equals(colType) && !colVal.equals("undefined") && !colVal.isEmpty() && !"'null'".equals(colVal)) {
							additionValue.put(colName, colVal.split(",")[0]);
							columnValues += colVal.split(",")[0] + ",";
							reserveAllColsAndVals += colName + "~A~" + colVal.split(",")[0] + ",";
						}
						
						else if ("lookup".equals(colType) && !colVal.equals("undefined") && !colVal.isEmpty()) {
							if("null".equals(colVal)) {
						columnValues += null + ",";
							}else {

							columnValues += "'" + colVal + "',";
							}

//							additionValue.put(colName, colVal.split(",")[0]);
//							columnValues += colVal.split(",")[0] + ",";
//							reserveAllColsAndVals += colName + "~A~" + colVal.split(",")[0] + ",";
						} 
						
						else if ("checkbox".equals(colType) && !colVal.equals("undefined") && !colVal.isEmpty()) {
							if("true".equals(colVal)) {
								
								columnValues += "'1',";
							
							}else {

								columnValues += "'0',";
							}
						} 
						
						else if ("date".equals(colType) && !colName.equals("CREATION_DATE")
								&& !colName.equals("UPDATE_DATE")) {
							if (colVal.equals("Invalid date") ) {
								additionValue.put(colName, null);
								columnValues += null + ",";
							} else {
								columnValues += "TO_DATE(" + "'" + colVal + "'" + ", 'MM/dd/yyyy')" + ",";
								additionValue.put(colName, "TO_DATE(" + "'" + colVal + "'" + ", 'MM/dd/yyyy')");
							}
							reserveAllColsAndVals += colName + "~A~" + "TO_DATE(" + "'" + colVal + "'" + ", 'MM/dd/yyyy')" + ",";
						}else if(colName.equals("UPDATED_BY") && "null".equals(colVal)) {
							columnValues += null + ",";
						} 
						
						
						
						else if ("CREATION_DATE".equals(colName) || colName.equals("UPDATE_DATE")) {
							additionValue.put(colName, "CURRENT_DATE");
							columnValues += "CURRENT_DATE" + ",";
						}
						
						else if ("file".equals(colType) && !"".equals(colVal)) {
							
							if(colVal.indexOf("data:") != -1) {
								
							v21FilesDto.setTableName(tableName);
							v21filemodel.setTableName(tableName);
							v21FilesDto.setPrimaryCol(ResultGetPrimaryCol);
							v21filemodel.setPrimaryCol(ResultGetPrimaryCol);
							
							V21FileModel newV21filemodel = new V21FileModel();
							newV21filemodel.setFileCol(colName);
							newV21filemodel.setFileData(colVal.getBytes());
							newV21filemodel.setPrimaryCol(ResultGetPrimaryCol);
							newV21filemodel.setPrimaryVal(v21FilesDto.getPrimaryVal());
							newV21filemodel.setTableName(v21FilesDto.getTableName());
							
							v21fileRepo.save(newV21filemodel);
							isFile = true;
							v21filemodel.setFileCol(colName);
							v21filemodel.setFileData(colVal.getBytes());
							v21FilesDto.setFileCol(colName);
							columnValues += "UTL_RAW.CAST_TO_RAW('" + 1 + "')" + ",";
						}else {
							columnValues += "UTL_RAW.CAST_TO_RAW('" + 1 + "')" + ",";
						}
						}
						else if ("signature".equals(colType) && !"".equals(colVal)) {
							if(colVal.indexOf("data:image") != -1) {
							v21SignatureDto.setTableName(tableName);
							v21Signaturemodel.setTableName(tableName);
							v21SignatureDto.setPrimaryCol(ResultGetPrimaryCol);
							v21Signaturemodel.setPrimaryCol(ResultGetPrimaryCol);
							isSignature = true;
							v21Signaturemodel.setFileCol(colName);
							v21Signaturemodel.setFileData(colVal.getBytes());
							v21SignatureDto.setFileCol(colName);
							columnValues += "UTL_RAW.CAST_TO_RAW('" + 1 + "')" + ",";
							v21SignatureRepo.save(v21Signaturemodel);
						}else {
							columnValues += "UTL_RAW.CAST_TO_RAW('" + 1 + "')" + ",";
						}
						}
						else if ("".equals(colVal) || colVal.equals("undefined")) {
							additionValue.put(colName, null);
							columnValues += null + ",";
						} else if ("text".equals(colType)) {
							columnValues += "".equals(colVal) ? null : "'" + colVal + "'" + ",";
							reserveAllColsAndVals += colName + "~A~" + colVal + ",";
						}else if ("number".equals(colType)) {
							columnValues += "".equals(colVal) ? null :  colVal + ",";
							reserveAllColsAndVals += colName + "~A~" + colVal + ",";
						}else {
							columnValues += "".equals(colVal) ? null :  "'" + colVal + "'" + ",";
							reserveAllColsAndVals += colName + "~A~" + colVal + ",";
						}

//					} 
					
//					if(colName.contains("Custom_")) {
//						long columnId = Long.parseLong(colName.split("_")[1].toString());
//						String commonVal = colName.split("_")[2];
//						long tableId = Long.parseLong(colName.split("_")[4].toString());
//						long destinationField = Long.parseLong(colName.split("_")[3].toString());
//						String destinationFieldVal = colVal;
//						tblName = iAddedTableRepository.findTableNameById(tableId);
//						String commonValCol = cfgColumnConfigRepository.findColNameByColumnId(columnId);
//						String destinationFieldColName = cfgColumnConfigRepository.findColNameByColumnId(destinationField);
//						String primaryKey = getPrimaryKey(tblName);
//						String remainingCols = "";
//						String remainingColVals = "";
//						String primaryCol = "";
//						String primaryColVal = "";
//				
//
//						String getAllMandatoryColumns = "SELECT STRING_AGG(COLUMN_NAME, ',') WITHIN GROUP(ORDER BY 1)\r\n"
//								+ "  FROM information_schema.columns\r\n" + " WHERE TABLE_NAME = '" + tblName.split("\\.")[1]
//								+ "'\r\n" + "   AND OWNER = '" + tblName.split("\\.")[0] + "'\r\n"
//								+ "   AND NULLABLE <> 'Y'\r\n"
//								+ "   AND COLUMN_NAME NOT IN ('CREATION_DATE', 'CREATED_BY', 'UPDATE_DATE', 'UPDATED_BY')";
//
//						String getAllMandatoryColumnsRes = executeNativeSelectQuery(getAllMandatoryColumns);
//
//						if (!primaryKey.equals("-1")) {
//							primaryCol = GetSequenceWithStaticTable(tblName).split(":")[0];
//							primaryColVal = GetSequenceWithStaticTable(tblName).split(":")[1];
//						}
//
//						reserveAllColsAndVals = reserveAllColsAndVals.indexOf(",") == 0 ? reserveAllColsAndVals.substring(1) : reserveAllColsAndVals;
//						
//						String[] t1 = getAllMandatoryColumnsRes.split(",");
//						String[] t2 = reserveAllColsAndVals.split(",");
//						
//						
//						for (Map.Entry<String, String> entry : generalInfo.entrySet()) {
//							String key = entry.getKey(); // Get the key
//							String value = entry.getValue(); // Get the value
//							
//							for(int gg = 0; gg < t1.length; gg++) {
//								if(t1[gg].equals(key)) {
//									remainingCols +=key + ",";
//									remainingColVals +=value+",";
//								}
//							}
//							
//						}
//
//						remainingCols = remainingCols.indexOf(",") == 0 ? remainingCols.substring(1) : remainingCols;
//						remainingColVals = remainingColVals.indexOf(",") == 0 ? remainingColVals.substring(1) : remainingColVals;
//
//						String q = "INSERT INTO " + tblName + " (" + remainingCols + commonValCol
//								+ ",CREATION_DATE, CREATED_BY," + primaryCol + "," + destinationFieldColName + ") "
//								+ "VALUES" + " (" + remainingColVals + commonVal + ",CURRENT_DATE, -7," + primaryColVal
//								+ ",'" + destinationFieldVal + "') ";
//						
//						q=q.replace(",,", ","); 
//						executeNativeCUDQuery(q);
//					}
					
				}
				
				columnNames = columnNames.indexOf(",") == 0 ? columnNames.substring(1) : columnNames;
				columnValues = columnValues.replace(",)", ")");

				query = "INSERT INTO " + tableName + "(" + columnNames + ")" + " VALUES " + "(" + columnValues + ")";
				query = query.replace(",)", ")").replace("(,", "(");
			

				//system.out.println(" 1111111111 >>>>>>>>>>>>>>>>>>>>");
				if (foundMandatoryEmptyForeignColumns == 0 && (hasEnoughColumns > 5 || hasEnoughColumns == 5)) {
					executeNativeCUDQuery(query);
				}
				//system.out.println(" 22222222222 >>>>>>>>>>>>>>>>>>>>");

				if (isFile == true && ResultGetPrimaryCol != "") {
					callProcedurev21Files(v21FilesDto);
				}else if (isSignature == true && ResultGetPrimaryCol != "") {
					callProcedurev21Signature(v21SignatureDto);
				}
				
			}

			resp.setCode("100");
			resp.setDescription("Success");
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "";

			resp.setCode("102");
			resp.setDescription("Something went wrong >>> " + errorMessage);
		}

		return resp;

	}
	public static int countOccurrences(String input, char target) {
		int count = 0;
		// Iterate through each character in the string
		for (int i = 0; i < input.length(); i++) {
			// Check if the current character matches the target character
			if (input.charAt(i) == target) {
				count++;
			}
		}
		return count;
	}

	public void executeNativeCUDQuery(String query) {
		// For create,update,delete queries
		if (!query.equals("")) {
			try {
				entityManagerR.createNativeQuery(query).executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String executeNativeSelectQuery(String query) {
		// For select queries
		String result = "";
		if (!query.equals("")) {
			try {
				result = (String) entityManagerR.createNativeQuery(query).getSingleResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String getDynamicGridHeaders(List<DynamicFormGet> dynamicFormGet) {
		String json = "[";
		json += "{\"headerName\" : \"\", \"field\" : \"\", \"checkboxSelection\" : true, \"maxWidth\" : \"50\", \"headerCheckboxSelection\" : true},";
		for (int i = 0; i < dynamicFormGet.size(); i++) {
			String colNames = "";
			String isLink = "";
			@SuppressWarnings("unused")
			String isCombo = "";
			String linkedObjectId = "";
			for (int j = 0; j < dynamicFormGet.get(i).getDynamicTable().size(); j++) {
				for (int k = 0; k < dynamicFormGet.get(i).getDynamicTable().get(j).getColumns().size(); k++) {
					colNames = dynamicFormGet.get(i).getDynamicTable().get(j).getColumns().get(k).getColName();
					isLink = dynamicFormGet.get(i).getDynamicTable().get(j).getColumns().get(k).getIsLink();
					isLink = isLink == null ? "0" : isLink;
					linkedObjectId = dynamicFormGet.get(i).getDynamicTable().get(j).getColumns().get(k)
							.getLinkedObjectId();
					if (isLink.equals("1")) {
						if (j == dynamicFormGet.get(i).getDynamicTable().get(j).getColumns().size() - 1) {
							json += "{" + "\"headerName\" : \"" + colNames + "\", " + "\"field\" : \"" + colNames
									+ "\", " + "\"isLink\" : true, "
									+ "\"link\" : \"/dsp/augmentedConfig/form/update/:objectId/-1/previewForm/\","
									+ "\"linkValue\" : \"" + linkedObjectId + "\"," + "\"linkType\" : \"linkPopUp\","
									+ "\"linkParameters\" : \"objectId\"" + "}";

						} else {
							json += "{" + "\"headerName\" : \"" + colNames + "\", " + "\"field\" : \"" + colNames
									+ "\", " + "\"isLink\" : true, "
									+ "\"link\" : \"/dsp/augmentedConfig/form/update/:objectId/-1/previewForm/\","
									+ "\"linkValue\" : \"" + linkedObjectId + "\"," + "\"linkType\" : \"linkPopUp\","
									+ "\"linkParameters\" : \"objectId\"" + "},";

						}
					} else {
						json += "{\"headerName\" : \"" + colNames + "\", \"field\" : \"" + colNames + "\"},";
					}
				}
			}
			if (i != dynamicFormGet.size() - 1) {
				json += ",";
			}
		}
		json += "]";
		json = json.replace("},]", "}]");
		return json;
	}

	@Override
	public CustomResponse getColumnId(String tableName) {
		CustomResponse resp = CustomResponse.builder().build();
		try {
			String input = tableName;
			String owner = "";
			String name = "";
			String[] parts = input.split("\\,");

			for (String element : parts) {
				@SuppressWarnings("unused")
				String[] parts2 = element.split("\\.");
				owner += "," + "'" + element.split("\\.")[0] + "'";
				name += "," + "'" + element.split("\\.")[1] + "'";
			}
			owner = owner.substring(1);
			name = name.substring(1);

			String result1 = "SELECT cols.column_name\r\n" + "  FROM information_schema.table_constraints cons\r\n"
					+ "  JOIN INFORMATION_SCHEMA.constraint_column_usage cols\r\n" + "    ON cons.TABLE_SCHEMA = cols.TABLE_SCHEMA\r\n"
					+ "   AND cons.constraint_name = cols.constraint_name\r\n" + " WHERE cons.constraint_type = 'PRIMARY KEY'\r\n"
					+ "   and cols.TABLE_SCHEMA in (" + owner + ")\r\n" + "   AND cols.table_name in (" + name + " )";
			@SuppressWarnings("unchecked")
			List<String> firstValue = entityManagerR.createNativeQuery(result1).getResultList();
			String value = "";

			for (int i = 0; i < firstValue.size(); i++) {
				if (i == 0) {
					value = "'" + firstValue.get(i) + "'";
				}
				value = value + "," + "'" + firstValue.get(i) + "'";

			}
			String result2 = " SELECT cols.column_name " + "	 FROM information_schema.table_constraints cons"
					+ "          JOIN INFORMATION_SCHEMA.constraint_column_usage cols" + " ON cons.TABLE_SCHEMA = cols.TABLE_SCHEMA"
					+ "           AND cons.constraint_name = cols.constraint_name"
					+ "         WHERE cons.constraint_type = 'PRIMARY KEY'" + "			  and cols.TABLE_SCHEMA in (" + owner + ")"
					+ "           AND cols.table_name in (" + name + ")"
					+ " 	      and NOT EXISTS (  SELECT col.table_name FROM information_schema.table_constraints con"
					+ "          JOIN INFORMATION_SCHEMA.constraint_column_usage col" + " ON con.TABLE_SCHEMA = col.TABLE_SCHEMA"
					+ "           AND con.constraint_name = col.constraint_name"
					+ "         WHERE con.constraint_type = 'FOREIGN KEY'" + "           "
					+ "			  and col.column_name in (" + value + ")" + "			  and col.TABLE_SCHEMA in (" + owner
					+ ")" + "           AND col.table_name in (" + name + ") "
					+ "	and col.TABLE_NAME = cols.TABLE_NAME" + ")";

			String result = entityManagerR.createNativeQuery(result2).getSingleResult().toString();
			resp.setCode("100");
			resp.setDescription(result);
		} catch (Exception e) {
			resp.setCode("102");
			resp.setDescription("Fail - " + e.getMessage());
		}
		return resp;
	}

	@Override
	public List<ObjectNode> getDynamicGridData(List<DynamicFormGet> dynamicFormGet) {
		String query = buildDynamicQuery(dynamicFormGet.get(0), "grid");
		List<ObjectNode> result = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, query);
		return result;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public String buildDynamicQuery(DynamicFormGet inputObject, String queryType) {

		//system.out.println("JP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +inputObject );
		@SuppressWarnings("unused")
		String whereCondition2 = "";

		String returnValue = "";
		byte[] resultFileData;
		byte[] resultSignatureData;
		String primaryVal = "";
		@SuppressWarnings("unused")
		String primaryVal2 = "";
		String primaryColumn = "";
		String PrimaryValV21File = "";
		@SuppressWarnings("unused")
		boolean isFile = false;
		@SuppressWarnings("unused")
		boolean isSignature = false;
		@SuppressWarnings({ "unused"})
		String primaryValSignature = "";
		@SuppressWarnings("unused")
		String PrimaryValV21Signature ="";
		Map<String, String> DocumentKeys = new HashMap<>();
		
		Map<String, String> ConditionValue = new HashMap<>();
//		JSONArray selectedR;
		
		System.out.println(" getSelectedRowId ----------------"+inputObject.getSelectedRowId());
		JSONArray selectedR = new JSONArray(new String(inputObject.getSelectedRowId()));
		if (selectedR.length() > 0) {
			for (int a = 0; a < selectedR.length(); a++) {
				String colNa = selectedR.getJSONObject(a).getString("colname").toUpperCase().trim();
				String colVal = selectedR.getJSONObject(a).getString("colvalue");
				ConditionValue.put(colNa, colVal);
			}

		}

		if (queryType.equals("grid")) {
			StringBuilder query = new StringBuilder("SELECT ");

			// Create a map to store the table alias names
			Map<String, String> tableAliases = new HashMap<>();

			// Create a map to store the primaryKeys names
			Map<String, String> primaryKeys = new HashMap<>();

			// Create a set to store unique columns
			Set<String> uniqueColumns = new HashSet<>();

			// Create a set to store unique columns
			@SuppressWarnings("unused")
			Set<String> tablesColumns = new HashSet<>();

			// Create a list to store the JOIN conditions
			@SuppressWarnings("unused")
			List<String> joinConditions = new ArrayList<>();

			int aliasCounter = 1;
			String whereCondition = "";
			String MainIdCondition = "";

			for (DynamicFormDto dynamicTable : inputObject.getDynamicTable()) {
				String tableName = dynamicTable.getTableName();
				String alias = "T" + aliasCounter;
				aliasCounter++;
				tableAliases.put(tableName, alias);
				if (!getPrimaryKey(tableName).equals("-1")) {

					primaryKeys.put(tableName, getPrimaryKey(tableName));
				}
			}
			boolean columnHere = false;

			for (DynamicFormDto dynamicTable : inputObject.getDynamicTable()) {
				// Build the SELECT clause for this table
				for (DynamicFormColsDto column : dynamicTable.getColumns()) {
					String columnName = "";

					if (!columnHere) {
						columnName = tableAliases.get(dynamicTable.getTableName()) + "." + column.getColName();
						if (uniqueColumns.add(column.getColName())) {
							query.append(columnName);
						}
						columnHere = true;
					} else {
						columnName = tableAliases.get(dynamicTable.getTableName()) + "." + column.getColName();
						if (uniqueColumns.add(column.getColName())) {
							query.append(" , ").append(columnName);
						}
					}

					for (String tableName1 : tableAliases.keySet()) {
						String mainId = inputObject.getSelectedRowId().split("=")[0].replaceAll("\\s", "")
								.toUpperCase();
						if (inputObject.getPrimaryColumn().equals(primaryKeys.get(tableName1))) {
							if (!inputObject.getPrimaryColumn().equals("ROW_ID")) {
								MainIdCondition = " AND " + tableAliases.get(tableName1) + "."
										+ inputObject.getPrimaryColumn() + "=" + inputObject.getSelectedRowId();
							}
						} else {
							if (mainId.equals(primaryKeys.get(tableName1))) {
								MainIdCondition = " AND " + tableAliases.get(tableName1) + "."
										+ inputObject.getSelectedRowId();
							}
						}

						if (column.getColName().equals(primaryKeys.get(tableName1))
								&& !dynamicTable.getTableName().equals(tableName1)) {
							whereCondition += " AND " + tableAliases.get(dynamicTable.getTableName()) + "."
									+ column.getColName() + " = " + tableAliases.get(tableName1) + "."
									+ primaryKeys.get(tableName1) + " ";
						}
					}
				}
			}

			//system.out.println("query >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +query );

			query.append(" FROM ");
			boolean tableHere = false;
			for (String tableName : tableAliases.keySet()) {
				if (!tableHere) {
					query.append(tableName).append(" " + tableAliases.get(tableName));
					tableHere = true;
				} else {
					query.append("," + tableName).append(" " + tableAliases.get(tableName));
				}
			}
			String myCondition = "";

			if (inputObject.getWhereConditions() != null) {
				for (int ii = 0; ii < inputObject.getWhereConditions().size(); ii++) {
					String myColumnName = inputObject.getWhereConditions().get(ii).getColumnName();
					String myTableName = inputObject.getWhereConditions().get(ii).getTableName();
					myCondition += " AND " + tableAliases.get(myTableName) + "." + myColumnName + " "
							+ inputObject.getWhereConditions().get(ii).getCondition() + " "
							+ inputObject.getWhereConditions().get(ii).getValue();
				}
			}
			
			
			query.append(" WHERE 1=1 " + whereCondition + " " + MainIdCondition + " " + myCondition);
			returnValue = query.toString();
		}
		//system.out.println("returnValue >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +returnValue );

		if (queryType.equals("form")) {
			JSONArray selectedRowIds = new JSONArray(new String(inputObject.getSelectedRowId()));
			String jsonRes = "";
			for (int i = 0; i < inputObject.getDynamicTable().size(); i++) {
				PrimaryValV21File="";
				primaryVal = "";
				int checkIfCustomFieldsExists = 0;
				@SuppressWarnings("unused")
				int checkIfNeededAsDualSelect = 0;
				
				String reservedCommon = "";
				String reservedCommonValues = "";
				String whereCondition = "";
				int includeInJson = 0;

				String customFields = "";
				String tableName = inputObject.getDynamicTable().get(i).getTableName();
				@SuppressWarnings("unused")
				String customFieldsubQueries = "";
				String columnsAr = "";
				String columnaAr2 = "";
				String resultGetPrimaryCol = "";
				primaryVal ="";
				PrimaryValV21File="";

				
				String MyValueIs="SELECT count(1)  FROM information_schema.table_constraints cons "
						+ " JOIN INFORMATION_SCHEMA.constraint_column_usage cols ON cons.constraint_name = cols.constraint_name "
						+ " WHERE cons.TABLE_SCHEMA ='" + tableName.split("\\.")[0] + "' AND cons.table_name = '"
						+ tableName.split("\\.")[1] + "' AND cons.constraint_type = 'PRIMARY KEY'";
				
				resultGetPrimaryCol =String.valueOf(entityManagerR.createNativeQuery(MyValueIs).getSingleResult());
				String getPrimaryCol = "SELECT STRING_AGG(DISTINCT cols.column_name, ',' ORDER BY cols.column_name)\r\n" + //
										" " + " FROM information_schema.table_constraints cons "
							+ " JOIN INFORMATION_SCHEMA.constraint_column_usage cols ON cons.constraint_name = cols.constraint_name "
							+ " WHERE cons.TABLE_SCHEMA ='" + tableName.split("\\.")[0] + "' AND cons.table_name = '"
							+ tableName.split("\\.")[1] + "' AND cons.constraint_type = 'PRIMARY KEY'";
				if(Integer.parseInt(resultGetPrimaryCol) != 0) {
				resultGetPrimaryCol = executeNativeSelectQuery(getPrimaryCol);
				

				//system.out.println("resultGetPrimaryCol >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +resultGetPrimaryCol );

				}
				for (int j = 0; j < inputObject.getDynamicTable().get(i).getColumns().size(); j++) {
					
					
					String colName = inputObject.getDynamicTable().get(i).getColumns().get(j).getColName();
					String colType = inputObject.getDynamicTable().get(i).getColumns().get(j).getColType();	
					if (("file".equals(colType))&& (!colName.equals("CREATION_DATE") && !colName.equals("CREATED_BY")
							&& !colName.equals("UPDATE_DATE") && !colName.equals("UPDATED_BY")
							&& colName.indexOf("Custom_") == -1)) {
						
						isFile = true;
						columnaAr2 += colName + "~C~" + colType + ",";
						columnsAr += "'1' as " + colName + " " + ",";
						JSONArray selectedRowIdss = new JSONArray(new String(inputObject.getSelectedRowId()));
						//system.out.println("selectedRowIdss >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +selectedRowIdss );

						if (selectedRowIdss.length() > 0) {
							for (int a = 0; a < selectedRowIdss.length(); a++) {
								String colNa = selectedRowIdss.getJSONObject(a).getString("colname").toUpperCase().trim();
								String colVal = selectedRowIdss.getJSONObject(a).getString("colvalue");
								DocumentKeys.put(colNa, colVal);
							}

						}

						for (Map.Entry<String, String> entry : DocumentKeys.entrySet()) {
							String key = entry.getKey(); // Get the key
							String value = entry.getValue(); // Get the value
							if (resultGetPrimaryCol.indexOf(key) != -1) {
								if (PrimaryValV21File == "") {
									PrimaryValV21File += key;
									primaryVal += value;
								} else {
									PrimaryValV21File += "," + key;
									primaryVal += "," + value;

								}
								whereCondition2 += " AND " + key + " = " + value;
							}
						}
//						whereCondition2 = whereCondition2.substring(0, whereCondition2.lastIndexOf("AND"));

					}
					///////////////////////////////////////////jp//////////////////////////////////////
					else if(("signature".equals(colType))&& (!colName.equals("CREATION_DATE") && !colName.equals("CREATED_BY")
							&& !colName.equals("UPDATE_DATE") && !colName.equals("UPDATED_BY")
							&& colName.indexOf("Custom_") == -1)) {
						
						isSignature = true;
						columnaAr2 += colName + "~C~" + colType + ",";
						columnsAr += "'1' as " + colName + " " + ",";
						JSONArray selectedRowIdss = new JSONArray(new String(inputObject.getSelectedRowId()));

						if (selectedRowIdss.length() > 0) {
							for (int a = 0; a < selectedRowIdss.length(); a++) {
								String colNa = selectedRowIdss.getJSONObject(a).getString("colname").toUpperCase()
										.trim();
								String colVal = selectedRowIdss.getJSONObject(a).getString("colvalue");
								DocumentKeys.put(colNa, colVal);
							}

						}		
					} 

					//////////////////////////////////////////////////////////////////////////////////////////////////
					else if (("text".equals(colType) ||"textarea".equals(colType) || "hidden".equals(colType) || "combo".equals(colType) || "time".equals(colType)
							|| "date".equals(colType) || "number".equals(colType) || "date time".equals(colType) || "phone number".equals(colType) || "e-mail".equals(colType) || "lookup".equals(colType) || "checkbox".equals(colType))
							&& (!colName.equals("CREATION_DATE") && !colName.equals("CREATED_BY")
									&& !colName.equals("UPDATE_DATE") && !colName.equals("UPDATED_BY")
									&& colName.indexOf("Custom_") == -1)) {

						
						
						columnaAr2 += colName + "~C~" + colType + ",";
						columnsAr += colName + ",";
						
					} 
					

				}

				columnsAr = columnsAr.endsWith(",") ? columnsAr.substring(0, columnsAr.length() - 1) : columnsAr;
				columnaAr2 = columnaAr2.endsWith(",") ? columnaAr2.substring(0, columnaAr2.length() - 1) : columnsAr;
				columnsAr = removeDuplicates(columnsAr, ",", ",");
				String colVal = "";
				@SuppressWarnings("unused")
				String colType = "";
				//system.out.println("columnsAr >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +columnsAr );

				String [] ValueTech = columnsAr.split(",");
				//system.out.println("selectedRowIds >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +selectedRowIds );

				for (int x = 0; x < selectedRowIds.length(); x++) {
					colType = selectedRowIds.getJSONObject(x).getString("type");
					String colName = selectedRowIds.getJSONObject(x).getString("colname").toUpperCase().trim();
					colVal = selectedRowIds.getJSONObject(x).getString("colvalue");
					String whereParam = "";
					if (colVal instanceof String) {
						whereParam = colName + " = '" + colVal+"'";
					}
					else
					{
					 	whereParam = colName + " = " + colVal+"";

					}
					//system.out.println("colVAl >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +colType );
					//system.out.println("  colName >>>>>>>>>>>>>"+colName);
					for(String row : ValueTech) {
						if (resultGetPrimaryCol.indexOf(colName) != -1 || row.equals(colName) ) {
							
							includeInJson = 1;
							whereCondition = whereCondition + whereParam + ",";
						} 
						
//						else if (resultGetPrimaryCol.indexOf(colName) != -1 && row.equals(colName) ) {
//						if(PrimaryValV21File == "") {
//							PrimaryValV21File += colName;
//							   primaryVal +=colVal;
//						}else {
//							PrimaryValV21File +=","+colName;
//							   primaryVal +=","+colVal;
//						}
//						}

					}
					
				
				}
				//system.out.println("whereCondition >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " +whereCondition );


//				whereCondition = !whereCondition.equals("") ? " AND " + whereCondition : "";
//				whereCondition = whereCondition.endsWith(",") ? whereCondition.substring(0, whereCondition.length() - 1) : whereCondition.replace(",", " AND ");
//				if (columnsAr.indexOf("Custom_") != -1) {
//					String whereConditionNeww = whereCondition.replace(" AND ", ",");
//					whereConditionNeww = whereConditionNeww.indexOf(",") == 0 ? whereConditionNeww.substring(1) : whereConditionNeww;
//					String[] whereConditionAr = whereConditionNeww.split(",");
//					String whereConditionNew = "";
//					for(int ih = 0; ih < whereConditionAr.length; ih ++) {
//						if(resultGetPrimaryCol.indexOf(",") != -1) {
//							String[] resultGetPrimaryColAr = resultGetPrimaryCol.split(",");
//							for(int x = 0; x < resultGetPrimaryColAr.length; x ++) {
//								if(whereConditionAr[ih].indexOf(resultGetPrimaryColAr[x]) == -1 && whereConditionNew.indexOf(whereConditionAr[ih]) == -1) {
//									whereConditionNew += whereConditionAr[ih] + ",";
//								}
//							}
//						} else {
//							if(whereConditionAr[ih].indexOf(resultGetPrimaryCol) == -1 && whereConditionNew.indexOf(whereConditionAr[ih]) == -1) {
//								whereConditionNew += whereConditionAr[ih] + ",";
//							}
//						}
//					}
//					whereConditionNew = whereConditionNew.endsWith(",") ? whereConditionNew.substring(0, whereConditionNew.length() - 1) : whereConditionNew;
//					whereConditionNew = whereConditionNew.replace(",", " AND ");
//					whereConditionNew = !whereConditionNew.equals("") ? " AND " + whereConditionNew : ""; 
//					
//					
//					if(resultGetPrimaryCol.indexOf(",") != -1) {
//						String[] resultGetPrimaryColAr = resultGetPrimaryCol.split(",");
//						for(int y = 0; y < resultGetPrimaryColAr.length; y ++) {
//							if(whereConditionNew.indexOf(resultGetPrimaryColAr[y]) == -1) {
//								checkIfNeededAsDualSelect = 1;
//								customFieldsubQueries = customFieldsubQueries.replace("#CUSTOM_WHERE#", whereConditionNew);	
//							}
//						}
//					} else {
//						if(whereConditionNew.indexOf(resultGetPrimaryCol) == -1) {
//							checkIfNeededAsDualSelect = 1;
//							customFieldsubQueries = customFieldsubQueries.replace("#CUSTOM_WHERE#", whereConditionNew);	
//						}
//					}
//					columnsAr = columnsAr.replace("#CUSTOM_WHERE#", whereConditionNew);
//				}
				whereCondition = whereCondition.replace(",", " AND ");
				String removedDuplicates1 = 	removeDuplicates(whereCondition, " AND ", " AND ");
				resultFileData = v21fileRepo.getFileData(colVal, tableName);
				////////////////////////////////jp//////////////////////
				resultSignatureData = v21SignatureRepo.getFileData(colVal, tableName);
				////////////////////////////////////////////////////////////////////////
				
				String query = "SELECT " + columnsAr + " FROM " + tableName + " WHERE 1 = 1  " + removedDuplicates1;
				
				// If custom fields are available then tune the query results and check if they are filled else execute a select from dual query
				if(checkIfCustomFieldsExists == 1) {
					query = query + " AND " + reservedCommon + " NOT IN ("+reservedCommonValues+")";

					query = query.replace(",)", ")");
					
					@SuppressWarnings("unused")
					String query_1 = "SELECT cast(COUNT(1) as text) FROM " + tableName + " WHERE 1 = 1 " + whereCondition;
//					String checkIfCustomFieldQueryHasValues = executeNativeSelectQuery(query_1);
					
//					if(checkIfCustomFieldQueryHasValues.equals("0")) {
//						customFieldsubQueries = customFieldsubQueries.endsWith(",") ? customFieldsubQueries.substring(0, customFieldsubQueries.length() - 1) : customFieldsubQueries;
//						query = "SELECT " + customFieldsubQueries + " FROM DUAL";
//						
//					}
				}
				//system.out.println(" includeInJson ---------"+includeInJson);
				if (includeInJson == 1) {
					@SuppressWarnings("rawtypes")
					List result = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, query.toString());

				
//system.out.println("  result ----------"+result);

					if (result.size() > 0) {
						String resString = result.get(0).toString();
						resString = resString.replace("{", "").replace("}", "");
						resString = resString.replace("\",\"", "\"~A~\"");

						String[] res = resString.split("~A~");
						for (int x = 0; x < res.length; x++) {
							String[] tt = columnaAr2.split(",");

							//system.out.println("tt ---------"+tt);

							for (int xx = 0; xx < tt.length; xx++) {
								String r = "\"" + tt[xx].split("~")[0] + "\"";
								String y = res[x].split(":")[0];
								//system.out.println("y --------------- "+y);
								//system.out.println("r --------------- "+r);

								if (r.toUpperCase().equals(y.toUpperCase())) {
									String[] customFieldsAr = customFields.split(",");

									//system.out.println("json customFieldsAr "+customFieldsAr);


									for (int j = 0; j < customFieldsAr.length; j++) {
										if (!customFieldsAr[j].toString().equals("")) {
											@SuppressWarnings("unused")
											String colNamee = "\"" + customFieldsAr[j].toString().split("~E~")[1] + "\"";
											String customColName = "\"" + customFieldsAr[j].toString().split("~E~")[0] + "\"";
											if (res[x].split(":")[0].toString().equals(customColName)) {
												jsonRes += "{";
												jsonRes += "\"colName\"" + ":" + customColName + "," + "\"colType\""
														+ ":" + "\"" + tt[xx].split("~C~")[1] + "\"" + ","
														+ "\"colValue\"" + " : " + "\"" + res[x].split(":")[1] + "\"";
												jsonRes += "},";
											} else {
												jsonRes += "{";
												jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
														+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
														+ "," + "\"colValue\"" + " : " + "\"" + res[x].split(":")[1]
														+ "\"";
												jsonRes += "},";
											}

											//system.out.println("json resp111 "+jsonRes);


										} else {
											if ("file".equals(tt[xx].split("~C~")[1])) {
//												String removedDuplicates = removeDuplicates(resultGetPrimaryCol);
												String removedDuplicates = 	removeDuplicates(resultGetPrimaryCol, ",", ",");
												
												PrimaryValV21File = 	removeDuplicates(PrimaryValV21File, ",", ",");
												primaryVal = 	removeDuplicates(primaryVal, ",", ",");
												if(removedDuplicates.indexOf(",") != -1) {
													String[] resultGetPrimaryColAr = removedDuplicates.split(",");
													for(int n = 0; n < resultGetPrimaryColAr.length; n ++) {
														if (res[x].split(":")[0].equals(resultGetPrimaryColAr[n])) {
															if (primaryColumn == "") {
																primaryColumn += res[x].split(":")[0];
															} else {
																primaryColumn += ", " + res[x].split(":")[0];
															}
														}
													}
												} else {
													if (res[x].split(":")[0].equals(resultGetPrimaryCol)) {
														if (primaryColumn == "") {
															primaryColumn += res[x].split(":")[0];
														} else {
															primaryColumn += ", " + res[x].split(":")[0];
														}
													}
												}
												String rearrangedPrimaryVal="";
												if(primaryVal.indexOf(",") != -1) {
												String[] partsPrimaryVal = primaryVal.split(",");
										         rearrangedPrimaryVal = partsPrimaryVal[0] + " ," + partsPrimaryVal[1];
												}else {
													rearrangedPrimaryVal = primaryVal;
												}
												resultFileData = v21fileRepo.getFileData(rearrangedPrimaryVal, tableName.split("\\.")[1]);
												@SuppressWarnings("unused")
												String yy = res[x].split(":", 2)[1].replace("\"", "");
												if (resultFileData != null && !resultFileData.equals("")) {
													String resultFileDataString = new String(resultFileData);
													// String resultFileDataString="";
													jsonRes += "{";
													jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
															+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
															+ "," + "\"colValue\"" + " : " + "\"" + resultFileDataString
															+ "\"";
													jsonRes += "},";
												}
											}else if ("time".equals(tt[xx].split("~C~")[1]) ) {

											       String[] parts = res[x].split("\":\"")[1].split(" ");

											       // Split the time part by colon
											       String[] timeParts = parts[1].split(":");

											       // Extract the hour and minute parts
											       String hour = timeParts[0];
											       String minute = timeParts[1];

											       // Concatenate to get the time in HH:mm format
											       String time = hour + ":" + minute;



											jsonRes += "{";
											jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
											+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
											+ "," + "\"colValue\"" + " : " + "\"" + '"'+time
											+ "\"";
											jsonRes += "},";


											}else if ("date time".equals(tt[xx].split("~C~")[1]) ) {

										        String[] parts = res[x].split("\\s+");
										        String datePart = parts[0];

										        // Split the time part by colon and take only the first two elements
										        String[] timeParts = parts[1].split(":");
										        String formattedTime = timeParts[0] + ":" + timeParts[1];

										        // Combine the date and time parts
										        String formattedDate = datePart + " " + formattedTime;
											       
											      
											jsonRes += "{";
											jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
											+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
											+ "," + "\"colValue\"" + " : " + "\"" + '"'+formattedDate.split("\":\"")[1]
											+ "\"";
											jsonRes += "},";


											}
											///////////////////////////////////////jp//////////////////////////////////
											else if("signature".equals(tt[xx].split("~C~")[1])) {
												
												if (res[x].split(":")[0].equals(resultGetPrimaryCol)) {
													if (primaryColumn == "") {
														primaryColumn += res[x].split(":")[0];
													} else {
														primaryColumn += ", " + res[x].split(":")[0];
													}
												}
												
												
												resultSignatureData = v21SignatureRepo.getFileData(colVal, tableName.split("\\.")[1]);
												@SuppressWarnings("unused")
												String yy = res[x].split(":", 2)[1].replace("\"", "");
												
												if (resultSignatureData != null && !resultSignatureData.equals("")) {
													String resultSignatureDataString = new String(resultSignatureData);
													// String resultFileDataString="";
													jsonRes += "{";
													jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
															+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
															+ "," + "\"colValue\"" + " : " + "\"" + resultSignatureDataString
															+ "\"";
													jsonRes += "},";

												}
												
											} 
											else if("checkbox".equals(tt[xx].split("~C~")[1])) {
												if(res[x].split(":")[1].equals("\"1\"")){
												jsonRes += "{";
												jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
														+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
														+ "," + "\"colValue\"" + " : " + "\"true\"";
												jsonRes += "},";
												}else {
													jsonRes += "{";
													jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
															+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
															+ "," + "\"colValue\"" + " : " + "\"false\"";
													jsonRes += "},";
												}
												
											} 
											else {

												jsonRes += "{";
												jsonRes += "\"colName\"" + ":" + res[x].split(":")[0] + ","
														+ "\"colType\"" + ":" + "\"" + tt[xx].split("~C~")[1] + "\""
														+ "," + "\"colValue\"" + " : " + "\"" + res[x].split(":")[1]
														+ "\"";
												jsonRes += "},";

											}
										}
									}
								}
							}
						}
						jsonRes = jsonRes.replace("\"\"", "\"");
						
					}
				}

			}
			returnValue = "[" + jsonRes + "]";
			returnValue = returnValue.replace("},]", "}]");
		        // //system.out.println(" returnValue -----"+returnValue);
		        byte[] bytesToEncode = returnValue.getBytes();
		        @SuppressWarnings("unused")
				String base64Encoded = Base64.encodeBase64String(bytesToEncode);
				//system.out.println("base64Encoded byte ---"+base64Encoded);

		}

		return returnValue;
	}

	


	public static String removeDuplicates(String txt, String splitterRegex, String separator) {
		List<String> values = new ArrayList<String>();
		String[] splitted = txt.split(splitterRegex);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < splitted.length; ++i) {
			if (!values.contains(splitted[i])) {
				values.add(splitted[i]);
				sb.append(separator);
				sb.append(splitted[i]);
			}
		}
		return sb.substring(1);

	}

	@Override
	public String getDynamicFormData(List<DynamicFormGet> dynamicFormGet) {
		System.out.println("AAAAAAAAAA>>>>>>>>>>>>>"+dynamicFormGet);
		String query = buildDynamicQuery(dynamicFormGet.get(0), "form");
		return query;
	}

	public static String removeDuplicates(String input) {
		String[] columns = input.split(",");
		Set<String> uniqueColumns = new LinkedHashSet<>(Arrays.asList(columns));
		return uniqueColumns.stream().collect(Collectors.joining(","));
	}
	
	
	
	@Override
	@Transactional
	public CustomResponse dynamicDeleteFormBuilder(List<DynamicFormGet> dynamicFormGet) {
		JSONArray selectedRowIds = new JSONArray(new String(dynamicFormGet.get(0).getSelectedRowId().toLowerCase()));
		Map<String, String> primaryKeys = new HashMap<>();
		@SuppressWarnings("unused")
		String returnStatus = "";
		try {
			if (selectedRowIds.length() > 0) {
				for (int a = 0; a < selectedRowIds.length(); a++) {
					String colName = selectedRowIds.getJSONObject(a).getString("colname").toUpperCase().trim();
					String colVal = selectedRowIds.getJSONObject(a).getString("colvalue");

					primaryKeys.put(colName, colVal);
				}
			}

//			for (int i = dynamicFormGet.get(0).getDynamicTable().size() - 1; i >= 0; i--) {
			for(int i = 0 ; i<dynamicFormGet.get(0).getDynamicTable().size() ;i++ ) {
				String queri = "";
				String whereCondition = "";
				String tableName = dynamicFormGet.get(0).getDynamicTable().get(i).getTableName();

				for (int j = 0; j < dynamicFormGet.get(0).getDynamicTable().get(i).getColumns().size(); j++) {
					String columnName = dynamicFormGet.get(0).getDynamicTable().get(i).getColumns().get(j).getColName();


					
					for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
						String key = entry.getKey(); // Get the key
						String value = entry.getValue(); // Get the value
						if (columnName.equals(key)) {
							whereCondition += key + " = " + value + " AND ";
						}
					}
				}
				queri = " DELETE FROM " + tableName + " WHERE " + whereCondition;
				queri = queri.substring(0, queri.lastIndexOf("AND"));
				entityManagerR.createNativeQuery(queri).executeUpdate(); 
			}
			returnStatus = "success";
		} catch (Exception e) {
			e.printStackTrace();
			returnStatus = "error";
		}
		return null;
	}
	
	@Override
	@Transactional
	public CustomResponse updateDynForm(List<DynamicFormGet> dynamicFormGet) {
		CustomResponse resp = CustomResponse.builder().build();
		V21FilesDto v21FilesDto = new V21FilesDto();
		V21FileModel v21filemodel = new V21FileModel();
		V21SignatureDto v21SignatureDto = new V21SignatureDto();
		V21SignatureModel v21Signaturemodel = new V21SignatureModel();	
		 Map<String, String> generalInfo = new HashMap<>();
		Map<String, String> reservedSeq  = new HashMap<>();

		String checkvalueif="";
		int checkvalueif1 = 0;
		@SuppressWarnings("unused")
		String WhereConditionValue="";
		@SuppressWarnings("unused")
		int valueOfCondition=0;
		
		boolean isFile = false;
		boolean isSignature = false;
		try {
			
			if (!dynamicFormGet.get(0).getSelectedRowId().equals("-1")) {
				JSONArray selectedRowIds = new JSONArray(new String(dynamicFormGet.get(0).getSelectedRowId()));
				for (int a = 0; a < selectedRowIds.length(); a++) {
					String colName = selectedRowIds.getJSONObject(a).getString("colname").toUpperCase().trim();
					String colVal = selectedRowIds.getJSONObject(a).getString("colvalue");
					
					checkvalueif += "," + colName;
					generalInfo.put(colName,colVal);
				}
			}
			
			
			String query = "";
			String reservedCustomCommonFields = "";
			for (int i = 0; i < dynamicFormGet.size(); i++) {
				valueOfCondition = 0;
				WhereConditionValue = "";
				
				String testSignaturyPriamryVal = "";
				String testSignaturyPriamryCol = "";
				for (int x = 0; x < dynamicFormGet.get(i).getDynamicTable().size(); x++) {
					
					String testPriamryVal = "";
					String testPriamryCol = "";					
					checkvalueif="";
					checkvalueif1=0;
					@SuppressWarnings("unused")
					int hasCustomField = 0;
					Map<String, String> selectedRow = new HashMap<>();

					String tableName = dynamicFormGet.get(i).getDynamicTable().get(x).getTableName();
					String sql_0 = "SELECT cast(COUNT(1) as text) \r\n" + "  FROM information_schema.table_constraints A, INFORMATION_SCHEMA.constraint_column_usage B \r\n"
									+ " WHERE A.TABLE_NAME = '" + tableName.split("\\.")[1] + "' \r\n"
									+ "   AND A.CONSTRAINT_TYPE = 'PRIMARY KEY' \r\n" + "   AND A.TABLE_SCHEMA = B.TABLE_SCHEMA \r\n"
									+ "   AND A.TABLE_SCHEMA = '" + tableName.split("\\.")[0] + "' \r\n"
									+ "   AND A.CONSTRAINT_NAME = B.CONSTRAINT_NAME \r\n";
					String checkIfPrimaryKeyExist = (String) entityManagerR.createNativeQuery(sql_0).getSingleResult();
					
					String primaryColumn = "";
					String primaryColValue = "";
					if (!checkIfPrimaryKeyExist.equals("0") && !dynamicFormGet.get(0).getSelectedRowId().equals("-1") ) {
						String sql_1 = "SELECT STRING_AGG(B.COLUMN_NAME,',') WITHIN GROUP (ORDER BY COLUMN_NAME) as COLUMN_NAME \r\n"
								+ "  FROM information_schema.table_constraints A, INFORMATION_SCHEMA.constraint_column_usage B \r\n" + " WHERE A.TABLE_NAME = '"
								+ tableName.split("\\.")[1] + "' \r\n" + "   AND A.CONSTRAINT_TYPE = 'PRIMARY KEY' \r\n"
								+ "   AND A.TABLE_SCHEMA = B.TABLE_SCHEMA \r\n" + "   AND A.TABLE_SCHEMA = '" + tableName.split("\\.")[0]
								+ "' \r\n" + "   AND A.CONSTRAINT_NAME = B.CONSTRAINT_NAME \r\n";
						primaryColumn = (String) entityManagerR.createNativeQuery(sql_1).getSingleResult();
					}
					String primaryColumnV21Signature=primaryColumn;
					
					JSONArray selectedRowIds = new JSONArray(new String(dynamicFormGet.get(0).getSelectedRowId()));
					String whereCondition = "";
					String columnValues = "";
					query = "";
					String setQuery = "";
					String insertQuery = "";
					@SuppressWarnings("unused")
					int isInWhereCondition = 0;
					String commonValCol = "";
					String commonVal = "";
					 testSignaturyPriamryVal = "";
					testSignaturyPriamryCol = "";
					int hasEnoughColumns = 0;
					List<String> primaryValue = new ArrayList<String>();
					String rearrangedTestPriamryVal="";
					String rearrangedtestPriamryCol="";
					Map<String, String> insertQuery1 = new HashMap<>();
					Map<String, String> updateQuery = new HashMap<>();
					
					Map<String, String> setQuery1 = new HashMap<>();
					for (int j = 0; j < dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().size(); j++) {
						String colName2 = dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().get(j).getColName();
						String colValue2 = dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().get(j).getColValue();
						@SuppressWarnings("unused")
						String colType2 = dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().get(j).getColType();
						
						
					
					if (primaryColumnV21Signature.indexOf(",") != -1) {			
						String[] tt = primaryColumnV21Signature.split(",");
						for (int i1 = 0; i1 < tt.length; i1++) {
							if (colName2.equals(tt[i1])) {
								primaryValue.add(colValue2);
								if (testPriamryVal == "") {
									testPriamryCol += colName2;
									testPriamryVal += colValue2;
								} else {
									testPriamryCol += "," + colName2;
									testPriamryVal += "," + colValue2;
								}
								if(testSignaturyPriamryVal == "") {
									testSignaturyPriamryCol += colName2;
									testSignaturyPriamryVal += colValue2;
								}else {
									testSignaturyPriamryCol += "," + colName2;
									testSignaturyPriamryVal += "," + colValue2;
								}
							}
						}
						
					} else {
						if (colName2.equals(primaryColumnV21Signature)) {
							if (testPriamryVal == "") {
								testPriamryCol += colName2;
								testPriamryVal += colValue2;
							} else {
								testPriamryCol += "," + colName2;
								testPriamryVal += "," + colValue2;
							}
							if(testSignaturyPriamryVal == "") {
								testSignaturyPriamryCol += colName2;
								testSignaturyPriamryVal += colValue2;
							}else {
								testSignaturyPriamryCol += "," + colName2;
								testSignaturyPriamryVal += "," + colValue2;
							}
						}
					}
					
					if(testPriamryCol.indexOf(",") != -1) {
						
						String[] partsTestPriamryCol = testPriamryCol.split(",");
				        rearrangedtestPriamryCol = partsTestPriamryCol[1] + " ," + partsTestPriamryCol[0];
				        if(!colValue2.isEmpty()) {
				        	
				    	String[] partsTestPriamryVal = testPriamryVal.split(",");
				    
				        rearrangedTestPriamryVal = partsTestPriamryVal[1] + " ," + partsTestPriamryVal[0];
				        }
						}else{
							rearrangedtestPriamryCol = testPriamryCol;
							rearrangedTestPriamryVal = testPriamryVal;
						}
					
					}
					
					
					
					for (int j = 0; j < dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().size(); j++) {
//						hasEnoughColumns++;
						String colName = dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().get(j).getColName();
						String colValue = dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().get(j).getColValue();
						String colType = dynamicFormGet.get(i).getDynamicTable().get(x).getColumns().get(j).getColType();
						
						for (Map.Entry<String, String> entry : reservedSeq.entrySet()) {
							
							String key = entry.getKey(); // Get the key
							String value = entry.getValue(); // Get the value	
							
							if(colName.equals(key)) {
								insertQuery1.put(key, value);
								
							}
						}
						
						if(colName.indexOf(checkvalueif) == -1) {
							checkvalueif1++;
						}					
						
						String updateCustomQuery = "";
						String insertCustomQuery = "";
						String searchCustomQuery = "";
						String paramCustomQuery = "";
						String query1 = "";
						String fullTblNameCustomQuery = "";

						// In case of custom field, then execute special treatment
						if (colName.indexOf("Custom_") != -1) {
							hasCustomField = 1;
							long columnId = Long.parseLong(colName.split("_")[1].toString());
							commonVal = colName.split("_")[2];
							long tableId = Long.parseLong(colName.split("_")[4].toString());
							long destinationField = Long.parseLong(colName.split("_")[3].toString());
							String tblName = iAddedTableRepository.findTableNameById(tableId);
							commonValCol = cfgColumnConfigRepository.findColNameByColumnId(columnId);
							String destinationFieldColName = cfgColumnConfigRepository.findColNameByColumnId(destinationField);
							reservedCustomCommonFields += commonValCol + "~" + commonVal + ",";
							fullTblNameCustomQuery = tblName;
							String getPrimaryKey = getPrimaryKey(tblName);
							String seqColName_1 = "";
							String seqColRes_1 = "";
							String remainingColVals = "";
							String remainingCols = "";
							String getAllMandatoryColumns = "SELECT STRING_AGG(COLUMN_NAME, ',') WITHIN GROUP(ORDER BY COLUMN_NAME)\r\n"
									+ "  FROM information_schema.columns\r\n" + " WHERE TABLE_NAME = '" + tblName.split("\\.")[1]
									+ "'\r\n" + "   AND TABLE_SCHEMA = '" + tblName.split("\\.")[0] + "'\r\n"
									+ "   AND IS_NULLABLE <> 'YES'\r\n"
									+ "   AND COLUMN_NAME NOT IN ('CREATION_DATE', 'CREATED_BY', 'UPDATE_DATE', 'UPDATED_BY')";

							String getAllMandatoryColumnsRes = executeNativeSelectQuery(getAllMandatoryColumns);
		
								String[] t1 = getAllMandatoryColumnsRes.split(",");
								
								
								for (Map.Entry<String, String> entry : generalInfo.entrySet()) {
														String key = entry.getKey(); // Get the key
														String value = entry.getValue(); // Get the value
														
														for(int gg = 0; gg < t1.length; gg++) {
															if(t1[gg].equals(key)) {
																remainingCols +=key + ",";
																remainingColVals +=value+",";
															}
														}
														
													}
							
								remainingCols = remainingCols.indexOf(",") == 0 ? remainingCols.substring(1) : remainingCols;
								remainingColVals = remainingColVals.indexOf(",") == 0 ? remainingColVals.substring(1) : remainingColVals;
							
							
							if (!getPrimaryKey.equals("-1")) {
								seqColName_1 = GetSequenceWithStaticTable(tblName).split(":")[0];
								seqColRes_1 = GetSequenceWithStaticTable(tblName).split(":")[1];
							}

							searchCustomQuery = "SELECT cast(COUNT(1) as text) FROM " + tblName + " WHERE 1 = 1 AND " + commonValCol + " = " + commonVal + " #CUSTOM_WHERE#";
							if (!colValue.equals("")) {
								updateCustomQuery = "update " + tblName + " set " + destinationFieldColName + " = " + colValue + ", UPDATE_DATE = CURRENT_DATE, UPDATED_BY = -7 " + " WHERE  1 = 1 AND " + commonValCol + " = " + commonVal + " #CUSTOM_WHERE#";
							}
							insertCustomQuery = "insert into " + tblName + " (" +  seqColName_1 + "," + destinationFieldColName + "," + commonValCol + ",CREATION_DATE,CREATED_BY,#CUSTOM_INSERT_COLS#) values ("  + seqColRes_1 + "," + colValue + "," + commonVal + ",CURRENT_DATE,-7,#CUSTOM_INSERT_VALUES#)";
						} else {
							if (!colName.equals("CREATION_DATE") && !colName.equals("CREATED_BY")) {
								String selectedRowIdsInfo = "";
								if (selectedRowIds.length() > 0) {
									for (int a = 0; a < selectedRowIds.length(); a++) {
										String colName1 = selectedRowIds.getJSONObject(a).getString("colname").toUpperCase().trim();
										String colVal1 = selectedRowIds.getJSONObject(a).getString("colvalue");

										selectedRow.put(colName1, colVal1);
										if (a == selectedRowIds.length() - 1) {
											selectedRowIdsInfo = selectedRowIdsInfo + colName1 + "~" + colVal1;
										} else {
											selectedRowIdsInfo = selectedRowIdsInfo + colName1 + "~" + colVal1 + ",";
										}
										
										reservedCustomCommonFields = reservedCustomCommonFields.endsWith(",") ? reservedCustomCommonFields.substring(0, reservedCustomCommonFields.length() - 1) : reservedCustomCommonFields;
										if (!"".equals(reservedCustomCommonFields)) {
											if (reservedCustomCommonFields.indexOf(",") != -1) {
												String[] reservedCustomCommonFieldsAr = reservedCustomCommonFields.split(",");
												for (int h = 0; h < reservedCustomCommonFieldsAr.length; h++) {
													if (colName.equals(reservedCustomCommonFieldsAr[h].split("~")[0])) {
														if (whereCondition.indexOf(reservedCustomCommonFieldsAr[h].split("~")[0]) == -1) {
															whereCondition = whereCondition + reservedCustomCommonFieldsAr[h].split("~")[0] + " <> " + reservedCustomCommonFieldsAr[h].split("~")[1] + ",";
														}
													}
												}
											} else {
												if (whereCondition.indexOf(reservedCustomCommonFields.split("~")[0]) == -1) {
													if (colName.equals(reservedCustomCommonFields.split("~")[0])) {
														whereCondition = whereCondition + reservedCustomCommonFields.split("~")[0] + " <> " + reservedCustomCommonFields.split("~")[1] + ",";
													}
												}
											}
											if (colName.equals(primaryColumn)) {
												for (Map.Entry<String, String> entry : selectedRow.entrySet()) {
													String key = entry.getKey(); // Get the key
													String value = entry.getValue(); // Get the value
													if (key.equals(colName) && whereCondition.indexOf(colName) == -1) {
														whereCondition = whereCondition + key + " = " + value + ",";
													}
												}
											}
										}
										if (primaryColumn.indexOf(",") != -1) {
											String[] primaryColumnAr = primaryColumn.split(",");
											for (int ki = 0; ki < primaryColumnAr.length; ki++) {
												if (colName1.equals(colName) || colName.equals(primaryColumnAr[ki])) {
													isInWhereCondition = 1;
													if (whereCondition.indexOf(colName) == -1) {
														for (Map.Entry<String, String> entry : selectedRow.entrySet()) {
															String key = entry.getKey();
															String value = entry.getValue();
															if (key.equals(colName) && whereCondition.indexOf(colName) == -1) {
																whereCondition = whereCondition + key + " = " + value + ",";
															}
														}
													}
												}
											}
										} else {
											if (colName1.equals(colName) || colName.equals(primaryColumn)) {
												isInWhereCondition = 1;
												if (whereCondition.indexOf(colName) == -1) {
													for (Map.Entry<String, String> entry : selectedRow.entrySet()) {
														String key = entry.getKey(); // Get the key
														String value = entry.getValue(); // Get the value
														if (key.equals(colName) && whereCondition.indexOf(colName) == -1) {

															// if (value instanceof String) {
															// 	whereCondition = whereCondition + key + " = '" + value + "',";
															// }
															// else
															// {
																whereCondition = whereCondition + key + " = " + value + ",";

															// }
														}
													}
												}
											}
										}
									}
								}

								if (!selectedRowIdsInfo.equals("") && !primaryColumn.equals(colName)) {
									String[] tt = selectedRowIdsInfo.split(",");
									for (int b = 0; b < tt.length; b++) {

//system.out.println("  colName---"+colName);
//system.out.println("  colValue---"+colValue);
//system.out.println("  colType---"+colType);

										if (primaryColumn.indexOf(colName) != -1 && colValue.equals("") && !primaryColValue.equals("")) {
											if (primaryColValue.indexOf(",") != -1) {
												String[] tr = primaryColValue.split(",");
												for (int ta = 0; ta < tr.length; ta++) {
													if (tr[ta].toString().split("~")[0].indexOf(colName) != -1) {
														columnValues = tr[ta].split("~")[1];
													}
												}
											} else {
												columnValues = primaryColValue.split("~")[1];
											}
										} else if (tt[b].split("~")[0].equals(colName)) {
											columnValues = tt[b].split("~")[1];
										} else {

											if ("".equals(colValue) || "undefined".equals(colValue) ) {
												columnValues = null;
											} else {
												if ("text".equals(colType) || "textarea".equals(colType)  ) {
													columnValues = (colValue == null || 
															        colValue.equals("null") || 
															        colValue.equals("undefined") || 
															        colValue.isEmpty()) ? "null" : "'" + colValue + "'";
												}
												else if ("lookup".equals(colType) && !colValue.equals("undefined") && !colValue.isEmpty()) {
													columnValues = "'" + colValue + "'";
												} 

												if ("number".equals(colType)) {

													if(colValue.isEmpty() ) {
													columnValues = null;
													}else {
														columnValues = colValue;
													}
												}

												if ("date".equals(colType)) {
													if (colValue.equals("Invalid date")) {
														columnValues = null;
													} else {
														columnValues = "TO_DATE(" + "'" + colValue + "'" + ", 'MM/dd/yyyy')";
													}
												}

												if ("date time".equals(colType)) {
													if (colValue.equals("Invalid date")) {
														columnValues = null;
													} else {
														columnValues = "TO_TIMESTAMP(" + "'" + colValue + "'" + ", 'YYYY-MM-DD HH24:MI')";
												
													}
												}
												
												if ("time".equals(colType)) {
													
														columnValues = "TO_DATE( "+"'" + colValue + "'"+" ,'HH24:MI')" ;
													
												}
												
												if ("phone number".equals(colType) || "e-mail".equals(colType)) {
													columnValues = "'"+  colValue +"'" ;
												}	
												
												if ("checkbox".equals(colType)) {
													if("true".equals(colValue)) {

														columnValues = "'1'";
													
													}else {

														columnValues = "'0'";
													}
												}
												
												if ("file".equals(colType)) {
													if(colValue.indexOf("data:") != -1) {
													String primaryColV21file = removeDuplicates(rearrangedtestPriamryCol);
													String primaryValV21file = removeDuplicates(rearrangedTestPriamryVal);
													v21FilesDto.setPrimaryCol(primaryColV21file);
													v21filemodel.setPrimaryCol(primaryColV21file);
													v21FilesDto.setPrimaryVal(primaryValV21file);
													v21filemodel.setPrimaryVal(primaryValV21file);

													v21FilesDto.setTableName(tableName);
													v21filemodel.setTableName(tableName);
													int test = v21fileRepo.test(v21FilesDto.getPrimaryCol(), v21FilesDto.getTableName(), v21FilesDto.getPrimaryVal());

													isFile = true;
													v21filemodel.setFileCol(colName);
													v21FilesDto.setFileCol(colName);
													v21filemodel.setFileData(colValue.getBytes());
													columnValues = "UTL_RAW.CAST_TO_RAW(" + "'" + 1 + "'" + ")";

													if (test == 0) {
														v21fileRepo.save(v21filemodel);
													} else {
														v21fileRepo.updateV21Files(v21filemodel.getFileData(), v21FilesDto.getPrimaryVal());
													}
													}else {
														columnValues = "UTL_RAW.CAST_TO_RAW(" + "'" + 1 + "'" + ")";
													}
												} 
												else if("signature".equals(colType)) {
													if(colValue.indexOf("data:image") != -1) {
														String primaryColV21Signature = removeDuplicates(testSignaturyPriamryCol);
														String primaryValV21Signature = removeDuplicates(testSignaturyPriamryVal);
														v21SignatureDto.setPrimaryCol(primaryColV21Signature);
														v21Signaturemodel.setPrimaryCol(primaryColV21Signature);
														v21SignatureDto.setPrimaryVal(primaryValV21Signature);
														v21Signaturemodel.setPrimaryVal(primaryValV21Signature);
														v21SignatureDto.setTableName(tableName);
														v21Signaturemodel.setTableName(tableName);
														
														int test = v21SignatureRepo.test(v21SignatureDto.getPrimaryCol(), v21SignatureDto.getTableName(), v21SignatureDto.getPrimaryVal());
														isSignature = true;
														v21Signaturemodel.setFileCol(colName);
														v21SignatureDto.setFileCol(colName);
														v21Signaturemodel.setFileData(colValue.getBytes());
														columnValues = "UTL_RAW.CAST_TO_RAW(" + "'" + 1 + "'" + ")";

														if (test == 0) {
															v21SignatureRepo.save(v21Signaturemodel);
														} else {
															v21SignatureRepo.updateV21Signature(v21Signaturemodel.getFileData(), v21SignatureDto.getPrimaryVal());
														}
													}else {
														
														columnValues = "UTL_RAW.CAST_TO_RAW(" + "'" + 1 + "'" + ")";
													}
												}
												else if ("combo".equals(colType)) {
													columnValues = colValue.split(",")[0];

												}
												else if ("lookup".equals(colType) && !colValue.equals("undefined") && !colValue.isEmpty()) {
													columnValues = "'" + colValue + "'";
												}
												
												else if (!"phone number".equals(colType) 
															&& !"date time".equals(colType) 
															&& !"date".equals(colType) 
															&& !"number".equals(colType) 
															&& !"text".equals(colType) 
															&& !"time".equals(colType) 
															&& !"file".equals(colType)
															&& !"signature".equals(colType)
															&& !"e-mail".equals(colType)
															&& !"checkbox".equals(colType)) {
													columnValues = colValue;
												}
											}
										}
									}
								}
                            
								if(!colName.equals(primaryColumn)) {
									
									setQuery += " " + colName + " = " + columnValues + ";";
									insertQuery += "#" + colName + "~" + columnValues;
									insertQuery1.put(colName, columnValues);
									
								

									
								}
								for (Map.Entry<String, String> entry : reservedSeq.entrySet()) {
									String key = entry.getKey(); // Get the key
									String value = entry.getValue(); // Get the value	
									if(colName.equals(key)) {
									insertQuery1.put(key, value);
									}
								}
								
								for (Map.Entry<String, String> entry : generalInfo.entrySet()) {
									String key = entry.getKey(); // Get the key
									String value = entry.getValue(); // Get the value
									if((colName.equals(key) && insertQuery.indexOf(key)!=-1) || primaryColumn.equals(key)) {
										insertQuery += "#" + key + "~" + value;
										insertQuery1.put(key, value);
									}
									
								}
								
							} else {
								if (setQuery.equals("")) {
									if (colName.equals("CREATION_DATE")) {
										insertQuery += "#" + colName + "~CURRENT_DATE";
										insertQuery1.put(colName, "CURRENT_DATE");
									} else {
										insertQuery += "#" + colName + "~" + colValue;
										insertQuery1.put(colName, colValue);
									}
								} else {
									if (colName.equals("CREATION_DATE")) {
										insertQuery += "#" + colName + "~CURRENT_DATE";
										insertQuery1.put(colName, "CURRENT_DATE");
									} else {
										insertQuery += "#" + colName + "~" + colValue;
										insertQuery1.put(colName, colValue);
									}
								}
							}
						}
						if (!searchCustomQuery.equals("")) {
							String whereCond = "";

							String customFieldsColumns = "";
							String customFieldsColValues = "";
							paramCustomQuery = paramCustomQuery.endsWith(",") ? paramCustomQuery.substring(0, paramCustomQuery.length() - 1) : paramCustomQuery;
							
							if (selectedRowIds.length() > 0) {
								for (int a = 0; a < selectedRowIds.length(); a++) {
									String colName1 = selectedRowIds.getJSONObject(a).getString("colname").toUpperCase().trim();
									String colVal1 = selectedRowIds.getJSONObject(a).getString("colvalue");
									
									String checkIfColumnsExists = "select cast(COUNT(1) as text) from information_schema.columns\r\n" 
																  + "where column_name = '" + colName1 + "' \r\n" 
											                      + "and table_name = '" + fullTblNameCustomQuery.split("\\.")[1] 
											                      + "'\r\n" + "and TABLE_SCHEMA = '" + fullTblNameCustomQuery.split("\\.")[0] + "'";
									
									String checkIfColumnsExistsRes = executeNativeSelectQuery(checkIfColumnsExists);
									if (!"0".equals(checkIfColumnsExistsRes)) {
										if(paramCustomQuery.indexOf(colName1) == -1)
											paramCustomQuery += colName1 + "~~" + colVal1 + ",";
									}
								}
							}
							paramCustomQuery = paramCustomQuery.endsWith(",") ? paramCustomQuery.substring(0, paramCustomQuery.length() - 1) : paramCustomQuery;
							
							if (paramCustomQuery.indexOf(",") != -1) {
								String[] mm = paramCustomQuery.split(",");
								for (int l = 0; l < mm.length; l++) {
									if(customFieldsColumns.indexOf(mm[l].split("~~")[0]) == -1) {
										customFieldsColumns += mm[l].split("~~")[0] + ",";
										customFieldsColValues += "," + mm[l].split("~~")[1] + ",";;
										whereCond += " AND " + mm[l].split("~~")[0] + " = " + mm[l].split("~~")[1];
									}
								}
							} else {
								if(customFieldsColumns.indexOf(paramCustomQuery.split("~~")[0]) == -1) {
									customFieldsColumns += paramCustomQuery.split("~~")[0] + ",";
									customFieldsColValues += paramCustomQuery.split("~~")[1] + ",";
									whereCond += " AND " + paramCustomQuery.split("~~")[0] + " = " + paramCustomQuery.split("~~")[1];
								}
							}
							whereCond = whereCond.endsWith(",") ? whereCond.substring(0, whereCond.length() - 1) : whereCond;
							
							
							if(!whereCond.equals("")) {
								String whereCondNeww = whereCond.replace(" AND ", ",");
								whereCondNeww = whereCondNeww.substring(1);
								

								String[] whereCondAr = whereCondNeww.split(",");
								String whereCondNew = "";
								for(int ih = 0; ih < whereCondAr.length; ih ++) {
									if(primaryColumn.indexOf(",") != -1) {
										String[] primaryColumnAr = primaryColumn.split(",");
										for(int iu = 0; iu < primaryColumnAr.length; iu ++) {
											if(whereCondAr[ih].indexOf(primaryColumnAr[iu]) == -1) {
												whereCondNew += whereCondAr[ih] + ",";
											}
										}
									} else {
										if(whereCondAr[ih].indexOf(primaryColumn) == -1) {
											whereCondNew += whereCondAr[ih] + ",";
										}
									}
								}
								whereCondNew = whereCondNew.endsWith(",") ? whereCondNew.substring(0, whereCondNew.length() - 1) : whereCondNew;
								whereCondNew = whereCondNew.replace(",", " AND ");
								whereCondNew = !whereCondNew.equals("") ? " AND " + whereCondNew : ""; 
				
								

								searchCustomQuery = searchCustomQuery.replace("#CUSTOM_WHERE#", whereCondNew);
								updateCustomQuery = updateCustomQuery.replace("#CUSTOM_WHERE#", whereCondNew);	
							}						
							
							if (!customFieldsColumns.equals("")) {
								customFieldsColumns = customFieldsColumns.endsWith(",") ? customFieldsColumns.substring(0, customFieldsColumns.length() - 1) : customFieldsColumns;
								customFieldsColValues = customFieldsColValues.endsWith(",") ? customFieldsColValues.substring(0, customFieldsColValues.length() - 1) : customFieldsColValues;

								insertCustomQuery = insertCustomQuery.replace("#CUSTOM_INSERT_COLS#", customFieldsColumns);
								insertCustomQuery = insertCustomQuery.replace("#CUSTOM_INSERT_VALUES#", customFieldsColValues);
							}
							
							insertCustomQuery = insertCustomQuery.replace("(,", "(");
							updateCustomQuery = updateCustomQuery.replace("(,", "(");
							searchCustomQuery = searchCustomQuery.replace("(,", "(");

							
							query1 = executeNativeSelectQuery(searchCustomQuery);
							
							if (query1.equals("0") && !insertCustomQuery.equals("")) {
								executeNativeCUDQuery(insertCustomQuery);
							} else if (!updateCustomQuery.equals("")) {
								executeNativeCUDQuery(updateCustomQuery);
							}
						}

					}
					whereCondition = whereCondition.endsWith(",") ? whereCondition.substring(0, whereCondition.length() - 1) : whereCondition;
					whereCondition = whereCondition.replace(",", " AND ");
					
					String checkIfTableEmpty = "0"; 
						
					if (!whereCondition.equals("") && whereCondition != null) {
						
						String whereCondNeww = whereCondition.replace(" AND ", ",");
						whereCondNeww = whereCondNeww.indexOf(",") == 1 ? whereCondNeww.substring(1) : whereCondNeww;
						String[] whereCondAr = whereCondNeww.split(",");
						String whereCondNew = "";
						for(int ih = 0; ih < whereCondAr.length; ih ++) {
							if(primaryColumn.indexOf(",") != -1) {
								String[] primaryColumnAr = primaryColumn.split(",");
								for(int iu = 0; iu < primaryColumnAr.length; iu ++) {
									if(whereCondAr[ih].indexOf(primaryColumnAr[iu]) == -1) {
										whereCondNew += whereCondAr[ih] + ",";
									}
								}
							} else {
								if(whereCondAr[ih].indexOf(primaryColumn) == -1) {
									whereCondNew += whereCondAr[ih] + ",";
								}
							}
						}
						whereCondNew = whereCondNew.endsWith(",") ? whereCondNew.substring(0, whereCondNew.length() - 1) : whereCondNew;
						whereCondNew = whereCondNew.replace(",", " AND ");
						whereCondNeww = whereCondNeww.replace(",", " AND ");
						whereCondNew = !whereCondNew.equals("") ? " AND " + whereCondNew : ""; 

								if(whereCondNeww.startsWith(" AND ")) {
									whereCondNeww = whereCondNeww.replaceFirst(" AND ", "");	
								}
								
						String query1 = "SELECT cast(COUNT(1) as text) FROM " + tableName + " WHERE 1 = 1 AND " + whereCondNeww;
						checkIfTableEmpty = (String) entityManagerR.createNativeQuery(query1).getSingleResult();
						
					}
					
					if (checkIfTableEmpty.equals("0")) {
						int foundMandatoryEmptyForeignColumns = 0;
						int foundMandatoryEmptyColumns = 0;

						String getPrimaryKey = getPrimaryKey(tableName);

						String seqColName = "";
						String seqColRes = "";
						
						if (!getPrimaryKey.equals("-1")) {
							seqColName = GetSequenceWithStaticTable(tableName).split(":")[0];
							seqColRes = GetSequenceWithStaticTable(tableName).split(":")[1];
							reservedSeq.put(seqColName, seqColRes);
						}
						
						String[] ii = insertQuery.split("#");
						String columns = "";
						String values = "";
						
						String columns1 = "";
						String values1 = "";
						for (int j = 0; j < ii.length; j++) {

							if(!ii[j].equals("") && ii[j] != null) {
								columns += ii[j].split("~")[0] + ",";
								if (!seqColName.equals("") && seqColName.trim().equals(ii[j].split("~")[0].trim())) {
									values += seqColRes + ",";
								} else {
									values += ii[j].split("~")[1].trim() + ",";
								}
							}
						}
						columns = columns.endsWith(",") ? columns.substring(0, columns.length() - 1) : columns;
						values = values.endsWith(",") ? values.substring(0, values.length() - 1) : values;
						
						columns = columns + "," + seqColName;
						values = values + "," + seqColRes;
						for (Map.Entry<String, String> entry : insertQuery1.entrySet()) {
							String key = entry.getKey(); // Get the key
							String value = entry.getValue(); // Get the value	
							
			if(!("null").equals(value) && !"null".equals(value) 
			&& !"'null'".equals(value) && !("'null'").equals(value) 
			&& !("NULL").equals(value) && !"NULL".equals(value)) {
							columns1+= key + " , ";
							values1+= value + " , ";
							}
						}
						columns1 += seqColName;
						values1 += seqColRes;
						hasEnoughColumns = countOccurrences(columns, ',');
						
						query = "INSERT INTO " + tableName + " (" + columns1 + ") VALUES (" + values1 + ")";
						String getAllMandatoryForeigns = "SELECT STRING_AGG(A.COLUMN_NAME, ',') WITHIN GROUP(ORDER BY COLUMN_NAME) \r\n"
								+ "  FROM INFORMATION_SCHEMA.constraint_column_usage A, information_schema.table_constraints T \r\n"
								+ " WHERE T.CONSTRAINT_NAME = A.CONSTRAINT_NAME \r\n"
								+ "   AND T.TABLE_NAME = A.TABLE_NAME \r\n" + "   AND T.TABLE_NAME = '"
								+ tableName.split("\\.")[1] + "' \r\n" + "   AND T.CONSTRAINT_TYPE = 'FOREIGN KEY' \r\n"
								+ "   AND T.TABLE_SCHEMA = A.TABLE_SCHEMA \r\n" + "   AND T.TABLE_SCHEMA = '" + tableName.split("\\.")[0]
								+ "' \r\n" + "   AND EXISTS (SELECT 1 \r\n"
								+ "          FROM information_schema.columns S \r\n"
								+ "         WHERE S.TABLE_NAME = T.TABLE_NAME \r\n"
								+ "           AND S.TABLE_SCHEMA = T.TABLE_SCHEMA \r\n"
								+ "           AND S.COLUMN_NAME = A.COLUMN_NAME \r\n"
								+ "           AND S.IS_NULLABLE <> 'YES')";

						String getAllMandatoryForeignsRes = executeNativeSelectQuery(getAllMandatoryForeigns);
						
						// Check if any foreign mandatory column is empty or null to ignore it's table
						if (!"".equals(getAllMandatoryForeignsRes) && getAllMandatoryForeignsRes != null) {
							if (getAllMandatoryForeignsRes.indexOf(",") != -1) {
								String[] getAllMandatoryForeignsAr = executeNativeSelectQuery(
										getAllMandatoryForeigns).split(",");
								for (int k = 0; k < getAllMandatoryForeignsAr.length; k++) {
									for (int kk = 0; kk < ii.length; kk++) {
										if (getAllMandatoryForeignsAr[k].equals(ii[kk].split("~")[0])
												&& ("".equals(ii[kk].split("~")[1])
														|| ii[kk].split("~")[1].equals("undefined"))) {
											foundMandatoryEmptyForeignColumns++;
										}
									}
								}
							} else {
								for (int kk = 0; kk < ii.length; kk++) {
									if (getAllMandatoryForeignsRes.equals(ii[kk].split("~")[0])
											&& ("".equals(ii[kk].split("~")[1])
													|| ii[kk].split("~")[1].equals("undefined"))) {
										foundMandatoryEmptyForeignColumns++;
									}
								}
							}
						}

						String sql = "SELECT cast(COUNT(1) as text)" + "  FROM information_schema.columns" + " WHERE TABLE_NAME = '" + tableName.split("\\.")[1] + "'" + "   AND TABLE_SCHEMA = '" + tableName.split("\\.")[0] + "'" + "   AND IS_NULLABLE <> 'YES'";

						String getAllMandatoryColumnsCount = (String)entityManagerR.createNativeQuery(sql).getSingleResult();
						if (!getAllMandatoryColumnsCount.equals("0") && checkvalueif1 != 0 ) {
							sql = "select STRING_AGG(COLUMN_NAME, ',') WITHIN GROUP(ORDER BY COLUMN_NAME)\r\n" + "  FROM information_schema.columns\r\n" + " WHERE TABLE_NAME = '" + tableName.split("\\.")[1] + "'" + "   AND TABLE_SCHEMA = '" + tableName.split("\\.")[0] + "'" + "   AND IS_NULLABLE <> 'YES'" + " AND COLUMN_DEFAULT IS NULL";
							
							String getAllMandatoryColumns = executeNativeSelectQuery(sql);
							
							// Check if any of the mandatory columns of a given table are empty then ignore it
							if (!"".equals(getAllMandatoryColumns) && getAllMandatoryColumns != null) {
								if (getAllMandatoryColumns.indexOf(",") != -1) {
									String[] getAllMandatoryColumnsAr = getAllMandatoryColumns.split(",");
									for (int k = 0; k < getAllMandatoryColumnsAr.length; k++) {
										for (int kk = 0; kk < ii.length; kk++) {
											if (getAllMandatoryColumnsAr[k].equals(ii[kk].split("~")[0]) 
													&& ("".equals(ii[kk].split("~")[1]) || ii[kk].split("~")[1].equals("undefined") || ii[kk].split("~")[1].equals("null")) 
													&& (!getAllMandatoryColumnsAr[k].equals(seqColName))) {
												foundMandatoryEmptyColumns++;
											}
										}
									}
								} else {
									for (int kk = 0; kk < ii.length; kk++) {
										if (getAllMandatoryColumns.equals(ii[kk].split("~")[0]) 
												&& ("".equals(ii[kk].split("~")[1]) || ii[kk].split("~")[1].equals("undefined") || ii[kk].split("~")[1].equals("null"))
												&& (!getAllMandatoryColumns.equals(seqColName))) {
												foundMandatoryEmptyColumns++;
											}
										}
									}
								}
							}

                    query = query.replace(",)",")");
                    query = query.replace("(,","(");
                    query = query.replace(", )",")");
                    
                    
                    
                
                    
						if (foundMandatoryEmptyForeignColumns == 0 && foundMandatoryEmptyColumns == 0 && hasEnoughColumns > 1  && checkIfTableEmpty.equals("0")) {
							executeNativeCUDQuery(query);
						}
					} else {
						String updateQuerys = "";
						setQuery = setQuery.endsWith(";") ? setQuery.substring(0, setQuery.length() - 1) : setQuery;
						
						if(setQuery.indexOf(';') !=-1 && !setQuery.isEmpty()) {
						String[] WW = setQuery.split(";");
							for(int W = 0; W <WW.length; W++) {
                        	 setQuery1.put(WW[W].split(" = ")[0], WW[W].split(" = ")[1]);
                        	 
                         }
						for (Map.Entry<String, String> entry : setQuery1.entrySet()) {
							String key = entry.getKey(); // Get the key
							String value = entry.getValue(); // Get the value	
							if(!value.equals("null") && !"null".equals(value) && !"'null'".equals(value) && !value.equals("'null'")
								&& !value.equals("NULL") && !"NULL".equals(value)) {
							
									updateQuery.put(key, value);
									
									updateQuerys += key +" = "+ value + " ,";
								
								}
								}
								
								

						} 
						if(updateQuerys.lastIndexOf(";")!=-1) {
							updateQuerys += updateQuerys.substring(0, setQuery.lastIndexOf(";"));
							updateQuerys = updateQuerys.replace(";", ",");
						}else {
							
							if(updateQuerys.lastIndexOf(";")!=-1) {
								updateQuerys = updateQuerys.substring(0, setQuery.lastIndexOf(";"));
								
								updateQuerys += setQuery.replace(";", ",");
							}
							
						}
						
						
//						}
//						else {
//							for (Map.Entry<String, String> entry : setQuery1.entrySet()) {
//								String key = entry.getKey(); // Get the key
//								String value = entry.getValue(); // Get the value	
//								
//								if(!value.equals("null") && !"null".equals(value) ) {
//									
//									setQuery = key +" = "+ value ;
//								}
//						}
//						}
						updateQuerys = updateQuerys.replace(";", ",");
						if (updateQuerys.endsWith(",")) {
							updateQuerys = updateQuerys.substring(0, updateQuerys.length() - 1);
				        }
						query = "UPDATE " + tableName + " SET " + updateQuerys + " WHERE 1 = 1 AND " + whereCondition;
						if(!setQuery.isEmpty() && !checkIfTableEmpty.equals("0") && !updateQuerys.isEmpty()) {
						executeNativeCUDQuery(query);
						}
					}
				}

				resp.setCode("100");
				resp.setDescription("Success");
			}
			if (isFile) {
				callProcedurev21Files(v21FilesDto);
			}
			if(isSignature) {
				callProcedurev21Signature(v21SignatureDto);
			}
		} catch (Exception e) {
			e.printStackTrace();

			resp.setCode("102");
			resp.setDescription("Fail - " + e.getMessage());
		}

		return resp;
	}

	
	
	
	@SuppressWarnings("finally")
	@Override
	public List<ObjectNode> getQbeId(long qbeId, long queryType, List<QbeIdDto> qbeIdDto) {
		List<ObjectNode> resultData = null;
		String whereCond = "-1";
		try {
			if (qbeId == -1) {
				//system.out.println("in if >>>>>>>>>>>>>>>>>>");
				String query = "select -1";
				@SuppressWarnings("unchecked")
				List<ObjectNode> dataList = entityManagerR.createNativeQuery(query).getResultList();

				resultData = dataList;
			} else {
				//system.out.println("in else>>>>>>>>>>>>>>>>>>");

				@SuppressWarnings("unused")
				queryDto dto = new queryDto();
				//system.out.println("qbeId>>>>>>>>>>11111111111>>>>>>>> : "+qbeId);

				byte[] encodedQuery = sqbQueryDetailsRepository.getQueryBlob(qbeId);
				String decodedQuery = new String(Base64.decodeBase64(encodedQuery));
				//system.out.println("decodedQuery>>>>>>>>>>11111111111>>>>>>>>"+decodedQuery);

				String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,
						decodedQuery.indexOf("</sql>") - 3);
				//system.out.println("query>>>>>>>>>>11111111111>>>>>>>>"+query);

				String headsString = decodedQuery.split("<LstHeads>")[1];
				headsString = headsString.split("</LstHeads>")[0];
				String[] headers = headsString.split("<EltHeads ");
				String execHeadsJsonString = "[";
				for (int i = 1; i < headers.length; i++) {
					String header = headers[i].split("dbName=\"")[1];
					header = header.split("\"")[0];
					if (header.equals("ROW_ID")) {
						execHeadsJsonString += "{\"headerName\":\"" + header + "\",\"field\":\"" + header
								+ "\", \"hide\":\"true\", \"suppressToolPanel\":\"true\"}";
					} else {
						String tt = "";			
						for (int n = 0; n < qbeIdDto.size(); n++) {
							if (qbeIdDto.get(n).getLink().size() > 0) {
								List<LinkQbeDto> x = qbeIdDto.get(n).getLink();
								for (int l = 0; l < x.size(); l++) {
									String isLink = x.get(l).getIsLink();
									String menuId = x.get(l).getMenuName();
									String colName = x.get(l).getColName();
									String colDesc = x.get(l).getColDesc();

									if(colDesc==null) {
										colDesc="null";
									}
									
									if (colName.equals(header) || colDesc.toUpperCase().replace(" ","_").equals(header) || colDesc.toUpperCase().replace(" ","").equals(header) || colDesc.toUpperCase().equals(header)) {
										if (isLink.equals("1")) {
											tt = "{\"headerName\":\"" + header + "\"," + "\"field\":\"" + header + "\","
													+ "\"isLink\":\"true\","
													+ "\"link\" : \"/dsp/augmentedConfig/form/update/:objectId/-1/previewForm/\","
													+ "\"linkValue\" : \"" + menuId + "\", "
													+ "\"linkType\" : \"linkPopUp\"" + "}";
										}
									} else {
										if (tt.equals("")) {
											tt = "{\"headerName\":\"" + header + "\",\"field\":\"" + header + "\"}";
										}
									}
								}
							}else if(qbeIdDto.get(n).getIsHidden().size() > 0) {

								List<HiddenQbeDto> x = qbeIdDto.get(n).getIsHidden();
								for (int l = 0; l < x.size(); l++) {
									String isGridHidden = x.get(l).getIsGridHidden();
									@SuppressWarnings("unused")
									String menuId = x.get(l).getMenuName();
									@SuppressWarnings("unused")
									String colName = x.get(l).getColName();
									String colDesc = x.get(l).getColDesc();

									if (colDesc.toUpperCase().equals(header)) {
										if (isGridHidden.equals("1")) {
											tt = "{\"headerName\":\"" + header + "\",\"field\":\"" + header
													+ "\", \"hide\":\"true\", \"suppressToolPanel\":\"true\"}";
										}
									} else {
										if (tt.equals("")) {
											tt = "{\"headerName\":\"" + header + "\",\"field\":\"" + header + "\"}";
										}
									}
								}
							}else if (tt.equals("")){
								tt = "{\"headerName\":\"" + header + "\",\"field\":\"" + header + "\"}";
							}
							whereCond = qbeIdDto.get(0).getWhereCond();
						}
						execHeadsJsonString += tt;
					}

					if (i != headers.length - 1) {
						execHeadsJsonString += ",";
					}
				}
				execHeadsJsonString += "]";
				String parametersString = decodedQuery.split("<pars>")[1];
				parametersString = parametersString.split("</pars>")[0];
				String[] parameters = parametersString.split("<par ");
				for (int i = 1; i < parameters.length; i++) {
					String paramType = parameters[i].split("fieldType=\"")[1];
					paramType = paramType.split("\"")[0];
					String id = parameters[i].split("id=\"")[1];
					id = id.split("\"")[0];
					String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,
							parameters[i].indexOf("]"));
					query = query.replace("#" + id + "#", "[" + paramName + "]");
				}

				if (queryType == 0) {
					for (int o = 0; o < qbeIdDto.size(); o++) {
						for (int oo = 0; oo < qbeIdDto.get(o).getParameters().size(); oo++) {
							String paramName = qbeIdDto.get(o).getParameters().get(oo).getParamName().toUpperCase();
							String paramValue = qbeIdDto.get(o).getParameters().get(oo).getParamValue();

							if (!paramName.equals("")) {
								String jsonParam = "[{\"paramName\":\"" + paramName + "\",\"paramValue\":\"" + paramValue + "\"}]";
								JSONArray parametersList = new JSONArray(new String(jsonParam));
								for (int i = 0; i < parametersList.length(); i++) {
									query = query.toUpperCase().replace("[" + parametersList.getJSONObject(i).getString("paramName").toUpperCase() + "]", parametersList.getJSONObject(i).getString("paramValue"));
								}
							}
						}
					}

					@SuppressWarnings("unchecked")
					List<ObjectNode> dataList = entityManagerR.createNativeQuery(query).getResultList();
					resultData = dataList;
				}

				if (queryType == 1) {
					JSONArray execHeads = new JSONArray(new String(execHeadsJsonString));
					String modifExecHeads = "[{\"headerName\" : \"\", \"field\" : \"\", \"checkboxSelection\" : true, \"maxWidth\" : \"50\", \"headerCheckboxSelection\" : true},";
					modifExecHeads += execHeads.toString().substring(1);

					JSONArray newExecHeads = new JSONArray(new String(modifExecHeads));
					for (int o = 0; o < qbeIdDto.size(); o++) {
						for (int oo = 0; oo < qbeIdDto.get(o).getParameters().size(); oo++) {
							String paramName = qbeIdDto.get(o).getParameters().get(oo).getParamName().toUpperCase();
							String paramValue = qbeIdDto.get(o).getParameters().get(oo).getParamValue();
							if (!paramName.equals("")) {
								String jsonParam = "[{\"paramName\":\"" + paramName + "\",\"paramValue\":\"" + paramValue + "\"}]";
								JSONArray parametersList = new JSONArray(new String(jsonParam));
								for (int i = 0; i < parametersList.length(); i++) {
									String paramVal = parametersList.getJSONObject(i).getString("paramValue");
									query = query.toUpperCase().replace("[" + parametersList.getJSONObject(i).getString("paramName").toUpperCase() + "]", paramVal);
								}
							}
						}
					}

					if (!whereCond.equals("-1") && !whereCond.equals(null)) {
						query = " select * from ( " + query + " )   where " + whereCond;
					}

					List<ObjectNode> dataList = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, query);
					String data = "[{ \"headers\" : [" + newExecHeads + "], \"result\" : [" + dataList + "] }]";


					JSONArray test = new JSONArray(new String(data));
					ObjectMapper objectMapper = new ObjectMapper();
					
					resultData = objectMapper.readValue(test.toString(), new TypeReference<List<ObjectNode>>() {
					});
					
				}
				if (queryType == 2) {
					for (int o = 0; o < qbeIdDto.size(); o++) {
						for (int oo = 0; oo < qbeIdDto.get(o).getParameters().size(); oo++) {
							String paramName = qbeIdDto.get(o).getParameters().get(oo).getParamName().toUpperCase();
							String paramValue = qbeIdDto.get(o).getParameters().get(oo).getParamValue();

							if (!paramName.equals("")) {
								String jsonParam = "[{\"paramName\":\"" + paramName + "\",\"paramValue\":\""
										+ paramValue + "\"}]";
								JSONArray parametersList = new JSONArray(new String(jsonParam));

								for (int i = 0; i < parametersList.length(); i++) {
									String paramVal = parametersList.getJSONObject(i).getString("paramValue");
									paramVal = "'" + paramVal + "'";
									query = query.toUpperCase().replace(
											"[" + parametersList.getJSONObject(i).getString("paramName").toUpperCase()
													+ "]",
											paramVal);
								}
							}
						}
					}
					List<ObjectNode> dataList = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, query);
					resultData = dataList;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return resultData;
		}
	}

	@Override
	public String getParamsName(long queryId) {
		String paramsString = "";
		byte[] encodedQuery = sqbQueryDetailsRepository.getQueryBlob(queryId);
		if (encodedQuery != null) {
			String decodedQuery = new String(Base64.decodeBase64(encodedQuery));
			String parametersString = decodedQuery.split("<pars>")[1];
			parametersString = parametersString.split("</pars>")[0];
			String[] parameters = parametersString.split("<par ");

			paramsString = "[";

			for (int i = 1; i < parameters.length; i++) {
				String paramType = parameters[i].split("fieldType=\"")[1];
				paramType = paramType.split("\"")[0];
				String id = parameters[i].split("id=\"")[1];
				id = id.split("\"")[0];
				String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,
						parameters[i].indexOf("]"));
				paramsString += "{\"paramName\":\"" + paramName + "\",\"paramType\":\"" + paramType
						+ "\",\"paramDefault\":\"" + 1 + "\"}";

				if (i != parameters.length - 1) {
					paramsString += ",";
				}
			}
			paramsString += "]";
		}
		return paramsString;
	}

	@Override
	public CustomResponse addFieldSet(List<FieldSetDto> fieldSetDto) {
		long objectId = fieldSetDto.get(0).getObjectId();
		try {
			Date now = new Date();

			CfgColumnGroupModel columnModel1 = new CfgColumnGroupModel();

			columnModel1.setName(fieldSetDto.get(0).getFiledsetName());
			columnModel1.setOrderNb(fieldSetDto.get(0).getOrderField());
			columnModel1.setIsHidden("0");
			columnModel1.setAdvancedSearch("0");
			columnModel1.setIsreadOnly("0");
			columnModel1.setCreationDate(now);
			columnModel1.setCreatedBy(fieldSetDto.get(0).getUserId());
			columnModel1.setGroupCode(columnModel1.getId());
			cfgColumnGroupRepository.save(columnModel1);
			long fieldsetid = columnModel1.getId();

			CfgFieldsetObjectModel relationModel = new CfgFieldsetObjectModel();
			relationModel.setObjectId(objectId);
			relationModel.setFieldSetId(fieldsetid);
			relationModel.setCreationDate(now);
			relationModel.setCreatedBy(fieldSetDto.get(0).getUserId());
			cfgFieldsetRepository.save(relationModel);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();
	}

	@Override
	public CustomResponse updateFieldSet(List<FieldSetDto> fieldSetDto) {
		CustomResponse resp = CustomResponse.builder().build();
		@SuppressWarnings("unused")
		long objectId = fieldSetDto.get(0).getObjectId();
		long fieldsetId = fieldSetDto.get(0).getFieldsetId();
		String fieldsetName = fieldSetDto.get(0).getFiledsetName();
		long orderNo = fieldSetDto.get(0).getOrderField();
		cfgColumnGroupRepository.updateFieldset(fieldsetId, fieldsetName, orderNo);
		resp.setCode("0");
		resp.setStatus("success");
		resp.setDescription("updated successfully!");
		return resp;
	}

	@Override
	public List<CfgColumnGroupModel> getAllFieldSetsCombo(long objectId) {
		return cfgColumnGroupRepository.getAllFieldSetsCombo(objectId);
	}

	@Override
	public List<CfgColumnGroupModel> getFieldSetData(long objectId, long fieldsetId) {
		return cfgColumnGroupRepository.getFieldSetData(objectId, fieldsetId);
	}

	@Override
	public CustomResponse createFormButton(long objectId, ButtonDto buttonDto) {
		String inputString = buttonDto.getObjectButtonId();
		byte[] byteArray = inputString.getBytes(StandardCharsets.UTF_8);

		long buttonId = -1;
		CfgColumnConfigModel columnModel = new CfgColumnConfigModel(); // Create a new instance for each row
		columnModel.setColumnName(buttonDto.getButtonName());
		columnModel.setCreatedBy(buttonDto.getCreatedBy());
		columnModel.setOrderNo(buttonDto.getOrder());
		columnModel.setBlobFile(byteArray);
		columnModel.setColumnLength(100);
		columnModel.setIsMultiple("0");
		columnModel.setIsSuspended("0");
		columnModel.setIsLink("0");
		columnModel.setSizeField("1");
		columnModel.setIsMandatory("0");
		columnModel.setColumnType(14);
		columnModel.setGroupId(buttonDto.getFieldSet());
		columnModel.setMenus(null);
		columnModel.setTableId(-1);
		columnModel.setIsExcluded("0");
		columnModel.setLanguageId(13);
		columnModel.setIsEditable("0");

		cfgColumnConfigRepository.save(columnModel);

	///InDisplayLogs

	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

	ObjectMapper logMapper = new ObjectMapper();

   ObjectNode logNodeNew = logMapper.convertValue(columnModel, ObjectNode.class);

   List<ObjectNode> logNode=new ArrayList<>();

   logNode.add(logNodeNew);

	insertInDisplayLogs(methodName,logNode,objectId);

   /// 

   

		buttonId = columnModel.getColumnId();

		// In case table_id = -1 already exists don't do anything else insert it

		int findByObjectIdAndTableId = cfgTableObjectifRelRepository.findByObjectIdAndTableId(objectId, -1);

//		CfgTableObjectifRelModel tableObjRelModel = cfgTableObjectifRelRepository.findByObjectIdAndTableId(objectId,-1);

		if (findByObjectIdAndTableId == 0) {
			CfgTableObjectifRelModel tt = new CfgTableObjectifRelModel();
			tt.setObjectId(objectId);
			tt.setTableId(-1);
			tt.setCreatedBy(buttonDto.getCreatedBy());
			cfgTableObjectifRelRepository.save(tt);
		}

		return CustomResponse.builder().code("0").status("success").id(buttonId).description("Success").build();
	}

	@Override
	public CustomResponse updateFormButton(long objectId, ButtonDto buttonDto) {
		String inputString = buttonDto.getObjectButtonId();
		byte[] byteArray = inputString.getBytes(StandardCharsets.UTF_8);

		cfgColumnConfigRepository.updateFormButton(buttonDto.getButtonId(), buttonDto.getButtonName(),
		buttonDto.getOrder(), buttonDto.getFieldSet(), byteArray,buttonDto.getUpdatedBy());

		

		

		///InDisplayLogs

		 String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

		 ObjectMapper logMapper = new ObjectMapper();

       ObjectNode logNodeNew = logMapper.convertValue(buttonDto, ObjectNode.class);

       List<ObjectNode> logNode=new ArrayList<>();

       logNode.add(logNodeNew);

	     insertInDisplayLogs(methodName,logNode,objectId);

		/// 

		
		return CustomResponse.builder().code("0").status("success").id(buttonDto.getButtonId()).description("Success")
				.build();
	}

	@Override
	public CfgColumnConfigModel getButtonData(long buttonId) {
		return cfgColumnConfigRepository.findByColumnId(buttonId);
	}

	@Override
	public CustomResponse deleteButton(long columnId,long objectId,long userId) {		// Delete Button

		CfgColumnConfigModel columnModel = cfgColumnConfigRepository.findByColumnId(columnId);
		if (columnModel != null) {
			cfgColumnConfigRepository.delete(columnModel);
					///InDisplayLogs

					columnModel.setUpdatedBy(userId);

					String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		
					ObjectMapper logMapper = new ObjectMapper();
		
					ObjectNode logNodeNew = logMapper.convertValue(columnModel, ObjectNode.class);
		
					List<ObjectNode> logNode=new ArrayList<>();
		
					logNode.add(logNodeNew);
		
					insertInDisplayLogs(methodName,logNode,objectId);
		
					/// 
//			CfgTableObjectifRelModel tableObjRelModel = cfgTableObjectifRelRepository.findByObjectIdAndTableId(objectId, -1);
//			cfgTableObjectifRelRepository.delete(tableObjRelModel);
			return CustomResponse.builder().code("0").status("SUCCESS").description("DELETED SUCCESSFULLY").build();
		}
		return CustomResponse.builder().code("0").status("Fail").description("Button Not Found").build();

	}

	@Override
	public List<CfgObjectDefMenus> getMenusButton() {
		return objectDefMenusRepository.getMenusButton();
	}

	@Override
	public CustomResponse deleteFieldSet(long fieldSetId) {
		List<CfgColumnConfigModel> cfgColumnConfigModel = cfgColumnConfigRepository.findByGroupId(fieldSetId);
		String returnMessage = "";
		String returnStatus = "";
		if (cfgColumnConfigModel.size() > 0) {
			returnStatus = "error";
			returnMessage = "Please remove linked columns from fieldset";
		} else {
			returnStatus = "success";
			returnMessage = "Success";
			cfgFieldsetRepository.deleteByFieldSetId(fieldSetId);
			cfgColumnGroupRepository.deleteById(fieldSetId);
		}
		return CustomResponse.builder().code("0").status(returnStatus).description(returnMessage).build();
	}

	public String capitalizeString(String value) {
		String input = value.replace("_", " ");
		String[] words = input.split(" "); // Split the input into words

		StringBuilder result = new StringBuilder();
		for (String word : words) {
			if (word.length() > 0) {
				char firstChar = Character.toUpperCase(word.charAt(0)); // Uppercase the first character
				String restOfWord = word.substring(1); // Get the rest of the word
				result.append(firstChar).append(restOfWord.toLowerCase()).append(" "); // Append the modified word
			}
		}

		// Remove the trailing space and print the result
		String output = result.toString().trim();
		return output;
	}

	@Override
	public List<String> dynamicSearch(DynamicSearchDto dynamicSearchDto) {
		String tableName = dynamicSearchDto.getTableName();
		@SuppressWarnings("unused")
		String condition = dynamicSearchDto.getCondition();
		String field = dynamicSearchDto.getColumns().get(0).getField();
		@SuppressWarnings("unused")
		String operator = dynamicSearchDto.getColumns().get(0).getOperator();
		String value = dynamicSearchDto.getColumns().get(0).getValue();

		String query = " select " + field + " from " + tableName + " where " + field + " = '" + value + "' ";
		entityManagerR.createNativeQuery(query).getResultList();
		return null;
	}

	@Override
	public List<String> getDynamicGridDataInsert(List<DynamicFormDto> dynamicSearchDto) {
		String tableName1 = "";
		String colName1 = "";
		for (int i = 0; i < dynamicSearchDto.size(); i++) {

			if (i == dynamicSearchDto.size() - 1) {
				tableName1 += dynamicSearchDto.get(i).getTableName();
			} else {
				tableName1 += dynamicSearchDto.get(i).getTableName() + ",";
			}

			for (int j = 0; j < dynamicSearchDto.get(i).getColumns().size(); j++) {
				if (j == dynamicSearchDto.get(i).getColumns().size() - 1) {
					colName1 += "A." + dynamicSearchDto.get(i).getColumns().get(j).getColName();
				} else {
					colName1 += "A." + dynamicSearchDto.get(i).getColumns().get(j).getColName() + ",";
				}

			}

		}

		@SuppressWarnings("unused")
		String query1 = " select " + colName1 + " from " + tableName1
				+ " A ,  SUITEDBA.CFG_TABLE_OBJECT_REL T where T.OBJECT_ID = " + dynamicSearchDto.get(0).getObjectId();
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ObjectNode> GetColVal(String TableName) {
		@SuppressWarnings("unchecked")
		List<ObjectNode> MyListC = new ArrayList();
		String TabOwner = TableName.split("\\.")[0];
		String TabV = TableName.split("\\.")[1];
		@SuppressWarnings("unused")
		String sqlO = "";

		String sqlT = "select column_name as name ,  column_name as id  from information_schema.columns  where table_name in ('"
				+ TabV + "') and table_schema in ('" + TabOwner + "')";
		MyListC = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, sqlT);
		return MyListC;
	}

	@Override
	public List<ObjectNode> getAllTablesVal() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT concat(owner, '.', tableName) as name ,concat(owner, '.', tableName) as id FROM AllTableModel "
						+ " ");
		
		// where UPPER(owner) like '%DBA'
	}

	@Override
	public List<ObjectNode> getOneTables(long objectId, long tableId) {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT concat(T.tableOwner, '.', T.tableName) as id ,concat(T.tableOwner, '.', T.tableName) as name FROM CfgTableConfigModel T,CfgTableObjectifRelModel R"
						+ " WHERE R.objectId = " + objectId + " and T.tableId = R.tableId " + " and  R.tableId = "
						+ tableId);

	}

//	@Override
//	public List<TechDynamicRuleBuilder> getDBRGrid(long objectId, long columnId) {
//		return techDynamicRuleBuilderRepo.getDBRGrid(objectId, columnId);
//		
//	}

	@Override
	public List<ObjectNode> getDBRGrid(long objectId, long columnId) {
		// return techDynamicRuleBuilderRepo.getDBRGrid(objectId, columnId);
		return ObjectToJsonRepository.getJson(entityManagerR,
				" select CASE WHEN A.actionType = 1 THEN 'saveNew' ELSE 'update' END as actionType, A.ruleId as ruleId,A.ruleDescription as ruleDescription "
						+ " from TechDynamicRuleBuilder A " + " WHERE A.objectId = " + objectId + " and A.columnId = "
						+ columnId + " ORDER BY A.orderNo ASC");

	}

	@Override
	public List<TechDynamicRuleBuilder> getDBRInfo(long ruleId) {
		return techDynamicRuleBuilderRepo.getDBRInfo(ruleId);
	}

	@Override
	public CustomResponse updateDRBRule(long ruleId, List<TechDynamicRuleBuilder> techDynamicRuleBuilder) {
		TechDynamicRuleBuilder tt = techDynamicRuleBuilderRepo.findByRuleId(ruleId);
			///InDisplayLogs

			ObjectMapper logMapperOld = new ObjectMapper();

			ObjectNode logNodeOld = logMapperOld.convertValue(tt, ObjectNode.class);
	
			///
	
			
	
			System.out.println("TTTTT>>>>>>>>"+tt);
		long ruleIdd;
		try {
			if (tt == null) {
				return CustomResponse.builder().code("102").status("fail")
						.description(
								"Rule with this id  = " + techDynamicRuleBuilder.get(0).getRuleId() + " not found!")
						.build();
			} else {
				tt.setUpdateDate(new Date());
				tt.setUpdatedBy(techDynamicRuleBuilder.get(0).getUpdatedBy());
				tt.setRuleDescription(techDynamicRuleBuilder.get(0).getRuleDescription());
				tt.setRuleAction(techDynamicRuleBuilder.get(0).getRuleAction());
				tt.setRuleData(techDynamicRuleBuilder.get(0).getRuleData());
				tt.setColumnId(techDynamicRuleBuilder.get(0).getColumnId());
				tt.setObjectId(techDynamicRuleBuilder.get(0).getObjectId());
				tt.setOrderNo(techDynamicRuleBuilder.get(0).getOrderNo());
				tt.setIsExcluded(techDynamicRuleBuilder.get(0).getIsExcluded());
				
				tt.setHasAdvancedConditions(techDynamicRuleBuilder.get(0).getHasAdvancedConditions());
				techDynamicRuleBuilderRepo.save(tt);
				ruleIdd = tt.getRuleId();
			}
					///InDisplayLogs

					String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

					ObjectMapper logMapper = new ObjectMapper();
	   
					ObjectNode logNodeNew = logMapper.convertValue(techDynamicRuleBuilder.get(0), ObjectNode.class);
	   
					List<ObjectNode> logNode=new ArrayList<>();
	   
					logNode.add(logNodeOld);
	   
					logNode.add(logNodeNew);
	   
					insertInDisplayLogs(methodName,logNode,techDynamicRuleBuilder.get(0).getUpdatedBy());
	   
				   /// 
	   
					
			return CustomResponse.builder().code("100").status("success").id(ruleIdd)
					.description("Updated successfully!").build();
		} catch (Exception e) {
			return CustomResponse.builder().code("101").status("error").description(e.getMessage()).build();
		}
	}

	@Override
	public CustomResponse saveDRBRule(List<TechDynamicRuleBuilder> techDynamicRuleBuilder) {
		try {
			for (int i = 0; i < techDynamicRuleBuilder.size(); i++) {
				TechDynamicRuleBuilder tt = new TechDynamicRuleBuilder();
				tt.setObjectId(techDynamicRuleBuilder.get(i).getObjectId());
				tt.setRuleAction(techDynamicRuleBuilder.get(i).getRuleAction());
				tt.setRuleData(techDynamicRuleBuilder.get(i).getRuleData());
				tt.setRuleDescription(techDynamicRuleBuilder.get(i).getRuleDescription());
				tt.setCreationDate(new Date());
				tt.setCreatedBy(techDynamicRuleBuilder.get(i).getCreatedBy());
				tt.setColumnId(techDynamicRuleBuilder.get(i).getColumnId());
				tt.setOrderNo(techDynamicRuleBuilder.get(i).getOrderNo());
				tt.setHasAdvancedConditions(techDynamicRuleBuilder.get(0).getHasAdvancedConditions());
				tt.setActionType(techDynamicRuleBuilder.get(i).getActionType());
				tt.setIsExcluded(techDynamicRuleBuilder.get(i).getIsExcluded());

				techDynamicRuleBuilderRepo.save(tt);
			}
					///InDisplayLogs

					String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

					ObjectMapper logMapper = new ObjectMapper();
	   
					 ObjectNode logNodeNew = logMapper.convertValue(techDynamicRuleBuilder.get(0), ObjectNode.class);
	   
					 List<ObjectNode> logNode=new ArrayList<>();
	   
					 logNode.add(logNodeNew);
	   
					insertInDisplayLogs(methodName,logNode,techDynamicRuleBuilder.get(0).getCreatedBy());
	   
				   /// 
			return CustomResponse.builder().code("100").status("success").description("Saved Successfully").build();
		} catch (Exception e) {
			return CustomResponse.builder().code("101").status("error").description(e.getMessage()).build();
		}
	}

	@Override
	public CustomResponse deleteDRBRule(long ruleId,long userId) {
		///InDisplayLogs

		TechDynamicRuleBuilder tt = techDynamicRuleBuilderRepo.findByRuleId(ruleId);

		 String methodName = new Object(){}.getClass().getEnclosingMethod().getName();

		 ObjectMapper logMapper = new ObjectMapper();

        ObjectNode logNodeOld = logMapper.convertValue(tt, ObjectNode.class);

        List<ObjectNode> logNode=new ArrayList<>();

        logNode.add(logNodeOld);

	     insertInDisplayLogs(methodName,logNode,userId);

		/// 		techDynamicRuleBuilderRepo.deletedById(ruleId);
		return CustomResponse.builder().code("100").status("success").description("Deleted Successfully").build();
	}

	@Override
	public String getOrders(long objectId, String orderNo, String type) {
		String result = " SELECT ORDER_NO as orderNo FROM SUITEDBA.CFG_TABLE_OBJECT_REL WHERE object_id = " + objectId;
		String finalRes = "";

		@SuppressWarnings("unchecked")
		List<ObjectNode> finalResult = entityManagerR.createNativeQuery(result).getResultList();

		if (finalResult.size() > 0) {
			for (int i = 0; i < finalResult.size(); i++) {
				String results = String.valueOf(finalResult.get(i));
				if (orderNo.equals(results.toString())) {
					if (type.equals("saveNew")) {
						finalRes = "match";
					} else if (type.equals("update")) {
						finalRes = "noMatch";
					}

				} else {
					finalRes = "noMatch";
				}
			}
		} else {
			finalRes = "noMatch";
		}
		return finalRes;
	}

	@Override
	public List<ObjectNode> GetColValLkp(String TableName) {
		@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
		List<ObjectNode> MyListC = new ArrayList();
		String TabV = TableName.split("\\.")[1];
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT columnName as name, columnName as id FROM DbaTabColsModel where (tableName) in ('" + TabV
						+ "')");
	}

	@Override
	public CustomResponse getRelationBetween2tables(RelationBetweenTablesDto relationTablesDto) {
		CustomResponse resp = CustomResponse.builder().build();
		long tableMainId = relationTablesDto.getTableMainId();
		try {
			String query = "SELECT DISTINCT T.TABLE_OWNER || '.' || T.TABLE_NAME\r\n"
					+ "  FROM SUITEDBA.CFG_TABLE_OBJECT_REL O," + "       SUITEDBA.CFG_TABLE_CONFIG     T,"
					+ "       SUITEDBA.CFG_OBJECT_DEF       D" + " WHERE D.OBJECT_ID = O.OBJECT_ID"
					+ "   AND T.TABLE_ID = O.TABLE_ID" + "   AND (D.OBJECT_ID = " + tableMainId + " OR D.OBJECT_P_ID = "
					+ tableMainId + ")" + "   AND T.TABLE_NAME <> 'NO TABLE'";

			String tableNameSelected = relationTablesDto.getTableName().split("\\.")[1];
			String OwnerOfTableNameSelected = relationTablesDto.getTableName().split("\\.")[0];
			@SuppressWarnings("unchecked")
			List<ObjectNode> getAllTables = entityManagerR.createNativeQuery(query).getResultList();
			String bulkQueries = "";
			for (int i = 0; i < getAllTables.size(); i++) {
				if (String.valueOf(getAllTables.get(i)).indexOf(".") != 1) {
					String owner = String.valueOf(getAllTables.get(i)).split("\\.")[0];
					String tableName = String.valueOf(getAllTables.get(i)).split("\\.")[1];

					if (i == 0) {
						bulkQueries = "SELECT count(1) AS TT " + "  FROM information_schema.table_constraints c "
								+ " where (c.table_name) = '" + tableNameSelected + "' "
								+ "   and (c.CONSTRAINT_SCHEMA) = '" + OwnerOfTableNameSelected + "' "
								+ "   and c.constraint_type = 'FOREIGN KEY' " + "   and c.r_constraint_name = "
								+ "       (SELECT s.constraint_name " + "          FROM information_schema.table_constraints s "
								+ "         where s.table_name = '" + tableName + "' "
								+ "           and s.TABLE_SCHEMA = '" + owner + "' "
								+ "           and s.constraint_type = 'PRIMARY KEY')";

						bulkQueries = bulkQueries + " UNION ALL " + "SELECT count(1) AS TT "
								+ "  FROM information_schema.table_constraints c " + " where c.table_name = '" + tableName
								+ "') " + "   and(c.CONSTRAINT_SCHEMA='" + owner + "' "
								+ "   and c.constraint_type = 'FOREIGN KEY' " + "   and c.r_constraint_name = "
								+ "       (SELECT s.constraint_name " + "          FROM information_schema.table_constraints s "
								+ "         where s.table_name = '" + tableNameSelected + "' "
								+ "           and s.CONSTRAINT_SCHEMA = '" + OwnerOfTableNameSelected + "' "
								+ "           and s.constraint_type = 'PRIMARY KEY')";
					}

					if (i != 0) {
						bulkQueries = bulkQueries + " UNION ALL " + " SELECT count(1) AS TT "
								+ "  FROM information_schema.table_constraints c " + " where c.table_name =('"
								+ tableNameSelected + "' " + "   and c.CONSTRAINT_SCHEMA) =('"
								+ OwnerOfTableNameSelected + "' " + "   and c.constraint_type = 'FOREIGN KEY' "
								+ "   and c.r_constraint_name = " + "       (SELECT s.constraint_name "
								+ "          FROM information_schema.table_constraints s " + "         where s.table_name = '"
								+ tableName + "' " + "           and s.CONSTRAINT_SCHEMA = '" + owner + "' "
								+ "           and s.constraint_type = 'PRIMARY KEY')";

						bulkQueries = bulkQueries + " UNION ALL " + " SELECT count(1) AS TT "
								+ "  FROM information_schema.table_constraints c " + " where c.table_name = '" + tableName
								+ "' " + "   and c.CONSTRAINT_SCHEMA = '" + owner + "' "
								+ "   and c.constraint_type = 'FOREIGN KEY' " + "   and c.r_constraint_name = "
								+ "       (SELECT s.constraint_name " + "          FROM information_schema.table_constraints s "
								+ "         where (s.table_name) = ('" + tableNameSelected + "') "
								+ "           and (s.CONSTRAINT_SCHEMA) = ('" + OwnerOfTableNameSelected + "') "
								+ "           and s.constraint_type = 'PRIMARY KEY')";
					}
				}
			}

			String res = "SELECT SUM(TT) FROM (" + bulkQueries + ")";
			int getFinalResult = Integer
					.valueOf(String.valueOf(entityManagerR.createNativeQuery(res).getSingleResult()));
			if (getFinalResult > 0) {
				resp.setStatus("true");
			} else {
				resp.setStatus("false");
			}
			resp.setDescription("success");
			return resp;
		} catch (Exception e) {
			return CustomResponse.builder().code("101").status("error").description(e.getMessage()).build();
		}
	}

	@Override
	public List<TechDynamicRuleBuilder> getDBRGridByRuleActionAndObjectId(long objectId, long ruleAction,
			long columnId) {
		return techDynamicRuleBuilderRepo.getDBRGridByRuleActionAndObjectId(objectId, ruleAction, columnId);
	}

	@Override
	public List<ObjectNode> getTabTables2(long objectId) {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT concat(T.tableOwner, '.', T.tableName) as name, concat(T.tableOwner, '.', T.tableName) as id FROM CfgTableConfigModel T,CfgTableObjectifRelModel R"
						+ " WHERE R.objectId = " + objectId + " and T.tableId = R.tableId and T.tableName != " + null);
	}

	@Override
	public List<ObjectNode> fieldOrderManagement(long objectId) {
		return ObjectToJsonRepository.getJson(entityManagerR,
				" SELECT C.columnId as id, C.columnDescription as description, C.orderNo as orderNo"
						+ " FROM CfgColumnConfigModel C, CfgTableConfigModel T, CfgTableObjectifRelModel TT"
						+ " WHERE T.tableId = C.tableId  AND C.isSuspended <> 1 AND C.columnDescription IS NOT NULL " + " AND  T.tableId = TT.tableId " + " AND TT.objectId = "
						+ objectId + "" + " ORDER BY C.orderNo ASC");
	}

	@Override
	public CustomResponse updateFieldOrder(List<FieldOrderNo> fieldOrderNo) {
		String returnedStatus = "";
		String returnedDesc = "";
		String returnedCode = "";
		try {
			for (int i = 0; i < fieldOrderNo.size(); i++) {
				cfgColumnConfigRepository.updateFieldOrder(fieldOrderNo.get(i).getColumnId(),
						fieldOrderNo.get(i).getOrderNo());
			}

			returnedStatus = "success";
			returnedDesc = "Updated Successfully";
			returnedCode = "100";
		} catch (Exception e) {
			returnedStatus = "error";
			returnedDesc = "Something went wrong, check logs";
			returnedCode = "100";

			e.printStackTrace();
		}
		return CustomResponse.builder().code(returnedCode).status(returnedStatus).id(0).description(returnedDesc)
				.build();
	}

	@Override
	public List<ObjectNode> getAllProcAndPack() {
		try {
			List<ObjectNode> res = ObjectToJsonRepository.getJson(entityManagerR,
					"SELECT concat(a.owner, '.', a.objectName) as id, concat(a.owner, '.', a.objectName) as name "
							+ " FROM AllObjectsModel a" + " where a.objectType IN ('PROCEDURE','PACKAGE')");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public String callApi(AfterSaveDto AfterSaveDto) {
		String x = "Success";
		String StringQuery = "INSERT INTO TECHDBA.TEMP_RES_AFTER_SAVE(TYPE_OF_CALL,P_NAME) VALUES('callApi','')";
//		entityManagerR.createNativeQuery(StringQuery).executeUpdate();	
		executeNativeCUDQuery(StringQuery);
		return x;
	}

	@Override
	@Transactional
	public CustomResponse callProcedure(ProcedureDto procuderDto) {
		String returnedCode = "-1";
		String returnedStatus = "";
		String returnedDesc = "";
		try {
			callMainPROC(procuderDto.getProcedureName(), procuderDto.getProcParam(), procuderDto.getExecutedBy());
			@SuppressWarnings("unused")
			String result = "Calling procedure Successssss";
			String StringQuery = "INSERT INTO TECHDBA.TEMP_RES_AFTER_SAVE(TYPE_OF_CALL,P_NAME) VALUES('callProcedure','"
					+ procuderDto.getProcedureName() + "')";
			executeNativeCUDQuery(StringQuery);

			returnedDesc = "Successfully executed process";
			returnedCode = "100";
			returnedStatus = "success";
		} catch (Exception e) {
			returnedDesc = e.getMessage();
			returnedCode = "101";
			returnedStatus = "error";
		}
		return CustomResponse.builder().code(returnedCode).status(returnedStatus).id(0).description(returnedDesc)
				.build();
	}

	@Override
	public List<RuleBuilderComboModel> getDBRCombo(long objectId, long columnId) {
		return null;
	}

	@Override
	public List<TechDynamicRuleBuilder> getDBRGridByRuleActionAndRuleId(long objectId, long ruleAction, long ruleId) {
		return techDynamicRuleBuilderRepo.getDBRGridByRuleActionAndRuleId(objectId, ruleAction, ruleId);
	}

	@Override
	public List<AdvancedResultDto> getAdvancedResult(List<AdvancedResultDto> addList) {
		List<AdvancedResultDto> yourList = new ArrayList<>();
		for (int i = 0; i < addList.size(); i++) {
			AdvancedResultDto item = new AdvancedResultDto();
			item.setBeginCondition(addList.get(i).getBeginCondition());
			item.setField(addList.get(i).getField());
			item.setCondition(addList.get(i).getCondition());
			item.setValue(addList.get(i).getValue());
			item.setOperator(addList.get(i).getOperator());
			item.setEndCondition(addList.get(i).getEndCondition());

			yourList.add(item);
		}

		return yourList;
	}

	@Transactional
	public void callMainPROC(String ProcName, String ProcParam, String ExecutedBy) {
		String procedureCall = "BEGIN SSDX_ENG.P_EXECUTE_V21_PROC(?, ?, ?); END;";
		
		if (newProcParams != null) {

	        ProcParam = ProcParam + "~" + newProcParams;

	    }
			jdbcTemplate.update(procedureCall, ProcName, ProcParam, ExecutedBy);
	}

	@Override
	public CustomResponse insertCustomField(List<InsertCustomField> insertCustomField) {
		String returnedStatus = "";
		String returnedDesc = "";
		String returnedCode = "";

		String columnName = insertCustomField.get(0).getColumnName();
		String columnDesc = insertCustomField.get(0).getColumnDescription();
		long createdBy = insertCustomField.get(0).getCreatedBy();
		long tableId = Long.parseLong(columnName.split("_")[4]);
		long objectId = insertCustomField.get(0).getObjectId();
		@SuppressWarnings("unused")
		long destinationField = Long.parseLong(columnName.split("_")[3]);

		try {
			// create field set group
			CfgColumnGroupModel columnModel2 = new CfgColumnGroupModel();
			columnModel2.setName("Custom Field");
			columnModel2.setOrderNb(0);
			columnModel2.setIsHidden("0");
			columnModel2.setAdvancedSearch("0");
			columnModel2.setIsreadOnly("0");
			columnModel2.setCreationDate(new Date());
			columnModel2.setCreatedBy(createdBy);
			columnModel2.setGroupCode(columnModel2.getId());
			cfgColumnGroupRepository.save(columnModel2);
			long fieldsetid = columnModel2.getId();

			// create link between field set and form
			CfgFieldsetObjectModel relationModel = new CfgFieldsetObjectModel();
			relationModel.setFieldSetId(fieldsetid);
			relationModel.setObjectId(objectId);
			relationModel.setCreationDate(new Date());
			relationModel.setCreatedBy(createdBy);
			cfgFieldsetRepository.save(relationModel);

			// create column
			CfgColumnConfigModel columnModel = new CfgColumnConfigModel();
			columnModel.setColumnDescription(capitalizeString(columnDesc));
			columnModel.setColumnName(columnName);
			columnModel.setCreatedBy(createdBy);
			columnModel.setTableId(tableId);
			columnModel.setIsLink("0");
			columnModel.setMenus(null);
			columnModel.setOrderNo(1);
			columnModel.setColumnLength(100);
			columnModel.setIsMultiple("0");
			columnModel.setIsSuspended("0");
			columnModel.setSizeField("1");
			columnModel.setGroupId(fieldsetid);
			columnModel.setIsExcluded("0");
			columnModel.setLanguageId(13);
			columnModel.setIsEditable("0");

			columnModel.setIsMandatory("0"); // always create custom field as nullable
			columnModel.setColumnType(1); // always create custom field as varchar2
			cfgColumnConfigRepository.save(columnModel);

			returnedStatus = "success";
			returnedDesc = "Created successfully";
			returnedCode = "100";
		} catch (Exception e) {
			returnedStatus = "error";
			returnedDesc = "Something went wrong, check logs";
			returnedCode = "101";
		}

		return CustomResponse.builder().code(returnedCode).status(returnedStatus).id(0).description(returnedDesc)
				.build();
	}

	@Override
	@Transactional
	public String sendForApproval(ProcedureDto procedureDto) {

		callMainPROC(procedureDto.getProcedureName(), procedureDto.getProcParam() + "~", "");
		String StringQuery = "INSERT INTO TECHDBA.TEMP_RES_AFTER_SAVE(TYPE_OF_CALL,P_NAME) VALUES('callProcedure','" + procedureDto.getProcedureName() + "')";
//		entityManagerR.createNativeQuery(StringQuery).executeUpdate();
		executeNativeCUDQuery(StringQuery);
		return null;
	}

	@Override
	public List<ObjectNode> getColumnsSelected(int tableId) {

		String Query = "SELECT c.columnId as id, c.columnName as name FROM CfgColumnConfigModel c WHERE c.tableId = "
				+ tableId;
		List<ObjectNode> Query1 = ObjectToJsonRepository.getJson(entityManagerR, Query);
		return Query1;
	}

	@Override
	public List<ObjectNode> getFieldDependencies(long objectId) {
		String query = "SELECT COLUMN_ID, DEPENDENT_QBE_ID FROM SUITEDBA.CFG_COLUMN_CONFIG C WHERE C.TABLE_ID IN (SELECT TABLE_ID FROM SUITEDBA.CFG_TABLE_OBJECT_REL WHERE OBJECT_ID = "
				+ objectId + ") AND DEPENDENT_QBE_ID IS NOT NULL";
		List<ObjectNode> t = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, query);

		return t;
	}

	@Override
	public List<ObjectNode> getColNameAndColId(List<Integer> columnId) {
		String x = "";
		for (int i = 0; i < columnId.size(); i++) {
			if (i == 0) {
				x = x + columnId.get(i);
			} else {
				x = x + "," + columnId.get(i);
			}

		}

		String Query = "SELECT c.columnId as id, c.columnName as name FROM CfgColumnConfigModel c WHERE c.columnId in ("
				+ x + ")";
		List<ObjectNode> Query1 = ObjectToJsonRepository.getJson(entityManagerR, Query);
		return Query1;
	}

@Override
public CustomResponse CallCheckExisting(CustomAPIDto parameters) {
		String category = "";
		String legalId = "";
		String firstName = "";
		String lastName = "";
		String fatherName = "";
		String legalDocument = "";
		String registrationNumber = "";
		String registrationCountryId = "";
		String underEstablishment = "";
		String businessLicenseNumber = "";
		String data = "";
		Integer CheckExtCommercialProfile;
		Integer CheckExtCommercialDraft;
		Integer CheckExtLicenseProfile;
		Integer CheckExtLicenseDraft;
		Integer CheckExtIndivDraft;
		Integer CheckExtIndivProfile;
		@SuppressWarnings("unused")
		Integer SessionIdInd;

		for (int i = 0; i < parameters.getColumns().size(); i++) {
			if (parameters.getColumns().get(i).getColName().equals("PARTY_TYPE_CODE")) {
				category = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("REGISTRATION_NO")) {
				registrationNumber = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("REGISTRATION_COU_ID")) {
				registrationCountryId = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("IS_SOC_UNDER_ESTABLISHMENT")) {
				underEstablishment = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("TECH_LICENSE_DOC_NO")) {
				businessLicenseNumber = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("PIC_IDENTIFIER")) {
				legalId = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_FIRST_NAME")) {
				firstName = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_LAST_NAME")) {
				lastName = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_FATHER_NAME")) {
				fatherName = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("PIC_ID")) {
				legalDocument = parameters.getColumns().get(i).getColVal();
			}
		}

		String customerFullName = "'" + firstName + " " + fatherName + " " + lastName + "'";
		if (category.equals("8") && underEstablishment.equals("0")) {
			if (registrationNumber.equals("")) {
				data = data + "  Registration Number";
			}
			if (businessLicenseNumber.equals("")) {
				data = data + " & Business License Number";
			}
			if (registrationCountryId.equals("")) {
				data = data + " & Registration Country";
			}
			if (data == "") {
			} else {
				String data_1 = data;
				if (data.startsWith(" &")) {
					data_1 = data_1.replaceFirst("&", "");
				}
				return CustomResponse.builder().code("0").status("Please fill " + data_1).id(0).description("alert")
						.build();
			}

			if (!registrationNumber.equals("") && !businessLicenseNumber.equals("")
					&& !registrationCountryId.equals("")) {
				String result1 = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SDEDBA.REF_CUSTOMER C ," + "   SDEDBA.REF_CUSTOMER_MISC_INFO M"
						+ " WHERE C.CUSTOMER_ID = M.CUSTOMER_ID" + " AND C.PARTY_TYPE_CODE = 8"
						+ " AND ( C.REGISTRATION_NO = TO_CHAR ( " + registrationNumber + " )"
						+ "      and M.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " ) )"
						+ " AND C.Registration_Cou_Id = TO_CHAR ( " + registrationCountryId + " )" + " UNION ALL" + ""
						+ " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
						+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
						+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
						+ " AND A.SESSION_ID = S.SESSION_ID" + " AND ( VC.REGISTRATION_NO IN TO_CHAR ( "
						+ registrationNumber + " )" + "      and VM.TECH_LICENSE_DOC_NO = TO_CHAR ( "
						+ businessLicenseNumber + " ) )" + " AND VC.Registration_Cou_Id IN TO_CHAR ( "
						+ registrationCountryId + " )" + " AND s.status_code <> 1198 " + " AND S.STATUS_CODE <> ("
						+ "      SELECT STATUS_ID" + "      FROM sts_status"
						+ "      where upper ( STATUS_NAME ) = 'CREATED' )";
				@SuppressWarnings("unchecked")
				List<String> GetCountCommercial = entityManagerR.createNativeQuery(result1).getResultList();
				String t = GetCountCommercial.toString();
				t = t.replace("[", "").replace("]", "");
				CheckExtCommercialProfile = Integer.parseInt(t.split(",")[0].toString().trim());
				CheckExtCommercialDraft = Integer.parseInt(t.split(",")[1].toString().trim());

				if (CheckExtCommercialProfile == 1) {
					if (CheckExtCommercialDraft == 1) {
						String query = "SELECT cast(S.SESSION_ID as text)" + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,"
								+ "   SUITEDBA.CFG_OBJECT_SESSION S ," + "   VLPDBA.REF_CUSTOMER VC ,"
								+ "   VLPDBA.REF_CUSTOMER_MISC_INFO VM" + " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
								+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND ( VC.REGISTRATION_NO IN TO_CHAR ( " + registrationNumber + " )"
								+ "      AND VM.tech_license_doc_no = TO_CHAR ( " + businessLicenseNumber + " ) )"
								+ " AND VC.Registration_Cou_Id IN TO_CHAR ( " + registrationCountryId + " )"
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> (" + "      SELECT STATUS_ID"
								+ "      FROM sts_status" + "      where upper ( STATUS_NAME ) = 'CREATED' )";
						String getSessionIdCommercial = String
								.valueOf(entityManagerR.createNativeQuery(query).getSingleResult());
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number : " + getSessionIdCommercial)
								.id(0).description("alert").build();
					} else {
						String Parameters = "[{\"colName\":\"REGISTRATIONNO\",\"colVal\":\"" + registrationNumber
								+ "\"},{\"colName\":\"BUSINESSLICENSENUMBERID\",\"colVal\":\"" + businessLicenseNumber
								+ "\"}]";
						return CustomResponse.builder().objectId("30708~A~" + Parameters).code("1")
								.status("underEstbNo").id(0).description("open").build();
					}
				} else {
					if (CheckExtCommercialDraft == 1) {
						String query = "SELECT cast(S.SESSION_ID as text)" + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,"
								+ "   SUITEDBA.CFG_OBJECT_SESSION S ," + "   VLPDBA.REF_CUSTOMER VC ,"
								+ "   VLPDBA.REF_CUSTOMER_MISC_INFO VM" + " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
								+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND ( VC.REGISTRATION_NO IN TO_CHAR ( " +"'" + registrationNumber +"'" + " )"
								+ "      AND VM.tech_license_doc_no = TO_CHAR ( " +"'" + businessLicenseNumber +"'" + " ) )"
								+ " AND VC.Registration_Cou_Id IN TO_CHAR ( " + registrationCountryId + " )"
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> (" + "      SELECT STATUS_ID"
								+ "      FROM sts_status" + "      where upper ( STATUS_NAME ) = 'CREATED' )  limit 1";
						
						String getSessionIdCommercial = String
								.valueOf(entityManagerR.createNativeQuery(query).getSingleResult());
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number : " + getSessionIdCommercial)
								.id(0).description("alert").build();
					} else {
						return CustomResponse.builder().code("0").showSaveButton(1).status("Customer Doesn't Exist")
								.id(0).description("alert").build();
					}
				}

			}
		}

		if (category.equals("8") && underEstablishment.equals("1")) {
			if (businessLicenseNumber.equals("")) {
				data = data + " & Business License Number";
			}
			if (registrationCountryId.equals("")) {
				data = data + " & Registration Country";
			}
			if (data == "") {
			} else {
				String data_1 = data;
				if (data.startsWith(" &")) {
					data_1 = data_1.replaceFirst(" &", "");
				}
				return CustomResponse.builder().code("0").status("Please fill " + data_1).id(0).description("alert").build();
			}

			if (!businessLicenseNumber.equals("") && !registrationCountryId.equals("")) {
				String result1 = " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SDEDBA.REF_CUSTOMER C ," + "   SDEDBA.REF_CUSTOMER_MISC_INFO M"
						+ " WHERE C.CUSTOMER_ID = M.CUSTOMER_ID" + " AND C.PARTY_TYPE_CODE = 8"
						+ " AND C.Registration_Cou_Id = TO_CHAR ( " + registrationCountryId + " )"
						+ " AND M.tech_license_doc_no= TO_CHAR ( " + businessLicenseNumber + " )" + " UNION ALL" + ""
						+ " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
						+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
						+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
						+ " AND A.SESSION_ID = S.SESSION_ID" + " AND VC.Registration_Cou_Id = TO_CHAR ( "
						+ registrationCountryId + " )" + " AND VM.tech_license_doc_no = TO_CHAR ( "
						+ businessLicenseNumber + " )" + " AND s.status_code <> 1198 " + " AND S.STATUS_CODE <> ("
						+ "      SELECT STATUS_ID" + "      FROM sts_status"
						+ "      where upper ( STATUS_NAME ) = 'CREATED' )";

				@SuppressWarnings("unchecked")
				List<String> GetCountCommercial = entityManagerR.createNativeQuery(result1).getResultList();
				String t = GetCountCommercial.toString();
				t = t.replace("[", "").replace("]", "");
				CheckExtLicenseProfile = Integer.parseInt(t.split(",")[0].toString().trim());
				CheckExtLicenseDraft = Integer.parseInt(t.split(",")[1].toString().trim());

				if (CheckExtLicenseProfile == 1) {
					if (CheckExtLicenseDraft == 1) {
						String query = "SELECT cast(S.SESSION_ID as text)" + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,"
								+ "   SUITEDBA.CFG_OBJECT_SESSION S ," + "   VLPDBA.REF_CUSTOMER VC ,"
								+ "   VLPDBA.REF_CUSTOMER_MISC_INFO VM" + " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
								+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " )"
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> (" + "      SELECT STATUS_ID"
								+ "      FROM sts_status" + "      where upper ( STATUS_NAME ) = 'CREATED' )";
						String getSessionIdLicense = String
								.valueOf(entityManagerR.createNativeQuery(query).getSingleResult());
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number : " + getSessionIdLicense).id(0)
								.description("alert").build();
					} else {
						// open a screen
						String Parameters = "[{\"colName\":\"BUSINESSLICENSENUMBERID\",\"colVal\":\""
								+ businessLicenseNumber + "\"}]";
						return CustomResponse.builder().objectId("30706~A~" + Parameters).code("1")
								.status("underEstbYes").id(0).description("open").build();
					}
				} else {
					if (CheckExtLicenseDraft == 1) {
						String query = "SELECT cast(S.SESSION_ID as text)" + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,"
								+ "   SUITEDBA.CFG_OBJECT_SESSION S ," + "   VLPDBA.REF_CUSTOMER VC ,"
								+ "   VLPDBA.REF_CUSTOMER_MISC_INFO VM" + " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
								+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " )"
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> (" + "      SELECT STATUS_ID"
								+ "      FROM sts_status" + "      where upper ( STATUS_NAME ) = 'CREATED' )";
						String getSessionIdLicense = String
								.valueOf(entityManagerR.createNativeQuery(query).getSingleResult());
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number : " + getSessionIdLicense).id(0)
								.description("alert").build();
					} else {
						return CustomResponse.builder().code("0").showSaveButton(1).status("Customer Doesn't Exist")
								.id(0).description("alert").build();
					}
				}
			}
		}
		if (category.equals("7")) {
			data = "";
			if (legalId == "") {
				if (data == "") {
					data = "Legal Id ";
				} else {
					data = data + " & Legal Id";
				}
			}

			if (legalDocument == "") {
				if (data == "") {
					data = " Legal Document ";
				} else {
					data = data + " & Legal Document ";
				}
			}

			if (data == "") {
			} else {
				return CustomResponse.builder().code("0").status("Please fill  " + data).id(0).description("alert").build();
			}
			if (legalId != "" && legalDocument != "") {
				String count1 = " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS "
						+ " FROM SDEDBA.REF_CUSTOMER C , " + "   SDEDBA.REF_CUSTOMER_MISC_INFO M "
						+ " WHERE C.CUSTOMER_ID = M.CUSTOMER_ID " + " AND C.PARTY_TYPE_CODE = 7 " + " AND EXISTS ( "
						+ "      SELECT 1 " + "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T "
						+ "      WHERE T.PIC_ID IN ( " + "           SELECT I.PIC_ID "
						+ "           FROM SDEDBA.REF_CUST_PARTY_IDENTIFICATION I "
						+ "           WHERE I.CUSTOMER_ID = C.CUSTOMER_ID "  
						+ "           AND ( upper ( I.PIC_INTERNAL_CODE ) = upper ( TO_CHAR ( '" + legalId + "' ) ) "
						+ "                OR upper ( I.PIC_IDENTIFIER ) = upper ( TO_CHAR ( '" + legalId + "' ) ) ) ) "
						+ "      AND T.PIC_TYPE IS NULL " + "      AND upper ( T.PIC_ID ) = upper ( " + legalDocument
						+ " ) ) " + " UNION ALL " + ""
						+ " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS "
						+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A , " + "   SUITEDBA.CFG_OBJECT_SESSION S , "
						+ "   VLPDBA.REF_CUSTOMER VC " + " WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID "
						+ " AND A.SESSION_ID = S.SESSION_ID " + " AND EXISTS ( " + "      SELECT 1 "
						+ "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T " + "      WHERE T.PIC_ID IN ( "
						+ "           SELECT I.PIC_ID " + "           FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I "
						+ "           WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID " 
						+ "           AND ( upper ( I.PIC_INTERNAL_CODE ) = upper ( TO_CHAR ( '" + legalId + "' ) ) "
						+ "                OR upper ( I.PIC_IDENTIFIER ) = upper ( TO_CHAR ( '" + legalId + "' ) ) ) ) "
						+ "      AND T.PIC_TYPE IS NULL " + "      AND upper ( T.PIC_ID ) = upper ( " + legalDocument
						+ " ) ) " + " AND s.status_code <> 1198 " + " AND S.STATUS_CODE <> ( "
						+ "      SELECT STATUS_ID " + "      FROM sts_status "
						+ "      where upper ( STATUS_NAME ) = 'CREATED' )";

				@SuppressWarnings("unchecked")
				List<String> GetCountIndiv = entityManagerR.createNativeQuery(count1).getResultList();
				String t = GetCountIndiv.toString();
				t = t.replace("[", "").replace("]", "");
				CheckExtIndivProfile = Integer.parseInt(t.split(",")[0].toString().trim());
				CheckExtIndivDraft = Integer.parseInt(t.split(",")[1].toString().trim());
				if (CheckExtIndivProfile == 1) {
					if (CheckExtIndivDraft == 1) {
						String query1 = " SELECT cast(S.SESSION_ID as text) " + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A , "
								+ "   SUITEDBA.CFG_OBJECT_SESSION S , " + "   VLPDBA.REF_CUSTOMER VC "
								+ " WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID " + " AND A.SESSION_ID = S.SESSION_ID "
								+ " AND EXISTS ( " + "      SELECT 1 "
								+ "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T " + "      WHERE T.PIC_ID IN ( "
								+ "           SELECT I.PIC_ID "
								+ "           FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I "
								+ "           WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID " + "           AND I.ORDER_NO = 1 "
								+ "           AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' ) "
								+ "                OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) ) ) "
								+ "      AND T.PIC_TYPE IS NULL " + "      AND T.PIC_ID = " + legalDocument + " ) "
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> ( " + "      SELECT STATUS_ID "
								+ "      FROM sts_status " + "      where upper ( STATUS_NAME ) = 'CREATED' )";
						String getSessionIdInd = (String) entityManagerR.createNativeQuery(query1).getSingleResult();

						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number : " + getSessionIdInd).id(0)
								.description("alert").build();
					} else {
						String Parameters = "[{\"colName\":\"LEGALID\",\"colVal\":\"" + legalId
								+ "\"},{\"colName\":\"SESSIONID\",\"colVal\":\"" + 12345
								+ "\"},{\"colName\":\"LEGALDOCUMENT\",\"colVal\":\"" + legalDocument + "\"}]";
						return CustomResponse.builder().objectId("30701~A~" + Parameters).code("1").status("exactMatch")
								.id(0).description("open").build();
					}
				} else {
					if (CheckExtIndivDraft == 1) {
						String query1 = " SELECT cast(S.SESSION_ID as text) " + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A , "
								+ "   SUITEDBA.CFG_OBJECT_SESSION S , " + "   VLPDBA.REF_CUSTOMER VC "
								+ " WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID " + " AND A.SESSION_ID = S.SESSION_ID "
								+ " AND EXISTS ( " + "      SELECT 1 "
								+ "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T " + "      WHERE T.PIC_ID IN ( "
								+ "           SELECT I.PIC_ID "
								+ "           FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I "
								+ "           WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID " + "           AND I.ORDER_NO = 1 "
								+ "           AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' ) "
								+ "                OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) ) ) "
								+ "      AND T.PIC_TYPE IS NULL " + "      AND T.PIC_ID = " + legalDocument + " ) "
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> ( " + "      SELECT STATUS_ID "
								+ "      FROM sts_status " + "      where upper ( STATUS_NAME ) = 'CREATED' )";
						String getSessionIdInd = (String) entityManagerR.createNativeQuery(query1).getSingleResult();
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number : " + getSessionIdInd).id(0)
								.description("alert").build();
					} else {
						String query2 = " select DECODE ( count ( 1 ) , 0 , 0 , 1 ) as Pass " + " from ( "
								+ "      SELECT 1 as NUMBEROFRECORDS " + "      FROM SDEDBA.REF_CUSTOMER C , "
								+ "        SDEDBA.REF_CUSTOMER_MISC_INFO M "
								+ "      WHERE C.CUSTOMER_ID = M.CUSTOMER_ID " + "      AND C.PARTY_TYPE_CODE = 7 "
								+ "      AND UTL_MATCH.JARO_WINKLER_SIMILARITY ( UPPER ( M.CUSTOMER_FIRST_NAME ) ||UPPER ( M.CUSTOMER_FATHER_NAME ) ||UPPER ( M.CUSTOMER_LAST_NAME ) , "
								+ "             TO_CHAR ( UPPER ( " + customerFullName + " ) ) ) >= 80 )";
						String GetCountIndiv1 = String
								.valueOf(entityManagerR.createNativeQuery(query2).getSingleResult());
						if (GetCountIndiv1.equals("0")) {
							return CustomResponse.builder().code("0").showSaveButton(1).status("Customer Doesn't Exist")
									.id(0).description("alert").build();

						} else {
							// open screen (near match)
							String Parameters = "[{\"colName\":\"CustomerFullName\",\"colVal\":\"" + customerFullName
									+ "\"},{\"colName\":\"legalDocument\",\"colVal\":\"" + legalDocument + "\"}]";
							return CustomResponse.builder().objectId("30702~A~" + Parameters).code("1")
									.status("nearMatch").id(1).description("open").build();
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public CustomResponse CallCheckExistingGeneralInfo(CustomAPIDto parameters) {
		String category = "";
		String legalId = "";
		String firstName = "";
		String lastName = "";
		String fatherName = "";
		String legalDocument = "";
		String registrationNumber = "";
		String registrationCountryId = "";
		@SuppressWarnings("unused")
		String underEstablishment = "";
		String businessLicenseNumber = "";
		String sessionId = "";
		String data = "";
		Integer CheckExtCommercialProfile;
		Integer CheckExtCommercialDraft;
		@SuppressWarnings("unused")
		Integer NumberOfRecords;
		@SuppressWarnings("unused")
		String customerTypeId = "";
		String linkToCustomer = "";
		String customerSubCategory = "";

		for (int i = 0; i < parameters.getColumns().size(); i++) {
			if (parameters.getColumns().get(i).getColName().equals("PARTY_TYPE_CODE")) {
				category = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_TYPE_ID")) {
				customerTypeId = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("REGISTRATION_NO")) {
				registrationNumber = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("REGISTRATION_COU_ID")) {
				registrationCountryId = parameters.getColumns().get(i).getColVal();
			}
			else if (parameters.getColumns().get(i).getColName().equals("TECH_LICENSE_DOC_NO")) {
				businessLicenseNumber = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("PIC_IDENTIFIER")) {
				legalId = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_FIRST_NAME")) {
				firstName = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_LAST_NAME")) {
				lastName = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_FATHER_NAME")) {
				fatherName = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("PIC_ID")) {
				legalDocument = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("ASSOCIATED_CUSTOMER_ID")) {
				linkToCustomer = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("CUSTOMER_ASSOCIATION_TYPE")) {
				customerSubCategory = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("session_id")) {
				sessionId = parameters.getColumns().get(i).getColVal();
			}
		}

		if (businessLicenseNumber.equals("")) {
			underEstablishment = "0";
		} else {
			underEstablishment = "1";
		}
		String customerFullName = "'" + firstName + " " + fatherName + " " + lastName + "'";

		if (category.equals("7")) {
			if (legalDocument.equals("")) {
				data = data + " legal Document";
			}

			if (legalId.equals("")) {
				data = data + "legal Id";
			}

			if (linkToCustomer.equals("")) {
				data = data + " & linked To Customer";
			}

			if (customerSubCategory.equals(null)) {
				data = data + " & Customer Sub Category";
			}

			if (data == "") {
			}

			else {
				String data_1 = data;
				if (data.startsWith(" &")) {
					data_1 = data_1.replaceFirst("&", "");
					for (int i = 1; i < data.length(); i++) {
						data_1 += data.charAt(i);
					}
				}
				return CustomResponse.builder().code("0").status("Please fill " + data_1).id(0).description("alert")
						.build();
			}

			if (!legalDocument.equals("") && !legalId.equals("") && !linkToCustomer.equals("")
					&& !customerSubCategory.equals(null)) {
				String queryOne_1 = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) AS NUMBEROFRECORDS"
						+ " FROM SDEDBA.REF_CUSTOMER C ," + "   SDEDBA.REF_CUSTOMER_MISC_INFO M"
						+ " WHERE C.CUSTOMER_ID = M.CUSTOMER_ID" + " AND C.PARTY_TYPE_CODE = 7" + " AND EXISTS ("
						+ "      SELECT 1" + "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T"
						+ "      WHERE T.PIC_ID IN (" + "           SELECT I.PIC_ID"
						+ "           FROM SDEDBA.REF_CUST_PARTY_IDENTIFICATION I"
						+ "           WHERE I.CUSTOMER_ID = C.CUSTOMER_ID" + "           AND I.ORDER_NO = 1"
						+ "           AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' )"
						+ "                OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) ) )"
						+ "      AND T.PIC_TYPE IS NULL" + "      AND T.PIC_ID = " + legalDocument + " )"
						+ " AND EXISTS (" + "      SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 )"
						+ "      FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A" + "      WHERE A.SESSION_ID = " + sessionId
						+ " " + "      AND A.ELEM_DATA_VALUE = C.CUSTOMER_ID )" + " UNION ALL" + ""
						+ " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) AS NUMBEROFRECORDS"
						+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
						+ "   VLPDBA.REF_CUSTOMER VC" + " WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
						+ " AND A.SESSION_ID = S.SESSION_ID" + " AND EXISTS (" + "      SELECT 1"
						+ "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T" + "      WHERE T.PIC_ID IN ("
						+ "           SELECT I.PIC_ID" + "           FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I"
						+ "           WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID" + "           AND I.ORDER_NO = 1"
						+ "           AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' )"
						+ "                OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) ) )"
						+ "      AND T.PIC_TYPE IS NULL" + "      AND T.PIC_ID = " + legalDocument + " )"
						+ "      AND s.status_code <> 1198 " + "	   AND S.STATUS_CODE <> ("
						+ "      SELECT STATUS_ID" + "      FROM sts_status"
						+ "      where ( STATUS_NAME ) = 'CREATED' )";

				@SuppressWarnings("unchecked")
				List<String> GetCountqueryOne_1 = entityManagerR.createNativeQuery(queryOne_1).getResultList();
				String t = GetCountqueryOne_1.toString();
				t = t.replace("[", "").replace("]", "");
				CheckExtCommercialProfile = Integer.parseInt(t.split(",")[0].toString().trim());
				CheckExtCommercialDraft = Integer.parseInt(t.split(",")[1].toString().trim());

				if (CheckExtCommercialProfile == 1) {
					if (CheckExtCommercialDraft == 1) {
					//Query return if customer exist under the same session
					String FoundUnderTheSameSession2 ="SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) AS NUMBEROFRECORDS "  + 
							"FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,\r\n" + 
							"   SUITEDBA.CFG_OBJECT_SESSION S ,\r\n" + 
							"   VLPDBA.REF_CUSTOMER VC\r\n" + 
							"WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID\r\n" + 
							"AND A.SESSION_ID = S.SESSION_ID\r\n" + 
							"AND A.SESSION_ID = "+sessionId+" \r\n" + 
							"AND EXISTS (SELECT 1 FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I \r\n" + 
							"WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID  AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' ) OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) )   \r\n" + 
							"      AND I.PIC_ID = "+legalDocument+" )"; 
					
						@SuppressWarnings("unchecked")
						List<String> FoundUnderTheSameSession2List = entityManagerR.createNativeQuery(FoundUnderTheSameSession2).getResultList();
						String tt22 = FoundUnderTheSameSession2List.toString();
						tt22 = tt22.replace("[","").replace("]", "");
						Integer NumberOfRecords22 = Integer.parseInt(tt22.split(",")[0].toString().trim());
					if(NumberOfRecords22 == 1) {
						String GetDataSameCustomer = "SELECT L.CUSTOMER_FIRST_NAME ||' '|| L.customer_father_name||' '||L.customer_last_name  as CustomerName ,\r\n" + 
								"   r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType ,\r\n" + 
								"   r.ASSOCIATED_CUSTOMER_ID as ParentCustomer " + 
								"FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION s,SUITEDBA.CFG_OBJECT_SESSION_DTL vc,\r\n" + 
								"VLPDBA.REF_CUSTOMER_ASSOCIATION r , VLPDBA.REF_CUSTOMER_MISC_INFO L\r\n" + 
								"where vc.customer_id = s.customer_id and\r\n" + 
								"r.customer_id = s.customer_id and\r\n" + 
								"l.customer_id = s.customer_id and\r\n" + 
								"vc.session_id = "+sessionId+" \r\n" + 
								"and s.pic_identifier = TO_CHAR ( '"+legalId+"' )\r\n" + 
								"and s.pic_id = "+legalDocument+"";
												
						  List<String> allCategoryFound = new ArrayList<>();
						  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
						  @SuppressWarnings("unchecked")
						List<Object[]> resultList = entityManagerR.createNativeQuery(GetDataSameCustomer).getResultList();
							for (Object[] row : resultList) {
							    List<String> rowData = new ArrayList<>();
							    for (Object column : row) {
							        rowData.add(String.valueOf(column));
							    }
							    GetDataSameCustomer_1.add(rowData);
							}
							
						  Integer customerExist1 = 0;					  						
					      for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
					          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
					              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
					          }
					      }
					      if (!allCategoryFound.isEmpty()) {
					          for (String categoryy : allCategoryFound) {
					              if (customerSubCategory.equals(categoryy)) {
					                  customerExist1 += 1;
					              }
					            }
					          if(customerExist1.equals(0)) {
					          	
					        	     	String Parameters3 = "[{colName:LEGALID,colVal:"+12+"},{colName:LEGALDOCUMENT,colVal:"+12+"},{colName:SESSION_,colVal:"+"}]";
					  		            return CustomResponse.builder().objectId("30760~A~"+Parameters3).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0)
											.description("open").build();
					          }
					        if(!customerExist1.equals(0)) {
					      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
										.description("alert").build();
					        }
					       }
					    else {
					    	String Parameters3 = "[{colName:LEGALID,colVal:"+12+"},{colName:LEGALDOCUMENT,colVal:"+12+"},{colName:SESSION_,colVal:"+"}]";
		  		            return CustomResponse.builder().objectId("30760~A~"+Parameters3).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0)
								.description("open").build();
					    }
					      }
	      				else {
						String getSessionIdInd_Query = "SELECT cast(S.SESSION_ID as text)"
								+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
								+ "   VLPDBA.REF_CUSTOMER VC" + " WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
								+ " AND A.SESSION_ID = S.SESSION_ID" + " AND EXISTS (" + "      SELECT 1"
								+ "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T" + "      WHERE T.PIC_ID IN ("
								+ "           SELECT I.PIC_ID"
								+ "           FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I"
								+ "           WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID" + "           AND I.ORDER_NO = 1"
								+ "           AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' )"
								+ "                OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) ) )"
								+ "      AND T.PIC_TYPE IS NULL" + "      AND T.PIC_ID = " + legalDocument + " )"
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> (" + "      SELECT STATUS_ID"
								+ "      FROM sts_status" + "      where ( STATUS_NAME ) = 'CREATED' )";

						String getSessionIdInddd = (String) entityManagerR.createNativeQuery(getSessionIdInd_Query)
								.getSingleResult();
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number " + getSessionIdInddd).id(0)
								.description("alert").build();

					}
            	  	   }

					else {
						String Parameters = "[{colName:LEGALID,colVal:" + legalId
								+ "},{colName:LEGALDOCUMENT,colVal:" + legalDocument
								+ "},{colName:session_id,colVal:" + sessionId + "}]";
						return CustomResponse.builder().objectId("30764~A~" + Parameters).code("0")
								.status("CallCheckExistingGeneralInfo1/exactMatch").id(0).description("open").build();
					}
				}

				else {
					if (CheckExtCommercialDraft == 1) {
						String FoundUnderTheSameSession2 ="SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) AS NUMBEROFRECORDS " + 
								"FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,\r\n" + 
								"   SUITEDBA.CFG_OBJECT_SESSION S ,\r\n" + 
								"   VLPDBA.REF_CUSTOMER VC\r\n" + 
								"WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID\r\n" + 
								"AND A.SESSION_ID = S.SESSION_ID\r\n" + 
								"AND A.SESSION_ID = "+sessionId+" \r\n" + 
								"AND EXISTS (SELECT 1 FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I \r\n" + 
								"WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID  AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' ) OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) )   \r\n" + 
								"      AND I.PIC_ID = "+legalDocument+" )"; 
						
							@SuppressWarnings("unchecked")
							List<String> FoundUnderTheSameSession2List = entityManagerR.createNativeQuery(FoundUnderTheSameSession2).getResultList();
							String tt22 = FoundUnderTheSameSession2List.toString();
							tt22 = tt22.replace("[","").replace("]", "");
							Integer NumberOfRecords22 = Integer.parseInt(tt22.split(",")[0].toString().trim());
						if(NumberOfRecords22 == 1) {

							String GetDataSameCustomer = "SELECT L.CUSTOMER_FIRST_NAME ||' '|| L.customer_father_name||' '||L.customer_last_name  as CustomerName ,\r\n" + 
									"   r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType ,\r\n" + 
									"   r.ASSOCIATED_CUSTOMER_ID as ParentCustomer " + 
									"FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION s,SUITEDBA.CFG_OBJECT_SESSION_DTL vc,\r\n" + 
									"VLPDBA.REF_CUSTOMER_ASSOCIATION r , VLPDBA.REF_CUSTOMER_MISC_INFO L\r\n" + 
									"where vc.customer_id = s.customer_id and\r\n" + 
									"r.customer_id = s.customer_id and\r\n" + 
									"l.customer_id = s.customer_id and\r\n" + 
									"vc.session_id = "+sessionId+" \r\n" + 
									"and s.pic_identifier = TO_CHAR ( '"+legalId+"' )\r\n" + 
									"and s.pic_id = "+legalDocument+"";
													
							  List<String> allCategoryFound = new ArrayList<>();
							  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
							  @SuppressWarnings("unchecked")
							List<Object[]> resultList = entityManagerR.createNativeQuery(GetDataSameCustomer).getResultList();
								for (Object[] row : resultList) {
								    List<String> rowData = new ArrayList<>();
								    for (Object column : row) {
								        rowData.add(String.valueOf(column));
								    }
								    GetDataSameCustomer_1.add(rowData);
								}
						  Integer customerExist1 = 0; 
						  for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
						          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
						              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
						          }
						      }
						      if (!allCategoryFound.isEmpty()) {
						          for (String categoryy : allCategoryFound) {
						              if (customerSubCategory.equals(categoryy)) {
						                  customerExist1 += 1;
						              }
						            }
						          if(customerExist1.equals(0)) {
						          	
						        	  String Parameters3 = "[{colName:LEGALID,colVal:"+12+"},{colName:LEGALDOCUMENT,colVal:"+12+"},{colName:SESSION_,colVal:"+"}]";
					  		            return CustomResponse.builder().objectId("30760~A~"+Parameters3).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0)
											.description("open").build();
						          }
						        if(!customerExist1.equals(0)) {
						      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
											.description("alert").build();
						        }
						       }
						    else {
						    	String Parameters3 = "[{colName:LEGALID,colVal:"+12+"},{colName:LEGALDOCUMENT,colVal:"+12+"},{colName:SESSION_,colVal:"+"}]";
			  		            return CustomResponse.builder().objectId("30760~A~"+Parameters3).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0)
									.description("open").build();
						
						    }
					}
	      				else {
						String getSessionIdInd_Query = "SELECT cast(S.SESSION_ID as text)"
								+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
								+ "   VLPDBA.REF_CUSTOMER VC" + " WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
								+ " AND A.SESSION_ID = S.SESSION_ID" + " AND EXISTS (" + "      SELECT 1"
								+ "      FROM SDEDBA.REF_PARTY_IDENTIFICATION_TYPE T" + "      WHERE T.PIC_ID IN ("
								+ "           SELECT I.PIC_ID"
								+ "           FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I"
								+ "           WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID" + "           AND I.ORDER_NO = 1"
								+ "           AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' )"
								+ "                OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) ) )"
								+ "      AND T.PIC_TYPE IS NULL" + "      AND T.PIC_ID = " + legalDocument + " )"
								+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> (" + "      SELECT STATUS_ID"
								+ "      FROM sts_status" + "      where ( STATUS_NAME ) = 'CREATED' )";

						String getSessionIdInd = (String) entityManagerR.createNativeQuery(getSessionIdInd_Query)
								.getSingleResult();
						return CustomResponse.builder().code("0")
								.status("Customer already exists under session number " + getSessionIdInd).id(0)
								.description("alert").build();
	      				}
					} else {
						String FoundUnderTheSameSession2 ="SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) AS NUMBEROFRECORDS \r\n" + 
								"FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,\r\n" + 
								"   SUITEDBA.CFG_OBJECT_SESSION S ,\r\n" + 
								"   VLPDBA.REF_CUSTOMER VC\r\n" + 
								"WHERE A.ELEM_DATA_VALUE = VC.CUSTOMER_ID\r\n" + 
								"AND A.SESSION_ID = S.SESSION_ID\r\n" + 
								"AND A.SESSION_ID = "+sessionId+" \r\n" + 
								"AND EXISTS (SELECT 1 FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION I \r\n" + 
								"WHERE I.CUSTOMER_ID = VC.CUSTOMER_ID  AND ( I.PIC_INTERNAL_CODE = TO_CHAR ( '" + legalId + "' ) OR I.PIC_IDENTIFIER = TO_CHAR ( '" + legalId + "' ) )   \r\n" + 
								"      AND I.PIC_ID = "+legalDocument+" )"; 
						
							@SuppressWarnings("unchecked")
							List<String> FoundUnderTheSameSession2List = entityManagerR.createNativeQuery(FoundUnderTheSameSession2).getResultList();
							String tt22 = FoundUnderTheSameSession2List.toString();
							tt22 = tt22.replace("[","").replace("]", "");
							Integer NumberOfRecords22 = Integer.parseInt(tt22.split(",")[0].toString().trim());
						if(NumberOfRecords22 == 1) {					
							String GetDataSameCustomer = "SELECT L.CUSTOMER_FIRST_NAME ||' '|| L.customer_father_name||' '||L.customer_last_name  as CustomerName ,\r\n" + 
									"   r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType ,\r\n" + 
									"   r.ASSOCIATED_CUSTOMER_ID as ParentCustomer " + 
									"FROM VLPDBA.REF_CUST_PARTY_IDENTIFICATION s,SUITEDBA.CFG_OBJECT_SESSION_DTL vc,\r\n" + 
									"VLPDBA.REF_CUSTOMER_ASSOCIATION r , VLPDBA.REF_CUSTOMER_MISC_INFO L\r\n" + 
									"where vc.customer_id = s.customer_id and\r\n" + 
									"r.customer_id = s.customer_id and\r\n" + 
									"l.customer_id = s.customer_id and\r\n" + 
									"vc.session_id = "+sessionId+" \r\n" + 
									"and s.pic_identifier = TO_CHAR ( '"+legalId+"' )\r\n" + 
									"and s.pic_id = "+legalDocument+"";
													
							  List<String> allCategoryFound = new ArrayList<>();
							  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
							  @SuppressWarnings("unchecked")
							List<Object[]> resultList = entityManagerR.createNativeQuery(GetDataSameCustomer).getResultList();
								for (Object[] row : resultList) {
								    List<String> rowData = new ArrayList<>();
								    for (Object column : row) {
								        rowData.add(String.valueOf(column));
								    }
								    GetDataSameCustomer_1.add(rowData);
								}
							    Integer customerExist1 = 0;
						      for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
						          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
						              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
						          }
						      }
						      if (!allCategoryFound.isEmpty()) {
						          for (String categoryy : allCategoryFound) {
						              if (customerSubCategory.equals(categoryy)) {
						                  customerExist1 += 1;
						              }
						            }
						          if(customerExist1.equals(0)) {
						          	
						        	  //   	String Parameters3 = "[{colName:LEGALID,colVal:"+12+"},{colName:LEGALDOCUMENT,colVal:"+12+"},{colName:SESSION_,colVal:"+"}]";
						//  		  return CustomResponse.builder().objectId("30760~A~"+Parameters3).code("1").status("CallCheckExistingGeneralInfo/exactMatch").id(0)
						//						.description("open").build();
//						          	return CustomResponse.builder().code("sameSession").status("same customer different type ").id(0)
//											.description("alert").build();
						          	String Parameters = "[{colName:LEGALID,colVal:" + legalId
											+ "},{colName:LEGALDOCUMENT,colVal:" + legalDocument
											+ "},{colName:session_id,colVal:" + sessionId + "}]";
									return CustomResponse.builder().objectId("30760~A~" + Parameters).code("sameSession")
											.status("CallCheckExistingGeneralInfo1/exactMatch").id(0).description("open").build();
						          }
						        if(!customerExist1.equals(0)) {
						      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
											.description("alert").build();
						        }
						       }
						    else {
						    	@SuppressWarnings("unused")
								String Parameters4 = "[{colName:LEGALID,colVal:"+12+"},{colName:LEGALDOCUMENT,colVal:"+12+"},{colName:SESSION_,colVal:"+"}]";
						    	return CustomResponse.builder().code("sameSession").status("existe under the same session different parent ").id(0)
										.description("alert").build();
						
						    }
						      }
						else {
						String GetCountIndiv_Query = "select DECODE ( count ( 1 ) , 0 , 0 , 1 ) as Pass" + " from ("
								+ "      SELECT 1 as NUMBEROFRECORDS" + "      FROM SDEDBA.REF_CUSTOMER C ,"
								+ "        SDEDBA.REF_CUSTOMER_MISC_INFO M"
								+ "      WHERE C.CUSTOMER_ID = M.CUSTOMER_ID" + "      AND C.PARTY_TYPE_CODE = 7"
								+ "      AND UTL_MATCH.JARO_WINKLER_SIMILARITY ( M.CUSTOMER_FIRST_NAME || M.CUSTOMER_FATHER_NAME || M.CUSTOMER_LAST_NAME ,"
								+ "             TO_CHAR ( " + customerFullName + " ) ) >= 80 )" + " UNION ALL" + ""
								+ " select DECODE ( count ( 1 ) , 0 , 0 , 1 ) as Pass" + " from ("
								+ "      SELECT 1 as NUMBEROFRECORDS" + "      FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,"
								+ "        SUITEDBA.CFG_OBJECT_SESSION S ," + "        VLPDBA.REF_CUSTOMER C ,"
								+ "        VLPDBA.REF_CUSTOMER_MISC_INFO M"
								+ "      WHERE A.ELEM_DATA_VALUE = C.CUSTOMER_ID"
								+ "      AND A.SESSION_ID = S.SESSION_ID" + "      AND C.CUSTOMER_ID = M.CUSTOMER_ID"
								+ "      AND C.PARTY_TYPE_CODE = 7"
								+ "      AND UTL_MATCH.JARO_WINKLER_SIMILARITY ( M.CUSTOMER_FIRST_NAME||M.CUSTOMER_FATHER_NAME||M.CUSTOMER_LAST_NAME ,"
								+ "             TO_CHAR ( " + customerFullName + " ) ) >= 80"
								+ "      AND S.STATUS_CODE <> 1198 " + "      AND S.STATUS_CODE <> ("
								+ "           SELECT STATUS_ID" + "           FROM sts_status"
								+ "           WHERE ( STATUS_NAME ) = 'CREATED' ) )";

						@SuppressWarnings("unchecked")
						List<String> GetQueryResult = entityManagerR.createNativeQuery(GetCountIndiv_Query).getResultList();
						String tttt = GetQueryResult.toString();
						tttt = tttt.replace("[", "").replace("]", "");

						Integer ProfilePass = Integer.parseInt(tttt.split(",")[0].toString().trim());
						@SuppressWarnings("unused")
						Integer KycPass = Integer.parseInt(tttt.split(",")[1].toString().trim());

						if (ProfilePass == 0) {
							return CustomResponse.builder().code("0").showSaveButton(1).status("Customer Doesn't Exist")
									.id(0).description("alert").build();
						} else {
							String Parameters = "[{colName:CustomerFullName,colVal:" + customerFullName
									+ "},{colName:SESSIONID,colVal:" + sessionId
									+ "},{colName:legalDocument,colVal:" + legalDocument + "}]";
							return CustomResponse.builder().objectId("30721~A~" + Parameters).code("1")
									.status("GeneralInfo/nearMatch").id(0).description("open").build();
						  }
						}
					}
				}
			}
		}		

		if (category.equals("8") && !registrationNumber.equals("")) {
			if (registrationNumber.equals("")) {
				data = data + " Registration Number";
			}
			if (businessLicenseNumber.equals("")) {
				data = data + " & Business License Number";
			}
			if (linkToCustomer.equals("")) {
				data = data + " & Linked To Customer";
			}
			if (customerSubCategory.equals("")) {
				data = data + " & Customer Sub Category";
			}
			if (data == "") {
			} else {
				String data_1 = data;
				if (data.startsWith(" &")) {
					data_1 = data_1.replaceFirst("&", "");
				}
				return CustomResponse.builder().code("0").status("Please fill " + data_1).id(0).description("alert")
						.build();
			}

			if (!registrationNumber.equals("") && !businessLicenseNumber.equals("") && !linkToCustomer.equals("")
					&& !customerSubCategory.equals("")) {
				String Queryy = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SDEDBA.REF_CUSTOMER C ," + "   SDEDBA.REF_CUSTOMER_MISC_INFO M"
						+ " WHERE C.CUSTOMER_ID = M.CUSTOMER_ID" + " AND C.PARTY_TYPE_CODE = 8"
						+ " AND ( C.REGISTRATION_NO = TO_CHAR ( " + registrationNumber + " )"
						+ "      AND M.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " ) )" + " UNION ALL"
						+ "" + " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
						+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
						+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
						+ " AND A.SESSION_ID = S.SESSION_ID" + " AND ( VC.REGISTRATION_NO = TO_CHAR ( "
						+ registrationNumber + " )" + "      AND VM.TECH_LICENSE_DOC_NO = TO_CHAR ( "
						+ businessLicenseNumber + " ) )" + " AND s.status_code <> 1198 " + " AND S.STATUS_CODE <> ("
						+ "      SELECT STATUS_ID" + "      FROM sts_status"
						+ "      where ( STATUS_NAME ) = 'CREATED' )";

				@SuppressWarnings("unchecked")
				List<String> QueryResult = entityManagerR.createNativeQuery(Queryy).getResultList();

				String t = QueryResult.toString();
				t = t.replace("[", "").replace("]", "");

				Integer CheckExtCorpUnderEstNoProfile = Integer.parseInt(t.split(",")[0].toString().trim());
				Integer CheckExtCorpUnderEstNoDraft = Integer.parseInt(t.split(",")[1].toString().trim());
				if (CheckExtCorpUnderEstNoProfile == 1) {
					if (CheckExtCorpUnderEstNoDraft == 1) {
						//Query return if customer exist under the same session
						String Queryy_No_Of_Records = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
								+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
								+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
								+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
								+ " AND A.SESSION_ID = " + sessionId + " " + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND ( VC.REGISTRATION_NO = TO_CHAR ( " + registrationNumber + " )"
								+ "      OR VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " ) )";

						@SuppressWarnings("unchecked")
						List<String> Queryy_No_Of_RecordsList = entityManagerR.createNativeQuery(Queryy_No_Of_Records)
								.getResultList();
						String ttt3 = Queryy_No_Of_RecordsList.toString();
						ttt3 = ttt3.replace("[", "").replace("]", "");
						Integer numberOfRecordsss = Integer.parseInt(ttt3.split(",")[0].toString().trim());
						if (numberOfRecordsss == 1) {
							//customer exist under the same session (query return same customer )
							String SameCustomer = "select ref.CUSTOMER_NAME as CustomerName,\r\n" + 
									"            r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType,\r\n" + 
									"            r.ASSOCIATED_CUSTOMER_ID as ParentCustomer " + 
									"    from VLPDBA.REF_CUSTOMER  ref ,VLPDBA.REF_CUSTOMER_ASSOCIATION r ,\r\n" + 
									"    VLPDBA.REF_CUSTOMER_MISC_INFO l ,VLPDBA.REF_CUST_PARTY_IDENTIFICATION s , SUITEDBA.CFG_OBJECT_SESSION_DTL vc\r\n" + 
									"    where ref.customer_id = s.customer_id and vc.customer_id = s.customer_id and \r\n" + 
									"          r.customer_id = s.customer_id and l.customer_id = s.customer_id and \r\n" + 
									"          vc.session_id ="+sessionId+" and   ref.REGISTRATION_NO = "+registrationNumber+"\r\n" + 
									"         and l.TECH_LICENSE_DOC_NO = "+businessLicenseNumber+" and ref.REGISTRATION_COU_ID ="+registrationCountryId;
									

							List<String> allCategoryFound = new ArrayList<>();
							  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
							  @SuppressWarnings("unchecked")
							List<Object[]> resultList = entityManagerR.createNativeQuery(SameCustomer).getResultList();
								for (Object[] row : resultList) {
								    List<String> rowData = new ArrayList<>();
								    for (Object column : row) {
								        rowData.add(String.valueOf(column));
								    }
								    GetDataSameCustomer_1.add(rowData);
								}
								//checking if same customer have the same type and the same parent 
							    Integer customerExist1 = 0;
						      for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
						          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
						              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
						          }
						      }
						      if (!allCategoryFound.isEmpty()) {
						          for (String categoryy : allCategoryFound) {
						              if (customerSubCategory.equals(categoryy)) {
						                  customerExist1 += 1;
						              }
						            }
						          if(customerExist1.equals(0)) {
						          	
						        	  String ParametersSS = "[{colName:REGISTRATIONNO,colVal:"+registrationNumber+"},{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
					  		            return CustomResponse.builder().objectId("30722~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//							          	return CustomResponse.builder().code("0").status("same customer different type ").id(0)
//												.description("alert").build();
						          }
						        if(!customerExist1.equals(0)) {
						      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
											.description("alert").build();
						        }
						       }
						    else {
						    	String ParametersSS = "[{colName:REGISTRATIONNO,colVal:"+registrationNumber+"},{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
			  		            return CustomResponse.builder().objectId("30722~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//						    	return CustomResponse.builder().code("0").status("existe under the same session different parent ").id(0)
//										.description("alert").build();
						
						    }
						}

						else {
							String getSessionId_Query = "SELECT cast(S.SESSION_ID as text)"
									+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
									+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
									+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
									+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
									+ " AND ( VC.REGISTRATION_NO IN TO_CHAR ( " + registrationNumber + " )"
									+ "      OR VM.tech_license_doc_no = TO_CHAR (" + businessLicenseNumber + ") )"
									+ " AND VC.Registration_Cou_Id IN TO_CHAR ( " + registrationCountryId + " )"
									+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> ("
									+ "      SELECT STATUS_ID" + "      FROM sts_status"
									+ "      where ( STATUS_NAME ) = 'CREATED' )";

							String SessionIddd = (String) entityManagerR.createNativeQuery(getSessionId_Query)
									.getSingleResult();
							return CustomResponse.builder().code("0")
									.status("Customer already exists under session number " + SessionIddd).id(0)
									.description("alert").build();

						}
					} else {
						String Parameters = "[{colName:REGISTRATIONNO,colVal:" + registrationNumber
								+ "},{colName:BUSINESSLICENSENUMBERID,colVal:" + businessLicenseNumber
								+ "},{colName:SESSION_ID,colVal:"+sessionId+"}]";
						return CustomResponse.builder().objectId("30766~A~" + Parameters).code("1")
								.status("CallCheckExistingGeneralInfo/underEstNo").id(0).description("open").build();
					}

				} else {
					if (CheckExtCorpUnderEstNoDraft == 1) {
						String Queryy_No_Of_Records = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
								+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
								+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
								+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
								+ " AND A.SESSION_ID = " + sessionId + " " + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND ( VC.REGISTRATION_NO = TO_CHAR ( " + registrationNumber + " )"
								+ "      OR VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " ) )";

						@SuppressWarnings("unchecked")
						List<String> Queryy_No_Of_RecordsList = entityManagerR.createNativeQuery(Queryy_No_Of_Records)
								.getResultList();
						String ttt3 = Queryy_No_Of_RecordsList.toString();
						ttt3 = ttt3.replace("[", "").replace("]", "");
						Integer numberOfRecordsss = Integer.parseInt(ttt3.split(",")[0].toString().trim());
						if (numberOfRecordsss == 1) {
							//customer exist under the same session (query return same customer )
							String SameCustomer = "select ref.CUSTOMER_NAME as CustomerName,\r\n" + 
									"            r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType,\r\n" + 
									"            r.ASSOCIATED_CUSTOMER_ID as ParentCustomer\r\n" + 
									"    from VLPDBA.REF_CUSTOMER  ref ,VLPDBA.REF_CUSTOMER_ASSOCIATION r ,\r\n" + 
									"    VLPDBA.REF_CUSTOMER_MISC_INFO l ,VLPDBA.REF_CUST_PARTY_IDENTIFICATION s , SUITEDBA.CFG_OBJECT_SESSION_DTL vc\r\n" + 
									"    where ref.customer_id = s.customer_id and vc.customer_id = s.customer_id and \r\n" + 
									"          r.customer_id = s.customer_id and l.customer_id = s.customer_id and \r\n" + 
									"          vc.session_id ="+sessionId+" and   ref.REGISTRATION_NO = "+registrationNumber+"\r\n" + 
									"         and l.TECH_LICENSE_DOC_NO = "+businessLicenseNumber+" and ref.REGISTRATION_COU_ID ="+registrationCountryId;
									

							List<String> allCategoryFound = new ArrayList<>();
							  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
							  @SuppressWarnings("unchecked")
							List<Object[]> resultList = entityManagerR.createNativeQuery(SameCustomer).getResultList();
								for (Object[] row : resultList) {
								    List<String> rowData = new ArrayList<>();
								    for (Object column : row) {
								        rowData.add(String.valueOf(column));
								    }
								    GetDataSameCustomer_1.add(rowData);
								}
								//checking if same customer have the same type and the same parent 
							    Integer customerExist1 = 0;
						      for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
						          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
						              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
						          }
						      }
						      if (!allCategoryFound.isEmpty()) {
						          for (String categoryy : allCategoryFound) {
						              if (customerSubCategory.equals(categoryy)) {
						                  customerExist1 += 1;
						              }
						            }
						          if(customerExist1.equals(0)) {
						        	     	String ParametersSS = "[{colName:REGISTRATIONNO,colVal:"+registrationNumber+"},{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
						  		            return CustomResponse.builder().objectId("30722~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();  						          	
						          }
						        if(!customerExist1.equals(0)) {
						      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
											.description("alert").build();
						        }
						       }
						    else {
						    	String ParametersSS = "[{colName:REGISTRATIONNO,colVal:"+registrationNumber+"},{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
			  		            return CustomResponse.builder().objectId("30722~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//						    	return CustomResponse.builder().code("0").status("existe under the same session different parent ").id(0)
//										.description("alert").build();
						
						    }
						}

						else {
							String Parameters = "[{colName:REGISTRATIONNO,colVal:" + registrationNumber
									+ "},{colName:BUSINESSLICENSENUMBERID,colVal:"
									+ businessLicenseNumber + "},{colName:SESSION_ID,colVal:"+sessionId+"}]";
							return CustomResponse.builder().objectId("30722~A~" + Parameters).code("1")
									.status("CallCheckExistingGeneralInfo/underEstNo").id(0).description("open")
									.build();
						}
					} else {
						return CustomResponse.builder().code("0").showSaveButton(1).status("Customer Doesn't Exist")
								.id(0).description("alert").build();
					}

				}
			}
		}
		if (category.equals("8") && registrationNumber.equals("")) {
			if (businessLicenseNumber.equals("")) {
				data = data + " & Business License Number";
			}
			if (linkToCustomer.equals("")) {
				data = data + " & Link To Customer";
			}
			if (customerSubCategory.equals("")) {
				data = data + " & Customer Sub Category";
			}
			if (data == "") {
			} else {
				String data_1 = data;
				if (data.startsWith(" &")) {
					data_1 = data_1.replaceFirst("&", "");
				}
				return CustomResponse.builder().code("0").status("Please fill " + data_1).id(0).description("alert")
						.build();
			}

			if (!businessLicenseNumber.equals("") && !linkToCustomer.equals("") && !customerSubCategory.equals("")) {
				String Query_getCountOfCorpUnderEstabYes = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SDEDBA.REF_CUSTOMER C ," + "   SDEDBA.REF_CUSTOMER_MISC_INFO M"
						+ " WHERE C.CUSTOMER_ID = M.CUSTOMER_ID" + " AND C.PARTY_TYPE_CODE = " + 8
						+ " AND M.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " )" + " AND NOT EXISTS ("
						+ "      SELECT 1" + "      FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A"
						+ "      WHERE A.SESSION_ID = " + sessionId + "      AND A.ELEM_DATA_VALUE = C.CUSTOMER_ID )"
						+ " UNION ALL" + "" + " SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
						+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
						+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
						+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
						+ " AND A.SESSION_ID = S.SESSION_ID" + " AND VM.TECH_LICENSE_DOC_NO = TO_CHAR ( "
						+ businessLicenseNumber + " )" + " AND s.status_code <> 1198 " + " AND S.STATUS_CODE <> ("
						+ "      SELECT STATUS_ID" + "      FROM sts_status"
						+ "      where ( STATUS_NAME ) = 'CREATED' )";

				@SuppressWarnings("unchecked")
				List<String> Result_getCountOfCorpUnderEstabYes = entityManagerR
						.createNativeQuery(Query_getCountOfCorpUnderEstabYes).getResultList();
				String ttttt = Result_getCountOfCorpUnderEstabYes.toString();
				ttttt = ttttt.replace("[", "").replace("]", "");
				Integer CheckExtCorpUnderEstYesProfile = Integer.parseInt(ttttt.split(",")[0].toString().trim());
				Integer CheckExtCorpUnderEstYesDraft = Integer.parseInt(ttttt.split(",")[1].toString().trim());

				if (CheckExtCorpUnderEstYesProfile == 1) {
					if (CheckExtCorpUnderEstYesDraft == 1) {

						//Query return if customer exist under the same session
						String Queryy_No_Of_Records = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
								+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
								+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
								+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
								+ " AND A.SESSION_ID = " + sessionId + " " + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND ( VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " ) ) and ref.REGISTRATION_COU_ID="+registrationCountryId;

						@SuppressWarnings("unchecked")
						List<String> Queryy_No_Of_RecordsList = entityManagerR.createNativeQuery(Queryy_No_Of_Records)
								.getResultList();
						String ttt3 = Queryy_No_Of_RecordsList.toString();
						ttt3 = ttt3.replace("[", "").replace("]", "");
						Integer numberOfRecordsss = Integer.parseInt(ttt3.split(",")[0].toString().trim());
						if (numberOfRecordsss == 1) {
							//customer exist under the same session (query return same customer )
							String SameCustomer = "select ref.CUSTOMER_NAME as CustomerName,\r\n" + 
									"            r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType,\r\n" + 
									"            r.ASSOCIATED_CUSTOMER_ID as ParentCustomer\r\n" + 
									"    from VLPDBA.REF_CUSTOMER  ref ,VLPDBA.REF_CUSTOMER_ASSOCIATION r ,\r\n" + 
									"    VLPDBA.REF_CUSTOMER_MISC_INFO l ,VLPDBA.REF_CUST_PARTY_IDENTIFICATION s , SUITEDBA.CFG_OBJECT_SESSION_DTL vc\r\n" + 
									"    where ref.customer_id = s.customer_id and vc.customer_id = s.customer_id and \r\n" + 
									"          r.customer_id = s.customer_id and l.customer_id = s.customer_id and \r\n" + 
									"          vc.session_id ="+sessionId+"\r\n" + 
									"         and l.TECH_LICENSE_DOC_NO = "+businessLicenseNumber+" and ref.REGISTRATION_COU_ID ="+registrationCountryId;
									

							List<String> allCategoryFound = new ArrayList<>();
							  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
							  @SuppressWarnings("unchecked")
							List<Object[]> resultList = entityManagerR.createNativeQuery(SameCustomer).getResultList();
								for (Object[] row : resultList) {
								    List<String> rowData = new ArrayList<>();
								    for (Object column : row) {
								        rowData.add(String.valueOf(column));
								    }
								    GetDataSameCustomer_1.add(rowData);
								}
								//checking if same customer have the same type and the same parent 
							    Integer customerExist1 = 0;
						      for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
						          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
						              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
						          }
						      }
						      if (!allCategoryFound.isEmpty()) {
						          for (String categoryy : allCategoryFound) {
						              if (customerSubCategory.equals(categoryy)) {
						                  customerExist1 += 1;
						              }
						            }
						          if(customerExist1.equals(0)) {
						          	
						        	  String ParametersSS = "[{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
					  		            return CustomResponse.builder().objectId("30724~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//						          	return CustomResponse.builder().code("0").status("same customer different type ").id(0)
//											.description("alert").build();
						          }
						        if(!customerExist1.equals(0)) {
						      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
											.description("alert").build();
						        }
						       }
						    else {
						    	String ParametersSS = "[{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
			  		            return CustomResponse.builder().objectId("30724~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//						    	return CustomResponse.builder().code("0").status("existe under the same session different parent ").id(0)
//										.description("alert").build();
						
						    }
						}

						else {
							String query = "SELECT cast(S.SESSION_ID as text)" + " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ,"
									+ "   SUITEDBA.CFG_OBJECT_SESSION S ," + "   VLPDBA.REF_CUSTOMER VC ,"
									+ "   VLPDBA.REF_CUSTOMER_MISC_INFO VM" + " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
									+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
									+ " AND VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " )"
									+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> ("
									+ "      SELECT STATUS_ID" + "      FROM sts_status"
									+ "      where ( STATUS_NAME ) = 'CREATED' )";
							String getSessionIdLicense = String
									.valueOf(entityManagerR.createNativeQuery(query).getSingleResult());
							return CustomResponse.builder().code("0")
									.status("Customer already existe under this session " + getSessionIdLicense).id(0)
									.description("alert").build();
						}
					} else {

						String Parameters = "[{colName:BUSINESSLICENSENUMBERID,colVal:"
								+ businessLicenseNumber + "}]";
						return CustomResponse.builder().objectId("30767~A~" + Parameters).code("1")
								.status("notSameSession/underEstYes").id(0).description("open").build();
					}

				} else {
					if (CheckExtCorpUnderEstYesDraft == 1) {
						//Query return if customer exist under the same session
						String Queryy_No_Of_Records = "SELECT DECODE ( count ( 1 ) , 0 , 0 , 1 ) as NUMBEROFRECORDS"
								+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
								+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
								+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID" + " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID"
								+ " AND A.SESSION_ID = " + sessionId + " " + " AND A.SESSION_ID = S.SESSION_ID"
								+ " AND ( VM.TECH_LICENSE_DOC_NO = TO_CHAR ( " + businessLicenseNumber + " ) )";

						@SuppressWarnings("unchecked")
						List<String> Queryy_No_Of_RecordsList = entityManagerR.createNativeQuery(Queryy_No_Of_Records)
								.getResultList();
						String ttt3 = Queryy_No_Of_RecordsList.toString();
						ttt3 = ttt3.replace("[", "").replace("]", "");
						Integer numberOfRecordsss = Integer.parseInt(ttt3.split(",")[0].toString().trim());
						if (numberOfRecordsss == 1) {
							//customer exist under the same session (query return same customer )
							String SameCustomer = "select ref.CUSTOMER_NAME as CustomerName,\r\n" + 
									"            r.CUSTOMER_ASSOCIATION_TYPE  as CategoryType,\r\n" + 
									"            r.ASSOCIATED_CUSTOMER_ID as ParentCustomer " + 
									"    from VLPDBA.REF_CUSTOMER  ref ,VLPDBA.REF_CUSTOMER_ASSOCIATION r ,\r\n" + 
									"    VLPDBA.REF_CUSTOMER_MISC_INFO l ,VLPDBA.REF_CUST_PARTY_IDENTIFICATION s , SUITEDBA.CFG_OBJECT_SESSION_DTL vc\r\n" + 
									"    where ref.customer_id = s.customer_id and vc.customer_id = s.customer_id and \r\n" + 
									"          r.customer_id = s.customer_id and l.customer_id = s.customer_id and \r\n" + 
									"          vc.session_id ="+sessionId+"\r\n" + 
									"         and l.TECH_LICENSE_DOC_NO = "+businessLicenseNumber+" and ref.REGISTRATION_COU_ID ="+registrationCountryId;
									

							List<String> allCategoryFound = new ArrayList<>();
							  List<List<String>> GetDataSameCustomer_1 = new ArrayList<>();
							  @SuppressWarnings("unchecked")
							List<Object[]> resultList = entityManagerR.createNativeQuery(SameCustomer).getResultList();
								for (Object[] row : resultList) {
								    List<String> rowData = new ArrayList<>();
								    for (Object column : row) {
								        rowData.add(String.valueOf(column));
								    }
								    GetDataSameCustomer_1.add(rowData);
								}
								//checking if same customer have the same type and the same parent 
							    Integer customerExist1 = 0;
						      for (int i = 0; i < GetDataSameCustomer_1.size(); i++) {
						          if (linkToCustomer.equals(GetDataSameCustomer_1.get(i).get(2))) {
						              allCategoryFound.add(String.valueOf(GetDataSameCustomer_1.get(i).get(1)));
						          }
						      }
						      if (!allCategoryFound.isEmpty()) {
						          for (String categoryy : allCategoryFound) {
						              if (customerSubCategory.equals(categoryy)) {
						                  customerExist1 += 1;
						              }
						            }
						          if(customerExist1.equals(0)) {
						          	
						        	  String ParametersSS = "[{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
					  		            return CustomResponse.builder().objectId("30724~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//						          	return CustomResponse.builder().code("0").status("same customer different type ").id(0)
//											.description("alert").build();
						          }
						        if(!customerExist1.equals(0)) {
						      	return CustomResponse.builder().code("0").status("Customer already existe under this session with the same type ").id(0)
											.description("alert").build();
						        }
						       }
						    else {
						    	String ParametersSS = "[{colName:BUSINESSLICENSENUMBERID,colVal:"+businessLicenseNumber+"},{colName:SESSION_ID,colVal:"+sessionId+"}]";
			  		            return CustomResponse.builder().objectId("30724~A~"+ParametersSS).code("sameSession").status("CallCheckExistingGeneralInfoSameSession/exactMatch").id(0).description("open").build();
//						    	return CustomResponse.builder().code("0").status("existe under the same session different parent ").id(0)
//										.description("alert").build();
						
						    }
						}

						else {

							String getSessionId_Query6 = "SELECT cast(S.SESSION_ID as text)"
									+ " FROM SUITEDBA.CFG_OBJECT_SESSION_DTL A ," + "   SUITEDBA.CFG_OBJECT_SESSION S ,"
									+ "   VLPDBA.REF_CUSTOMER VC ," + "   VLPDBA.REF_CUSTOMER_MISC_INFO VM"
									+ " WHERE VM.CUSTOMER_ID = VC.CUSTOMER_ID"
									+ " AND A.ELEM_DATA_VALUE = VC.CUSTOMER_ID" + " AND A.SESSION_ID = S.SESSION_ID"
									+ " AND ( VC.REGISTRATION_NO IN TO_CHAR ( " + registrationNumber + " )"
									+ "      OR VM.tech_license_doc_no = TO_CHAR (" + businessLicenseNumber + ") )"
									+ " AND VC.Registration_Cou_Id IN TO_CHAR ( " + registrationCountryId + " )"
									+ " AND S.STATUS_CODE <> 1198 " + " AND S.STATUS_CODE <> ("
									+ "      SELECT STATUS_ID" + "      FROM sts_status"
									+ "      where ( STATUS_NAME ) = 'CREATED' )";

							@SuppressWarnings("unused")
							String SessionId6 = (String) entityManagerR.createNativeQuery(getSessionId_Query6)
									.getSingleResult();

							return CustomResponse.builder().code("0")
									.status("Customer already existe under this session ").id(0).description("alert")
									.build();

						}
					} else {
						return CustomResponse.builder().code("0").showSaveButton(1).status("Customer Doesn't Exist")
								.id(0).description("alert").build();
					}
				}
			}
		}
		if (category.equals("")) {
			return CustomResponse.builder().code("0").status("Fill The Party Type Code. ").id(0).description("alert")
					.build();
		}
		return null;
	}

	@Override
	public List<ObjectNode> getTablesLinkedToForm(int objectId) {
		List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
				" SELECT d.tableId as id, (select concat(t.tableOwner, '.', t.tableName) from CfgTableConfigModel t where t.tableId = d.tableId) as name"
						+ " FROM CfgTableObjectifRelModel d WHERE d.objectId = " + objectId + " AND d.tableId <> -1");
		return result;
	}

	@Override
	public String getColNameByColumnId(int columnId) {
		return cfgColumnConfigRepository.findColNameByColumnId(columnId);
	}
	public List<BigDecimal> getAssociatedCustomerIds(BigDecimal customerId) {

        String ListOfCustomers1 = "SELECT DISTINCT (customer_id) " +

                "FROM sdedba.REF_CUSTOMER_ASSOCIATION s " +

                "WHERE associated_customer_id = " + customerId;



       

        return new ArrayList<>();

    }
	@Override
	public CustomResponse Validate(CustomAPIDto parameters) {

		String NumberOfLegalPaper = "";
		String LegalPaperType = "";
		String Legal_ID_Expiry_Date = "";
		String Legal_ID_Issue_Date = "";
		String procuderParam = "";
		String customerId = "";
		@SuppressWarnings("unused")
		String ParentCustomerId = "";
		String sessionId = "";
		long customerSubCategory = 0;
		long sameSession = parameters.getSameSession();
		long userId = parameters.getUserId();
		long NearMatch = parameters.getNearBy();
		
		for (int i = 0; i < parameters.getColumns().size(); i++) {
			if (parameters.getColumns().get(i).getColName().equals("PIC_IDENTIFIER")) {
				NumberOfLegalPaper = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("PIC_ID")) {
				LegalPaperType = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("EXPIRATION_DATE")) {
				Legal_ID_Expiry_Date = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("ISSUE_DATE")) {
				Legal_ID_Issue_Date = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("customer_id")) {
				customerId = parameters.getColumns().get(i).getColVal();
			} else if (parameters.getColumns().get(i).getColName().equals("session_id")) {
				sessionId = parameters.getColumns().get(i).getColVal();
			} else if(parameters.getColumns().get(i).getColName().equals("CUSTOMER_ASSOCIATION_TYPE")) {
				String colVal = parameters.getColumns().get(i).getColVal();
				customerSubCategory = Long.parseLong(colVal);
			}
		}

		if (sessionId.equals("-1")) {
			// String query = "SELECT S_OBJECT_SESSION.NEXTVAL"; //oracle
			String query = "SELECT nextval('S_OBJECT_SESSION')";
			;
			sessionId = String.valueOf(entityManagerR.createNativeQuery(query).getSingleResult());
		} else {
		}

		if (customerId.equals("-1")) {
			return CustomResponse.builder().code("0").status("Select at least one row").id(0).description("alert")
					.build();
		} else {
			List<BigDecimal> allCustomer = new ArrayList<>();

			List<BigDecimal> allCustomer1 = new ArrayList<>();
			String Query = "SELECT s.party_type_code FROM VLPDBA.Ref_Customer s where s.customer_id = " + customerId;
			String Category = String.valueOf(entityManagerR.createNativeQuery(Query).getSingleResult());
			String ListOfCustomers = "select customer_id \r\n" + 

			"from  suitedba.cfg_object_session_dtl \r\n" + 

			"where is_main = 1 \r\n" + 

			"and  session_id = (SELECT SESSION_ID FROM SUITEDBA.cfg_object_session_dtl WHERE CUSTOMER_ID ="  + customerId+ ")\r\n" + 

			"union\r\n" + 

			"select customer_id \r\n" + 

			"from sdedba.REF_CUSTOMER_ASSOCIATION \r\n" + 

			"where ASSOCIATED_CUSTOMER_ID ="  + customerId;	

			

			String CountQuery = "select count(1) from cfg_object_session_dtl where session_id ="

					+ " (SELECT SESSION_ID FROM SUITEDBA.cfg_object_session_dtl WHERE CUSTOMER_ID =" + customerId+")";	

			

			List<BigDecimal> CountNumberList = entityManagerR.createNativeQuery(CountQuery).getResultList();
			@SuppressWarnings("unchecked")
			List<BigDecimal> GetListOfCustomers = entityManagerR.createNativeQuery(ListOfCustomers).getResultList();
			allCustomer = GetListOfCustomers;



			long CountNumber = Long.parseLong(CountNumberList.get(0).toString());







			while(!allCustomer.isEmpty() && CountNumber > allCustomer.size()){

				List<BigDecimal> demoList = new ArrayList<>();

				String counter = "0";

				for(int i=0;i<allCustomer.size();i++) {

					

					String ListOfCustomers1 = "SELECT DISTINCT (customer_id) \r\n" + 

		                    "FROM sdedba.REF_CUSTOMER_ASSOCIATION s \r\n" + 

		                    "WHERE customer_association_type is not null "	

		                    + "and associated_customer_id = "+allCustomer.get(i)

		                    + " and associated_customer_id <> "+customerId ;



					List<BigDecimal> list1 = entityManagerR.createNativeQuery(ListOfCustomers1).getResultList();

					System.out.println("list1 ghady>>>>>"+ list1);

						removeDuplicates(list1.toString());

					demoList.addAll(list1);

				

					if(!list1.isEmpty()) {

						counter = "1";

						

						GetListOfCustomers.addAll(list1);

					}}

				

				if(counter.equals("1")) {

					allCustomer = demoList;

				}else {

					allCustomer.clear();

				}

			} 
				// Iterate over the list of BigDecimal objects GetListOfCustomers
            if(GetListOfCustomers.isEmpty()) {
            	if (Category.equals("7")) {
					procuderParam = "customerId/"+customerId + "~" + "userId/"+userId + "~" + "sessionId/"+sessionId + "~" + "LegalPaperType/"+LegalPaperType + "~"
							+ "NumberOfLegalPaper/"+NumberOfLegalPaper + "~" + "NearMatch/"+NearMatch + "~" + "Legal_ID_Issue_Date/"+Legal_ID_Issue_Date + "~" + "Legal_ID_Expiry_Date/"+Legal_ID_Expiry_Date + "~" + "sameSession/"+sameSession + "~" + "customerSubCategory/"+customerSubCategory;
				} else if (Category.equals("8")) {
					procuderParam = "customerId/"+customerId + "~" + "userId/"+userId + "~" + "sessionId/"+sessionId + "~" + "LegalPaperType/"+ "~"
							+ "NumberOfLegalPaper/" + "~" + "NearMatch/" + "~" + "Legal_ID_Issue_Date/" + "~" + "Legal_ID_Expiry_Date/" + "~" + "sameSession/"+sameSession + "~" + "customerSubCategory/"+customerSubCategory;
				}
				callMainPROC("SSDX_ENG.P_INITIATION_CHECK_EXISTING", procuderParam, "");
            }else {
            	
            	if (Category.equals("7")) {
					procuderParam = "customerId/"+customerId + "~" + "userId/"+userId + "~" + "sessionId/"+sessionId + "~" + "LegalPaperType/"+LegalPaperType + "~"
							+ "NumberOfLegalPaper/"+NumberOfLegalPaper + "~" + "NearMatch/"+NearMatch + "~" + "Legal_ID_Issue_Date/"+Legal_ID_Issue_Date + "~" + "Legal_ID_Expiry_Date/"+Legal_ID_Expiry_Date + "~" + "sameSession/"+sameSession + "~" + "customerSubCategory/"+customerSubCategory ;
				} else if (Category.equals("8")) {
					procuderParam = "customerId/"+customerId + "~" + "userId/"+userId + "~" + "sessionId/"+sessionId + "~" + "LegalPaperType/"+ "~"
							+ "NumberOfLegalPaper/" + "~" + "NearMatch/" + "~" + "Legal_ID_Issue_Date/" + "~" + "Legal_ID_Expiry_Date/" + "~" + "sameSession/"+sameSession + "~" + "customerSubCategory/"+customerSubCategory ;

				}
            	callMainPROC("SSDX_ENG.P_INITIATION_CHECK_EXISTING", procuderParam, "");
            	
            	for (BigDecimal customerIDBigDecimal : GetListOfCustomers) {
    			    // Convert BigDecimal to String
    			    String customerIDString = customerIDBigDecimal.toString();
    				if (Category.equals("7")) {
    					procuderParam = "customerId/"+customerIDString + "~" + "userId/"+userId + "~" + "sessionId/"+sessionId + "~" + "LegalPaperType/"+LegalPaperType + "~"
    							+ "NumberOfLegalPaper/"+NumberOfLegalPaper + "~" + "NearMatch/"+NearMatch + "~" + "Legal_ID_Issue_Date/"+Legal_ID_Issue_Date + "~" + "Legal_ID_Expiry_Date/"+Legal_ID_Expiry_Date + "~" + "sameSession/"+sameSession + "~" + 
    							"customerSubCategory/"+customerSubCategory;
    				} else if (Category.equals("8")) {
    					procuderParam = "customerId/"+customerIDString + "~" + "userId/"+userId + "~" + "sessionId/"+sessionId + "~" + "LegalPaperType/"+ "~"
    							+ "NumberOfLegalPaper/" + "~" + "NearMatch/" + "~" + "Legal_ID_Issue_Date/" + "~" + "Legal_ID_Expiry_Date/" + "~" + "sameSession/"+sameSession + "~" + "customerSubCategory/"+customerSubCategory ;
    				}
    				
    				callMainPROC("SSDX_ENG.P_INITIATION_CHECK_EXISTING", procuderParam, "");
    			}
            }
			
//            return null;
			return CustomResponse.builder().code(sessionId).status("").id(0).description("validate").build();
		}
	}

	@Override
	public List<ObjectNode> getAllButtons(long objectId) {
		try {

			String query = " SELECT concat(T.tableOwner, '.', T.tableName) as tableName, "
					+ " C.columnId as id, C.isMultiple as isMultiple, "
					+ " T.tableId as tableId, C.isExcluded as isExcluded, C.dependencyDefaultValue as dependencyDefaultValue, "
					+ " C.columnName as name, C.groupId as groupId,R.orderNo as orderNo, "
					+ " C.columnType as columnTypeCode, C.columnDescription as columnDescription, "
					+ " (SELECT lower(name) FROM Syslines WHERE heaCode = 426 AND id = C.columnType) as columnType, "
					+ " C.sizeField as sizeField, C.isMandatory as isMandatory , C.isLink as isLink, C.qbeReadOnly as qbeReadOnly, C.query as query, "
					+ " C.columnLength as columnLength, C.defaultValue as defaultValue, C.mandatoryQuery as mandatoryQuery, C.menus as menus,C.isExcluded as isExcluded, "
					+ " C.isSuspended as isSuspended, C.blobFile as blobFile "
					+ " FROM CfgTableConfigModel T, CfgColumnConfigModel C, CfgObjectDefModel O, CfgTableObjectifRelModel R, CfgFieldsetObjectModel Y "
					+ " WHERE T.tableId = C.tableId " + " AND O.objectId = R.objectId "
					+ " AND Y.objectId = O.objectId " + " AND Y.fieldSetId = C.groupId "
					+ " AND T.tableId = R.tableId AND C.columnType = 14 " + " AND O.objectId = " + objectId
					+ " order by C.orderNo ASC ";
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR, query);
			return result;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<List<ObjectNode>> execQueryInDisplay(long qbeId) {

		byte[] encodedQuery = sqbQueryDetailsRepository.getQueryBlob(qbeId);
		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);
		String headsString = decodedQuery.split("<LstHeads>")[1];
		headsString = headsString.split("</LstHeads>")[0];

		String[] headers = headsString.split("<EltHeads ");

		String execHeadsJsonString = "[";

		for (int i = 1; i < headers.length; i++) {
			String header = headers[i].split("dbName=\"")[1];

			header = header.split("\"")[0];

			execHeadsJsonString += "{\"headerName\":\"" + header + "\",\"field\":\"" + header + "\"}";

			if (i != headers.length - 1) {
				execHeadsJsonString += ",";

			}
		}
		execHeadsJsonString += "]";

		String parametersString = decodedQuery.split("<pars>")[1];
		parametersString = parametersString.split("</pars>")[0];
		String[] parameters = parametersString.split("<par ");

		for (int i = 1; i < parameters.length; i++) {
			String paramType = parameters[i].split("fieldType=\"")[1];
			paramType = paramType.split("\"")[0];
			String id = parameters[i].split("id=\"")[1];
			id = id.split("\"")[0];
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,
					parameters[i].indexOf("]"));
			query = query.replace("#" + id + "#", "[" + paramName + "]");
		}

		@SuppressWarnings("unused")
		JSONArray execHeads = new JSONArray(new String(execHeadsJsonString));

//		if(jsonHeaders!=null) {
//			for(int i=0;i<jsonHeaders.length();i++) {
//
//				query=query.replace("["+jsonHeaders.getString(i)+"]","'"+json.get(jsonHeaders.getString(i)).toString()+"'");
//
//			}
//		}

		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> headerList = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(execHeadsJsonString);

			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
				headerList.add(object);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// JSONArray parametersList=new JSONArray(new String
		// (queryDto.getParameters()));
//		
//		for(int i=0;i<parametersList.length();i++){
//			query=query.replace("["+parametersList.getJSONObject(i).getString("paramName")+"]",parametersList.getJSONObject(i).getString("paramValue"));
//		}
//		

		List<ObjectNode> dataList = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, query);

		List<List<ObjectNode>> alldata = new ArrayList<>();

		alldata.add(headerList);
		alldata.add(dataList);
		return alldata;
	}

	@Override
	@Transactional
	public void callProcedurev21Files(V21FilesDto v21FilesDto) {

		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("techdba.v21_files_transfer")
				.registerStoredProcedureParameter("TABLE_NAME", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("PRIMARY_COL", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("PRIMARY_VAL", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("FILE_COL", String.class, ParameterMode.IN);
		storedProcedure.setParameter("TABLE_NAME", v21FilesDto.getTableName());
		storedProcedure.setParameter("PRIMARY_COL", v21FilesDto.getPrimaryCol());
		storedProcedure.setParameter("PRIMARY_VAL", v21FilesDto.getPrimaryVal());
		storedProcedure.setParameter("FILE_COL", v21FilesDto.getFileCol());

		storedProcedure.execute();
	}
	

	@Override
	@Transactional
	public void callProcedurev21Signature(V21SignatureDto v21SignatureDto) {

		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("techdba.v21_signature_transfer")
				.registerStoredProcedureParameter("TABLE_NAME", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("PRIMARY_COL", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("PRIMARY_VAL", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("FILE_COL", String.class, ParameterMode.IN);
		storedProcedure.setParameter("TABLE_NAME", v21SignatureDto.getTableName());
		storedProcedure.setParameter("PRIMARY_COL", v21SignatureDto.getPrimaryCol());
		storedProcedure.setParameter("PRIMARY_VAL", v21SignatureDto.getPrimaryVal());
		storedProcedure.setParameter("FILE_COL", v21SignatureDto.getFileCol());

		storedProcedure.execute();
	}


//	public List<ObjectNode> fetchDialNums() {		
//		return ObjectToJsonRepository.getJson(entityManager,"SELECT REPLACE(a.intlDirectDiallingCode,' ','') as id,a.intlDirectDiallingCode as name FROM PhoneDialModel a");
//	
//	}


	public List<ObjectNode> fetchDialNums() {		
		return ObjectToJsonRepository.getJson(entityManager,"SELECT CONCAT(CONCAT(C.isoCouCodeAlpha, ''), REPLACE(D.intlDirectDiallingCode, ' ', '')) AS id," + 
				"	       CONCAT(CONCAT(C.isoCouCodeAlpha, ''), D.intlDirectDiallingCode) AS name" + 
				"	  FROM PhoneDialModel D, RefComCountryModel C" + 
				"	 WHERE C.isoCouCodeNum = D.couCode");
	
	}
	
	@Override
	public CompletableFuture<Integer> insertQueryForm(QueryFormDto queryFormDto) {
	    Date date = new Date();

	    List<WfmConditionVariableModel> wfmConditionVariableList = new ArrayList<>();
	    @SuppressWarnings("unused")
		List<TechRuleCondition> techRuleConditionList = new ArrayList<>();

	    TechRuleCondition techRuleCondition = new TechRuleCondition();
	    WfmConditionModel wfmConditionModel = new WfmConditionModel();

	    try {
	        wfmConditionModel.setCreationDate(date);
	        wfmConditionModel.setQbeId(queryFormDto.getQbeId());
	        wfmConditionModel.setConditionOperator(0);

	        return CompletableFuture.supplyAsync(() -> {
	            wfmConditionRepo.save(wfmConditionModel);
	            for (int i = 0; i < queryFormDto.getParamKeysValues().size(); i++) {
	                WfmConditionVariableModel wfmConditionVariableModel = new WfmConditionVariableModel();
	                wfmConditionVariableModel.setConditionId(wfmConditionModel.getConditionId());
	                wfmConditionVariableModel.setConditionVarOrder(i);
	                wfmConditionVariableModel.setConditionVarTechnicalName(queryFormDto.getParamKeysValues().get(i).get("id").asText());
	                wfmConditionVariableModel.setConditionVarValue(queryFormDto.getParamKeysValues().get(i).get("value").asText());
	                wfmConditionVariableModel.setCreationDate(date);
	                wfmConditionVariableModel.setCreatedBy(queryFormDto.getUserId());
	                wfmConditionVariableModel.setIsCompoundKey("0");

	                wfmConditionVariableList.add(wfmConditionVariableModel);
	            }
	            wfmConditionVariableRepo.saveAll(wfmConditionVariableList);
	            return wfmConditionModel.getConditionId();
	        }).thenAccept(conditionId -> {
	            techRuleCondition.setConditionId(conditionId);
	            techRuleCondition.setRuleId(queryFormDto.getRuleCode());
	            techRuleConditionRepo.save(techRuleCondition);
	            //system.out.println("CONDITION ID>>>>>>>>>" + conditionId);
	            callprodWfm(conditionId, queryFormDto.getUserId(), queryFormDto.getRuleCode());
	        }).thenApply(ignored -> 0).exceptionally(ex -> {
	            ex.printStackTrace();
	            return 1;
	        });
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return CompletableFuture.completedFuture(1);
	    }
	}

//	@Override
//	public int insertQueryForm(QueryFormDto queryFormDto) {
//		
//		Date date=new Date();
//		
//		
//		List<WfmConditionVariableModel> wfmConditionVariableList = new ArrayList<>();
//		List<TechRuleCondition> techRuleConditionList = new ArrayList<>();
//
//		
//		TechRuleCondition techRuleCondition=new TechRuleCondition();
//		WfmConditionModel wfmConditionModel=new WfmConditionModel();
//		try {
//		wfmConditionModel.setCreationDate(date);
//		wfmConditionModel.setQbeId(queryFormDto.getQbeId());
//		wfmConditionModel.setConditionOperator(0);
//
//		wfmConditionRepo.save(wfmConditionModel);
//		for(int i=0;i<queryFormDto.getParamKeysValues().size();i++) {
//		
//			WfmConditionVariableModel wfmConditionVariableModel =new WfmConditionVariableModel();
//			
//			wfmConditionVariableModel.setConditionId(wfmConditionModel.getConditionId());
//			wfmConditionVariableModel.setConditionVarOrder(i);
//			wfmConditionVariableModel.setConditionVarTechnicalName(queryFormDto.getParamKeysValues().get(i).get("id").asText());
//			wfmConditionVariableModel.setConditionVarValue(queryFormDto.getParamKeysValues().get(i).get("value").asText());
//			wfmConditionVariableModel.setCreationDate(date);
//			wfmConditionVariableModel.setCreatedBy(queryFormDto.getUserId());
//			wfmConditionVariableModel.setIsCompoundKey("0");
//			
//			wfmConditionVariableList.add(wfmConditionVariableModel);
//		}
//		
//		wfmConditionVariableRepo.saveAll(wfmConditionVariableList);
//
//		
//		techRuleCondition.setConditionId(wfmConditionModel.getConditionId());		
//		techRuleCondition.setRuleId(queryFormDto.getRuleCode());
//		
//		techRuleConditionRepo.save(techRuleCondition);
//		//system.out.println("CONDITION ID>>>>>>>>>"+wfmConditionModel.getConditionId());
//		callprodWfm(wfmConditionModel.getConditionId(),queryFormDto.getUserId(),queryFormDto.getRuleCode());
//	
//		
//		
//		return 0;
//		}catch(Exception ex) {
//			return 1;
//		}
//	}

	public void callprodWfm(long i,int j,int z) {
		//system.out.println("CONDID>>>>>>>>>>>"+i);
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SSDX_ENG.P_TRIGGER_DEFINITION")
				.registerStoredProcedureParameter("CONDID", long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("USERID", int.class, ParameterMode.IN)
				.registerStoredProcedureParameter("RULEID", int.class, ParameterMode.IN);

		storedProcedure.setParameter("CONDID",i);
		storedProcedure.setParameter("USERID", j);
		storedProcedure.setParameter("RULEID", z);

		storedProcedure.execute();
	}
	
	
	
	@Override
	public List<List<ObjectNode>> getQueryForm(long ruleCode) {
		
		long conditionId=techRuleConditionRepo.fetchConditionId(ruleCode);
		
		long qbeId=wfmConditionRepo.fetchQbeIdCond(conditionId);
		
		List<ObjectNode> paramData=getQueryParams(qbeId);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<List<ObjectNode>> allData=new ArrayList();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<ObjectNode> conditionValues=new ArrayList();
		List<ObjectNode> paramKeysValuesTemp= ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT a.condition_Var_Technical_Name,a.condition_Var_Value FROM wfm_condition_variable a where a.condition_id="+conditionId);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<ObjectNode> paramKeysValues=new ArrayList();

		String paramsString=paramData.get(0).get("queryParams").asText();
		//system.out.println("PARAMSTRING>>>>>>>>>>>"+paramsString);
		
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> paramsList = new ArrayList<>();

		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(paramsString);
		
		
			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
				paramsList.add(object);
			}
		
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//system.out.println("PARAMS ARRAY>>>>>>>>>>>"+paramsList);
		//system.out.println("PARAMS KEY VALUE TEMP>>>>>>>>>>>"+paramKeysValuesTemp);

		
		for(int i=0;i<paramKeysValuesTemp.size();i++) {
			for(int j=0;j<paramsList.size();j++) {
				//system.out.println("CONDITONVARTECH>>>>>>>"+paramKeysValuesTemp.get(i).get("CONDITION_VAR_TECHNICAL_NAME").asText().split("var_")[1]);
				//system.out.println("PARAM>>>>>>>>>"+paramsList.get(j).get("paramId").asText());

				if(paramKeysValuesTemp.get(i).get("CONDITION_VAR_TECHNICAL_NAME").asText().split("var_")[1].equals(paramsList.get(j).get("paramId").asText())) {
					ObjectMapper paramMapper = new ObjectMapper();	
			        ObjectNode objectNodeParam = paramMapper.createObjectNode();
			        objectNodeParam.put("CONDITION_VAR_TECHNICAL_NAME", paramKeysValuesTemp.get(i).get("CONDITION_VAR_TECHNICAL_NAME").asText());        
			        objectNodeParam.put("CONDITION_VAR_VALUE", paramKeysValuesTemp.get(i).get("CONDITION_VAR_VALUE").asText());
			        objectNodeParam.put("paramName", paramsList.get(j).get("paramName").asText());
			        paramKeysValues.add(objectNodeParam);
				}
			}
		}
		
		
		ObjectMapper conditionMapper = new ObjectMapper();
	
        ObjectNode objectNode = conditionMapper.createObjectNode();
        
        objectNode.put("conditionId", conditionId);        

        objectNode.put("ruleCode", ruleCode);
        
        objectNode.put("qbeId", qbeId);
        
        conditionValues.add(objectNode);
		
        //system.out.println("paramKeysValyes>>>>>>"+paramKeysValues);
        allData.add(conditionValues);
        allData.add(paramKeysValues);
        
		return allData;
	}
	
	
	@Override
	public String getQbeName(long qbeId) {
		
		return qbeUserQueryRepository.getQbeSpecName(qbeId);
	
	}
	
	@Override
	public int checkIfConditionExists(long ruleCode) {
		return techRuleConditionRepo.checkIfExists(ruleCode);
	}
	
	@Transactional
	@Override
	public int updateQueryForm(QueryFormDto queryFormDto) {
		//List<ObjectNode> del=ObjectToJsonRepository.getJsonNativeQuery(entityManager,"DELETE FROM TECHDBA.TECH_RULE_DATA");

		@SuppressWarnings("unused")
		Date date=new Date();
		
		
		@SuppressWarnings("unused")
		List<WfmConditionVariableModel> wfmConditionVariableList = new ArrayList<>();
		@SuppressWarnings("unused")
		List<TechRuleCondition> techRuleConditionList = new ArrayList<>();

		
		@SuppressWarnings("unused")
		TechRuleCondition techRuleCondition=new TechRuleCondition();
		@SuppressWarnings("unused")
		WfmConditionModel wfmConditionModel=new WfmConditionModel();
		try {

			Optional<WfmConditionActivityCondDModel> condExists=wfmConditionActivityCondDRepo.findById(queryFormDto.getConditionId());
			
			if (condExists.isPresent()) {
				//system.out.println("CONDITION ID>>>>>>"+queryFormDto.getConditionId());
				wfmConditionActivityCondDRepo.deleteWfmConditionActivityCond(queryFormDto.getConditionId());
				wfmConditionActivityCondDRepo.deleteById(queryFormDto.getConditionId());
			} 

			
			wfmConditionVariableRepo.deleteWfmConditionVariables(queryFormDto.getConditionId());

			wfmConditionRepo.deleteWfmCondition(queryFormDto.getConditionId());
			
			long fetchRuleId=techRuleConditionRepo.fetchRuleMainId(queryFormDto.getRuleCode());
			
			//system.out.println("RULE ID>>>>>>>>>"+fetchRuleId);
			
			techRuleConditionRepo.deleteById(fetchRuleId);
			
		


		CompletableFuture<Integer> returnVal=insertQueryForm(queryFormDto);
		//system.out.println("CompletableFuture integer>>>>>"+returnVal);
		return 0;
		}catch(Exception ex) {
			ex.printStackTrace();
			return 1;
		}
	}

	@Override
	public List<ObjectNode> getColumnsQuery(long queryId) {
		
		byte[] encodedQuery = sqbQueryDetailsRepository.getQueryBlob(queryId);
		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<ObjectNode> linkTypes=new ArrayList();
		//system.out.println("linkTypes >>>>>>>>>>>>>>>>>> : " + linkTypes);
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,
				decodedQuery.indexOf("</sql>") - 3);
		
		query = query.substring(query.indexOf("[") + 1);
		//system.out.println("query >>>>>>>>>>>>>>>>>> : " + query);
		query = query.substring(0, query.indexOf("]'"));

		query="["+query+"]";
		
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			//system.out.println("1111111111111111: ");

			jsonArray = objMapper.readTree(query);
		
			//system.out.println("22222222222222 ");

			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
			    list.add(object);
			}
			//system.out.println("33333333333333 >>>>>> :"+list);

			
        for (ObjectNode node : list) {
        	//system.out.println("-------->"+list.toString());
            if (node.has("isLink") && node.has("menuId")) {
            	//system.out.println("-------->"+node.get("colName").asText());
                ObjectNode newNode = objMapper.createObjectNode();
                newNode.put("name", node.get("colName").asText());
                newNode.put("menus", node.get("menuId").asText());
                newNode.put("isLink", node.get("isLink").asText());
                newNode.put("columnDescription", node.get("colName").asText());
            	linkTypes.add(newNode);
            }
        	//system.out.println("4444444444444444");

        }
		return linkTypes;
		}catch(Exception ex) {
			return null;
		}
	}
	
	@Override
	public String getQueryFormButton(long objectId) {		
		return queryFormButtonRepo.getButtonList(objectId);
	}
	
	@Override
	public List<ObjectNode> getQueryFormButtonJSON(long objectId) {
		String buttonJSONString=queryFormButtonRepo.getButtonListJSON(objectId);
		String[] buttonJSONArray=buttonJSONString.split(",");
		
		String buttonString="[";
		for(int i=0;i<buttonJSONArray.length;i++) {
			if(i==buttonJSONArray.length-1) {
			buttonString+="{\"id\":\""+buttonJSONArray[i]+"\"}";
			}else {
			buttonString+="{\"id\":\""+buttonJSONArray[i]+"\"},";
			}
		}
		buttonString+="]";		
		
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(buttonString);
		
		
			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
			    list.add(object);
			}
			
        
		return list;
		}catch(Exception ex) {
			return null;
		}

	}
	
	
	
//	
//	SELECT 
//    DISTINCT REGEXP_SUBSTR(REPLACE(REPLACE(REPLACE(button_LIST,'["',''),'"]',''),'","',','), '[^,]+', 1, LEVEL) AS id
//FROM TECHDBA.QUERY_FORMS_BUTTONS WHERE OBJECT_ID=31390
//CONNECT BY REGEXP_SUBSTR(REPLACE(REPLACE(REPLACE(button_LIST,'["',''),'"]',''),'","',','), '[^,]+', 1, LEVEL) IS NOT NULL ORDER BY id;

@Override
public List<ObjectNode> getQueryParams(long queryId) {
byte[] encodedQuery=sqbQueryDetailsRepository.getQueryBlob(queryId);
String decodedQuery=new String(Base64.decodeBase64(encodedQuery));

//String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
//dataStoreId=dataStoreId.split("\"")[1];

//System.out.println("DataStoreId:>>>>"+dataStoreId);		

String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);

System.out.println("Query Name is:>>>>"+queryName);

String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);

System.out.println("Query is>>>>"+query);


String headsString=decodedQuery.split("<LstHeads>")[1];
headsString=headsString.split("</LstHeads>")[0];

System.out.println("Heads String:>>>>>>>"+headsString);

String[] headers=headsString.split("<EltHeads ");

System.out.println("List of Heads>>>"+headers);

String queryHeadsJsonString="[";
String execHeadsJsonString="[";

for(int i =1;i<headers.length;i++)
{
	System.out.println("HEADER>>>"+headers[i]);
	String header = headers[i].split("dbName=\"")[1];
	System.out.println("header "+i+">>>>>>>>>>>"+header);

	header = header.split("\"")[0];
	System.out.println("header "+i+">>>>>>>>>>>"+header);
	
	String field = headers[i].split("fieldType=\"")[1];
	field = field.split("\"")[0];
	System.out.println("field "+i+">>>>>>>>>>>"+field);
	
	queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
	execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

	if(i!=headers.length-1) {
		queryHeadsJsonString+=",";
		execHeadsJsonString+=",";

	}
}
queryHeadsJsonString+="]";
execHeadsJsonString+="]";


String parametersString=decodedQuery.split("<pars>")[1];
parametersString=parametersString.split("</pars>")[0];
String[] parameters=parametersString.split("<par ");
System.out.println("List of Heads>>>"+parameters);



String paramsString="[";

for(int i =1;i<parameters.length;i++)
{
	System.out.println("PARAMETER>>>"+parameters[i]);
	String paramType = parameters[i].split("fieldType=\"")[1];
	System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
	paramType = paramType.split("\"")[0];
	System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
	
	
	
	String paramDefault="1";
	if(paramType.equals("query")) {
		System.out.println("TYPE IS QUERY!!!!!");
	paramDefault=parameters[i].split("subQID=\"")[1];
	paramDefault = paramDefault.split("\"")[0];
	System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
	}
	
	
	String id = parameters[i].split("id=\"")[1];
	id= id.split("\"")[0];
	System.out.println("id "+i+">>>>>>>>>>>"+id);
	
	
	String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));


	System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
	if(paramType.equals("query")) {

		
		paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\",\"paramId\":\""+id+"\"}";

	}else {
		paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\",\"paramId\":\""+id+"\"}";

	}

	query=query.replace("#"+id+"#", "["+paramName+"]");
	
	if(i!=parameters.length-1) {
		paramsString+=",";

	}
}
paramsString+="]";


System.out.println("The Full Query is:>>>>>>"+query);
//		
//		JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
//		JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
//		JSONArray paramAdd=new JSONArray(new String(paramsString));


//        setSessionVal("paramAdd_"+sessionSerial,userId,paramAdd,sessionSerial,2);
//        setSessionVal("queryHeads_"+sessionSerial,userId,queryHeads,sessionSerial,2);
//        setSessionVal("execHeads_"+sessionSerial,userId,execHeads,sessionSerial,2);

List<ObjectNode> queryData=new ArrayList<>();

ObjectMapper objectMapper = new ObjectMapper();
ObjectNode objectNode = objectMapper.createObjectNode();

objectNode.put("query", query);
objectNode.put("queryId", queryId);
objectNode.put("queryParams", paramsString);
objectNode.put("queryName", queryName);
objectNode.put("execHeads",execHeadsJsonString);
queryData.add(objectNode);

return queryData;
}

@Override
public int insertDynamicReportForm(long userId,String reportName,List<DynamicFormGet> dynamicFormGet) {
	long tableId=0;
	String tableString="";
	Date date=new Date();
	long objectId=dynamicFormGet.get(0).getObjectId();
	String primaryCol=dynamicFormGet.get(0).getPrimaryColumn();
	String selectedRowId=dynamicFormGet.get(0).getSelectedRowId().toString();
	
	String tableName=dynamicFormGet.get(0).getDynamicTable().get(0).getTableName();
	System.out.println("TABLE NAME>>>>>>>>"+tableName);
	long orderNo=dynamicFormGet.get(0).getDynamicTable().get(0).getOrderNo();
	List<DynamicFormColsDto> columns=dynamicFormGet.get(0).getDynamicTable().get(0).getColumns();
		
//			System.out.println("TABLE SPLIT 1>>>>>"+tableName.split("\\.")[0]);
//			System.out.println("TABLE SPLIT 2>>>>>"+tableName.split("\\.")[1]);
	List<ObjectNode> orderNoMax=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT MAX(ORDER_NO) as MAXDATA FROM SUITEDBA.CFG_TABLE_OBJECT_REL where object_id="+objectId);
	System.out.println("AAAAAAAAA>>>>>>>>>>>"+orderNoMax);

	for(int i=0;i<orderNoMax.get(0).get("MAXDATA").asInt();i++) {
	

		//tableId=cfgTableObjectRelRepo.fetchTableId(objectId,orderNo);
		
		List<ObjectNode> tableIds=ObjectToJsonRepository.getJson(entityManagerR,"SELECT tableId as tableId FROM TableObjectModel WHERE objectId="+objectId+" AND orderNo ="+orderNo);
		System.out.println("BBBBBBBBBB>>>>>>>>>>>"+tableIds);

		for(int j=0;j<tableIds.size();j++) {
			if(i!=orderNoMax.get(0).get("MAXDATA").asInt()-1) {
				tableString+="'"+tableIds.get(j).get("tableId").asInt()+"',";

			}else {
				tableString+="'"+tableIds.get(j).get("tableId").asInt()+"'";
			}
		}
		
		
	}
	tableString=tableString.replaceAll("''","','");

	//long tableId=iAddedTableRepository.fetchTableId(tableName.split("\\.")[0],tableName.split("\\.")[1]);
	
	List<ObjectNode> colsData=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.columnId as columnId,a.columnName as columnName FROM CfgColumnConfigModel a WHERE a.tableId IN ("+tableString+")");
	
	
	List<ObjectNode> allColsData=new ArrayList<>();   
	ObjectMapper objectMapper = new ObjectMapper();
	
	for(int i=0;i<columns.size();i++) {
		for(int j=0;j<colsData.size();j++) {
			System.out.println("colName>>>>>>>>"+columns.get(i).getColName());
			System.out.println("colData>>>>>>>>"+colsData.get(j).get("columnName").toString().replaceAll("\"",""));
			System.out.println("colValue>>>>>>>>"+columns.get(i).getColValue());

			if(columns.get(i).getColName().equals(colsData.get(j).get("columnName").toString().replaceAll("\"","")) && !columns.get(i).getColValue().equals("") && !columns.get(i).getColValue().equals("Invalid date")) {
				 ObjectNode objectNode = objectMapper.createObjectNode();
				 objectNode.put("columnId",  colsData.get(j).get("columnId").asText());
				 objectNode.put("columnName", colsData.get(j).get("columnName").asText());
				 objectNode.put("columnValue", columns.get(i).getColValue());
				 allColsData.add(objectNode);
			}
		}
	}
	
	System.out.println("ALLCOLSDATA>>>>>>>>>"+allColsData);
	
	CfgReportDynamicConfigModel cfgReportDynamicConfigModel=new CfgReportDynamicConfigModel();
	
	cfgReportDynamicConfigModel.setReportName(reportName);
	cfgReportDynamicConfigModel.setExecutionDate(date);
	cfgReportDynamicConfigModel.setCreationDate(date);
	cfgReportDynamicConfigModel.setIsSystemGenerated("1");
	cfgReportDynamicConfigModel.setCreatedBy(userId);
	//cfgReportDynamicConfigModel.setBpId(null);

	cfgReportDynamicConfigRepo.save(cfgReportDynamicConfigModel);
	
	List<CfgReportColumnFilterModel> allFilters=new ArrayList<>();
	//System.out.println("ALL");
	for(int i=0;i<allColsData.size();i++) {
		CfgReportColumnFilterModel cfgReportColumnFilterModel=new CfgReportColumnFilterModel();
		
		cfgReportColumnFilterModel.setReportDynamicConfigId(cfgReportDynamicConfigModel.getReportDynamicConfigId());
		cfgReportColumnFilterModel.setColumnId(allColsData.get(i).get("columnId").asLong());
		cfgReportColumnFilterModel.setConditionValue(allColsData.get(i).get("columnValue").toString());
		cfgReportColumnFilterModel.setCreationDate(date);
		cfgReportColumnFilterModel.setCreatedBy(userId);
		cfgReportColumnFilterModel.setTechIsDynamicDate("0");

		allFilters.add(cfgReportColumnFilterModel);
	}
	
	cfgReportColumnFilterRepo.saveAll(allFilters);
	
	return (int) cfgReportDynamicConfigModel.getReportDynamicConfigId();
}


@Transactional
@Override
public int updateDynamicReportForm(long userId,long reportId,List<DynamicFormGet> dynamicFormGet) {
	@SuppressWarnings("unused")
	long tableId=0;
	String tableString="";
	Date date=new Date();
	long objectId=dynamicFormGet.get(0).getObjectId();
	@SuppressWarnings("unused")
	String primaryCol=dynamicFormGet.get(0).getPrimaryColumn();
	@SuppressWarnings("unused")
	String selectedRowId=dynamicFormGet.get(0).getSelectedRowId().toString();
	
	String tableName=dynamicFormGet.get(0).getDynamicTable().get(0).getTableName();
	long orderNo=dynamicFormGet.get(0).getDynamicTable().get(0).getOrderNo();
	List<DynamicFormColsDto> columns=dynamicFormGet.get(0).getDynamicTable().get(0).getColumns();
	
	System.out.println("TABLE NAME>>>>>>>>>"+tableName);
	
	
	List<ObjectNode> orderNoMax=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT MAX(ORDER_NO) as MAXDATA FROM SUITEDBA.CFG_TABLE_OBJECT_REL where object_id="+objectId);
	System.out.println("AAAAAAAAA>>>>>>>>>>>"+orderNoMax);

	for(int i=0;i<orderNoMax.get(0).get("MAXDATA").asInt();i++) {
		
		List<ObjectNode> tableIds=ObjectToJsonRepository.getJson(entityManagerR,"SELECT tableId as tableId FROM TableObjectModel WHERE objectId="+objectId+" AND orderNo ="+orderNo);
		System.out.println("BBBBBBBBBB>>>>>>>>>>>"+tableIds);

		for(int j=0;j<tableIds.size();j++) {
			if(i!=orderNoMax.get(0).get("MAXDATA").asInt()-1) {
				tableString+="'"+tableIds.get(j).get("tableId").asInt()+"',";

			}else {
				tableString+="'"+tableIds.get(j).get("tableId").asInt()+"'";
			}
		}
	}
	tableString=tableString.replaceAll("''","','");
	
//			long tableId=cfgTableObjectRelRepo.fetchTableId(objectId);
//
//			//long tableId=iAddedTableRepository.fetchTableId(tableName.split(".")[0],tableName.split(".")[1]);
//			
	List<ObjectNode> colsData=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.columnId as columnId,a.columnName as columnName FROM CfgColumnConfigModel a WHERE a.tableId IN ("+tableString+")");
//			
	
	List<ObjectNode> allColsData=new ArrayList<>();   
	ObjectMapper objectMapper = new ObjectMapper();
	
	for(int i=0;i<columns.size();i++) {
		for(int j=0;j<colsData.size();j++) {
			System.out.println("colName>>>>>>>>"+columns.get(i).getColName());
			System.out.println("colData>>>>>>>>"+colsData.get(j).get("columnName").toString().replaceAll("\"",""));
			System.out.println("colValue>>>>>>>>"+columns.get(i).getColValue());

			if(columns.get(i).getColName().equals(colsData.get(j).get("columnName").toString().replaceAll("\"","")) && !columns.get(i).getColValue().equals("") && !columns.get(i).getColValue().equals("Invalid date")) {
				 ObjectNode objectNode = objectMapper.createObjectNode();
				 objectNode.put("columnId",  colsData.get(j).get("columnId").asText());
				 objectNode.put("columnName", colsData.get(j).get("columnName").asText());
				 objectNode.put("columnValue", columns.get(i).getColValue());
				 allColsData.add(objectNode);
			}
		}
	}
	
	System.out.println("ALLCOLSDATA>>>>>>>>>"+allColsData);
	
	List<CfgReportColumnFilterModel> allFilters=new ArrayList<>();
	
	for(int i=0;i<allColsData.size();i++) {
		
		int filterExists=cfgReportColumnFilterRepo.checkIfFilterExists(reportId,allColsData.get(i).get("columnId").asLong());
		if(filterExists==0) {
		CfgReportColumnFilterModel cfgReportColumnFilterModel=new CfgReportColumnFilterModel();
		
		cfgReportColumnFilterModel.setReportDynamicConfigId(reportId);
		cfgReportColumnFilterModel.setColumnId(allColsData.get(i).get("columnId").asLong());
		cfgReportColumnFilterModel.setConditionValue(allColsData.get(i).get("columnValue").toString());
		cfgReportColumnFilterModel.setCreationDate(date);
		cfgReportColumnFilterModel.setCreatedBy(userId);
		
		allFilters.add(cfgReportColumnFilterModel);
		}else {
		
			cfgReportColumnFilterRepo.updateColumnFilter(reportId,allColsData.get(i).get("columnId").asLong(),allColsData.get(i).get("columnValue").toString());
			
		}
		}
	
	cfgReportColumnFilterRepo.saveAll(allFilters);
	
	return 0;
}






@Override
public CustomResponse JasonBuilder(String Jbuilder) throws RemoteException {
	return null;
}		

@Override
public int insertDynamicReportGrid(long userId,String reportName,String selectedRows) {
	CfgReportDynamicConfigModel cfgReportDynamicConfigModel=new CfgReportDynamicConfigModel();
	Date date=new Date();

	cfgReportDynamicConfigModel.setReportName(reportName);
	cfgReportDynamicConfigModel.setExecutionDate(date);
	cfgReportDynamicConfigModel.setCreationDate(date);
	cfgReportDynamicConfigModel.setIsSystemGenerated("1");
	cfgReportDynamicConfigModel.setCreatedBy(userId);
	System.out.println("SELECTED ROWS>>>>>>>>>"+selectedRows);
	cfgReportDynamicConfigRepo.save(cfgReportDynamicConfigModel);
	String[] listOfCols=selectedRows.split(",");
	List<CfgReportColumnOutputModel> allColsOutput=new ArrayList<>();
	
	for(int i=0;i<listOfCols.length;i++) {
		CfgReportColumnOutputModel cfgReportColumnOutputModel=new CfgReportColumnOutputModel();
		
		cfgReportColumnOutputModel.setReportDynamicConfigId(cfgReportDynamicConfigModel.getReportDynamicConfigId());
		cfgReportColumnOutputModel.setColumnId(Long.parseLong(listOfCols[i]));
		cfgReportColumnOutputModel.setCreationDate(date);
		cfgReportColumnOutputModel.setCreatedBy(userId);
		
		allColsOutput.add(cfgReportColumnOutputModel);
	}
	
	cfgReportColumnOutputRepo.saveAll(allColsOutput);
	
	return (int) cfgReportDynamicConfigModel.getReportDynamicConfigId();
}


@Transactional
@Override
public int updateDynamicReportGrid(long userId,long reportId,String selectedRows) {
	Date date=new Date();

	cfgReportColumnOutputRepo.deleteAllByReportId(reportId);
	
	String[] listOfCols=selectedRows.split(",");
	List<CfgReportColumnOutputModel> allColsOutput=new ArrayList<>();
	
	for(int i=0;i<listOfCols.length;i++) {
		CfgReportColumnOutputModel cfgReportColumnOutputModel=new CfgReportColumnOutputModel();
		
		cfgReportColumnOutputModel.setReportDynamicConfigId(reportId);
		cfgReportColumnOutputModel.setColumnId(Long.parseLong(listOfCols[i]));
		cfgReportColumnOutputModel.setCreationDate(date);
		cfgReportColumnOutputModel.setCreatedBy(userId);
		
		allColsOutput.add(cfgReportColumnOutputModel);
	}
	cfgReportColumnOutputRepo.saveAll(allColsOutput);

	return 0;
}

@Override
public List<List<ObjectNode>> getDynamicReportResult(long reportId, List<DynamicFormGet> dynamicFormGet) {
	@SuppressWarnings("unused")
	long tableId=0;
	String tableString="";
	List<List<ObjectNode>> allData=new ArrayList<>();	
	
	@SuppressWarnings("unused")
	Date date=new Date();
	long objectId=dynamicFormGet.get(0).getObjectId();
	@SuppressWarnings("unused")
	String primaryCol=dynamicFormGet.get(0).getPrimaryColumn();
	@SuppressWarnings("unused")
	String selectedRowId=dynamicFormGet.get(0).getSelectedRowId().toString();
	
	String tableName=dynamicFormGet.get(0).getDynamicTable().get(0).getTableName();
	System.out.println("TABLE NAME>>>>>>>>"+tableName);
	long orderNo=dynamicFormGet.get(0).getDynamicTable().get(0).getOrderNo();
	List<DynamicFormColsDto> columns=dynamicFormGet.get(0).getDynamicTable().get(0).getColumns();

	
List<ObjectNode> orderNoMax=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT MAX(ORDER_NO) as MAXDATA FROM SUITEDBA.CFG_TABLE_OBJECT_REL where object_id="+objectId);
	
System.out.println("AAAAAAAAA>>>>>>>>>>>"+orderNoMax);
	for(int i=0;i<orderNoMax.get(0).get("MAXDATA").asInt();i++) {
		
		List<ObjectNode> tableIds=ObjectToJsonRepository.getJson(entityManagerR,"SELECT tableId as tableId FROM TableObjectModel WHERE objectId="+objectId+" AND orderNo ="+orderNo);
		System.out.println("BBBBBBBBBB>>>>>>>>>>>"+tableIds);

		for(int j=0;j<tableIds.size();j++) {
			if(i!=orderNoMax.get(0).get("MAXDATA").asInt()-1) {
				tableString+="'"+tableIds.get(j).get("tableId").asInt()+"',";

			}else {
				tableString+="'"+tableIds.get(j).get("tableId").asInt()+"'";
			}
		}
	}
	tableString=tableString.replaceAll("''","','");

	//long tableId=cfgTableObjectRelRepo.fetchTableId(objectId);
				
	List<ObjectNode> colsData=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.columnId as columnId,a.columnName as columnName FROM CfgColumnConfigModel a WHERE a.tableId IN ("+tableString+")");
	
	
	List<ObjectNode> allColsData=new ArrayList<>();   
	ObjectMapper objectMapper = new ObjectMapper();
	
	for(int i=0;i<columns.size();i++) {
		for(int j=0;j<colsData.size();j++) {
			System.out.println("colName>>>>>>>>>"+columns.get(i).getColName());
			System.out.println("colData>>>>>>>>>"+colsData.get(j).get("columnName").toString().replaceAll("\"",""));
			System.out.println("colValue>>>>>>>>"+columns.get(i).getColValue());

			if(columns.get(i).getColName().equals(colsData.get(j).get("columnName").toString().replaceAll("\"","")) && !columns.get(i).getColValue().equals("") && !columns.get(i).getColValue().equals("Invalid date")) {
				 ObjectNode objectNode = objectMapper.createObjectNode();
				 objectNode.put("columnId",  colsData.get(j).get("columnId").asText());
				 objectNode.put("columnName", colsData.get(j).get("columnName").asText());
				 objectNode.put("columnValue", columns.get(i).getColValue());
				 allColsData.add(objectNode);
			}
		}
	}
	

	
	String previewQuery="SELECT ";
	
	System.out.println("REPORT ID>>>>>>"+reportId);
//	try {
	List<ObjectNode> allOutputColsIds=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT LISTAGG(column_id, ',') WITHIN GROUP (ORDER BY column_id) AS columnString FROM CFG_report_column_output where report_dynamic_config_id="+reportId);
//	List<ObjectNode> allOutputColsName=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT LISTAGG(column_name, ',') WITHIN GROUP (ORDER BY column_name) AS columnNameString FROM CFG_COLUMN_CONFIG where column_id IN("+allOutputColsIds.get(0).get("columnString").toString()+")");

//	}catch(Exception ex) {
	
	System.out.println("allOutputColsIds>>>>>>>>>>>>>>>"+allOutputColsIds.get(0).get("COLUMNSTRING"));
	
	List<ObjectNode> allOutputColsName=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT LISTAGG(column_name, ',') WITHIN GROUP (ORDER BY column_name) AS columnNameString FROM CFG_COLUMN_CONFIG where column_id IN("+allOutputColsIds.get(0).get("COLUMNSTRING").toString().replaceAll("\"","")+")");

	//}
	//System.out.println("allOutputColsIds>>>>>"+allOutputColsIds);
	
	//List<ObjectNode> allOutputColsName=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT LISTAGG(column_name, ',') WITHIN GROUP (ORDER BY column_name) AS columnNameString FROM CFG_COLUMN_CONFIG where column_id IN("+allOutputColsIds.get(0).get("columnString").toString()+")");

	List<ObjectNode> headerResult=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,"SELECT column_name as headerName,column_name as field FROM CFG_COLUMN_CONFIG where column_id IN("+allOutputColsIds.get(0).get("COLUMNSTRING").toString().replaceAll("\"","")+")");
	System.out.println("headerResult>>>>>>>>>>>>>>>"+headerResult);

	allData.add(headerResult);
	
	String allOutputColsNamesString=allOutputColsName.get(0).get("COLUMNNAMESTRING").toString().replaceAll("\"","");
	

	previewQuery+=allOutputColsNamesString+" FROM "+tableName;
	
	if(!allColsData.isEmpty()) {
		previewQuery+=" WHERE ";
		
		for(int i=0;i<allColsData.size();i++) {
			if(i==allColsData.size()-1) {
				previewQuery+=allColsData.get(i).get("columnName").toString().replaceAll("\"","")+"= '"+allColsData.get(i).get("columnValue").toString().replaceAll("\"","")+"'";
			}else {
				previewQuery+=allColsData.get(i).get("columnName").toString().replaceAll("\"","")+"='"+allColsData.get(i).get("columnValue").toString().replaceAll("\"","")+"' AND ";
			}
		}
	}
	
	List<ObjectNode> dataResult=ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,previewQuery);
	
	allData.add(dataResult);
	
	System.out.println("ALL DATA>>>>>>"+allData);

	return allData;
}

@Override
public List<ObjectNode> getDynamicReportData(long reportId) {

	List<ObjectNode> colIdColValArray=ObjectToJsonRepository.getJson(entityManagerR,"SELECT columnId as columnId,conditionValue as columnVal FROM CfgReportColumnFilterModel WHERE reportDynamicConfigId="+reportId);
	String colIds="";
	
	for(int i=0;i<colIdColValArray.size();i++) {
		if(i!=colIdColValArray.size()-1) {
			colIds+="'"+colIdColValArray.get(i).get("columnId").toString().replaceAll("\"","")+"',";
		}else{
			colIds+="'"+colIdColValArray.get(i).get("columnId").toString().replaceAll("\"","")+"'";
		}			
	}
	System.out.println("COLUMNS 1>>>>>>"+colIds);
	
	List<ObjectNode> colsData=ObjectToJsonRepository.getJson(entityManagerR,"SELECT C.columnId as columnId,C.columnName as columnName,(SELECT lower(name) FROM Syslines WHERE heaCode = 426 AND id = C.columnType) as columnType FROM CfgColumnConfigModel C WHERE C.columnId IN ("+colIds+")");

	System.out.println("COLUMNS 2>>>>>>"+colsData);

	List<ObjectNode> finalColRes= new ArrayList<>();
	 ObjectMapper objectMapper = new ObjectMapper();

	for(int i=0;i<colsData.size();i++) {
		for(int j=0;j<colIdColValArray.size();j++) {
			if(colIdColValArray.get(j).get("columnId").equals(colsData.get(i).get("columnId"))) {
					
				ObjectNode objectNode = objectMapper.createObjectNode();
				objectNode.put("colName",  colsData.get(j).get("columnName").asText());
				objectNode.put("colType", colsData.get(j).get("columnType").asText());
				objectNode.put("colValue", colIdColValArray.get(i).get("columnVal").asText().replaceAll("\"",""));
				finalColRes.add(objectNode);		
				
			}
		}
	}
	
	return finalColRes;
}

@Override
public String GetAllCustDocumnet(long customer_id) {
	
	
	String colIdColValArray="   SELECT \r\n" + 
			"       M.MEDIA_NAME AS DOC_DESC,\r\n" + 
			"     CAST(SUBSTR(to_clob(X.MEDIA_FILE), 1, INSTR(to_clob(X.MEDIA_FILE), ',', 1)-1) AS VARCHAR2(4000)) AS DOC_NAME, \r\n" + 
			"       (CASE\r\n" + 
			"         WHEN A.IS_DOC_REQUIRED = 0 THEN\r\n" + 
			"          'No'\r\n" + 
			"         WHEN A.IS_DOC_REQUIRED = 1 THEN\r\n" + 
			"          'Yes'\r\n" + 
			"       END) AS Is_Mandatory,\r\n" + 
			"       X.MEDIA_BDATE AS START_DATE,\r\n" + 
			"       X.MEDIA_EDATE AS EXPIRY_DATE\r\n" + 
			"  FROM SDEDBA.REF_COM_DOCUMENT_CONFIG A,\r\n" + 
			"       SDEDBA.REF_COM_MEDIA           M,\r\n" + 
			"       VLPDBA.REF_CUSTOMER_MEDIA      X,\r\n" + 
			"       VLPDBA.REF_CUSTOMER            D,\r\n" + 
			"       VLPDBA.REF_CUSTOMER_MISC_INFO C\r\n" + 
			" WHERE M.MEDIA_ID = A.TECH_MEDIA_ID\r\n" + 
			"   AND X.MEDIA_ID = M.MEDIA_ID\r\n" + 
			"   \r\n" + 
			"   AND A.COU_ID =\r\n" + 
			"       (SELECT C.COU_ID\r\n" + 
			"          FROM MDMDBA.MDM_BSN_ASSOCIATED_UNIT_GRP K,\r\n" + 
			"               MDMDBA.MDM_BSN_UNIT_GROUP          G1,\r\n" + 
			"               MDMDBA.MDM_BSN_UNIT_GROUP          G2,\r\n" + 
			"               MDMDBA.MDM_BUSINESS_UNIT           U1,\r\n" + 
			"               MDMDBA.MDM_BSN_UNIT_GROUP_STRUC    U2,\r\n" + 
			"               MDMDBA.MDM_BRANCH                  B,\r\n" + 
			"               SDEDBA.REF_COM_COUNTRY             C\r\n" + 
			"         WHERE K.BSN_GROUP_P_ID = G1.BSN_GROUP_ID\r\n" + 
			"           AND K.BSN_GROUP_C_ID = G2.BSN_GROUP_ID\r\n" + 
			"           AND U1.BSN_GROUP_ID = G2.BSN_GROUP_ID\r\n" + 
			"           AND U2.BSN_GROUP_ID = G1.BSN_GROUP_ID\r\n" + 
			"           AND B.BRANCH_ID = U1.ENT_CODE\r\n" + 
			"           AND C.BSN_GROUP_ID = U2.BSN_GROUP_ID\r\n" + 
			"           AND U1.BSN_UNIT_TYPE_CODE = 56\r\n" + 
			"           AND U2.BSN_GROUP_TYPE_CODE = 2\r\n" + 
			"           AND D.CUSTOMER_ID = X.CUSTOMER_ID\r\n" + 
			"           AND U1.BSN_GROUP_ID =\r\n" + 
			"               (SELECT U1.BSN_GROUP_ID\r\n" + 
			"                  FROM MDM_BRANCH B, MDM_BUSINESS_UNIT U1\r\n" + 
			"                 WHERE B.BRANCH_ID = U1.ENT_CODE\r\n" + 
			"                   AND U1.BSN_GROUP_ID =\r\n" + 
			"                       (SELECT MI.BRANCH_BSN_GROUP_ID\r\n" + 
			"                          FROM VLPDBA.REF_CUSTOMER_MISC_INFO MI\r\n" + 
			"                         WHERE MI.CUSTOMER_ID = X.CUSTOMER_ID)))\r\n" + 
			" AND C.CUSTOMER_ID=x.customer_id\r\n" + 
			"AND d.party_type_code = a.party_type_code\r\n" + 
			"AND d.party_type_code = 7\r\n" + 
			" AND X.CUSTOMER_ID = " + customer_id;

	 @SuppressWarnings("unchecked")
	List<Object[]> resultList =  entityManagerR.createNativeQuery(colIdColValArray).getResultList();
	 
	 System.out.println("resultList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " + resultList  ) ;
	 
		for (Object[] row : resultList) {
			System.out.println("charbel >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> : " + row);
		}
	
	return null;
}

@Override
		public CustomResponse GetScheduleApi(long activity_id) throws RemoteException{
			//system.out.println("activity_id in implementation ---------- " + activity_id);
	         TestWebServiceImplService locator = new TestWebServiceImplServiceLocator();
	         try {
				TestWebServiceImpl service = locator.getTestWebServiceWS();
				

				//system.out.println("activity_id nadine >>>>>>>>> " + activity_id);
				String params = ""+activity_id +",null"; 
				//system.out.println("params nadine >>>>>>>> " +params);
				service.startScheduledAct(params); 
				
			} catch (ServiceException | javax.xml.rpc.ServiceException e) {
				e.printStackTrace();
			}

			return CustomResponse.builder().code("0").build();
		}

@Override
		public CustomResponse GetRunApi(String ruleId) throws RemoteException {
			
	        String jsonString = ruleId;
	        JSONArray jsonArray = new JSONArray(jsonString);
	        String targetValue = null;
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject obj = jsonArray.getJSONObject(i);
	            if (obj.getString("TYPE").equals("GRID") &&
	                obj.getString("COLNAME").equals("BUSINESS_RULE_ID")) {
	                targetValue = obj.getString("COLVALUE");
	                //system.out.println("rule id 1111111<<<<<<<<<<<<<<<<< " + targetValue);
	                break;
	            }
	        }
	        
	        
	        //system.out.println("rule id 22222222222 <<<<<<<<<<<<<<<<< " + targetValue);
	        
	        try {
	            TestWebServiceImplService locator = new TestWebServiceImplServiceLocator();
	            TestWebServiceImpl service = locator.getTestWebServiceWS();

	            //system.out.println("111111111111222222222");
	            //system.out.println("rule id <<<<<<<<<<<<<<<<< " + targetValue);
	            
	            // Call the web service method with the extracted targetValue
	            service.executeRule(targetValue); 

	        } catch (ServiceException e) {
	            e.printStackTrace();
	        } catch (javax.xml.rpc.ServiceException e) {
				e.printStackTrace();
			}


			return CustomResponse.builder().code("0").build();
		}

@Override
public List<ObjectNode> getApiColumnName(String columnsIdsString) {
	
	return ObjectToJsonRepository.getJson(entityManagerR, "SELECT a.columnName as columnName from CfgColumnConfigModel a where a.columnId IN ("+columnsIdsString+")");
}


//		public List<ObjectNode> getDynReports(){
//			return ObjectToJsonRepository.getJson(entityManager,"SELECT r.reportDynamicConfigId as reportDynamicConfigId "
//					+"    ,r.reportDynamicConfigPId as reportDynamicConfigPId "
//				    		  +"    ,r.reportInternalCode as  reportInternalCode "
//				    		  +"    ,r.reportName as reportName "
//				    		  +"     ,r.executionDate as  executionDate "
//				    		  +"     ,r.isSystemGenerated as  isSystemGenerated "
//				    		  +"     ,(case when isSystemGenerated = '1' and reportDynamicConfigPId is null then '1' else '0' end) as isTemplate "
//				    		  +"     ,r.bpId as bpId "
//				    		  +"      ,(SELECT concat(concat(fname,' '),name) "
//				    		  +"         FROM EmployeeCommon "
//				          +"        WHERE employeeId = r.executedBy) as executedBy "
//				       
//				 +" FROM DynamicReportModel r");
//		          
	
//			  +"      ,(SELECT COUNT(1) "
//		      +"		  FROM (SELECT reportDynamicConfigPId AS rn "
//						  +"	          FROM DynamicReportModel rc "
//				        		  +"	  WHERE rc.reportDynamicConfigPId = r.reportDynamicConfigId) as t "
//		 +"	 WHERE t.rn = 1 ) as hasChild  "
	
	//return null;
//}

@Override
public List<ObjectNode> exportRules(String ruleIds) {
	try {
		System.out.println("----------------------------------heeeerreeeeeeee----------------------------------");
		System.out.println("ruleIds---------->" + ruleIds);
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.ruleAction as ruleAction,a.ruleDescription as ruleDescription,a.ruleData as ruleData,a.columnId as columnId,a.orderNo as orderNo,a.isExcluded as isExcluded,a.hasAdvancedConditions as hasAdvancedConditions,a.actionType as actionType "
				+"FROM TechDynamicRuleBuilder a "
				+"WHERE a.ruleId IN("+ruleIds+")");
	} catch (Exception ex) {
		System.out.println("An error occurred: " + ex.getMessage());
		return null;
	}
}

@SuppressWarnings("unlikely-arg-type")
@Override
public int importDynamicRules(long objectId, List<ObjectNode> dynamicRulesData) {
  Date date=new Date();
	try {
		List<TechDynamicRuleBuilder> techDynamicRuleBuilderDataList=new ArrayList<>();
		 for(int i=0;i<dynamicRulesData.size();i++) {
	  
			 String ruleDataString=dynamicRulesData.get(i).get("ruleData").asText();
			 
/////////////////MAIN COLUMNID\\\\\\\\\\\\\\\\\\\\\\     		
			 
			 long columnId=dynamicRulesData.get(i).get("columnId").asLong();
	  
			List<ObjectNode> coltableIds=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.tableId as tableId from CfgTableObjectifRelModel a WHERE a.objectId="+objectId);
			String coltableIdsString="";
				
			for(int j=0;j<coltableIds.size();j++) {
					if(j!=coltableIds.size()-1) {
						coltableIdsString+=coltableIds.get(j).get("tableId").asLong()+",";
					}else {
						coltableIdsString+=coltableIds.get(j).get("tableId").asLong();
					}
				}
			
			  System.out.println("COL TABLE IDS STRING>>>>>>>>"+coltableIdsString);
			  
			  String colName=cfgColumnConfigRepository.findColNameByColumnId(columnId);
			  
			  List<ObjectNode> coltableNames=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.columnId as columnId,a.columnName as columnName FROM CfgColumnConfigModel a WHERE a.columnId IN ("+coltableIdsString+")");
		 
			  System.out.println("COL TABLE Names list>>>>>>>>"+coltableNames);

			  for(int z=0;z<coltableNames.size();z++) {
				  System.out.println("COL NAME>>>>>>>>>>>"+colName);	
				  System.out.println("colName.equals(coltableNa>>>>>>>>>>>"+colName.equals(coltableNames.get(z).get("columnName")));

				  if(colName.equals(coltableNames.get(z).get("columnName").asText())){
					  columnId=coltableNames.get(z).get("columnId").asLong();
				  break;
				  }
	  
				  }
					   
			 ObjectMapper objMapper = new ObjectMapper();
			List<ObjectNode> ruleDataList = new ArrayList<>();
			JsonNode jsonArray;
			try {
				jsonArray = objMapper.readTree(dynamicRulesData.get(i).get("ruleData").asText());

				for (JsonNode element : jsonArray) {
					ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
					ruleDataList.add(object);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			 
			 System.out.println("RULE DATA LIST>>>>"+ruleDataList);
			 
			 for(int q=0;q<ruleDataList.size();q++) {
				 
				 
				 
				 
				 ///////STEP 1\\\\\\\\\\\\\\\
				 if(ruleDataList.get(q).get("step").asLong()==1) {
					 String dataId=ruleDataList.get(q).get("data").asText();
				 
					 List<ObjectNode> coltableIdsStep1=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.tableId as tableId from CfgTableObjectifRelModel a WHERE a.objectId="+objectId);
					  String coltableIdsStringStep1="";
						  
					  for(int j=0;j<coltableIdsStep1.size();j++) {
							  if(j!=coltableIdsStep1.size()-1) {
								  coltableIdsStringStep1+=coltableIdsStep1.get(j).get("tableId").asLong()+",";
							  }else {
								  coltableIdsStringStep1+=coltableIdsStep1.get(j).get("tableId").asLong();
							  }
						  }
					  
						System.out.println("COL TABLE IDS STRING11111>>>>>>>>"+coltableIdsString);
						
						String colNameStep1=cfgColumnConfigRepository.findColNameByColumnId(Long.parseLong(dataId));
						
						List<ObjectNode> coltableNamesStep1=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.columnId as columnId,a.columnName as columnName FROM CfgColumnConfigModel a WHERE a.tableId IN ("+coltableIdsStringStep1+")");
				   
						System.out.println("COL TABLE Names list111111>>>>>>>>"+coltableNamesStep1);

						for(int z=0;z<coltableNamesStep1.size();z++) {
							System.out.println("COL NAME111111>>>>>>>>>>>"+colNameStep1);	
							System.out.println("colName.equals(coltableNa>>>>>>>>>>>1111111"+coltableNamesStep1.get(z).get("columnName"));

							if(colNameStep1.equals(coltableNamesStep1.get(z).get("columnName").asText())){
								  ruleDataString=ruleDataString.replace(dataId,coltableNamesStep1.get(z).get("columnId").asText());

							break;
							}
							
							
							
							}
					 
					 
				 }
				 //////////////STEP 41 + STEP 44\\\\\\\\\\\\
				 if((ruleDataList.get(q).get("step").asLong()==44 || ruleDataList.get(q).get("step").asLong()==41) && !ruleDataList.get(q).get("data").toString().equals("\"\"")) {
					 System.out.println("RULE DATA 44>>>>>>>>>>"+ruleDataList.get(q).get("data").toString());
					 String dataId=ruleDataList.get(q).get("data").toString().replace("[","").replace("]","").replace("\"","");
					 System.out.println("DATA IDSS22222222>>>>>>>>>"+dataId);
					 String [] dataIds = null;
					 if(dataId.contains("id")) {
						  JSONArray idsArray = new JSONArray("["+dataId+"]");
						  String[] ids = new String[idsArray.length()];
						 
						 for (int v = 0;v<idsArray.length(); v++) {
							 JSONObject jsonObject=null;
							 System.out.println("JSON OBJECT>>>>>"+idsArray);
							 try {
								 jsonObject = idsArray.getJSONObject(v);
								 if (jsonObject.has("id")) {
									 ids[v] = "\""+jsonObject.getLong("id")+"\"";
								 }
							 }
							 catch(Exception ex) {
								 System.out.println("NOT A JSON OBJECT");
								 ids[v] = "\""+idsArray.getLong(v)+"\"";

							 }
							 
						 }
						 dataIds=ids;
					 }
						 else {
						 dataIds=dataId.split(",");

					 }
					 
					 
					 for(int k=0;k<dataIds.length;k++) {
					 
					 String dataIdStep44=dataIds[k];
					 System.out.println("DATA STEP 44>>>>>>>"+dataIdStep44);
					 List<ObjectNode> coltableIdsStep44=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.tableId as tableId from CfgTableObjectifRelModel a WHERE a.objectId="+objectId);
					  String coltableIdsStringStep44="";
						  
					  for(int j=0;j<coltableIdsStep44.size();j++) {
							  if(j!=coltableIdsStep44.size()-1) {
								  coltableIdsStringStep44+=coltableIdsStep44.get(j).get("tableId").asLong()+",";
							  }else {
								  coltableIdsStringStep44+=coltableIdsStep44.get(j).get("tableId").asLong();
							  }
						  }
					  
						System.out.println("COL TABLE IDS STRING22222222>>>>>>>"+coltableIdsStringStep44);
						
						String colNameStep44=cfgColumnConfigRepository.findColNameByColumnId(Long.parseLong(dataIdStep44.replace("\"","")));
						
						List<ObjectNode> coltableNamesStep44=ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.columnId as columnId,a.columnName as columnName FROM CfgColumnConfigModel a WHERE a.tableId IN ("+coltableIdsStringStep44+")");
				   
						System.out.println("COL TABLE Names list2222222>>>>>>>>"+coltableNamesStep44);

						for(int z=0;z<coltableNamesStep44.size();z++) {
							System.out.println("COL NAME222222222>>>>>>>>>>>"+colNameStep44);	
							System.out.println("colName.equals(coltableNa>>>>>>>>>>>222222222"+coltableNamesStep44.get(z).get("columnName"));

							if(colNameStep44.equals(coltableNamesStep44.get(z).get("columnName").toString())){
								  ruleDataString=ruleDataString.replace(dataIdStep44,coltableNamesStep44.get(z).get("columnId").asText());

							break;
							}
							
							
						}
							}
				
				 }
				 //previousStep=ruleDataList.get(q).get("step");
				 }
			 
  
			 TechDynamicRuleBuilder techDynamicRuleData=new TechDynamicRuleBuilder();
			 techDynamicRuleData.setObjectId(objectId);
			 techDynamicRuleData.setRuleAction(dynamicRulesData.get(i).get("ruleAction").asLong());
			 techDynamicRuleData.setRuleDescription(dynamicRulesData.get(i).get("ruleDescription").asText());
			 //techDynamicRuleData.setRuleData(dynamicRulesData.get(i).get("ruleData").asText());
			 techDynamicRuleData.setColumnId(columnId);
			 techDynamicRuleData.setRuleData(ruleDataString);

			 
			 //techDynamicRuleData.setColumnId(dynamicRulesData.get(i).get("columnId").asLong());
			 techDynamicRuleData.setOrderNo(dynamicRulesData.get(i).get("orderNo").asText());
			 techDynamicRuleData.setIsExcluded(dynamicRulesData.get(i).get("isExcluded").asLong());
			 techDynamicRuleData.setHasAdvancedConditions(dynamicRulesData.get(i).get("hasAdvancedConditions").asLong());
			 techDynamicRuleData.setActionType(dynamicRulesData.get(i).get("actionType").asLong());
			 techDynamicRuleData.setUpdateDate(date);
			 techDynamicRuleData.setCreationDate(date);
			 //techDynamicRuleData.setCreationDate(date);
			 //techDynamicRuleData.setCreationDate(date);

			 techDynamicRuleBuilderDataList.add(techDynamicRuleData);
		 }
		 System.out.println("WOSLEETT >>>>>>>>>>>> "+techDynamicRuleBuilderDataList);
		 techDynamicRuleBuilderRepo.saveAll(techDynamicRuleBuilderDataList);
		
		return 1;
	} catch (Exception ex) {
		ex.printStackTrace();
		return 0;
	}
}


//////// INDISPLAYLOGS//////////////////// 
public void insertInDisplayLogs(String functionName,List<ObjectNode> logNode,long id) {
	
	System.out.println("Log Data : " + logNode);
	System.out.println("FUNCTION NAME>>"+functionName);
	InDisplayLogsModel inDisplayLogsModel=new InDisplayLogsModel();
	
	String tableName="";
	String actionType="";
	Date logDate=new Date();
	long loggedBy=0;
	String loggedByName="";
	String changes="";
	String actionText="";
	
	switch(functionName) {
	case "saveDRBRule":
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Dynamic Rule";
		actionType="Insert";
		actionText="User "+loggedByName+" has saved a new Rule \""+logNode.get(0).get("ruleDescription")+"\" under the \""+objectDefRepository.getObjectName(logNode.get(0).get("objectId").asLong())+"\" tab";
		changes=logNode.toString();
		break;
	case "updateDRBRule":
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Dynamic Rule";
		actionType="Update";
		actionText="User "+loggedByName+" has updated the Rule \""+logNode.get(0).get("ruleDescription")+"\" under the \""+objectDefRepository.getObjectName(logNode.get(0).get("objectId").asLong())+"\" tab";
		changes="OLD:\n"+logNode.get(0).toString()+"\nNEW:\n"+logNode.get(1).toString();
					break;
	case "deleteDRBRule":
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Dynamic Rule";
		actionType="Delete";
		actionText="User "+loggedByName+" has deleted the Rule \""+logNode.get(0).get("ruleDescription")+"\" under the \""+objectDefRepository.getObjectName(logNode.get(0).get("objectId").asLong())+"\" tab";
		changes=logNode.get(0).toString();
					break;
	case "updateGridinsert":
		loggedBy=logNode.get(0).get("createdBy").asLong();
		loggedByName=usmUserMiscInfoRepo.getUserName(loggedBy);
		tableName="Handled Tables";
		actionType="Insert";
		actionText="User "+loggedByName+" has added a new Table \""+logNode.get(0).get("tableName")+"\" under the \""+objectDefRepository.getObjectName(id)+"\" tab";
		changes=logNode.get(0).toString();
					break;
	case "updateGridupdate":
		loggedBy=logNode.get(1).get("updatedBy").asLong();
		loggedByName=usmUserMiscInfoRepo.getUserName(loggedBy);
		tableName="Handled Tables";
		actionType="Update";
		actionText="User "+loggedByName+" has updated the Table \""+logNode.get(1).get("tableName")+"\" under the \""+objectDefRepository.getObjectName(id)+"\" tab";
		changes="OLD:\n"+logNode.get(0).toString()+"\nNEW:\n"+logNode.get(1).toString();
					break;
	case "updateGriddelete":
		loggedBy=logNode.get(0).get("updatedBy").asLong();
		loggedByName=usmUserMiscInfoRepo.getUserName(loggedBy);
		tableName="Handled Tables";
		actionType="Delete";
		actionText="User "+loggedByName+" has deleted the Table \""+logNode.get(0).get("tableName")+"\" under the \""+objectDefRepository.getObjectName(id)+"\" tab";
		changes=logNode.get(0).toString();
					break;
	case "columnModifier":
		long objectId=cfgTableObjectifRelRepository.getObjectId(logNode.get(0).get("tableId").asLong());
		String objectName=objectDefRepository.getObjectName(objectId);
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Column Modified";
		actionType="Update";
		actionText="User "+loggedByName+" has updated the Field \""+logNode.get(0).get("columnName")+"\" under the \""+objectName+"\" tab";
		changes="OLD\n:"+logNode.get(0).toString()+"\nNEW:\n"+logNode.get(1).toString();
					break;
	case "deleteTab":
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Tabs";
		actionType="Delete";
		actionText="User "+loggedByName+" has deleted the Tab \""+logNode.get(0).get("menuName")+", child of "+objectDefRepository.getObjectName(logNode.get(0).get("parentId").asLong());
		changes=logNode.get(0).toString();
					break;
	case "createTab":
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Tabs";
		actionType="Insert";
		actionText="User "+loggedByName+" has created the Tab \""+logNode.get(0).get("menuName")+", child of "+objectDefRepository.getObjectName(logNode.get(0).get("parentId").asLong());
		changes=logNode.get(0).toString();
					break;	
	case "updateTab":
		loggedBy=id;
		loggedByName=usmUserMiscInfoRepo.getUserName(id);
		tableName="Tabs";
		actionType="Update";
		actionText="User "+loggedByName+" has updated the Tab \""+logNode.get(0).get("menuName")+", child of "+objectDefRepository.getObjectName(logNode.get(0).get("parentId").asLong());
		changes="OLD:\n"+logNode.get(0).toString()+"\nNEW:\n"+logNode.get(1).toString();
					break;	
	case "createFormButton":
		loggedBy=logNode.get(0).get("createdBy").asLong();
		loggedByName=usmUserMiscInfoRepo.getUserName(loggedBy);
		tableName="Button Created";
		actionType="Insert";
		actionText="User "+loggedByName+" has added a new Button \""+logNode.get(0).get("columnName")+"\" under the \""+objectDefRepository.getObjectName(id)+"\" tab";
		changes=logNode.get(0).toString();
					break;
	case "updateFormButton":
		loggedBy=logNode.get(0).get("updatedBy").asLong();
		loggedByName=usmUserMiscInfoRepo.getUserName(loggedBy);
		tableName="Button Updated";
		actionType="Update";
		actionText="User "+loggedByName+" has updated the Button \""+logNode.get(0).get("buttonName")+"\" under the \""+objectDefRepository.getObjectName(id)+"\" tab";
		changes=logNode.get(0).toString();
					break;
	case "deleteButton":
		loggedBy=logNode.get(0).get("updatedBy").asLong();
		loggedByName=usmUserMiscInfoRepo.getUserName(loggedBy);
		tableName="Button Deleted";
		actionType="Delete";
		actionText="User "+loggedByName+" has deleted the Button \""+logNode.get(0).get("columnName")+"\" under the \""+objectDefRepository.getObjectName(id)+"\" tab";
		changes=logNode.get(0).toString();
					break;
	}
	
	inDisplayLogsModel.setTableName(tableName);
	inDisplayLogsModel.setActionType(actionType);
	inDisplayLogsModel.setLogDate(logDate);
	inDisplayLogsModel.setLoggedBy(loggedBy);
	inDisplayLogsModel.setChanges(changes);
	inDisplayLogsModel.setActionText(actionText);
	inDisplayLogsRepo.save(inDisplayLogsModel);
}
}


