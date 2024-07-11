package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.app.queryBuilder.model.QueryDecodeEngineModel;

public interface QueryDecodeEngineRepo extends JpaRepository <QueryDecodeEngineModel, Long>{

}
