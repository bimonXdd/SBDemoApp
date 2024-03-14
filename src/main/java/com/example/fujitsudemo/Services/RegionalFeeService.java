package com.example.fujitsudemo.Services;

import org.springframework.stereotype.Component;

@Component
public class RegionalFeeService {

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
