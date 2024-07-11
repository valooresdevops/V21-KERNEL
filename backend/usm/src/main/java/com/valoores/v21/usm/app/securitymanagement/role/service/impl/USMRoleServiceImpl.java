
package com.valoores.v21.usm.app.securitymanagement.role.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.common.syslines.repository.ISyslinesRepository;
import com.valoores.v21.usm.app.securitymanagement.role.dto.USMRoleDto;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRole;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRoleMulti;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMSanction;
import com.valoores.v21.usm.app.securitymanagement.role.repository.IUSMRoleMultiRepository;
import com.valoores.v21.usm.app.securitymanagement.role.repository.IUSMRoleRepository;
import com.valoores.v21.usm.app.securitymanagement.role.service.IUSMRoleService;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.IUSMMdmBsnUnitGroupStrucRepository;
import com.valoores.v21.usm.backend.CustomResponse;
import com.valoores.v21.usm.common.ObjectToJsonRepository;

@Service
public class USMRoleServiceImpl implements IUSMRoleService {

    @Autowired
    private EntityManager entityManagerR;

    @Resource
    private IUSMRoleRepository usmRoleRepository;

    @Resource
    private IUSMRoleMultiRepository usmRoleMultiRepository;

    @Resource
    private ISyslinesRepository syslinesRepository;

    @Resource
    private IUSMMdmBsnUnitGroupStrucRepository usmMdmBsnUnitGroupStrucRepository;

    @Transactional
    @Override
    public CustomResponse addUSMRole(USMRoleDto theDto) {

        long roleId = 0;
        CustomResponse resp = CustomResponse.builder().build();
        USMRole usmRole = new USMRole();
        System.out.println(" role id ------- >>>>>>>>>>>>> "+theDto.getId());
        System.out.println("getBugType is :::::::::::::: "+theDto.getBugType());
        System.out.println("getBugName is :::::::::::::: "+theDto.getBugName());

        String providedRoleName = theDto.getRoleName();

        if (usmRoleRepository.findByRoleName(providedRoleName) != null) {

            resp.setCode("1");
            resp.setStatus("Fail.");
            resp.setDescription("USM Role ['" + providedRoleName + "'] already exists!");

        }

        if(usmRoleRepository.findById(theDto.getId()) != null) {
            System.out.println(" in >>>>>>>>>>>>>>>>>>>>>");

            Date now = new Date();
            theDto.getBugName().stream().forEach(bugId -> {

                USMRoleMulti usmRoleMulti = new USMRoleMulti();
//                usmRoleMulti.setRoleId(usmRole.getId());
                usmRoleMulti.setRoleId(theDto.getId());
                usmRoleMulti.setBugId(Integer.parseInt(bugId));
                usmRoleMulti.setBugTypeId(theDto.getBugType());
                usmRoleMulti.setCreationDate(now);
                usmRoleMulti.setCreatedBy(Long.parseLong(theDto.getUserId()));
                usmRoleMultiRepository.save(usmRoleMulti);
            });

        }

        else {

            usmRole.setRoleName(theDto.getRoleName());
            usmRole.setRoleType(theDto.getRoleType());

            if (theDto.getIsDualAuthentication().equals("true")) {
                usmRole.setIsDualAuthentication("1");
                
            } else {
                usmRole.setIsDualAuthentication("0");
            }

            Date now = new Date();
            usmRole.setCreationDate(now);
            usmRole.setCreatedBy(theDto.getUserId());
            usmRoleRepository.save(usmRole);
            roleId = usmRole.getId();

            if (roleId != 0) {
                theDto.getBugName().stream().forEach(bugId -> {

                    USMRoleMulti usmRoleMulti = new USMRoleMulti();
                    usmRoleMulti.setRoleId(usmRole.getId());
                    usmRoleMulti.setBugId(Integer.parseInt(bugId));
                    usmRoleMulti.setBugTypeId(theDto.getBugType());
                    usmRoleMulti.setCreationDate(now);
                    usmRoleMulti.setCreatedBy(Long.parseLong(theDto.getUserId()));
                    usmRoleMultiRepository.save(usmRoleMulti);
                });
            }

            return CustomResponse.builder().code("0").status("success").id(usmRole.getId())
                    .description("SAVED SUCCESSFULLY").build();
        }
        return resp;
    }

