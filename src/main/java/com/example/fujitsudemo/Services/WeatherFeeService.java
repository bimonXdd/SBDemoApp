package com.example.fujitsudemo.Services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Data
@Service
@RequiredArgsConstructor
public class WeatherFeeService {
    String vehicleType;


    public WeatherFeeService(String vehicleType){
        this.vehicleType = vehicleType;
    }




    public double getATEF(double airTemp) {
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")){

            if (airTemp < -10) return 1;

            else if(airTemp > -10 && airTemp <= 0) return 0.5;

            else return 0;
        }

        else return 0;

    }

    public double getWSEF(double windSpeed) {
        if (vehicleType.equals("Bike")){
            if (windSpeed > 10 && windSpeed <= 20) return 0.5;

            else if (windSpeed > 20){
                //error
            }

        }
        return 0;

    }

    public double getWPEF(String phenomenon) {
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")){
            try{
                if (checkSnow(phenomenon)) return 1;
                else if (checkRain(phenomenon)) return 0.5;
                else if (phenomenon.equals("glaze") || phenomenon.equals("hail") || phenomenon.equals("thunder")){

            }}
            catch (NullPointerException e){
                return 0;
            }
        }
        return 0;
    }
    private boolean checkSnow(String phenomenon){
        String condition = phenomenon.split(" ")[1];

        if (condition.equals("snow") || condition.equals("sleet") || condition.equals("snowfall")){
            return true;
        }
        else return false;
    }
    private boolean checkRain(String phenomenon){
        String condition = phenomenon.split(" ")[1];

        if (condition.equals("rain") || condition.equals("shower")) {
            return true;
        }
        else return false;
    }






}
