package com.example.fujitsudemo.Services;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.XMLParser.WeatherXMLParse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RestController
@EnableScheduling
public class databaseService {
    @Autowired
    WeatherRepo weatherRepo;

    @Autowired
    WeatherStationsRepo weatherStationsRepo;

    @Scheduled(cron ="${cronExpression}")
    @GetMapping(path ="/update")
    public String updateDb() throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        for (String station: new String[]{"Pärnu","Tartu-Tõravere","Tallinn-Harku"}){
            WeatherXMLParse parser = retriveWeather(station);
            WeatherEntity dbInfo = new WeatherEntity(new WeatherStationsEntity(parser.getWMOcode(),station),
                    parser.getAirTemp(),
                    parser.getWindSpeed(),
                    parser.getWPhenomenon(),
                    parser.getTimestamp());

            //With a fast for loop, the weatherRepo doest have time to save all, so a small timeout is needed
            TimeUnit.SECONDS.sleep(1);
            weatherRepo.save(dbInfo);
        }

        return "The Database has been updated! ";
    }
    @PostConstruct
    public void initStations(){
        weatherStationsRepo.save(new WeatherStationsEntity(41803,"Pärnu"));
        weatherStationsRepo.save(new WeatherStationsEntity(26038,"Tallinn-Harku"));
        weatherStationsRepo.save(new WeatherStationsEntity(26242,"Tartu-Tõravere"));
    }
    public WeatherXMLParse retriveWeather(String Station) throws ParserConfigurationException, IOException, SAXException {

        WeatherXMLParse parserXML = new WeatherXMLParse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752",Station);
        parserXML.getWeatherInfo();
        return parserXML;
    }

}
