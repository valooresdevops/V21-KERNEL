package com.valoores.v21.usm.app.securitymanagement.usermanagement.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMUser;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRoleMulti;
import com.valoores.v21.usm.app.securitymanagement.role.repository.IUSMRoleRepository;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMUserDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.SessionModel;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMCurrency;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMLanguage;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMRefSysParamLines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMStatus;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMSyslines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserMulti;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserRoles;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.IUSMUserMultiRepository;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.IUSMUserRepository;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.InDisplayLogsRepo;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.repository.SessionRepo;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.service.IUSMUserService;
import com.valoores.v21.usm.backend.CustomResponse;
import com.valoores.v21.usm.backend.LoginResponse;
import com.valoores.v21.usm.backend.UserCredentials;
import com.valoores.v21.usm.client.IKeycloakUserClient;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.utils.StringUtils;

@Service
public class USMUserServiceImpl implements IUSMUserService {

	@Resource
	private IUSMUserRepository usmUserRepository;
	@Resource
	private IUSMUserMultiRepository usmUserMultiRepository;
	@Resource
	private IUSMRoleRepository usmRoleRepository;
	@Resource
	private IKeycloakUserClient keycloakClient;
	@Resource
	private SessionRepo sessionRepo;
//    @Resource
//    private EventDispatcher eventDispatcher;

	@Autowired
	private EntityManager entityManagerR;
	boolean exists = false;

	@Resource
	private InDisplayLogsRepo inDisplayLogsRepo;

	@Override
	public List<ObjectNode> getAllUSMUsers() {
		
//		"select usmuser0_.USER_ID as id,\n" + 
//			    "       usmuser0_.USER_LOGIN as username,\n" + 
//			    "       usmuser0_.USER_FULL_NAME as \"fullName\",\n" + 
//			    "       usmuser0_.IS_PWD_LDAP_AUTHENTIFIED as \"isPwdLdapAuth\",\n" + 
//			    "       (select usmstatus1_.STATUS_NAME\n" + 
//			    "          from SDEDBA.sts_status usmstatus1_\n" + 
//			    "         where usmstatus1_.STATUS_ID = usmuser0_.STATUS_CODE) as status,\n" + 
//			    "       (select usmuserrol2_.ROLE_NAME\n" + 
//			    "          from USMDBA.USM_ROLE_MISC_INFO usmuserrol2_\n" + 
//			    "         where usmuserrol2_.ROLE_ID =\n" + 
//			    "               (select * from (select usmusermul3_.ROLE_ID\n" + 
//			    "                  from USMDBA.usm_user_multi_misc_info usmusermul3_\n" + 
//			    "                 where usmusermul3_.IS_DEFAULT = 1\n" + 
//			    "                   and usmusermul3_.USER_ID = usmuser0_.USER_ID\n" + 
//			    "                   order by usmusermul3_.creation_date desc)\n" + 
//			    "                 )\n" + 
//			    "           ) as \"defaultRole\"\n" + 
//			    "  from USMDBA.USM_USER_MISC_INFO usmuser0_"
//		
		List<ObjectNode> MyList1 = ObjectToJsonRepository.getJsonNativeQuery(entityManagerR, 
			    "select b.USER_ID as \"ID\",     " + 
			    "       b.USER_LOGIN as \"USERNAME\",     " + 
			    "       b.USER_FULL_NAME as \"FULLNAME\",     " + 
			    "       b.IS_PWD_LDAP_AUTHENTIFIED as \"ISPWDLDAPAUTH\",     " + 
			    "       (select a.STATUS_NAME     " + 
			    "          from SDEDBA.sts_status a     " + 
			    "         where a.STATUS_ID = b.STATUS_CODE " + 
			    "         LIMIT 1) as status,     " + 
			    "       (select d.ROLE_NAME     " + 
			    "          from USMDBA.USM_ROLE_MISC_INFO d     " + 
			    "         where d.ROLE_ID =     " + 
			    "               (select c.ROLE_ID     " + 
			    "                  from USMDBA.usm_user_multi_misc_info c     " + 
			    "                 where c.IS_DEFAULT = '1'  " + 
			    "                   and c.USER_ID = b.USER_ID     " + 
			    "                 order by c.creation_date desc " + 
			    "                 LIMIT 1) " + 
			    "         LIMIT 1) as \"DEFAULTROLE\"     " + 
			    "  from USMDBA.USM_USER_MISC_INFO b" );
		System.out.println("MyList1 >>>>>>>>>> "+MyList1);
		return MyList1;
	} 
 
