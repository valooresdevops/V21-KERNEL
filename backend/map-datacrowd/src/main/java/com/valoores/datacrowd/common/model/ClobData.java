package com.valoores.datacrowd.common.model;

import javax.persistence.Lob;



//@Entity
public class ClobData {
    // other columns 

//    @Column(name="Clob", columnDefinition="Clob NOT NULL") 
    @Lob 
    private String Clob;

	public String getClob() {
		return Clob;
	}

	public void setClob(String clob) {
		Clob = clob;
	}

    // setters and getters
}