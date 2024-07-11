package com.valoores.datacrowd.common.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.common.model.mdmServiceProvider;



@Repository
public interface ServiceProviderRepository  extends JpaRepository<mdmServiceProvider, Long> {
	
	
	 
	@Query( "select  t.ServiceProviderInternlCode from mdmServiceProvider t where t.ServiceProviderTypeID is not null")
	public int[] getallproviders();
		
	
	@Query( "select distinct t.ServiceProviderTypeID from mdmServiceProvider t where  t.ServiceProviderID in :providers  and  t.ServiceProviderTypeID is not null")
	public int[] getproviderType(@Param("providers") List<Long> providers);


	/*
	 * @Procedure(name="P_FILL_DATA_FILTERING") public void
	 * callprocedure(@Param("ReportJsonParamId") String
	 * ReportJsonParamId,@Param("UserId") Long UserId);
	 * 
	 */
}
