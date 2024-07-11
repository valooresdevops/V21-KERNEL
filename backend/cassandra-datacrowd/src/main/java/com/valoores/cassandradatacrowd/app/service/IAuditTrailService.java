package com.valoores.cassandradatacrowd.app.service;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;

public interface IAuditTrailService {

	CustomResponse addLoggedUser( String insertQuery);

}
