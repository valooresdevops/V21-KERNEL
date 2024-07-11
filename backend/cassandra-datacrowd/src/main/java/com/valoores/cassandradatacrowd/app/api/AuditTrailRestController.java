package com.valoores.cassandradatacrowd.app.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;
import com.valoores.cassandradatacrowd.app.service.IAuditTrailService;

/**
 * @author marcelino.a
 */

@RestController
@RequestMapping("/api")
public class AuditTrailRestController {

  @Autowired
  private IAuditTrailService auditTrailService;

  String attributes = "";
  String attributesValue = "";
  String tableName = "";
  String insertQuery = "";
  String attributeType = "";

  @PostMapping("/auditTrail")
  public CustomResponse addLoggedUser(@RequestBody String params, HttpServletRequest request) {

    CustomResponse resp = CustomResponse.builder().build();
    JSONObject obj = new JSONObject(params);
    
    try {
    	if(obj.has("userName"))
    	{   
    		System.out.println(" userName = "+obj.getString("userName"));
    		request.getSession().setAttribute("userName", obj.getString("userName"));
    	}

    	tableName = obj.getString("tableName");

      if (!tableName.equals("")) {
    	  
        obj.getJSONObject("attributes").keys().forEachRemaining(k -> {

          if (attributes.equals("")) {
            attributes += k;
          } else {
            attributes += "," + k;
          }

          attributeType = obj.getJSONObject("attributes").get(k).getClass().getSimpleName();

	          if (attributesValue.equals("")) {
	        	  
	              if(attributeType.equals("String")) {
	        	  
	            	  System.out.println(" data 00 >> "+isValidDate(obj.getJSONObject("attributes").getString(k)));
	            	  if(isValidDate(obj.getJSONObject("attributes").getString(k)))
	            	  {
	            		  String Date1 = obj.getJSONObject("attributes").getString(k);
		                  @SuppressWarnings("deprecation")
                      Date Date2 = new Date(Date1);
		                  long datemillis = Date2.getTime();
		            	  attributesValue +=datemillis;

	            	  }
	            	  else
	            	  {
		            	  attributesValue +="'"+ obj.getJSONObject("attributes").getString(k)+"'";
	            	  }
	              }
	              else if(attributeType.equals("Integer"))
	              {
	            	attributesValue += obj.getJSONObject("attributes").getInt(k);

	              }
	            
	          } else {
	        	  
	              if(attributeType.equals("String")) {
	            	  
	            	  if(isValidDate(obj.getJSONObject("attributes").getString(k)))
	            	  {
	            		  String Date1 = obj.getJSONObject("attributes").getString(k);
		                  @SuppressWarnings("deprecation")
                      Date Date2 = new Date(Date1);
		                  long datemillis = Date2.getTime();
		            	  attributesValue += ", " + datemillis;

	            	  }
	            	  else
	            	  {
	            		  attributesValue += ", '" + obj.getJSONObject("attributes").getString(k).toString()+"'";	            		  
	            	  }

	              }
	              else if(attributeType.equals("Integer"))
	              {
	            	  attributesValue += "," + obj.getJSONObject("attributes").getInt(k);
	              }
	          }
          
          

        });

        insertQuery = "insert into " + tableName + "(" + attributes + ")values(" + attributesValue + ")";
        tableName = "";
        attributes = "";
        attributesValue="";
        auditTrailService.addLoggedUser(insertQuery);
        
        resp.setStatus("success");
      }

    } catch (Exception e) {
    	
      tableName = "";
      attributes = "";
      attributesValue = "";
      resp.setStatus("failed");
      e.printStackTrace();
    }

    return resp;
  }
  
  public static boolean isValidDate(String inDate) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
      dateFormat.setLenient(false);
      try {
          dateFormat.parse(inDate.trim());
      } catch (Exception pe) {
          return false;
      }
      return true;
  }

}