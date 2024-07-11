package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.model.MdmBsnUnitGroup;
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.repository.IMdmBsnUnitGroupRepository;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.dto.BugMappingDto;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.model.BugMapping;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.repository.IBugMappingRepository;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.service.IBugMappingService;
import com.valoores.v21.usm.backend.CustomResponse;

@Service
public class BugMappingServiceImpl implements IBugMappingService {

	@Autowired
	private IBugMappingRepository bugMappingRepository;

	@Autowired
	private EntityManager entityManagerR;

	@Autowired
	private IMdmBsnUnitGroupRepository mdmBsnUnitGroupRepository;

	@Override
	public List<ObjectNode> getAllBugMapping() {

		return ObjectToJsonRepository.getJson(entityManagerR,
				"select a.suiteLdapBsngpId as suiteLdapBsngpId, " + "a.bsnGroupId as bsnGroupId, "
						+ "a.ldapConfigId as ldapConfigId, " + "a.ldapRoleLinkedToType as ldapRoleLinkedToType, "
						+ "a.ldapRoleLinkedToDesc as ldapRoleLinkedToDesc, " + "a.creationDate as creationDate, "
						+ "a.createdBy as createdBy, " + "a.updateDate as updateDate, " + "a.updatedBy as updatedBy"
						+ " FROM BugMapping a");
	}

	@SuppressWarnings("unused")
	private BugMappingDto mapEntityToDto(BugMapping bugMapping) {
		BugMappingDto responseDto = new BugMappingDto();

		responseDto.setSuiteLdapBsngpId(bugMapping.getSuiteLdapBsngpId());
		responseDto.setBsnGroupId(bugMapping.getBsnGroupId());
		responseDto.setLdapConfigId(bugMapping.getLdapConfigId());
		responseDto.setLdapRoleLinkedToType(bugMapping.getLdapRoleLinkedToType());
		responseDto.setLdapRoleLinkedToDesc(bugMapping.getLdapRoleLinkedToDesc());
		responseDto.setCreationDate(bugMapping.getCreationDate());
		responseDto.setCreatedBy(bugMapping.getCreatedBy());
		responseDto.setUpdateDate(bugMapping.getUpdateDate());
		responseDto.setUpdatedBy(bugMapping.getUpdatedBy());

		return responseDto;
	}

	@Transactional
	@Override
	public CustomResponse addBugMapping(BugMappingDto bugMappingDto) {
		BugMapping bugMapping = new BugMapping();

		bugMapping.setSuiteLdapBsngpId(bugMappingDto.getSuiteLdapBsngpId());
		bugMapping.setBsnGroupId(bugMappingDto.getBsnGroupId());
		bugMapping.setLdapConfigId(bugMappingDto.getLdapConfigId());
		bugMapping.setLdapRoleLinkedToType(bugMappingDto.getLdapRoleLinkedToType());
		bugMapping.setLdapRoleLinkedToDesc(bugMappingDto.getLdapRoleLinkedToDesc());
		bugMapping.setCreationDate(bugMappingDto.getCreationDate());
		bugMapping.setCreatedBy(bugMappingDto.getCreatedBy());
		bugMapping.setUpdateDate(bugMappingDto.getUpdateDate());
		bugMapping.setUpdatedBy(bugMappingDto.getUpdatedBy());

		bugMappingRepository.save(bugMapping);
		return CustomResponse.builder().code("0").status("success").id(bugMapping.getSuiteLdapBsngpId())
				.description("SAVED SUCCESSFULLY").build();

	}

	@Override
	public List<ObjectNode> getLDAPConfigurationn() {

		return ObjectToJsonRepository.getJson(entityManagerR,
				"select ldapConfigId as ldapConfigId , ldapServerUrl as ldapServerUrl  , ldapSearchBase as ldapSearchBase  , ldapSearchBaseRoot as ldapSearchBaseRoot "
						+ "from LdapConfiguration ");

	}

	@Override
	public List<MdmBsnUnitGroup> getMdmBsnUnitGroup() {
		return mdmBsnUnitGroupRepository.findAll();

	}

	@Transactional
	public CustomResponse deleteBugMapping(long suiteLdapBsngpId) {

		bugMappingRepository.deleteById((long) suiteLdapBsngpId);
		return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();

	}
}
