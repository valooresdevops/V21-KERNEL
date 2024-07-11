package com.valoores.datacrowd.app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.valoores.datacrowd.app.model.WebApi;

@Repository
public interface WebApiMethodRepository extends JpaRepository<WebApi, Integer>{

	@Query( "select  t.excutionResult from WebApi t where t.methodlogId = :reportJsonParamId")
	public String getShapeType(int reportJsonParamId);
	
	@Query( "select  t.excutionResult from WebApi t where t.methodlogId = :reportJsonParamId")
	public byte[]  getSimulation(int reportJsonParamId);
}
