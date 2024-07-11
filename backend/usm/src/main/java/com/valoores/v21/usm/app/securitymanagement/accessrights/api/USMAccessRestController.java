package com.valoores.v21.usm.app.securitymanagement.accessrights.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.securitymanagement.accessrights.service.IUSMAccessService;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMMultiMiscDto;
import com.valoores.v21.usm.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM AccessRights", description = "USM AccessRights' exposed APIs")
@RestController
@RequestMapping("/api")
public class USMAccessRestController {

    @Autowired
    private IUSMAccessService usmAccessRightsService;

    /*@Operation(summary = "Get all Access Rights")
    @GetMapping(path = "/getAllUSMAccess/{menuPaths}/{id}/{appName}/{appMenu}/{subLevel}/{inColumn}", produces = "application/json")
    public List<USMAccessDto> getAllUSMAccess( @PathVariable String menuPaths,
                                               @PathVariable long id,
                                               @PathVariable long appName,
                                               @PathVariable String appMenu,
                                               @PathVariable long subLevel,
                                               @PathVariable long inColumn )
    {
        System.out.println("menuPaths ===== >>>" + menuPaths );
        return usmAccessRightsService.getAllUSMAccess(id, menuPaths, appName, appMenu, subLevel, inColumn);
    }
    */

//    @Operation(summary = "Get all Access Rights")
//    @GetMapping(path = "/getAllUSMAccess/{menuPaths}/{id}/{userRoleId}/{appName}/{appMenu}/{subLevel}/{inColumn}", produces = "application/json")
//    public List<ObjectNode> getAllUSMAccess(   @PathVariable String menuPaths,
//                                               @PathVariable long id,
//                                               @PathVariable String userRoleId,
//                                               @PathVariable long appName,
//                                               @PathVariable String appMenu,
//                                               @PathVariable String subLevel,
//                                               @PathVariable String inColumn )
//    {
//        return usmAccessRightsService.getAllUSMAccess(id,userRoleId, menuPaths, appName, appMenu, subLevel, inColumn);
//    }

    @PostMapping("/AddUSMApplication")
    public CustomResponse AddUSMApplication(@RequestBody USMMultiMiscDto usmMultiMiscDto)
    {
     System.out.println("in AddUSMApplication Function ");
     return usmAccessRightsService.AddUSMApplication(usmMultiMiscDto);
    }

    @Operation(summary = "Get all Access Rights")
    @PostMapping(path = "/getUSMAccessRightsApi/{id}/{userRoleId}/{menupaths}/{appName}/{appMenu}/{subLevel}/{inColumn}")
    public List<ObjectNode> getAllUSMAccess(@PathVariable long id,@PathVariable String userRoleId,
            @PathVariable String menupaths,
            @PathVariable long appName,
            @PathVariable String appMenu,
            @PathVariable String subLevel,
            @PathVariable String inColumn) {
        
        return usmAccessRightsService.getAllUSMAccess(id,userRoleId, menupaths,  appName,  appMenu, subLevel, inColumn);
    }
    //added by Mira to delete application
    @Operation(summary = "Delete existing application from usm_user_multi_misc_info or usm_role_multi_misc_info")
	@DeleteMapping("/DeleteUSMApplication/{id}/{menupaths}/{menu}")
	public CustomResponse DeleteUSMApplication(@PathVariable long id, @PathVariable String menupaths,@PathVariable String menu ) {
		return usmAccessRightsService.DeleteUSMApplication(id,menupaths,menu);
	}

     @GetMapping(path = "/comboAppType/")
    public List<ObjectNode> comboAppType(){
    	return usmAccessRightsService.comboAppType();
    }
    
    @PostMapping("/getAccessRightsGrid/{menuPCode}")
    public List<ObjectNode> getAccessRightsGrid(@PathVariable String menuPCode){
    	return usmAccessRightsService.getAccessRightsGrid(menuPCode);
    }
} 