	@Transactional
	@Override
	public CustomResponse addUSMUser(USMUserDto usmUserDto) {

		CustomResponse resp = CustomResponse.builder().build();
		USMUser usmUser = new USMUser();

		String providedUsername = usmUserDto.getUsername();

		if (usmUserRepository.findByUsername(providedUsername) != null) {

			// USERNAME ALREADY EXISTS
			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("USM User ['" + providedUsername + "'] already exists!");

		} else {
			System.out.println(">>>"+usmUserDto.getId());
			mapDtoToUserEntity(usmUserDto, usmUser, "create");
			//usmUserDto.setCreatedBy(usmUserDto.getCreatedBy().toString());
			usmUserRepository.save(usmUser);

			mapDtoToUserMultiEntity(usmUserDto, usmUser, "create");
            System.out.println("111111111111111111111111111"+usmUser.getId());
			resp.setId(usmUser.getId());
			
			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("USM User ['" + providedUsername + "'] created successfully!");
			resp.setUserId(String.valueOf(usmUser.getId()));

		}
		return resp;
	}

	@Transactional
	@Override
	public CustomResponse updateUSMUser(USMUserDto usmUserDto) {

		USMUser usmUser = usmUserRepository.findById(usmUserDto.getId());
		CustomResponse resp = CustomResponse.builder().build();

		// USM USER NOT FOUND GIVEN THE ID PROVIDED
		if (usmUser == null) {

			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("USM User with id = " + usmUserDto.getId() + " not found!");

		} else {

			mapDtoToUserEntity(usmUserDto, usmUser, "update");
			usmUserRepository.save(usmUser);

			mapDtoToUserMultiEntity(usmUserDto, usmUser, "update");
            System.out.println("2222222222222222222222222222");

			resp.setId(usmUserDto.getId());
			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("USM User with id = " + usmUserDto.getId() + " updated successfully!");
		}
		return resp;
	}

	@Transactional
	public CustomResponse deleteUSMUser(long id) {

		USMUser usmUser = usmUserRepository.findById(id);
		CustomResponse resp = CustomResponse.builder().build();

		if (usmUser != null) {

			usmUserMultiRepository.deleteAllByUserId((long) id);
			usmUserRepository.deleteById(usmUser.getId());

			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("USM User with id = " + id + " deleted successfully!");

		} else {
			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("USM User with id = " + id + " not found!");
		}
		return resp;
	}

	@Override
	public LoginResponse userLogin(UserCredentials credentials) {
		LoginResponse resp = LoginResponse.builder().build();

		String providedUsername = credentials.getUsername();
		String providedPassword = StringUtils.sha256(credentials.getPassword());

		USMUser usmUser = usmUserRepository.findByUsername(providedUsername);
		String usmUserRoles = usmUserMultiRepository.findRoleName(usmUser.getId());
		String roleId = usmUserMultiRepository.findRoleId(usmUser.getId());

		// USERNAME FOUND!
		String registeredPassword = usmUser.getPassword();
		//String roleName = usmUserRoles;

		if (providedPassword.equalsIgnoreCase(registeredPassword)) {
			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("Login successful!");
			resp.setUserId(String.valueOf(usmUser.getId()));
			resp.setRoleName(usmUserRoles);
			resp.setRoleId(roleId);
		} else {
			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("Incorrect password!");
		}
		return resp;
	}

	@Override
	public List<USMUser> getUSMUsersUsernameContaining(String username) {

		return usmUserRepository.findByUsernameContaining(username);
	}

