package com.valoores.datacrowd.backend;




import com.valoores.datacrowd.utils.StringUtils;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Hidden
public class CustomResponse {
	
	private String code;
	private String status;
	private String description;
	private String userId;
	private long id;
	
	@Override
	public String toString() {
		return StringUtils.toJsonString(this);
	}
}
