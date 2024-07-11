package com.valoores.inDisplayApplication.app.formBuilder.dto.QBEParamDto;

import java.util.List;
import lombok.Data;

@Data
public class QbeIdDto {
	private String queryId;
	private List<ParamQbeDto> parameters;
	private List<LinkQbeDto> link;
	private String whereCond;
	private List<HiddenQbeDto> isHidden;

}