	@Override
	public USMUserDto getUSMUserById(long id) {
		

		USMUser usmUser = usmUserRepository.findById(id);
		List<USMUserMulti> usmUserMulti = usmUserMultiRepository.getAllUsersWithoutAccessRights(usmUser.getId());
		System.out.println("333333333333333333");
		return mapEntityToDtoOnUpdateForm(usmUser, usmUserMulti);
	}

	@Override
	public Object getIdAndCreationDate(long id) {
		return usmUserRepository.getIdAndCreationDate(id);
	}

	private void mapDtoToUserEntity(USMUserDto usmUserDto, USMUser usmUser, String flag) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY");

			usmUser.setUsername(usmUserDto.getUsername());

			// CHECK IF PASSWORD WAS CHANGED IN THE UPDATE THEN USE SHA256 HASHING ELSE SEND
			// IT AS PLAIN BECAUSE IT'S ALREADY HASHED
			if (flag.equals("update")) {
				if (usmUser.getPassword().equals(usmUserDto.getPassword())) {
					usmUser.setPassword(usmUserDto.getPassword());
				} else {
					usmUser.setPassword(StringUtils.sha256(usmUserDto.getPassword()));
				}

			} else {
				usmUser.setPassword(StringUtils.sha256(usmUserDto.getPassword()));
			}

			usmUser.setLanId(usmUserDto.getLanId());

			if (usmUserDto.getIsPwdLdapAuth().equals("false")) {
				usmUser.setIsPwdLdapAuth("0");
			} else {
				usmUser.setIsPwdLdapAuth("1");
			}
			if (usmUserDto.getChangePassword().equals("false")) {
				usmUser.setChangePassword("0");
			} else {

				usmUser.setChangePassword("1");

			}
			if (usmUserDto.getPwdExpDate() != null) {
				Date pwdExpDate = formatter.parse(usmUserDto.getPwdExpDate());
				usmUser.setPwdExpDate(pwdExpDate);
			} else {
				usmUser.setPwdExpDate(null);
			}

			if (usmUserDto.getPwdExpPrd() != null) {
				usmUser.setPwdExpPrd(Integer.parseInt(usmUserDto.getPwdExpPrd()));
			} else {
				usmUser.setPwdExpPrd(null);
			}
			usmUser.setPwdExpPrdNbr(usmUserDto.getPwdExpPrdNbr());
			Date sysdate = new Date();
			usmUser.setPwdUpdateDate(sysdate);

			usmUser.setFirstName(usmUserDto.getFirstName());
			usmUser.setLastName(usmUserDto.getLastName());
			usmUser.setFullName(usmUserDto.getFirstName() + " " + usmUserDto.getLastName());
			usmUser.setEmail(usmUserDto.getEmail());
			usmUser.setGender(usmUserDto.getGender());
			usmUser.setCivilStatus(usmUserDto.getCivilStatus());

			if (!usmUserDto.getDateOfBirth().contentEquals("") && !usmUserDto.getDateOfBirth().equals(null)
					&& !usmUserDto.getDateOfBirth().equals("null")) {
				Date date = formatter.parse(usmUserDto.getDateOfBirth());
				usmUser.setDateOfBirth(date);
			} else {
				usmUser.setDateOfBirth(null);
			}

			usmUser.setCurrency(usmUserDto.getCurrency());
			usmUser.setStatus(usmUserDto.getStatus());
			usmUser.setFirstAddress(usmUserDto.getFirstAddress());
			usmUser.setSecondAddress(usmUserDto.getSecondAddress());
			usmUser.setPhone(usmUserDto.getPhone());
			usmUser.setMobile(usmUserDto.getMobile());
			usmUser.setPostalCode(usmUserDto.getPostalCode());

			// CHECK IF UPLOADED IMAGE IS DUMMY TEXT FILE OR NO
			if (!usmUserDto.getMedia().getOriginalFilename().equals("DummyTxtFile.txt")) {
				usmUser.setMedia(usmUserDto.getMedia().getBytes());
			}

			System.out.println("flag >>>" + flag);

			Date now = new Date();
			if (flag.equals("create")) {
				usmUser.setCreationDate(now);
				usmUser.setCreatedBy(usmUserDto.getUserId());

			}

