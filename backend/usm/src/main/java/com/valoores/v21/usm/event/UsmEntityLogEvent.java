package com.valoores.v21.usm.event;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class UsmEntityLogEvent implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4460227238317253303L;
	private final String entityType;
	private final long entityId;
	private final String actionType;

	
	public UsmEntityLogEvent() {
    	this.entityType = "";
    	this.entityId = -1L;
    	this.actionType = "";
    }

}