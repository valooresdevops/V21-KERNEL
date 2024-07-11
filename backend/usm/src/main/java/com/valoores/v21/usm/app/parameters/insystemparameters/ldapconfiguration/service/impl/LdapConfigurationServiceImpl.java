package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.naming.directory.DirContext;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.dto.LdapConfigurationDto;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.model.LdapConfiguration;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.repository.ILdapConfigurationRepository;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.service.ILdapConfigurationService;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.service.impl.USMUserServiceImpl;
import com.valoores.v21.usm.backend.CustomResponse;

@Service
public class LdapConfigurationServiceImpl implements ILdapConfigurationService {

	@Autowired
	private ILdapConfigurationRepository ldapConfigurationRepository;
	
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Autowired
	private EntityManager entityManagerR;

	@SuppressWarnings("unused")
	@Autowired
	private USMUserServiceImpl usmUserServiceImpl;

	static DirContext ldapContext;

	@SuppressWarnings("unused")
	private LdapConfigurationDto mapEntityToDto(LdapConfiguration ldapConfiguration) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		LdapConfigurationDto responseDto = new LdapConfigurationDto();
		responseDto.setLdapConfigId(ldapConfiguration.getLdapConfigId());
		responseDto.setLdapServerUrl(ldapConfiguration.getLdapServerUrl());
		responseDto.setLdapServerPort(ldapConfiguration.getLdapServerPort());
		responseDto.setLdapAdminLogin(ldapConfiguration.getLdapAdminLogin());
		responseDto.setLdapAdminPwd(ldapConfiguration.getLdapAdminPwd());
		responseDto.setLdapSearchBase(ldapConfiguration.getLdapSearchBase());
		responseDto.setCreatedBy(ldapConfiguration.getCreatedBy());
		responseDto.setIsLdapConfigActive(ldapConfiguration.getIsLdapConfigActive());
		responseDto.setLdapSearchBaseRoot(ldapConfiguration.getLdapSearchBaseRoot());
		responseDto.setLdapConnectionTimeoutPeriod(ldapConfiguration.getLdapConnectionTimeoutPeriod());
		responseDto.setLdapResponseTimeoutPeriod(ldapConfiguration.getLdapResponseTimeoutPeriod());

		return responseDto;
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

	@Override
	public List<ObjectNode> getAllLdapConfig() {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"select a.ldapConfigId as ldapConfigId, " + "a.ldapServerUrl as ldapServerUrl, "
						+ "a.ldapServerPort as ldapServerPort, " + "a.ldapAdminLogin as ldapAdminLogin, "
						+ "a.ldapAdminPwd as ldapAdminPwd, " + "a.ldapSearchBase as ldapSearchBase, "
						+ "a.isLdapConfigActive as isLdapConfigActive, "
						+ "a.ldapSearchBaseRoot as ldapSearchBaseRoot, "
						+ "a.ldapConnectionTimeoutPeriod as ldapConnectionTimeoutPeriod, "
						+ "a.ldapResponseTimeoutPeriod as ldapResponseTimeoutPeriod " + "FROM LdapConfiguration a");
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

	@Transactional
	@Override
	public CustomResponse addLdapConfiguration(LdapConfigurationDto ldapConfigurationDto) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		LdapConfiguration ldapconfiguration = new LdapConfiguration();

