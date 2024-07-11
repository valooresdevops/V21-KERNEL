package com.valoores.datacrowd.utils.objecttojson;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Repository
public class ObjectToJsonRepository {
	
	@Autowired private EntityManager entityManager;
	
	  public static List<ObjectNode>  getJson(EntityManager entityManagerR,String query  )  {
		  
		  List<ObjectNode> json = new ArrayList<>();

	        ObjectMapper mapper = new ObjectMapper();

				  entityManagerR
				  .createQuery(query)
				  .unwrap(org.hibernate.query.Query.class)
				  .setResultTransformer(
				      new ResultTransformer() {
				          @Override
				          public Object transformTuple(
				                  Object[] tuple,
				                  String[] aliases) {
				        	  
				        	     ObjectNode node = mapper.createObjectNode();
				        	     for(int i = 0 ; i < tuple.length ; i++)
				        	     {
				        	    	 
				        	    	 if(tuple[i] != null)
					               	 node.put(aliases[i],tuple[i].toString());
				        	     }

				               	 json.add(node);
				        	  return json;
				          }
				   
				          @Override
				          public List transformList(List tuples) {
				              return tuples;
				          }
				      }
				  )
				  .getResultList();
	    	return json;
	    	
	    }
	  
}