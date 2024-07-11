package com.valoores.inDisplayApplication.app.formBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.TECHDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "QUERY_FORMS_BUTTONS", schema = TECHDBA)
@Getter
@Setter
public class QueryFormButtonModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_QUERY_FORM_ID")
	@SequenceGenerator(name = "SEQ_QUERY_FORM_ID", schema = "TECHDBA", sequenceName = "SEQ_QUERY_FORM_ID", allocationSize = 1)
	@Column(name = "MAIN_ID")
	private long mainId;
	
	@Column(name = "OBJECT_ID")
	private long objectId;
	
	@Column(name = "BUTTON_LIST")
	private String buttonList;
}
