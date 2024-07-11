package com.valoores.v21.usm.app.securitymanagement.usermanagement.api;

import java.awt.FontFormatException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMUser;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMUserDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMCurrency;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMLanguage;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMRefSysParamLines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMStatus;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMSyslines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserRoles;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.service.IUSMUserService;
import com.valoores.v21.usm.backend.CustomResponse;
import com.valoores.v21.usm.backend.LoginResponse;
import com.valoores.v21.usm.backend.UserCredentials;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class USMUserRestController {
    @Autowired
    private IUSMUserService usmUserService;

    @Operation(summary = "Get all USM users")
    @PostMapping(path = "/usmusers", produces = "application/json")
    public List<ObjectNode> getAllUSMUsers() {

        return usmUserService.getAllUSMUsers();

    }

    @Operation(summary = "Get USM user provided EMP_ID")
    @GetMapping(path = "/usmuser/{id}", produces = "application/json")
    public ResponseEntity<USMUserDto> findUSMUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(usmUserService.getUSMUserById(id));
    }

    @Operation(summary = "Get USM users whose usernames contain a string")
    @GetMapping(path = "/usmuser/usernameContains/{username}", produces = "application/json")
    public List<USMUser> findByUSMUsernameContaining(@PathVariable("username") String username){
        return usmUserService.getUSMUsersUsernameContaining(username);
    }

    @Operation(summary = "Add new USM user")
    @PostMapping("/usmuser/add")
    public CustomResponse addUSMUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("firstName") String firstName,
                                     @RequestParam("lastName") String lastName,
                                     @RequestParam("userId") String userId,
                                     @RequestParam("email") String email,
                                     @RequestParam("language") String lanId,
                                     @RequestParam("ispwd") String ispwd,
                                     @RequestParam("gender") String gender,
                                     @RequestParam("civilStatus") String civilStatus,
                                     @RequestParam("dateOfBirth") String dateOfBirth,
