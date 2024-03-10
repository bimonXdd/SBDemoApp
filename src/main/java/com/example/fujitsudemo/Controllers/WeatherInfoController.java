package com.example.fujitsudemo.Controllers;

import com.example.fujitsudemo.Repos.WeatherRepoTartu;
import com.example.fujitsudemo.Repos.WeatherTartu;
import com.example.fujitsudemo.XMLParser.WeatherXMLParse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class WeatherInfoController {

    @Autowired
    WeatherRepoTartu weatherRepo;

    @GetMapping(path="/addWind")
    public String sendWind() throws ParserConfigurationException, IOException, SAXException {

        WeatherXMLParse parserTartu = new WeatherXMLParse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php?timestamp=1615381752","Tartu-Tõravere");
        parserTartu.getWeatherInfo();
        WeatherTartu a = new WeatherTartu(parserTartu.getWMOcode(),parserTartu.getAirTemp(),parserTartu.getWindSpeed(),parserTartu.getWPhenomenon(),parserTartu.getTimestamp());

        WeatherTartu windObjectSaved =  weatherRepo.save(a);
        return "sent : ";
    }

}
