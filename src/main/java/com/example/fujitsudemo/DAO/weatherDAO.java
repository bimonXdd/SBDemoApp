package com.example.fujitsudemo.DAO;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.Services.WeatherXMLParseService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@RestController
@EnableScheduling
public class weatherDAO {
    @Autowired
    WeatherRepo weatherRepo;

    @Autowired
    WeatherStationsRepo weatherStationsRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public weatherDAO() {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron ="${cronExpression}")
    @GetMapping(path ="/update")
    public String updateDb() throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        for (String station: new String[]{"Pärnu","Tartu-Tõravere","Tallinn-Harku"}){
            WeatherXMLParseService parser = retriveWeatherXML(station);
            WeatherEntity dbInfo = new WeatherEntity(new WeatherStationsEntity(parser.getWMOcode(),station),
                    parser.getAirTemp(),
                    parser.getWindSpeed(),
                    parser.getWPhenomenon(),
                    parser.getTimestamp());


            weatherRepo.save(dbInfo);
            //With a fast for loop, the weatherRepo doest have time to save all, so a small timeout is needed
            TimeUnit.SECONDS.sleep(1);

        }

        return "The Database has been updated! ";
    }
    @PostConstruct
    public void initStations(){
        weatherStationsRepo.save(new WeatherStationsEntity(41803,"Pärnu"));
        weatherStationsRepo.save(new WeatherStationsEntity(26038,"Tallinn-Harku"));
        weatherStationsRepo.save(new WeatherStationsEntity(26242,"Tartu-Tõravere"));
    }
    private WeatherXMLParseService retriveWeatherXML(String Station) throws ParserConfigurationException, IOException, SAXException {

        WeatherXMLParseService parserXML = new WeatherXMLParseService("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752",Station);
        parserXML.getWeatherInfo();

        return parserXML;
    }
    public double getWindSpeed(String weatherStation){
        double windSpeed;

        try {
             windSpeed = jdbcTemplate.queryForObject(String.format("SELECT WIND_SPEED FROM WEATHER_DATA " +
                     "  WHERE WMO = %d ORDER BY TIME_STAMP DESC LIMIT 1", getWMO(weatherStation)), Double.class);

        }catch (NullPointerException e){
            throw new RuntimeException("windSpeed query threw a NullPointer ");
        }
        catch (EmptyResultDataAccessException e){
            throw new RuntimeException("Empty result returned from DB query for Wind speed");
        }
        return windSpeed;
    }

    public double getAirTemp(String weatherStation){
        double airTemp;

        try {
            airTemp = jdbcTemplate.queryForObject(String.format("SELECT AIR_TEMPERATURE FROM WEATHER_DATA " +
                    "  WHERE WMO = %d ORDER BY TIME_STAMP DESC LIMIT 1", getWMO(weatherStation)), Double.class);

        }catch (NullPointerException e){
            throw new RuntimeException("airTemp query threw a NullPointer ");
        }
        catch (EmptyResultDataAccessException e){
            throw new RuntimeException("Empty result returned from DB query for air temperature");
        }
        return airTemp;
    }


        public String getPhenomenon(String weatherStation){
        String phenomenon;
        try {
            phenomenon = jdbcTemplate.queryForObject(String.format("SELECT PHENOMENON FROM WEATHER_DATA " +
                    "  WHERE WMO = %d ORDER BY TIME_STAMP DESC LIMIT 1", getWMO(weatherStation)), String.class);

        }catch (NullPointerException e){
            throw new RuntimeException("No phenomenon at current time");
        }catch (EmptyResultDataAccessException e){
            throw new RuntimeException("Empty result returned from DB query for phenomenon");
        }
        return phenomenon;
    }

    public Long getWMO(String weatherStation){
        long WMOCode;
        try{

            WMOCode = jdbcTemplate.queryForObject(String.format("SELECT WMO FROM STATIONS WHERE STATION_NAME = %s", weatherStation) , Long.class);

        }catch (NullPointerException e){
            throw new RuntimeException("weatherstation WMO query returned NullPointer");
        } catch (EmptyResultDataAccessException e){
            throw new RuntimeException("Empty result returned from DB query for WMO");
        }
        return WMOCode;
    }

}