//  @RequestParam("numberSetting") String numberSetting,
                                     @RequestParam("currency") String currency,
                                     @RequestParam("firstAddress") String firstAddress,
                                     @RequestParam("secondAddress") String secondAddress,
                                     @RequestParam("phone") String phone,
                                     @RequestParam("mobile") String mobile,
                                     @RequestParam("postalCode") String postalCode,
                                     @RequestParam("media") MultipartFile media,
                                     @RequestParam("bugType") String bugType,
                                     @RequestParam("bugName") Set<String> bugName,
                                     @RequestParam("defaultRole") String defaultRole,
                                     @RequestParam("role") Set<String> role,
                                     @RequestParam("status") String status,
                                     @RequestParam("pwdExpPrdNbr") String pwdExpPrdNbr,
                                     @RequestParam("pwdExpPrd") String pwdExpPrd,
                                     @RequestParam("pwdExpDate") String pwdExpDate,
                                     @RequestParam("changePassword") String changePassword) throws ParseException {


        USMUserDto usmUserDto = new USMUserDto();
        usmUserDto.setUsername(username);
        usmUserDto.setPassword(password);
        usmUserDto.setFirstName(firstName);
        usmUserDto.setLastName(lastName);
        usmUserDto.setUserId(Long.parseLong(userId));

        if(email.equals("") || email.equals(null) || email.equals("null")) {
            usmUserDto.setEmail("");
        }
        else
        {
            usmUserDto.setEmail(email);
        }

        usmUserDto.setIsPwdLdapAuth(ispwd);
        usmUserDto.setGender(gender);

        if(pwdExpPrd.equals("") || pwdExpPrd.equals("null")) {
            usmUserDto.setPwdExpPrd(null);
        }else
        {
            usmUserDto.setPwdExpPrd(pwdExpPrd);

        }
        if(pwdExpDate.equals("") || pwdExpDate.equals("null")) {
            usmUserDto.setPwdExpDate(null);
        }else
        {
            usmUserDto.setPwdExpDate(pwdExpDate);

        }
        if(pwdExpPrdNbr.equals("") || pwdExpPrdNbr.equals("null")) {
            usmUserDto.setPwdExpPrdNbr(null);
        }else
        {
        	usmUserDto.setPwdExpPrdNbr(Integer.parseInt(pwdExpPrdNbr));

        }
            usmUserDto.setChangePassword(changePassword);

            usmUserDto.setDateOfBirth(dateOfBirth);
// usmUserDto.setNumberSetting(Integer.parseInt(numberSetting));
        if(lanId.equals("") || lanId.equals("null")) {
            usmUserDto.setLanId(null);
        } else {
            usmUserDto.setLanId(Integer.parseInt(lanId));
        }
        if(civilStatus.equals("") || civilStatus.equals("null")) {
            usmUserDto.setCivilStatus(null);
        } else {
usmUserDto.setCivilStatus(Integer.parseInt(civilStatus));
        }
        if(currency.equals("") || currency.equals("null")) {
            usmUserDto.setCurrency(null);
        } else {
            usmUserDto.setCurrency(Integer.parseInt(currency));
        }
        if(status.equals("") || status.equals("null")) {
            usmUserDto.setStatus(null);
        } else {
            usmUserDto.setStatus(Integer.parseInt(status));
        }
        if(bugType.equals("") || bugType.equals("null")) {
            usmUserDto.setBugType(null);
        } else {

             usmUserDto.setBugType(Integer.parseInt(bugType));
        }
		if (defaultRole.equals("") || defaultRole.equals("null")) {
            usmUserDto.setDefaultRole(null);
        } else {

             usmUserDto.setDefaultRole(Integer.parseInt(defaultRole));
        }

        if(firstAddress.equals("") || firstAddress.equals(null)) {
            usmUserDto.setFirstAddress("");
        } else {

            usmUserDto.setFirstAddress(firstAddress);
        }

        if(secondAddress.equals("") || secondAddress.equals(null)) {
            usmUserDto.setSecondAddress("");
        } else {

            usmUserDto.setSecondAddress(secondAddress);
        }

        if(phone.equals("") || phone.equals(null)) {
            usmUserDto.setPhone("");
        } else {

            usmUserDto.setPhone(phone);
        }

        if(mobile.equals("") || mobile.equals(null)) {
            usmUserDto.setMobile("");
        } else {

            usmUserDto.setMobile(mobile);
        }

        if(postalCode.equals("") || postalCode.equals(null)) {
            usmUserDto.setPostalCode("");
        } else {

            usmUserDto.setPostalCode(postalCode);
        }

         usmUserDto.setMedia(media);
//         usmUserDto.setBugType(Integer.parseInt(bugType));
         usmUserDto.setUsmMdmBsnUnitGroup(bugName);
         usmUserDto.setUsmRoles(role);

        CustomResponse resp = usmUserService.addUSMUser(usmUserDto);
        return resp;
    }

    @Operation(summary = "Update an existing USM user provided an EMP_ID")
    @PutMapping("/usmuser/update/{id}")
    public CustomResponse updateUSMUser(@PathVariable("id") Integer id,
                                         @RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         @RequestParam("firstName") String firstName,
                                         @RequestParam("lastName") String lastName,
                                         @RequestParam("email") String email,
                                         @RequestParam("language") String lanId,
                                         @RequestParam("ispwd") String ispwd,
                                         @RequestParam("gender") String gender,
                                         @RequestParam("civilStatus") String civilStatus,
                                         @RequestParam("dateOfBirth") String dateOfBirth,
                                         //  @RequestParam("numberSetting") String numberSetting,
                                         @RequestParam("currency") String currency,
                                         @RequestParam("firstAddress") String firstAddress,
                                         @RequestParam("secondAddress") String secondAddress,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("mobile") String mobile,
                                         @RequestParam("postalCode") String postalCode,
                                         @RequestParam("media") MultipartFile media,
                                         @RequestParam("bugType") String bugType,
                                         @RequestParam("bugName") Set<String> bugName,
                                         @RequestParam("defaultRole") String defaultRole,
                                         @RequestParam("role") Set<String> role,
                                         @RequestParam("status") String status,
                                        // @RequestParam("application") String application,
                                         @RequestParam("pwdExpPrdNbr") String pwdExpPrdNbr,
                                         @RequestParam("pwdExpPrd") String pwdExpPrd,
                                         @RequestParam("pwdExpDate") String pwdExpDate,
                                         @RequestParam("changePassword") String changePassword,
                                         @RequestParam("userId") String userId) throws ParseException {


        USMUserDto usmUserDto = new USMUserDto();
        usmUserDto.setUserId(Long.parseLong(userId));
        usmUserDto.setId(id);
        usmUserDto.setUsername(username);
        usmUserDto.setPassword(password);
        usmUserDto.setFirstName(firstName);
        usmUserDto.setLastName(lastName);

        if(email.equals("") || email.equals(null) || email.equals("null")) {
            usmUserDto.setEmail("");
        }
        else
        {
            usmUserDto.setEmail(email);
        }

        usmUserDto.setIsPwdLdapAuth(ispwd);
        usmUserDto.setGender(gender);
        usmUserDto.setDateOfBirth(dateOfBirth);
// usmUserDto.setNumberSetting(Integer.parseInt(numberSetting));
        if(firstAddress.equals("") || firstAddress.equals(null)) {
            usmUserDto.setFirstAddress("");
        } else {

            usmUserDto.setFirstAddress(firstAddress);
        }

        if(secondAddress.equals("") || secondAddress.equals(null)) {
            usmUserDto.setSecondAddress("");
        } else {

            usmUserDto.setSecondAddress(secondAddress);
        }

        if(phone.equals("") || phone.equals(null)) {
            usmUserDto.setPhone("");
        } else {

            usmUserDto.setPhone(phone);
        }

        if(mobile.equals("") || mobile.equals(null)) {
            usmUserDto.setMobile("");
        } else {

            usmUserDto.setMobile(mobile);
        }

        if(postalCode.equals("") || postalCode.equals(null)) {
            usmUserDto.setPostalCode("");
        } else {

            usmUserDto.setPostalCode(postalCode);
        }
        usmUserDto.setMedia(media);
        usmUserDto.setChangePassword(changePassword);

        if(pwdExpPrd.equals("") || pwdExpPrd.equals("null")) {
            usmUserDto.setPwdExpPrd(null);
        }else
        {
            usmUserDto.setPwdExpPrd(pwdExpPrd);
        }
        if(pwdExpDate.equals("") || pwdExpDate.equals("null")) {
            usmUserDto.setPwdExpDate(null);
        }else
        {
            usmUserDto.setPwdExpDate(pwdExpDate);
        }
        if(pwdExpPrdNbr.equals("") || pwdExpPrdNbr.equals("null")) {
            usmUserDto.setPwdExpPrdNbr(null);
        }else
        {
usmUserDto.setPwdExpPrdNbr(Integer.parseInt(pwdExpPrdNbr));
        }

        if(lanId.equals("") || lanId.equals("null")) {
            usmUserDto.setLanId(null);
        } else {
            usmUserDto.setLanId(Integer.parseInt(lanId));
        }
        if(civilStatus.equals("") || civilStatus.equals("null")) {
            usmUserDto.setCivilStatus(null);
        } else {
usmUserDto.setCivilStatus(Integer.parseInt(civilStatus));
        }
        if(currency.equals("") || currency.equals("null")) {
            usmUserDto.setCurrency(null);
        } else {
            usmUserDto.setCurrency(Integer.parseInt(currency));
        }
        if(status.equals("") || status.equals("null")) {
            usmUserDto.setStatus(null);
        } else {
            usmUserDto.setStatus(Integer.parseInt(status));
        }
//        if(application.equals("") || application.equals("null")) {
//            usmUserDto.setApplication(null);;
//        } else {
//            usmUserDto.setApplication(application);
//        }
        if(bugType.equals("") || bugType.equals("null")) {
            usmUserDto.setBugType(null);
        } else {

             usmUserDto.setBugType(Integer.parseInt(bugType));
        }
        if(defaultRole.equals("") || defaultRole.equals("null")) {
            usmUserDto.setDefaultRole(null);
        } else {

          usmUserDto.setDefaultRole(Integer.parseInt(defaultRole));
         }
        usmUserDto.setUsmMdmBsnUnitGroup(bugName);
         usmUserDto.setUsmRoles(role);


        CustomResponse resp = usmUserService.updateUSMUser(usmUserDto);
        return resp;

    }

    @Operation(summary = "Delete an existing USM user provided an EMP_ID")
    @DeleteMapping("/usmuser/delete/{id}")
    public CustomResponse deleteUSMUser(@PathVariable("id") Integer id) {
        CustomResponse resp = usmUserService.deleteUSMUser(id);
        return resp;
    }

    @Operation(summary = "USM user login endpoint")
    @PostMapping("/login")
    public LoginResponse loginUSM(@RequestBody UserCredentials creds) {
        return usmUserService.userLogin(creds);
    }

    @GetMapping("/getStatusCombo")
    public List<USMStatus> getStatusCombo() {
        return usmUserService.getStatusCombo();
    }

    @GetMapping("/getLanguageCombo")
    public List<USMLanguage> getLanguageCombo() {
        return usmUserService.getLanguageCombo();
    }

    @GetMapping("/getBugTypeCombo")
    public List<USMSyslines> getBugTypeCombo() {
        return usmUserService.getBugTypeCombo();
    }

    @GetMapping("/getBugNameCombo/{id}/{userId}")
    public List<ObjectNode> getBugNameCombo(@PathVariable("id") String id ,@PathVariable("userId") String userId) {
        return usmUserService.getBugNameCombo(id,Integer.parseInt(userId));
    }

    @GetMapping("/getCivilStatusCombo")
    public List<USMRefSysParamLines> getCivilStatusCombo() {
        return usmUserService.getCivilStatusCombo();
    }

    @GetMapping("/getCurrencyCombo")
    public List<USMCurrency> getCurrencyCombo() {
        return usmUserService.getCurrencyCombo();
    }

    @GetMapping("/getRoleCombo")
    public List<USMUserRoles> getRoleCombo() {
        return usmUserService.getRoleCombo();
    }

