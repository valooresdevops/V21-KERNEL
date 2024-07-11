package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "TECH_BUTTON", schema = "TECHDBA")
@Getter
@Setter
public class ButtonModel {
	
	@Id
	@Column(name="BUTTON_ID")
	private long id;
	
	@Column(name="BUTTON_NAME")
	private String name;

}
