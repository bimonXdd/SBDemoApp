package com.example.fujitsudemo.Repos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Weather Data")
public class WeatherEntity {

    @ManyToOne
    @JoinColumn(name = "WMO")
    public WeatherStationsEntity WMO;



    @Column(name = "timeStamp")
    @Id
    public long timeStamp;

    @Column(name = "airTemperature")
    public double airTemp;
    @Column(name = "windSpeed")
    public double windSpeed;
    @Column(name = "Phenomenon")
    public String phenomenon;
    public WeatherEntity(WeatherStationsEntity wmoCode, Double airTemp, Double windSpeed, String Phenomenon, long timestamp) {

        this.WMO = wmoCode;
        this.airTemp = airTemp;
        this.windSpeed = windSpeed;
        this.phenomenon = Phenomenon;
        this.timeStamp = timestamp;
    }



}