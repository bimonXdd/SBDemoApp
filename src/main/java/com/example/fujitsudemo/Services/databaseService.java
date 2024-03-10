package com.example.fujitsudemo.Services;

import com.example.fujitsudemo.Repos.*;
import com.example.fujitsudemo.XMLParser.WeatherXMLParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Service
@RestController
@EnableScheduling
public class databaseService {
    @Autowired
    WeatherRepoTartu weatherRepoTartu;
    @Autowired
    WeatherRepoTallinn weatherRepoTallinn;
    @Autowired
    WeatherRepoParnu weatherRepoParnu;

    @Scheduled(cron ="${cronExpression}")
    @GetMapping(path ="/update")
    public String updateDb() throws ParserConfigurationException, IOException, SAXException {

        WeatherXMLParse tartu = retriveWeather("Tartu-Tõravere");
        WeatherTartu updateTartu = new WeatherTartu(tartu.getWMOcode(),
                tartu.getAirTemp(),
                tartu.getWindSpeed(),
                tartu.getWPhenomenon(),
                tartu.getTimestamp());

        WeatherXMLParse tallinn = retriveWeather("Tallinn-Harku");
        WeatherTallinn updateTallinn = new WeatherTallinn(tallinn.getWMOcode(),
                tallinn.getAirTemp(),
                tallinn.getWindSpeed(),
                tallinn.getWPhenomenon(),
                tallinn.getTimestamp());

        WeatherXMLParse parnu = retriveWeather("Pärnu");
        WeatherParnu updatePärnu = new WeatherParnu(parnu.getWMOcode(),
                parnu.getAirTemp(),
                parnu.getWindSpeed(),
                parnu.getWPhenomenon(),
                parnu.getTimestamp());

        weatherRepoTartu.save(updateTartu);
        weatherRepoTallinn.save(updateTallinn);
        weatherRepoParnu.save(updatePärnu);

        return "The Database has been updated! ";
    }
    public WeatherXMLParse retriveWeather(String Station) throws ParserConfigurationException, IOException, SAXException {

        WeatherXMLParse parserXML = new WeatherXMLParse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752",Station);
        parserXML.getWeatherInfo();
        return parserXML;
    }

}
