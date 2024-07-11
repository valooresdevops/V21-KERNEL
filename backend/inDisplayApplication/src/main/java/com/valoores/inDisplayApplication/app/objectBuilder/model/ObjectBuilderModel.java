package com.valoores.inDisplayApplication.app.objectBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.QBEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QBE_USER_QUERY", schema = QBEDBA)
@Data
public class ObjectBuilderModel {
	 	@Id
	    @Column(name = "QBE_ID")
	    private long QBE_ID;

	    @Column(name = "QUERY_NAME")
	    private String QUERY_NAME;


	    @Column(name = "CREATED_BY")
	    private Date CREATED_BY;
}
