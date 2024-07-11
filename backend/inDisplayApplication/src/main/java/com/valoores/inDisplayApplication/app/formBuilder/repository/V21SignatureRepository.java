package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.V21SignatureModel;

@Repository
public interface V21SignatureRepository extends JpaRepository <V21SignatureModel, Long> {
	@Query("SELECT A.fileData FROM V21SignatureModel A WHERE A.primaryVal = :primaryVal AND tableName =:tableName ")
	byte[] getFileData(String primaryVal,String tableName);
	@Query("SELECT count(1) FROM V21SignatureModel A WHERE A.primaryCol =:primaryCol AND tableName =:tableName AND A.primaryVal =:priamryVal ")
	int test(String primaryCol,String tableName,String priamryVal);
	@Transactional
	@Modifying
	@Query("UPDATE V21SignatureModel A set  A.fileData = :fileData where  A.primaryVal = :primaryVal")
	void updateV21Signature(byte [] fileData,String primaryVal);

	
}
