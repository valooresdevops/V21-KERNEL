package com.valoores.v21.usm.app.common.mdmbsnunitgroupstruc.model;

import static com.valoores.v21.usm.utils.Schemas.MDMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name = "mdm_bsn_unit_group_struc", schema = MDMDBA)
@Data

public class MdmBsnUnitGroupCode {
	
	@Id
	@Column(name = "BSN_GROUP_ID")
	private Integer id;
	
	@Column(name = "BSN_GROUP_TYPE_CODE")
	private Integer bsnGroupTypeCode;

}
