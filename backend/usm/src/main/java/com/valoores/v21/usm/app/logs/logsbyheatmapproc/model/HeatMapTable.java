package com.valoores.v21.usm.app.logs.logsbyheatmapproc.model;
import static com.valoores.v21.usm.utils.Schemas.SSDX_TMP;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "HeatMapTable", schema = SSDX_TMP)
@Getter
@Setter

public class HeatMapTable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
    @Column(name="x")
    private int x;

    @Column(name= "y")
    private int y;

    @Column(name= "valuess")
    private int values;

    @Column(name= "days")
    private String days;

    @Column(name= "dates")
    private String dates;

    @Column(name= "times")
    private String times;

}
