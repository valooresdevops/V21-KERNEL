package com.valoores.v21.usm.app.securitymanagement.accessrights.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.menu.repository.IUSMMenuRepository;
import com.valoores.v21.usm.app.securitymanagement.accessrights.service.IUSMAccessService;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRoleMulti;
import com.valoores.v21.usm.app.securitymanagement.role.repository.IUSMRoleMultiRepository;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMMultiMiscDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserMulti;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.IUSMUserMultiRepository;
import com.valoores.v21.usm.backend.CustomResponse;

@Service
public class USMAccessServiceImpl implements IUSMAccessService{

    @Resource
    private IUSMUserMultiRepository usmUserMultiRepository;

    @Resource
    private IUSMMenuRepository usmMenuRepository;

    @Resource
    private IUSMRoleMultiRepository usmRoleMultiRepository;

    @Autowired
    private EntityManager entityManagerR;


    @Override
    public List<ObjectNode> getAllUSMAccess(long id,String userRoleId, String menupaths, long appName, String appMenu,String subLevel,String inColumn)
     {
       System.out.println("ronyy 22222222222222222222222222222222222222222222222222");
       @SuppressWarnings("unused")
    String sqlCond = "";
    // Convert appName to a three-digit format
       String formattedAppName = String.format(Locale.US, "%03d", appName);
       List<ObjectNode> result ;
       System.out.println("subLevel is : "+subLevel);
       System.out.println("formattedAppName >>>>>> : " +formattedAppName);
//        if (subLevel.equals("false"))
//       {
//        sqlCond =  "  AND MMI.menuCode = '"+appMenu+"'" ;
//       }
//       else
//       {
//           sqlCond =  "  AND MMI.menuCode like ('"+appMenu+"%')" ;
//       }

      if(menupaths.equals("userMgmt"))
       {
          System.out.println("Rony >>>>>>>>>>>>>>");
          System.out.println("id is >> "+id);
          System.out.println("menupaths is >> "+menupaths);
          System.out.println("formattedAppName is >> "+formattedAppName);
          System.out.println("appMenu is >> "+appMenu);
          System.out.println("subLevel is >> "+subLevel);
          System.out.println("inColumn is >> "+inColumn);
          
//          userRole="select distinct MMI.roleId from USMUserMulti MMI where MMI.userId="+id
//        		     + " and MMI.roleId<>0";
          
//        
//        result = ObjectToJsonRepository.getJson(entityManagerR, "select MU.menuName as name, MX.isDisplay as isDisplay, MMI.id as id, "
//                                             + " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, "
//                                             + " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate, "
//                                             + " round((length(MU.menuCode)-3)/3)+1 as orderCode "
//                                                + "  from USMUserMulti MMI, "
//                                             + " USMAccessMatrix MX, "
//                                             + "     USMMenu MU "
//                                             + " where MMI.accessCode = MX.accessCode"
//                                             + " AND MMI.menuCode = MU.menuCode"
//                                             + " AND MMI.userId = " + id
//                                             +  sqlCond
//                                             + " order by MMI.menuCode"
//                                            );//was working when the trigger was on usm_user_misc_info
          //this is a partial code to call the main menus from usm menu but it needs to be completed by calling those saved in the multi table
//          result = ObjectToJsonRepository.getJson(entityManagerR,"select MU.menuName as name, MX.isDisplay as isDisplay, " + 
//          		                                           " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, " + 
//          		                                            " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate," + 
//          		                                             " round((length(MU.menuCode)-3)/3)+1 as orderCode "+
//          		                                            " from USMAccessMatrix MX,USMMenu MU"+
//          		                                             " where MU.menuPCode= "+appName+"and MX.accessCode= '0000000' ");
         
          if (userRoleId.equals("-1")) {
//              result = ObjectToJsonRepository.getJson(entityManagerR, "select MU.menuName as name, MX.isDisplay as isDisplay, MMI.id as id, "
//                      + " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, "
//                      + " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate, "
//                      + " round((length(MU.menuCode)-3)/3)+1 as orderCode "
//                      + " from USMUserMulti MMI, "
//                      + " USMAccessMatrix MX, "
//                      + " USMMenu MU "
//                      + " where MMI.accessCode = MX.accessCode"
//                      +" and MMI.accessCode='0000000'"
//                      + " AND MU.menuCode like '"+appName+"%'"
//                      
//                      + " AND MMI.userId = " + id
//                      + sqlCond
//                      + " order by MMI.menuCode"
//              );
        	  result = ObjectToJsonRepository.getJson(entityManagerR, "select MU.menuName as name, MX.isDisplay as isDisplay, MMI.id as id, "
                      + " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, "
                      + " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate, "
                      + " round((length(MU.menuCode)-3)/3)+1 as orderCode "
                      + " from USMUserMulti MMI, "
                      + " USMAccessMatrix MX, "
                      + " USMMenu MU "
                      + " where MMI.accessCode = MX.accessCode"
                      + " AND MMI.menuCode = MU.menuCode"
                      + " AND MU.menuPCode ='"+formattedAppName+"'"
                      + " AND MMI.userId = " + id
                      + " order by MU.menuPCode"
              );
              return result;
        	  
          }

      

       }
       else  if(menupaths.equals("role"))
       System.out.println("role is >> "+id);
       System.out.println("menupaths is >> "+menupaths);
       System.out.println("appName is >> "+appName);
       System.out.println("appMenu is >> "+appMenu);
       System.out.println("subLevel is >> "+subLevel);
       System.out.println("inColumn is >> "+inColumn);
       {
//           result = ObjectToJsonRepository.getJson(entityManagerR, "select MU.menuName as name, MX.isDisplay as isDisplay, MMI.multiId as id, "
//                   + " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, "
//                   + " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate, "
//                   + " round((length(MU.menuCode)-3)/3)+1 as orderCode "
//                   + "  from USMRoleMulti MMI, "
//                   + "     USMRole MI, "
//                   + "     USMAccessMatrix MX, "
//                   + "     USMMenu MU "
//                   + " where MMI.roleId = MI.id"
//                   + " AND MMI.accessCode = MX.accessCode"
//                   + " AND MMI.menuCode = MU.menuCode"
//                   + " AND MMI.roleId = " + id
//                 +  sqlCond
//                   + " order by MMI.menuCode"
//                  );//was working when the trigger was on usm_user_misc_info
    	   //this is a partial code to call the main menus from usm menu but it needs to be completed by calling those saved in the multi table
//    	   result = ObjectToJsonRepository.getJson(entityManagerR,"select MU.menuName as name, MX.isDisplay as isDisplay, " + 
//                     " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, " + 
//                      " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate," + 
//                       " round((length(MU.menuCode)-3)/3)+1 as orderCode "+
//                      " from USMAccessMatrix MX,USMMenu MU"+
//                       " where MU.menuPCode= "+appName+"and MX.accessCode= '0000000' ");
          
//    	   result = ObjectToJsonRepository.getJson(entityManagerR, "select MU.menuName as name, MX.isDisplay as isDisplay, MMI.multiId as id, "
//                   + " MX.isAdd as isAdd, MX.isModify as isModify , MX.isDelete as isDelete, "
//                   + " MX.isPrint as isPrint , MX.isExport as isExport, MX.isTranslate as isTranslate, "
//                   + " round((length(MU.menuCode)-3)/3)+1 as orderCode "
//                   + " from USMRoleMulti MMI, "
//                   + " USMRole MI, "
//                   + " USMAccessMatrix MX, "
//                   + " USMMenu MU "
//                   + " where MMI.roleId = MI.id"
//                   + " AND MMI.accessCode = MX.accessCode"
//                   + " AND MMI.menuCode = MU.menuCode"
//                   + " AND MMI.roleId = " + id
//                   + sqlCond
//                   + " order by MMI.menuCode"
//           );
    	   result = ObjectToJsonRepository.getJson(entityManagerR, "SELECT MU.menuName AS name, MX.isDisplay AS isDisplay, MMI.multiId AS id, "
    		        + "MX.isAdd AS isAdd, MX.isModify AS isModify, MX.isDelete AS isDelete, "
    		        + "MX.isPrint AS isPrint, MX.isExport AS isExport, MX.isTranslate AS isTranslate, "
    		        + "ROUND((LENGTH(MU.menuCode) - 3) / 3) + 1 AS orderCode "
    		        + " from USMRoleMulti MMI, "
                    + " USMAccessMatrix MX, "
                    + " USMMenu MU "
                    + " where MMI.accessCode = MX.accessCode"
                    + " AND MMI.menuCode = MU.menuCode"
                    + " AND MU.menuPCode ='"+formattedAppName+"'"
                    + " AND MMI.roleId = " + id                   
                    + " order by MU.menuPCode");
    	   return result;

       }


    }

//
//    @Transactional
//    @Override
//    public CustomResponse updateUSMAccess(ArrayList<USMAccessDto> usmAccessDtoObjList , String menuPaths, Integer userLogedId) {
//
//        CustomResponse resp = CustomResponse.builder().build();
//        resp.setCode("1");
//        resp.setStatus("Fail");
//        resp.setDescription("USM Access Rights updated on ids = " );
//        String Desc = "USM Access Rights updated on ids = " ;
//
//        usmAccessDtoObjList.stream().forEach(dtoLst ->{
//
//           if(dtoLst.getModeType().equals("~updateRow~"))
//           {
//             Long id =Long.valueOf(dtoLst.getId());
//             String accessCode = dtoLst.getIsDisplay() + dtoLst.getIsAdd() + dtoLst.getIsModify() + dtoLst.getIsDelete() + dtoLst.getIsPrint() + dtoLst.getIsExport() + "0";
//            //accessCode.Integer accessCodes = Integer.parseInt(accessCode);
//             if(menuPaths.equals("userMgmt"))
//               {
//                usmUserMultiRepository.updateAccessRightOnUser(id, accessCode , userLogedId );
//               }
//             else
//               {
// usmRoleMultiRepository.updateAccessRightOnRole(id, accessCode, userLogedId);
//               }
//
//           }
//        });
//
//          return resp;
//    }


