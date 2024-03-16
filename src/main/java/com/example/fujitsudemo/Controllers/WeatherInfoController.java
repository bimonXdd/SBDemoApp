package com.example.fujitsudemo.Controllers;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.DAO.weatherDAO;
import com.example.fujitsudemo.Services.RegionalFeeService;
import com.example.fujitsudemo.Services.WeatherFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Class WeatherInfoController
 *
 * @description Controller for calculating the fees for the delivery app
 *
 * @AutowiredComponents WeatherRepo,weatherDAO,RegionalFeeService
 *
 */
@RestController
public class WeatherInfoController {

    @Autowired
    WeatherRepo weatherRepo;

    @Autowired
    weatherDAO weatherDAO;

    @Autowired
    RegionalFeeService regionalFeeService;

    /**
     * Returns the fee calculation for food delivery app.
     *
     * @param city  String for the city the user chooses
     * @param vehicleType String for the vehicle the user chooses
     * @return Calculation for fee in form of a String
     *
     */

    @GetMapping(path = "/extraFee")
    public ExtraFee getExtraFee(@RequestParam("City") String city,
                              @RequestParam("vehicleType") String vehicleType) {

        String stationName = "";

        //City to station relation declaration
        if (city.equals("Tallinn")) stationName = "'Tallinn-Harku'";

        else if (city.equals("Tartu")) stationName = "'Tartu-Tõravere'";

        else if (city.equals("Pärnu")) stationName = "'Pärnu'";


        //Gets the latest weather from DAO(Note: should probably be in weatherFeeService, but for now is here)
        double airTemp = weatherDAO.getAirTemp(stationName);
        double windSpeed = weatherDAO.getWindSpeed(stationName);
        String phenomenon = weatherDAO.getPhenomenon(stationName);


        //Gives an error, if it's too windy for a bike ride
        if (vehicleType.equals("Bike") && windSpeed > 20) return new ExtraFee(1,city,vehicleType,null, null, null, null, null,  "Usage of selected vehicle type is forbidden");

        //Gives an error, if the weather is thunder, hail or glaze
        if (vehicleType.equals("Bike") || vehicleType.equals("Scooter")) {
            if (phenomenon != null) {
                if (phenomenon.equals("thunder") || phenomenon.equals("hail") || phenomenon.equals("glaze")) {
                    return new ExtraFee(1,city,vehicleType,null, null, null, null, null,  "Usage of selected vehicle type is forbidden");
                }
            }
        }


        // Get all the required extra fees and calculate it
        WeatherFeeService fee = new WeatherFeeService(vehicleType);

        double ATEF,WPEF,WSEF,RBF,feeSum;

         ATEF = fee.getATEF(airTemp);
         WPEF = fee.getWPEF(phenomenon);
         WSEF = fee.getWSEF(windSpeed);
         RBF = regionalFeeService.getRBF(vehicleType, city);
         feeSum = +ATEF + RBF + WPEF + WSEF;

        ExtraFee extraFee = new ExtraFee(1, city,vehicleType,RBF, WSEF, WPEF, ATEF, feeSum, null);

        return extraFee ;
    }


}
