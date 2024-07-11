package com.valoores.v21.usm.app.securitymanagement.accessrightsgrid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.securitymanagement.accessrights.model.refTechSessionReports;

@Repository
public interface IUSMAccessgridRepository extends JpaRepository<refTechSessionReports, Long> {
    // Use the table name as a String
    @Modifying
    @Query(value = "INSERT INTO REF_TECH_SESSION_REPORTS (TECH_COLUMN145) VALUES (:column145)", nativeQuery = true)
    void insertTechColumn(@Param("column145") String col145);
    
// // Script to grant access to the table
//    @Modifying
//    @Query(value = "GRANT ALL ON REF_TECH_SESSION_REPORTS TO public", nativeQuery = true)
//    void grantAccessToTable();
}

