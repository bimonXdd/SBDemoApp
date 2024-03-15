package com.example.fujitsudemo;

import com.example.fujitsudemo.Services.RegionalFeeService;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

public class RegionalFeeServiceTest {

   @Autowired
    private RegionalFeeService regionalFeeService;



   //tests for all the valid cases of RBF
    @ParameterizedTest
    @CsvSource({
            "Scooter, Tallinn, 3.5",
            "Scooter, Tartu, 3",
            "Scooter, Pärnu, 2.5",
            "Car, Tallinn, 4",
            "Car, Tartu, 3.5",
            "Car, Pärnu, 3",
            "Bike, Tallinn, 3",
            "Bike, Tartu, 2.5",
            "Bike, Pärnu, 2",
            // Add more parameter sets as needed
    })
    public void testGetRBFValid(String vehicleType, String City,double expectedValue) {
        double result = regionalFeeService.getRBF(vehicleType,City);
        assertEquals(expectedValue, result);
    }

    @ParameterizedTest
    @CsvSource({
            "Scooterz, Tallinn", // non existent vehicle
            "#, Tallinn", //symbol
            "-333, -333", //integers
            "true, Pärnu", //boolean
            "Tallinn, Tallinn", //city city
            ", Tartu", // <empty>, city
            "Tartu, Bike", // reversed
            "null, null", // null
            ","
    })
    public void testGetRBFInvalid(String vehicleType, String City){
        Executable executable = () -> regionalFeeService.getRBF(vehicleType,City);
        assertThrows(RuntimeException.class, executable);
    }
}
