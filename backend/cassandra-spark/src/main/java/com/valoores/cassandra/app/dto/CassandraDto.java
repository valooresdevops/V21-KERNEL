package com.valoores.cassandra.app.dto;

import java.io.Serializable;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CassandraDto implements Serializable{


	private static final long serialVersionUID = 1L;
	private String device_id;
//	private String device_name;

	@Override
	public String toString() {
		
		return (new JSONObject(this)).toString();
		}

}