		ldapconfiguration.setLdapServerUrl(ldapConfigurationDto.getLdapServerUrl());
		ldapconfiguration.setLdapServerPort(ldapConfigurationDto.getLdapServerPort());
		ldapconfiguration.setLdapAdminLogin(ldapConfigurationDto.getLdapAdminLogin());
		ldapconfiguration.setLdapAdminPwd(ldapConfigurationDto.getLdapAdminPwd());
		ldapconfiguration.setLdapSearchBase(ldapConfigurationDto.getLdapSearchBase());
		ldapconfiguration.setCreatedBy(ldapConfigurationDto.getCreatedBy());
		ldapconfiguration.setIsLdapConfigActive(ldapConfigurationDto.getIsLdapConfigActive());
		ldapconfiguration.setLdapSearchBaseRoot(ldapConfigurationDto.getLdapSearchBaseRoot());
		ldapconfiguration.setLdapConnectionTimeoutPeriod(ldapConfigurationDto.getLdapConnectionTimeoutPeriod());
		ldapconfiguration.setLdapResponseTimeoutPeriod(ldapConfigurationDto.getLdapResponseTimeoutPeriod());
		ldapConfigurationRepository.save(ldapconfiguration);

//		{
//		  "ldapServerUrl": "10.1.10.1",
//		  "ldapServerPort": "389",
//		  "ldapAdminLogin": "beirut\\antoun.k",
//		  "ldapAdminPwd": "Valoores@2021",
//		  "ldapSearchBase": "DC=beirut,DC=softsolutions-group,DC=com",
//		  "createdBy": -88,
//		  "isLdapConfigActive": "0",
//		  "ldapSearchBaseRoot": "dc=beirut,dc=softsolutions-group,dc=com",
//		  "ldapConnectionTimeoutPeriod": 0,
//		  "ldapResponseTimeoutPeriod": 0
//		}

//		try {
//			Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
//			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//			ldapEnv.put(Context.PROVIDER_URL, "ldap://" + ldapConfigurationDto.getLdapServerUrl() + ":"
//					+ ldapConfigurationDto.getLdapServerPort());
//			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
//			ldapEnv.put(Context.SECURITY_PRINCIPAL, ldapConfigurationDto.getLdapAdminLogin());
//			ldapEnv.put(Context.SECURITY_CREDENTIALS, ldapConfigurationDto.getLdapAdminPwd());
//			ldapEnv.put(Context.REFERRAL, "follow");
//			
//			
//
//			ldapContext = new InitialDirContext(ldapEnv);
//			SearchControls searchCtls = new SearchControls();
//			String returnedAtts[] = { "sn", "givenName", "samAccountName" };
//			searchCtls.setReturningAttributes(returnedAtts);
//			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
////			String searchFilter = "(&(objectClass=user))";
//			String searchFilter = "(&(objectCategory=person)(objectClass=user)(!(userAccountControl:1.2.840.113556.1.4.803:=2)))";
//			String searchBase = ldapConfigurationDto.getLdapSearchBase();
//			NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);
//
//			USMUserDto usmUserDto = new USMUserDto();
//
//			JSONArray ArrayOfLdapUsers = new JSONArray();
//			JSONObject jsonObject = null;
//			
//			while (answer.hasMore()) {
//				SearchResult obj = (SearchResult) answer.next();
//				Attributes atts = obj.getAttributes();
//
//				jsonObject = new JSONObject();
//				
//				for (NamingEnumeration ne = atts.getAll(); ne.hasMoreElements();) {
//					Attribute attr = (Attribute) ne.next();
//					String attrID = attr.getID();
//					
//					for (Enumeration vals = attr.getAll(); vals.hasMoreElements();) {
//						Object objZ = vals.nextElement();
//						String nxtElmt = objZ.toString();
//
//						if(attr.toString().indexOf("sn:") != -1)
//							jsonObject.put("sn", nxtElmt);
//						
//						if(attr.toString().indexOf("givenName:") != -1)
//							jsonObject.put("givenName", nxtElmt);
//						
//						if(attr.toString().indexOf("sAMAccountName:") != -1)
//							jsonObject.put("sAMAccountName", nxtElmt);
//					}
//				}
//				ArrayOfLdapUsers.put(jsonObject);
//			}
//			System.out.println("11111111111111111111");
//			System.out.println(ArrayOfLdapUsers);
//			ldapContext.close();
//			
////			for(int i = 0; i <= ArrayOfLdapUsers.length(); i ++) {
////				System.out.println("ArrayOfLdapUsers ================= " + ArrayOfLdapUsers.getJSONObject(i));
////			}
//			
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}

		return CustomResponse.builder().code("0").status("success").id(ldapconfiguration.getLdapConfigId())
				.description("SAVED SUCCESSFULLY").build();
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

	@Transactional
	@Override
	public CustomResponse updateLdapConfiguration(long ldapConfigId, LdapConfigurationDto ldapConfigurationDto) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		LdapConfiguration ldapConfiguration = ldapConfigurationRepository.findByldapConfigId(ldapConfigId);
		CustomResponse resp = CustomResponse.builder().build();

		if (ldapConfiguration == null) {

			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription(
					"LDAP Configuration with id = " + ldapConfigurationDto.getLdapConfigId() + " not found!");

		} else {

			ldapConfiguration.setLdapServerUrl(ldapConfigurationDto.getLdapServerUrl());
			ldapConfiguration.setLdapServerPort(ldapConfigurationDto.getLdapServerPort());
			ldapConfiguration.setLdapAdminLogin(ldapConfigurationDto.getLdapAdminLogin());
			ldapConfiguration.setLdapAdminPwd(ldapConfigurationDto.getLdapAdminPwd());
			ldapConfiguration.setLdapSearchBase(ldapConfigurationDto.getLdapSearchBase());
			ldapConfiguration.setCreatedBy(ldapConfigurationDto.getCreatedBy());
			ldapConfiguration.setIsLdapConfigActive(ldapConfigurationDto.getIsLdapConfigActive());
			ldapConfiguration.setLdapSearchBaseRoot(ldapConfigurationDto.getLdapSearchBaseRoot());
			ldapConfiguration.setLdapConnectionTimeoutPeriod(ldapConfigurationDto.getLdapConnectionTimeoutPeriod());
			ldapConfiguration.setLdapResponseTimeoutPeriod(ldapConfigurationDto.getLdapResponseTimeoutPeriod());
			ldapConfigurationRepository.save(ldapConfiguration);
			return CustomResponse.builder().code("0").status("success").id(ldapConfiguration.getLdapConfigId())
					.description("UPDATED SUCCESSFULLY").build();
		}
		return resp;
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

	@Transactional
	public CustomResponse deleteLdapConfiguration(long ldapConfigId) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		ldapConfigurationRepository.deleteById((long) ldapConfigId);
		return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

	@Override
	public LdapConfigurationDto getLdapConfigurationByLdapConfigurationId(long id) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		LdapConfigurationDto ldapConfigurationDto = new LdapConfigurationDto();
		LdapConfiguration ldapconfiguration = ldapConfigurationRepository.findByLdapConfigId(id);
		ldapConfigurationDto.setLdapServerUrl(ldapconfiguration.getLdapServerUrl());
		ldapConfigurationDto.setLdapServerPort(ldapconfiguration.getLdapServerPort());
		ldapConfigurationDto.setLdapAdminLogin(ldapconfiguration.getLdapAdminLogin());
		ldapConfigurationDto.setLdapAdminPwd(ldapconfiguration.getLdapAdminPwd());
		ldapConfigurationDto.setLdapSearchBase(ldapconfiguration.getLdapSearchBase());
		ldapConfigurationDto.setCreatedBy(ldapconfiguration.getCreatedBy());
		ldapConfigurationDto.setIsLdapConfigActive(ldapconfiguration.getIsLdapConfigActive());
		ldapConfigurationDto.setLdapSearchBaseRoot(ldapconfiguration.getLdapSearchBaseRoot());
		ldapConfigurationDto.setLdapConnectionTimeoutPeriod(ldapconfiguration.getLdapConnectionTimeoutPeriod());
		ldapConfigurationDto.setLdapResponseTimeoutPeriod(ldapconfiguration.getLdapResponseTimeoutPeriod());

		return ldapConfigurationDto;
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

}
