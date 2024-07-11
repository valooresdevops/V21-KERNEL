package com.valoores.inDisplayApplication.app.dynamicSearch.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.common.ObjectToJsonRepository;
import com.valoores.inDisplayApplication.app.dynamicSearch.model.DynamicSearch;
import com.valoores.inDisplayApplication.app.dynamicSearch.repository.DynSrchRepository;
import com.valoores.inDisplayApplication.app.dynamicSearch.repository.DynamicSearchRepository;
import com.valoores.inDisplayApplication.app.formBuilder.repository.SqbQueryDetailsRepository;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class DynamicSearchServiceImpl implements IDynamicSearchService {

	@Autowired
	private EntityManager entityManagerR;

	@Autowired
	private DynamicSearchRepository dynamicsearchrepository;

	@Autowired
	private DynSrchRepository dynsrchrepository;

	@Autowired
	DataSourceProperties dataSourceProperties;

	@Autowired
	private SqbQueryDetailsRepository sqbQueryDetailsRepository;

	@SuppressWarnings("unused")
	@Override
	public List<ObjectNode> getDynamicSearch(long objectId, String sourceQuery) {

		List<ObjectNode> result = null;
		ArrayList<String> array = new ArrayList<String>();
		ArrayList<String> arrayType = new ArrayList<String>();
		DynamicSearch columnModel2 = new DynamicSearch();

		try {
			System.out.println(" sourceQuery   " + sourceQuery);
			System.out.println("objectId  ==============      " + objectId);

			String checkDynamicSearchTable;
			long filled;
			JSONArray headsType = null;

			if (sourceQuery.contentEquals("0")) {

				result = ObjectToJsonRepository.getJson(entityManagerR,
						" SELECT c.columnId as id, c.columnName as name, c.columnDesc as columnDesc, "
								+ "c.columnTypeCode as columnTypeCode,"
								+ " (SELECT l.name FROM Syslines l WHERE l.heaCode = 426  AND c.columnTypeCode = l.id) as columnTypeCode, "
								+ "( CASE WHEN c.qbeId IS NOT NULL THEN c.qbeId ELSE -1 END) AS isDropdown "
								+ "FROM DynamicSearchModel c  WHERE c.tableId IN (SELECT tableId FROM TableObjectModel "
								+ "where objectId = " + objectId + ") AND c.columnTypeCode != 14");

				String jsonString = result.toString();
				JSONArray jsonArray = new JSONArray(jsonString);

				List<JSONObject> modifiedObjects = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					if (jsonObject.getString("columnTypeCode").equals("Number")) {
						jsonObject.put("columnTypeCode", "NUMBER");
					} else if (jsonObject.getString("columnTypeCode").equals("Text")) {
						jsonObject.put("columnTypeCode", "VARCHAR2");
					} else if (jsonObject.getString("columnTypeCode").equals("Date")) {
						jsonObject.put("columnTypeCode", "DATE");
					}
					modifiedObjects.add(jsonObject);
				}

				JSONArray modifiedJsonArray = new JSONArray(modifiedObjects);
				String resultReplaced = modifiedJsonArray.toString();

				ObjectMapper mapper = new ObjectMapper();

				List<ObjectNode> nodeList = mapper.readValue(resultReplaced, new TypeReference<List<ObjectNode>>() {
				});
				result = nodeList;

				for (int i1 = 0; i1 < modifiedJsonArray.length(); i1++) {
					Object objectArray = modifiedJsonArray.get(i1);

					JSONObject objectArray111 = new JSONObject(objectArray.toString());
					System.out.println(" objectArray111   not SQ " + objectArray111);
					array.add(objectArray111.getString("name"));
					arrayType.add(objectArray111.getString("columnTypeCode"));

				}

				JSONArray heads = new JSONArray(array);
				headsType = new JSONArray(arrayType);

				checkDynamicSearchTable = "select count(1) from techdba.TECH_DYNAMIC_SEARCH a where a.object_id = "
						+ objectId;
				filled = Long.parseLong(
						entityManagerR.createNativeQuery(checkDynamicSearchTable).getSingleResult().toString());

				System.out.println(
						"checkDynamicSearchTable =  not SQ  " + checkDynamicSearchTable + "  filled with " + filled);

				if (filled <= 0) {
					List<DynamicSearch> columnModelList = new ArrayList<>();

					System.out.println(" resultReplaced.length()   not SQ " + resultReplaced.length());
					for (int iQ = 0; iQ < resultReplaced.length(); iQ++) {
						if (iQ == 0) {
							for (int j = 0; j < headsType.length(); j++) {
								
								System.out.println(" heads.getString(j)   "+ j +"   "+ heads.getString(j));
								columnModel2 = new DynamicSearch();

								columnModel2.setColName(heads.getString(j));
								columnModel2.setColType(headsType.getString(j));
								columnModel2.setColAppName(heads.getString(j));
								columnModel2.setCreatedBy(-6);
								columnModel2.setObjectId(objectId);

								columnModelList.add(columnModel2);
							}

						} else {
							break;
						}
					}

					dynsrchrepository.saveAll(columnModelList);

				}

				result = ObjectToJsonRepository.getJson(entityManagerR,
						" SELECT c.dynSrchId  as id ," + " c.colAppName as name ," + " c.colType as columnTypeCode,"
								+ " (case when c.colType ='C' then '1' else   '0' end ) AS isDropdown "
								+ " FROM DynamicSearch c  " + " WHERE c.objectId = " + objectId + " " + " ");
			} else {
				
				System.out.println(" sq == 1");
				String getQBEIdQuery = " SELECT qbe_id from  SUITEDBA.CFG_object_def where object_Id = " + objectId;
				long qbeId = Long
						.parseLong(entityManagerR.createNativeQuery(getQBEIdQuery).getSingleResult().toString());

				System.out.println("qbeId            7777777777777   " + qbeId);

				List<ObjectNode> queryyy = getQueryParams(qbeId);
				JsonNode queryJsonNode = queryyy.get(0).get("query");
				JsonNode queryParams = queryyy.get(0).get("queryParams");
				String queryParamsString = queryParams.toString();
				queryParamsString = queryParamsString.substring(1, queryParamsString.length() - 1);
				queryParamsString = queryParamsString.replace("\\", "");

				JSONArray queryHeadersArray = new JSONArray(queryParamsString);
				String paramName = "null";
				String paramDefault = "null";

				for (int i = 0; i < queryHeadersArray.length(); i++) {
					JSONObject jsonObject = queryHeadersArray.getJSONObject(i);

					paramName = jsonObject.getString("paramName");
					paramDefault = jsonObject.getString("paramDefault");
				}

				byte[] encodedQuery = sqbQueryDetailsRepository.getQueryBlob(qbeId);
				String decodedQuery = new String(Base64.decodeBase64(encodedQuery));
//			String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);
				String query = queryJsonNode.asText();

				query = query.replace("\\n", "");
				query = query.replace("\\", "");
				paramName = "[" + paramName + "]";

				if (query.contains(paramName)) {
					System.out.println(" contain ");
					System.out.println(" paramName  " + paramName);
					query = query.replace(paramName, paramDefault);
				}

				Connection con = DriverManager.getConnection(dataSourceProperties.determineUrl(), "qbedba", "qbedba");
				Statement stmt = con.createStatement();
				query.replaceAll("\"", "\\\\\"");
				//query = query.substring(1, query.length() - 1);
				ResultSet rs = stmt.executeQuery(query);

				ResultSetMetaData rsMetaData = rs.getMetaData();
				String jsonString1 = "[";
				int count = rsMetaData.getColumnCount();
				for (int i = 1; i <= count; i++) {
					jsonString1 += "{\"headerName\":\"" + rsMetaData.getColumnName(i) + "\",\"field\":\""
							+ rsMetaData.getColumnTypeName(i) + "\",\"businessName\":\"" + rsMetaData.getColumnName(i)
							+ "\"}";
					if (i != count) {
						jsonString1 += ",";
					}
				}
				jsonString1 += "]";
				JSONArray queryHeadsValue = new JSONArray(new String(jsonString1));

				System.out.println(" queryHeadsValue  SQ   " + queryHeadsValue);
				for (int i = 0; i < queryHeadsValue.length(); i++) {
					Object objectArray = queryHeadsValue.get(i);
					JSONObject objectArray111 = new JSONObject(objectArray.toString());

					System.out.println(" objectArray111   " + objectArray111);
					array.add(objectArray111.getString("headerName"));
					arrayType.add(objectArray111.getString("field"));

				}

				JSONArray heads = new JSONArray(array);
				headsType = new JSONArray(arrayType);

				checkDynamicSearchTable = "select count(1) from techdba.TECH_DYNAMIC_SEARCH a where a.object_id = "
						+ objectId;
				filled = Long.parseLong(
						entityManagerR.createNativeQuery(checkDynamicSearchTable).getSingleResult().toString());


				if (filled <= 0) {
					for (int i = 0; i < heads.length(); i++) {
								DynamicSearch columnModel3 = dynsrchrepository.findByObjectIdAndColName(objectId, heads.getString(i));
								if (columnModel3 == null) {
									columnModel2 = new DynamicSearch();
									columnModel2.setColName(heads.getString(i));
									columnModel2.setColType(headsType.getString(i));
									columnModel2.setColAppName(heads.getString(i));
									columnModel2.setCreatedBy(-6);
									columnModel2.setObjectId(objectId);
									dynsrchrepository.save(columnModel2);
								}
					}

				}

				result = ObjectToJsonRepository.getJson(entityManagerR,
						" SELECT c.dynSrchId  as id ," + " c.colAppName as name ," + " c.colType as columnTypeCode,"
								+ " (case when c.colType ='C' then '1' else   '0' end ) AS isDropdown "
								+ " FROM DynamicSearch c  " + " WHERE c.objectId = " + objectId + " " + " ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<ObjectNode> getSearchType() {
		List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
				" SELECT l.id as id, l.name as name FROM Syslines l  WHERE l.heaCode = 46 ");
		return result;
	}
	
	
//	private void handleDropdown3(Integer dropdown3, String fieldId) throws Exception {
//	    String dropdown3Combo = " select cmb_sql from techdba.TECH_DYNAMIC_SEARCH a where a.dyn_srch_id =" + fieldId;
//	    System.out.println("dropdown3Combo " + dropdown3Combo);
//	    String dropdown3Name = entityManagerR.createNativeQuery(dropdown3Combo).getSingleResult().toString();
//
//	    String dropdown3Query = " select b.name from( " + dropdown3Name + ") b where b.id in(" + dropdown3+")";
//	    System.out.println("dropdown3Query " + dropdown3Query);
//	    dropdown3Name = entityManagerR.createNativeQuery(dropdown3Query).getSingleResult().toString();
//	    System.out.println("dropdown3Name " + dropdown3Name);
//
//	    // Append the result to some final result or condition
//	    // Adjust this part according to how you want to use dropdown3Name
//	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String getWhereCondition(String object, Integer elementSize) {
		System.out.println(" object    my listtt  " + object);
		ArrayList<String> array = new ArrayList<String>();
		ArrayList<String> arrayType = new ArrayList<String>();

		String condition = "";
		String dropdown3Name = "";
		String dropdown3Query = "";
		String advanceSearchProcedure = "";
		String finalResult ="";
		Integer dropdown3 = 0;
		try {

			System.out.println(" elementSize  " + elementSize);

			if (elementSize == 0) {

				JSONArray objArray = new JSONArray(object);
				JSONObject objectArray = objArray.getJSONObject(0);

				String searchType = objectArray.getString("searchType");
				String text = objectArray.getString("text");
				String fieldId = objectArray.getString("dropdown");
				long objectId = objectArray.getLong("objectId");
//				dropdown3 = objectArray.getInt("dropdown3");
	            Object dropdown3Obj = objectArray.get("dropdown3");

				String sourceQuery = objectArray.getString("sourceQuery");
				
				String isAdvanceSearchProcedure = "select ADVANCED_SEARCH_PROCEDURE_NAME from cfg_object_def where object_id = "+objectId;
				  Object result = entityManagerR.createNativeQuery(isAdvanceSearchProcedure).getSingleResult();

		            if (result != null) {
		                advanceSearchProcedure = result.toString();
		            } else {
		                advanceSearchProcedure = ""; // or any default value you want to assign
		            }				String columnNameQuery = " SELECT  a.col_name    FROM TECHDBA.TECH_DYNAMIC_SEARCH  a WHERE  a.dyn_srch_id  = "
						+ fieldId;
				System.out.println(" columnNameQuery   " + columnNameQuery);
				String fieldName = entityManagerR.createNativeQuery(columnNameQuery).getSingleResult().toString();

				String columnTypeQuery = " SELECT  a.col_type    FROM TECHDBA.TECH_DYNAMIC_SEARCH  a WHERE  a.dyn_srch_id  = "
						+ fieldId;
				System.out.println(" columnTypeQuery   " + columnTypeQuery);
				String fieldType = entityManagerR.createNativeQuery(columnTypeQuery).getSingleResult().toString();
				System.out.println(" fieldType   " + fieldType);

				if (fieldType.equals("DATE")) {
					DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

					Date originalDate = inputDateFormat.parse(text);
					text = outputDateFormat.format(originalDate).toUpperCase();
					text = "'" + text + "'";
				}
				if (dropdown3Obj != null && !dropdown3Obj.equals(0)) {
				 if (dropdown3Obj instanceof Integer) {
		                 dropdown3 = (Integer) dropdown3Obj;
		                 String dropdown3Combo = " select cmb_sql from techdba.TECH_DYNAMIC_SEARCH  a where a.dyn_srch_id  ="
									+ fieldId;
							System.out.println(" dropdown3Combo   " + dropdown3Combo);
							dropdown3Name = entityManagerR.createNativeQuery(dropdown3Combo).getSingleResult().toString();
		
						dropdown3Query = "  select b.name  from( " + dropdown3Name + ") b where b.id=" + dropdown3;
							System.out.println(" dropdown3Query   " + dropdown3Query);
							dropdown3Name = entityManagerR.createNativeQuery(dropdown3Query).getSingleResult().toString();
							System.out.println(" dropdown3Name   " + dropdown3Name);
		            } else if (dropdown3Obj instanceof JSONArray) {
		                JSONArray dropdown3Array = (JSONArray) dropdown3Obj;
		                StringBuilder dropdown3Values = new StringBuilder();
		                for (int i = 0; i < dropdown3Array.length(); i++) {
		                    if (i > 0) {
		                        dropdown3Values.append(", ");
		                    }
		                    dropdown3Values.append(dropdown3Array.getInt(i));
		                }
		                dropdown3=1;
		                String dropdown3Combo = "select cmb_sql from techdba.TECH_DYNAMIC_SEARCH a where a.dyn_srch_id = " + fieldId;
		                dropdown3Name = entityManagerR.createNativeQuery(dropdown3Combo).getSingleResult().toString();
		                dropdown3Query = "select b.name from (" + dropdown3Name + ") b where b.id IN (" + dropdown3Values.toString() + ")";
		                System.out.println("dropdown3Query " + dropdown3Query);

		                List<String> namesList = entityManagerR.createNativeQuery(dropdown3Query).getResultList();
		                dropdown3Name = String.join(", ", namesList);
		                System.out.println("dropdown3Name " + dropdown3Name);
		            }
			}
				String searchNameQuery = " SELECT LIN_NAME  FROM REF_SYS_LINES   WHERE hea_Code = 46 and LIN_CODE= "
						+ searchType;
				String searchName = entityManagerR.createNativeQuery(searchNameQuery).getSingleResult().toString();

				if (searchName.equals("Equal To")) {
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

					if(fieldType.equals("DATE")) {
						condition = fieldName + " = TO_DATE("+text+",'DD/MM/YYYY')";

					}else if (fieldType.equals("VARCHAR2")) {

						condition = "upper(" + fieldName + ")" + " like " + " upper('" + text + "')";

					}else if (fieldType.equals("COMBO")) {
						System.out.println(">>>>>>>>>>>>>>>>>111111111111>>>>>>>>>>>>>>>>>>");

						if (dropdown3 != 0) {
							if(dropdown3Name.indexOf(',')!= -1) {
								String[] dropdown3Names = dropdown3Name.split(",");
								for (int i = 0; i < dropdown3Names.length; i++) {
									System.out.println(">>>>>>>>dropdown3Names    :"+dropdown3Names[i]);
									String dropdownName = dropdown3Names[i].trim();
									if(condition == "") {
										condition = "upper(" + fieldName + ")" + " like " + " upper('" + dropdownName + "')";

									}else {
										condition +=" or upper(" + fieldName + ")" + " like " + " upper('" + dropdownName + "')";

									}
									System.out.println(">>>>>>>>condition    :"+condition);

								}
							}else {
								condition = "upper(" + fieldName + ")" + " like " + " upper('" + dropdown3Name + "')";

							}
						}else {
							condition = fieldName + " = " + text;

						}

					} else {
						if (dropdown3 != 0) {
							condition = "upper(" + fieldName + ")" + " like " + " upper('" + dropdown3Name + "')";
						} else {
						}
					}

				} else if (searchName.equals("Not Equal To")) {
					if(fieldType.equals("DATE")) {
						condition = fieldName + " != TO_DATE("+text+",'DD/MM/YYYY')";

					}else if (fieldType.equals("VARCHAR2")) {
						condition = "upper(" + fieldName + ") " + " not like " + " upper('" + text + "')";
					}else if (fieldType.equals("COMBO")) {
						System.out.println(">>>>>>>>>>>>>>>>>111111111111>>>>>>>>>>>>>>>>>>");

						if (dropdown3 != 0) {
							if(dropdown3Name.indexOf(',')!= -1) {
								String[] dropdown3Names = dropdown3Name.split(",");
								for (int i = 0; i < dropdown3Names.length; i++) {
									System.out.println(">>>>>>>>dropdown3Names    :"+dropdown3Names[i]);
									String dropdownName = dropdown3Names[i].trim();
									if(condition == "") {
										condition = "upper(" + fieldName + ")" + " not like " + " upper('" + dropdownName + "')";

									}else {
										condition +=" or upper(" + fieldName + ")" + " not like " + " upper('" + dropdownName + "')";

									}
									System.out.println(">>>>>>>>condition    :"+condition);

								}
							}else {
								condition = "upper(" + fieldName + ")" + "not like " + " upper('" + dropdown3Name + "')";

							}
						}else {
							condition = fieldName + " != " + text;

						}

					} else {

						if (dropdown3 != 0) {
							condition = "upper(" + fieldName + ")" + " not like " + " upper('" + dropdown3Name + "')";
						} else {
							condition = fieldName + " != " + text;
						}
					}

				} else if (searchName.equals("Started By")) {

					condition = "upper(" + fieldName + ")" + " like " + "upper('" + text + "%')";

				} else if (searchName.equals("Contains")) {
					System.out.println(" ccccccccccccc");

					condition = "upper(" + fieldName + ")" + " like " + " upper('%" + text + "%')";

				} else if (searchName.equals("Greater Than")) {
					System.out.println(" >>>>>>>>>>");
					if(fieldType.equals("DATE")) {
						condition = fieldName + " > TO_DATE("+text+",'DD/MM/YYYY')";

					}else {
					condition = fieldName + " > " + text;
					}
				} else if (searchName.equals("Greater Than Or Equal To")) {
					
					System.out.println(" >>==");
					if(fieldType.equals("DATE")) {
						condition = fieldName + " >= TO_DATE("+text+",'DD/MM/YYYY')";

					}else {
					condition = fieldName + " >=  " + text;
					}
				} else if (searchName.equals("Less Than")) {
					System.out.println(" <<");
					if(fieldType.equals("DATE")) {
						condition = fieldName + " < TO_DATE("+text+",'DD/MM/YYYY')";

					}else {
					condition = fieldName + " < " + text;
					}

				} else if (searchName.equals("Less Than Or Equal To")) {
					System.out.println(" <<==");
					if(fieldType.equals("DATE")) {
						condition = fieldName + " <= TO_DATE("+text+",'DD/MM/YYYY')";

					}else {
					condition = fieldName + " <= " + text;
					}
				}

			} else if (elementSize == 1) {
				JSONArray objectArray = new JSONArray(object);

				System.out.println(" objectArray   " + objectArray + "   lll " + objectArray.length());
				for (int k = 0; k < objectArray.length(); k++) {

					JSONObject objectArrayItem = objectArray.getJSONObject(k);
					String searchType = objectArrayItem.getString("searchType");
					String text = objectArrayItem.getString("text");
					String fieldId = objectArrayItem.getString("dropdown");
					long objectId = objectArrayItem.getLong("objectId");
					dropdown3 = objectArrayItem.getInt("dropdown3");
					String sourceQuery = objectArrayItem.getString("sourceQuery");

					System.out.println(" searchType    " + searchType);
					System.out.println(" text    " + text);
					System.out.println(" fieldId    " + fieldId);
					System.out.println(" objectId    " + objectId);
					System.out.println(" dropdown3    " + dropdown3);
					System.out.println(" sourceQuery    " + sourceQuery);
					String isAdvanceSearchProcedure = "select ADVANCED_SEARCH_PROCEDURE_NAME from cfg_object_def where object_id = "+objectId;
					  Object result = entityManagerR.createNativeQuery(isAdvanceSearchProcedure).getSingleResult();

			            if (result != null) {
			                advanceSearchProcedure = result.toString();
			            } else {
			                advanceSearchProcedure = ""; // or any default value you want to assign
			            }					String columnNameQuery = " SELECT  a.col_name    FROM TECHDBA.TECH_DYNAMIC_SEARCH  a WHERE  a.dyn_srch_id  = "
							+ fieldId;
					String fieldName = entityManagerR.createNativeQuery(columnNameQuery).getSingleResult().toString();

					String columnTypeQuery = " SELECT  a.col_type    FROM TECHDBA.TECH_DYNAMIC_SEARCH  a WHERE  a.dyn_srch_id  = "
							+ fieldId;
					String fieldType = entityManagerR.createNativeQuery(columnTypeQuery).getSingleResult().toString();

					if (fieldType.equals("DATE")) {
						DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						DateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

						Date originalDate = inputDateFormat.parse(text);
						text = outputDateFormat.format(originalDate).toUpperCase();
						text = "'" + text + "'";
					}

					if (dropdown3 != 0) {
						String dropdown3Combo = " select cmb_sql from techdba.TECH_DYNAMIC_SEARCH  a where a.dyn_srch_id  ="
								+ fieldId;
						System.out.println(" dropdown3Combo   " + dropdown3Combo);
						dropdown3Name = entityManagerR.createNativeQuery(dropdown3Combo).getSingleResult().toString();
						System.out.println(" dropdown3Name ____________  " + dropdown3Name);

						dropdown3Query = "  select b.name  from( " + dropdown3Name + ") b where b.id=" + dropdown3;
						System.out.println(" dropdown3Query   " + dropdown3Query);
						dropdown3Name = entityManagerR.createNativeQuery(dropdown3Query).getSingleResult().toString();
						System.out.println(" dropdown3Name   " + dropdown3Name);
					}

					String searchNameQuery = " SELECT LIN_NAME  FROM REF_SYS_LINES   WHERE hea_Code = 46 and LIN_CODE= "
							+ searchType;
					String searchName = entityManagerR.createNativeQuery(searchNameQuery).getSingleResult().toString();

					if (searchName.equals("Equal To")) {
						if(fieldType.equals("DATE")) {
							if (condition.equals("")) {
								condition = fieldName + " = TO_DATE("+text+",'DD/MM/YYYY')";
							} else {
								condition += fieldName + " = TO_DATE("+text+",'DD/MM/YYYY')";
								}

						}else if (fieldType.equals("VARCHAR2")) {

							if (condition.equals("")) {
								condition = "upper(" + fieldName + ")" + " like " + " upper('" + text + "')";
							} else {
								condition += " and upper(" + fieldName + ")" + " like " + " upper('" + text + "')";
							}

						} else {
							if (dropdown3 != 0) {
								if (condition.equals("")) {
									condition = "upper(" + fieldName + ")" + " like " + " upper('" + dropdown3Name
											+ "')";
								} else {
									condition += " and  upper(" + fieldName + ")" + " like " + " upper('"
											+ dropdown3Name + "')";
								}
							} else {

								if (condition.equals("")) {
									condition = fieldName + " = " + text;
								} else {
									condition += " and   " + fieldName + " = " + text;
								}

							}
						}

					} else if (searchName.equals("Not Equal To")) {
						if(fieldType.equals("DATE")) {
							if (condition.equals("")) {
								condition = fieldName + " != TO_DATE("+text+",'DD/MM/YYYY')";
							} else {
								condition += fieldName + " != TO_DATE("+text+",'DD/MM/YYYY')";
								}

						}else if (fieldType.equals("VARCHAR2")) {

							if (condition.equals("")) {
								condition = " upper(" + fieldName + ") " + " not like " + " upper('" + text + "')";
							} else {
								condition += " and  upper(" + fieldName + ") " + " not like " + " upper('" + text
										+ "')";
							}

						} else {

							if (dropdown3 != 0) {

								if (condition.equals("")) {
									condition = "upper(" + fieldName + ")" + " not like " + " upper('" + dropdown3Name
											+ "')";
								} else {
									condition += " and upper(" + fieldName + ")" + " not like " + " upper('"
											+ dropdown3Name + "')";
								}
							} else {

								if (condition.equals("")) {
									condition = fieldName + " != " + text;
								} else {
									condition += "  and " + fieldName + " != " + text;
								}
							}
						}

					} else if (searchName.equals("Started By")) {
						System.out.println(" SSSSSSSSSSSSSS");

						if (condition.equals("")) {
							condition = "upper(" + fieldName + ")" + " like " + "upper('" + text + "%')";
						} else {
							condition += " and  upper(" + fieldName + ")" + " like " + "upper('" + text + "%')";
						}

					} else if (searchName.equals("Contains")) {
						System.out.println(" ccccccccccccc");

						if (condition.equals("")) {
							condition = "upper(" + fieldName + ")" + " like " + " upper('%" + text + "%')";
						} else {
							condition += "  and  upper(" + fieldName + ")" + " like " + " upper('%" + text + "%')";
						}

					} else if (searchName.equals("Greater Than")) {
						if (fieldType.equals("DATE")) {
							if (condition.equals("")) {
								condition = fieldName + " > TO_DATE("+text+",'DD/MM/YYYY')";
							} else {
								condition += "  and " + fieldName + " > TO_DATE("+text+",'DD/MM/YYYY')";
							}
						}else {

						if (condition.equals("")) {
							condition = fieldName + " > " + text;
						} else {
							condition += "  and " + fieldName + " > " + text;
						}
					}
					} else if (searchName.equals("Greater Than Or Equal To")) {
						if (fieldType.equals("DATE")) {
							if (condition.equals("")) {
								condition = fieldName + "  >= TO_DATE("+text+",'DD/MM/YYYY')";
							} else {
								condition += "  and " + fieldName + "  >= TO_DATE("+text+",'DD/MM/YYYY')";
							}
						}else {
						if (condition.equals("")) {
							condition = fieldName + " >=  " + text;
						} else {
							condition += " and " + fieldName + " >=  " + text;
						}
						}
					} else if (searchName.equals("Less Than")) {
						if (fieldType.equals("DATE")) {
							if (condition.equals("")) {
								condition = fieldName + "  < TO_DATE("+text+",'DD/MM/YYYY')";
							} else {
								condition += "  and " + fieldName + "  < TO_DATE("+text+",'DD/MM/YYYY')";
							}
						}else {
						if (condition.equals("")) {
							condition = fieldName + " < " + text;
						} else {
							condition += " and  " + fieldName + " < " + text;
						}
						}

					} else if (searchName.equals("Less Than Or Equal To")) {
						if (fieldType.equals("DATE")) {
							if (condition.equals("")) {
								condition = fieldName + "  <= TO_DATE("+text+",'DD/MM/YYYY')";
							} else {
								condition += "  and " + fieldName + "  <= TO_DATE("+text+",'DD/MM/YYYY')";
							}
						}else {
						if (condition.equals("")) {
							condition = fieldName + " <= " + text;
						} else {
							condition += "  and  " + fieldName + " <= " + text;
						}
						}
					}
				}

			}
			if(advanceSearchProcedure != "") {
				callProcedureIsAdvancedSearch(advanceSearchProcedure);
			}
			System.out.println(" condition  " + condition);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return condition;
	}
	
	public void callProcedureIsAdvancedSearch(String procedureName) {
		StoredProcedureQuery storedProcedure = entityManagerR.createStoredProcedureQuery(procedureName);

		storedProcedure.execute();
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public List getThirdDropDown(String searchId,String userId) {
		List comboSql = null;
		try {
			String getComboQuery = "select cmb_sql from techdba.TECH_DYNAMIC_SEARCH  a where a.dyn_srch_id  ="
					+ searchId;
			String query = entityManagerR.createNativeQuery(getComboQuery).getSingleResult().toString();
			if (query.contains("userId")) {
		        query = query.replace("userId", String.valueOf(userId));
		    } 
			comboSql = entityManagerR.createNativeQuery(query).getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comboSql;
	}

	@SuppressWarnings("unused")
	@Override
	public List<ObjectNode> getQueryParams(long queryId) {

		String field = null;
		byte[] encodedQuery = dynamicsearchrepository.getQueryBlob(queryId);
		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];


		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);


		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);


		String headsString = decodedQuery.split("<LstHeads>")[1];
		headsString = headsString.split("</LstHeads>")[0];
  
		String[] headers = headsString.split("<EltHeads ");
 

		String queryHeadsJsonString = "[";
		String execHeadsJsonString = "[";

		for (int i = 1; i < headers.length; i++) {
			String header = headers[i].split("dbName=\"")[1];
			header = header.split("\"")[0];

			field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];
			System.out.println("field " + i + ">>>>>>>>>>>" + field);

			queryHeadsJsonString += "{\"headerName\":\"" + header + "\",\"field\":\"" + field + "\",\"businessName\":\""
					+ header + "\"}";
			execHeadsJsonString += "{\"headerName\":\"" + header + "\",\"field\":\"" + field + "\"}";

			if (i != headers.length - 1) {
				queryHeadsJsonString += ",";
				execHeadsJsonString += ",";

			}
		}
		queryHeadsJsonString += "]";
		execHeadsJsonString += "]";

		String parametersString = decodedQuery.split("<pars>")[1];
		parametersString = parametersString.split("</pars>")[0];
		String[] parameters = parametersString.split("<par ");

		String paramsString = "[";

		for (int i = 1; i < parameters.length; i++) {
			String paramType = parameters[i].split("fieldType=\"")[1];
			paramType = paramType.split("\"")[0];

			String paramDefault = "1";
			if (paramType.equals("query")) {
				System.out.println("TYPE IS QUERY!!!!!");
				paramDefault = parameters[i].split("subQID=\"")[1];
				paramDefault = paramDefault.split("\"")[0];
			}

			String id = parameters[i].split("id=\"")[1];
			id = id.split("\"")[0];
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,
					parameters[i].indexOf("]"));

			if (paramType.equals("query")) {

				paramsString += "{\"paramName\":\"" + paramName + "\",\"paramType\":\"" + paramType
						+ "\",\"paramDefault\":\"" + paramDefault + "\"}";

			} else {
				paramsString += "{\"paramName\":\"" + paramName + "\",\"paramType\":\"" + paramType
						+ "\",\"paramDefault\":\"" + Integer.parseInt(paramDefault) + "\"}";

			}

			query = query.replace("#" + id + "#", "[" + paramName + "]");

			if (i != parameters.length - 1) {
				paramsString += ",";
			}
		}
		paramsString += "]";

		List<ObjectNode> queryData = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectNode = objectMapper.createObjectNode();

		System.out.println(" query    before  " + query);
		objectNode.put("query", query);
		objectNode.put("queryId", queryId);
		objectNode.put("queryParams", paramsString);
//        objectNode.put("queryName", queryName);
		objectNode.put("queryHeaders", execHeadsJsonString);

		queryData.add(objectNode);
		System.out.println(" queryData  == " + queryData);
		return queryData;
	}

}
