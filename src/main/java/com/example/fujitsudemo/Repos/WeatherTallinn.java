package com.example.fujitsudemo.Repos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class WeatherTallinn {
    @Id
    public long timeStamp;

    public long WMO;
    public double airTemp;
    public double windSpeed;
    public String phenomenon;
    public WeatherTallinn(long wmoCode, Double airTemp, Double windSpeed, String Phenomenon, long timestamp) {
        this.WMO = wmoCode;
        this.airTemp = airTemp;
        this.windSpeed = windSpeed;
        this.phenomenon = Phenomenon;
        this.timeStamp = timestamp;
    }


}
