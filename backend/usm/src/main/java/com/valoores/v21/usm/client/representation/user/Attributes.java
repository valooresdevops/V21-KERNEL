package com.valoores.v21.usm.client.representation.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attributes {
	
	@JsonProperty("LDAP_ENTRY_DN")
	private List<String> ldapEntryDn;
	@JsonProperty("LDAP_ID")
	private List<String> ldapId;
	@JsonProperty
	private List<String> modifyTimestamp;
	@JsonProperty
	private List<String> createTimestamp;

}
