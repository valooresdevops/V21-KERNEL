package com.valoores.v21.usm.client.representation.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Access {
	
	@JsonProperty
	private boolean manageGroupMembership;
	@JsonProperty
	private boolean view;
	@JsonProperty
	private boolean mapRoles;
	@JsonProperty
	private boolean impersonate;
	@JsonProperty
	private boolean manage;

}
