package com.valoores.inDisplayApplication.app.objectBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_DASHBOARD", schema =SUITEDBA )
@Data
public class DashboardModel {
	@Id
    @Column(name = "DASHBOARD_ID")
    private long DASHBOARD_ID;

	 @Column(name = "EMP_ID")
	    private long EMP_ID;
}
