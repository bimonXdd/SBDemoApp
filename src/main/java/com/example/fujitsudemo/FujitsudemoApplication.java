package com.example.fujitsudemo;

import com.example.fujitsudemo.Repos.WeatherStationsRepo;
import com.example.fujitsudemo.Services.databaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FujitsudemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FujitsudemoApplication.class, args);
        new databaseService();


    }

}