			if (flag.equals("update")) {
				usmUser.setUpdateDate(now);
				usmUser.setUpdatedBy(usmUserDto.getUserId());

			}

//

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mapDtoToUserMultiEntity(USMUserDto usmUserDto, USMUser usmUser, String flag) {

		try {

			usmUserMultiRepository.deleteByUserId(usmUser.getId());

			String[] list = convert(usmUserDto.getUsmRoles());
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < list.length; i++) {
				ids.add(Long.valueOf(list[i]));
			}

			List<USMRoleMulti> bugIds = usmUserRepository.getBugIds(ids);

			usmUserDto.getUsmRoles().stream().forEach(roleIds -> {
				exists = false;

				bugIds.stream().forEach(roleBugIds -> {

					USMUserMulti usmUserMulti = new USMUserMulti();

					if (roleBugIds.getBugId() != 0) {
						if (roleBugIds.getRoleId() == Long.parseLong(roleIds)) {

							usmUserMulti.setBsnGroupId(Integer.parseInt(String.valueOf(roleBugIds.getBugId())));
							System.out.println("(usmUserDto.getDefaultRole() "+usmUserDto.getDefaultRole() );
							if (usmUserDto.getDefaultRole() != null) {
								
								if (usmUserDto.getDefaultRole().equals(Integer.parseInt(roleIds))) {
									System.out.println("usmUserMulti.setIsDefault(1)");	
									usmUserMulti.setIsDefault("1");
								}
							} else {
								System.out.println("usmUserMulti.setIsDefault(0)");	
								usmUserMulti.setIsDefault("0");
							}
							usmUserMulti.setRoleId(Integer.parseInt(roleIds));
							usmUserMulti.setUserId(usmUser.getId());
							Date now = new Date();
							usmUserMulti.setCreationDate(now);
							usmUserMulti.setCreatedBy(usmUserDto.getUserId());
							usmUserMultiRepository.save(usmUserMulti);
							exists = true;
						}
					}
				});

				if (exists == false) {
					USMUserMulti usmUserMulti = new USMUserMulti();
					System.out.println("(usmUserDto.getDefaultRole() "+usmUserDto.getDefaultRole() );
					if (usmUserDto.getDefaultRole() != null) {
						System.out.println("usmUserMulti.setIsDefault(1)");	
						if (usmUserDto.getDefaultRole().equals(Integer.parseInt(roleIds))) {
							System.out.println("inside usmUserMulti.setIsDefault(1)");
							usmUserMulti.setIsDefault("1");
						}
					} else {
						System.out.println("usmUserMulti.setIsDefault(0)");	
						usmUserMulti.setIsDefault("0");
					}
					usmUserMulti.setRoleId(Integer.parseInt(roleIds));
					usmUserMulti.setUserId(usmUser.getId());
					Date now = new Date();
					usmUserMulti.setCreationDate(now);
					usmUserMulti.setCreatedBy(usmUserDto.getUserId());
					usmUserMultiRepository.save(usmUserMulti);
					exists = true;
				}

			});

			usmUserDto.getUsmMdmBsnUnitGroup().stream().forEach(bugId -> {

				exists = false;

				bugIds.stream().forEach(roleBugIds -> {

					if (roleBugIds.getBugId() != 0) {

						if (roleBugIds.getBugId() == Long.parseLong(bugId)) {

							exists = true;

						}
					}
				});

				if (exists == false) {
					USMUserMulti usmUserMulti = new USMUserMulti();

					usmUserMulti.setBsnGroupId(Integer.parseInt(bugId));
					usmUserMulti.setUserId(usmUser.getId());
					usmUserMulti.setBsnGroupTypeId(usmUserDto.getBugType());
					Date now = new Date();
					usmUserMulti.setCreationDate(now);
					usmUserMulti.setCreatedBy(usmUserDto.getUserId());
					usmUserMultiRepository.save(usmUserMulti);
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private USMUserDto mapEntityToDtoOnUpdateForm(USMUser usmUser, List<USMUserMulti> usmUserMulti) {
		USMUserDto responseDto = new USMUserDto();
		Set<String> usmMdmBsnUnitGroup = new HashSet<>();
		Set<String> usmRoles = new HashSet<>();

		responseDto.setId(usmUser.getId());
		responseDto.setUsername(usmUser.getUsername());
		responseDto.setPassword(usmUser.getPassword());
		responseDto.setFirstName(usmUser.getFirstName());
		responseDto.setLastName(usmUser.getLastName());
		responseDto.setFullName(usmUser.getFullName());
		responseDto.setEmail(usmUser.getEmail());
		responseDto.setGender(usmUser.getGender());
		responseDto.setCivilStatus(usmUser.getCivilStatus());
		responseDto.setLanId(usmUser.getLanId());
		responseDto.setDateOfBirth(String.valueOf(usmUser.getDateOfBirth()));
		responseDto.setNumberSetting(usmUser.getNumberSetting());
		responseDto.setCurrency(usmUser.getCurrency());
		if (usmUser.getFirstAddress() != null) {
			responseDto.setFirstAddress(usmUser.getFirstAddress());
		} else {
			responseDto.setFirstAddress("");

		}
		if (usmUser.getSecondAddress() != null) {
			responseDto.setSecondAddress(usmUser.getSecondAddress());
		} else {
			responseDto.setSecondAddress("");

		}
		if (usmUser.getPhone() != null) {
			responseDto.setPhone(usmUser.getPhone());
		} else {
			responseDto.setPhone("");

		}
		if (usmUser.getMobile() != null) {
			responseDto.setMobile(usmUser.getMobile());
		} else {
			responseDto.setMobile("");

		}
		if (usmUser.getPostalCode() != null) {
			responseDto.setPostalCode(usmUser.getPostalCode());
		} else {
			responseDto.setPostalCode("");

		}
		responseDto.setUserImage(usmUser.getMedia());

		usmUserMulti.stream().forEach(userMulti -> {

			if (userMulti.getBsnGroupId() != null && userMulti.getRoleId() == null)
				usmMdmBsnUnitGroup.add(String.valueOf(userMulti.getBsnGroupId()));

			if (userMulti.getRoleId() != null)
				usmRoles.add(String.valueOf(userMulti.getRoleId()));

			if (userMulti.getBsnGroupTypeId() != null && userMulti.getRoleId() == null)
				responseDto.setBugType(userMulti.getBsnGroupTypeId());

			if (userMulti.getIsDefault() != null)
				responseDto.setDefaultRole(userMulti.getRoleId());
		});

		responseDto.setUsmMdmBsnUnitGroup(usmMdmBsnUnitGroup);
		responseDto.setUsmRoles(usmRoles);
		responseDto.setIsPwdLdapAuth(usmUser.getIsPwdLdapAuth());
		responseDto.setStatus(usmUser.getStatus());
		responseDto.setCreationDate(String.valueOf(usmUser.getCreationDate()));
		responseDto.setChangePassword(usmUser.getChangePassword());
		responseDto.setPwdExpDate(String.valueOf(usmUser.getPwdExpDate()));
		responseDto.setPwdExpPrd(String.valueOf(usmUser.getPwdExpPrd()));
		responseDto.setPwdExpPrdNbr(usmUser.getPwdExpPrdNbr());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getUsmMdmBsnUnitGroup());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getUsmRoles());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getIsPwdLdapAuth());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getStatus());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getCreationDate());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getChangePassword());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getPwdExpDate());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getPwdExpPrd());
		System.out.println("RRRRRRRRRRR>>>>>>>>>>"+responseDto.getPwdExpPrdNbr());
		
		return responseDto;
	}

	@Override
	public List<USMStatus> getStatusCombo() {

		return usmUserRepository.getStatusCombo();
	}

	@Override
	public List<USMLanguage> getLanguageCombo() {
		return usmUserRepository.getLanguageCombo();
	}

	@Override
	public List<USMSyslines> getBugTypeCombo() {
		return usmUserRepository.getBugTypeCombo();
	}

	@Override
	public List<USMRefSysParamLines> getCivilStatusCombo() {
		return usmUserRepository.getCivilStatusCombo();
	}

	@Override
	public List<USMCurrency> getCurrencyCombo() {
		return usmUserRepository.getCurrencyCombo();

	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public List<ObjectNode> getBugNameCombo(String id, Integer userId) {

System.out.println("getBugNameComboId >>>>>>>>>>>>> " + id);
		
		String sqlCond = "";
		String whereExists = "";

		if (!id.equals("null")) {
			Integer idParam = Integer.parseInt(id);
			sqlCond = " and s.strucBsnGroupTypeId = " + idParam;
		}

		if (!userId.equals(null) && !userId.equals("")) {
			whereExists = " and not exists(select m from USMUserMulti m where m.bsnGroupId = g.bsnGroupId and m.userId = "
					+ userId + " and m.roleId is not null)";
		}

		return ObjectToJsonRepository.getJson(entityManagerR,
				"select g.bsnGroupId as id ,g.bsnGroupName as name"
						+ " from USMMdmBsnUnitGroup g,USMMdmBsnUnitGroupStruc s,USMSyslines rs"
						+ " where g.bsnGroupId = s.strucBsnGroupId" + "   and rs.id  =  s.strucBsnGroupTypeId"
						+ "    and rs.heaCode   = 6023" + "     and g.bsnGroupId <> 0" + " " + sqlCond + ""

						+ " " + whereExists + "");

	}

	@Override
	public List<USMUserRoles> getRoleCombo() {
		return usmUserRepository.getRoleCombo();
	}

//    @Override
//    public List<USMUserRoles> getDefaultRoleCombo(USMComboDto usmComboDto)  {
//
//        if(usmComboDto !=null)
//        {
//            String[] list = convert(usmComboDto.getId());
//            List<Long> allIds = new ArrayList<>();
//            for (int i = 0; i < list.length; i++) {
//                allIds.add(Long.valueOf(list[i]));
//            }
//                return usmUserRepository.getRoleCombo(allIds);
//        }
//        else
//        {
//            return usmUserRepository.getRoleCombo();
//        }
//
//    }

	public static String[] convert(Set<String> setOfString) {

		// Create String[] of size of setOfString
		String[] arrayOfString = new String[setOfString.size()];

		// Copy elements from set to string array
		// using advanced for loop
		int index = 0;
		for (String str : setOfString)
			arrayOfString[index++] = str;

		// return the formed String[]
		return arrayOfString;
	}

	@Override
	public void insertCaptchaSession(String captchaSerial,String code) {
		Date date=new Date();
		SessionModel sessionModel=new SessionModel();
		
		sessionModel.setSESSION_SERIAL(captchaSerial);
		sessionModel.setSESSION_ATTRIBUTE("loginCaptcha_"+captchaSerial);
		sessionModel.setCREATION_DATE(date);
		sessionModel.setCREATED_BY(-1);
		sessionModel.setSESSION_VALUE(code.getBytes());
		
		sessionRepo.save(sessionModel);
	}
	
	@Override
	public int checkCaptcha(String captchaField,String captchaSerial) {
		

		String captchaAnswer=new String(sessionRepo.getAttributeValue("loginCaptcha_"+captchaSerial));
		System.out.println("captchaField>>>"+captchaField);
		System.out.println("captchaSerial>>>"+captchaSerial);
		System.out.println("captchaAnswer>>>"+captchaAnswer);

		if(captchaField.equals(captchaAnswer)) {
			System.out.println("Success");
			return 1;
		}
		return 0;
	}

	@Override
	public List<ObjectNode> getInDisplayLogs(){
		
		return ObjectToJsonRepository.getJson(entityManagerR,"SELECT a.logId as logId,a.tableName as tableName,a.actionType as actionType,a.logDate as logDate,(SELECT b.empUserLogin as empUserLogin FROM UsmUserMiscInfo b WHERE b.id=a.loggedBy) as loggedBy,a.changes as changes,a.actionText as actionText FROM InDisplayLogsModel a order by a.logDate desc");
	
	}

	@Override
    public String getInDisplayLogsDetails(long logId) {
		System.out.println("LOG IDDD>>>>>>>>>"+logId);
		return inDisplayLogsRepo.fetchLogDetails(logId);
		
	}

}
