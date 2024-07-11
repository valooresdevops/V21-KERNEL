package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.formBuilder.model.AllObjectsModel;

@Repository
public interface AllObjectsRepo extends JpaRepository<AllObjectsModel , String>{

}