package com.example.fujitsudemo.DAO;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.Services.WeatherXMLParseService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * @Class weatherDAO
 *
 * @description Piece between db and Services
 * @AutowiredComponents WeatherRepo, WeatherStationsRepo, JdbcTemplate
 *
 */
@Repository
@EnableScheduling
@Data
public class weatherDAO {
    @Autowired
    WeatherRepo weatherRepo;

    @Autowired
    WeatherStationsRepo weatherStationsRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @Method updateDb
     *
     * Updates the DB according to cron and WeatherXMLParseService given values
     * Current Given Values: "Pärnu","Tartu-Tõravere","Tallinn-Harku"
     * --- Might throw exeptions from WeatherXMLParseService --
     *
     * @throws ParserConfigurationException from WeatherXMLParseService
     * @throws IOException from WeatherXMLParseService
     * @throws SAXException from WeatherXMLParseService
     * @throws InterruptedException from WeatherXMLParseService
     *
     * ---
     *
     */

    @Scheduled(cron = "${cronExpression}")
    public String updateDb() throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        for (String station : new String[]{"Pärnu", "Tartu-Tõravere", "Tallinn-Harku"}) {
            WeatherXMLParseService parser = retriveWeatherXML(station);
            WeatherEntity dbInfo = new WeatherEntity(new WeatherStationsEntity(parser.getWMOcode(), station),
                    parser.getAirTemp(),
                    parser.getWindSpeed(),
                    parser.getWPhenomenon(),
                    parser.getTimeStamp());


            weatherRepo.save(dbInfo);
            //With a fast for loop, the weatherRepo doest have time to save all, so a small timeout is needed
            TimeUnit.SECONDS.sleep(1);

        }

        return "The Database has been updated! ";
    }


    /**
     * @Method initStations
     * Method for initiating stations table for DB
     *
     * @description initiates table for stations with:
     * WMOs and stationNames for: "Pärnu","Tartu-Tõravere","Tallinn-Harku"
     */
    @PostConstruct
    public void initStations() {
        weatherStationsRepo.save(new WeatherStationsEntity(41803, "Pärnu"));
        weatherStationsRepo.save(new WeatherStationsEntity(26038, "Tallinn-Harku"));
        weatherStationsRepo.save(new WeatherStationsEntity(26242, "Tartu-Tõravere"));
    }


    /**
     * @Method getWindSpeed
     * Returns latest wind speed from DB
     *
     * @param weatherStation String of weatherStation in the form "'<weatherStation>'"
     * @description querys DB based on weatherStationName for wind speed
     * @throws NullPointerException incase query returns null
     * @throws EmptyResultDataAccessException if no data in db for given WMO
     *
     * @return Latest wind speed from DB
     */

    public double getWindSpeed(String weatherStation) {
        double windSpeed;

        try {
            windSpeed = jdbcTemplate.queryForObject(String.format("SELECT WIND_SPEED FROM WEATHER_DATA " +
                    "  WHERE WMO = %d ORDER BY TIME_STAMP DESC LIMIT 1", getWMO(weatherStation)), Double.class);

        } catch (NullPointerException e) {
            throw new RuntimeException("windSpeed query threw a NullPointer ");
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Empty result returned from DB query for Wind speed");
        }
        return windSpeed;
    }


    /**
     *
     * @Method getAirTemp
     * Returns latest air temperature from DB
     *
     * @param weatherStation String of weatherStation in the form "'<weatherStation>'"
     * @description querys DB based on weatherStationName for air temperature
     *
     * @throws NullPointerException incase query returns null
     * @throws EmptyResultDataAccessException if no data in db for given WMO
     *
     * @return Latest air temperature from DB
     */
    public double getAirTemp(String weatherStation) {
        double airTemp;

        try {
            airTemp = jdbcTemplate.queryForObject(String.format("SELECT AIR_TEMPERATURE FROM WEATHER_DATA " +
                    "  WHERE WMO = %d ORDER BY TIME_STAMP DESC LIMIT 1", getWMO(weatherStation)), Double.class);

        } catch (NullPointerException e) {
            throw new RuntimeException("airTemp query threw a NullPointer ");
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Empty result returned from DB query for air temperature");
        }
        return airTemp;
    }


    /**
     *
     * @Method getPhenomenon
     * Returns latest weather phenomenon from DB
     *
     * @param weatherStation String of weatherStation in the form "'<weatherStation>'"
     *
     * @throws NullPointerException           in case that the database query returns null
     * @throws EmptyResultDataAccessException when no phenomenon info for phenomenon based on WMO
     * @description querys DB based on weatherStationName for weather phenomenon
     *
     * @return Latest weather phenomenon from DB
     */
    public String getPhenomenon(String weatherStation) {
        String phenomenon;
        try {
            phenomenon = jdbcTemplate.queryForObject(String.format("SELECT PHENOMENON FROM WEATHER_DATA " +
                    "  WHERE WMO = %d ORDER BY TIME_STAMP DESC LIMIT 1", getWMO(weatherStation)), String.class);

        } catch (NullPointerException e) {
            throw new RuntimeException("No phenomenon at current time");
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Empty result returned from DB query for phenomenon");
        }
        return phenomenon;
    }

    /**
     *
     * @Method getWMO
     * Returns WMO from DB
     *
     * @param weatherStation String of weatherStation in the form "'<weatherStation>'"
     *
     * @throws NullPointerException           if no WMO given for queryd weather station
     * @throws EmptyResultDataAccessException if given weather station is wrong format or not in DB
     * @description querys DB Stations table for WMO based on station name.
     *
     * @return according WMOcode for weatherStation
     */
    public Long getWMO(String weatherStation) {
        long WMOCode;
        try {
            WMOCode = jdbcTemplate.queryForObject(String.format("SELECT WMO FROM STATIONS WHERE STATION_NAME = %s", weatherStation), Long.class);


        } catch (NullPointerException e) {
            throw new RuntimeException("weatherstation WMO query returned NullPointer");
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Empty result returned from DB query for WMO, weather station might not be in DB");
        }
        return WMOCode;
    }

    //Returns weather data from XMLService based on link
    private WeatherXMLParseService retriveWeatherXML(String Station) throws ParserConfigurationException, IOException, SAXException {

        WeatherXMLParseService parserXML = new WeatherXMLParseService("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752", Station);
        parserXML.getWeatherInfo();

        return parserXML;
    }


}
