package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usm_suite_ldap_bsngp_synchro", schema = USMDBA)
@Getter
@Setter
@NoArgsConstructor
public class BugMapping {
	@Id
	@Column(name = "SUITE_LDAP_BSNGP_ID")
	private long suiteLdapBsngpId;

	@Column(name = "BSN_GROUP_ID")
	private long bsnGroupId;

	@Column(name = "LDAP_CONFIG_ID")
	private long ldapConfigId;

	@Column(name = "LDAP_ROLE_LINKED_TO_TYPE")
	private long ldapRoleLinkedToType;

	@Column(name = "LDAP_ROLE_LINKED_TO_DESC")
	private String ldapRoleLinkedToDesc;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "CREATED_BY")
	private long createdBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "UPDATED_BY")
	private long updatedBy;

}
