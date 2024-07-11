package com.valoores.datacrowd.app.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.tech_angular_button;




@Repository
public interface AngularButtonRepository extends JpaRepository<tech_angular_button, Long>{

	 @SuppressWarnings("rawtypes")
	@Query( "select  t.btnname,t.btnicon,t.btnstyle,t.btnfucntion, t.btniconoffline from tech_angular_button t where  (t.flag = 1 and t.moretools = 0) ")
	 public List getButtonName() ;
	 
	 @SuppressWarnings("rawtypes")
	 @Query( "select  t.btnname,t.btnicon,t.btnstyle,t.btnfucntion,t.btniconoffline from tech_angular_button t where  (t.flag = 1 and t.moretools = 1) ") 
	 public List getMoreTools() ;


	
	
	
	
	
	
}
	