    @Transactional
    @Override
    public CustomResponse updateUSMRole(long id, USMRoleDto theDto) {

        USMRole usmRole = usmRoleRepository.findById(id);
        CustomResponse resp = CustomResponse.builder().build();

        System.out.println(" role id >>>>>>>>>>>>> update mode ");


        // USM ROLE NOT FOUND GIVEN THE ID PROVIDED
        if (usmRole == null) {

            resp.setCode("1");
            resp.setStatus("Fail");
            resp.setDescription("USM Role with id = " + theDto.getId() + " not found!");
        } else {

            usmRole.setRoleName(theDto.getRoleName());
            usmRole.setRoleType(theDto.getRoleType());
            usmRole.setUpdatedBy(theDto.getUserId());
            if (theDto.getIsDualAuthentication().equals("true")) {
                usmRole.setIsDualAuthentication("1");
            } else {
                usmRole.setIsDualAuthentication("0");
            }

            Date now = new Date();
            usmRole.setUpdateDate(now);
            usmRole.setUpdatedBy(theDto.getUserId());
            usmRoleRepository.save(usmRole);

            usmRoleMultiRepository.deleteByRoleId(id);
            theDto.getBugName().stream().forEach(bugId -> {
                USMRoleMulti usmRoleMulti = new USMRoleMulti();
                usmRoleMulti.setRoleId(usmRole.getId());
                usmRoleMulti.setBugId(Integer.parseInt(bugId));
                usmRoleMulti.setBugTypeId(theDto.getBugType());
                usmRoleMulti.setCreationDate(now);
                usmRoleMulti.setCreatedBy(Long.parseLong(theDto.getUserId()));
                usmRoleMultiRepository.save(usmRoleMulti);
            });

            resp.setId(usmRole.getId());
            resp.setCode("0");
            resp.setStatus("success");
            resp.setDescription("updated successfully!");

        }

        return resp;
    }

    @Transactional
    public CustomResponse deleteUSMRole(long id) {

        USMRole usmRole = usmRoleRepository.findById(id);

        // Remove the related role from role entity.
        if (usmRole != null) {

            usmRoleMultiRepository.deleteAllByRoleId((long) id);

            usmRoleRepository.deleteById((long) id);

            return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
        }
        return CustomResponse.builder().code("1").status("fail").description("ERROR!").build();
    }

    @SuppressWarnings("unused")
    private USMRoleDto mapEntityToDto(USMRole usmRole) {
        USMRoleDto responseDto = new USMRoleDto();

        String roleTypeName = syslinesRepository.findNameByIdAndHeaCode(usmRole.getRoleType(), 420);
        responseDto.setId(usmRole.getId());
        responseDto.setRoleName(usmRole.getRoleName());
        responseDto.setRoleTypeName(roleTypeName);
        responseDto.setUpdatedBy(usmRole.getUpdatedBy());
        return responseDto;
    }

    @Override
    public USMRoleDto getUSMRoleByRoleId(long id) {

//    	ronyyyyyyyyyyyyyyyyyyyyyyy
        USMRoleDto theDto = new USMRoleDto();
        System.out.println("id >>>>>>>>>>>>>>>>>>>>>> "+id);
        USMRole usmRole = usmRoleRepository.findById(id);
        theDto.setId(usmRole.getId());
        theDto.setRoleName(usmRole.getRoleName());
        theDto.setRoleType(usmRole.getRoleType());
        theDto.setIsDualAuthentication(usmRole.getIsDualAuthentication());
        Set<String> bug = new HashSet<>();
        List<USMRoleMulti> usmRoleMulti = usmRoleMultiRepository.findByRoleId(usmRole.getId());

        usmRoleMulti.stream().forEach(bugIds -> {

            if (bugIds.getBugId() != null) {
                bug.add(String.valueOf(bugIds.getBugId()));
            }

            theDto.setBugType(bugIds.getBugTypeId());

        });

        theDto.setBugName(bug);

        return theDto;
    }

    @Override
    public List<ObjectNode> getAllUSMRoles() {

//        List<USMRoleDto> usmRolesDtos = new ArrayList<>();
//        usmRoleRepository.findAll().forEach(usmRole -> {
//            USMRoleDto usmRoleDto = mapEntityToDto(usmRole);
//            usmRolesDtos.add(usmRoleDto);
//            System.out.println("usmRolesDtos >>>>>>>>>>>>>>>>>>>>>>> : "+usmRolesDtos);
//        });
        return ObjectToJsonRepository.getJson(entityManagerR,   " SELECT     A.id AS id, " + 
												        		"			 A.roleName as roleName , " + 
												        		"			 A.roleType as roleTypeName  " + 
												        		"			 from USMRole A ")	;
    }

    @Override
    public USMRole getUSMRoleByRoleName(String name) {
        return usmRoleRepository.findByRoleName(name);
    }

