package com.valoores.v21.usm.app.common.mdmbsnunitgroup.model;

import static com.valoores.v21.usm.utils.Schemas.MDMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mdm_bsn_unit_group", schema = MDMDBA)
@Getter
@Setter
@NoArgsConstructor
public class MdmBsnUnitGroup {
	@Id
	@Column(name = "BSN_GROUP_ID")
	@Hidden
	private long bsnGroupId;

	@Column(name = "BSN_GROUP_NAME")
	private String bsnGroupName;

	@Column(name = "CREATION_DATE")
	@Hidden
	@JsonIgnore
	private Date creationDate;

	@Column(name = "CREATED_BY")
	@Hidden
	@JsonIgnore
	private String createdBy;

	@Column(name = "UPDATE_DATE")
	@Hidden
	@JsonIgnore
	private Date updateDate;

	@Column(name = "UPDATED_BY")
	@Hidden
	@JsonIgnore
	private String updatedBy;
}
