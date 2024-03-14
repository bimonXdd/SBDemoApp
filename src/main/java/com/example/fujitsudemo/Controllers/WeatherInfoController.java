package com.example.fujitsudemo.Controllers;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.DAO.weatherDAO;
import com.example.fujitsudemo.Services.WeatherFeeService;
import com.example.fujitsudemo.Services.WeatherXMLParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
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



    @GetMapping(path="/addWind")
    public String sendWind() throws ParserConfigurationException, IOException, SAXException {


        WeatherXMLParseService parser = new WeatherXMLParseService("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752","Tartu-Tõravere");
        parser.getWeatherInfo();

       // WeatherEntity a = new WeatherEntity(new WeatherStations(parser.getWMOcode(),parser),parser.getAirTemp(),parser.getWindSpeed(),parser.getWPhenomenon(),parser.getTimestamp(), parser.getWeatherStation());
       // WeatherEntity windObjectSaved =  weatherRepo.save(a);

        return "sent : ";
    }
    @GetMapping(path = "/data")
    public String getweatherInfo(){
        String stationName = "'Tallinn-Harku'";
    try {
        double Wind = weatherDAO.getWindSpeed(stationName);
        String phenomenon = weatherDAO.getPhenomenon(stationName);
        double air =weatherDAO.getAirTemp(stationName);

        return "Air: "+ air   + "Wind: "+ Wind + " Phenomenon: "+ phenomenon;
    }catch (EmptyResultDataAccessException e){
        throw new RuntimeException("DB might be empty, threw a EmptyResultDataAccessException");
    }

    }
    @GetMapping(path = "/extraFee")
    public String getExtraFee(){
        String vehicleType = "Bike";
        String stationName = "'Tallinn-Harku'";
        double Wind = weatherDAO.getWindSpeed(stationName);
        String phenomenon = weatherDAO.getPhenomenon(stationName);
        double air =weatherDAO.getAirTemp(stationName);
        WeatherFeeService fee = new WeatherFeeService(vehicleType);
        double ATEF = fee.getATEF(Wind);
        return "ATEF: " + ATEF;
    }





}
