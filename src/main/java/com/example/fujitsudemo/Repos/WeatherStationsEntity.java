package com.example.fujitsudemo.Repos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Class WeatherStationsEntity
 *
 * @description Entity object for db
 *
 */
@Entity
@Table(name = "Stations")
@NoArgsConstructor
@Data
public class WeatherStationsEntity {
    @Id
    private long WMO;

    @Column(name = "stationName")
    private String stationName;

    public WeatherStationsEntity(long WMOCode, String stationName){
        this.WMO=WMOCode;
        this.stationName=stationName;
    }



}
