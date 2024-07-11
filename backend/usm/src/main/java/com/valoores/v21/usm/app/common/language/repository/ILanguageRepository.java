package com.valoores.v21.usm.app.common.language.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.language.model.Language;

public interface ILanguageRepository extends JpaRepository  <Language, Long> {
	
	@Query("select a from Language a")
	 List<Language> getLanguage();

}
