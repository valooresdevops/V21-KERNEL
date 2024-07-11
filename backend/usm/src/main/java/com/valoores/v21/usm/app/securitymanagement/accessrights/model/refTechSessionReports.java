package com.valoores.v21.usm.app.securitymanagement.accessrights.model;
import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "REF_TECH_SESSION_REPORTS", schema = SDEDBA)
@Data
@NoArgsConstructor
public class refTechSessionReports {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
	
	@Column(name = "TECH_COLUMN145")
	private String col145;
}
