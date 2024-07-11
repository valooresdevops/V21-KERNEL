package com.example.demo.app.queryBuilder.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.app.queryBuilder.service.QbeToolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class QbeToolServiceImpl implements QbeToolService{

	@Autowired
	private EntityManager entityManagerR;
	
	@Override
	public List<ObjectNode> getAllTables() {
		return getJson(entityManagerR, "SELECT CONCAT(CONCAT(q.OWNER,'.'),q.TABLE_NAME) AS TABLE_NAME from AllTablesModel q"); 
	}


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
		public List<ObjectNode> getTableColumns(String tableName) {
			String owner=tableName.split("\\.")[0];
			String table=tableName.split("\\.")[1];
			
			return getJson(entityManagerR, "SELECT q.COLUMN_NAME AS COLUMN_NAME from TableColumns q WHERE q.TABLE_NAME='"+table+"' AND OWNER='"+owner+"'"); 
		}
		

}