    @SuppressWarnings("unused")
    private void alert(String string) {
		
	}

	@Override
    public CustomResponse AddUSMApplication(USMMultiMiscDto usmMultiMiscDto) {
// /usm/src/main/java/com/valoores/v21/usm/app/menu/model/USMMenu.java
        //rony
        Date currentDate = new Date();
        List<USMUserMulti> usmList = new ArrayList<>();
        USMRoleMulti roles = new USMRoleMulti();
        // usm.setUserId(usmMultiMiscDto.getUserId());
        
        System.out.println("USMMMM>>>>>>>>>>>>>"+usmMultiMiscDto.getAccessCode());

        String jsonString=usmMultiMiscDto.getAccessCode();
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(jsonString);
			
		
		for (JsonNode element : jsonArray) {
			ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
		    list.add(object);
		}
        
        if (usmMultiMiscDto.getUserId()!=0 ) {
            for(int i=0;i<list.size();i++){
                USMUserMulti usm=new USMUserMulti();
                usm.setUserId(usmMultiMiscDto.getUserId());
                usm.setMenuCode(list.get(i).get("id").asText());
                usm.setAccessCode(list.get(i).get("access_code").asText());
                usm.setCreationDate(currentDate);
                System.out.println("USER ID>>>>>"+usm.getUserId());
                System.out.println("menu codee>>>>>"+usm.getMenuCode());
                System.out.println("ACCESS CODE>>>>>"+usm.getAccessCode());

                usm.setCreatedBy(usmMultiMiscDto.getCreatedBy());
        	 if (usmMultiMiscDto.getUserRoleId()!="-1" ) {
        		 usm.setRoleId(Integer.parseInt(usmMultiMiscDto.getUserRoleId()));
        	 }
             usmList.add(usm);
            }
        	

//             usm.setCreatedBy(Long.parseLong(usmMultiMiscDto.getCreatedBy()));
// System.out.println("USMMMM>>>>>>>>>>>>>"+usmMultiMiscDto.getUserRoleId());

             

             System.out.println("USM USER MULTI MISN INFO DATA>>>>>>"+usmMultiMiscDto);
             usmUserMultiRepository.saveAll(usmList);
       
       
            }
        else {
        	System.out.println("usmMultiMiscDto.getRoleId()="+usmMultiMiscDto.getRoleId());
        	 roles.setRoleId(usmMultiMiscDto.getRoleId());
         	//System.out.println("usmMultiMiscDto.getMenuCode()="+ usmMultiMiscDto.getMenuCode());
        	 roles.setMenuCode(usmMultiMiscDto.getMenuCode());
        	 roles.setAccessCode("0000000");
        	
        	 roles.setCreationDate(currentDate);
        	 //System.out.println("currentDate="+ currentDate);
        	 roles.setCreatedBy(usmMultiMiscDto.getCreatedBy());
        	 //System.out.println("usmMultiMiscDto.getCreatedBy()="+ usmMultiMiscDto.getCreatedBy());
        	 usmRoleMultiRepository.save(roles);
        }
    }catch(Exception ex){
            ex.printStackTrace();
    }
       

        
        
