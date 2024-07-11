package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto.USMAdRoleMappingDto;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto.USMAdRoleMappingLdapConfiDto;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.model.USMAdRoleMapping;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.model.USMAdRoleMappingLdapConfig;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.repository.IUSMAdRoleMappingRepository;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.service.IUSMAdRoleMappingService;
import com.valoores.v21.usm.backend.CustomResponse;

@Service
public class USMAdRoleMappingServiceImpl implements IUSMAdRoleMappingService {

	@Autowired
	private IUSMAdRoleMappingRepository usmRoleMappingRepository;

	@Autowired
	private EntityManager entityManagerR;

	private USMAdRoleMappingLdapConfiDto mapEntityToDto(USMAdRoleMappingLdapConfig usmAdRoleMappingLdapConfig) {
		USMAdRoleMappingLdapConfiDto responseDto = new USMAdRoleMappingLdapConfiDto();
		responseDto.setId(usmAdRoleMappingLdapConfig.getConfigId());
		responseDto.setName(usmAdRoleMappingLdapConfig.getConfigBasePath());

		return responseDto;
	}

	@Override
	public List<USMAdRoleMappingLdapConfiDto> getLdapConfCombo() {
		List<USMAdRoleMappingLdapConfiDto> usmAdRoleMappingLdapConfiDtos = new ArrayList<>();
		usmRoleMappingRepository.getLdapConfCombo().forEach(x -> {

			USMAdRoleMappingLdapConfiDto usmAdRoleMappingLdapConfiDto = mapEntityToDto(x);
			usmAdRoleMappingLdapConfiDtos.add(usmAdRoleMappingLdapConfiDto);

		});
		return usmAdRoleMappingLdapConfiDtos;
	}

	@Override
	public List<Syslines> getObjectTypeCombo() {
		return usmRoleMappingRepository.getObjectTypeCombo();
	}

	@Transactional
	@Override
	public CustomResponse addUSMAdRoleMapping(USMAdRoleMappingDto adRoleMappingDto)

	{

		CustomResponse resp = CustomResponse.builder().build();
		USMAdRoleMapping rolemapping = new USMAdRoleMapping();
		long providedRoleMappingId = adRoleMappingDto.getId();

		if (usmRoleMappingRepository.findById(providedRoleMappingId) != null) {
			resp.setCode("1");
			resp.setStatus("Fail.");
			resp.setDescription("USM Role ['" + providedRoleMappingId + "'] already exists!");

		} else {
			rolemapping.setId(adRoleMappingDto.getId());
			rolemapping.setRoleId(adRoleMappingDto.getRoleId());
			rolemapping.setObjectTypeName(adRoleMappingDto.getObjectTypeName());
			rolemapping.setObjectCN(adRoleMappingDto.getObjectCN());
			rolemapping.setCreationDate(adRoleMappingDto.getCreationDate());
			rolemapping.setUpdateDate(adRoleMappingDto.getUpdateDate());
			rolemapping.setCreatedBy(adRoleMappingDto.getCreatedBy());

			usmRoleMappingRepository.save(rolemapping);
			return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();
		}
		return resp;
	}

	@Override
	public CustomResponse deleteUSMAdRoleMapping(long id) {

		USMAdRoleMapping rolemapping = usmRoleMappingRepository.findById(id);

		if (rolemapping != null) {
			usmRoleMappingRepository.deleteById((long) id);
			return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
		}
		return CustomResponse.builder().code("1").status("fail").description("ERROR!").build();
	}

	@Override
	public List<ObjectNode> getRoleMappingNameId(String id) {

		String sqlCond = "";
		Integer idParam = Integer.parseInt(id);

		if (id != "")
			sqlCond = " and rd.id = " + idParam;

		return ObjectToJsonRepository.getJson(entityManagerR,
				"select ld.configUrl as ldapServer , ld.configBasePath as basePath  , rs.objectTypeName as ldapObjType  , rs.objectCN as ldapObjCN "
						+ "from USMAdRoleMapping rs," + "USMAdRoleMappingLdapConfig ld ," + "USMRole rd,"
						+ "Syslines rsys" + " where rd.id = rs.roleId " + "   and rsys.id = rs.objectTypeName "
						+ "	  and rsys.heaCode = 439 " + sqlCond + "");

	}

	@Override
	public List<ObjectNode> getLDAPConfiguration() {

		return ObjectToJsonRepository.getJson(entityManagerR,
				"select ldapConfigId as ldapConfigId , ldapServerUrl as ldapServerUrl  , ldapSearchBase as ldapSearchBase  , ldapSearchBaseRoot as ldapSearchBaseRoot "
						+ "from LdapConfiguration ");

	}

}
