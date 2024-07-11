package com.valoores.v21.usm.app.logs.logsbyheatmap.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.logsbyheatmap.service.ILogsByHeatmapService;
import com.valoores.v21.usm.common.ObjectToJsonRepository;

@Service
public class ILogsByHeatmapServiceImpl implements ILogsByHeatmapService {
	
	@Autowired 
	private EntityManager entityManager;
	
	@Override
	public List<ObjectNode> getAllLogsByHeatmap(String loginDate, String logoutDate) {
		
		List<ObjectNode> json = new ArrayList<>();
		String query =  " WITH  \r\n "  + 
				 " T AS \r\n "  + 
				 " (SELECT TRIM(INITCAP(TO_CHAR(TO_DATE('"+ loginDate +"', 'dd/mm/yyyy') + ROW_NUMBER() OVER (ORDER BY 1) - 1, 'day'))) AS line_Name, \r\n "  + 
				 "          TO_CHAR(TO_DATE('"+ loginDate +"', 'dd/mm/yyyy') + ROW_NUMBER() OVER (ORDER BY 1) - 1, 'dd/mm/yyyy') AS dmy, \r\n "  + 
				 "          rownum AS display_Order \r\n "  + 
				 "   FROM usmdba.usm_user_log \r\n "  + 
				 "     WHERE TO_DATE('"+ loginDate +"', 'dd/mm/yyyy') + rownum - 1 <= TO_DATE('"+ logoutDate +"', 'dd/mm/yyyy')) , \r\n "  + 
				 "  \r\n "  + 
				 " TT AS  \r\n "  + 
				 " (SELECT TRIM(l.lin_name) AS line_Name, \r\n "  + 
				 "          l.hea_Code, \r\n "  + 
				 "          l.lin_Code, \r\n "  + 
				 "          rownum AS display_Order \r\n "  + 
				 "   FROM ref_sys_lines l \r\n "  + 
				 "   WHERE l.hea_Code = 2006 \r\n "  + 
				 "     AND l.lin_Code IN (17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41)) \r\n "  + 
				 "   \r\n "  + 
				 "  \r\n "  + 
				 " SELECT t.display_Order - 1 AS x, \r\n "  + 
				 "        tt.display_Order - 1 AS y, \r\n "  + 
				 "        COUNT(v.emp_Id) AS valuess, \r\n "  + 
				 "        t.line_Name AS Days, \r\n "  + 
				 "        t.dmy AS dates, \r\n "  + 
				 "        tt.line_Name AS Times \r\n "  + 
				 " FROM T \r\n "  + 
				 " CROSS JOIN TT \r\n "  + 
				 " LEFT JOIN usmdba.usm_user_log_details ld ON TRIM(INITCAP(TO_CHAR(ld.operation_Date, 'day'))) = t.line_Name \r\n "  + 
				 "                                 AND TRIM(TO_CHAR(ld.operation_Date, 'dd/mm/yyyy')) = t.dmy \r\n "  + 
				 "                                 AND TRIM(CONCAT(TO_CHAR(ld.operation_Date, 'HH24'), ':00')) = tt.line_Name \r\n "  + 
				 "                                 AND ld.operation_Date <> TO_DATE('"+ logoutDate +"', 'dd/mm/yyyy') \r\n "  + 
				 "  \r\n "  + 
				 " LEFT JOIN usmdba.usm_user_log v ON v.log_Id = ld.log_Id \r\n "  + 
				 "  \r\n "  + 
				 " GROUP BY t.display_Order, tt.display_Order, t.line_Name, t.dmy, tt.line_Name \r\n "  + 
				 " ORDER BY t.display_Order, tt.display_Order";
		
		json = ObjectToJsonRepository.getJsonNativeQuery(entityManager, query);
System.out.println( " \n\n\n json >>>>>>>>>>>>>>   "  + json);
        
    	return json;
	}

}
