package com.valoores.inDisplayApplication.app.common;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Repository
public class ObjectToJsonRepository {

	@SuppressWarnings("unused")
	@Autowired
	private EntityManager entityManager;

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
								if (tuple[i].toString().length() > 30 && tuple[i].toString().indexOf(" ") == -1) {
									@SuppressWarnings("unused")
									boolean isBase64 = Base64.isBase64(tuple[i].toString());
//									if (isBase64) {
//										node.put(aliases[i], decodeBase64(tuple[i].toString()));
//									} else {
										node.put(aliases[i], tuple[i].toString());
//									}
								} else {
									node.put(aliases[i], tuple[i].toString());
								}
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

}
