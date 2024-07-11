package com.valoores.v21.usm.app.securitymanagement.accessrightsgrid.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.menu.repository.IUSMMenuRepository;
import com.valoores.v21.usm.app.securitymanagement.accessrightsgrid.service.IUSMAccessgridService;
import com.valoores.v21.usm.app.securitymanagement.role.repository.IUSMRoleMultiRepository;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.IUSMUserMultiRepository;

@Service
public class USMAccessgridServiceImpl implements IUSMAccessgridService{

    @Resource
    private IUSMUserMultiRepository usmUserMultiRepository;

    @Resource
    private IUSMMenuRepository usmMenuRepository;

    @Resource
    private IUSMRoleMultiRepository usmRoleMultiRepository;

    // @Autowired
    // private EntityManager entityManagerR;

    // @Autowired
	// private PlatformTransactionManager transactionManager;
    
    // @Autowired
	//  private IUSMAccessgridRepository usmAccessRepository;
    
    // @Autowired
	// private javax.sql.DataSource dataSource;

//	@Override
//	public CustomResponse updateAccessRightsApi(String id,String userRoleId, String menuPaths, String appName, String appMenu, String subLevel,
//			String inColumn, ArrayList<USMAccessDto> usmAccessDtoLstObj) {
//		 ObjectMapper objectMapper = new ObjectMapper();
//		    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		    org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
//		    String sqlCond = "";
//		    if (subLevel.equals("false"))
//		       {
//		        sqlCond =  "  AND MMI.MENU_CODE = '"+appMenu+"'" ;
//		       }
//		       else
//		       {
//		           sqlCond =  "  AND MMI.MENU_CODE like '"+appMenu+"%'" ;
//		       }
//
//		    
//		    try {
//		    	DataSourceUtils.getConnection(dataSource);
//		    	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		    	 System.out.println("usmAccessDtoLstObj >>>>>>>>>>>>>>"+usmAccessDtoLstObj.toString());
//		    	        // Convert the ArrayList<USMAccessDto> to a JSON string
//		    	        String jsonString = objectMapper.writeValueAsString(usmAccessDtoLstObj);
//		    	        System.out.println("jsonString >>>>>>>>>>>>>>"+jsonString);
//		    	        //get access to insert
//		    	        //usmAccessRepository.grantAccessToTable();
//		    	        // Insert the JSON string into the table using the repository
//		    	        usmAccessRepository.insertTechColumn(jsonString);
//
//
//		        // Call the stored procedure
//		        System.out.println("id >>>>>>>>>>>>>>"+id);
//		        if(menuPaths.equals("userMgmt"))
//		        {
//		        	 System.out.println("userMgmt >>>>>>>>>>>>>>");
//		        	 System.out.println("sqlCond >>>>>>>>>>>>>>"+sqlCond);
//				String sql = String.valueOf(entityManagerR.createNativeQuery("CALL ssdx_eng.usm_update_accessrights_userMgmt(:id,:sqlCond)")
//					.setParameter("id", id) .setParameter("sqlCond", sqlCond).executeUpdate());	
//System.out.println("ewwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww11 :" +sql);
//System.out.println("ewwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww22 :" +status.toString());
//		        transactionManager.commit(status);
//		        }
//		        else {
//		        	 System.out.println("role >>>>>>>>>>>>>>");
//		        	entityManagerR.createNativeQuery("CALL ssdx_eng.usm_update_accessrights_userMgmt_role(:roleid,:sqlCond)")
//					.setParameter("roleid", id) .setParameter("sqlCond", sqlCond).executeUpdate();	
//
//		        transactionManager.commit(status);
//		       }
//		        return CustomResponse.builder().code("1").status("Success").description("Project added successfully").build();
//		        
//		    } catch (Exception e) {
//		        Connection con = DataSourceUtils.getConnection(dataSource);
//		        DataSourceUtils.releaseConnection(con, dataSource);
//
//		        transactionManager.rollback(status);
//		        String errorMessage = e.getMessage();
//		        return CustomResponse.builder().code("2").status("fail").description(errorMessage).build();
//		    }
//	}
}

