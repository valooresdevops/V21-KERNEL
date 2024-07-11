package com.valoores.wfm.app.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.wfm.app.monitor.service.MonitorService;

@Service
public class MonitorServiceImpl implements MonitorService{

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
	public List<ObjectNode> unusedTrigger() { 

		return getJson(entityManagerR, " SELECT DISTINCT T.triggerName as id, T.triggerName as triggerName" + 
				"     FROM AllTriggersModel T" + 
				"   WHERE (T.owner = 'WFMDBA' OR T.owner = 'WFMUSER') " + 
				"   AND T.triggerName LIKE 'WFM_%'" + 
				"   and T.triggerName  not in (" + 
				"   (SELECT D.triggerName as triggerName FROM ActivityDetailModel D))"); 
	}
//native queries 	
	
	
	
	@Override
	public List<ObjectNode> missingDoc() { 

		return getJson(entityManagerR, "WITH processplan AS (" + 
				"  SELECT p.bp_plan_id," + 
				"         p.bp_id," + 
				"         p.bp_status_code statuscode" + 
				"  FROM wfm_bp_planning p" + 
				"  WHERE (p.bp_edate IS NOT NULL AND SYSDATE BETWEEN p.bp_bdate AND p.bp_edate)" + 
				"     OR (p.bp_edate IS NULL AND SYSDATE >= p.bp_bdate)" + 
				")" + 
				"SELECT *" + 
				"FROM (" + 
				"  SELECT p.bp_id ID," + 
				"         p.bp_name BUSPROCNAME," + 
				"         ba.bp_activity_name BPACTIVITYNAME," + 
				"         ds.bp_activity_id ATTACHEDDOCID" + 
				"    FROM wfmdba.wfm_bp_activity_document ds" + 
				"    JOIN wfm_bp_activity ba ON ba.bp_activity_id = ds.bp_activity_id" + 
				"    JOIN wfm_business_process p ON ba.bp_id = p.bp_id" + 
				"    JOIN processplan pl ON pl.bp_id = p.bp_id" + 
				"    " + 
				"  UNION" + 
				"  " + 
				"  SELECT p.bp_id ID," + 
				"         p.bp_name BUSPROCNAME," + 
				"         ba.bp_activity_name BPACTIVITYNAME," + 
				"         d.bp_activity_doc_id ATTACHEDDOCID" + 
				"    FROM wfmdba.wfm_bp_activity_document d" + 
				"    JOIN wfm_bp_activity ba ON d.bp_activity_id = ba.bp_activity_id" + 
				"    JOIN wfm_business_process p ON ba.bp_id = p.bp_id" + 
				"    JOIN processplan pl ON pl.bp_id = p.bp_id" + 
				") "); 
	}
	
	
	@Override
	public List<ObjectNode> runningProcess() { 

		return getJson(entityManagerR, "WITH processplan AS (" + 
				"  SELECT p.bp_plan_id," + 
				"         p.bp_id," + 
				"         p.bp_status_code statuscode" + 
				"  FROM wfm_bp_planning p" + 
				"  WHERE (p.bp_edate IS NOT NULL AND SYSDATE BETWEEN p.bp_bdate AND p.bp_edate)" + 
				"     OR (p.bp_edate IS NULL AND SYSDATE >= p.bp_bdate)" + 
				")" + 
				"SELECT *" + 
				"FROM (" + 
				"  SELECT p.bp_id ID," + 
				"         p.bp_name BUSPROCNAME," + 
				"         CASE pl.statuscode" + 
				"           WHEN 1 THEN 'Active'" + 
				"           WHEN 2 THEN 'Inactive'" + 
				"           ELSE 'Not Available'" + 
				"         END STATUSNAME" + 
				"    FROM wfmdba.wfm_bp_activity_document ds" + 
				"    JOIN wfm_bp_activity ba ON ba.bp_activity_id = ds.bp_activity_id" + 
				"    JOIN wfm_business_process p ON ba.bp_id = p.bp_id" + 
				"    JOIN processplan pl ON pl.bp_id = p.bp_id" + 
				"    " + 
				"  UNION" + 
				"  " + 
				"  SELECT p.bp_id ID," + 
				"         p.bp_name BUSPROCNAME," + 
				"         CASE pl.statuscode" + 
				"           WHEN 1 THEN 'Active'" + 
				"           WHEN 2 THEN 'Inactive'" + 
				"           ELSE 'Not Available'" + 
				"         END STATUSNAME" + 
				"    FROM wfmdba.wfm_bp_activity_document d" + 
				"    JOIN wfm_bp_activity ba ON d.bp_activity_id = ba.bp_activity_id" + 
				"    JOIN wfm_business_process p ON ba.bp_id = p.bp_id" + 
				"    JOIN processplan pl ON pl.bp_id = p.bp_id" + 
				")"); 
	}
	
	@Override
	public List<ObjectNode> invalidProcess() { 

		return getJson(entityManagerR, "WITH processplan AS (" + 
				"  SELECT p.bp_plan_id," + 
				"         p.bp_id," + 
				"         p.bp_status_code statuscode" + 
				"  FROM wfm_bp_planning p" + 
				"  WHERE (p.bp_edate IS NOT NULL AND SYSDATE BETWEEN p.bp_bdate AND p.bp_edate)" + 
				"     OR (p.bp_edate IS NULL AND SYSDATE >= p.bp_bdate)" + 
				")" + 
				"SELECT *" + 
				"FROM (" + 
				"  SELECT p.bp_id ID," + 
				"         p.bp_name BUSPROCNAME," + 
				"         CASE pl.statuscode" + 
				"           WHEN 1 THEN 'Active'" + 
				"           WHEN 2 THEN 'Inactive'" + 
				"           ELSE 'Not Available'" + 
				"         END STATUSNAME" + 
				"    FROM wfmdba.wfm_bp_activity_document ds" + 
				"    JOIN wfm_bp_activity ba ON ba.bp_activity_id = ds.bp_activity_id" + 
				"    JOIN wfm_business_process p ON ba.bp_id = p.bp_id" + 
				"    JOIN processplan pl ON pl.bp_id = p.bp_id" + 
				"    " + 
				"  UNION" + 
				"  " + 
				"  SELECT p.bp_id ID," + 
				"         p.bp_name BUSPROCNAME," + 
				"         CASE pl.statuscode" + 
				"           WHEN 1 THEN 'Active'" + 
				"           WHEN 2 THEN 'Inactive'" + 
				"           ELSE 'Not Available'" + 
				"         END STATUSNAME" + 
				"    FROM wfmdba.wfm_bp_activity_document d" + 
				"    JOIN wfm_bp_activity ba ON d.bp_activity_id = ba.bp_activity_id" + 
				"    JOIN wfm_business_process p ON ba.bp_id = p.bp_id" + 
				"    JOIN processplan pl ON pl.bp_id = p.bp_id" + 
				")"); 
	}
}
