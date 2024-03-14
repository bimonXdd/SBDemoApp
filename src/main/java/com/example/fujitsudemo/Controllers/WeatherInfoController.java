package com.example.fujitsudemo.Controllers;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.DAO.weatherDAO;
import com.example.fujitsudemo.Services.RegionalFeeService;
import com.example.fujitsudemo.Services.WeatherFeeService;
import com.example.fujitsudemo.Services.WeatherXMLParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@RestController
public class WeatherInfoController {

    @Autowired
    WeatherRepo weatherRepo;

    @Autowired
    weatherDAO weatherDAO;

    @Autowired
    RegionalFeeService regionalFeeService;


    @GetMapping(path = "/extraFee")
    public String getExtraFee(@RequestParam("City") String city,
                              @RequestParam("vehicleType") String vehicleType) {

        String stationName = "";


        if (city.equals("Tallinn")) stationName = "'Tallinn-Harku'";

        else if (city.equals("Tartu")) stationName = "'Tartu-Tõravere'";

        else if (city.equals("Pärnu")) stationName = "'Pärnu'";


        double airTemp = weatherDAO.getAirTemp(stationName);
        double windSpeed = weatherDAO.getWindSpeed(stationName);
        String phenomenon = weatherDAO.getPhenomenon(stationName);

        //Gives an error, if it's too windy for a bike ride
        if (vehicleType.equals("Bike") && windSpeed > 20) return "Usage of selected vehicle type is forbidden";

        //Gives an error, if the weather is thunder, hail or glaze
        if (vehicleType.equals("Bike") || vehicleType.equals("Scooter")) {
            if (phenomenon != null) {
                if (phenomenon.equals("thunder") || phenomenon.equals("hail") || phenomenon.equals("glaze")) {
                    return "Usage of selected vehicle type is forbidden";
                }
            }
        }


        WeatherFeeService fee = new WeatherFeeService(vehicleType);
        double ATEF = fee.getATEF(airTemp);
        double WPEF = fee.getWPEF(phenomenon);
        double WSEF = fee.getWSEF(windSpeed);
        double RBF = regionalFeeService.getRBF(vehicleType, city);
        double feeSum = +ATEF + RBF + WPEF + WSEF;

        return "ATEF + RBF + WPEF + WSEF = " + ATEF + " + " + RBF + " + " + WPEF + " + " + WSEF + " = " + feeSum ;
    }

    @GetMapping(path = "/extraFee/error")
    public String errorPage() {
        return "An error has occurred, the Databse might be empty check terminal.";
    }


}
