package com.example.fujitsudemo.Services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Class WeatherFeeService
 *
 * @description Returnsed the required parameters for calculating delivery service fee
 * based on weather and vehicle type
 *
 */
@Data
@Service
@RequiredArgsConstructor
public class WeatherFeeService {
    String vehicleType;

    public WeatherFeeService(String vehicleType){
        this.vehicleType = vehicleType;
    }


    /**
     * @Method getATEF
     *
     * @description returns ATEF(Air Temperature Extra Fee) based on given airTemperature, and vehicle type
     *
     * @param airTemp air temperature gotten from weatherDAO
     *
     * @throws NullPointerException in case windspeed in null
     * @return ATEF(Air Temperature Extra Fee)
     */

    public double getATEF(double airTemp) {

        try{
            if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")){

                if (airTemp < -10) return 1;

                else if(airTemp >= -10 && airTemp <= 0) return 0.5;

                else return 0;
            }
        }catch (NullPointerException e){
            return 0;
        }

        return 0;

    }



    /**
     * @Method getWSEF
     *
     * @description returns WSEF(Wind speed extra fee) based on given windspeed, and vehicle type
     *
     * @param windSpeed wind speed gotten from weatherDAO
     *
     * @throws NullPointerException in case windspeed in null
     *
     * @return WSEF(Wind speed extra fee)
     */
    public double getWSEF(double windSpeed) {
        try{if (vehicleType.equals("Bike")){
            if (windSpeed > 10 && windSpeed <= 20) return 0.5;
        }
        }catch (NullPointerException e){
            return 0;
        }

        return 0;

    }


    /**
     * @Method getWPEF
     *
     * @description returns WPEF(Weather phenomenon extra fee)
     * based on given phenomenon and vehicleType of the class
     *
     * @param phenomenon phenomenon gotten from weatherDAO
     *
     * @throws NullPointerException if query returns null
     *
     * @return WPEF(Weather phenomenon extra fee)
     */
    public double getWPEF(String phenomenon) {
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")){
            try{
                if (checkSnow(phenomenon)) return 1;
                else if (checkRain(phenomenon)) return 0.5;
             }
            catch (NullPointerException e){
                return 0;
            }
        }
        return 0;
    }

    //check for snow realted phenomenons
    private boolean checkSnow(String phenomenon){
        String condition = phenomenon.split(" ")[1];

        if (condition.equals("snow") || condition.equals("sleet") || condition.equals("snowfall")){
            return true;
        }
        else return false;
    }

    //checks for rain related phenomenons
    private boolean checkRain(String phenomenon){
        String condition = phenomenon.split(" ")[1];

        if (condition.equals("rain") || condition.equals("shower")) {
            return true;
        }
        else return false;
    }






}
