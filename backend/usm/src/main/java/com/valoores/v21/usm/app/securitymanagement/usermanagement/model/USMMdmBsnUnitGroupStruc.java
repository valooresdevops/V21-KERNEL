package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.MDMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "mdm_bsn_unit_group_struc", schema = MDMDBA)
@Data
@NoArgsConstructor
public class USMMdmBsnUnitGroupStruc {


	@Id
	@Column(name = "BSN_GROUP_ID")
	@Hidden
	private long strucBsnGroupId;
	
	@Column(name = "STRUC_DATA_ID")
	private Integer strucDataId;
	
	@Column(name = "BSN_GROUP_TYPE_CODE")
	private Integer strucBsnGroupTypeId;

    @OneToOne
    @PrimaryKeyJoinColumn
    private USMMdmBsnUnitGroup usmMdmBsnUnitGroup;
	
	@Column(name = "CREATION_DATE")
	@Hidden
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private String createdBy;
	
	@Column(name = "UPDATE_DATE")
	@Hidden
	private Date updateDate;
	
	@Column(name = "UPDATED_BY")
	@Hidden
	private String updatedBy;
	
}
