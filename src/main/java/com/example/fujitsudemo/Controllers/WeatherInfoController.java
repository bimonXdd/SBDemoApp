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
import java.util.List;


@RestController
public class WeatherInfoController {

    @Autowired
    WeatherRepo weatherRepo;

    @Autowired
    weatherDAO weatherDAO;

    @Autowired
    RegionalFeeService regionalFeeService;


    @GetMapping(path = "/addWind")
    public String sendWind() throws ParserConfigurationException, IOException, SAXException {


        WeatherXMLParseService parser = new WeatherXMLParseService("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752", "Tartu-Tõravere");
        parser.getWeatherInfo();

        // WeatherEntity a = new WeatherEntity(new WeatherStations(parser.getWMOcode(),parser),parser.getAirTemp(),parser.getWindSpeed(),parser.getWPhenomenon(),parser.getTimestamp(), parser.getWeatherStation());
        // WeatherEntity windObjectSaved =  weatherRepo.save(a);

        return "sent : ";
    }

    @GetMapping(path = "/data")
    public String getweatherInfo() {
        String stationName = "'Tallinn-Harku'";
        try {
            double Wind = weatherDAO.getWindSpeed(stationName);
            String phenomenon = weatherDAO.getPhenomenon(stationName);
            double air = weatherDAO.getAirTemp(stationName);

            return "Air: " + air + "Wind: " + Wind + " Phenomenon: " + phenomenon;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("DB might be empty, threw a EmptyResultDataAccessException");
        }

    }

    @GetMapping(path = "/extraFee")
    public String getExtraFee(@RequestParam("City")String city,
                              @RequestParam("vehicleType") String vehicleType){
        String stationName = "";

        if (city.equals("Tallinn")) {
            stationName = "'Tallinn-Harku'";
        } else if (city.equals("Tartu")) {
            stationName = "'Tartu-Tõravere'";
        } else if (city.equals("Pärnu")) {
            stationName = "'Pärnu'";
        }

        double windSpeed = weatherDAO.getWindSpeed(stationName);
        if (vehicleType.equals("Bike") && windSpeed > 20) return "Usage of selected vehicle type is forbidden";

        String phenomenon = weatherDAO.getPhenomenon(stationName);
        if (vehicleType.equals("Bike") || vehicleType.equals("Scooter")) {
            if (phenomenon.equals("thunder") || phenomenon.equals("hail") || phenomenon.equals("glaze")) {
                return "Usage of selected vehicle type is forbidden";
            }
        }

        double airTemp = weatherDAO.getAirTemp(stationName);
        WeatherFeeService fee = new WeatherFeeService(vehicleType);
        double ATEF = fee.getATEF(windSpeed);
        return "ATEF: " + ATEF + " RBF: " + regionalFeeService.getRBF(vehicleType, city);
    }

    @GetMapping(path = "/error")
    public String errorPage(){
        return "An error has occurred, the Databse might be empty check terminal.";
    }


}
