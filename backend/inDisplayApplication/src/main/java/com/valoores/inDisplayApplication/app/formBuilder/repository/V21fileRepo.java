package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.V21FileModel;

@Repository
public interface V21fileRepo extends JpaRepository <V21FileModel, Long>{
//SQBQuery findById(long id);
//
//	
//	@Modifying
//	@Query("delete from SQBQuery a where a.QBE_ID = :id ")
//	void deleteAllByQueryId(long id);
//
//	@Query("SELECT a.QUERY_NAME FROM SQBQuery a WHERE a.QBE_ID=:queryId")
//	String fetchQueryName(long queryId);
//	
	
//	 @Query(value = "INSERT INTO V21FileModel (tableName,primaryCol,primaryVal,fileCol,fileData) VALUE (:tableName ,:primaryCol ,:primaryVal ,:fileCol ,:fileData)")
//	    void insertIntoTable(String tableName, String primaryCol , String primaryVal, String fileCol ,byte fileData);
	@Transactional
	@Modifying
	@Query("UPDATE V21FileModel A set  A.fileData = :fileData where  A.primaryVal = :primaryVal")
	void updateV21Files(byte [] fileData,String primaryVal);

	
	@Query("SELECT A.fileData FROM V21FileModel A WHERE A.primaryVal = :primaryVal AND tableName like %:tableName% ")
	byte[] getFileData(String primaryVal,String tableName);
	
	@Query("SELECT A.primaryVal FROM V21FileModel A WHERE A.primaryCol = :primaryCol AND tableName =:tableName ")
	String getPrimaryVal(String primaryCol,String tableName);
	
	@Query("SELECT count(1) FROM V21FileModel A WHERE A.primaryCol =:primaryCol AND tableName =:tableName AND A.primaryVal =:priamryVal ")
	int test(String primaryCol,String tableName,String priamryVal);

}