    //    USMMenu usm_Menu = new USMMenu();
//        usm_Menu.setMenuCode(usmMultiMiscDto.getMenuCode());
//        usm_Menu.setMenuName(usmMultiMiscDto.getMenu_name());
//        usm_Menu.setCanBeLogInPage("0");
//        usm_Menu.setMenuAcceptReport("0");
//        usm_Menu.setMenuManaged("0");
//        usm_Menu.setMenuOrder(0);
//        usm_Menu.setMenuPCode(usmMultiMiscDto.getMenuCode());
//        usm_Menu.setMenuType(6);
//        usm_Menu.setMenuVar("test");
//        usm_Menu.setMenuVersion("001");
//        usmMenuRepository.save(usm_Menu);
        return CustomResponse.builder().code("1").status("Success").description("Project added successfully").build();

    

          }  

	@Override
	public CustomResponse DeleteUSMApplication(long id,String menupaths, String menu) {
		
	     
		 if(menupaths.equals("userMgmt"))
	       {
			// Remove the related app from usm_user_multi_misc_info entity.
			 IUSMUserMultiRepository.deleteAppByUserId(id, menu);
			
			 return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
	       
	       } else  if(menupaths.equals("role")) {
	    	// Remove the related app from usm_role_multi_misc_info entity.
	    	   IUSMRoleMultiRepository.deleteAppByRoleId(id, menu);
	    	   return CustomResponse.builder().code("0").status("success").description("DELETED SUCCESSFULLY").build();
	       }
		 
	    
	        return CustomResponse.builder().code("1").status("fail").description("ERROR!").build();
	}

    @Override
	public List<ObjectNode> comboAppType(){
		System.out.println("get combe type--------------------------->");
		return ObjectToJsonRepository.getJson(entityManagerR,"select appCode as id, appName as name from USMSuiteApplication t");
	}
	
	@Override
	public List<ObjectNode> getAccessRightsGrid(String menuPCode){
		System.out.println("IT WORKED!!!!!!!!!!!!!!");
		return ObjectToJsonRepository.getJson(entityManagerR, "select menuCode as id, menuName as name from USMMenu where menuPCode like '"+menuPCode+"%'");
	}
} 