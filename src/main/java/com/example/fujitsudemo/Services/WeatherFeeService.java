package com.example.fujitsudemo.Services;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WeatherFeeService {
    double windSpeed;
    String phenomenon;
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

    public double getWSEF() {
        if (vehicleType.equals("Bike")){
            if (windSpeed > 10 && windSpeed <= 20) return 0.5;

            else if (windSpeed > 20){
                //error
            }

        }
        return 0;

    }

    public double getWPEF() {
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")){

            if (checkSnow()) return 1;
            else if (checkRain()) return 0.5;
            else if (phenomenon.equals("glaze") || phenomenon.equals("hail") || phenomenon.equals("thunder")){
                //error
            }
        }
        return 0;
    }
    private boolean checkSnow(){
        String condition = phenomenon.split(" ")[1];

        if (condition.equals("snow") || condition.equals("sleet") || condition.equals("snowfall")){
            return true;
        }
        else return false;
    }
    private boolean checkRain(){
        String condition = phenomenon.split(" ")[1];
        if (condition.equals("rain") || condition.equals("shower")) {
            return true;
        }
        else return false;
    }



}
