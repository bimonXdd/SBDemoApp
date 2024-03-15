package com.example.fujitsudemo.Services;


import lombok.Data;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Class WeatherXMLParseService
 *
 * @description Handles the parsing and xml scraping for the application
 *
 */
@Data
public class WeatherXMLParseService {

    private final String xmlLink;

    private String weatherStation;
    @Getter
    private Double windSpeed;
    @Getter
    private Double airTemp;
    @Getter
    private String WPhenomenon;
    @Getter
    private long timeStamp;
    @Getter
    private long WMOcode;

    public WeatherXMLParseService(String XmlLink) {
        this.xmlLink = XmlLink;

    }

    public WeatherXMLParseService(String XmlLink, String weatherStation) {
        this.xmlLink = XmlLink;
        this.weatherStation = weatherStation;
    }


    //prepares XML document for parsing
    private Document prepareXMLDoc() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new URL(xmlLink).openStream());
    }

    //Return a list of windspeed, airtemp, and phenomenon(if excist) of the specified city

    /**
     * @return ATEF(Air Temperature Extra Fee)
     * @throws ParserConfigurationException in case parser is configured incorrectly
     * @throws IOException                  for input/output with extracting from XML
     * @throws SAXException                 for XMLparser
     * @Method getWeatherInfo
     * @description extracts a list of airTemp, windSpeed and phenomenon(if excists)
     */
    public List<String> getWeatherInfo() throws ParserConfigurationException, IOException, SAXException {

        //loop though all elements of xml file and find the city, then adds eveything to a list and returns it
        List<String> weatherInfo = new ArrayList<>();
        NodeList stations = prepareXMLDoc().getElementsByTagName("station");
        for (int i = 0; i < stations.getLength(); i++) {
            Element station = (Element) stations.item(i);

            if (getStationName(station).equals(weatherStation)) {
                this.WMOcode = Long.parseLong(extractWMOcode(station));

                String windSpeedStr = extractAirTemp(station);
                this.windSpeed = windSpeedStr.isEmpty() ? Double.NaN : Double.parseDouble(windSpeedStr);
                weatherInfo.add(windSpeedStr);

                String airTempStr = extractWindSP(station);
                this.airTemp = airTempStr.isEmpty() ? Double.NaN : Double.parseDouble(airTempStr);
                ;
                weatherInfo.add(airTempStr);

                this.WPhenomenon = extractPhenom(station);
                weatherInfo.add(WPhenomenon);

                this.timeStamp = extractTimestamp();

                return weatherInfo;
            }
        }
        throw new RuntimeException("WeatherStation was not found: " + weatherStation);

    }


    // Returns Timestamp(if exists) based on the link given
    private long extractTimestamp() throws ParserConfigurationException, IOException, SAXException {
        return Long.parseLong(prepareXMLDoc().getDocumentElement().getAttribute("timestamp"));

    }

    private String getStationName(Element station) {
        return station.getElementsByTagName("name").item(0).getTextContent();
    }

    private String extractWindSP(Element station) {
        return station.getElementsByTagName("airtemperature").item(0).getTextContent();
    }

    private String extractAirTemp(Element station) {
        return station.getElementsByTagName("windspeed").item(0).getTextContent();

    }

    private String extractWMOcode(Element station) {
        return station.getElementsByTagName("wmocode").item(0).getTextContent();
    }

    private String extractPhenom(Element station) {
        String phenom = station.getElementsByTagName("phenomenon").item(0).getTextContent();

        //if there is a weather phenomenon
        if (phenom.equals("")) {
            return null;
        } else {
            return phenom;
        }

    }

}

