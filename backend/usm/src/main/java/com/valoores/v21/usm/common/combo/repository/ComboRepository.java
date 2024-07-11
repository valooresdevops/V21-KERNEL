package com.valoores.v21.usm.common.combo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ComboRepository {
	
	@SuppressWarnings("rawtypes")
	public static List combo(EntityManager entityManagerR, String EntityName) {

		@SuppressWarnings("unused")
		ObjectMapper mapper = new ObjectMapper();

		return entityManagerR.createQuery("select a from " + EntityName + " a").getResultList();

	}

}
