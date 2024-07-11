package com.valoores.wfm.app.settings.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.wfm.app.settings.service.SettingsService;

@Service
public class SettingsServiceImpl implements SettingsService{

	@Autowired
	private EntityManager entityManagerR;
	
	
	
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
	public List<ObjectNode> getVacations() { 

		return getJson(entityManagerR, "SELECT t.autoEscalationId as autoEscalationId," + 
				"         t.escalationType as escalationType," + 
				"         t.parentEscalatorCode as parentEscalatorCode," + 
				"         t.childEscalatorCode as childEscalatorCode," + 
				"         t.beginDate as beginDate," + 
				"         t.endDate as endDate," + 
				"         t.escalationObject as escalationObject," + 
				"         t.createdBy as createdBy," + 
				"           (SELECT concat(empFname, ' ', empName) as empName FROM HREmployeeModel  WHERE empId = t.createdBy) AS createdByName," + 
				"           t.updatedBy as updatedBy," + 
				"           (SELECT concat(empFname, ' ', empName) AS empName FROM HREmployeeModel  WHERE empId = t.updatedBy) AS updatedByName," + 
				"           t.creationDate as creationDate, t.updateDate as updateDate," + 
				"         CASE WHEN t.escalationType = 1 THEN (SELECT concat(empFname, ' ', empName) as empName FROM HREmployeeModel WHERE empId = t.parentEscalatorCode)" + 
				"         ELSE (SELECT rd.roleName as roleName FROM RoleDefinitionModel rd WHERE rd.roleId = t.parentEscalatorCode)" + 
				"         END AS parentEscalatorName," + 
				"         CASE WHEN t.escalationType = 1 THEN (SELECT concat(empFname, ' ', empName) as empName FROM HREmployeeModel WHERE empId = t.childEscalatorCode)" + 
				"       ELSE (SELECT rd.roleName as roleName FROM RoleDefinitionModel rd WHERE rd.roleId = t.childEscalatorCode)" + 
				"       END AS childEscalatorName" + 
				"    FROM AutomaticEscalationModel t"); 
	}
	
	
	
	
}
