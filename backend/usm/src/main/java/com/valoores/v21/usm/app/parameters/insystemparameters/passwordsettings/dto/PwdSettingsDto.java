package com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PwdSettingsDto {

	private String MinLength ;
	private String minimumNumberOfDigits;
	private String MNOUL;
	private String MNOLL;
	private String MNOSC;
	private String NOPIH;
	private String IPUF;
	private String expirationDate;
	private String Period;
	private String expirationPeriod;

}
