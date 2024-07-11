package com.valoores.v21.usm.app.logs.fieldhistorylog.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.fieldhistorylog.repository.IFieldHistoryLogRepository;
import com.valoores.v21.usm.app.logs.fieldhistorylog.service.IFieldHistoryLogService;
import com.valoores.v21.usm.common.ObjectToJsonRepository;

@Service

public class FieldHistoryLogServiceImpl implements IFieldHistoryLogService {

	@Autowired
	private EntityManager entityManagerR;
	
	@Resource
	public IFieldHistoryLogRepository fieldHistoryLogRepository;

	@Autowired private 
	javax.sql.DataSource dataSource;
	
	
	@Override
	public List<ObjectNode> getAllFieldHistoryLog( String application, String menu, String user, String field, String fromDate, String toDate) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		System.out.println("in the first of the function ronyyyyyyyyyy >>>>>>>>"+application + "  "+menu+ "  "+user+ "  "+field+ "  "+fromDate+toDate);
		String sql = "SELECT m.menuPath as menu, "  + 
			        " tr.pk_elem_data_value as objectId, "+ 
				     " ml.elem_data_name as field, "+
				     " u.empUserLogin as userr, "+ 
				     " tr.elem_data_old_value as oldvalue, "+
				     " tr.elem_data_new_value as newvalue, "+
					" tr.creation_date as creationDate " + 
				  "      FROM " + 
					"           USMMenu m, " + 
					"           USMApplication sa, " + 
				    "           UsmUserMiscInfo u, " + 
					"           LogsDetails ld, " +
					"           VersioningCommon tr, " +
					"           Versioning v," +
					"           BMBusinessAreaD d, " +
					"           LogUser l, " +
					"           BMBusinessAreaM mb, " +
					"           BMDataTaxonomy ml "+

 					"       WHERE l.logId = ld.logId "+ 
					"       AND l.empId = u.id " + 
					"       AND u.appCode = sa.id " + 
//					"       AND t.menuCode = m.menuCode " + 
					"       AND v.menu_code=m.menuCode " +  
					"       AND tr.data_versioning_id = v.id " +
					"       AND u.id = tr.created_by " +
					"       AND d.business_area_id = mb.business_area_id " +
					"       AND ml.ELEM_DATA_ID = mb.elem_data_id " +
					"       AND d.id = v.col_sequence"+
					"       AND ROWNUM<'100'"
					;
		if(!application.equals("-1")) {
			sql = sql + " AND sa.id="+ application;
		}
		if(!menu.equals("-1")) {
			sql = sql + " AND m.menuCode="+ menu;
		}
		if(!user.equals("-1")) {
			sql = sql + " AND u.id="+ user;
		}
		if(!field.equals("-1")) {
			sql = sql + " AND ml.ELEM_DATA_ID="+ field;
		}
		if(!fromDate.equals("-1")) {
			sql = sql + " AND DATE_TRUNC('day', tr.creation_date) >= to_date('"+ fromDate +"','dd-MM-yyyy')";
		}
		if(!toDate.equals("-1")) {
			sql = sql + " AND DATE_TRUNC('day', tr.creation_date) <= to_date('"+ toDate +"','dd-MM-yyyy')";
		}
		
		System.out.println("sql >>>>>>>>>>>>>  " + sql);
		
		return ObjectToJsonRepository.getJson(entityManagerR,sql);
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}




	@Override
	public List<ObjectNode> getFieldCombo() {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		System.out.println("testing2");
		String sql = "SELECT ml.elem_data_name as name, "+ " ml.ELEM_DATA_ID as id "+
				"      FROM BMDataTaxonomy ml, "+ "BMBusinessAreaM m, " + "BMBusinessAreaD d, "+"Versioning dv "+
				"       WHERE d.business_area_id = m.business_area_id" + 
				"       AND ml.ELEM_DATA_ID = m.elem_data_id" + 
				"       AND d.id = dv.col_sequence" ;
		return ObjectToJsonRepository.getJson(entityManagerR,sql);
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}




	






	

	
		

}
