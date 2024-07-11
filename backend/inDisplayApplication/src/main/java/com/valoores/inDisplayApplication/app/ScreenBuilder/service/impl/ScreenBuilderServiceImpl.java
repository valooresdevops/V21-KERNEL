package com.valoores.inDisplayApplication.app.ScreenBuilder.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.ScreenBuilder.dto.ScreenDto;
import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ScreenMenuModel;
import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ScreenObjectModel;
import com.valoores.inDisplayApplication.app.ScreenBuilder.repository.ApplicationRepo;
import com.valoores.inDisplayApplication.app.ScreenBuilder.repository.ScreenMenuRepo;
import com.valoores.inDisplayApplication.app.ScreenBuilder.repository.ScreenObjectRepo;
import com.valoores.inDisplayApplication.app.ScreenBuilder.repository.ScreenUserAccessRepo;
import com.valoores.inDisplayApplication.app.ScreenBuilder.service.ScreenBuilderService;
import com.valoores.inDisplayApplication.backend.CustomResponse;

@Service
public class ScreenBuilderServiceImpl implements ScreenBuilderService {

	@Autowired
	private EntityManager entityManagerR;

	@Resource
	private ScreenObjectRepo screenObjectRepo;
	@Resource
	private ScreenMenuRepo screenMenuRepo;
	@Resource
	private ApplicationRepo applicationRepo;

	@Resource
	private ScreenUserAccessRepo screenUserAccessRepo;