//    @GetMapping("/getDefaultRoleCombo")
//    public List<USMUserRoles> getDefaultRoleCombo(@RequestBody USMComboDto usmComboDto) {
//        return usmUserService.getDefaultRoleCombo(usmComboDto);
//    }

    @GetMapping("/getPwdExpPeriodCombo")
    public String getPwdExpPeriodCombo() {

        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject json1 = new JSONObject();

        json.put("id",3);
        json.put("name","Week");
        array.put(json);
        json1.put("id",4);
        json1.put("name","Month");
        array.put(json1);


        return array.toString();
    }
    
    @PostMapping("/fetchCaptchaCode/{captchaSerial}")
    public String fetchCaptchaCode(@PathVariable("captchaSerial") String captchaSerial) throws IOException, FontFormatException {

    	  String captchaStr = "";
          Captcha cpt;
         
//              cpt = new ArithmeticCaptcha(130, 48, 4);
//              // log.error("\n\n\n\n===="+cpt.getArithmeticString());//challenge
//              // log.error("\n\n\n\n===="+cpt.text());//response
//         
//              cpt = new SpecCaptcha(130, 48, 4);
//              imgStr = cpt.toBase64();
         
              cpt = new SpecCaptcha(130, 48, 4);
              cpt.setCharType(Captcha.TYPE_ONLY_NUMBER);
              cpt.setFont(Captcha.FONT_9);
              captchaStr = cpt.toBase64();
              System.out.println("captchaBase64>>>>"+captchaStr);

              captchaStr = cpt.toBase64();
              System.out.println("captcha>>>>"+cpt.text());
              usmUserService.insertCaptchaSession(captchaSerial,cpt.text());
          return captchaStr;
    }
    
    
    
    @PostMapping("/checkCaptcha/{captchaField}/{captchaSerial}")
    public int checkCaptcha(@PathVariable("captchaField") String captchaField,@PathVariable("captchaSerial") String captchaSerial) throws IOException, FontFormatException {

          return usmUserService.checkCaptcha(captchaField,captchaSerial);

    }
      
    @PostMapping("/getInDisplayLogs")
    public List<ObjectNode> getInDisplayLogs(){
    	    
    	return usmUserService.getInDisplayLogs();
    	
    }
    @PostMapping("/getInDisplayLogsDetails/{logId}")
    public String getInDisplayLogsDetails(@PathVariable("logId") long logId){
    	    
    	return usmUserService.getInDisplayLogsDetails(logId);
    	
    }
    
    
//    @PostMapping("/insertInDisplayLogsData")
//    public void insertInDisplayLogsData(@RequestBody UserLogsDto userLogsDto){
//    	    
//    	usmUserService.insertInDisplayLogsData(userLogsDto);
//    	
//    }
//    
}
