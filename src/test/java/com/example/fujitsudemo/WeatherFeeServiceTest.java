package com.example.fujitsudemo;

import com.example.fujitsudemo.Services.WeatherFeeService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WeatherFeeServiceTest {

    @ParameterizedTest
    @CsvSource({
            "10, 0", // pos aiTemp edgeCase
            "15, 0", // pos airTemp warm
            "0, 0.5", // negTemp edgeCase1
            "-10, 0.5", // negTemp edgeCase2
            "-12, 1", // negTemp rly cold

    })
    public void getATEFTest(double airTemp, double expectedResult){
        WeatherFeeService bikeTest = new WeatherFeeService("Bike");
        WeatherFeeService scooterTest = new WeatherFeeService("Scooter");
        WeatherFeeService carTest = new WeatherFeeService("Car");

        double bikeResult = bikeTest.getATEF(airTemp);
        assertEquals(bikeResult,expectedResult);

        double scooterResult = scooterTest.getATEF(airTemp);
        assertEquals(scooterResult,expectedResult);

        //doesnt effect car
        double carResult = carTest.getATEF(airTemp);
        assertEquals(carResult,0);


    }

    @ParameterizedTest
    @CsvSource({
            "10, 0.5", //edge1
            "20, 0,5", //edge2
            "13, 0.5",
            "0, 0",
            "-13, 0", // impossible
            "44, 0", // error, but should be given in controller atm so 0

    })
    public void getWSEFTest(double windSpeed, double expectedResult){
        WeatherFeeService bikeTest = new WeatherFeeService("Bike");
        WeatherFeeService scooterTest = new WeatherFeeService("Scooter");
        WeatherFeeService carTest = new WeatherFeeService("Car");

        double bikeResult = bikeTest.getATEF(windSpeed);
        assertEquals(bikeResult,expectedResult);

        double scooterResult = scooterTest.getATEF(windSpeed);
        assertEquals(scooterResult,0);

        //doesnt effect car
        double carResult = carTest.getATEF(windSpeed);
        assertEquals(carResult,0);


    }

}
