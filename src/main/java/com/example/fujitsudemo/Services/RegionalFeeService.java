package com.example.fujitsudemo.Services;

import org.springframework.stereotype.Component;

/**
 * @Class RegionalFeeService
 *
 * @description class made for regional base fee
 *
 */
@Component
public class RegionalFeeService {

    /**
     * @Method getRBF(Regional Base Fee)
     *
     * @description Returns a RBF(double) based on the given param
     *
     * @param city RBF based on city (currently checks Tallinn, Tartu, Pärnu)
     * @param vehicleType RBG based on vehicle type (currently checks: Car, Scooter, Bike)
     *
     * @throws RuntimeException if "The City of vehicleType is not and option/valid."
     *
     */
    public double getRBF(String vehicleType, String city){

        if (city.equals("Tallinn")){
            if (vehicleType.equals("Car")) return 4;
            if (vehicleType.equals("Scooter")) return 3.5;
            if (vehicleType.equals("Bike")) return 3;
        }
        else if (city.equals("Tartu")){
            if (vehicleType.equals("Car")) return 3.5;
            if (vehicleType.equals("Scooter")) return 3;
            if (vehicleType.equals("Bike")) return 2.5;
        }
        else if (city.equals("Pärnu")){
            if (vehicleType.equals("Car")) return 3;
            if (vehicleType.equals("Scooter")) return 2.5;
            if (vehicleType.equals("Bike")) return 2;
        }

        throw new RuntimeException("The City of vehicleType is not and option/valid.");
    }
}