	@SuppressWarnings("deprecation")
	public static List<ObjectNode> getJson(EntityManager entityManagerR, String query) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		entityManagerR.createQuery(query).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {

						ObjectNode node = mapper.createObjectNode();
						for (int i = 0; i < tuple.length; i++) {
							if (tuple[i] != null) {

								node.put(aliases[i], tuple[i].toString());
							}
						}
						json.add(node);
						return json;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public List transformList(List tuples) {
						return tuples;
					}
				}).getResultList();
		return json;

	}

	@SuppressWarnings("deprecation")
	public static List<ObjectNode> getJsonNativeQuery(EntityManager entityManagerR, String query) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		entityManagerR.createNativeQuery(query).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {

						ObjectNode node = mapper.createObjectNode();
						for (int i = 0; i < tuple.length; i++) {
							if (tuple[i] != null) {
								node.put(aliases[i], tuple[i].toString());
							}
						}
						json.add(node);
						return json;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public List transformList(List tuples) {
						return tuples;
					}
				}).getResultList();
		return json;
	}

	public static String decodeBase64(String value) {

		try {
			return new String(java.util.Base64.getDecoder().decode(value));
		} catch (IllegalArgumentException ex) {
			System.out.println("ERROR decoding string '" + value + "':::::::: not a valid Base64 encoded string.");
			throw new RuntimeException(ex);
		}

	}

	@Override
	public List<ObjectNode> fetchGridsTableData() {
		List<ObjectNode> query1 = getJson(entityManagerR,
				" SELECT cd.OBJECT_ID as OBJECT_ID, cd.OBJECT_NAME AS OBJECT_NAME , cd.REPORT_ID AS PARENT_ID, tbl.OBJECT_NAME AS PARENT_NAME "
						+ "FROM TableGridDataModel cd, TableGridDataModel tbl "
						+ "WHERE cd.OBJECT_TYPE IN (1, 17) AND cd.REPORT_ID = tbl.OBJECT_ID");

		List<ObjectNode> query2 = getJson(entityManagerR,
				"SELECT cd.OBJECT_ID AS OBJECT_ID, cd.OBJECT_NAME AS OBJECT_NAME, cd.REPORT_ID AS PARENT_ID, '' AS PARENT_NAME "
						+ "    FROM TableGridDataModel cd "
						+ "    WHERE cd.OBJECT_TYPE IN (1, 17) AND cd.REPORT_ID IS NULL");

		query1.addAll(query2);

		return query1;
	}

	@Override
	public List<ObjectNode> fetchApplicationList() {

		return getJson(entityManagerR,
				" SELECT a.APP_CODE as code,a.APP_NAME as application FROM ApplicationListModel a WHERE a.APP_MANAGED='1'");
	}

	@Override
	public List<ObjectNode> fetchParentMenuList(String menuCode) {
		if (menuCode.length() == 1) {
			return getJson(entityManagerR,
					" SELECT a.MENU_CODE as menu_code,a.MENU_NAME as menu_name FROM ParentMenuListModel a WHERE (a.MENU_P_CODE LIKE '00"
							+ menuCode + "%' OR a.MENU_CODE LIKE '00" + menuCode + "%') AND a.MENU_TYPE IN (1,8)");
		} else {
			return getJson(entityManagerR,
					" SELECT a.MENU_CODE as menu_code,a.MENU_NAME as menu_name FROM ParentMenuListModel a WHERE (a.MENU_P_CODE LIKE '0"
							+ menuCode + "%' OR a.MENU_CODE LIKE '0" + menuCode + "%') AND a.MENU_TYPE IN (1,8)");

		}
	}

	@Override
	public int addScreen(ScreenDto screenDto) {

		try {
			ScreenObjectModel sbomodel = new ScreenObjectModel();
			ScreenMenuModel sbmmodel = new ScreenMenuModel();
			Date date = new Date();
			String code = screenDto.getParentMenuId();
			String NextAction = screenDto.getNextAction();

			String menuCode1 = screenMenuRepo.getNewMenuCode(code);
			System.out.println("mennnnnnuuu>>>>>"+code);
			if(menuCode1==null) {
				 menuCode1=code;
			}
	        StringBuilder stringBuilder = new StringBuilder(menuCode1);
	        
	        char lastChar = stringBuilder.charAt(stringBuilder.length() - 1);
	        int lastDigit = Character.getNumericValue(lastChar);
	        int newDigit = lastDigit + 2;
	        stringBuilder.setCharAt(stringBuilder.length() - 1, Character.forDigit(newDigit, 10));
	        String menuCode = stringBuilder.toString();
	        System.out.println("menuCode>>>>>>>>>"+menuCode);
			if (menuCode != null && menuCode!=code) {
				menuCode = "0"+(Long.parseLong(menuCode1)+1);

				//menuCode = "0" + menuCode;
			} else {
				menuCode = screenDto.getParentMenuId() + "001";
			}
			sbmmodel.setMENU_CODE(menuCode);
			sbmmodel.setMENU_TYPE(1);
			sbmmodel.setMENU_P_CODE(screenDto.getParentMenuId());
			sbmmodel.setMENU_NAME(screenDto.getScreenName());
			sbmmodel.setMENU_VARIABLE("ScreenBuilder_" + menuCode + "_SB");
			sbmmodel.setMenuIconDesc("fas fa-circle fa-2xs");
			sbmmodel.setCREATION_DATE(date);
			sbmmodel.setCREATED_BY(screenDto.getUserId());
			if (screenDto.getIsSuspended().equals("Suspended")) {
				sbmmodel.setMENU_MANAGED("0");
			} else {
				sbmmodel.setMENU_MANAGED("1");
			}

			if (screenDto.getNextAction().equals("nextAction")) {
				sbmmodel.setMENU_TYPE(8);
			} else {
				sbmmodel.setMENU_TYPE(1);
			}
			screenMenuRepo.save(sbmmodel);

			sbomodel.setOBJECT_NAME(screenDto.getScreenName());
			sbomodel.setOBJECT_TYPE(16);
			sbomodel.setIS_MAIN(2);
			sbomodel.setCREATED_BY(screenDto.getUserId());
			sbomodel.setCREATION_DATE(date);
			sbomodel.setOBJECT_PARAM(Base64.encodeBase64(screenDto.getData().getBytes()));
			sbomodel.setMENU_CODE(sbmmodel.getMENU_CODE());

			
			
			System.out.println(">>>"+sbomodel);
			screenObjectRepo.save(sbomodel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

//	@Override
//	public List<ObjectNode> getAllScreens() {
//
//		return getJson(entityManagerR, "SELECT c.objectId AS ID,c.OBJECT_NAME as SCREEN_NAME,\r\n"
//				+ "(SELECT m.MENU_NAME\r\n" + " FROM ScreenMenuModel m\r\n"
//				+ "  WHERE m.MENU_CODE=(SELECT n.MENU_P_CODE \r\n" + "                     FROM ScreenMenuModel n \r\n"
//				+ "                     WHERE n.MENU_CODE=c.MENU_CODE )) AS PARENT_MENU,\r\n"
//				+ "(SELECT a.APP_NAME \r\n" + "FROM ApplicationListModel a "
//				+ "WHERE a.APP_CODE=  SUBSTRING(c.MENU_CODE, 1, 3) AS Application,"
//				+ "(SELECT (CASE WHEN v.MENU_MANAGED='1' THEN 'Active' ELSE 'Suspended' END) FROM ScreenMenuModel v WHERE v.MENU_CODE=c.MENU_CODE) AS STATE\r\n"
//				+ "FROM ScreenObjectModel c WHERE OBJECT_TYPE=16 AND IS_MAIN=2\r\n" + "");
//	}
//	
	
	@Override
	public List<ObjectNode> getAllScreens() {
	    return getJson(entityManagerR, 
	        "SELECT c.objectId AS ID, c.OBJECT_NAME AS SCREEN_NAME, " +
	        "   (SELECT m.MENU_NAME FROM ScreenMenuModel m " +
	        "    WHERE m.MENU_CODE = (SELECT n.MENU_P_CODE FROM ScreenMenuModel n " +
	        "                         WHERE n.MENU_CODE = c.MENU_CODE)) AS PARENT_MENU, " +
	        "   (SELECT a.APP_NAME FROM ApplicationListModel a " +
	        "    WHERE a.APP_CODE = CAST(SUBSTRING(c.MENU_CODE, 1, 3) AS integer)) AS Application, " +
	        "   (SELECT CASE WHEN v.MENU_MANAGED = '1' THEN 'Active' ELSE 'Suspended' END " +
	        "    FROM ScreenMenuModel v WHERE v.MENU_CODE = c.MENU_CODE) AS STATE " +
	        "FROM ScreenObjectModel c " +
	        "WHERE c.OBJECT_TYPE = 16 AND c.IS_MAIN = 2"
	    );
	}
	@Transactional
	public CustomResponse deleteScreenObject(long id) {
		ScreenObjectModel sbomodel = screenObjectRepo.findByObjectId(id);

		CustomResponse resp = CustomResponse.builder().build();
		String menuCode = screenObjectRepo.getMenuCode(id);
		System.out.println("MENU_CODE>>>>>>>>>>>>>>>>" + menuCode);
		if (sbomodel != null) {

			screenObjectRepo.deleteScreenObject(id);

			screenMenuRepo.deleteScreenMenu(String.valueOf(menuCode));

			System.out.println("SCREEN OBJECT DELETED>>>>>>>>>>" + id);

			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("Query with id = " + id + " deleted successfully!");

		} else {
			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("Query with id = " + id + " not found!");
		}

		return resp;
	}

	@Transactional
	public CustomResponse deleteScreen(long id) {
		ScreenObjectModel sbomodel = screenObjectRepo.findByObjectId(id);

		CustomResponse resp = CustomResponse.builder().build();
		String menuCode = screenObjectRepo.getMenuCode(id);
		System.out.println("MENU_CODE>>>>>>>>>>>>>>>>" + menuCode);
		if (sbomodel != null) {
			screenUserAccessRepo.deleteScreenUserAcess(menuCode);
			screenObjectRepo.deleteScreenObject(id);

			screenMenuRepo.deleteScreenMenu(String.valueOf(menuCode));

			System.out.println("SCREEN OBJECT DELETED>>>>>>>>>>" + id);

			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("Query with id = " + id + " deleted successfully!");

		} else {
			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("Query with id = " + id + " not found!");
		}

		return resp;
	}

	@Override
	public List<ObjectNode> getScreenData(long screenId) {
		byte[] data = screenObjectRepo.getEncodedData(screenId);
		String decodedData = new String(Base64.decodeBase64(data));
		List<ObjectNode> screenForm = getJson(entityManagerR, "SELECT c.OBJECT_NAME as screenName,\r\n"
				+ "(SELECT m.MENU_CODE\r\n" + " FROM ScreenMenuModel m\r\n"
				+ "  WHERE m.MENU_CODE=(SELECT n.MENU_P_CODE \r\n" + "                     FROM ScreenMenuModel n \r\n"
				+ "                     WHERE n.MENU_CODE=c.MENU_CODE )) AS screenParentMenuId,\r\n"
				+ "(SELECT m.MENU_NAME\r\n" + " FROM ScreenMenuModel m\r\n"
				+ "  WHERE m.MENU_CODE=(SELECT n.MENU_P_CODE \r\n" + "                     FROM ScreenMenuModel n \r\n"
				+ "                     WHERE n.MENU_CODE=c.MENU_CODE )) AS screenParentMenu,\r\n"
				+ "(SELECT a.APP_CODE " + "        FROM ApplicationListModel a\r\n"
				+ "        WHERE a.APP_CODE=CAST(SUBSTR(c.MENU_CODE,1,3) AS integer))AS screenApplicationId,\r\n"
				+ "(SELECT a.APP_NAME \r\n" + "FROM ApplicationListModel a\r\n"
				+ "WHERE a.APP_CODE=CAST(SUBSTR(c.MENU_CODE,1,3) AS integer)) AS screenApplication,\r\n"
				+ "(SELECT (CASE WHEN v.MENU_MANAGED='1' THEN 'Active' ELSE 'Suspended' END) FROM ScreenMenuModel v WHERE v.MENU_CODE=c.MENU_CODE) AS screenIsSuspended\r\n"
				+ "FROM ScreenObjectModel c WHERE OBJECT_TYPE=16 AND IS_MAIN=2 AND OBJECT_ID=" + screenId);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("data", decodedData.toString());

		screenForm.add(objectNode);
		return screenForm;
	}

	@Transactional
	@Override
	public int updateScreen(ScreenDto screenDto) {

		@SuppressWarnings("unused")
		CustomResponse deleteScreen = deleteScreen(screenDto.getScreenId());
		@SuppressWarnings("unused")
		int addScreen = addScreen(screenDto);
		return 0;
	}

	@Override
	public List<ObjectNode> getScreenPreviewData(String menuVariable) {
		String menuCode = screenMenuRepo.getMenuCode(menuVariable);
		System.out.println("MENU VARIABLE>>>>>>>>>>"+menuVariable);
		

		BigDecimal objectId = screenObjectRepo.getObjectIdFromMenu(menuCode);
		System.out.println("objectId>>>>>>>>>>"+objectId);

		byte[] objectParams = screenObjectRepo.getEncodedData(objectId.longValue());
		String decodedData = new String(Base64.decodeBase64(objectParams));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<ObjectNode> res = new ArrayList();

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("screenId", objectId);
		objectNode.put("screenData", decodedData);

		res.add(objectNode);

		return res;
	}

}