    @Override
    public List<Syslines> getBugTypeCombo() {
        return usmRoleRepository.getBugTypeCombo();
    }


    @Override
    public List<ObjectNode> getBugNameCombo(String id) {

//        String sqlCond = "";
//        Integer idParam;
//        if (!id.equals(null)) {
//            idParam = Integer.parseInt(id);
//            if (idParam != -1)
//                sqlCond = " and s.bsnGroupTypeCode = " + idParam;
//        } else {
//            sqlCond = "";
//        }
//
//        return ObjectToJsonRepository.getJson(entityManagerR,
//                "select g.bsnGroupId as id ,g.bsnGroupName as name"
//                        + " from MdmBsnUnitGroup g,MdmBsnUnitGroupCode s,Syslines rs" + " where g.bsnGroupId = s.id"
//                        + " and rs.id = s.bsnGroupTypeCode" + " and rs.heaCode = 6023" + " and g.bsnGroupId <> 0" + " "
//                        + sqlCond + "");
    	
    	String query = "";
    	if (!id.equals(null) && !id.equals("")) {
    		if(id.equals("21")) {
    			query = "SELECT G2.bsnGroupId as ID , G2.bsnGroupName as NAME"
    					+ "FROM MdmBsnAssociatedUnitGrp K, MdmBsnUnitGroup G1, "
    					+ "		MdmBsnUnitGroup G2, MdmBusinessUnit U1, "
    					+ "		MdmBsnUnitGroupCode U2, MdmBranch B, RefComCountry C"
    					+ "WHERE K.bsnGroupPId = G1.bsnGroupId" 
    					+ "  AND K.bsnGroupCId = G2.bsnGroupId"
    					+ "	 AND U1.bsnGroupId = G2.bsnGroupId" 
    					+ "  AND U2.bsnGroupId = G1.bsnGroupId"  
    					+ "	 AND B.branchId = U1.entCode" 
    					+ "	 AND C.bsnGroupId = U2.bsnGroupId" 
    					+ "	 AND U1.bsnUnitTypeCode = 56" 
    					+ "	 AND U2.bsnGroupTypeCode = 2";
    		}
    	}
    	
    	return ObjectToJsonRepository.getJson(entityManagerR, query);
    	
    }

	@Override
	public List<ObjectNode> getRoleSanctionList(long id) {

		return ObjectToJsonRepository.getJson(entityManagerR,"    select src.entitySourceId AS id, "
				+ "  src.entitySourceCode AS refCode, "
				+ "  src.entitySourceName AS name, "
				+ "  src.entitySourceNameAbbrev AS abbrev, "
				+ "  rd.id AS  roleId, rd.roleName AS  roleName \r\n" + 
				"      FROM USMRoleSnctionLstEntSrc rl, "
				+ "    USMRoleDefinition rd, "
				+ "    ImpSanctionListEntitySrc src \r\n" + 
				"      WHERE rl.id = rd.id\r\n" + 
				"        and rl.entitySourceId = src.entitySourceId\r\n" + 
				"        and rl.id = " + id);
	}

	@Override
	public CustomResponse addUSMRoleSnctionLstEntSrc(USMSanction roleSrc) {
		
		String sanctionList ="     insert into USMDBA.USM_ROLE_SNCTION_LST_ENT_SRC(role_id,entity_source_id,created_by)\r\n" + 
				"     select "+roleSrc.getRoleId()+", "+roleSrc.getEntitySourecId()+" , "+ roleSrc.getCreatedBy() + 
				"      from dual\r\n" + 
				"      where not exists (select 1 from USMDBA.USM_ROLE_SNCTION_LST_ENT_SRC src \r\n" + 
				"                         where src.role_id = "+ roleSrc.getRoleId()+" and src.entity_source_id = "+ roleSrc.getEntitySourecId()+")";
		
//		String sourceCode = (String) entityManagerR.createNativeQuery(sanctionList).getSingleResult();
		
		System.out.println("sourceCode >>>>>>>>>>>>>>>>>>>>>>>>>> : " + sanctionList);
		return null;
	}

	@Override
	public List<ObjectNode> getAllRoleSanctionList() {
		return ObjectToJsonRepository.getJson(entityManagerR,"    select src.entitySourceId AS id, "
				+ "  src.entitySourceName AS name " +
				"      FROM USMRoleSnctionLstEntSrc rl, "
				+ "    USMRoleDefinition rd, "
				+ "    ImpSanctionListEntitySrc src \r\n" + 
				"      WHERE rl.id = rd.id\r\n" + 
				"        and rl.entitySourceId = src.entitySourceId\r\n");
	}
}

